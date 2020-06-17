package com.woaiwangpai.iwb.mvp.network.callback;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.woaiwangpai.iwb.mvp.network.nested.NestedRequest;
import com.woaiwangpai.iwb.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;

import io.reactivex.Observable;


/**
 * Created by Gabriel on 2018/10/15.
 * Email 17600284843@163.com
 */
public abstract class NestedCallback<T, E> implements NestedRequest {

    private static final String TAG = "NestedCallback";

    public NestedCallback() {
    }


    @Override
    public Observable previousRequest(Object o) {
        return onSuccessResponse(cast(o));
    }

    @Override
    public void secondRequest(Object response) {
        //onComplete();
        try {
            onSecondResponse(casts(response));
        } catch (Exception e) {
            onException(e);
        }
    }

    /**
     * 第二次加载成功
     */
//    //protected void onComplete() {
//        MapUtils.clear();//清除map存储缓存
//    }

    @Override
    public void beforeRequest() {
        // load before the network starts
    }


    protected void onException(Exception e) {
        Log.e(TAG, e.toString());
    }


    protected abstract Observable<T> onSuccessResponse(T response);

    protected abstract void onSecondResponse(E response);

    public T cast(Object response) {
        return (T) response;
    }

    public E casts(Object response) {
        return (E) response;
    }


    /**
     * 无需创建bean类，直接解析，适合用于只返status和msg的接口回调
     * 失败是默认toast提示msg字段
     *
     * @param jsonResponse json字符串，解密后的
     * @param handler      成功时的回调
     */
    public void simpleHandle(String jsonResponse, OnSuccessHandler handler) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonResponse);
            if (jsonObject.optInt("status") == 1) {
                handler.onSuccess(jsonObject, jsonObject.optString("msg"));
            } else {
//                ToastUtils.showToast(App.getInstance(), jsonObject.optString("msg"));
            }
        } catch (JSONException e) {
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
    public void simpleHandle(Context context, String json, StatusCallBack.OnSuccessListener listener) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(json);
            if (jsonObject.optInt("status") == 1) {
                listener.onSuccess(jsonObject.optString("msg"));
            } else if (jsonObject.optInt("status") == 2) {
                onPageMinus();
                //NotLoginUtils.setLoginDialog(context);
            } else {
                onPageMinus();
                ToastUtils.show(jsonObject.optString("msg"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            onException(e);
        }
    }

    protected void onPageMinus() {

    }

    ;


    public boolean isStatus(String response) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(response);
            if (jsonObject.optInt("status") == 1) {
                return true;
            } else {
//                ToastUtils.showToast(App.getInstance(), jsonObject.optString("msg"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }


    public interface OnSuccessHandler {
        void onSuccess(JSONObject obj, String msg);
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
}
