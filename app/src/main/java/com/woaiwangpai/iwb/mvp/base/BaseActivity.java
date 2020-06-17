package com.woaiwangpai.iwb.mvp.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.IMEventListener;
import com.woaiwangpai.iwb.MyApplication;
import com.woaiwangpai.iwb.utils.ToastUtils;
import com.woaiwangpai.iwb.utils.manager.AppManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

/**
 * @Author : YiYaJing
 * @Data : 2020/6/17 9:03
 * @Email : yiyajing8023@163.com
 * @Description : activity基类
 */
public abstract class BaseActivity extends AppCompatActivity {
    public abstract int getContentViewId();

    protected Activity context;
    private boolean isWindow = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ButterKnife.bind(this);
        TUIKit.addIMEventListener(mIMEventListener);
        AppManager.getAppManager().addActivity(this);
        this.context = this;
        //默认为浅色背景， 状态栏字体颜色为黑色， 适用Android6.0以后的版本
        isBackgroundLight(false);
        initView();
        setListener();
        if (isWindow) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    // 因为EMUI3.1系统与这种沉浸式方案API有点冲突，会没有沉浸式效果。
                    // 所以这里加了判断，EMUI3.1系统不清除FLAG_TRANSLUCENT_STATUS
                    if (!isEMUI3_1() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    }
//                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE );
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(Color.TRANSPARENT);
                }
            }
        }

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

    protected abstract void initView();

    protected abstract void setListener();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
    }

    /**
     * 设置沉浸式状态栏
     * @param topbar 腾讯得topbar
     * @param color 导航栏颜色
     */
    protected void setTopBar(QMUITopBar topbar, int color) {
        topbar.setBackgroundColor(ContextCompat.getColor(this, color));
        // 沉浸式状态栏
        QMUIStatusBarHelper.translucent(this);
        //设置状态栏黑色字体和图标，
        //支持4.4以上的MIUI和flyme  以及5.0以上的其他android
        QMUIStatusBarHelper.setStatusBarLightMode(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 显示窗口得时候，设置沉浸式状态栏
     * @param isWindow
     */
    protected void setWindow(boolean isWindow) {
        this.isWindow = isWindow;
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     *
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void finish() {
        super.finish();
//         overridePendingTransition(R.anim.activity_invariant, R.anim.activity_right_out);
    }


    protected void showToast(String msg) {
        ToastUtils.show(msg);
    }

    public static boolean isEMUI3_1() {
        if ("EmotionUI_3.1".equals(getEmuiVersion())) {
            return true;
        }
        return false;
    }

    @SuppressLint("PrivateApi")
    private static String getEmuiVersion() {
        Class<?> classType = null;
        try {
            classType = Class.forName("android.os.SystemProperties");
            Method getMethod = classType.getDeclaredMethod("get", String.class);
            return (String) getMethod.invoke(classType, "ro.build.version.emui");
        } catch (ClassNotFoundException e) {
        } catch (NoSuchMethodException e) {
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        } catch (Exception e) {
        }
        return "";
    }

    /**
     * 默认背景是暗色的
     *
     * @param isLight
     */
    protected void isBackgroundLight(boolean isLight) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (isLight) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            }
        }
    }

    /**
     * 设置字体大小不随系统改变
     *
     * @return
     */
    @Override
    public Resources getResources() {
        Resources resources = super.getResources();
        Configuration configuration = new Configuration();
        configuration.setToDefaults();
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return resources;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        if (startActivitySelfCheck(intent)) {
            // 查看源码得知 startActivity 最终也会调用 startActivityForResult
            super.startActivityForResult(intent, requestCode, options);
        }
    }

    private String mActivityJumpTag;
    private long mActivityJumpTime;

    /**
     * 检查当前 Activity 是否重复跳转了，不需要检查则重写此方法并返回 true 即可
     *
     * @param intent 用于跳转的 Intent 对象
     * @return 检查通过返回true, 检查不通过返回false
     */
    protected boolean startActivitySelfCheck(Intent intent) {
        // 默认检查通过
        boolean result = true;
        // 标记对象
        String tag;
        if (intent.getComponent() != null) { // 显式跳转
            tag = intent.getComponent().getClassName();
        } else if (intent.getAction() != null) { // 隐式跳转
            tag = intent.getAction();
        } else {
            return result;
        }

        if (tag.equals(mActivityJumpTag) && mActivityJumpTime >= SystemClock.uptimeMillis() - 500) {
            // 检查不通过
            result = false;
        }

        // 记录启动标记和时间
        mActivityJumpTag = tag;
        mActivityJumpTime = SystemClock.uptimeMillis();
        return result;
    }

    //add 获取的一个权限
    static boolean isOk = false;

    @SuppressLint("CheckResult")
    protected static boolean requestPermission(Activity activity, String permissions) {
        RxPermissions rxPermission = new RxPermissions(activity);
        rxPermission.requestEach(new String[]{permissions}).subscribe(new Consumer<Permission>() {
            @Override
            public void accept(Permission permission) throws Exception {
                if (permission.granted) {
                    // 用户已经同意该权限
                    isOk = true;
                } else if (permission.shouldShowRequestPermissionRationale) {
                    // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时。还会提示请求权限的对话框
                    isOk = false;
                } else {
                    // 用户拒绝了该权限，而且选中『不再询问』
                    isOk = false;
                }
            }
        });
        return isOk;
    }

    //权限数组
    @SuppressLint("CheckResult")
    protected static void requestPermission(Activity activity, String[] permissions, PermissionsResultListener listener) {
        RxPermissions rxPermission = new RxPermissions(activity);
        rxPermission.requestEach(permissions).subscribe(new Consumer<Permission>() {
            @Override
            public void accept(Permission permission) throws Exception {
                if (permission.granted) {
                    // 用户已经同意该权限
                    listener.onSuccessful();
                } else if (permission.shouldShowRequestPermissionRationale) {
                    // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时。还会提示请求权限的对话框
                    listener.onFailure();
                } else {
                    // 用户拒绝了该权限，而且选中『不再询问』
                    listener.onFailure();
                }
            }
        });

    }

    //权限接口
    public interface PermissionsResultListener {
        //成功
        void onSuccessful();

        //失败
        void onFailure();
    }
}
