package com.woaiwangpai.iwb.message.helper;

import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.component.face.CustomFace;
import com.tencent.qcloud.tim.uikit.component.face.CustomFaceGroup;
import com.tencent.qcloud.tim.uikit.config.CustomFaceConfig;
import com.tencent.qcloud.tim.uikit.config.TUIKitConfigs;

public class ConfigHelper {

    public ConfigHelper() {

    }

    public TUIKitConfigs getConfigs() {
        return TUIKit.getConfigs().setCustomFaceConfig(initCustomFaceConfig());
    }

    private CustomFaceConfig initCustomFaceConfig() {
        //创建一个表情组对象
//        CustomFaceGroup faceConfigs = new CustomFaceGroup();
//        //设置表情组每页可显示的表情列数
//        faceConfigs.setPageColumnCount(5);
//        //设置表情组每页可显示的表情行数
//        faceConfigs.setPageRowCount(2);
//        //设置表情组号
//        faceConfigs.setFaceGroupId(1);
//        //设置表情组的主ICON
//        faceConfigs.setFaceIconPath("4349/xx07@2x.png");
//        //设置表情组的名称
//        faceConfigs.setFaceIconName("4350");
//        for (int i = 1; i <= 15; i++) {
//            //创建一个表情对象
//            CustomFaceConfig faceConfig = new CustomFaceConfig();
//            String index = "" + i;
//            if (i < 10)
//                index = "0" + i;
//            //设置表情所在Asset目录下的路径
//            faceConfig.setAssetPath("4349/xx" + index + "@2x.png");
//            //设置表情所名称
//            faceConfig.setFaceName("xx" + index + "@2x");
//            //设置表情宽度
//            faceConfig.setFaceWidth(240);
//            //设置表情高度
//            faceConfig.setFaceHeight(240);
//            faceConfigs.addCustomFace(faceConfig);
//        }
//        groupFaces.add(faceConfigs);

        CustomFaceConfig config = new CustomFaceConfig();
        //自定义表情包
        CustomFaceGroup faceConfigs = new CustomFaceGroup();
        faceConfigs.setPageColumnCount(5);
        faceConfigs.setPageRowCount(2);
        faceConfigs.setFaceGroupId(1024);
        faceConfigs.setFaceIconPath("4350/tt01@2x.png");
        faceConfigs.setFaceIconName("4350");
        for (int i = 0; i <= 16; i++) {
            CustomFace customFace = new CustomFace();
            String index = "" + i;
            if (i < 10) {
                index = "0" + i;
            }
            customFace.setAssetPath("4350/tt" + index + "@2x.png");
            customFace.setFaceName("tt" + index + "@2x");
            customFace.setFaceWidth(170);
            customFace.setFaceHeight(170);
            faceConfigs.addCustomFace(customFace);
        }
        config.addFaceGroup(faceConfigs);

        //自定义表情
//        CustomFaceGroup faceConfigBean = new CustomFaceGroup();
//        faceConfigBean.setPageColumnCount(5);
//        faceConfigBean.setPageRowCount(2);
//        faceConfigBean.setFaceGroupId(1023);
//        faceConfigBean.setLikeIconPath(R.drawable.icon_im_heard);
//        faceConfigBean.setFaceIconName("icon_im_heard");
//        CustomFace customFace = new CustomFace();
//        customFace.setLikePath(R.drawable.icon_add_brow);
//        customFace.setFaceName(CharacterUtils.ADD_LIKE_FACE);
//        customFace.setFaceWidth(150);
//        customFace.setFaceHeight(150);
//        faceConfigBean.addCustomFace(customFace);
//        config.addFaceGroup(faceConfigBean);
        return config;
    }

}
