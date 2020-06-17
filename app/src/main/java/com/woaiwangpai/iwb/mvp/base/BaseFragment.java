package com.woaiwangpai.iwb.mvp.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUILoadingView;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.IMEventListener;
import com.woaiwangpai.iwb.MyApplication;
import com.woaiwangpai.iwb.mvp.interfaces.IView;
import com.woaiwangpai.iwb.utils.ToastUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @Author : YiYaJing
 * @Data : 2020/6/17 9:03
 * @Email : yiyajing8023@163.com
 * @Description : Fragment基类
 */
public abstract class BaseFragment extends Fragment implements IView {

    protected abstract int getLayoutId();

    protected Context context;
    protected View mRootView;
    protected Unbinder unbinder;
    private QMUILoadingView loadView;

    /*---------------多层嵌套懒加载---------------------*/
    /**
     * 第一次用户可见
     */
    private boolean mIsFirstVisible = true;

    /**
     * View是否创建完成
     */
    private boolean isViewCreated = false;

    /**
     * Fragment 是否可见
     */
    private boolean currentVisibleState = false;
    /*---------------多层嵌套懒加载---------------------*/

    protected abstract void initData(Bundle savedInstanceState);

    protected abstract void setListener();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();//解绑
//        当 View 被销毁的时候我们需要重新设置 isViewCreated mIsFirstVisible 的状态
        isViewCreated = false;
        mIsFirstVisible = true;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView != null) {
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (parent != null) {
                parent.removeView(mRootView);
            }
            return mRootView;
        }
        mRootView = inflater.inflate(getLayoutId(), container, false);
        unbinder = ButterKnife.bind(this, mRootView);
        this.context = getActivity();
        this.initData(savedInstanceState);
        setListener();
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TUIKit.addIMEventListener(mIMEventListener);
        if (loadView == null) {
            loadView = new QMUILoadingView(getActivity());
            loadView.setVisibility(View.GONE);
            loadView.setSize(120);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            layoutParams.gravity = Gravity.CENTER;
            getActivity().addContentView(loadView, layoutParams);
        }
        isViewCreated = true;
        if (!isHidden() && getUserVisibleHint()) {
            prepareFetchData(true);
        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isViewCreated) {
            if (isVisibleToUser && !currentVisibleState) {
                prepareFetchData(true);
            } else if (!isVisibleToUser && currentVisibleState) {
                prepareFetchData(false);
            }
        }
    }

    /**
     * 设置沉浸式状态栏
     * @param topbar 腾讯得topbar
     * @param color 导航栏颜色
     */
    protected void setTopBar(QMUITopBar topbar, int color) {
        topbar.setBackgroundColor(ContextCompat.getColor(getActivity(), color));
        topbar.addLeftBackImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseFragment.this.getActivity().finish();
            }
        });
        // 沉浸式状态栏
        QMUIStatusBarHelper.translucent(getActivity());
        //设置状态栏黑色字体和图标，
        //支持4.4以上的MIUI和flyme  以及5.0以上的其他android
        QMUIStatusBarHelper.setStatusBarLightMode(getActivity());
    }

    /**
     * 判断懒加载条件
     *
     * @param forceUpdate 强制更新
     */
    public void prepareFetchData(boolean forceUpdate) {
        if (forceUpdate && isParentInvisible()) {
            return;
        }

        if (currentVisibleState == forceUpdate) {
            return;
        }

        currentVisibleState = forceUpdate;

        if (forceUpdate) {
            if (mIsFirstVisible) {
                mIsFirstVisible = false;
                onFragmentFirstVisible();
            }
            onFragmentResume();
            dispatchChildVisibleState(true);
        } else {
            dispatchChildVisibleState(false);
            onFragmentPause();
        }
    }

    /**
     * 分发子fragment的可见事件
     *
     * @param visible
     */
    private void dispatchChildVisibleState(boolean visible) {
        FragmentManager childFragmentManager = getChildFragmentManager();
        List<Fragment> fragments = childFragmentManager.getFragments();
        if (!fragments.isEmpty()) {
            for (Fragment child : fragments) {
                // 如果只有当前 子 fragment getUserVisibleHint() = true 时候分发事件，并将 也就是我们上面说的 Bottom2InnerFragment1
                if (child instanceof BaseFragment && !child.isHidden() && child.getUserVisibleHint()) {
                    ((BaseFragment) child).prepareFetchData(visible);
                }
            }
        }
    }

    /**
     * 用于分发可见时间的时候父获取 fragment 是否隐藏
     *
     * @return true fragment 不可见， false 父 fragment 可见
     */
    private boolean isParentInvisible() {
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof BaseFragment) {
            BaseFragment fragment = (BaseFragment) parentFragment;
            return !fragment.isSupportVisible();
        } else {
            return false;
        }

    }

    private boolean isSupportVisible() {
        return currentVisibleState;
    }

    /**
     * 该方法与 setUserVisibleHint 对应，调用时机是 show，hide 控制 Fragment 隐藏的时候，
     * 注意的是，只有当 Fragment 被创建后再次隐藏显示的时候才会调用，第一次 show 的时候是不会回调的。
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            prepareFetchData(false);
        } else {
            prepareFetchData(true);
        }
    }

    protected void onFragmentPause() {
//        LogUtils.e(getClass().getSimpleName() + "  对用户不可见");
    }

    private void onFragmentResume() {
        loadData();
    }

    /**
     * 对用户第一次可见
     */
    public void onFragmentFirstVisible() {
//        LogUtils.e(getClass().getSimpleName() + "  第一次对用户可见");
    }

    /**
     * 懒加载
     */
    protected abstract void loadData();

    protected void showToast(String msg) {
        ToastUtils.show(msg);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mRootView = null;
//        Log.i("BaseFragment", "onDestroy: " + getClass().getSimpleName());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mIsFirstVisible) {
            if (!isHidden() && !currentVisibleState && getUserVisibleHint()) {
                prepareFetchData(true);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (currentVisibleState && getUserVisibleHint()) {
            prepareFetchData(false);
        }
    }

    private boolean isFragmentVisible(Fragment fragment) {
        return !fragment.isHidden() && fragment.getUserVisibleHint();
    }
}
