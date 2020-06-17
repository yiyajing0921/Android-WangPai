package com.woaiwangpai.iwb.wechat.share.auth;

/**
 * 登录用户实体
 */
public class User {

    public String openid;

    public String nickName;

    public String avatar;

    public User(String openid, String nickName, String avatar){
        this.openid = openid;
        this.nickName = nickName;
        this.avatar = avatar;
    }
}
