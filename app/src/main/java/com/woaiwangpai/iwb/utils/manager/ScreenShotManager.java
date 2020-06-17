package com.woaiwangpai.iwb.utils.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.view.View;

import com.woaiwangpai.iwb.R;
import com.woaiwangpai.iwb.utils.LogUtils;
import com.woaiwangpai.iwb.utils.ToastUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @Author : YiYaJing
 * @Data : 2020/6/17 8:52
 * @Email : yiyajing8023@163.com
 * @Description : 屏幕截图
 */

public class ScreenShotManager {

    private static final String TAG = "ScreenShotManager";
    private static Context context;

    /**
     * 进行截取屏幕
     *
     * @param pActivity
     * @return bitmap
     */
    public static String takeScreenShot(Activity pActivity) {
        context = pActivity;
        Bitmap bitmap = null;
        View view = pActivity.getWindow().getDecorView();
        // 设置是否可以进行绘图缓存
        view.setDrawingCacheEnabled(true);
        // 如果绘图缓存无法，强制构建绘图缓存
        view.buildDrawingCache();
        // 返回这个缓存视图
        bitmap = view.getDrawingCache();

        // 获取状态栏高度
        Rect frame = new Rect();
        // 测量屏幕宽和高
        view.getWindowVisibleDisplayFrame(frame);
        int stautsHeight = 100;
        LogUtils.i("jiangqq", "状态栏的高度为:" + stautsHeight);
        int width = pActivity.getWindowManager().getDefaultDisplay().getWidth();
        int height = (int) width * 7 / 5;         //截图时控制距离，距离屏幕底部多高距离
        bitmap = Bitmap.createBitmap(bitmap, 0, stautsHeight, width, height);
        LogUtils.i("截图截图截图截图", "截图width:" + width + "截图height:" + height + "顶部开始Y坐标" + stautsHeight);
        String path = SaveSdCardManager.saveMyBitmap(System.currentTimeMillis() + ".jpg", bitmap);
        return path;
    }

    //好用的保存
    public static String getSavePath(Bitmap bitmap) {
        String path = SaveSdCardManager.saveMyBitmap(System.currentTimeMillis() + ".jpg", bitmap);
        return path;
    }

    /**
     * 首先默认个文件保存路径
     */
    private static final String SAVE_PIC_PATH = Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED) ? Environment.getExternalStorageDirectory().getAbsolutePath() : "/mnt/sdcard";//保存到SD卡
    private static final String SAVE_REAL_PATH = SAVE_PIC_PATH + "/good/savePic";//保存的确切位置

    public static void saveFile(Bitmap bm, String fileName, String path) throws IOException {
        String fileName1 = System.currentTimeMillis() + ".jpg";
        String subForder = SAVE_REAL_PATH + fileName1;
        File foder = new File(subForder);
        if (!foder.exists()) {
            foder.mkdirs();
        }
        File myCaptureFile = new File(subForder, fileName);
        if (!myCaptureFile.exists()) {
            myCaptureFile.createNewFile();
        }
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        bos.flush();
        bos.close();
    }

    /**
     * @param bitmap
     * @return
     */
    public static String createImageFromBitmap(Context context, Bitmap bitmap) {
        String fileName = System.currentTimeMillis() + ".jpg";
        try {
            String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + fileName;
            File appDir = new File(storePath);
            if (!appDir.exists()) {
                appDir.mkdir();
            }

            File file = new File(appDir, fileName);

            FileOutputStream fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片
            // 把数据写入文件，100表示不压缩
            boolean isSuccess = bitmap.compress(Bitmap.CompressFormat.JPEG, 60, fos);
            fos.flush();
            fos.close();

            //其次把文件插入到系统图库（不要插入到图库，直接通知更新数据库就行了）
//            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);

            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
        } catch (Exception e) {
            e.printStackTrace();
            fileName = null;
        }
        LogUtils.i("fileName", fileName);
        return fileName.toString();

    }


    /**
     * 保存图片到sdcard中
     *
     * @param pBitmap
     */
    public static boolean savePic(Bitmap pBitmap, String strName) {
        String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + strName;
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(strName);
            if (null != fos) {
                pBitmap.compress(Bitmap.CompressFormat.JPEG, 60, fos);
                fos.flush();
                fos.close();
                return true;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 保存文件到指定路径
     *
     * @param context
     * @param bmp
     * @param storePath
     * @return
     */
    public static boolean saveImageToGallery(Context context, Bitmap bmp, String storePath) {
        //首先创建路径（有则不创建，没有则创建）
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }

        //这个才是文件
        String fileName = System.currentTimeMillis() + ".png";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            //通过io流的方式来压缩保存图片
            boolean isSuccess = bmp.compress(Bitmap.CompressFormat.PNG, 100, bos);
//            fos.flush();
//            fos.close();
            bos.flush();
            bos.close();


//            //其次把文件插入到系统图库（不要插入到图库，直接通知更新数据库就行了）
//            try {
//                MediaStore.Images.Media.insertImage(context.getContentResolver(),
//                        file.getAbsolutePath(), fileName, null);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }

            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));

            if (isSuccess) {
                ToastUtils.show("图片保存到" + storePath + File.separator + fileName + "!");
                return true;
            } else {
                ToastUtils.show("保存图片失败!");
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 设置位图的背景色
     *
     * @param bitmap 需要设置的位图
     * @param color  背景色
     */
    public static void setBitmapBGColor(Bitmap bitmap, int color) {
        for (int i = 0; i < bitmap.getWidth(); i++) {
            for (int j = 0; j < bitmap.getHeight(); j++) {
                bitmap.setPixel(i, j, color);//将bitmap的每个像素点都设置成相应的颜色
            }
        }
    }

    /**
     * 获取一个 View 的缓存视图
     */
    public static Bitmap getCacheBitmapFromView(Context context, View view) {
        //第一种方案   返回的bitmap不为空
        if (view.getMeasuredWidth() <= 0 || view.getMeasuredHeight() <= 0) {
            return null;
        }
        //不能使用此方法的宽高
//        Bitmap b = Bitmap.createBitmap(view.getLayoutParams().width,
//                view.getLayoutParams().height, Bitmap.Config.ARGB_8888);
        //一定要使用这个方法的宽高
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(),
                view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        setBitmapBGColor(bitmap, context.getResources().getColor(R.color.white));
        Canvas c = new Canvas(bitmap);
//        view.layout(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        view.draw(c);
        return bitmap;
    }
}
