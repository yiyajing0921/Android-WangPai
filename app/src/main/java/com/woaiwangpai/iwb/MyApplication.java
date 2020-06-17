package com.woaiwangpai.iwb;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.multidex.MultiDex;

import com.chuanglan.shanyan_sdk.OneKeyLoginManager;
import com.kingja.loadsir.core.LoadSir;
import com.lzy.okgo.https.HttpsUtils;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.tencent.imsdk.TIMBackgroundParam;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMGroupManager;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMOfflinePushListener;
import com.tencent.imsdk.TIMOfflinePushNotification;
import com.tencent.imsdk.TIMOfflinePushSettings;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupDetailInfoResult;
import com.tencent.imsdk.session.SessionWrapper;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.IMEventListener;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.utils.TUIKitLog;
import com.woaiwangpai.iwb.constant.Constant;
import com.woaiwangpai.iwb.constant.SourceHelper;
import com.woaiwangpai.iwb.constant.crash.CaocConfig;
import com.woaiwangpai.iwb.constant.crash.CrashHandler;
import com.woaiwangpai.iwb.message.helper.ConfigHelper;
import com.woaiwangpai.iwb.mvp.base.BaseApplication;
import com.woaiwangpai.iwb.mvp.network.callback.loadsir.EmptyCallback;
import com.woaiwangpai.iwb.mvp.network.callback.loadsir.ErrorCallBack;
import com.woaiwangpai.iwb.mvp.network.callback.loadsir.FinancialCallback;
import com.woaiwangpai.iwb.mvp.network.callback.loadsir.LoadingCallback;
import com.woaiwangpai.iwb.mvp.network.callback.loadsir.TimeoutCallback;
import com.woaiwangpai.iwb.mvp.network.nested.NestedOkGo;
import com.woaiwangpai.iwb.utils.LogUtils;
import com.woaiwangpai.iwb.utils.SharedPreferenceUtil;
import com.woaiwangpai.iwb.utils.ToastUtils;
import com.woaiwangpai.iwb.utils.cache.acache.ACache;
import com.woaiwangpai.iwb.utils.interceptor.AddCookiesInterceptor;
import com.woaiwangpai.iwb.utils.interceptor.NetCacheInterceptor;
import com.woaiwangpai.iwb.wechat.share.auth.Social;

import java.io.File;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * author mahongyin
 * time: 2019/8/21 19:37
 * email: mhy.work@qq.com
 */
public class MyApplication extends BaseApplication {
    private static MyApplication myApplication;
    public Dialog dialog = null;
    private static Context sContext;
    private boolean isDebug = true;//全局debug开关

    private static final String TAG = "52pai";

    //txIm id
    public static final int SDKAPPID = 1400238707;
//    private List<CustomFaceBean.DataBean.ListBean> faceList = new ArrayList<>();

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public boolean isDebug() {
        return isDebug;
    }

    public void setDebug(boolean isDebug) {
        this.isDebug = isDebug;
    }


    //    //MyApplication.getInstance().getContext()获取全局context
    public static Context getContext() {
        return sContext;
    }

    public static MyApplication getInstance() {
        if (myApplication != null && myApplication instanceof MyApplication) {
            return myApplication;
        } else {
            myApplication = new MyApplication();
            myApplication.onCreate();
            return myApplication;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        sContext = this.getApplicationContext();
        if (!BuildConfig.DEBUG) {
            setDebug(false);
        }
        //        AutoLayoutConifg.getInstance().useDeviceSize();
        LoadSir.beginBuilder().addCallback(new EmptyCallback()).addCallback(new ErrorCallBack()).addCallback(new TimeoutCallback()).addCallback(new LoadingCallback()).addCallback(new FinancialCallback()).setDefaultCallback(LoadingCallback.class).commit();

        //初始化缓存
        mACache = ACache.get(sContext);
        //初始化弹窗样式
        ToastUtils.init(this);
        //初始化轻量缓存
        SharedPreferenceUtil.init(this, SharedPreferenceUtil.PREFERENCE_NAME);
        //初始化网络请求
        okHttp();
        //微信
        registToWX();
        //IM
        initTXIM();
        //登陆分享
        Social.init(this);
        if (!BuildConfig.DEBUG) {
            //正式
            CrashHandler crashHandler = CrashHandler.getInstance();
            crashHandler.init(this);
        } else {
            // 加入崩溃的查看界面，DEbug
            CaocConfig.Builder.create().apply();
        }
        //闪验初始化
        initShanYanSDK();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelName = "通知消息";
            String channelMaxName = "系统消息";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            int max = NotificationManager.IMPORTANCE_MAX;//优先级
            createNotificationChannel(CHANNELID, channelName, importance);
            createNotificationChannel(CHANNELMAX, channelMaxName, max);
        }

    }

    /**
     * 闪验
     */
    private void initShanYanSDK() {
        //闪验SDK配置debug开关 （必须放在初始化之前，开启后可打印闪验SDK更加详细日志信息）
        OneKeyLoginManager.getInstance().setDebug(true);

        //闪验SDK初始化（建议放在Application的onCreate方法中执行）
        OneKeyLoginManager.getInstance().init(getApplicationContext(), "BjVX1s58", (code, result) -> {
            Log.i(TAG, "闪验结果result==" + result);
            Log.i(TAG, "闪验结果code==" + code);
        });
    }

    //初始化腾讯Im
    private void initTXIM() {
        //判断是否是在主线程
        if (SessionWrapper.isMainProcess(getApplicationContext())) {
            /**
             * TUIKit的初始化函数
             *
             * @param context  应用的上下文，一般为对应应用的ApplicationContext
             * @param sdkAppID 您在腾讯云注册应用时分配的sdkAppID
             * @param configs  TUIKit的相关配置项，一般使用默认即可，需特殊配置参考API文档
             */
            TUIKit.init(this, SDKAPPID, new ConfigHelper().getConfigs());
            setTXPush();
//            if ( ThirdPushTokenMgr.USER_GOOGLE_FCM ) {
//                FirebaseInstanceId.getInstance().getInstanceId()
//                        .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
//                            @Override
//                            public void onComplete(Task<InstanceIdResult> task) {
//                                if (!task.isSuccessful()) {
//                                    DemoLog.w(TAG, "getInstanceId failed exception = " + task.getException());
//                                    return;
//                                }
//
//                                // Get new Instance ID token
//                                String token = task.getResult().getToken();
//                                DemoLog.i(TAG, "google fcm getToken = " + token);
//
//                                ThirdPushTokenMgr.getInstance().setThirdPushToken(token);
//                            }
//                        });
//            } else if (IMFunc.isBrandXiaoMi()) {
//                // 小米离线推送
//                MiPushClient.registerPush(this, PrivateConstants.XM_PUSH_APPID, PrivateConstants.XM_PUSH_APPKEY);
//            } else if (IMFunc.isBrandHuawei()) {
//                // 华为离线推送
//                HMSAgent.init(this);
//            } else if (MzSystemUtils.isBrandMeizu(this)) {
//                // 魅族离线推送
//                PushManager.register(this, PrivateConstants.MZ_PUSH_APPID, PrivateConstants.MZ_PUSH_APPKEY);
//            } else if (IMFunc.isBrandVivo()) {
//                // vivo离线推送
//                PushClient.getInstance(getApplicationContext()).initialize();
//            }

            registerActivityLifecycleCallbacks(new StatisticActivityLifecycleCallback());
        }

        String username = SharedPreferenceUtil.getStringData("username");
        String imsig = SharedPreferenceUtil.getStringData("imsig");
        if (!username.equals("") && !imsig.equals("")) {
            TUIKit.login(username, imsig, new IUIKitCallBack() {
                @Override
                public void onSuccess(Object data) {
                    SharedPreferences shareInfo = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = shareInfo.edit();
                    editor.putBoolean("auto_login", true);
                    editor.commit();
                    Log.d("登录腾讯", "登录腾讯im成功");
                    setTXPush();
                }

                @Override
                public void onError(String module, int errCode, String errMsg) {
//                    ToastUtils.show("登录腾讯im失败");
                }
            });
        }
    }

    public static void setTXPush() {
        TIMOfflinePushSettings settings = new TIMOfflinePushSettings();
        //开启离线推送
        settings.setEnabled(true);

        TIMManager.getInstance().setOfflinePushSettings(settings);
        TIMManager.getInstance().setOfflinePushListener(new TIMOfflinePushListener() {
            @Override
            public void handleNotification(TIMOfflinePushNotification timOfflinePushNotification) {
                Log.d("MyApplication", "收到离线消息");
                // 这里的doNotify是ImSDK内置的通知栏提醒，
                ArrayList<String> list = new ArrayList<>();
                list.add(timOfflinePushNotification.getSenderIdentifier());
                TIMFriendshipManager.getInstance().getUsersProfile(list, false, new TIMValueCallBack<List<TIMUserProfile>>() {
                    @Override
                    public void onError(int i, String s) {
                        TUIKitLog.e(TAG, "loadUserProfile err code = " + i + ", desc = " + s);
//                        if(i == 6014){
//                            if (MyApplication.getInstance().isLogin()) {
//                                ToastUtils.show("登录失效，请重新登录");
//                            }
//                        }
                    }

                    @Override
                    public void onSuccess(List<TIMUserProfile> timUserProfiles) {
                        if (timUserProfiles == null || timUserProfiles.size() != 1) {
                            timOfflinePushNotification.doNotify(sContext, R.mipmap.ic_launcher);
                            return;
                        }
                        final TIMUserProfile profile = timUserProfiles.get(0);
                        String content = timOfflinePushNotification.getContent();
                        if (timOfflinePushNotification.getConversationType() == TIMConversationType.Group) {
                            if (null != timOfflinePushNotification.getSenderNickName()) {
                                if (timOfflinePushNotification.getSenderNickName().length() > 0) {
                                    timOfflinePushNotification.setContent(timOfflinePushNotification.getSenderNickName() + ":" + content.substring(content.indexOf(":") + 1));
                                }
                            } else {
                                timOfflinePushNotification.setContent(profile.getNickName() + ":" + content.substring(content.indexOf(":") + 1));
                            }
                            ArrayList<String> slist = new ArrayList<String>();
                            slist.add(timOfflinePushNotification.getConversationId());
                            //获取服务器群组信息
                            TIMGroupManager.getInstance().getGroupInfo(slist, //需要获取信息的群组 ID 列表
                                    new TIMValueCallBack<List<TIMGroupDetailInfoResult>>() {
                                        @Override
                                        public void onError(int i, String s) {
                                            if (i == 6014) {
                                                if (MyApplication.getInstance().isLogin()) {
                                                    ToastUtils.show("登录失效，请重新登录");
                                                }
                                            }
                                        }

                                        @Override
                                        public void onSuccess(List<TIMGroupDetailInfoResult> timGroupDetailInfoResults) {
                                            if (timGroupDetailInfoResults == null || timGroupDetailInfoResults.size() != 1) {
                                                timOfflinePushNotification.doNotify(sContext, R.mipmap.ic_launcher);
                                                return;
                                            }
                                            TIMGroupDetailInfoResult groupDetailInfo = timGroupDetailInfoResults.get(0);
                                            timOfflinePushNotification.setTitle(groupDetailInfo.getGroupName());
                                            timOfflinePushNotification.doNotify(sContext, R.mipmap.ic_launcher);
                                        }
                                    });
                        } else if (timOfflinePushNotification.getConversationType() == TIMConversationType.C2C) {
                            timOfflinePushNotification.setTitle(profile.getNickName());
                            timOfflinePushNotification.setContent(content.substring(content.indexOf(":") + 1));
                            timOfflinePushNotification.doNotify(sContext, R.mipmap.ic_launcher);
                        } else {
                            timOfflinePushNotification.setContent(content.substring(content.indexOf(":") + 1));
                            timOfflinePushNotification.doNotify(sContext, R.mipmap.ic_launcher);
                        }

                    }
                });
                //应用也可以选择自己利用回调参数notification来构造自己的通知栏提醒
                //PushUtil.getInstance(MyApp_.getInstance());
                //notification.getTitle();        //标题
                //notification.getContent();      //内容
                //notification.getConversationId();      //获取会话id
                //notification.getSenderIdentifier();      //获取发送者id
            }
        });
    }

    class StatisticActivityLifecycleCallback implements Application.ActivityLifecycleCallbacks {
        private int foregroundActivities = 0;
        private boolean isChangingConfiguration;

        /**
         * @param activity
         * @param bundle
         */
        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {
            Log.i(TAG, "onActivityCreated bundle: " + bundle);
            if (bundle != null) { // 若bundle不为空则程序异常结束
                LogUtils.e("发生什么异常" + activity.getLocalClassName());
                // 重启整个程序
//                Intent intent = new Intent(activity, StartActivity.class);
//                Intent intent = new Intent(activity, NewSplashActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
            }
        }

        @Override
        public void onActivityStarted(Activity activity) {
            foregroundActivities++;
            if (foregroundActivities == 1 && !isChangingConfiguration) {
                // 应用切到前台
                Log.i(TAG, "application enter foreground");
                TIMManager.getInstance().doForeground(new TIMCallBack() {
                    @Override
                    public void onError(int code, String desc) {
                        Log.e(TAG, "doForeground err = " + code + ", desc = " + desc);
                    }

                    @Override
                    public void onSuccess() {
                        Log.i(TAG, "doForeground success");
                    }
                });
                TUIKit.removeIMEventListener(mIMEventListener);
            }
            isChangingConfiguration = false;
        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {
            foregroundActivities--;
            if (foregroundActivities == 0) {
                // 应用切到后台
                Log.i(TAG, "application enter background");
                int unReadCount = 0;
                List<TIMConversation> conversationList = TIMManager.getInstance().getConversationList();
                for (TIMConversation timConversation : conversationList) {
                    unReadCount += timConversation.getUnreadMessageNum();
                }
                TIMBackgroundParam param = new TIMBackgroundParam();
                param.setC2cUnread(unReadCount);
                TIMManager.getInstance().doBackground(param, new TIMCallBack() {
                    @Override
                    public void onError(int code, String desc) {
                        Log.e(TAG, "doBackground err = " + code + ", desc = " + desc);
                    }

                    @Override
                    public void onSuccess() {
                        Log.i(TAG, "doBackground success");
                    }
                });
                // 应用退到后台，消息转化为系统通知
                TUIKit.addIMEventListener(mIMEventListener);
            }
            isChangingConfiguration = activity.isChangingConfigurations();
        }

        private IMEventListener mIMEventListener = new IMEventListener() {
            @Override
            public void onNewMessages(List<TIMMessage> msgs) {
                for (TIMMessage msg : msgs) {
                    // 小米手机需要在设置里面把demo的"后台弹出权限"打开才能点击Notification跳转。TIMOfflinePushNotification后续不再维护，如有需要，建议应用自己调用系统api弹通知栏消息。
                    TIMOfflinePushNotification notification = new TIMOfflinePushNotification(MyApplication.this, msg);
                    notification.doNotify(MyApplication.this, R.mipmap.ic_launcher);
                }
            }
        };

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    }

    /**
     * 获取当前的网络状态  -1：没有网络  1：WIFI网络 2：移动网络
     *
     * @param context
     * @return
     */
    public int getAPNType(Context context) {
        int netType = -1;
        try {
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo == null) {
                return netType;
            }
            int nType = networkInfo.getType();
            if (nType == ConnectivityManager.TYPE_MOBILE) {
                netType = 2;
            } else if (nType == ConnectivityManager.TYPE_WIFI) {
                netType = 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return netType;
    }

    public static String CHANNELID = "message";
    public static String CHANNELMAX = "system";

    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel(String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(channel);
        }
    }

    public static IWXAPI mWxApi;

    private void registToWX() {
        //AppConst.WEIXIN.APP_ID是指你应用在微信开放平台上的AppID，记得替换。
        mWxApi = WXAPIFactory.createWXAPI(this, Constant.WEIXIN_APP_ID, false);
        // 将该app注册到微信
        mWxApi.registerApp(Constant.WEIXIN_APP_ID);
    }

    private void okHttp() {
        // 指定缓存路径,缓存大小30Mb
        Cache cache = new Cache(new File(sContext.getCacheDir(), Constant.HTTPCACHE), 1024 * 1024 * 20);
//        在application初始化OKGo的时忽略证书验证.
//        第一步:
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpsUtils.SSLParams sslParams1 = HttpsUtils.getSslSocketFactory();
        builder.sslSocketFactory(sslParams1.sSLSocketFactory, sslParams1.trustManager);
//        第二步:
        builder.hostnameVerifier((hostname, session) -> {
            //强行返回true 强行验证成功
            return true;
        });
        builder.proxy(Proxy.NO_PROXY);//防代理 抓包
        builder.addInterceptor(new HttpLoggingInterceptor("okGoTAG"));
        builder.addInterceptor(new AddCookiesInterceptor(this));
        builder.cache(cache);
        builder.addNetworkInterceptor(new NetCacheInterceptor());//使用缓存拦截器
/*********************************************/
        NestedOkGo.getInstanse().init(builder.build(), this);
/**********************************************************************/
    }

    public static boolean isProxy() {
        String proxyAddress = "";
        int proxyPort = 0;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            proxyAddress = System.getProperty("http.proxyHost");
            String proxyPortString = System.getProperty("http.proxyPort");
            proxyPort = Integer.parseInt((proxyPortString != null ? proxyPortString : "-1"));
        } else {
            proxyAddress = android.net.Proxy.getHost(getContext());//this
            proxyPort = android.net.Proxy.getPort(getContext());//this
        }
        if (!TextUtils.isEmpty(proxyAddress) && proxyPort != -1) {
            Log.e("", "有代理,不能访问");
            return true;
        }
        return false;
    }

    /**
     * 检查代理
     *
     * @return
     */
    private boolean checkAgent() {
        if (false) {
            return false;
        } else {
            String proHost = android.net.Proxy.getDefaultHost();
            int proPort = android.net.Proxy.getDefaultPort();
            if (proHost == null || proPort < 0) {
                return false;
            } else {
                Log.e("", "有代理,不能访问");
                return true;
            }
        }
    }

    //Acache 清除
    public void clearACache() {
        mACache.clear();//清除所有数据
    }

    //Mian fragment postion
//    int mainPage = 0;
    private String mainPage = "android-home";

    /**
     * main 启动第几fragment 标志
     *
     * @param page -1是第0页， 0是默认值不对应具体页，1-3对应各自页索引值
     */
    public void setMianPage(String page) {
        mainPage = page;
    }

    public String getMainPage() {
        return mainPage == null ? "" : mainPage;
    }

    private String sotype;

    //任务 类型
    public String getSotype() {
        return sotype;
    }

    public void setSotype(String type) {
        this.sotype = type;
    }

    private String token, imsig, userName, promote_id, invite_code, type, phone_auth, level, birthday, gender, memberIdentity, avatarGround, skinBg;
    private ACache mACache;
//    private HomeBottomMenuBean.DataBean bottomMenu;

    /**
     * token
     *
     * @return
     */
    public String getToken() {
        token = mACache.getAsString("token") == null ? "" : mACache.getAsString("token");
        return token;
    }

    public void setToken(String tk, int saveTime) {
        mACache.put("token", tk, saveTime);
        this.token = tk;
    }

    public void setToken(String tk) {
        mACache.put("token", tk, ACache.TIME_MOON);//保存一月
        this.token = tk;
    }

    public void deleteToken() {
        mACache.remove("token");
    }

    /**
     * type 身份 账号类型:模特,眼线
     *
     * @return
     */
    public String getType() {
        type = mACache.getAsString("type") == null ? "" : mACache.getAsString("type");
        return type;
    }

    public void setType(String tp, int saveTime) {
        mACache.put("type", tp, saveTime);
        this.type = tp;
    }

    public void setType(String tp) {
        mACache.put("type", tp, ACache.TIME_MOON);//保存一月
        this.type = tp;
    }

    public void deleteType() {
        mACache.remove("type");
    }


    /**
     * 眼线：0普通眼线 1眼线队长 2眼线组长
     * 模特: 0:组员   1:组长    2: 主管
     */
    public String getLevel() {
        level = mACache.getAsString("level") == null ? "" : mACache.getAsString("level");
        return level;
    }

    public void setLevel(String level) {
        if (TextUtils.isEmpty(level)) {
            mACache.remove("level");
        } else {
            mACache.put("level", level);
        }
        this.level = level;
    }

    public String getUserName() {
        return userName == null ? "" : userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    /**
     * IM标签
     *
     * @param sig
     * @param saveTime
     */
    public void setImsig(String sig, int saveTime) {
        mACache.put("imsig", sig, saveTime);
        this.imsig = sig;
    }

    public void setImsig(String sig) {
        mACache.put("imsig", sig, ACache.TIME_MOON);
        this.imsig = sig;
    }

    public String getImSig() {
        token = mACache.getAsString("imsig") == null ? "" : mACache.getAsString("imsig");
        return imsig;
    }

    public void deleteImsig() {
        mACache.remove("imsig");
    }

    /**
     * 用户的邀请码invite_code
     *
     * @return
     */
    public String getinvite_code() {
        invite_code = mACache.getAsString("invite_code") == null ? "" : mACache.getAsString("invite_code");
        return invite_code;
    }

    public void setinvite_code(String invite_code) {
        mACache.put("invite_code", invite_code);
        this.invite_code = invite_code;
    }

    /**
     * 推荐id
     *
     * @param
     */
    public String getPromote_id() {
        promote_id = mACache.getAsString("promote_id") == null ? "" : mACache.getAsString("promote_id");
        return promote_id;
    }

    public void setPromote_id(String sig) {
        mACache.put("promote_id", sig);
        this.promote_id = sig;
    }

    public void deletePromote_id() {
        mACache.remove("promote_id");
    }

    /**
     * 绑定手机号状态1为已绑定手机号0为未绑定phone_auth
     */
    public String getPhone_auth() {
        phone_auth = mACache.getAsString("phone_auth") == null ? "" : mACache.getAsString("phone_auth");
        return phone_auth;
    }

    public void setPhone_auth(String phone_auth) {
        if (TextUtils.isEmpty(phone_auth)) {
            mACache.remove("phone_auth");
        } else {
            mACache.put("phone_auth", phone_auth);
        }
        this.phone_auth = phone_auth;

    }

    /**
     * birthday 生日
     */
    public String getBirthday() {
        birthday = mACache.getAsString("birthday") == null ? "" : mACache.getAsString("birthday");
        return birthday == null ? "" : birthday;
    }

    public void setBirthday(String birthday) {
        if (TextUtils.isEmpty(birthday)) {
            mACache.remove("birthday");
        } else {
            mACache.put("birthday", birthday);
        }
        this.birthday = birthday;
    }

    /**
     * gender 性别:1,男;2,女
     */
    public String getGender() {
        gender = mACache.getAsString("gender") == null ? "" : mACache.getAsString("gender");
        return gender == null ? "" : gender;
    }

    public void setGender(String gender) {
        if (TextUtils.isEmpty(gender)) {
            mACache.remove("gender");
        } else {
            mACache.put("gender", gender);
        }
        this.gender = gender;
    }

    //标记账号登录还是三方登陆 默认true手机登陆
    public boolean isPhoneLogin() {
        return isPhoneLogin;
    }

    public void isPhoneLogin(boolean is) {
        isPhoneLogin = is;
    }

    boolean isPhoneLogin = true;

    //是否登录true登录  判断token是否为空 登陆存token 退出登陆delete token
    public boolean isLogin() {
        return !TextUtils.isEmpty(getToken());

    }

    /**
     * 存储会员身份
     *
     * @return true 是会员身份
     * 0非会员 1会员  2超级会员
     */
    //多处需要区分会员身份  修改了一下
    //是会员 但是没判断超级会员
    public boolean isMemberIdentity() {
        memberIdentity = mACache.getAsString("memberIdentity") == null ? "" : mACache.getAsString("memberIdentity");
        return memberIdentity.equals(SourceHelper.member);
    }

    public void setMemberIdentity(int Is_member) {
        if (Is_member == 0) {
            //保存一月
            mACache.put("memberIdentity", SourceHelper.nonMember, ACache.TIME_MOON);
        } else if (Is_member == 1) {
            //保存一月
            mACache.put("memberIdentity", SourceHelper.member, ACache.TIME_MOON);
        } else if (Is_member == 3) {
            //保存一月
            mACache.put("memberIdentity", SourceHelper.supermember, ACache.TIME_MOON);
        }
    }

    public String getMemberIdentity() {
        memberIdentity = mACache.getAsString("memberIdentity") == null ? "" : mACache.getAsString("memberIdentity");
        return memberIdentity;
    }

//    public void setMemberIdentity(String tk) {
//        mACache.put("memberIdentity", tk, ACache.TIME_MOON);//保存一月
//        this.memberIdentity = tk;
//    }

    public void deleteMemberIdentity() {
        mACache.remove("memberIdentity");
    }

    /**
     * 存储头像挂件
     */
    public String getAvatarGround() {
        avatarGround = mACache.getAsString("avatarGround") == null ? "" : mACache.getAsString("avatarGround");
        return avatarGround;
    }

    public void setAvatarGround(String avatarUrl) {
        mACache.put("avatarGround", avatarUrl, ACache.TIME_MOON);//保存一月
        this.avatarGround = avatarUrl;
    }

    public void deleteAvatarGround() {
        mACache.remove("avatarGround");
    }

    /**
     * 存储皮肤背景
     */
    public String getSkinBg() {
        skinBg = mACache.getAsString("SkinBg") == null ? "" : mACache.getAsString("SkinBg");
        return skinBg;
    }

    public void setSkinBg(String skinBg) {
        mACache.put("SkinBg", skinBg, ACache.TIME_MOON);//保存一月
        this.skinBg = skinBg;
    }

    public void deleteSkinBg() {
        mACache.remove("SkinBg");
    }

//    /**
//     * 本地存储的表情包
//     *
//     * @return
//     */
//    public List<CustomFaceBean.DataBean.ListBean> getFaceList() {
//        if (faceList == null) {
//            return new ArrayList<>();
//        }
//        return faceList;
//    }
//
//    public void setFaceList(CustomFaceBean.DataBean.ListBean listBean) {
//        faceList.add(listBean);
//    }

//    public void deleteFaceList() {
//        faceList.clear();
//    }

//    /**
//     * 底部导航栏
//     *
//     * @return
//     */
//    public HomeBottomMenuBean.DataBean getBottomMenu() {
//        bottomMenu = mACache.getAsObject("bottom_menu") == null ? new HomeBottomMenuBean.DataBean() : (HomeBottomMenuBean.DataBean) mACache.getAsObject("bottom_menu");
//        return bottomMenu;
//    }
//
//    public void setBottomMenu(HomeBottomMenuBean.DataBean bean) {
//        mACache.put("bottom_menu", bean, ACache.TIME_MOON);//保存一月
//        this.bottomMenu = bean;
//    }

    public void deleteBottomMenu() {
        mACache.remove("bottom_menu");
    }
}
