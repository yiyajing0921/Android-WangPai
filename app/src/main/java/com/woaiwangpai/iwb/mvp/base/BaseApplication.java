package com.woaiwangpai.iwb.mvp.base;

import android.app.Application;

/**
 * @Author : YiYaJing
 * @Data : 2020/6/13 11:11
 * @Email : yiyajing8023@163.com
 * @Description : 尽量不用静态
 */
public abstract class BaseApplication extends Application {
    public static BaseApplication sApplication;
    //若Activity的context 会有内存泄漏 而 Application在整个生命周期不会
    @Override
    public void onCreate() {
        super.onCreate();
        sApplication=this;
    }
}
