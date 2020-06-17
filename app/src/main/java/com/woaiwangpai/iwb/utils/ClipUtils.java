package com.woaiwangpai.iwb.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import static com.woaiwangpai.iwb.utils.ToastUtils.showIToast;

/**
 * @Author : YiYaJing
 * @Data : 2020/6/17 8:32
 * @Email : yiyajing8023@163.com
 * @Description : 剪切板工具
 */

public class ClipUtils {
    public static void copyText(Context context,String qrCode){
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型
        ClipData mClipData = ClipData.newPlainText("Label", qrCode);
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
        showIToast(context,"复制成功");
    }

}
