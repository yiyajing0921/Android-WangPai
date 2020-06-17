package com.woaiwangpai.iwb.wechat.share;

import com.woaiwangpai.iwb.wechat.share.callback.SocialLoginCallback;
import com.woaiwangpai.iwb.wechat.share.callback.SocialShareCallback;
import com.woaiwangpai.iwb.wechat.share.entities.ShareEntity;
import com.woaiwangpai.iwb.wechat.share.entities.ThirdInfoEntity;

/**
 * Created by arvinljw on 17/11/24 16:06
 * Function：
 * Desc：
 */
public interface ISocial {
    void login(SocialLoginCallback callback);

    ThirdInfoEntity createThirdInfo();

    void share(SocialShareCallback callback, ShareEntity shareInfo);

    void onDestroy();
}
