package com.woaiwangpai.iwb.wechat.share.utils;

import com.woaiwangpai.iwb.constant.Constant;
import com.woaiwangpai.iwb.wechat.share.SocialHelper;

/**
 * Created by arvinljw on 17/11/27 17:33
 * Function：
 * Desc：
 * @author Administrator
 */

public enum SocialUtil {
    /**
     *
     */
    INSTANCE();
    /**
     *
     */
    public SocialHelper socialHelper;

    /**
     *
     */
    SocialUtil() {
        socialHelper = new SocialHelper.Builder()
                .setQqAppId(Constant.QQ_APP_ID)
//                .setQqAppId("qqAppId")
                .setWxAppId(Constant.WEIXIN_APP_ID)
//                .setWxAppId("wxAppId")
                .setWxAppSecret(Constant.WEIXIN_APP_SECRET)
//                .setWxAppSecret("wxAppSecret")
                .setWbAppId(Constant.WEIBO_APP_KEY)
//                .setWbAppId("wbAppId")
                .setWbRedirectUrl(Constant.WEIBO_RESULT_URL)
                .build();
    }
}
