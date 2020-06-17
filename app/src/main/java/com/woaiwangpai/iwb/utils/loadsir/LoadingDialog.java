package com.woaiwangpai.iwb.utils.loadsir;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.woaiwangpai.iwb.MyApplication;
import com.woaiwangpai.iwb.R;

/**
 * @Author : YiYaJing
 * @Data : 2020/6/17 8:54
 * @Email : yiyajing8023@163.com
 * @Description : 正在加载得loading
 */

public class LoadingDialog {
    private Dialog loadingDialog = MyApplication.getInstance().dialog;
    private Activity context;
    private String msg;
    private ImageView img_loading;
    private AlertDialog.Builder loadingAlerDialog;

    public LoadingDialog(Activity context) {
        this.context = context;
        //TODO:暂时注释
//        setLoadingDialog();
    }

    public LoadingDialog(Activity context, boolean isBase) {
        this.context = context;
    }


    public AlertDialog.Builder setLoadingDialog(View view) {
        loadingAlerDialog = new AlertDialog.Builder(context, AlertDialog.BUTTON_NEUTRAL);
        loadingAlerDialog.setView(view);
        TextView title = new TextView(context);
        title.setGravity(Gravity.CENTER);
        title.setText("修改会议室名称");
        loadingAlerDialog.setCustomTitle(title);
        return loadingAlerDialog;
    }

    /**
     * 会议室
     */
    public Dialog setLoadingDialog(View view, boolean isdialog) {
        // 创建自定义样式dialog
        loadingDialog = new Dialog(context, R.style.Theme_AppCompat_Dialog);
        // 是否可以按“返回键”消失
        loadingDialog.setCancelable(true);
        // 点击加载框以外的区域
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.setContentView(view);
        return loadingDialog;
    }

//    private void setLoadingDialog() {
//        LayoutInflater inflater = LayoutInflater.from(context);
//        // 得到加载view
//        View v = inflater.inflate(R.layout.dialog_loading, null);
//        // 加载布局
//        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_loading_view);
//        img_loading = (ImageView) v.findViewById(R.id.img_loading);
//        Glide.with(context).load(R.mipmap.page_loading).into(img_loading);
//        // 创建自定义样式dialog
//        if (loadingDialog == null) {
//            loadingDialog = new Dialog(context, R.style.LoadingDialogStyle);
//        }
//        // 是否可以按“返回键”消失
//        loadingDialog.setCancelable(true);
//        // 点击加载框以外的区域
//        loadingDialog.setCanceledOnTouchOutside(false);
//        // 设置布局
//        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.MATCH_PARENT));
//        Window window = loadingDialog.getWindow();
//        WindowManager.LayoutParams lp = window.getAttributes();
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        window.setGravity(Gravity.CENTER);
//        window.setAttributes(lp);
//        window.setWindowAnimations(R.style.LoadingDialogAnimStyle);
//    }

    /**
     * prams1  单个还是双按键
     * prams2  显示的内容
     * TODO:暂时注释
     */
    /*public Dialog setLoginDialog(int oneOrTwo, String text) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_login, null);
        CenterTextView content = view.findViewById(R.id.content);
        content.setText(text);
        TextView sure = view.findViewById(R.id.srue);
        TextView cancel = view.findViewById(R.id.cancel);
        cancel.setOnClickListener(v -> {
            App.getInstance().mUser.setIsLogin(false);
            loadingDialog.dismiss();
            loadingDialog.cancel();
        });
        sure.setOnClickListener(v -> {
            loadingDialog.dismiss();
            AppManager.getAppManager().finishAllActivity();
            LoginsActivity.start(context);
        });
        if (oneOrTwo == 1) {
            sure.setVisibility(View.GONE);
        } else {
            sure.setVisibility(View.VISIBLE);
        }
        // 创建自定义样式dialog
        loadingDialog = new Dialog(context, R.style.Theme_AppCompat_Dialog);
        // 是否可以按“返回键”消失
        loadingDialog.setCancelable(false);
        // 点击加载框以外的区域
        loadingDialog.setCanceledOnTouchOutside(false);
        loadingDialog.setContentView(view, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        return loadingDialog;
    }*/
/*
//TODO:暂时注释
    private void setLoadingDialog() {
        LayoutInflater inflater = LayoutInflater.from(context);
        // 得到加载view
        View v = inflater.inflate(R.layout.dialog_loading, null);
        // 加载布局
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_loading_view);
        img_loading = (ImageView) v.findViewById(R.id.img_loading);
        Glide.with(context).load(R.drawable.page_loading).into(img_loading);
        // 创建自定义样式dialog
        if (loadingDialog == null) {
            loadingDialog = new Dialog(context, R.style.LoadingDialogStyle);
        }
        // 是否可以按“返回键”消失
        loadingDialog.setCancelable(true);
        // 点击加载框以外的区域
        loadingDialog.setCanceledOnTouchOutside(false);
        // 设置布局
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        Window window = loadingDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.CENTER);
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.LoadingDialogAnimStyle);
    }*/

    public void show() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
        if (!context.isFinishing() && !loadingDialog.isShowing()) {
            loadingDialog.show();
        } else {
            loadingDialog.dismiss();
        }
    }

    public void close() {
        if (!context.isFinishing() && loadingDialog.isShowing()) {
            loadingDialog.cancel();
        }
    }

    public boolean isShow() {
        return loadingDialog.isShowing();
    }
}
