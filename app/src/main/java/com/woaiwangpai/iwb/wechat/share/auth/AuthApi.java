package com.woaiwangpai.iwb.wechat.share.auth;

import android.app.Activity;
import android.content.Intent;

import com.woaiwangpai.iwb.utils.LogUtils;


/**
 * 社会化组件基类实现
 */
public class AuthApi {

    protected Activity mActivity;

    private static int mAuthType;//1wx2wb3qq

    private static OnAuthListener mOnAuthListener;

    public AuthApi(Activity act) {
        mActivity = act;
    }

    /**
     * 设置社会化类型
     * @param authType type
     */
    protected void setAuthType(int authType) {
        mAuthType = authType;
    }

    /**
     * 获取社会化类型
     * @return 类型 {@link }
     */
    public int getAuthType(){
        return mAuthType;
    }

    /**
     * 执行登陆操作
     */
    public void doOauthVerify(){}

    /**
     * 执行登陆操作
     * @param act activity
     * @param socialType {@link }
     * @param l 回调监听
     */
    public static AuthApi doOauthVerify(Activity act, int socialType, OnAuthListener l){
        AuthApi authApi;

        switch (socialType){
            case 4:
                authApi = new AliAuth(act);
                break;
//            case 3:
//                authApi = new QQAuth(act);
//                break;
//                case 2:
//                authApi = new WeiboAuth(act);
//                break;
//            case 1:
//                authApi = new WeiXinAuth(act);
//                break;
            default:
                throw new IllegalStateException("error login type !");
        }

        authApi.setAuthType(socialType);  //调用都正常  按道理构造器时候会调用此方法 可是好像值不对
        authApi.setAuthListener(l);
        authApi.doOauthVerify();

        return authApi;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (getAuthType()){
//            case AuthType.QQ:
//                if (requestCode == Constants.REQUEST_LOGIN) {
//                    Tencent.handleResultData(data, ((QQAuth)this).getUIListener());
//                }
//                break;
//            case AuthType.WEIBO:
//                SsoHandler ssoHandler = ((WeiboAuth)this).getSsoHandler();
//                if(ssoHandler != null) {
//                    ((WeiboAuth) this).getSsoHandler().authorizeCallBack(requestCode, resultCode, data);
//                }
//                break;
            case 1:

                break;
                case 4:
                    LogUtils.e("alipay************************");
                break;
        }
    }

    public void setAuthListener(OnAuthListener l){
        mOnAuthListener = l;
    }

    /**
     * 登陆成功回调
     */
    public static void setCompleteCallBack(User user){
        if(mOnAuthListener != null){
            mOnAuthListener.onComplete(mAuthType, user);
        }
    }

    /**
     * 登陆错误回调
     */
    public static void setErrorCallBack(String error){
        if(mOnAuthListener != null){
            mOnAuthListener.onError(mAuthType, error);
        }
    }

    /**
     * 登陆取消回调
     */
    public static void setCancelCallBack(){
        if(mOnAuthListener != null){
            mOnAuthListener.onCancel(mAuthType);
        }
    }

    /**
     * 释放资源
     */
    public static void release(){
        mOnAuthListener = null;
    }

    public interface OnAuthListener {

        /**
         * 成功
         * @param type 登陆类型
         * @param user 返回实体
         */
        void onComplete(int type, User user);

        /**
         * 失败
         * @param type 登陆类型
         * @param error 失败原因
         */
        void onError(int type, String error);

        /**
         * 用户取消
         * @param type 登陆类型
         */
        void onCancel(int type);
    }
}
