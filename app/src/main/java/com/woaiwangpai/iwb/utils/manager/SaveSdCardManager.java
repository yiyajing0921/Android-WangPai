package com.woaiwangpai.iwb.utils.manager;

import android.graphics.Bitmap;

import com.woaiwangpai.iwb.utils.LogUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @Author : YiYaJing
 * @Data : 2020/6/17 8:52
 * @Email : yiyajing8023@163.com
 * @Description : 保存图片
 */

public class SaveSdCardManager {

    public static String saveMyBitmap(String bitName, Bitmap mBitmap) {
        String filePath = "";
        File f = new File("/sdcard/" + bitName);
        filePath = f.getAbsolutePath();
        try {
            f.createNewFile();
        } catch (IOException e) {
            LogUtils.i("在保存图片时出错", "在保存图片时出错" + e.toString());
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filePath;
    }

    public static String saveMyBitmapPng(String bitName, Bitmap mBitmap) {
        String filePath = "";
        File f = new File("/sdcard/" + bitName);
        filePath = f.getAbsolutePath();
        try {
            f.createNewFile();
        } catch (IOException e) {
            LogUtils.i("在保存图片时出错", "在保存图片时出错" + e.toString());
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filePath;
    }
}
