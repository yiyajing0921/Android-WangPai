package com.woaiwangpai.iwb.utils.loadsir;

import com.kingja.loadsir.callback.Callback;
import com.woaiwangpai.iwb.R;

/**
 * @Author : YiYaJing
 * @Data : 2020/6/17 8:54
 * @Email : yiyajing8023@163.com
 * @Description : 内容为空
 */

public class EmptyCallback extends Callback {
    @Override
    protected int onCreateView() {
        return R.layout.layout_empty;
    }
}
