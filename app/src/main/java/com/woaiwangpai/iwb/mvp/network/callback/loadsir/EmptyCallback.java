package com.woaiwangpai.iwb.mvp.network.callback.loadsir;

import com.kingja.loadsir.callback.Callback;
import com.woaiwangpai.iwb.R;

/**
 * @author zjy
 * @date 2017/12/8
 */

public class EmptyCallback extends Callback {
    @Override
    protected int onCreateView() {
        return R.layout.layout_empt;
    }
}
