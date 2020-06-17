package com.woaiwangpai.iwb.mvp.network.callback.loadsir;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.kingja.loadsir.callback.Callback;
import com.woaiwangpai.iwb.R;

/**
 * @author zjy
 * @date 2018/3/16
 */

public class LoadingCallback extends Callback {
    @Override
    protected int onCreateView() {
        return R.layout.layout_loading;
    }

    @Override
    protected void onViewCreate(Context context, View view) {
        super.onViewCreate(context, view);
        ImageView ivLoading = view.findViewById(R.id.iv_loading);
//        Glide.with(view).load(R.drawable.page_loading).into(ivLoading);
    }

    @Override
    public void onAttach(Context context, View view) {
        ImageView ivLoading = view.findViewById(R.id.iv_loading);
//        Glide.with(view).load(R.drawable.page_loading).into(ivLoading);
        super.onAttach(context, view);
    }
}
