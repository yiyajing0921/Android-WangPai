package com.woaiwangpai.iwb.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.woaiwangpai.iwb.R;


/**
 * @author :Daniel_Y
 * @date ：2018/1/4 on 16:43
 * Description: 公用的弹窗
 */
public class CommomDialog extends Dialog implements View.OnClickListener {
    private ImageView rlHeight;
    private TextView contentTxt;
    private TextView submitTxt;
    private Context mContext;
    private OnConfirmListener confirmListener;
    //    private OnCancelListener cancelListener;
    private String positiveName;
    private String negativeName;
    private String title;
    private Boolean isVisiable;
    private int resId;
    private int height;
    private boolean mHide = false;
    private boolean outside = false;

    public CommomDialog(Context context, int themeResId, String title, int height, OnConfirmListener listener) {
        super(context, themeResId);
        this.resId = themeResId;
        this.mContext = context;
        this.title = title;
        this.height = height;
        this.confirmListener = listener;
    }

    public CommomDialog(Context context, String title, int height, OnConfirmListener listener) {
        super(context);
        this.mContext = context;
        this.title = title;
        this.height = height;
        this.confirmListener = listener;
    }

    public CommomDialog setPositiveButton(String name) {
        this.positiveName = name;
        return this;
    }

    public CommomDialog setNegativeButton(String name) {
        this.negativeName = name;
        return this;
    }

    public CommomDialog setCanceledonTouchOutside(boolean outside) {
        this.outside = outside;
        return this;
    }

    public void setOnConfirmListener(OnConfirmListener listener) {
        this.confirmListener = listener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (resId != 0) {
            setContentView(resId);
        }else {
            setContentView(R.layout.dialog_approve);
        }

        initView();
    }

    public CommomDialog hideOrCancel(boolean hide) {
        this.mHide = hide;
        return this;
    }

    private void initView() {
        rlHeight = findViewById(R.id.rl_height);
        contentTxt = findViewById(R.id.tv_content);
        submitTxt = findViewById(R.id.tv_submit);

        Window window = this.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        this.getWindow().setAttributes(lp);

        submitTxt.setOnClickListener(this);
        if (!TextUtils.isEmpty(positiveName)) {
            submitTxt.setText(positiveName);
        }
        if (!TextUtils.isEmpty(negativeName)) {
//            submitTxt.setBackgroundResource(R.drawable.shape_bg_dialog);
        }
        if (!TextUtils.isEmpty(title)) {
            contentTxt.setText(title);
        }
//        if (isVisiable != null) {
//            mCancel.setVisibility(isVisiable ? View.VISIBLE : View.GONE);
//        }

        if (height != 0) {
            ViewGroup.LayoutParams layoutParams = rlHeight.getLayoutParams();
            layoutParams.height = FilterUtils.dp2px(mContext, height);
            rlHeight.setLayoutParams(layoutParams);
        }

    }

    /**
     * 判断取消按钮是否显示
     *
     * @param visiable
     */
    public void setCancelVisiable(boolean visiable) {
        this.isVisiable = visiable;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_submit) {
            if (confirmListener != null) {
                confirmListener.onClick(this, true);
            }
        }
    }

    public interface OnConfirmListener {
        void onClick(Dialog dialog, boolean confirm);
    }
}
