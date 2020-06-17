package com.woaiwangpai.iwb.mvp.impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.IMEventListener;
import com.woaiwangpai.iwb.MyApplication;
import com.woaiwangpai.iwb.mvp.base.BaseFragment;
import com.woaiwangpai.iwb.mvp.interfaces.IPresenter;
import com.woaiwangpai.iwb.mvp.interfaces.IView;
import com.woaiwangpai.iwb.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * @Author : YiYaJing
 * @Data : 2019/10/14 17:41
 * @Class : BaseMvpFragment
 */
public abstract class BaseMvpFragment<P extends IPresenter> extends BaseFragment implements IView {

    protected P mPresenter;
    private View emptyView;

    @Override
    protected void initData(Bundle savedInstanceState) {
        if (bindEvent()) {
            EventBus.getDefault().register(this);
        }
        mPresenter = getPresenter();
        if (mPresenter != null) {
            mPresenter.attachView((IView) this);
        }
        init(savedInstanceState);
        TUIKit.addIMEventListener(mIMEventListener);
        bindEvent();
    }

    // 监听做成静态可以让每个子类重写时都注册相同的一份。
    private static IMEventListener mIMEventListener = new IMEventListener() {
        @Override
        public void onForceOffline() {
            ToastUtils.show("登录失效，请重新登录");
            SharedPreferences shareInfo = MyApplication.getInstance().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = shareInfo.edit();
            editor.putBoolean("auto_login", false);
            editor.commit();
        }
    };

    public abstract P getPresenter();

    public abstract void init(Bundle state);

    /**
     * 获取view的text
     *
     * @param view
     * @return
     */
    @NonNull
    public String getString(TextView view) {
        if (view != null) {
            String trim = view.getText().toString().trim();
            return trim;
        } else {
            return "";
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ToastUtils.cancel();
        if (mPresenter != null) {
            //释放资源
            mPresenter.onDestroy();
        }
        if (bindEvent()) {
            EventBus.getDefault().unregister(this);
        }
        this.mPresenter = null;
    }

    protected boolean bindEvent() {
        return false;
    }

    @Override
    protected void showToast(String msg) {
        ToastUtils.show(msg);
    }

    @Override
    public void finishActivity() {
        getActivity().finish();
    }

    @Override
    public void onComplete() {
        // Do something after the network request over
    }

    @Override
    public void onException() {
        // Do something after the network request error
    }

    @Override
    public void onPageMinus() {
        // Reduce the number of pull-ups after loading error
    }
}
