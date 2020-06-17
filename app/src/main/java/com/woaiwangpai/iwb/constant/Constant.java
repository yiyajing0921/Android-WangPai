package com.woaiwangpai.iwb.constant;

import android.Manifest;

/**
 * 项目名 MyBase
 * 所在包 com.woaiwangpai.iwb.contans
 *
 * @author mahongyin
 * time: 2019/8/22 11:27
 * email: mhy.work@qq.com
 * 描述 说明:
 */
public class Constant {
    /**
     * 支付类型1.支付宝  2.钱包 3.微信
     * 1支付宝2余额支付3微信
     */
    public static int ALI_PAY = 1, CARD = 2, WECHAT = 3;
    public static String[] ALLPERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_CALENDAR, Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_CONTACTS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_SMS, Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA, Manifest.permission.CALL_PHONE, Manifest.permission.SEND_SMS};
    //    String[]gg={"",""};
    public static String[] CAMERA_PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    public static String[] RECORD_AUDIO_PERMISSIONS = new String[]{Manifest.permission.RECORD_AUDIO//录音机   视频需要
    };
    //三方登录前需要
    public static String[] INIT_PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE};
    //短信倒计时
    public static int SMS_TIME = 60;
    public static final String HTTPCACHE = "Httpcache";
    public static final int OVERLAY_PERMISSION_REQ_CODE = 108;
    public static String NETWORKLOW = "网络不给力";
    public static String ASSETS_PROVINCE = "province.json";
    public static String ASSETS_CHINA_CITY = "china_city_data.json";
    public static String CHINA_CITY = "china_city.json";
    public static String CITY_JSON = "city.json";
    public static final String DRAFTSTRING = "ChatDraft";
    //线上
    public static String BASE_URL = "https://api.woaiwangpai.com";
    public static String WEB_URL = "https://web.woaiwangpai.com";

    //自动分配地址  debug包是测试地址、  releas包线上地址
//    public static String BASE_URL = BuildConfig.apiUrl;
//    public static String WEB_URL = BuildConfig.webUrl;

    /**
     * 加载web
     */
    //返款规则
    public static String BACKMONEY_RULE = BASE_URL + "/refund.html";
    //注册协议
    public static String REGISTER_RULE = BASE_URL + "/page/RegisterProtocol.html";
    //爱豆规则
    //public static String IDOU_RULE_WEB = BASE_URL + "/page/IntegralRule.html";
    public static String IDOU_RULE_WEB = WEB_URL + "/beanrules";
    //用户隐私协议
    public static String USERYS_XY_WEB = BASE_URL + "/privacy.html";
    //牛人抽奖
//    public static String COW_DRAW_WEB = WEB_URL + "/out/manage/lottery/lottery" + "?app=android&token=" + MyApplication.getInstance().getToken();
    //我的个人中心
    public static String MINE_URL = WEB_URL + "/mine?app=android&token=";
    //任务详情
    public static String TASK_DETAIL_URL = WEB_URL + "/detail?app=android&token=";//_+id+type+keywprd
    //任务页
    public static String TASKWEB_URL = WEB_URL + "/task?app=android&token=";
    public static String HOMEWEB_URL = WEB_URL + "/index?app=android&token=";
    public static String TASKWEB = WEB_URL + "/task";//没问号
    public static String HOMEWEB = WEB_URL + "/index";//没问号
    public static String TASKDETAILWEB = WEB_URL + "/detail?";//有问号
    //任务直播
    public static final String LIVE_TASK = WEB_URL + "/live";
    //任务说明/description?type=买家秀
    public static final String DETAIL_TYPR = WEB_URL + "/description?type=";
    /**
     * API
     */
    public static String QQ_APP_ID = "101807669";//QQ开放平台的APP_ID 1106556586//mybase101807669//rele1107492512
    public static String QQ_APP_KEY = "6fff04a5ad90bdff081cc78544057d2d";//QQ开放平台的APP_ID
    public static String WEIBO_APP_KEY = "4092786483";//微博开放平台的APP_key//0c7daf8aecca98536708479e42286b81
    public static String WEIBO_RESULT_URL = "https://api.weibo.com/oauth2/default.html";//微博返回URL

    public static String WEIXIN_APP_ID = "wxbddb62b534debb5f";//车wx58fd613158c1dde6//我wx480966b2c042ac78 //web wxbddb62b534debb5f
    public static final String WEIXIN_APP_SECRET = "cf3e241cbf0048dba9164fca50bcbe60";//车239bd40d06b7447073f3da26652dc8ba

    /**
     * 接口访问成功的回调code
     */
    public static final String INTERFACE_REQUEST_CODE = "1";
    //身份
    public static final String modelString = "模特";
    public static final String eyeLinerString = "眼线";
    public static final String NOTICE_TYPE = "ApproveNoticeType";
    public static final String REFRESH_WEB = "refresh";
    public static final String REFRESH_MAIN_WEB = "mianRefresh";
    public static final String REFRESH_TASK_WEB = "taskRefresh";
    /**
     * 消息
     */
    //消息通知
    public static final String NOTICE_URL = BASE_URL + "/api/message/unreadNumNew";

    //消息通知详情
    public static final String NOTICE_DETAILS_URL = BASE_URL + "/api/message/listNew";

    //消息通知详情
    public static final String NOTICE_TASK_PROGRESS = BASE_URL + "/api/message/list";

    //消息通知数量
    public static final String NOTICE_NUMBER = BASE_URL + "/api/message/unreadNumber";

    //认证是否通过
    public static final String APPROVE_MESSAGE = BASE_URL + "/api/common/auth/check";

    //新结构的消息通知
    public static final String NOTICE_DETAILS_URL_NEW = BASE_URL + "/api/message/unreadNumNewA";

    // 系统通知列表
    public static String SYS_NOTI_POSTURL = BASE_URL + "/api/notice/index";
    //每月25号降级通知提醒
    public static String DOWN_NOTI_POSTURL = BASE_URL + "/api/propagate/downgraderemind";

    /**
     * 登录登出
     */
    //登录说明
    public static String LOGIN_TALK_GETURL = BASE_URL + "/api/login/explain";
    //退出登录
    public static String UNLOGIN_GETURL = BASE_URL + "/api/logout";
    //    找回密码-设置新密码
    public static String SET_PASSWORD_POSTURL = BASE_URL + "/api/forgotPassword";
    //    找回密码-验证手机号
    public static String PHONE_VERIFY_POSTURL = BASE_URL + "/api/phoneVerify";
    // 第三方登录绑定
    public static String BIND_POSTURL = BASE_URL + "/api/oauth/bind";
    //    手机号注册（三方绑定手机号注册） 弃用
    public static String REGISTER_POSTURL = BASE_URL + "/api/register";

    //    注册第一步-验证手机号和邀请码
    public static String REGISTER1_POSTURL = BASE_URL + "/api/registerStep1";
    //    注册第二步-设置用户名及密码
    public static String REGISTER2_POSTURL = BASE_URL + "/api/registerStep2";

    //账号登录
    public static String LOGIN_POSTURL = BASE_URL + "/api/login";
    //短信验证登录
    public static String MSM_LONGIN_GETURL = BASE_URL + "/api/oauth/phone/login";
    //短信验证码 from：reg /空
    public static String MSM_CODE_GETURL = BASE_URL + "/api/oauth/message";
    //微信绑定 弃用
    public static String WEIXIN_BIND_POSTURL = BASE_URL + "/api/wechat/bind";
    //微信登录
    public static String WEIXIN_LOGIN_GETURL = BASE_URL + "/api/login/wechat";
    //第三方登录应用id
    public static String THREE_LOGIN_INFO_GETURL = BASE_URL + "/api/oauth/info";
    //第三方登录
    public static String THREE_LOGIN_GETURL = BASE_URL + "/api/oauth/thirdLogin";
    /**
     * 个人中心
     */
    // 获取账号详情
    public static String USER_INFO_POSTURL = BASE_URL + "/api/user/info";
    // 账号详情更改
    public static String USER_UPDATE_POSTURL = BASE_URL + "/api/user/update";
    //用户show
    public static String USER_SHOW_GETURL = BASE_URL + "/api/user/show";
    //  绑定状态
    public static String BIND_STATE_GETURL = BASE_URL + "/api/user/bind/state";

    //  解除绑定
    public static String UNBIND_GETURL = BASE_URL + "/api/user/unbind";
    /**
     * 外宣
     */

    // 我的外宣首页
    public static String PUBLICITY_INDEX_POSTURL = BASE_URL + "/api/propagate/index";
    //获取外宣列表  弃用
    public static String PUBLICITY_LIST_POSTURL = BASE_URL + "/api/user/publicity/list";
    // 外宣收益接口
    public static String PUBLICITY_SALE_POSTURL = BASE_URL + "/api/propagate/profit";
    // 个人邀请二维码识别页面
    public static String READQRCODE_POSTURL = BASE_URL + "/api/propagate/readqrcode";
    // 显示个人邀请二维码
    public static String QRCODE_POSTURL = BASE_URL + "/api/propagate/qrcode";
    //炫耀接口
    public static String FLAUNT_POSTURL = BASE_URL + "/api/propagate/flaunt";
    //外宣邀请记录
    public static String INVITER_POSTURL = BASE_URL + "/api/propagate/invite";
    // 外宣分享邀请
    public static String INVITERSHARE_POSTURL = BASE_URL + "/api/propagate/inviteshare";
    // 外宣排名
    public static String RANK_POSTURL = BASE_URL + "/api/propagate/ranking";
    //添加外宣 登记，去掉transfer_date字段
    public static String CREATE_POSTURL = BASE_URL + "/api/user/publicity/create";

    // 用户外宣数据pk接口
    public static String PK_POSTURL = BASE_URL + "/api/propagate/pk";

    //获取外宣团队和组
    public static String TEAM_GETURL = BASE_URL + "/api/user/publicity/team";

    // 营销素材列表
    public static String PUBLICITYSHARELIST = BASE_URL + "/api/propagate/marketinglist";
    // 营销素材分享统计
    public static String PUBLICITYSHARECOUNT = BASE_URL + "/api/propagate/marketingcount";
    // 外宣领奖记录
    public static String PRIZERECORD = BASE_URL + "/api/propagate/drawlist";
    // 外宣申请信息
    public static String APPLY_INFO_POST = BASE_URL + "/api/propagate/applyinfo";

    //外宣升级申请审核
    public static String APPLY_AUTH_POST = BASE_URL + "/api/propagate/authapply";
    //外宣申请通知列表
    public static String APPLY_LIST_POST = BASE_URL + "/api/propagate/applylist";

    // 外宣通知查看申请详情
    public static String APPLY_DETAIL_POST = BASE_URL + "/api/propagate/readapply";
    //外宣牛人抽奖显示信息
    public static String COW_DRAW_POSTURL = BASE_URL + "/api/propagate/drawinfo";

    //外宣返还费用信息显示
    public static String REFUND_POSTURL = BASE_URL + "/api/propagate/refundinfo";

    //外宣返还费用申请提交
    public static String APPLYREFUND_POSTURL = BASE_URL + "/api/propagate/applyrefund";

    //外宣免费名额列表
    public static String COUPONLIST_POSTURL = BASE_URL + "/api/propagate/couponlist";

    //外宣领取免费名额
    public static String COUPON_POSTURL = BASE_URL + "/api/propagate/getcoupon";
    //外宣列表
    public static String OLDOUT_URL = WEB_URL + "/old/out";
    /**
     * 首页任务页搜人
     */
    //获取引导图
    public static String GUIDE_POSTURL = BASE_URL + "/api/propagate/getguide";
    //伦比图接口
    public static String BANNER_GETURL = BASE_URL + "/api/slide";
    //首页任务
    public static String TASK_HOME_POSTURL = BASE_URL + "/api/task/home";
    //任务页获取任务列表
    public static String TASK_POSTURL = BASE_URL + "api/task/list";
    //获取任务列表 new
    public static String TASK_NEW_POSTURL = BASE_URL + "/api/task/list_new";
    //获取任务列表 new2
    public static String TASK_NEW2_POSTURL = BASE_URL + "/api/task/list_new2";
    //侧边栏任务搜索接口
    public static String TASK_SOTYPE_GETURL = BASE_URL + "/api/task/sotype";
    //任务推送list
    public static String TASK_PUSH_GETURL = BASE_URL + "/api/message/list";
    //任务详情
    public static String TASK_INFO_POSTURL = BASE_URL + "/api/task/info";
    //简历列表
    public static String RESUME_LIST_POSTURL = BASE_URL + "/api/user/resume_list";
    //保存简历请求验证码
    public static String RESUME_CODE_POSTURL = BASE_URL + "/api/user/resume/get_code";
    //是否可创建简历
    public static String RESUME_CREATE_POSTURL = BASE_URL + "/api/user/is_resume";
    //保存简历
    public static String RESUME_SAVE_POSTURL = BASE_URL + "/api/user/save_resume";
    //投递简历
    public static String RESUME_SEND_POSTURL = BASE_URL + "/api/user/send_resume";
    //详情——任务推荐
    public static String OTHERTASK_POSTURL = BASE_URL + "/api/task/otherTask";
    //发弹幕
    public static String BARRAGE_LIVE_POSTURL = BASE_URL + "/api/live/addbarrage";
    //            预计置顶时间
    public static String TOP_TIME_POSTURL = BASE_URL + "/api/task/expecttime";

    //            直播页数据列表
    public static String TASK_LIVE_POSTURL = BASE_URL + "/api/live/datalist";
    //接任务
    public static String TASK_TASK_POSTURL = BASE_URL + "/api/task/take";
    //任务广告富文本 弃用
    public static String ADVERT_TASK_GETURL = BASE_URL + "/api/task/advert";//?way=app+token=
    //关注
    public static String ATTENTION_GETURL = BASE_URL + "/api/model/attention";
    //关注列表
    public static String ATTENTIONLIST_GETURL = BASE_URL + "/api/model/attentionList";
    //搜搜
    public static String SEARCH_POSTURL = BASE_URL + "/api/account/searchNew";
    //首页广告
    public static String STARTADVERT_GETURL = BASE_URL + "/api/startAdvert";
    //会员 商家 印象列表
    public static String IMPRESSIONLIST_POSTURL = BASE_URL + "/api/account/ImpressionList";
    //设置印象
    public static String SETIMPRESSIONLIST_POSTURL = BASE_URL + "/api/account/setImpression";
    //删除印象
    public static String IMPRESSIONLISTDEL_POSTURL = BASE_URL + "/api/account/ImpressionDel";
    //可删除印象列表 将id改为i_id
    public static String IMPRESSIONLISTDELLIST_POSTURL = BASE_URL + "/api/account/ImpressionDelList";

    //http://web.fea.woaiwangpai.com  查看待处理任务详情
//    public static String MYTASK_URL = WEB_URL + "/user/model/task?nav=2&opt=0&token=" + MyApplication.getInstance().getToken();

    // 任务分享接口
    public static String TASK_SHARE_POSTURL = BASE_URL + "/api/task/share";

    /**
     * 爱豆中心
     */
    public static String IDOUCENTER_GETURL = BASE_URL + "/api/Integral/index";
    //充值爱豆生成订单接口
    public static String CREATEORDER_GETURL = BASE_URL + "/api/Integral/createOrder";
    // 购买爱豆
    public static String IDOUBUY_GETURL = BASE_URL + "/api/Integral/buy";
    //积分记录 爱豆记录
    public static String INTEGRALLIST = BASE_URL + "/api/Integral/list";
    //接收转让积分
    public static String GETRETUREINTEGRAL = BASE_URL + "/api/Integral/transferAcquire";
    //转让详情接口
    public static String RETUREINTEGRALINFO = BASE_URL + "/api/Integral/transferInfo";
    //转让积分申请
    public static String RETUREINTEGRALREQUST = BASE_URL + "/api/Integral/transferRequest";
    //积分数量查询
    public static String INTEGRALNUM = BASE_URL + "/api/Integral/num";
    //补签签到
    public static String SIGNIN_GETURL = BASE_URL + "/api/signIn/sign";
    //领取每日任务奖励
    public static String DAILYAWARD_GETURL = BASE_URL + "/api/Integral/takeDailyAward";
    //获取金额积分对应类型
    public static String RECHARGETYPE_GETURL = BASE_URL + "/api/Integral/rechargeType";
    //签到卡分享成功获取爱豆接口
    public static String SHARE_GETURL = BASE_URL + "/api/signIn/shareSuccess";
    //    爱豆兑换（目前仅有兑换体验会员卡）
    public static String EXCHANGE_GETURL = BASE_URL + "/api/Integral/exchange";
    /**
     * ------------IM------------------
     */
    //获取好友信息
    public static String GETFRIENDDETAIL = BASE_URL + "/api/im/getfriendinfo";
    //获取用户信息
    public static String GETUSERDETAIL = BASE_URL + "/api/im/getuserinfo";
    //获取用户vip信息
    public static String GETUSERDETAILVIP = BASE_URL + "/api/im/getusersvip";
    //批量获取群组用户详细信息
    public static String GETRROUPMEMBER = BASE_URL + "/api/im/getgroupmember";
    //更新好友备注
    public static String CHANGEREMARK = BASE_URL + "/api/im/updatefriend";
    //更新群内我的备注
    public static String CHANGEGROUPREMARK = BASE_URL + "/api/im/updategroupmember";
    //群主修改群组信息
    public static String CHANGEGROUPINFO = BASE_URL + "/api/im/updategroup";
    //设置群组公告
    public static String IM_SET_GROUP_NOTICE = BASE_URL + "/api/im/setgroupnotice";
    //上传单张图片
    public static String UPLOADPIC = BASE_URL + "/api/common/picture/upload";
    //邀请用户进群
    public static String INVITEGROUP = BASE_URL + "/api/im/invitemember";

    /**
     * ------------------------IM------------------------------
     */
    //im获取用户自定表情
    public static final String IM_GET_USER_CUSTOM_FACE = BASE_URL + "/api/im/getfaceimgs";
    //im添加表情
    public static final String IM_ADD_CUSTOM_FACE = BASE_URL + "/api/im/addfaceimg";
    //im删除用户自定义表情
        public static final String IM_DELETE_CUSTOM_FACE = BASE_URL + "/api/im/delfaceimg";
    //添加收藏
    public static final String IM_ADD_FAVORITE_DATA = BASE_URL + "/api/im/addcollect";
    //删除收藏
    public static final String IM_DELETE_FAVORITE_DATA = BASE_URL + "/api/im/delcollect";
    //腾讯IM接口获取群组成员列表
    public static final String GET_GROUP_USER_LIST = BASE_URL + "/api/im/groupmembers";
    //获取收藏列表
    public static final String GET_FAVORITE_LIST = BASE_URL + "/api/im/getcollectlist";
    //查看收藏详情
    public static final String GET_FAVORITE_DETAILS = BASE_URL + "/api/im/getcollect";
    //im获取群二维码
    public static final String IM_GET_GROUP_QR = BASE_URL + "/api/im/qrcode";
    //查看群组管理员
    public static final String IM_LOOK_GROUP_MANAGER = BASE_URL + "/api/im/getgroupmange";
    //取消设置群组管理员
    public static final String IM_IS_CANCEL_MANAGER = BASE_URL + "/api/im/setgroupmange";
    //批量取消设置群组管理员
    public static final String IM_LIST_CANCEL_MANAGER = BASE_URL + "/api/im/batchsetgroupmange";
    //批量禁言和取消禁言
    public static final String IM_LIST_MUTED = BASE_URL + "/api/im/forbiddenmembers";
    //全部禁言或者全部取消禁言
    public static final String IM_ALL_MUTED = BASE_URL + "/api/im/setgroupshutup";
    //腾讯IM接口获取群组详情
    public static final String IM_GET_MUTED_LIST = BASE_URL + "/api/im/getgroupinfo";
    //查看群组公告
    public static final String GET_GROUP_NOTICE = BASE_URL + "/api/im/getgroupnotice";
    //im添加申请加好友
    public static final String IM_ADD_FRIEND = BASE_URL + "/api/im/applyfriend";
    //im删除好友
    public static final String IM_DELETE_FRIEND = BASE_URL + "/api/im/delfriend";
    //im审核好友通知
    public static final String IM_REVIEW_FRIEND_NOTIFY = BASE_URL + "/api/im/authfriend";
    //im申请好友通知列表
    public static final String IM_NEW_FRIEND_APPLICATION_LIST = BASE_URL + "/api/im/applyfriendlist";
    //腾讯IM接口获取好友列表
    public static final String IM_GET_GOOD_FRIEND_LIST = BASE_URL + "/api/im/getmyfriends";

    /**
     * ------------------------新首页------------------------------
     */
    //获取启动页数据
    public static final String GET_START_ACTIVITY_DATA = BASE_URL + "/api/app/startup";
    //首页底部导航栏
    public static final String BOTTOM_MENU = BASE_URL + "/api/app/bottomMenu";
    //首页框架
    public static final String NEW_HOME_FRAME_MAIN = BASE_URL + "/api/app/frame/getFrameAndData";
    //任务大厅筛选
    public static String sotypeStr = "",sortStr = "0";
    public static int typeI = 0;
    public static String searchStr = "";
    public static String locationStr = "北京";
    //任务大厅页面的轮播图
//    public static List<String> imagesUrl = new ArrayList<>();
//    public static List<TaskBean.DataBean.AdvertlistBean> advertlistBeans = new ArrayList<>();
}
