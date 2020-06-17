package com.woaiwangpai.iwb.mvp.network.nested;

import android.graphics.Bitmap;

import java.io.File;

import io.reactivex.Observable;

/**
 * Created by Gabriel on 2018/10/15.
 * Email 17600284843@163.com
 */
public interface NestedRequest {

    /**
     * 第一次请求参数
     *
     * @param t
     * @return
     */
    Observable previousRequest(Object t);

    /**
     * 第二次请求，返回Object（主要有String）
     *
     * @param response
     */
    void secondRequest(Object response);

    /**
     * 第二次請求，返回Bitmap
     *
     * @param response
     */
    void secondRequest(Bitmap response);

    /**
     * 第二次请求，返回File
     *
     * @param response
     */
    void secondRequest(File response);


    /**
     * 网络请求之前执行的方法
     */
    void beforeRequest();


}
