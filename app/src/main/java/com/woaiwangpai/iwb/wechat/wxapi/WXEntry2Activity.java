package com.woaiwangpai.iwb.wechat.wxapi;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.lzy.okgo.model.Response;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.woaiwangpai.iwb.MyApplication;
import com.woaiwangpai.iwb.constant.Constant;
import com.woaiwangpai.iwb.mvp.network.callback.StatusCallBack;
import com.woaiwangpai.iwb.mvp.network.nested.NestedOkGo;
import com.woaiwangpai.iwb.utils.LogUtils;
import com.woaiwangpai.iwb.utils.ToastUtils;
import com.woaiwangpai.iwb.wechat.bean.WeChatLoginBean;

import java.util.HashMap;
import java.util.Map;

public class WXEntry2Activity extends Activity implements IWXAPIEventHandler {
    private static final int RETURN_MSG_TYPE_LOGIN = 1;
    private static final int RETURN_MSG_TYPE_SHARE = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //如果没回调onResp，八成是这句没有写
        MyApplication.mWxApi.handleIntent(getIntent(), this);
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq req) {
        ToastUtils.show("" + req.openId);
    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    //app发送消息给微信，处理返回消息的回调
    @Override
    public void onResp(BaseResp resp) {
        LogUtils.i("wexin", resp.errStr);
        LogUtils.i("wexin错误码 : ", resp.errCode + "");
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                if (RETURN_MSG_TYPE_SHARE == resp.getType())
                    ToastUtils.show("分享失败");
                else
                    ToastUtils.show("登录失败");
                break;
            case BaseResp.ErrCode.ERR_OK:
                switch (resp.getType()) {
                    case RETURN_MSG_TYPE_LOGIN:
                        //拿到了微信返回的code,立马再去请求access_token
                        String code = ((SendAuth.Resp) resp).code;
                        LogUtils.i("code ", code);
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
                                        LogUtils.e("网络响应成功response:" + response);//获取登录结果json
                                        WeChatLoginBean bean = new Gson().fromJson(response.body(), WeChatLoginBean.class);
                                        String resultMsg = bean.getMsg();
                                        if (bean.getData().getOpenid() == null || bean.getData().getOpenid().equals("")) {
                                            //绑定过的 返会和账号登录返回一样
//                                        LoginBean bean= new Gson().fromJson(response,LoginBean.class);
                                            if (bean.getData().getUsername()!=null||!bean.getData().getUsername().equals("")){
                                                //TODO：登录到首页
//                                                startActivity(new Intent(WXEntry2Activity.this, NewWestMainActivity.class));
                                            }
                                            ToastUtils.show(resultMsg);
//                                    finish();
                                        } else {
                                            if (bean.getData().getOpenid() != null && !bean.getData().getOpenid().equals("")) {

                                                //TODO：未绑定过的
//                                                Intent intent = new Intent(WXEntry2Activity.this, WeChatBindActivity.class);
//                                                Bundle bundle = new Bundle();
//                                                bundle.putString("openid", bean.getData().getOpenid());
//                                                bundle.putString("avatar", bean.getData().getAvatar());
//                                                bundle.putString("nickname", bean.getData().getNickname());
//                                                bundle.putString("unionid", bean.getData().getUnionid());
//                                                intent.putExtras(bundle);
//                                                startActivity(intent);
//                                        finish();
                                            } else {
                                                ToastUtils.show(resultMsg);
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
                        break;
                    case RETURN_MSG_TYPE_SHARE:
                        ToastUtils.show("微信分享成功");
//                        finish();
                        break;
                    default:
                        break;
                }
        }
        finish();
    }
/*
    private IWXAPI wxAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        wxAPI = WXAPIFactory.createWXAPI(this,Constant.WEIXIN_APP_ID,true);
        wxAPI.registerApp(Constant.WEIXIN_APP_ID);
        wxAPI.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        wxAPI.handleIntent(getIntent(),this);
        Log.i("ansen","WXEntry2Activity onNewIntent");
    }

    @Override
    public void onReq(BaseReq arg0) {
        Log.i("ansen","WXEntry2Activity onReq:"+arg0);
    }

    @Override
    public void onResp(BaseResp resp){
        if(resp.getType()== ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX){//分享
            Log.i("ansen","微信分享操作.....");
            WeiXin weiXin=new WeiXin(2,resp.errCode,"");
            EventBus.getDefault().post(weiXin);
        }else if(resp.getType()==ConstantsAPI.COMMAND_SENDAUTH){//登陆
            Log.i("ansen", "微信登录操作.....");
            SendAuth.Resp authResp = (SendAuth.Resp) resp;
            WeiXin weiXin=new WeiXin(1,resp.errCode,authResp.code);
            EventBus.getDefault().post(weiXin);
        }
        finish();
    }
 */
}
