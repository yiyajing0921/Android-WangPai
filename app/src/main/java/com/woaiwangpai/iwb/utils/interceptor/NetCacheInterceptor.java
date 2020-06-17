package com.woaiwangpai.iwb.utils.interceptor;

import android.text.TextUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @Author : YiYaJing
 * @Data : 2020/6/17 8:55
 * @Email : yiyajing8023@163.com
 * 描述 构造拦截器:使用拦截器添加缓存时间会导致所有接口都会被强制缓存，另外一种方式是对特定的接口设置缓存机制。
 */
public class NetCacheInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response originResponse = chain.proceed(request);

        if (!TextUtils.isEmpty(request.header("Cache-Control"))) {
            //设置响应的缓存时间，即设置Cache-Control头，并移除pragma消息头，因为pragma也是控制缓存的一个消息头属性

//    must-revalidate，一旦缓存过期，必须向服务器重新请求，不得使用过期内容
//    no-cache，不使用缓存
//    no-store，不缓存请求的响应
//    no-transform，不得对响应进行转换或转变
//    public，任何响应都可以被缓存，即使响应默认是不可缓存或仅私有缓存可存的
//    private，表明响应只能被单个用户缓存，不能作为共享缓存（即代理服务器不能缓存它）
//    proxy-revalidate，与must-revalidate类似，但它仅适用于共享缓存（例如代理），并被私有缓存忽略。
//    max-age，缓存的有效时间
//    s-maxage，指定响应在公共缓存中的最大存活时间，它覆盖max-age和expires字段。
            originResponse = originResponse.newBuilder()
                    .removeHeader("pragma")
                    .header("Cache-Control", "max-age=60")
                    .build();
        }

        return originResponse;
    }
}
//    //使用拦截器
//    private OkHttpManager() {
//        // 缓存目录
//        File file = new File(Environment.getExternalStorageDirectory(), "a_cache");
//        // 缓存大小
//        int cacheSize = 10 * 1024 * 1024;
//        client = new OkHttpClient.Builder()
//                .cache(new Cache(file, cacheSize))
//                .addNetworkInterceptor(new NetCacheInterceptor())//使用拦截器
//                .connectTimeout(10, TimeUnit.SECONDS)
//                .writeTimeout(10, TimeUnit.SECONDS)
//                .readTimeout(10, TimeUnit.SECONDS)
//                .build();
//    }

