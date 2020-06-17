package com.woaiwangpai.iwb.wechat.share;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.woaiwangpai.iwb.utils.LogUtils;
import com.woaiwangpai.iwb.wechat.bean.WeiXin;
import com.woaiwangpai.iwb.wechat.wxapi.WXEntryActivity;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by arvinljw on 2018/9/27 15:06
 * Function：
 * Desc：
 */
public abstract class WXHelperActivity extends AppCompatActivity implements IWXAPIEventHandler {
    private IWXAPI api;
    private SocialHelper socialHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        socialHelper = getSocialHelper();
        if (socialHelper == null) {
            return;
        }

        String wxAppId = socialHelper.getBuilder().getWxAppId();
        api = WXAPIFactory.createWXAPI(this, wxAppId, true);
        api.registerApp(wxAppId);

        api.handleIntent(getIntent(), this);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }
    /**
     * 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
     */
    @Override
    public void onReq(BaseReq baseReq) {
        LogUtils.e("wx","WXEntryActivity onReq:"+baseReq);
    }

    @Override
    public void onResp(BaseResp baseResp) {

        LogUtils.e("WXEntryActivity", baseResp.errCode + baseResp.errStr);
        int errorCode = baseResp.errCode;
        LogUtils.e(baseResp.errStr);
        //登录
        switch (errorCode) {
            case BaseResp.ErrCode.ERR_OK:
                //用户同意
                String code = ((SendAuth.Resp) baseResp).code;
                WeiXin weiXin=new WeiXin(1,baseResp.errCode,code);
                EventBus.getDefault().post(weiXin);
                LogUtils.e("用户同意登录");
                WXEntryActivity.instance.finish();//ADD
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                //用户拒绝
                LogUtils.e("用户拒绝");
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                //用户取消
                LogUtils.e("用户取消");
                break;
            default:
                break;
        }
/****************----------------------------********************/
        if (socialHelper == null) {
            return;
        }
        if (baseResp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {
            //登录
            LogUtils.e("wx登录");

            if (baseResp.errCode == BaseResp.ErrCode.ERR_OK) {
                //ADD
                SendAuth.Resp authResp = (SendAuth.Resp) baseResp;
                WeiXin weiXin=new WeiXin(1,baseResp.errCode,authResp.code);
                EventBus.getDefault().post(weiXin);
                //
                String code = ((SendAuth.Resp) baseResp).code;
                socialHelper.sendAuthBackBroadcast(this, code);
                WXEntryActivity.instance.finish();//ADD
            } else {
                socialHelper.sendAuthBackBroadcast(this, null);
                WXEntryActivity.instance.finish();//ADD
            }
        } else if (baseResp.getType() == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX) {
            //分享
            LogUtils.e("wx分享");
            //ADD
            WeiXin weiXin=new WeiXin(2,baseResp.errCode,"");
            EventBus.getDefault().post(weiXin);//发送信息

            if (baseResp.errCode == BaseResp.ErrCode.ERR_OK) {
                socialHelper.sendShareBackBroadcast(this, true);
                WXEntryActivity.instance.finish();//ADD
            } else {
                socialHelper.sendShareBackBroadcast(this, false);
                WXEntryActivity.instance.finish();//ADD
            }
        }
        onBackPressed();
    }

    protected abstract SocialHelper getSocialHelper();
}
