package com.woaiwangpai.iwb.mvp.impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.qmuiteam.qmui.widget.QMUILoadingView;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.IMEventListener;
import com.woaiwangpai.iwb.MyApplication;
import com.woaiwangpai.iwb.mvp.base.BaseActivity;
import com.woaiwangpai.iwb.mvp.interfaces.IPresenter;
import com.woaiwangpai.iwb.mvp.interfaces.IView;
import com.woaiwangpai.iwb.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by Gabriel on 2018/10/19.
 * Email yiyajing8023@163.com
 */

public abstract class BaseMvpActivity<P extends IPresenter> extends BaseActivity implements IView {
    //实例化P.桥梁，辅助activity和model之间的交互
    protected P mPresenter;
    private QMUILoadingView loadView;
    private View emptyView;

    @Override
    protected void initView() {
        TUIKit.addIMEventListener(mIMEventListener);
        if (loadView == null) {
            loadView = new QMUILoadingView(this);
            loadView.setVisibility(View.GONE);
            loadView.setSize(120);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            layoutParams.gravity = Gravity.CENTER;
            addContentView(loadView, layoutParams);
        }
        if (bindEvent()) {
            if (!EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this);
            }

        }
        //和View绑定
        mPresenter = getPresenter();
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
        init();
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

    /**
     * 获取view的text
     *
     * @param view
     * @return
     */
    @NonNull
    public String getString(TextView view) {
        if (view != null) {
            return view.getText().toString().trim();
        } else {
            return "";
        }
    }

    @Override
    protected void showToast(String msg) {
        ToastUtils.show(msg);
    }

    // 两次点击间隔不能少于1000ms
    private static final int MIN_DELAY_TIME = 1000;
    private static long lastClickTime;

    //防双击
    public boolean isFastClick() {
        if ((System.currentTimeMillis() - lastClickTime) <= MIN_DELAY_TIME) {
            return true;
        } else {
            lastClickTime = System.currentTimeMillis();
            return false;
        }

    }

    /**
     * 是否绑定EventBus
     *
     * @return true：绑定；false：不绑定
     */
    protected boolean bindEvent() {
        return false;
    }

    public abstract P getPresenter();


    public abstract void init();

    @Override
    protected void onDestroy() {
        //把所有的数据销毁掉
        super.onDestroy();
//        和BasePresenter中的void onDestroy冲突 用一个即可
//        if (mPresenter != null)
//            mPresenter.onDestroy();//释放资源
        this.mPresenter = null;
        loadView.stop();
        if (bindEvent()) {
            if (EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().unregister(this);
            }
        }
    }

    @Override
    public void finishActivity() {
        finish();
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

    @Override
    public void showLoading() {
        loadView.setVisibility(View.VISIBLE);
        loadView.start();
    }

    @Override
    public void hideLoading() {
        loadView.stop();
        loadView.setVisibility(View.GONE);
    }

    /**
     * 数据是否加载完成
     * @return true 加载完成 false未加载完数据
     */
    public boolean getLoading() {
        if (loadView.isShown()) {
            return false;
        } else {
            return true;
        }
    }
}
