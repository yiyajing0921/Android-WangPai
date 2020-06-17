package com.woaiwangpai.iwb.mvp.network.callback;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * @Author : YiYaJing
 * @Data : 2020/6/13 15:15
 * @Description:
 */
class StatisticActivityLifecycleCallback implements Application.ActivityLifecycleCallbacks {
    private int foregroundActivities = 0;
    private boolean isChangingConfiguration;

    /**
     * @param activity
     * @param bundle
     */
    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        if (bundle != null) { // 若bundle不为空则程序异常结束
            // 重启整个程序
//            Intent intent = new Intent(activity, StartActivity.class);
//                Intent intent = new Intent(activity, NewSplashActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            activity.startActivity(intent);
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {
        foregroundActivities++;
        if (foregroundActivities == 1 && !isChangingConfiguration) {
            // 应用切到前台
//            TIMManager.getInstance().doForeground(new TIMCallBack() {
//                @Override
//                public void onError(int code, String desc) {
//                    Log.e(TAG, "doForeground err = " + code + ", desc = " + desc);
//                }
//
//                @Override
//                public void onSuccess() {
//                    Log.i(TAG, "doForeground success");
//                }
//            });
//            TUIKit.removeIMEventListener(mIMEventListener);
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
            int unReadCount = 0;
//            List<TIMConversation> conversationList = TIMManager.getInstance().getConversationList();
//            for (TIMConversation timConversation : conversationList) {
//                unReadCount += timConversation.getUnreadMessageNum();
//            }
//            TIMBackgroundParam param = new TIMBackgroundParam();
//            param.setC2cUnread(unReadCount);
//            TIMManager.getInstance().doBackground(param, new TIMCallBack() {
//                @Override
//                public void onError(int code, String desc) {
//                    Log.e(TAG, "doBackground err = " + code + ", desc = " + desc);
//                }
//
//                @Override
//                public void onSuccess() {
//                    Log.i(TAG, "doBackground success");
//                }
//            });
//            // 应用退到后台，消息转化为系统通知
//            TUIKit.addIMEventListener(mIMEventListener);
        }
        isChangingConfiguration = activity.isChangingConfigurations();
    }

//    private IMEventListener mIMEventListener = new IMEventListener() {
//        @Override
//        public void onNewMessages(List<TIMMessage> msgs) {
//            for (TIMMessage msg : msgs) {
//                // 小米手机需要在设置里面把demo的"后台弹出权限"打开才能点击Notification跳转。TIMOfflinePushNotification后续不再维护，如有需要，建议应用自己调用系统api弹通知栏消息。
//                TIMOfflinePushNotification notification = new TIMOfflinePushNotification(MyApplication.this, msg);
//                notification.doNotify(MyApplication.this, R.mipmap.ic_launcher);
//            }
//        }
//    };

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
   }
