package com.woaiwangpai.iwb.utils.cache.acache;

import com.woaiwangpai.iwb.MyApplication;


/**
 * @Author : YiYaJing
 * @Data : 2020/6/17 9:00
 * @Email : yiyajing8023@163.com
 * @Description : 保存页面数据
 */
public class ACacheUtils {
    private static ACache mACache= ACache.get(MyApplication.getContext());
    public static String getUrlCache(String url) {
        return  mACache.getAsString(url);
    }

    /**
     *
     * @param url 保存 key
     * @param json 保存value (String)
     */
    public static void setUrlCache(String url,String json) {
        mACache.put(url, json,ACache.TIME_DAY);
    }

    /**
     * 设置指定时间 保存  ACache.TIME
      * @param url
     * @param json
     * @param saveTime
     */
  public static void setUrlCache(String url,String json,int saveTime) {
        mACache.put(url, json,saveTime);
    }

    /**
     * 保存1天
     * @param url
     */
    public static void deleteUrlCache(String url) {
        mACache.remove(url);
    }

    /**
     * 判断网页有无缓存
     * @param url
     * @return
     */
   public static boolean urlHaveCache(String url){
        return mACache.getAsString(url)!=null;
    }
}
