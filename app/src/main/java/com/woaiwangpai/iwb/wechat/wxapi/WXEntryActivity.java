package com.woaiwangpai.iwb.wechat.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.lzy.okgo.model.Response;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.woaiwangpai.iwb.MyApplication;
import com.woaiwangpai.iwb.constant.Constant;
import com.woaiwangpai.iwb.mvp.network.callback.StatusCallBack;
import com.woaiwangpai.iwb.mvp.network.nested.NestedOkGo;
import com.woaiwangpai.iwb.utils.LogUtils;
import com.woaiwangpai.iwb.utils.ToastUtils;
import com.woaiwangpai.iwb.utils.manager.AppManager;
import com.woaiwangpai.iwb.wechat.bean.WeChatLoginBean;
import com.woaiwangpai.iwb.wechat.bean.WeiXin;
import com.woaiwangpai.iwb.wechat.share.SocialHelper;
import com.woaiwangpai.iwb.wechat.share.WXHelperActivity;
import com.woaiwangpai.iwb.wechat.share.utils.SocialUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信登陆分享回调Activity
 */
public class WXEntryActivity extends WXHelperActivity {// extends Activity implements IWXAPIEventHandler {
    private IWXAPI wxAPI= MyApplication.mWxApi;
  public static WXEntryActivity instance = null;
   //******
    @Override
    protected SocialHelper getSocialHelper() {
        return SocialUtil.INSTANCE.socialHelper;
    }
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance=this;
        AppManager.getAppManager().addActivity(this);
        //如果没回调onResp，八成是这句没有写
        //判断是否已经注册到微信
        MyApplication.mWxApi.handleIntent(getIntent(), this);
        LogUtils.e("----------------------微信---------------------");
        //分享或登录之后成功或失败调用此页
    }

    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        wxAPI.handleIntent(getIntent(),this);
        Log.i("wx","WXEntryActivity onNewIntent");
    }
    /**
     * 微信发送请求到第三方应用时，会回调到该方法
     */
    @Override
    public void onReq(BaseReq arg0) {
        LogUtils.e("wx","WXEntryActivity onReq:"+arg0);
    }
    @Override
    public void onResp(BaseResp resp){

                   int errorCode = resp.errCode;
                     switch (errorCode) {
                         case BaseResp.ErrCode.ERR_OK:
                                //用户同意
                                 String code = ((SendAuth.Resp) resp).code;
                                 LogUtils.e("用户同意");
                                break;
                        case BaseResp.ErrCode.ERR_AUTH_DENIED:
                                 //用户拒绝
                                 break;
                        case BaseResp.ErrCode.ERR_USER_CANCEL:
                                 //用户取消
                                break;
                        default:
                               break;
                       }
        LogUtils.e("微信1"+resp.errCode);

        if(resp.getType()== ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX){
            //分享
            LogUtils.e("微信分享操作1.....");
            WeiXin weiXin=new WeiXin(2,resp.errCode,"");
            EventBus.getDefault().post(weiXin);//发送信息
        }else if(resp.getType()==ConstantsAPI.COMMAND_SENDAUTH){
            //登陆
            LogUtils.e("微信登录操作.....");
            SendAuth.Resp authResp = (SendAuth.Resp) resp;
            WeiXin weiXin=new WeiXin(1,resp.errCode,authResp.code);
            EventBus.getDefault().post(weiXin);

            //拿到了微信返回的code,立马再去请求access_token
            String code = ((SendAuth.Resp) resp).code;
            LogUtils.e("code ", code);
            //就在这个地方，用网络库什么的或者自己封的网络api，发请求去咯，注意是get请求
            Map<String, String> bodyParams = new HashMap<>();
            bodyParams.put("code", code);
            bodyParams.put("from", "app");
            NestedOkGo.get()
                    .url(Constant.WEIXIN_LOGIN_GETURL)
                    .params(bodyParams)
                    .execute(new StatusCallBack() {
                        @Override
                        protected void onSimpleResponse(Response<String> response) {

                            LogUtils.e("网络响应成功response:" + response.body());//获取登录结果json
                            WeChatLoginBean bean = new Gson().fromJson(response.body(), WeChatLoginBean.class);
                            String resultMsg = bean.getMsg();
                            if (bean.getData().getOpenid() == null || bean.getData().getOpenid().equals("")) {
                                //绑定过的 返会和账号登录返回一样
//                                        LoginBean bean= new Gson().fromJson(response,LoginBean.class);
                                if (bean.getData().getUsername()!=null||!bean.getData().getUsername().equals("")){
//                                    startActivity(new Intent(WXEntryActivity.this, NewWestMainActivity.class));
                                }
                                ToastUtils.show("微信2"+resultMsg);
//                                    finish();
                            } else {
                                if (bean.getData().getOpenid() != null && !bean.getData().getOpenid().equals("")) {
                                    //未绑定过的
//                                    Intent intent = new Intent(WXEntryActivity.this, WeChatBindActivity.class);
//                                    Bundle bundle = new Bundle();
//                                    bundle.putString("openid", bean.getData().getOpenid());
//                                    bundle.putString("avatar", bean.getData().getAvatar());
//                                    bundle.putString("nickname", bean.getData().getNickname());
//                                    bundle.putString("unionid", bean.getData().getUnionid());
//                                    intent.putExtras(bundle);
//                                    startActivity(intent);
//                                        finish();
                                } else {
                                    ToastUtils.show("微信"+resultMsg);
//                                        finish();
                                }
                            }
                        }

                        @Override
                        public void onError(Response<String> response) {
                            super.onError(response);
                            ToastUtils.show("网络错误，请检查网络");
                        }
                    }).build();

        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
    }
}
