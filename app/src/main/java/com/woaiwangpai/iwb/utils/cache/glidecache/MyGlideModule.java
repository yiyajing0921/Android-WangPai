package com.woaiwangpai.iwb.utils.cache.glidecache;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.woaiwangpai.iwb.utils.cache.glidecache.config.GlideCatchConfig;


/**
 * @Author : YiYaJing
 * @Data : 2020/6/17 9:02
 * @Email : yiyajing8023@163.com
 * @Description : Glide4.9 以前创建MyAppGlideModule即GlideConfiguration 才能链式到用所有API
 * @GlideModule AN 此注解  替代之前清单文件配置    4.9没注解无效
 */
@GlideModule
public class MyGlideModule extends AppGlideModule {

    /**
     * 全局配置Glide选项
     * @param context
     * @param builder
     */
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        super.applyOptions(context,builder);
        //自定义缓存目录
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context,
                GlideCatchConfig.GLIDE_CARCH_DIR,
                GlideCatchConfig.GLIDE_CATCH_SIZE));
        //全局设置图片格式为RGB_565 非ARGB8888
        builder.setDefaultRequestOptions(new RequestOptions().format(DecodeFormat.PREFER_RGB_565));
    }

    /***
     * 注册自定义组件
     * @param context
     * @param glide
     * @param registry
     */
    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
super.registerComponents(context,glide,registry);
    }
}
