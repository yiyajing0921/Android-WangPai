/*
 * Copyright 2014-2017 Eduard Ereza Martínez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.woaiwangpai.iwb.constant.crash;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.Date;
import java.util.Deque;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 崩溃异常捕捉
 */
public final class CustomActivityOnCrash {

    private final static String TAG = "CustomActivityOnCrash";

    //Extras passed to the error activity传递给错误活动的附加项
    private static final String EXTRA_CONFIG = "com.woaiwangpai.iwb.crash.EXTRA_CONFIG";
    private static final String EXTRA_STACK_TRACE = "com.woaiwangpai.iwb.crash.EXTRA_STACK_TRACE";
    private static final String EXTRA_ACTIVITY_LOG = "com.woaiwangpai.iwb.crash.EXTRA_ACTIVITY_LOG";

    //General constants 普通常量
    private static final String INTENT_ACTION_ERROR_ACTIVITY = "com.woaiwangpai.iwb.crash.ERROR";
    private static final String INTENT_ACTION_RESTART_ACTIVITY = "com.woaiwangpai.iwb.crash.RESTART";
    private static final String CAOC_HANDLER_PACKAGE_NAME = "cat.ereza.customactivityoncrash";
    private static final String DEFAULT_HANDLER_PACKAGE_NAME = "com.android.internal.os";
    private static final int MAX_STACK_TRACE_SIZE = 131071; //128 KB - 1
    private static final int MAX_ACTIVITIES_IN_LOG = 50;

    //Shared preferences数据存储
    private static final String SHARED_PREFERENCES_FILE = "custom_activity_on_crash";
    private static final String SHARED_PREFERENCES_FIELD_TIMESTAMP = "last_crash_timestamp";

    //Internal variables内变量
    @SuppressLint("StaticFieldLeak") //这是一个应用程序范围的组件
    private static Application application;
    private static CaocConfig config = new CaocConfig();
    private static final Deque<String> activityLog = new ArrayDeque<>(MAX_ACTIVITIES_IN_LOG);
    private static WeakReference<Activity> lastActivityCreated = new WeakReference<>(null);
    private static boolean isInBackground = true;


    /**
     * 使用默认错误活动在应用程序上安装CustomActivityOnCrash。
     *
     * @param context 用于获取ApplicationContext的上下文。不能为空。
     */
    @RestrictTo(RestrictTo.Scope.LIBRARY)
    public static void install(@Nullable final Context context) {
        try {
            if (context == null) {
                Log.e(TAG, "Install failed: context is null!");
            } else {
                //INSTALL!
                final Thread.UncaughtExceptionHandler oldHandler = Thread.getDefaultUncaughtExceptionHandler();

                if (oldHandler != null && oldHandler.getClass().getName().startsWith(CAOC_HANDLER_PACKAGE_NAME)) {
                    Log.e(TAG, "CustomActivityOnCrash已经安装，什么也不做!");
                } else {
                    if (oldHandler != null && !oldHandler.getClass().getName().startsWith(DEFAULT_HANDLER_PACKAGE_NAME)) {
                        Log.e(TAG, "重要警告!您已经有了一个UncaughtExceptionHandler，您确定这是正确的吗?如果使用自定义UncaughtExceptionHandler，则必须在CustomActivityOnCrash之后初始化它!无论如何正在安装，但是不会调用原始处理程序。");
                    }

                    application = (Application) context.getApplicationContext();

                    //We define a default exception handler that does what we want so it can be called from Crashlytics/ACRA
                    Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                        @Override
                        public void uncaughtException(Thread thread, final Throwable throwable) {
                            if (config.isEnabled()) {
                                //执行CustomActivityOnCrash的UncaughtExceptionHandler
                                Log.e(TAG, "应用程序崩溃:-----------------------------------------", throwable);

                                if (hasCrashedInTheLastSeconds(application)) {
                                    Log.e(TAG, "应用程序最近已经崩溃，没有启动自定义错误活动，因为我们可以进入重启循环。你确定你的应用程序不会直接在init上崩溃吗?", throwable);
                                    if (oldHandler != null) {
                                        oldHandler.uncaughtException(thread, throwable);
                                        return;
                                    }
                                } else {
//                                    setLastCrashTimestamp(application, new Date().getTime());
                                    setLastCrashTimestamp(application, System.currentTimeMillis());

                                    Class<? extends Activity> errorActivityClass = config.getErrorActivityClass();

                                    if (errorActivityClass == null) {
                                        errorActivityClass = guessErrorActivityClass(application);
                                    }

                                    if (isStackTraceLikelyConflictive(throwable, errorActivityClass)) {
                                        Log.e(TAG, "您的应用程序类或错误活动已崩溃，自定义活动将不会启动!");
                                        if (oldHandler != null) {
                                            oldHandler.uncaughtException(thread, throwable);
                                            return;
                                        }
                                    } else if (config.getBackgroundMode() == CaocConfig.BACKGROUND_MODE_SHOW_CUSTOM || !isInBackground) {

                                        final Intent intent = new Intent(application, errorActivityClass);
                                        StringWriter sw = new StringWriter();
                                        PrintWriter pw = new PrintWriter(sw);
                                        throwable.printStackTrace(pw);
                                        String stackTraceString = sw.toString();
//将数据减少到128KB，这样在发送意图时就不会得到TransactionTooLargeException异常。
// Android的内存上限是1MB，但一些设备的内存似乎更低。
                                        if (stackTraceString.length() > MAX_STACK_TRACE_SIZE) {
                                            String disclaimer = " [stack trace too large]";
                                            stackTraceString = stackTraceString.substring(0, MAX_STACK_TRACE_SIZE - disclaimer.length()) + disclaimer;
                                        }
                                        intent.putExtra(EXTRA_STACK_TRACE, stackTraceString);

                                        if (config.isTrackActivities()) {
                                            StringBuilder activityLogStringBuilder = new StringBuilder();
                                            while (!activityLog.isEmpty()) {
                                                activityLogStringBuilder.append(activityLog.poll());
                                            }
                                            intent.putExtra(EXTRA_ACTIVITY_LOG, activityLogStringBuilder.toString());
                                        }

                                        if (config.isShowRestartButton() && config.getRestartActivityClass() == null) {
//我们可以设置restartActivityClass，因为应用程序将立即终止，
//和重新启动时，默认值将再次为空。
                                            config.setRestartActivityClass(guessRestartActivityClass(application));
                                        }

                                        intent.putExtra(EXTRA_CONFIG, config);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        if (config.getEventListener() != null) {
                                            config.getEventListener().onLaunchErrorActivity();
                                        }
                                        application.startActivity(intent);
                                    } else if (config.getBackgroundMode() == CaocConfig.BACKGROUND_MODE_CRASH) {
                                        if (oldHandler != null) {
                                            oldHandler.uncaughtException(thread, throwable);
                                            return;
                                        }
                                        //如果它是空的(不应该是空的)，我们让它继续并终止进程，否则它将被卡住
                                    }
                                    //Else (BACKGROUND_MODE_SILENT):什么也不做，让下面的代码终止进程
                                }
                                final Activity lastActivity = lastActivityCreated.get();
                                if (lastActivity != null) {
                                    //我们完成了这个活动，这就解决了一个导致无限递归的bug。
                                    lastActivity.finish();
                                    lastActivityCreated.clear();
                                }
                                killCurrentProcess();
                            } else if (oldHandler != null) {
                                oldHandler.uncaughtException(thread, throwable);
                            }
                        }
                    });
                    application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
                        int currentlyStartedActivities = 0;
                        final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

                        @Override
                        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                            if (activity.getClass() != config.getErrorActivityClass()) {
//复制自ACRA:
//忽略activityClass，因为我们想要最后一个
//启动应用程序活动，以便我们可以
//明确地把它关掉。
                                lastActivityCreated = new WeakReference<>(activity);
                            }
                            if (config.isTrackActivities()) {
                                activityLog.add(dateFormat.format(new Date()) + ": " + activity.getClass().getSimpleName() + " created\n");
                            }
                        }

                        @Override
                        public void onActivityStarted(Activity activity) {
                            currentlyStartedActivities++;
                            isInBackground = (currentlyStartedActivities == 0);
                            //Do nothing
                        }

                        @Override
                        public void onActivityResumed(Activity activity) {
                            if (config.isTrackActivities()) {
                                activityLog.add(dateFormat.format(new Date()) + ": " + activity.getClass().getSimpleName() + " resumed\n");
                            }
                        }

                        @Override
                        public void onActivityPaused(Activity activity) {
                            if (config.isTrackActivities()) {
                                activityLog.add(dateFormat.format(new Date()) + ": " + activity.getClass().getSimpleName() + " paused\n");
                            }
                        }

                        @Override
                        public void onActivityStopped(Activity activity) {
                            //Do nothing
                            currentlyStartedActivities--;
                            isInBackground = (currentlyStartedActivities == 0);
                        }

                        @Override
                        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                            //Do nothing
                        }

                        @Override
                        public void onActivityDestroyed(Activity activity) {
                            if (config.isTrackActivities()) {
                                activityLog.add(dateFormat.format(new Date()) + ": " + activity.getClass().getSimpleName() + " destroyed\n");
                            }
                        }
                    });
                }

                Log.i(TAG, "CustomActivityOnCrash has been installed.");
            }
        } catch (Throwable t) {
            Log.e(TAG, "installing CustomActivityOnCrash时发生未知错误，可能未正确初始化。如果需要，请将此报告为bug。", t);
        }
    }

    /**
     * Given an Intent, 从其中额外返回堆栈跟踪。
     *
     * @param intent The Intent. Must not be null.
     * @return stacktrace，如果没有提供，则为null。
     */
    @Nullable
    public static String getStackTraceFromIntent(@NonNull Intent intent) {
        return intent.getStringExtra(CustomActivityOnCrash.EXTRA_STACK_TRACE);
    }

    /**
     * Given an Intent, 从其中返回额外的配置。
     *
     * @param intent The Intent. Must not be null.
     * @return 配置，如果没有提供，则为null。
     */
    @Nullable
    public static CaocConfig getConfigFromIntent(@NonNull Intent intent) {
        CaocConfig config = (CaocConfig) intent.getSerializableExtra(CustomActivityOnCrash.EXTRA_CONFIG);
        if (config.isLogErrorOnRestart()) {
            String stackTrace = getStackTraceFromIntent(intent);
            if (stackTrace != null) {
               //之前的应用程序进程崩溃。这是崩溃的堆栈跟踪
                Log.e(TAG, "之前的应用程序进程崩溃。这是崩溃的堆栈跟踪-------------------------------------------------------------:\n" + getStackTraceFromIntent(intent));
            }
        }

        return config;
    }

    /**
     * Given an Intent, 从其中返回额外的活动日志。
     *
     * @param intent The Intent. 不能为空
     * @return 活动日志，如果没有提供，则为null。
     */
    @Nullable
    public static String getActivityLogFromIntent(@NonNull Intent intent) {
        return intent.getStringExtra(CustomActivityOnCrash.EXTRA_ACTIVITY_LOG);
    }

    /**
     * Given an Intent, 返回多个错误细节，包括来自意图的额外堆栈跟踪。
     *
     * @param context 一个有效的上下文。不能为空。
     * @param intent  The Intent. 不能为空.
     * @return 完整的错误详情。
     */
    @NonNull
    public static String getAllErrorDetailsFromIntent(@NonNull Context context, @NonNull Intent intent) {
        //我不认为这需要本地化，因为它是一个开发字符串……

        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

        //Get build date
        String buildDateAsString = getBuildDateAsString(context, dateFormat);

        //Get app version
        String versionName = getVersionName(context);

        String errorDetails = "";

        errorDetails += "Build version: " + versionName + " \n";
        if (buildDateAsString != null) {
            errorDetails += "Build date: " + buildDateAsString + " \n";
        }
        errorDetails += "Current date: " + dateFormat.format(currentDate) + " \n";
//在换行之间添加空格来修正#18。
//理想情况下，我们根本不应该使用这种方法……之所以采用这种格式，是因为它与默认的错误活动耦合。
//我们应该将它移动到一个返回bean的方法，并让任何人按照自己的意愿格式化它。
        errorDetails += "Device: " + getDeviceModelName() + " \n \n";
        errorDetails += "Stack trace:  \n";
        errorDetails += getStackTraceFromIntent(intent);

        String activityLog = getActivityLogFromIntent(intent);

        if (activityLog != null) {
            errorDetails += "\nUser actions: \n";
            errorDetails += activityLog;
        }
        return errorDetails;
    }

    /**
     *给定一个意图，重启应用程序并针对该意图启动startActivity。
     *如果意图没有NEW_TASK和CLEAR_TASK标志，则设置它们以确保
     *应用程序堆栈被完全清除。
     *如果提供了事件监听器，则调用restart app事件。
     *只能在您的错误活动中使用。
     *
     * @param activity 当前的错误 activity.不能为空。
     * @param intent   The Intent. 不能为空。
     * @param config   通过调用getConfigFromIntent获得的配置对象。
     */
    public static void restartApplicationWithIntent(@NonNull Activity activity, @NonNull Intent intent, @NonNull CaocConfig config) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        if (intent.getComponent() != null) {
//如果设置了类名，我们将强制它模拟启动器启动。
//如果我们不这样做，如果你从错误活动重新启动，然后按home，
//然后从启动程序启动活动，主活动在backstack上出现两次。
//这很可能不会产生任何有害影响，因为如果您设置了Intent组件，
//if将始终启动，而不管这里指定的操作是什么。
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
        }
        if (config.getEventListener() != null) {
            config.getEventListener().onRestartAppFromErrorActivity();
        }
        activity.finish();
        activity.startActivity(intent);
        killCurrentProcess();
    }

    public static void restartApplication(@NonNull Activity activity, @NonNull CaocConfig config) {
        Intent intent = new Intent(activity, config.getRestartActivityClass());
        restartApplicationWithIntent(activity, intent, config);
    }

    /**
     *关闭应用程序。
     *如果提供了事件监听器，则调用关闭应用程序事件。
     *只能在您的错误活动中使用。
     *
     * @param activity 当前错误活动。不能为空。
     * @param config   通过调用getConfigFromIntent获得的配置对象。
     */
    public static void closeApplication(@NonNull Activity activity, @NonNull CaocConfig config) {
        if (config.getEventListener() != null) {
            config.getEventListener().onCloseAppFromErrorActivity();
        }
        activity.finish();
        killCurrentProcess();
    }

    /// 第三方不得使用内部方法

    /**
     *如果您想检查配置，请使用CaocConfig.Builder.get();
     * @return 当前配置
     */
    @RestrictTo(RestrictTo.Scope.LIBRARY)
    @NonNull
    public static CaocConfig getConfig() {
        return config;
    }

    /**
     * 您不能使用这个，请使用CaocConfig.Builder.apply()
     * @param config 要使用的配置
     */
    @RestrictTo(RestrictTo.Scope.LIBRARY)
    public static void setConfig(@NonNull CaocConfig config) {
        CustomActivityOnCrash.config = config;
    }

    /**
     * - 应用程序在初始化时崩溃(handleBindApplication在堆栈中)
     * - 错误活动崩溃(activityClass在堆栈中)
     * @param throwable    检查堆栈跟踪的可抛出项
     * @param activityClass 应用程序崩溃时要启动的activity类
     * @return 如果堆栈跟踪是冲突的，且活动不能启动，则为true，否则为false
     */
    private static boolean isStackTraceLikelyConflictive(@NonNull Throwable throwable, @NonNull Class<? extends Activity> activityClass) {
        do {
            StackTraceElement[] stackTrace = throwable.getStackTrace();
            for (StackTraceElement element : stackTrace) {
                if ((element.getClassName().equals("android.app.ActivityThread") && element.getMethodName().equals("handleBindApplication")) || element.getClassName().equals(activityClass.getName())) {
                    return true;
                }
            }
        } while ((throwable = throwable.getCause()) != null);
        return false;
    }

    /**
     * @param context    一个有效的上下文。不能为空。
     * @param dateFormat 用于将日期转换为字符串的DateFormat
     * @return 格式化的日期，如果无法确定，则为"未知,Unknown"
     */
    @Nullable
    private static String getBuildDateAsString(@NonNull Context context, @NonNull DateFormat dateFormat) {
        long buildDate;
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0);
            ZipFile zf = new ZipFile(ai.sourceDir);

            //如果失败了，可以尝试使用旧的zip方法
            ZipEntry ze = zf.getEntry("classes.dex");
            buildDate = ze.getTime();


            zf.close();
        } catch (Exception e) {
            buildDate = 0;
        }

        if (buildDate > 312764400000L) {
            return dateFormat.format(new Date(buildDate));
        } else {
            return null;
        }
    }

    /**
     * @param context 一个有效的上下文。不能为空。
     * @return 版本名称，或者“未知(如果无法确定)Unknown
     */
    @NonNull
    private static String getVersionName(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (Exception e) {
            return "Unknown";
        }
    }

    /**
     * @return 设备型号名称(即， "LGE Nexus 5")
     */
    @NonNull
    private static String getDeviceModelName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    /**
     * @param s 要大写的字符串
     * @return 大写的字符串
     */
    @NonNull
    private static String capitalize(@Nullable String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    /**
*内部方法，用于猜测必须从错误活动调用哪个活动才能重新启动应用程序。
*它将首先从意图过滤器<action android:name="com. mx .mybase.crash "的AndroidManifest中获取活动。重启" / >,
*如果无法找到它们，它将获得默认启动程序。
*如果没有默认启动程序，则返回null。
     *
     * @param context 一个有效的上下文。不能为空。
     * @return 猜测的restart activity类，如果没有找到合适的类，则为null
     */
    @Nullable
    private static Class<? extends Activity> guessRestartActivityClass(@NonNull Context context) {
        Class<? extends Activity> resolvedActivityClass;

        //如果定义了action，就使用它
        resolvedActivityClass = getRestartActivityClassWithIntentFilter(context);

        //否则，获取默认启动程序活动
        if (resolvedActivityClass == null) {
            resolvedActivityClass = getLauncherActivity(context);
        }

        return resolvedActivityClass;
    }

    /**
*内部方法，用于通过intent-filter <action android:name="com. mx .mybase.crash "获取第一个activity。重启" / >,
*如果没有带有意图过滤器的活动，则返回null。
     *
     * @param context 一个有效的上下文。不能为空。
     * @return 一个有效的活动类，如果没有找到合适的，则为null
     */
    @SuppressWarnings("unchecked")
    @Nullable
    private static Class<? extends Activity> getRestartActivityClassWithIntentFilter(@NonNull Context context) {
        Intent searchedIntent = new Intent().setAction(INTENT_ACTION_RESTART_ACTIVITY).setPackage(context.getPackageName());
        List<ResolveInfo> resolveInfos = context.getPackageManager().queryIntentActivities(searchedIntent,
                PackageManager.GET_RESOLVED_FILTER);

        if (resolveInfos != null && resolveInfos.size() > 0) {
            ResolveInfo resolveInfo = resolveInfos.get(0);
            try {
                return (Class<? extends Activity>) Class.forName(resolveInfo.activityInfo.name);
            } catch (ClassNotFoundException e) {
                //不应该发生，打印到日志中!
                Log.e(TAG, "通过intent filter解析restart activity类时失败，堆栈跟踪如下", e);
            }
        }

        return null;
    }

    /**
*内部方法，用于获取应用程序的默认启动程序活动。
*如果没有可启动的活动，则返回null。
     *
     * @param context 一个有效的上下文。不能为空。
     * @return 一个有效的活动类，如果没有找到合适的，则为null
     */
    @SuppressWarnings("unchecked")
    @Nullable
    private static Class<? extends Activity> getLauncherActivity(@NonNull Context context) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        if (intent != null && intent.getComponent() != null) {
            try {
                return (Class<? extends Activity>) Class.forName(intent.getComponent().getClassName());
            } catch (ClassNotFoundException e) {
                //不应该发生，打印到日志中!
                Log.e(TAG, "当通过getLaunchIntentForPackage解析restart activity类时失败，堆栈跟踪如下!", e);
            }
        }

        return null;
    }

    /**
     *内部方法，用于猜测应用程序崩溃时必须调用哪个错误活动。
     *它将首先从意图过滤器<action android:name="com. mx .mybase.crash "的AndroidManifest中获取活动。错误" / >,
     *如果无法找到它们，则使用默认的错误活动。
     *
     * @param context 一个有效的上下文。不能为空。
     * @return 猜测的错误活动类，或者如果没有找到默认的错误活动
     */
    @NonNull
    private static Class<? extends Activity> guessErrorActivityClass(@NonNull Context context) {
        Class<? extends Activity> resolvedActivityClass;

        //如果定义了action，就使用它
        resolvedActivityClass = getErrorActivityClassWithIntentFilter(context);

        //否则，获取默认的错误活动
        if (resolvedActivityClass == null) {
            resolvedActivityClass = DefaultErrorActivity.class;
        }

        return resolvedActivityClass;
    }

    /**
     *内部方法，用于通过intent-filter <action android:name="com. mx .mybase.crash "获取第一个activity。错误" / >,
     *如果没有带有意图过滤器的活动，则返回null。
     *
     * @param context 一个有效的上下文。不能为空。
     * @return 一个有效的活动类，如果没有找到合适的，则为null
     */
    @SuppressWarnings("unchecked")
    @Nullable
    private static Class<? extends Activity> getErrorActivityClassWithIntentFilter(@NonNull Context context) {
        Intent searchedIntent = new Intent().setAction(INTENT_ACTION_ERROR_ACTIVITY).setPackage(context.getPackageName());
        List<ResolveInfo> resolveInfos = context.getPackageManager().queryIntentActivities(searchedIntent,
                PackageManager.GET_RESOLVED_FILTER);

        if (resolveInfos != null && resolveInfos.size() > 0) {
            ResolveInfo resolveInfo = resolveInfos.get(0);
            try {
                return (Class<? extends Activity>) Class.forName(resolveInfo.activityInfo.name);
            } catch (ClassNotFoundException e) {
                //不应该发生，打印到日志中!
                Log.e(TAG, "当通过intent filter解析error activity类时失败，堆栈跟踪如下!", e);
            }
        }

        return null;
    }

    /**
     * INTERNAL 方法，该方法将终止当前进程
     * 它是在重启或关闭应用程序后使用的。
     */
    private static void killCurrentProcess() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }

    /**
     * INTERNAL 方法，该方法存储最后一次崩溃时间戳
     *
     * @param timestamp ：当前时间戳
     */
    @SuppressLint("ApplySharedPref") //这必须立即完成，因为我们正在扼杀应用程序
    private static void setLastCrashTimestamp(@NonNull Context context, long timestamp) {
        context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE).edit().putLong(SHARED_PREFERENCES_FIELD_TIMESTAMP, timestamp).commit();
    }

    /**
     * INTERNAL 获取最后一个崩溃时间戳的内部方法
     *
     * @return 最后一次崩溃时间戳，如果没有设置-1。
     */
    private static long getLastCrashTimestamp(@NonNull Context context) {
        return context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE).getLong(SHARED_PREFERENCES_FIELD_TIMESTAMP, -1);
    }

    /**
     *内部方法，它告诉应用程序是否在最后几秒崩溃。
     *这是用来避免重新启动循环。
     *
     * @return 如果应用程序在最后几秒崩溃，则为true，否则为false。
     */
    private static boolean hasCrashedInTheLastSeconds(@NonNull Context context) {
        long lastTimestamp = getLastCrashTimestamp(context);
        long currentTimestamp = System.currentTimeMillis();
//        long currentTimestamp = new Date().getTime();

        return (lastTimestamp <= currentTimestamp && currentTimestamp - lastTimestamp < config.getMinTimeBetweenCrashesMs());
    }

    /**
     *事件发生时调用的接口，以便报告它们
     *以应用程序为例，谷歌分析事件。
     */
    public interface EventListener extends Serializable {
        void onLaunchErrorActivity();

        void onRestartAppFromErrorActivity();

        void onCloseAppFromErrorActivity();
    }
}
