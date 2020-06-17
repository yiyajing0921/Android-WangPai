package com.woaiwangpai.iwb.wechat.bean;

/**
 * @Author : YiYaJing
 * @Data : 2020/6/17 9:18
 * @Email : yiyajing8023@163.com
 * @Description : 微信登录实体
 */
public class WeChatLoginBean {
    /**
     * code : 1001
     * msg : 登录成功  已绑定
     * data : {"username":"testeyelinersss","nickname":"测试的眼线昵称","avatar":"","district":"","eyeliner":"","type":"眼线","gender":"2","birthday":"0000-00-00","token":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJmbWFwaS51Y2FpamlhLmNvbSIsInVpZCI6Mzg1NTMyLCJpYXQiOjE1NTI0Njc0NTcsImV4cCI6MTU1MzA3MjI1N30.u8xzknJBx5HzEEhbC-ePtvaIpU7A5YtW3mMl5Eexa1g"}
     * time : 1552467457
     */
    /**
     * code : 1001
     * msg : 登录成功  未绑定
     * data : {"openid":"okUAxs1mNGsIt0Es_6JQBCyhZbno","unionid":"oNbLJwmukvapKIiaqXAoQ1rp6P84","nickname":"« 子不语","avatar":"http://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTK3nCbqUphLxibvXMCdK5iaYqHiabeqXV5nCIk1EtbVPl2VzZCYppUPibxYYYiadMibeblmMib3S6iaSg4n4Q/132"}
     * time : 1552467574
     */
    private String code;
    private String msg;
    private DataBean data;
    private String time;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public static class DataBean {
        /**未绑定
         * openid : okUAxs1mNGsIt0Es_6JQBCyhZbno
         * unionid : oNbLJwmukvapKIiaqXAoQ1rp6P84
         * nickname : « 子不语
         * avatar : http://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTK3nCbqUphLxibvXMCdK5iaYqHiabeqXV5nCIk1EtbVPl2VzZCYppUPibxYYYiadMibeblmMib3S6iaSg4n4Q/132
         */

        private String openid;
        private String unionid;
        private String nickname;//都有
        private String avatar;//都有

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }

        public String getUnionid() {
            return unionid;
        }

        public void setUnionid(String unionid) {
            this.unionid = unionid;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        /** 已绑定
         * username : testeyelinersss
         * nickname : 测试的眼线昵称
         * avatar :
         * district :
         * eyeliner :
         * type : 眼线
         * gender : 2
         * birthday : 0000-00-00
         * token : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJmbWFwaS51Y2FpamlhLmNvbSIsInVpZCI6Mzg1NTMyLCJpYXQiOjE1NTI0Njc0NTcsImV4cCI6MTU1MzA3MjI1N30.u8xzknJBx5HzEEhbC-ePtvaIpU7A5YtW3mMl5Eexa1g
         */

        private String username;
//        private String nickname;
//        private String avatar;
        private String district;
        private String eyeliner;
        private String type;
        private String gender;
        private String birthday;
        private String token;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }


        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getEyeliner() {
            return eyeliner;
        }

        public void setEyeliner(String eyeliner) {
            this.eyeliner = eyeliner;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }

}
