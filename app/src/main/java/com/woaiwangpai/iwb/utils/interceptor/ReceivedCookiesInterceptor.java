package com.woaiwangpai.iwb.utils.interceptor;

import android.content.Context;
import android.content.SharedPreferences;

import com.woaiwangpai.iwb.utils.LogUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;


/**
 * @Author : YiYaJing
 * @Data : 2020/6/17 8:56
 * @Email : yiyajing8023@163.com
 * @Description :cookie拦截器
 */
public class ReceivedCookiesInterceptor implements Interceptor {

    private static final String TAG = "ReceivedCookiesInterceptor";
    private Context context;

    public ReceivedCookiesInterceptor(Context context) {
        super();
        this.context = context;

    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Response originalResponse = chain.proceed(chain.request());
        //这里获取请求返回的cookie
        if (!originalResponse.headers("set-cookie").isEmpty()) {
//            final StringBuffer cookieBuffer = new StringBuffer();
//            //最近在学习RxJava,这里用了RxJava的相关API大家可以忽略,用自己逻辑实现即可.大家可以用别的方法保存cookie数据
//            Observable.from(originalResponse.headers("Set-Cookie"))
//                    .map(new Func1<String, String>() {
//                        @Override
//                        public String call(String s) {
//                            String[] cookieArray = s.split(";");
//                            return cookieArray[0];
//                        }
//                    })
//                    .subscribe(new Action1<String>() {
//                        @Override
//                        public void call(String cookie) {
//                            cookieBuffer.append(cookie).append(";");
//                        }
//                    });
            String cookie = originalResponse.header("set-cookie").split(";")[0];
            SharedPreferences sharedPreferences = context.getSharedPreferences("cookie", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("cookie", cookie);
            LogUtils.i(TAG, "cookie*****" + cookie);
            editor.commit();
        }

        return originalResponse;
    }
}
