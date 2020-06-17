package com.woaiwangpai.iwb.utils;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author : YiYaJing
 * @Data : 2020/6/17 8:49
 * @Email : yiyajing8023@163.com
 * @Description : 局部text 颜色和点击事件
 */

public class MyClickSpan extends ClickableSpan {

    private int mHighLightColor =/*Color.RED;*/Color.parseColor("#FF4042");

    private boolean mUnderLine = false;

    private View.OnClickListener mClickListener;


    public MyClickSpan(View.OnClickListener listener) {

        this.mClickListener = listener;

    }

    /**
     *
     * @param color 高亮颜色
     * @param underline 下划线
     * @param listener 监听
     */
    public MyClickSpan(int color, boolean underline, View.OnClickListener listener) {

        this.mHighLightColor = color;

        this.mUnderLine = underline;

        this.mClickListener = listener;

    }


    @Override

    public void onClick(View widget) {

        if (mClickListener != null)

            mClickListener.onClick(widget);

    }


    @Override

    public void updateDrawState(TextPaint ds) {

        ds.setColor(mHighLightColor);

        ds.setUnderlineText(mUnderLine);

    }

    public static void setTextColorWithSpan(TextView tv, String text, String keyWord,String keyWord2) {

        tv.setClickable(true);

        tv.setHighlightColor(Color.TRANSPARENT);

        tv.setMovementMethod(LinkMovementMethod.getInstance());

        SpannableString s = new SpannableString(text);

        Pattern p = Pattern.compile(keyWord);

        Matcher m = p.matcher(s);

        while (m.find()) {
            int start = m.start();
            int end = m.end();
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#FF4042"));
            s.setSpan(foregroundColorSpan, start,end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        Pattern p2 = Pattern.compile(keyWord2);
        Matcher m2 = p2.matcher(s);
        while (m2.find()) {
            int start = m2.start();
            int end = m2.end();
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#FF4042"));
            s.setSpan(foregroundColorSpan, start,end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        tv.setText(s);

    }
    public static void setTextHighLightWithClick(TextView tv, String text, String keyWord, View.OnClickListener listener) {

        tv.setClickable(true);

        tv.setHighlightColor(Color.TRANSPARENT);

        tv.setMovementMethod(LinkMovementMethod.getInstance());

        SpannableString s = new SpannableString(text);

        Pattern p = Pattern.compile(keyWord);

        Matcher m = p.matcher(s);

        while (m.find()) {

            int start = m.start();

            int end = m.end();

            s.setSpan(new MyClickSpan(listener), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        }
        tv.setText(s);

    }
    /** 使用
     *         setTextHighLightWithClick(tv, "若照片显示为反转，请点击此处\"旋转\"，再进行提交", "\"旋转\"", new View.OnClickListener() {
     *             @Override
     *             public void onClick(View view) {
     *                // to do
     *             }
     *         });
     */
}
