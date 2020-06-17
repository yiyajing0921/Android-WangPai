package com.woaiwangpai.iwb.utils.manager;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.woaiwangpai.iwb.MyApplication;
import com.woaiwangpai.iwb.constant.Constant;
import com.woaiwangpai.iwb.utils.cache.glidecache.GlideCatchUtil;

import java.io.File;
import java.math.BigDecimal;

/**
 * @Author : YiYaJing
 * @Data : 2020/6/17 8:39
 * @Email : yiyajing8023@163.com
 * @Description : 清除APP所有数据
 */

public class DataCleanManager {
/*
Android中内部存储，外部存储的概念 ；
getDataDirectory，getFilesDir，getCacheDir，getDir，getExternalStorageDirectory，getExternalStoragePublicDirectory，getExternalFilesDir，getExternalCacheDir，getExternalCacheDir，getRootDirectory等方法区别联系 。
清除数据和清除缓存到底清除了什么数据 ；
/storage/sdcard，/sdcard，/mnt/sdcard，/storage/emulated/0之间的关系 ；
访问外部存储的API方法：
1、Environment.getExternalStorageDirectory().getAbsolutePath()
2、Environment.getExternalStoragePublicDirectory(“”).getAbsolutePath()
3、getExternalFilesDir(“”).getAbsolutePath()
4、getExternalCacheDir().getAbsolutePath()
1、Environment.getDataDirectory() = /data
这个方法是获取内部存储的根路径
2、getFilesDir().getAbsolutePath() = /data/user/0/packname/files
这个方法是获取某个应用在内部存储中的files路径
3、getCacheDir().getAbsolutePath() = /data/user/0/packname/cache
这个方法是获取某个应用在内部存储中的cache路径
4、getDir(“myFile”, MODE_PRIVATE).getAbsolutePath() = /data/user/0/packname/app_myFile
这个方法是获取某个应用在内部存储中的自定义路径
方法2,3,4的路径中都带有包名，说明他们是属于某个应用
…………………………………………………………………………………………
5、Environment.getExternalStorageDirectory().getAbsolutePath() = /storage/emulated/0
这个方法是获取外部存储的根路径
6、Environment.getExternalStoragePublicDirectory(“”).getAbsolutePath() = /storage/emulated/0
这个方法是获取外部存储的根路径
7、getExternalFilesDir(“”).getAbsolutePath() = /storage/emulated/0/Android/data/packname/files
这个方法是获取某个应用在外部存储中的files路径
8、getExternalCacheDir().getAbsolutePath() = /storage/emulated/0/Android/data/packname/cache
这个方法是获取某个应用在外部存储中的cache路径
Environment.getDownloadCacheDirectory() = /cache
Environment.getRootDirectory() = /system
 */

    /**
     * * 清除本应用所有的数据 * *
     *
     * @param context
     * @param filepath
     */
    public static void cleanApplicationData(Context context, String... filepath) {
        cleanInternalCache(context);
        cleanExternalCache(context);
        cleanDatabases(context);
        cleanSharedPreference(context);
        cleanFiles(context);
        cleanHttpCache(context);
        cleanACache();
        cleanGlideCache();
        deleteFilesByDirectory();
        deleteDirectory(Environment.getExternalStorageDirectory().getAbsolutePath() + "/AllenVersionPath");//下载apk路径
        if (filepath == null) {
            return;
        }
        for (String filePath : filepath) {
            cleanCustomCache(filePath);
        }
    }

    /**
     * 清理网络请求缓存
     *
     * @param context
     */
    public static void cleanHttpCache(Context context) {
        deleteFolderFile(context.getCacheDir() + "/" + Constant.HTTPCACHE, true);//清除httpcache磁盘缓存
    }

    /**
     * 清理Acache所有存储 包含token等存储
     */
    public static void cleanACache() {
        MyApplication.getInstance().clearACache();
    }

    /**
     * 清除Glide磁盘缓存
     */
    public static void cleanGlideCache() {
        GlideCatchUtil.getInstance().clearCacheMemory();
        GlideCatchUtil.getInstance().cleanCatchDisk();//清除Glide磁盘缓存，自己获取缓存文件夹并删除方法
    }

    /**
     * 查缓存大小
     *
     * @param context
     * @return
     * @throws
     */
    public static String getTotalCacheSize(Context context) throws Exception {
        long cacheSize = getFolderSize(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cacheSize += getFolderSize(context.getExternalCacheDir());
        }
        return getFormatSize(cacheSize);
    }

    /**
     * 清空应用内缓存
     *
     * @param context
     */
    public static boolean clearAllCache(Context context) {
        boolean isClearCache = false;
        boolean isClearExternalCache = false;
        isClearCache = deleteDir(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            isClearExternalCache = deleteDir(context.getExternalCacheDir());
            if (isClearCache && isClearExternalCache) {
                //如果应用内缓存和应用外部缓存都清理了，就代表清理缓存成功，否则清理缓存失败
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * 清理文件目录
     *
     * @param dir
     * @return
     */
    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    /**
     * * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache) * *
     *
     * @param context
     */
    public static void cleanInternalCache(Context context) {
        deleteFilesByDirectory(context.getCacheDir());
    }

    /**
     * * 清除本应用所有数据库(/data/data/com.xxx.xxx/databases) * *
     *
     * @param context
     */
    public static void cleanDatabases(Context context) {
        deleteFilesByDirectory(new File("/data/data/" + context.getPackageName() + "/databases"));
    }

    /**
     * * 清除本应用SharedPreference(/data/data/com.xxx.xxx/shared_prefs) *
     *
     * @param context
     */
    public static void cleanSharedPreference(Context context) {
        deleteFilesByDirectory(new File("/data/data/" + context.getPackageName() + "/shared_prefs"));
    }

    /**
     * * 按名字清除本应用数据库 * *
     *
     * @param context
     * @param dbName
     */
    public static void cleanDatabaseByName(Context context, String dbName) {
        context.deleteDatabase(dbName);
    }

    /**
     * * 清除/data/data/com.xxx.xxx/files下的内容 * *
     *
     * @param context
     */
    public static void cleanFiles(Context context) {
        deleteFilesByDirectory(context.getFilesDir());
    }

    /**
     * * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache)
     *
     * @param context
     */
    public static void cleanExternalCache(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            deleteFilesByDirectory(context.getExternalCacheDir());
        }
    }

    /**
     * * 清除自定义路径下的文件，使用需小心，请不要误删。而且只支持目录下的文件删除 * *
     *
     * @param filePath
     */
    public static void cleanCustomCache(String filePath) {
        deleteFilesByDirectory(new File(filePath));
    }

    /**
     * * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理 * *
     *
     * @param directory 文件夹
     */
    private static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                item.delete();
            }
        }
    }

    /**
     * 删除指定的 图片裁剪目录
     */
    private static void deleteFilesByDirectory() {
        File directory = new File("/storage/emulated/0/selector/crop");
//        File directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/selector/crop");
//        File directory = new File(Environment.getExternalStorageDirectory()+ "/selector/crop");
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                item.delete();
            }
        }
    }


    /**
     * 删除单个文件
     *
     * @param filePath 被删除文件的文件名
     * @return 文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
            return file.delete();
        }
        return false;
    }

    /**
     * 删除文件夹以及目录下的文件
     *
     * @param filePath 被删除目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String filePath) {
        boolean flag = false;
        //如果filePath不以文件分隔符结尾，自动添加文件分隔符
        if (!filePath.endsWith(File.separator)) {
            filePath = filePath + File.separator;
        }
        File dirFile = new File(filePath);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        flag = true;
        File[] files = dirFile.listFiles();
        //遍历删除文件夹下的所有文件(包括子目录)
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                //删除子文件
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) {
                    break;
                }
            } else {
                //删除子目录
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) {
                    break;
                }
            }
        }
        if (!flag) {
            return false;
        }
        //删除当前空目录
        return dirFile.delete();
    }

    /**
     * 根据路径删除指定的目录或文件，无论存在与否
     *
     * @param filePath 要删除的目录或文件
     * @return 删除成功返回 true，否则返回 false。
     */
    public boolean DeleteFolder(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return false;
        } else {
            if (file.isFile()) {
                // 为文件时调用删除文件方法
                return deleteFile(filePath);
            } else {
                // 为目录时调用删除目录方法
                return deleteDirectory(filePath);
            }
        }
    }

    // 获取文件
    //Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
    //Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 删除指定目录下文件及目录
     *
     * @param deleteThisPath
     * @param filePath
     * @return
     */
    public static void deleteFolderFile(String filePath, boolean deleteThisPath) {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                File file = new File(filePath);
                if (file.isDirectory()) {// 如果下面还有文件
                    File files[] = file.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        deleteFolderFile(files[i].getAbsolutePath(), true);
                    }
                }
                if (deleteThisPath) {
                    if (!file.isDirectory()) {// 如果是文件，删除
                        file.delete();
                    } else {// 目录
                        if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除
                            file.delete();
                        }
                    }
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * 格式化单位
     *
     * @param size
     * @return
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
//        if (kiloByte < 1) {
//            return size + "Byte";
//        }
        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "K";
        }
        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "M";
        }
        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "G";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "T";
    }

    public static String getCacheSize(File file) throws Exception {
        return getFormatSize(getFolderSize(file));
    }

    /**
     * 判断文件是否存在
     *
     * @return
     */
    public static boolean fileIsExists(String fileName) {
        try {
            File f = new File(fileName);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
