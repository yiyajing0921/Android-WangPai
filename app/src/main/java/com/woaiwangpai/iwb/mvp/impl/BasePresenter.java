package com.woaiwangpai.iwb.mvp.impl;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

import com.kingja.loadsir.core.LoadService;
import com.woaiwangpai.iwb.mvp.interfaces.IPresenter;
import com.woaiwangpai.iwb.mvp.interfaces.IView;
import com.woaiwangpai.iwb.mvp.network.callback.loadsir.EmptyCallback;

import java.util.List;

//import com.woaiwangpai.iwb.utils.loadsir.EmptyCallback;


/**
 * Created by Gabriel on 2018/10/19.
 * Email 17600284843@163.com
 */

public class BasePresenter<V extends IView> implements IPresenter<V>, LifecycleObserver {

    //Model数据
    // protected M mModel;
    //View 显示回显的接口
    protected V mRootView;

    public BasePresenter() {
        onStart();
    }

    @Override
    public void onStart() {
        //mRootView一定是activity的实现类。所有通过getLifecycle().addObserver(this);方法可以将activity的生命周期监听
        if (mRootView instanceof LifecycleOwner) {
            ((LifecycleOwner) mRootView).getLifecycle().addObserver(this);
        }
    }

    /**
     * 监听activity的销毁事件,当他销毁的时候会自动调用该方法取消生命周期的订阅
     *
     * @param owner
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy(LifecycleOwner owner) {
        owner.getLifecycle().removeObserver(this);
    }

    @Override
    public void onDestroy() {
        //this.mRootView = null;
    }

    @Override
    public void attachView(V view) {
        this.mRootView = view;
    }

    @Override
    public V getView() {
        return mRootView;
    }


    /**
     * 判断集合是否为null
     *
     * @param list
     * @param mService
     * @return
     */
    public boolean hasData(List list, LoadService mService) {
        if (list == null || list.size() <= 0) {
            mService.showCallback(EmptyCallback.class);
            return false;
        } else {
            return true;
        }
    }

}
