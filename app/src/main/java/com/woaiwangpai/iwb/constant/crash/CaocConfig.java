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

import android.app.Activity;

import androidx.annotation.DrawableRes;
import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Modifier;

public class CaocConfig implements Serializable {

    @IntDef({BACKGROUND_MODE_CRASH, BACKGROUND_MODE_SHOW_CUSTOM, BACKGROUND_MODE_SILENT})
    @Retention(RetentionPolicy.SOURCE)
    private @interface BackgroundMode {
        //I hate empty blocks
    }

    public static final int BACKGROUND_MODE_SILENT = 0;
    public static final int BACKGROUND_MODE_SHOW_CUSTOM = 1;
    public static final int BACKGROUND_MODE_CRASH = 2;

    private int backgroundMode = BACKGROUND_MODE_SHOW_CUSTOM;
    private boolean enabled = true;
    private boolean showErrorDetails = true;
    private boolean showRestartButton = true;
    private boolean logErrorOnRestart = true;
    private boolean trackActivities = false;
    private int minTimeBetweenCrashesMs = 3000;
    private Integer errorDrawable = null;
    private Class<? extends Activity> errorActivityClass = null;
    private Class<? extends Activity> restartActivityClass = null;
    private CustomActivityOnCrash.EventListener eventListener = null;

    @BackgroundMode
    public int getBackgroundMode() {
        return backgroundMode;
    }

    public void setBackgroundMode(@BackgroundMode int backgroundMode) {
        this.backgroundMode = backgroundMode;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isShowErrorDetails() {
        return showErrorDetails;
    }

    public void setShowErrorDetails(boolean showErrorDetails) {
        this.showErrorDetails = showErrorDetails;
    }

    public boolean isShowRestartButton() {
        return showRestartButton;
    }

    public void setShowRestartButton(boolean showRestartButton) {
        this.showRestartButton = showRestartButton;
    }

    public boolean isLogErrorOnRestart() {
        return logErrorOnRestart;
    }

    public void setLogErrorOnRestart(boolean logErrorOnRestart) {
        this.logErrorOnRestart = logErrorOnRestart;
    }

    public boolean isTrackActivities() {
        return trackActivities;
    }

    public void setTrackActivities(boolean trackActivities) {
        this.trackActivities = trackActivities;
    }

    public int getMinTimeBetweenCrashesMs() {
        return minTimeBetweenCrashesMs;
    }

    public void setMinTimeBetweenCrashesMs(int minTimeBetweenCrashesMs) {
        this.minTimeBetweenCrashesMs = minTimeBetweenCrashesMs;
    }

    @Nullable
    @DrawableRes
    public Integer getErrorDrawable() {
        return errorDrawable;
    }

    public void setErrorDrawable(@Nullable @DrawableRes Integer errorDrawable) {
        this.errorDrawable = errorDrawable;
    }

    @Nullable
    public Class<? extends Activity> getErrorActivityClass() {
        return errorActivityClass;
    }

    public void setErrorActivityClass(@Nullable Class<? extends Activity> errorActivityClass) {
        this.errorActivityClass = errorActivityClass;
    }

    @Nullable
    public Class<? extends Activity> getRestartActivityClass() {
        return restartActivityClass;
    }

    public void setRestartActivityClass(@Nullable Class<? extends Activity> restartActivityClass) {
        this.restartActivityClass = restartActivityClass;
    }

    @Nullable
    public CustomActivityOnCrash.EventListener getEventListener() {
        return eventListener;
    }

    public void setEventListener(@Nullable CustomActivityOnCrash.EventListener eventListener) {
        this.eventListener = eventListener;
    }

    public static class Builder {
        private CaocConfig config;

        @NonNull
        public static Builder create() {
            Builder builder = new Builder();
            CaocConfig currentConfig = CustomActivityOnCrash.getConfig();

            CaocConfig config = new CaocConfig();
            config.backgroundMode = currentConfig.backgroundMode;
            config.enabled = currentConfig.enabled;
            config.showErrorDetails = currentConfig.showErrorDetails;
            config.showRestartButton = currentConfig.showRestartButton;
            config.logErrorOnRestart = currentConfig.logErrorOnRestart;
            config.trackActivities = currentConfig.trackActivities;
            config.minTimeBetweenCrashesMs = currentConfig.minTimeBetweenCrashesMs;
            config.errorDrawable = currentConfig.errorDrawable;
            config.errorActivityClass = currentConfig.errorActivityClass;
            config.restartActivityClass = currentConfig.restartActivityClass;
            config.eventListener = currentConfig.eventListener;

            builder.config = config;

            return builder;
        }

        /**
         *定义当应用程序在后台时是否必须启动错误活动。
         * BackgroundMode。BACKGROUND_MODE_SHOW_CUSTOM:当应用程序在后台时启动错误活动，
         * BackgroundMode。BACKGROUND_MODE_CRASH:当应用程序在后台时，启动默认的系统错误，
         * BackgroundMode。BACKGROUND_MODE_SILENT:当应用程序在后台时，会无声崩溃，
         *默认为BackgroundMode。BACKGROUND_MODE_SHOW_CUSTOM(当崩溃发生时，应用程序将被带到前台)。
         */
        @NonNull
        public Builder backgroundMode(@BackgroundMode int backgroundMode) {
            config.backgroundMode = backgroundMode;
            return this;
        }

        /**
         *定义是否启用CustomActivityOnCrash崩溃拦截机制。
         *如果你想让CustomActivityOnCrash拦截崩溃，将其设置为true，
         *如果您希望将它们视为未安装库，则为false。
         *默认值为true。
         */
        @NonNull
        public Builder enabled(boolean enabled) {
            config.enabled = enabled;
            return this;
        }

        /**
         定义错误活动是否必须显示错误详细信息按钮。
         *如果您想显示完整的堆栈跟踪和设备信息，请将其设置为true，
         *如果你想隐藏它，则为false。
         *默认值为true。
         */
        @NonNull
        public Builder showErrorDetails(boolean showErrorDetails) {
            config.showErrorDetails = showErrorDetails;
            return this;
        }

        /**
         定义错误活动是否应该显示重启按钮。
         *如果你想显示重启按钮，将其设置为true，
         *如果要显示关闭按钮，则为false。
         *注意，即使重启是启用的，但你的应用程序没有任何启动活动，
         *默认错误活动仍然使用关闭按钮。
         *默认值为true。
         */
        @NonNull
        public Builder showRestartButton(boolean showRestartButton) {
            config.showRestartButton = showRestartButton;
            return this;
        }

        /**
         *定义自定义活动显示后是否必须再次记录堆栈跟踪。
         *如果您想再次记录堆栈跟踪，请将其设置为true，
         *如果不需要额外的日志记录，则为false。
         *此选项的存在是因为默认的Android Studio logcat视图只显示输出
         ，由于error活动在一个新进程上运行，
         *你不能轻易看到以前的输出。
         *在内部，当调用getConfigFromIntent()时，它被记录下来。
         *默认值为true。
         */
        @NonNull
        public Builder logErrorOnRestart(boolean logErrorOnRestart) {
            config.logErrorOnRestart = logErrorOnRestart;
            return this;
        }

        /**
         *定义是否应该跟踪用户访问的活动
         所以当错误发生时，它们会被报告。
         *默认为false。
         */
        @NonNull
        public Builder trackActivities(boolean trackActivities) {
            config.trackActivities = trackActivities;
            return this;
        }

        /**
         *定义应用程序崩溃之间必须经过的时间，以确定我们不是
         *在崩溃循环中。如果这次的事故比上次少，
         *错误活动将不会启动，系统崩溃屏幕将被调用。
         *默认值是3000。
         */
        @NonNull
        public Builder minTimeBetweenCrashesMs(int minTimeBetweenCrashesMs) {
            config.minTimeBetweenCrashesMs = minTimeBetweenCrashesMs;
            return this;
        }

        /**
         *定义在默认错误活动映像中使用哪个drawable。
         *如果您想使用默认图像以外的图像，请设置此选项。
         *默认为R.drawable。customactivityoncrash_error_image(一个可爱的上下颠倒的bug)。
         */
        @NonNull
        public Builder errorDrawable(@Nullable @DrawableRes Integer errorDrawable) {
            config.errorDrawable = errorDrawable;
            return this;
        }

        /**
         *设置错误活动类在崩溃发生时启动。
         *如果为空，则使用默认的错误活动。
         */
        @NonNull
        public Builder errorActivity(@Nullable Class<? extends Activity> errorActivityClass) {
            config.errorActivityClass = errorActivityClass;
            return this;
        }

        /**
         *设置错误活动在发生崩溃时必须启动的主活动类。
         *如果没有设置或设置为null，将使用默认的启动活动。
         *如果您的应用程序没有启动活动，并且没有设置此选项，则默认的错误活动将关闭。
         */
        @NonNull
        public Builder restartActivity(@Nullable Class<? extends Activity> restartActivityClass) {
            config.restartActivityClass = restartActivityClass;
            return this;
        }

        /**
         *设置事件发生时调用的事件侦听器，以便报告事件
         *以应用程序为例，谷歌分析事件。
         *如果未设置或设置为null，则不会报告任何事件。
         *
         * @param eventListener 事件侦听器。
         * @throws if the eventListener是一个内部类或匿名类
         */
        @NonNull
        public Builder eventListener(@Nullable CustomActivityOnCrash.EventListener eventListener) {
            if (eventListener != null && eventListener.getClass().getEnclosingClass() != null && !Modifier.isStatic(eventListener.getClass().getModifiers())) {
                //事件侦听器不能是内部类或匿名类，因为它需要序列化。将其更改为自己的类，或使其成为静态内部类。
                throw new IllegalArgumentException("The event listener cannot be an inner or anonymous class, because it will need to be serialized. Change it to a class of its own, or make it a static inner class.");
            } else {
                config.eventListener = eventListener;
            }
            return this;
        }

        @NonNull
        public CaocConfig get() {
            return config;
        }

        public void apply() {
            CustomActivityOnCrash.setConfig(config);
        }
    }


}
