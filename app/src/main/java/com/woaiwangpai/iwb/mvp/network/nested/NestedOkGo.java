package com.woaiwangpai.iwb.mvp.network.nested;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.convert.BitmapConvert;
import com.lzy.okgo.convert.Converter;
import com.lzy.okgo.convert.FileConvert;
import com.lzy.okgo.convert.StringConvert;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okgo.request.PostRequest;
import com.lzy.okrx2.adapter.ObservableBody;
import com.woaiwangpai.iwb.utils.LogUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;

/**
 * Created by Gabriel on 2018/10/15.
 * Email 17600284843@163.com
 */
@SuppressLint("CheckResult")
public class NestedOkGo {

    private static String firstUrl;//第一次请求的URL
    private boolean isNested = false;
    /**
     * 添加请求头
     */
    public static HttpHeaders httpHeaders = new HttpHeaders();
    /**
     * 请求参数
     */
    private static Map<String, String> httpParams = new HashMap<>();
    private static Map<String, Object> fileParams = new HashMap<>();
    private static MultipartBody.Builder builder = new MultipartBody.Builder();
    private Callback callback;//接口回调
    private NestedRequest onNestedRequest;
    private Context mContext;
    //设置返回参数
    public static final int TYPE_STRING = 0;
    public static final int TYPE_BITMAP = 1;
    public static final int TYPE_FILE = 2;
    public int responseType = TYPE_STRING;//默认String类型
    public boolean isHaveFile = false;

    //请求类型
    public static RequestType requestType = RequestType.TYPE_POST;
    private boolean isList;

    public static NestedOkGo getInstanse() {
        return new NestedOkGo();
    }

    public static NestedOkGo post() {
        requestType = RequestType.TYPE_POST;
        return new NestedOkGo();
    }

    public static NestedOkGo post(String url) {
        requestType = RequestType.TYPE_POST;
        return new NestedOkGo().url(url);
    }

    public static NestedOkGo post(Map<String, String> param, String url) {
        requestType = RequestType.TYPE_POST;
        return new NestedOkGo().params(param).url(url);
    }

    public static NestedOkGo get() {
        requestType = RequestType.TYPE_GET;
        return new NestedOkGo();
    }

    public static NestedOkGo get(Map<String, String> param, String url) {
        requestType = RequestType.TYPE_GET;
        return new NestedOkGo().params(param).url(url);
    }

    /**
     * 添加请求头
     *
     * @param header 请求头
     */
    public NestedOkGo addHeaders(HttpHeaders header) {
        httpHeaders = header;
        return this;
    }

    /**
     * 初始化OKGo
     *
     * @param app
     */
    public void init(Application app) {
        mContext = app;
        OkGo.getInstance().init(app);
    }

    public void init(OkHttpClient okHttpClient, Application app) {
        mContext = app;
        OkGo.getInstance().setOkHttpClient(okHttpClient).init(app);
    }


    /**
     * 设置参数
     *
     * @param key
     * @param value
     * @return
     */
    public NestedOkGo params(String key, String value) {
        httpParams.put(key, value);
        return this;
    }

    /**
     * 设置任意参数
     *
     * @param params
     * @param isFile
     * @return
     */
    public NestedOkGo params(Map<String, Object> params, boolean isFile) {
        isHaveFile = isFile;
        if (isFile) {
            builder.setType(MultipartBody.FORM);
        }//传输类型
        fileParams = params;
        return this;
    }

    public NestedOkGo params(Map<String, String> value) {
        httpParams = value;
        return this;
    }


    /**
     * 设置是否嵌套
     *
     * @param isNested
     * @return
     */
    public NestedOkGo setNested(boolean isNested) {
        this.isNested = isNested;
        return this;
    }

    public NestedOkGo executeNested(NestedRequest onNestedRequest) {
        this.onNestedRequest = onNestedRequest;
        return this;
    }


    /**
     * 设置URL
     *
     * @param first_url
     * @return
     */
    public NestedOkGo url(String first_url) {
        firstUrl = first_url;
        return this;
    }


    /**
     * 加载独立接口
     *
     * @param callback
     * @return
     */
    public NestedOkGo execute(Callback callback) {
        this.callback = callback;
        return this;
    }

    public NestedOkGo setResponseType(int responseType) {
        this.responseType = responseType;
        return this;
    }


    /**
     * 网络请求创建过程
     */
    public void build() {
        if (isNested) {//嵌套加载数据
            Observable<String> observable;
            if (httpHeaders != null && httpHeaders.toString().length() > 0) {
                observable = obtainObservable(firstUrl, httpHeaders, httpParams);
            } else {
                observable = httpParams.size() != 0 ? obtainObservable(firstUrl, httpParams) : obtainObservable(firstUrl);
            }
            switch (responseType) {
                case TYPE_STRING:
                    subscribeWithString(observable);
                    break;
                case TYPE_BITMAP:
                    subscribeWithBitmap(observable);
                    break;
                case TYPE_FILE:
                    subscribeWithFile(observable);
                    break;
            }

        } else {//正常加载数据
            switch (requestType) {
                case TYPE_GET:
                    if (isHaveFile && fileParams.size() != 0) {
//                        for (Map.Entry<String, Object> stringObjectEntry : fileParams.entrySet()) {//没有判空
//                            String key = stringObjectEntry.getKey();
//                            Object value = stringObjectEntry.getValue();
//                            if (value instanceof File) {//如果请求的值是文件
//                                File file = (File) value;
//                                //MediaType.parse("application/octet-stream")以二进制的形式上传文件
//                                builder.addFormDataPart("jokeFiles", ((File) value).getName(), RequestBody.create(MediaType.parse("application/octet-stream"), file));
//                            } else {//如果请求的值是string类型
//                                if(!"url".equals(key)){
//                                    builder.addFormDataPart(key, value.toString());
//                                }
//                            }
//                        }
                        GetRequest<Object> objectGetRequest;
                        if (httpHeaders != null && httpHeaders.toString().length() > 0) {
                            objectGetRequest = obtainGetObjectRequest(firstUrl, fileParams, httpHeaders);
                        } else {
                            objectGetRequest = obtainGetObjectRequest(firstUrl, fileParams);
                        }
                        objectGetRequest.execute(callback);
                    } else {
                        GetRequest<Object> getRequest;
                        if (httpHeaders != null && httpHeaders.toString().length() > 0) {
                            getRequest = obtainGetRequest(firstUrl, httpParams, httpHeaders);
                        } else {
                            getRequest = obtainGetRequest(firstUrl, httpParams);
                        }
                        getRequest.execute(callback);
                    }
                    break;
                case TYPE_POST:
                    if (isHaveFile && fileParams.size() != 0) {
                        PostRequest<Object> objectPostRequest;
                        if (httpHeaders != null && httpHeaders.toString().length() > 0) {
                            objectPostRequest = obtainPostObjectRequest(firstUrl, fileParams, httpHeaders);
                        } else {
                            objectPostRequest = obtainPostObjectRequest(firstUrl, fileParams);
                        }
                        objectPostRequest.execute(callback);
                    } else {
                        PostRequest<Object> postRequest;
                        if (httpHeaders != null && httpHeaders.get("client") != null && "android".equals(httpHeaders.get("client"))) {
                            postRequest = obtainPostRequest(firstUrl, httpParams, httpHeaders);
                        } else {
                            postRequest = obtainPostRequest(firstUrl, httpParams);
                        }
                        postRequest.execute(callback);
                    }
                    break;
                default:
                    Log.i("走到这里了", "build: ");
                    break;
            }
        }
    }

    /**
     * 嵌套请求字符串类型
     *
     * @param observable
     */
    private void subscribeWithString(Observable<String> observable) {
        LogUtils.e("subscribeWithString");
        observable.flatMap((Function<String, ObservableSource<String>>) o -> onNestedRequest.previousRequest(o)).doOnSubscribe(disposable -> onNestedRequest.beforeRequest()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(s -> onNestedRequest.secondRequest(s), throwable -> LogUtils.e("throwable-->" + throwable.toString()));
    }

    /**
     * 嵌套请求Bitmap类型
     *
     * @param observable
     */
    private void subscribeWithBitmap(Observable<String> observable) {
        LogUtils.e("subscribeWithBitmap");
        observable.flatMap((Function<String, ObservableSource<Bitmap>>) o -> onNestedRequest.previousRequest(o)).doOnSubscribe(disposable -> onNestedRequest.beforeRequest()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(bitmap -> onNestedRequest.secondRequest(bitmap), throwable -> LogUtils.e("throwable-->" + throwable.toString()));
    }

    /**
     * 嵌套请求File类型
     *
     * @param observable
     */
    private void subscribeWithFile(Observable<String> observable) {
        LogUtils.e("subscribeWithFile");
        observable.flatMap((Function<String, ObservableSource<File>>) o -> onNestedRequest.previousRequest(o)).doOnSubscribe(disposable -> onNestedRequest.beforeRequest()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(file -> onNestedRequest.secondRequest(file), throwable -> LogUtils.e("throwable-->" + throwable.toString()));
    }

    /**
     * 只添加路径
     *
     * @param url
     * @param <T>
     * @return
     */
    public static <T> Observable<T> obtainObservable(String url) {
        return OkGo.<T>post(url).converter((Converter<T>) new StringConvert()).adapt(new ObservableBody<T>());
    }

    /**
     * 路径和参数
     *
     * @param url
     * @param map
     * @param <T>
     * @return
     */
    public static <T> Observable<T> obtainObservable(String url, Map<String, String> map) {
        return OkGo.<T>post(url).params(map).converter((Converter<T>) new StringConvert()).adapt(new ObservableBody<T>());
    }

    /**
     * 路径、参数和请求头
     *
     * @param url
     * @param headers
     * @param map
     * @param <T>
     * @return
     */
    public static <T> Observable<T> obtainObservable(String url, HttpHeaders headers, Map<String, String> map) {
        return OkGo.<T>post(url).headers(headers).params(map).converter((Converter<T>) new StringConvert()).adapt(new ObservableBody<T>());
    }

    public static Observable<Bitmap> bitmapObservable(String url) {
        Observable<Bitmap> adapt = OkGo.<Bitmap>get(url).converter(new BitmapConvert()).adapt(new ObservableBody<>());
        return adapt;
    }

    public static Observable<File> fileObservable(String url) {
        return OkGo.<File>post(url).converter(new FileConvert()).adapt(new ObservableBody<File>());
    }

    public static <T> PostRequest<T> obtainPostRequest(String firstUrl) {
        return OkGo.post(firstUrl);
    }

    public static <T> PostRequest<T> obtainPostRequest(String firstUrl, Map<String, String> map) {
        return OkGo.<T>post(firstUrl).params(map);
    }

    public static <T> PostRequest<T> obtainPostRequest(String firstUrl, Map<String, String> map, HttpHeaders headers) {
        return OkGo.<T>post(firstUrl).headers(headers).params(map);
    }

    public static <T> PostRequest<T> obtainPostObjectRequest(String firstUrl, Map<String, Object> map) {
        return OkGo.<T>post(firstUrl).params((HttpParams) map);
    }

    public static <T> PostRequest<T> obtainPostObjectRequest(String firstUrl, Map<String, Object> map, HttpHeaders headers) {
        return OkGo.<T>post(firstUrl).headers(headers).params((HttpParams) map);
    }

    public static <T> GetRequest<T> obtainGetRequest(String url) {
        return OkGo.get(url);
    }

    public static <T> GetRequest<T> obtainGetRequest(String url, Map<String, String> map) {
        return OkGo.<T>get(url).params(map);
    }

    public static <T> GetRequest<T> obtainGetRequest(String url, Map<String, String> map, HttpHeaders headers) {
        return OkGo.<T>get(url).headers(headers).params(map);
    }

    public static <T> GetRequest<T> obtainGetObjectRequest(String url, Map<String, Object> map) {
        return OkGo.<T>get(url).params((HttpParams) map);
    }

    public static <T> GetRequest<T> obtainGetObjectRequest(String url, Map<String, Object> map, HttpHeaders headers) {
        return OkGo.<T>get(url).headers(headers).params((HttpParams) map);
    }


    public enum RequestType {
        TYPE_POST, TYPE_GET
    }
}
