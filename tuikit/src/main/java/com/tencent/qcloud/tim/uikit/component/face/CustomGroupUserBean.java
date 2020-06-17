package com.tencent.qcloud.tim.uikit.component.face;

import java.util.List;

/**
 * @Author : YiYaJing
 * @Data : 2020/1/2 13:35
 * @Description: 自定义推送名片
 */
public class CustomGroupUserBean {
    /**
     * 标识符
     */
    private String version;

    private String beginMessage;
    private List<DataBean> dataBeans;

    public String getBeginMessage() {
        return beginMessage == null ? "" : beginMessage;
    }

    public void setBeginMessage(String beginMessage) {
        this.beginMessage = beginMessage;
    }

    public String getVersion() {
        return version == null ? "" : version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<DataBean> getDataBeans() {
//        if (dataBeans == null) {
//            return new ArrayList<>();
//        }
        return dataBeans;
    }

    public void setDataBeans(List<DataBean> dataBeans) {
        this.dataBeans = dataBeans;
    }

    public static class DataBean {
        /**
         * 账户
         */
        private String userNickNameOrRemark;
        /**
         * 全部消息内容 @的成员 + 消息
         */
        private String desc;
        /**
         * 成员名
         */
        private String userName;
        /**
         * 消息内容
         */
        private String msg;

        public String getMsg() {
            return msg == null ? "" : msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getUserNickNameOrRemark() {
            return userNickNameOrRemark == null ? "" : userNickNameOrRemark;
        }

        public void setUserNickNameOrRemark(String userNickNameOrRemark) {
            this.userNickNameOrRemark = userNickNameOrRemark;
        }

        public String getDesc() {
            return desc == null ? "" : desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getUserName() {
            return userName == null ? "" : userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        @Override
        public String toString() {
            return desc;
        }
    }

}
