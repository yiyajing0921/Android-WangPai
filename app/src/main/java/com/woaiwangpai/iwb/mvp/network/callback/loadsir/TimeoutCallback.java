package com.woaiwangpai.iwb.mvp.network.callback.loadsir;

import com.kingja.loadsir.callback.Callback;
import com.woaiwangpai.iwb.R;

/**
 * @author :Daniel_Y
 * @date ：2017/12/19 on 18:48
 * Description:
 */
public class TimeoutCallback extends Callback {
    @Override
    protected int onCreateView() {
        return R.layout.layout_timeout;
    }
}
