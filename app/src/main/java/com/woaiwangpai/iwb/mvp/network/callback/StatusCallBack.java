package com.woaiwangpai.iwb.mvp.network.callback;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.kingja.loadsir.core.LoadService;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.woaiwangpai.iwb.MyApplication;
import com.woaiwangpai.iwb.constant.Constant;
import com.woaiwangpai.iwb.mvp.network.callback.loadsir.ErrorCallBack;
import com.woaiwangpai.iwb.mvp.network.callback.loadsir.TimeoutCallback;
import com.woaiwangpai.iwb.utils.LogUtils;
import com.woaiwangpai.iwb.utils.NetUtils;
import com.woaiwangpai.iwb.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by Gabriel on 2018/7/20.
 * Email 17600284843@163.com
 */

public abstract class StatusCallBack extends StringCallback {

    private LoadService mService;
    private boolean isShowErrorLayout;//true:列表有数据，不需要加载布局；false:列表无数据，需要加载布局

    /**
     * 简单接口加载时使用
     */
    public StatusCallBack() {
        this.mService = null;
    }

    /**
     * 没有上拉加载时需要替换View的情况下使用
     */
    public StatusCallBack(LoadService service) {
        this.mService = service;
        this.isShowErrorLayout = false;
    }

    /**
     * 有上拉加载时需要替换View的情况下使用
     *
     * @param isShowErrorLayout 表示当前页面是否已经加载有数据
     */
    public StatusCallBack(LoadService service, boolean isShowErrorLayout) {
        this.mService = service;
        this.isShowErrorLayout = isShowErrorLayout;
    }


    @Override
    public void onError(Response<String> response) {
        onCompleteLoading();
        LogUtils.e(String.format("onError--->%s", response.getException()));
        if (isServer()) {
            displayOnError();
        } else {
            toastOnError("服务器异常，访问失败");
        }
        onException(response.message());
        if (response.code() == 404) {
//            域名错误
//            http://api.alpha.fytpkk.cn   测试用的域名  https://api.fytpkk.cn   正式用的域名
            Constant.BASE_URL = "https://api.fytpkk.cn";
        }
    }


    @Override
    public void onSuccess(Response<String> response) {
        onCompleteLoading();
        try {
            LogUtils.e(String.format("response--->%s", response.body()));
//            LogUtils.e(String.format("decode--->%s", decode(response.body())));
            if (mService != null) mService.showSuccess();
            onSimpleResponse(response);
        } catch (Exception e) {
            LogUtils.e(String.format("response-error--->%s", response.body()));
            onException(e.toString());
        }
    }

//    public String decode(String response) {
//        return CodeUtil.Decode(response);
//    }

    public <T> T parse(String jsonString, Class<T> c) {
        T t = JSON.parseObject(jsonString, c);
//        changedDoubleQuo(t);
        return t;
    }

    /**
     * 更换接口中英文双引号问题
     *
     * @param t
     * @param <T>
     */
//    private <T> void changedDoubleQuo(T t) {
//        String doubleQuo = App.getInstance().getResources().getString(R.string.double_quotation);
//        String str_filter = App.getInstance().getResources().getString(R.string.str_filter);
//        Field[] declaredFields = t.getClass().getDeclaredFields();
//        for (int i = 0; i < declaredFields.length - 2; i++) {
//            Field field = declaredFields[i];
//            field.setAccessible(true);
//            try {
//                Object object = field.get(t);
//                if (object instanceof String) {
//                    setFieldData(t, doubleQuo, str_filter, field, object);
//                    setFieldData(t, "&quot;", str_filter, field, object);
//                } else if (object instanceof List) {
//                    List list = (List) object;
//                    for (Object o : list) {
//                        changedDoubleQuo(o);
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

    /**
     * 替换字段参数
     */
    private <T> void setFieldData(T t, String doubleQuo, String str_filter, Field field, Object object) throws IllegalAccessException {
        if (object.toString().contains(doubleQuo)) {
            String replaceAll = ((String) object).replaceAll(doubleQuo, str_filter);
            field.setAccessible(true);
            field.set(t, replaceAll);
        }
    }

    /**
     * 加载简单接口是调用
     *
     * @param bean
     * @return
     */
    protected boolean isStatusTrue(Object bean) {
        try {
            Method getStatus = bean.getClass().getDeclaredMethod("getStatus");
            getStatus.setAccessible(true);
            int status = (int) getStatus.invoke(bean);
            if (status == 1) {
                return true;
            } else if (status == 2) {
                ToastUtils.show("您的设备已在其他设备上登录，请重新登录");
            } else if (status == 3) {
                revokeException(bean);
            } else {
                loadException(bean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 加载需要View变换（断网、服务器异常、空数据）时调用
     *
     * @param activity
     * @param bean
     * @return
     */
    protected boolean isStatusTrue(Context activity, Object bean) {
        try {
            Method getStatus = bean.getClass().getDeclaredMethod("getStatus");
            getStatus.setAccessible(true);
            int status = (int) getStatus.invoke(bean);
            if (status == 1) {
                return true;
            } else if (status == 2) {
                onPageMinus();
                //NotLoginUtils.setLoginDialog(activity);
            } else if (status == 3) {
                revokeException(bean);
            } else {
                onPageMinus();
                loadException(bean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 加载异常
     */
    private void loadException(Object bean) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (isServer()) {
            //mService.showCallback(TimeoutCallback.class);
        } else {
            Method getMsg = bean.getClass().getDeclaredMethod("getMsg");
            getMsg.setAccessible(true);
            String msg = (String) getMsg.invoke(bean);
            toastOnError(msg);
        }
    }

    /**
     * 加载异常
     */
    private void revokeException(Object bean) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (isServer()) {
            //mService.showCallback(FinancialCallback.class);
        } else {
            Method getMsg = bean.getClass().getDeclaredMethod("getMsg");
            getMsg.setAccessible(true);
            String msg = (String) getMsg.invoke(bean);
            toastOnError(msg);
        }
    }

    /**
     * 显示错误
     *
     * @param msg
     */
    protected void toastOnError(String msg) {
        String showMessage = NetUtils.isNetworkAvailable(MyApplication.getInstance()) ? msg : "网络访问失败，请检查网络连接";//true:其他原因；false:网络错误
        ToastUtils.show(showMessage);
    }

    /**
     * 错误显示视图
     */
    private void displayOnError() {
        if (NetUtils.isNetworkAvailable(MyApplication.getInstance())) {
            mService.showCallback(TimeoutCallback.class);
        } else {
            mService.showCallback(ErrorCallBack.class);
        }
    }

    protected abstract void onSimpleResponse(Response<String> response);

    /**
     * 停止Loading
     */
    protected void onCompleteLoading() {
    }

    /**
     * 错误集合方法（包括解析错误、网络异常）
     *
     * @param errorLog
     */
    protected void onException(String errorLog) {
        onPageMinus();
        LogUtils.e(String.format("errorLog--->%s", errorLog));
    }

    /**
     * 上拉加载失败时需要将页数减1
     */
    protected void onPageMinus() {

    }

    /**
     * 判断是否简单的网络加载接口
     *
     * @return
     */
    private boolean isServer() {
        return mService != null && !isShowErrorLayout;
    }

    @Override
    public void onFinish() {
        //do something
    }

    /**
     * 判断是否有数据，没有就直接切换成空布局，有就切换回正常布局
     */
    protected boolean hasData(List been) {
        if (been == null || been.size() <= 0) {
            if (!isShowErrorLayout) {
                //mService.showCallback(EmptyCallback.class);
                onNoData();
            }
            return false;
        } else {
            return true;
        }
    }

    protected void onNoData() {

    }


    /**
     * status 为1时的回调函数
     */
    public interface OnSuccessListener {
        void onSuccess(String msg);
    }

    public interface OnSuccessHandler {
        void onSuccess(JSONObject obj, String msg);
    }

//    /**
//     * 无需创建bean类，直接解析，适合用于只返status和msg的接口回调
//     * 失败是默认toast提示msg字段
//     *
//     * @param response json字符串，解密后的
//     * @param listener 成功时的回调
//     */
    public void simpleHandle(Response<String> response, OnSuccessListener listener) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(response.body());
            if (jsonObject.optInt("status") == 1) {
                listener.onSuccess(jsonObject.optString("msg"));
            } else {
                ToastUtils.show(jsonObject.optString("msg"));
            }
        } catch (JSONException e) {
            LogUtils.e("response : " + response);
//            LogUtils.e("decode : " + decode(response.body()));
            LogUtils.e(e.getClass().getSimpleName());
            LogUtils.e(e.getMessage());
            toastOnError("服务器异常，访问失败");
            e.printStackTrace();
        }
    }

    /**
     * 无需创建bean类，直接解析，适合用于只返status和msg的接口回调
     * 失败是默认toast提示msg字段
     *
     * @param json     json字符串，解密后的
     * @param listener 成功时的回调
     */
    public void simpleHandle(Context context, String json, OnSuccessListener listener) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(json);
            if (jsonObject.optInt("status") == 1) {
                listener.onSuccess(jsonObject.optString("msg"));
            } else if (jsonObject.optInt("status") == 2) {
                onPageMinus();
                //NotLoginUtils.setLoginDialog(context);
            } else if (jsonObject.optInt("status") == 3) {
                revokeException(jsonObject);
            } else {
                onPageMinus();
                ToastUtils.show(jsonObject.optString("msg"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            onException(e.toString());
        }
    }


    /**
     * 无需创建bean类，直接解析，适合用于只返status和msg的接口回调
     * 失败是默认toast提示msg字段
     *
     * @param json    json字符串，解密后的
     * @param handler 成功时的回调
     */
    public void simpleHandle(Context context, String json, OnSuccessHandler handler) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(json);
            if (jsonObject.optInt("status") == 1) {
                handler.onSuccess(jsonObject, jsonObject.optString("msg"));
            } else if (jsonObject.optInt("status") == 2) {
                onPageMinus();
                //NotLoginUtils.setLoginDialog(context);
            } else {
                onPageMinus();
                ToastUtils.show(jsonObject.optString("msg"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            onException(e.toString());
        }
    }

    /**
     * 特殊情况下所传递内容需要判断特定参数值时调用
     *
     * @param response
     * @param param
     * @param listener
     */
    public void specialHandle(Context context, String response, int param, OnSpcialHandler listener) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(response);
            if (jsonObject.optInt("status") == 1) {
                listener.onSpcial(jsonObject, jsonObject.optString("msg"));
            } else if (jsonObject.optInt("status") == 2) {
                //NotLoginUtils.setLoginDialog(context);
            } else if (jsonObject.optInt("status") == param) {
                listener.onSpcialMsg(jsonObject.optString("msg"));
            } else {
                ToastUtils.show(jsonObject.optString("msg"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * status可能为其他值时调用
     */
    public interface OnSpcialHandler {
        void onSpcial(JSONObject jsonObject, String msg);

        void onSpcialMsg(String msg);
    }

}
