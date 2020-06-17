package com.woaiwangpai.iwb.utils.manager;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.woaiwangpai.iwb.utils.LogUtils;

import java.io.IOException;

/**
 * @Author : YiYaJing
 * @Data : 2020/6/17 8:52
 * @Email : yiyajing8023@163.com
 * @Description : 屏幕管理类
 */
public class ScreenManager {

    private static final String TAG = "ScreenManager";

    /**
     * 根据手机的分辨率从 dip 的单位 转成为 px(像素)
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 计算出来的位置，y方向就在anchorView的上面和下面对齐显示，x方向就是与屏幕右边对齐显示
     * 如果anchorView的位置有变化，就可以适当自己额外加入偏移来修正
     *
     * @param anchorView  呼出window的view
     * @param contentView window的内容布局
     * @return window显示的左上角的xOff, yOff坐标
     */
    public static int[] calculatePopWindowPos(final View anchorView, final View contentView) {
        final int windowPos[] = new int[2];
        final int anchorLoc[] = new int[2];
        // 获取锚点View在屏幕上的左上角坐标位置
        anchorView.getLocationOnScreen(anchorLoc);
        final int anchorHeight = anchorView.getHeight();
        // 获取屏幕的高宽
        final int screenHeight = getScreenHeight(anchorView.getContext());
        final int screenWidth = getScreenWidth(anchorView.getContext());
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        // 计算contentView的高宽
        final int windowHeight = contentView.getMeasuredHeight();
        final int windowWidth = contentView.getMeasuredWidth();
        // 判断需要向上弹出还是向下弹出显示
        final boolean isNeedShowUp = (screenHeight - anchorLoc[1] - anchorHeight < windowHeight);
        if (isNeedShowUp) {
            windowPos[0] = screenWidth - windowWidth;
            windowPos[1] = anchorLoc[1] - windowHeight;
        } else {
            windowPos[0] = screenWidth - windowWidth;
            windowPos[1] = anchorLoc[1] + anchorHeight;
        }
        return windowPos;
    }

    /**
     *
     * @param context
     * @return 屏幕dpi 480
     */
    public static int dpi(Context context) {
        //当前屏幕密度
        return (int) context.getResources().getDisplayMetrics().densityDpi;
    }

    /**
     *
     * @param context
     * @return 屏幕dpi/160
     */
    public static float density(Context context) {
//        dpi/160的结果
        //当前屏幕密度
        return (float) context.getResources().getDisplayMetrics().density;
    }
    /**
     * 获取屏幕高度(px)
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 获取屏幕宽度(px)
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕宽度(dp)
     */
    public static int getScreenWidthDp(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;         // 屏幕宽度（像素）
        int height = dm.heightPixels;       // 屏幕高度（像素）
        float density = dm.density;         // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = dm.densityDpi;     // 屏幕密度dpi（120 / 160 / 240）
        // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        int screenWidth = (int) (width / density);  // 屏幕宽度(dp)
        int screenHeight = (int) (height / density);// 屏幕高度(dp)


        String widthStr = "屏幕宽度（像素）：" + width;
        String heightStr = "屏幕宽度（像素）：" + height;
        String dpi = "屏幕密度dpi（120 / 160 / 240）：" + densityDpi;
        String widthDpStr = "屏幕宽度（dp）：" + screenWidth;
        String heightDpStr = "屏幕高度（dp）：" + screenHeight;
        LogUtils.i(TAG, "屏幕宽度（像素）：" + width);
        LogUtils.i(TAG, "屏幕高度（像素）：" + height);
        LogUtils.i(TAG, "屏幕密度（0.75 / 1.0 / 1.5）：" + density);
        LogUtils.i(TAG, "屏幕密度dpi（120 / 160 / 240）：" + densityDpi);
        LogUtils.i(TAG, "屏幕宽度（dp）：" + screenWidth);
        LogUtils.i(TAG, "屏幕高度（dp）：" + screenHeight);

        String allStr = widthStr + "*****" + heightStr + "*****" + dpi + "*****" + widthDpStr + "*****" + heightDpStr;
        try {
            ReadAndWriteManager.writeExternal(context, "mineData.txt", allStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return screenWidth;
    }

    private static int getInternalDimensionSize(Context context, String key) {
        int result = 0;
        try {
            int resourceId = context.getResources().getIdentifier(key, "dimen", "android");
            if (resourceId > 0) {
                result = Math.round(context.getResources().getDimensionPixelSize(resourceId) *
                        Resources.getSystem().getDisplayMetrics().density /
                        context.getResources().getDisplayMetrics().density);
            }
        } catch (Resources.NotFoundException ignored) {
            return 0;
        }
        return result;
    }

    public static int getStatusBarHeight(Context context) {
        return getInternalDimensionSize(context, "status_bar_height");
    }

    public static int getNavigationBarHeight(Context context) {
        boolean mInPortrait = context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        int result = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            if (hasNavBar((Activity) context)) {
                String key;
                if (mInPortrait) {
                    key = "navigation_bar_height";
                } else {
                    key = "navigation_bar_height_landscape";
                }
                return getInternalDimensionSize(context, key);
            }
        }
        return result;
    }

    private static boolean hasNavBar(Activity activity) {
        //判断小米手机是否开启了全面屏,开启了，直接返回false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (Settings.Global.getInt(activity.getContentResolver(), "force_fsg_nav_bar", 0) != 0) {
                return false;
            }
        }
        //其他手机根据屏幕真实高度与显示高度是否相同来判断
        WindowManager windowManager = activity.getWindowManager();
        Display d = windowManager.getDefaultDisplay();

        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            d.getRealMetrics(realDisplayMetrics);
        }

        int realHeight = realDisplayMetrics.heightPixels;
        int realWidth = realDisplayMetrics.widthPixels;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        d.getMetrics(displayMetrics);

        int displayHeight = displayMetrics.heightPixels;
        int displayWidth = displayMetrics.widthPixels;

        return (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0;
    }

    /**
     *   @Override
     *     protected void onCreate(Bundle savedInstanceState) {
     *         super.onCreate(savedInstanceState);
     *         Density.setDensity(getApplication(), this);
     *         setContentView(R.layout.activity_main);
     *     }
    要放在setContentView之前调用，实际项目中key将此方法调用放在BaseActivity中。
    图中手机像素的屏幕宽度为1080px，但是设置160*2 = 320dp就能占据全屏宽度，由此可知通过像素密度适配的方式，
    我们只需要根据参考设备来计算出density、scaledDensity、DensityDpi值，就能达到适配的效果。
     */
    private static final float WIDTH = 320; //参考设备的宽，单位dp
    private static float appDensity; //表示屏幕密度
    private static float appScaledDensity;//表示字体缩放比例,默认与appDensity相等

    public static void setDensity(final Application application, Activity activity) {
        //获取当前屏幕的显示信息
        DisplayMetrics displayMetrics = application.getResources().getDisplayMetrics();
        if (appDensity == 0) {
            //初始化赋值
            appDensity = displayMetrics.density;
            appScaledDensity = displayMetrics.scaledDensity;

            //添加字体变化监听
            application.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(Configuration newConfig) {
                    if (newConfig != null && newConfig.fontScale > 0) { //说明字体大小改变了
                        //重新赋值appScaledDensity
                        appScaledDensity = application.getResources().getDisplayMetrics().scaledDensity;
                    }
                }

                @Override
                public void onLowMemory() {

                }
            });
        }

        //计算目标值density、scaledDensity、densityDpi
        float targetDensity = displayMetrics.widthPixels / WIDTH;
        float targetScaledDensity = targetDensity * (appDensity / appScaledDensity);
        int targetDensityDpi = (int) (targetDensity * 160);

        //替换activity的density、scaledDensity、densityDpi
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        dm.density = targetDensity;
        dm.scaledDensity = targetScaledDensity;
        dm.densityDpi = targetDensityDpi;
    }
    /**
     * RecyclerView 移动到当前位置，
     * 根据当前RecyclerView的条目数量
     *
     * @param manager       设置RecyclerView对应的manager
     * @param mRecyclerView 当前的RecyclerView
     * @param n             要跳转的位置
     */
    public static void MoveToPosition(LinearLayoutManager manager, RecyclerView mRecyclerView, int n) {

        int firstItem = manager.findFirstVisibleItemPosition();
        int lastItem = manager.findLastVisibleItemPosition();
        if (n <= firstItem) {
            mRecyclerView.scrollToPosition(n);
        } else if (n <= lastItem) {
            int top = mRecyclerView.getChildAt(n - firstItem).getTop();
            mRecyclerView.scrollBy(0, top);
        } else {
            mRecyclerView.scrollToPosition(n);
        }
    }

    //强制停止RecyclerView滑动方法
    public static void forceStopRecyclerViewScroll(RecyclerView mRecyclerView) {
        mRecyclerView.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_CANCEL, 0, 0, 0));
    }
}
