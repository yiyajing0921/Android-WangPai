package com.woaiwangpai.iwb.utils;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import static java.util.regex.Pattern.compile;

/**
 * @Author : YiYaJing
 * @Data : 2020/6/17 8:50
 * @Email : yiyajing8023@163.com
 * @Description : 过滤器工具类
 */

public class FilterUtils {
    /**
     * 禁止输入表情
     *
     * @return
     */
    public static InputFilter emojiFilter() {
        InputFilter emojiFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                for (int index = start; index < end; index++) {
                    int type = Character.getType(source.charAt(index));
                    if (type == Character.SURROGATE) {
                        return "";
                    }
                }
                return null;
            }
        };
        return emojiFilter;
    }

    /**
     * 设置EditText可输入长度
     *
     * @param et
     * @param length
     */
    public static void setEditLength(EditText et, int length) {
        et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(length), emojiFilter()});
    }
    /**
     * 限制edittext 不能输入中文
     * @param editText
     */
    public static void setEdNoChinaese(final EditText editText){
        TextWatcher textWatcher=new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String txt = s.toString();
                //注意返回值是char数组
                char[] stringArr = txt.toCharArray();
                for (int i = 0; i < stringArr.length; i++) {
                    //转化为string
                    String value = new String(String.valueOf(stringArr[i]));
                    Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
                    Matcher m = p.matcher(value);
                    if (m.matches()) {
                        editText.setText(editText.getText().toString().substring(0, editText.getText().toString().length() - 1));
                        editText.setSelection(editText.getText().toString().length());
                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        editText.addTextChangedListener(textWatcher);
    }

    /**
     * 英文数字昵称
     *
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static String stringFilter(String str) throws PatternSyntaxException {
        String regEx = "[^a-zA-Z0-9\u4E00-\u9FA5-]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }
    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
        //"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        String telRegex = "[1][3456789]\\d{9}";
        if (TextUtils.isEmpty(mobiles)) {
            return false;
        }else if (mobiles.length()!=11){
            return false;
        }else {
            return mobiles.matches(telRegex);
        }
    }

    /**
     *  //微信名 6-20位
     * @param souce
     * @return
     */
    public static boolean StringWX_Name(String souce) {
        /*规则可以使用6—20个字母、数字、下划线和减号，必须以字母开头（不区分大小写），不支持设置中文*/
        String reg = "^[a-zA-Z][a-zA-Z0-9_-]{5,19}$";
        //第一位必须字母 +后边字母数字下划线减号
        boolean result = false;
        if((souce).matches(reg)) {
            result=true;
        }
        return result;
    }


    /**
     * 隐藏部分手机号133***1311
     * @param phoneNumber
     * @return
     */
    public static String hidePartPhone(String phoneNumber) {
        String phoneHidePart = phoneNumber.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        return phoneHidePart;
    }

    /**
     * 是否是英文字母
     * @param str
     * @return
     */
    public static boolean isEnglishLetter(String str) {
        Pattern pattern = compile("[a-zA-Z]");
        return pattern.matcher(str).matches();
    }

    //上传图片获取图片名称
    public static String getImgName(String path) {
        return path.substring(path.lastIndexOf("/") + 1, path.length());
    }


    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
    //dp转px 同上
    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        //当前屏幕密度因子
        return (int) (dp * scale + 0.5f);
    }

    public static int dpi(Context context) {
        //当前屏幕密度
        return (int) context.getResources().getDisplayMetrics().densityDpi;
    }

    public static float density(Context context) {
//        dpi/160的结果
        //当前屏幕密度
        return (float) context.getResources().getDisplayMetrics().density;
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

}
