package com.woaiwangpai.iwb.utils.interceptor;

import android.content.Context;
import android.content.SharedPreferences;

import com.woaiwangpai.iwb.utils.LogUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @Author : YiYaJing
 * @Data : 2020/6/17 8:54
 * @Email : yiyajing8023@163.com
 * @Description : 拦截器
 */

public class AddCookiesInterceptor implements Interceptor {

    private static final String TAG = "AddCookiesInterceptor";
    private Context context;

    public AddCookiesInterceptor(Context context) {
        super();
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        final Request.Builder builder = chain.request().newBuilder();
        SharedPreferences sharedPreferences = context.getSharedPreferences("cookie", Context.MODE_PRIVATE);
//        //最近在学习RxJava,这里用了RxJava的相关API大家可以忽略,用自己逻辑实现即可
//        Observable.just(sharedPreferences.getString("cookie", ""))
//                .subscribe(new Action1<String>() {
//                    @Override
//                    public void call(String cookie) {
//                        //添加cookie
//                        builder.addHeader("Cookie", cookie);
//                    }
//                });
        String cookie = sharedPreferences.getString("cookie", "");
        builder.addHeader("Cookie", cookie);
        LogUtils.i(TAG, "cookie*****" + cookie);
        return chain.proceed(builder.build());
    }
}
