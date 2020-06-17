package com.woaiwangpai.iwb.wechat.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by arvinljw on 17/11/27 15:37
 * Function：
 * Desc：
 */
final class SocialUtil {

    static String get(URL url) throws Exception {
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        if (conn.getResponseCode() == 200) {
            InputStream is = conn.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while (-1 != (len = is.read(buffer))) {
                out.write(buffer, 0, len);
                out.flush();
            }
            return out.toString("utf-8");
        }
        return null;
    }

    static String getAppStateName(Context context) {
        String packageName = context.getPackageName();
        int beginIndex = 0;
        if (packageName.contains(".")) {
            beginIndex = packageName.lastIndexOf(".");
        }
        return packageName.substring(beginIndex);
    }

    static String getQQSex(String gender) {
        return "男".equals(gender) ? "M" : "F";
    }

    static String getWXSex(String gender) {
        return "1".equals(gender) ? "M" : "F";
    }

    static String getWBSex(String gender) {
        return "m".equals(gender) ? "M" : "F";
    }

    static String buildTransaction(String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    static byte[] bmpToByteArray(final Bitmap bmp, boolean needThumb) {
        Bitmap newBmp;
        if (needThumb) {
            int width = bmp.getWidth();
            int height = bmp.getHeight();
            if (width > height) {
                height = height * 150 / width;
                width = 150;
            } else {
                width = width * 150 / height;
                height = 150;
            }
            newBmp = Bitmap.createScaledBitmap(bmp, width, height, true);
        } else {
            newBmp = bmp;
        }
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        newBmp.compress(Bitmap.CompressFormat.JPEG, 100, output);

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (!bmp.isRecycled()) {
                bmp.recycle();
            }
            if (!newBmp.isRecycled()) {
                newBmp.recycle();
            }
        }

        return result;
    }

    /**
     * 是否安装qq
     */
//    static boolean isQQInstalled(Context context) {
//        final PackageManager packageManager = context.getPackageManager();
//        List<PackageInfo> packageInfo = packageManager.getInstalledPackages(0);
//        if (packageInfo != null) {
//            for (int i = 0; i < packageInfo.size(); i++) {
//                String pn = packageInfo.get(i).packageName;
//                if (pn.equals("com.tencent.mobileqq")) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
    private static String qqPkgName = "com.tencent.mobileqq";
    /**
     * 是否安装qq
     */
    public static boolean checkApkExist(Context context, String packageName){
        if (TextUtils.isEmpty(packageName))
            return false;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName,
                        PackageManager.MATCH_UNINSTALLED_PACKAGES);
                Log.d("isInstan",info.toString());
            }else {
                ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName,
                    PackageManager.GET_UNINSTALLED_PACKAGES);
                Log.d("isInstan",info.toString());
            }

            return true;
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("",e.toString());
            return false;
        }
    }
    public static boolean isQQInstalled(Context context){
        return checkApkExist(context, qqPkgName);
    }
}
