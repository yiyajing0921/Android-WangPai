package com.woaiwangpai.iwb.message.helper;

import android.content.Context;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tencent.qcloud.tim.uikit.component.face.CustomGroupUserBean;
import com.tencent.qcloud.tim.uikit.modules.chat.ChatLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.input.InputLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.MessageLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.holder.ICustomMessageViewGroup;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.holder.IOnCustomMessageDrawListener;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfo;

public class ChatLayoutHelper {

    private static final String TAG = ChatLayoutHelper.class.getSimpleName();

    public static void customizeChatLayout(final Context context, final ChatLayout layout) {

//        //====== NoticeLayout使用范例 ======//
//        NoticeLayout noticeLayout = layout.getNoticeLayout();
//        noticeLayout.alwaysShow(true);
//        noticeLayout.getContent().setText("现在插播一条广告");
//        noticeLayout.getContentExtra().setText("参看有奖");
//        noticeLayout.setOnNoticeClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ToastUtil.toastShortMessage("赏白银五千两");
//            }
//        });
//
        //====== MessageLayout使用范例 ======//
        MessageLayout messageLayout = layout.getMessageLayout();
//        ////// 设置聊天背景 //////
//        messageLayout.setBackground(new ColorDrawable(0xFFEFE5D4));
//        ////// 设置头像 //////
//        // 设置默认头像，默认与朋友与自己的头像相同
//        messageLayout.setAvatar(R.drawable.ic_more_file);
        // 设置头像圆角
        messageLayout.setAvatarRadius(50);
//        // 设置头像大小
//        messageLayout.setAvatarSize(new int[]{48, 48});
//
//        ////// 设置昵称样式（对方与自己的样式保持一致）//////
//        messageLayout.setNameFontSize(12);
//        messageLayout.setNameFontColor(0xFF8B5A2B);
//
//        ////// 设置气泡 ///////
        // 设置自己聊天气泡的背景
//        messageLayout.setRightBubble(new ColorDrawable(0xFF5576AC));
//        // 设置朋友聊天气泡的背景
//        messageLayout.setLeftBubble(new ColorDrawable(0xFFE4E7EB));
//
//        ////// 设置聊天内容 //////
        // 设置聊天内容字体字体大小，朋友和自己用一种字体大小
//        messageLayout.setChatContextFontSize(13);
        // 设置自己聊天内容字体颜色
        messageLayout.setRightChatContentFontColor(0xFF333333);
        // 设置朋友聊天内容字体颜色
        messageLayout.setLeftChatContentFontColor(0xFF333333);
//
//        ////// 设置聊天时间 //////
//        // 设置聊天时间线的背景
//        messageLayout.setChatTimeBubble(new ColorDrawable(0xFFE4E7EB));
//        // 设置聊天时间的字体大小
//        messageLayout.setChatTimeFontSize(12);
//        // 设置聊天时间的字体颜色
//        messageLayout.setChatTimeFontColor(0xFF7E848C);
//
//        ////// 设置聊天的提示信息 //////
//        // 设置提示的背景
//        messageLayout.setTipsMessageBubble(new ColorDrawable(0xFFE4E7EB));
//        // 设置提示的字体大小
//        messageLayout.setTipsMessageFontSize(12);
//        // 设置提示的字体颜色
//        messageLayout.setTipsMessageFontColor(0xFF7E848C);
//
        // 设置自定义的消息渲染时的回调
        messageLayout.setOnCustomMessageDrawListener(new CustomMessageDraw());
//
//        // 新增一个PopMenuAction
//        PopMenuAction action = new PopMenuAction();
//        action.setActionName("test");
//        action.setActionClickListener(new PopActionClickListener() {
//            @Override
//            public void onActionClick(int position, Object data) {
//                ToastUtil.toastShortMessage("新增一个pop action");
//            }
//        });
//        messageLayout.addPopAction(action);
//
//        final MessageLayout.OnItemClickListener l = messageLayout.getOnItemClickListener();
//        messageLayout.setOnItemClickListener(new MessageLayout.OnItemClickListener() {
//            @Override
//            public void onMessageLongClick(View view, int position, MessageInfo messageInfo) {
//                l.onMessageLongClick(view, position, messageInfo);
//                ToastUtil.toastShortMessage("demo中自定义长按item");
//            }
//
//            @Override
//            public void onUserIconClick(View view, int position, MessageInfo messageInfo) {
//                l.onUserIconClick(view, position, messageInfo);
//                ToastUtil.toastShortMessage("demo中自定义点击头像");
//            }
//        });


        //====== InputLayout使用范例 ======//
        InputLayout inputLayout = layout.getInputLayout();

//        // TODO 隐藏音频输入的入口，可以打开下面代码测试
//        inputLayout.disableAudioInput(true);
//        // TODO 隐藏表情输入的入口，可以打开下面代码测试
//        inputLayout.disableEmojiInput(true);
//        // TODO 隐藏更多功能的入口，可以打开下面代码测试
//        inputLayout.disableMoreInput(true);
//        // TODO 可以用自定义的事件来替换更多功能的入口，可以打开下面代码测试
//        inputLayout.replaceMoreInput(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ToastUtil.toastShortMessage("自定义的更多功能按钮事件");
//                MessageInfo info = MessageInfoUtil.buildTextMessage("自定义的消息");
//                layout.sendMessage(info, false);
//            }
//        });
//        // TODO 可以用自定义的fragment来替换更多功能，可以打开下面代码测试
//        inputLayout.replaceMoreInput(new CustomInputFragment().setChatLayout(layout));
//
//        // TODO 可以disable更多面板上的各个功能，可以打开下面代码测试
//        inputLayout.disableCaptureAction(true);
//        inputLayout.disableSendFileAction(true);
//        inputLayout.disableSendPhotoAction(true);
//        inputLayout.disableVideoRecordAction(true);
        // TODO 可以自己增加一些功能，可以打开下面代码测试
//        InputMoreActionUnit unit = new InputMoreActionUnit();
//        unit.setIconResId(R.drawable.custom);
//        unit.setTitleId(R.string.test_custom_action);
//        unit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Gson gson = new Gson();
//                CustomMessageData customMessageData = new CustomMessageData();
//                String data = gson.toJson(customMessageData);
//                MessageInfo info = MessageInfoUtil.buildCustomMessage(data);
//                layout.sendMessage(info, false);
//            }
//        });
//        inputLayout.addAction(unit);
    }

//    public static class CustomInputFragment extends BaseInputFragment {
//        @Nullable
//        @Override
//        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//            View baseView = inflater.inflate(R.layout.test_chat_input_custom_fragment, container, false);
//            Button btn1 = baseView.findViewById(R.id.test_send_message_btn1);
//            btn1.setOnClickListener(v -> {
//                ToastUtil.toastShortMessage("自定义的按钮1");
//                if (getChatLayout() != null) {
//                    Gson gson = new Gson();
//                    CustomMessageData customMessageData = new CustomMessageData();
//                    String data = gson.toJson(customMessageData);
//                    MessageInfo info = MessageInfoUtil.buildCustomMessage(data);
//                    getChatLayout().sendMessage(info, false);
//                }
//            });
//            Button btn2 = baseView.findViewById(R.id.test_send_message_btn2);
//            btn2.setOnClickListener(v -> {
//                ToastUtil.toastShortMessage("自定义的按钮2");
//                if (getChatLayout() != null) {
//                    Gson gson = new Gson();
//                    CustomMessageData customMessageData = new CustomMessageData();
//                    String data = gson.toJson(customMessageData);
//                    MessageInfo info = MessageInfoUtil.buildCustomMessage(data);
//                    getChatLayout().sendMessage(info, false);
//                }
//            });
//            return baseView;
//        }
//
//    }

    /**
     * 自定义消息的bean实体，用来与json的相互转化,测试用例
     */
    public static class CustomMessageData {
        int version = 1;
        String text = "欢迎加入云通信IM大家庭！";
        String link = "https://cloud.tencent.com/document/product/269/3794";
    }

    public static class CustomMessageDraw implements IOnCustomMessageDrawListener {

        private static final int DEFAULT_MAX_SIZE = 120;
        private static final float DEFAULT_RADIUS = 5;
        private int version;


        @Override
        public void onDraw(ICustomMessageViewGroup parent, MessageInfo info) {

        }
        /**
         * 自定义消息渲染时，会调用该方法，本方法实现了自定义消息的创建，以及交互逻辑
         *
         * @param mOnItemClickListener
         * @param parent               自定义消息显示的父View，需要把创建的自定义消息view添加到parent里
         * @param info                 消息的具体信息
         * @param position
         */
//        @Override
//        public void onDraw(MessageLayout.OnItemClickListener mOnItemClickListener, ICustomMessageViewGroup parent, MessageInfo info, int position) {
//            // 获取到自定义消息的json数据
//            TIMCustomElem elem = (TIMCustomElem) info.getTIMMessage().getElement(0);
//            // 自定义的json数据，需要解析成bean实例
//            String data = new String(elem.getData());
//            // 把自定义消息view添加到TUIKit内部的父容器里
//            View customView = LayoutInflater.from(MyApplication.getInstance()).inflate(R.layout.message_adapter_content_custom, null, false);
//            parent.addMessageContentView(customView);
//            JSONObject jsonObject = null;
//            try {
//                jsonObject = new JSONObject(data);
//                version = jsonObject.getInt("version");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            if (version == 1) {
//                //自定义消息--名片
//                customCardView(mOnItemClickListener, customView, data, info, position);
//            } else if (version == 3) {
//                //自定义消息--@群成员
//                customGroupUserView(customView, data);
//            } else if (version == 5) {
//                //自定义回复消息
//                customReplyView(info, customView, data);
//            } else if (version == 7) {
//                //自定义转发的消息记录
//                customNoSpotView(info, customView, data);
//            }
//        }
//
//        private void customNoSpotView(MessageInfo info, View customView, String data) {
//            TextView mMsgNoSpot = customView.findViewById(R.id.tv_msg_nospot);
//            mMsgNoSpot.setVisibility(View.VISIBLE);
//        }

        /**
         * 自定义名片View
         *
         * @param onItemClickListener
         * @param customView
         * @param elem
         * @param info
         * @param position
         */
//        private static void customCardView(MessageLayout.OnItemClickListener onItemClickListener, View customView, String elem, MessageInfo info, int position) {
//            CustomCardBean customFaceBean = null;
//            try {
//                customFaceBean = new Gson().fromJson(elem, CustomCardBean.class);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            LinearLayout llCard = customView.findViewById(R.id.ll_card);
//            llCard.setVisibility(View.VISIBLE);
//            LinearLayout mLLGroupUser = customView.findViewById(R.id.ll_group_user);
//            mLLGroupUser.setVisibility(View.GONE);
//            customView.findViewById(R.id.rl_reply).setVisibility(View.GONE);
//            // 自定义消息view的实现，这里仅仅展示文本信息，并且实现超链接跳转
//            UserIconView mCivAvatar = customView.findViewById(R.id.holder_civ_avatar);
//            ImageView mAvatarHead = customView.findViewById(R.id.holder_icon_head);
//            TextView tvNikeName = customView.findViewById(R.id.tv_nikename);
//            final String text = "不支持的自定义消息：" + elem;
//
//            if (customFaceBean != null) {
//                //设置头像
//                if (!TextUtils.isEmpty(customFaceBean.getAvatarImg())) {
//                    List<String> list = new ArrayList<>();
//                    list.add(customFaceBean.getAvatarImg());
//                    mCivAvatar.setIconUrls(list);
//                } else {
//                    //显示默认头像
//                    mCivAvatar.invokeInformation(false);
//                }
//                //设置头像挂件
//                if (!TextUtils.isEmpty(customFaceBean.getHeadImg())) {
//                    mAvatarHead.setVisibility(View.VISIBLE);
//                    Glide.with(customView).load(customFaceBean.getHeadImg()).into(mAvatarHead);
//                } else {
//                    mAvatarHead.setVisibility(View.GONE);
//                }
//                tvNikeName.setText(customFaceBean.getCardName());
//            } else {
//                tvNikeName.setText(text);
//            }
//            customView.setClickable(true);
//
//            //自定义名片,根据ID获取好友信息
//            TIMFriendCheckInfo timFriendCheckInfo = new TIMFriendCheckInfo();
//            ArrayList<String> users = new ArrayList<>();
//            users.add(customFaceBean.getImID());
//            timFriendCheckInfo.setUsers(users);
//            timFriendCheckInfo.setCheckType(TIMFriendCheckType.TIM_FRIEND_CHECK_TYPE_BIDIRECTION);
//
//            llCard.setOnClickListener(v -> {
//                if (onItemClickListener != null) {
//                    onItemClickListener.onCardClick(customView, position, info);
//                } else {
//                    ToastUtil.toastLongMessage("错误");
//                }
//            });
//
//            llCard.setOnLongClickListener(v -> {
//                if (onItemClickListener != null) {
//                    onItemClickListener.onMessageLongClick(customView, position, info);
//                } else {
//                    ToastUtil.toastLongMessage("错误。");
//                }
//                return true;
//            });
//        }

        /**
         * 自定义\n@群成员
         *
         * @param customView
         * @param data
         */
//        private void customGroupUserView(View customView, String data) {
//            customView.findViewById(R.id.ll_card).setVisibility(View.GONE);
//            customView.findViewById(R.id.rl_reply).setVisibility(View.GONE);
//            LinearLayout mLLGroupUser = customView.findViewById(R.id.ll_group_user);
//            mLLGroupUser.setVisibility(View.VISIBLE);
//
//            // 自定义消息view的实现，这里仅仅展示文本信息，并且实现超链接跳转
//            TextView tvGroupUser = customView.findViewById(R.id.tv_group_user);
//            CustomGroupUserBean customGroupUserBean = null;
//            try {
//                customGroupUserBean = new Gson().fromJson(data, CustomGroupUserBean.class);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            setGroup(customGroupUserBean, tvGroupUser, "");
//        }

        /**
         * 自定义回复消息
         *
         * @param info
         * @param customView
         * @param data
         */
//        private void customReplyView(MessageInfo info, View customView, String data) {
//            RelativeLayout mRlReply = customView.findViewById(R.id.rl_reply);
//            customView.findViewById(R.id.ll_card).setVisibility(View.GONE);
//            customView.findViewById(R.id.ll_group_user).setVisibility(View.GONE);
//            mRlReply.setVisibility(View.VISIBLE);
//            LinearLayout mRlTypeText = customView.findViewById(R.id.rl_type_text);
//            TextView mTvFormerName = customView.findViewById(R.id.tv_former_name);
//            //文本类
//            TextView mTvFormerMsg = customView.findViewById(R.id.tv_former_msg);
//            //语音类
//            RelativeLayout mRlTypeAuthor = customView.findViewById(R.id.rl_type_author);
//            TextView mTvAuthorTime = customView.findViewById(R.id.tv_author_time);
//            //图片
//            RelativeLayout mRlTypeImage = customView.findViewById(R.id.rl_type_image);
//            ImageView mIvSmallImage = customView.findViewById(R.id.iv_small_image);
//            ImageView mIvVideoOpen = customView.findViewById(R.id.iv_video_open);
//            //位置、文件
//            RelativeLayout mRlTypeIcon = customView.findViewById(R.id.rl_type_icon);
//            ImageView mIvTypeIcon = customView.findViewById(R.id.iv_type_icon);
//            TextView mTvLocationDetails = customView.findViewById(R.id.tv_location_details);
//            TextView mTvMsg = customView.findViewById(R.id.tv_msg);
//            View mViewLine = customView.findViewById(R.id.view_line);
//            if (info.isSelf()) {
//                mViewLine.setBackgroundResource(R.drawable.bg_corners_blue_left_r5);
//                mRlTypeText.setBackgroundResource(R.drawable.bg_corners_blue_r5);
//            } else {
//                mViewLine.setBackgroundResource(R.drawable.bg_corners_gray_left_r5);
//                mRlTypeText.setBackgroundResource(R.drawable.bg_corners_white_r5);
//            }
//            ReplyBean replyBean = null;
//            try {
//                replyBean = new Gson().fromJson(data, ReplyBean.class);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            if (replyBean != null) {
//                //---------------原消息内容------------------
//                ReplyBean.ReplyContentBean replyContent = replyBean.getReplyContent();
//                ReplyBean.ReplyContentBean.UserInfoBean userInfo = replyContent.getUserInfo();
//                //昵称
//                String userName = TextUtils.isEmpty(userInfo.getNickname()) ? userInfo.getUsername() : userInfo.getNickname();
//                mTvFormerName.setText(TextUtils.isEmpty(userName) ? "未知用户" : userName + "：");
//                //原消息实体
//                PayloadBean payload = replyContent.getPayload();
//                //回复的消息
//                setGroup(replyBean.getDataBeans(), mTvMsg, replyBean.getMsg());
//                switch (replyContent.getType()) {
//                    case "TIMTextElem":
//                        mTvFormerMsg.setVisibility(View.VISIBLE);
//                        mRlTypeAuthor.setVisibility(View.GONE);
//                        mRlTypeImage.setVisibility(View.GONE);
//                        mRlTypeIcon.setVisibility(View.GONE);
//                        //原消息
//                        FaceManager.handlerEmojiText(mTvFormerMsg, info.getExtra().toString());
//                        mTvFormerMsg.setText(payload.getText());
//                        break;
//                    case "TIMSoundElem":
//                        mTvFormerMsg.setVisibility(View.GONE);
//                        mRlTypeAuthor.setVisibility(View.VISIBLE);
//                        mRlTypeImage.setVisibility(View.GONE);
//                        mRlTypeIcon.setVisibility(View.GONE);
//                        mTvAuthorTime.setText("" + payload.getSecond());
//                        break;
//                    case "TIMImageElem":
//                        mTvFormerMsg.setVisibility(View.GONE);
//                        mRlTypeAuthor.setVisibility(View.GONE);
//                        mRlTypeImage.setVisibility(View.VISIBLE);
//                        mIvVideoOpen.setVisibility(View.GONE);
//                        mRlTypeIcon.setVisibility(View.GONE);
//                        performImage(info, payload, mIvSmallImage);
//                        break;
//                    case "TIMVideoElem":
//                        mTvFormerMsg.setVisibility(View.GONE);
//                        mRlTypeAuthor.setVisibility(View.GONE);
//                        mRlTypeImage.setVisibility(View.VISIBLE);
//                        mIvVideoOpen.setVisibility(View.VISIBLE);
//                        mRlTypeIcon.setVisibility(View.GONE);
//                        performVideo(info, payload, mIvSmallImage);
//                        break;
//                    case "TIMLocationElem":
//                        mTvFormerMsg.setVisibility(View.GONE);
//                        mRlTypeAuthor.setVisibility(View.GONE);
//                        mRlTypeImage.setVisibility(View.GONE);
//                        mRlTypeIcon.setVisibility(View.VISIBLE);
//                        mIvTypeIcon.setImageResource(R.drawable.icon_file_location);
//                        mTvLocationDetails.setText(payload.getDescription());
//                        break;
//                    case "TIMFileElem":
//                        mTvFormerMsg.setVisibility(View.GONE);
//                        mRlTypeAuthor.setVisibility(View.GONE);
//                        mRlTypeImage.setVisibility(View.GONE);
//                        mRlTypeIcon.setVisibility(View.VISIBLE);
//                        if (payload.getFileName().contains(".xls")) {
//                            mIvTypeIcon.setImageResource(com.tencent.qcloud.tim.uikit.R.drawable.icon_file_xls);
//                        } else if (payload.getFileName().contains(".doc")) {
//                            mIvTypeIcon.setImageResource(com.tencent.qcloud.tim.uikit.R.drawable.icon_file_docx);
//                        } else if (payload.getFileName().contains(".pdf")) {
//                            mIvTypeIcon.setImageResource(com.tencent.qcloud.tim.uikit.R.drawable.icon_file_pdf);
//                        } else if (payload.getFileName().contains(".ppt")) {
//                            mIvTypeIcon.setImageResource(com.tencent.qcloud.tim.uikit.R.drawable.icon_file_ppt);
//                        }
//                        mTvLocationDetails.setText(payload.getFileName());
//                        break;
//                    case "TIMCustomElem":
//                        if (payload.getData() != null) {
//                            CustomBean customBean = payload.getData();
//                            version = Integer.parseInt(customBean.getVersion());
//                            if (version == 1) {
//                                //自定义消息--名片
//                                mTvFormerMsg.setVisibility(View.GONE);
//                                mRlTypeAuthor.setVisibility(View.GONE);
//                                mRlTypeImage.setVisibility(View.GONE);
//                                mRlTypeIcon.setVisibility(View.VISIBLE);
//                                mIvTypeIcon.setImageResource(com.tencent.qcloud.tim.uikit.R.drawable.icon_file_location);
//                                mTvLocationDetails.setText(customBean.getCardName());
//                            } else if (this.version == 3) {
//                                //自定义消息--@群成员
//                                mTvFormerMsg.setVisibility(View.VISIBLE);
//                                mRlTypeAuthor.setVisibility(View.GONE);
//                                mRlTypeImage.setVisibility(View.GONE);
//                                mRlTypeIcon.setVisibility(View.GONE);
//                                SpannableStringBuilder messageText = new SpannableStringBuilder();
//                                setNewGroup(customBean.getDataBeans(), customBean.getBeginMessage(), mTvFormerMsg, messageText);
//                            } else if (version == 4) {
//                                //自定义消息--自定义表情包
//                                mTvFormerMsg.setVisibility(View.GONE);
//                                mRlTypeAuthor.setVisibility(View.GONE);
//                                mRlTypeImage.setVisibility(View.VISIBLE);
//                                mRlTypeIcon.setVisibility(View.GONE);
//
//                                mIvSmallImage.setAdjustViewBounds(true);
//                                mIvSmallImage.setMaxWidth(100);
//                                mIvSmallImage.setMaxHeight(100);
//                                Glide.with(customView).load(customBean.getExpressionUrl()).into(mIvSmallImage);
//                            } else if (this.version == 5) {
//                                //自定义消息--自定义表情包
//                                mTvFormerMsg.setVisibility(View.GONE);
//                                mRlTypeAuthor.setVisibility(View.GONE);
//                                mRlTypeImage.setVisibility(View.VISIBLE);
//                                mRlTypeIcon.setVisibility(View.GONE);
//                                if (!TextUtils.isEmpty(customBean.getExpressionUrl()) && customBean.getExpressionUrl().length() > 0) {
//                                    mIvSmallImage.setAdjustViewBounds(true);
//                                    mIvSmallImage.setMaxWidth(100);
//                                    mIvSmallImage.setMaxHeight(100);
//                                    Glide.with(customView).load(customBean.getExpressionUrl()).into(mIvSmallImage);
//                                } else {
//                                    String faceData = new String(payload.getFaceData());
//                                    if (!faceData.contains("@2x")) {
//                                        faceData += "@2x";
//                                    }
//                                    Bitmap bitmap = FaceManager.getCustomBitmap(payload.getIndex(), faceData);
//                                    if (bitmap == null) {
//                                        // 自定义表情没有找到，用emoji再试一次
//                                        bitmap = FaceManager.getEmoji(new String(payload.getFaceData()));
//                                        if (bitmap == null) {
//                                            // TODO 临时找的一个图片用来表明自定义表情加载失败
//                                            mIvSmallImage.setImageDrawable(customView.getContext().getResources().getDrawable(com.tencent.qcloud.tim.uikit.R.drawable.face_delete));
//                                        } else {
//                                            Glide.with(customView).load(bitmap).into(mIvSmallImage);
////                    contentImage.setImageBitmap(bitmap);
//                                        }
//                                    } else {
//                                        Glide.with(customView).load(bitmap).into(mIvSmallImage);
//                                    }
//                                }
//                            } else {
//                                //自定义消息--自定义表情包
//                                mTvFormerMsg.setVisibility(View.GONE);
//                                mRlTypeAuthor.setVisibility(View.GONE);
//                                mRlTypeImage.setVisibility(View.VISIBLE);
//                                mRlTypeIcon.setVisibility(View.GONE);
//
//                                mIvSmallImage.setAdjustViewBounds(true);
//                                mIvSmallImage.setMaxWidth(100);
//                                mIvSmallImage.setMaxHeight(100);
//                                Glide.with(customView).load(customBean.getExpressionUrl()).into(mIvSmallImage);
//
//                            }
//                        }
//                        break;
//                    case "TIMFaceElem":
//                        String filter = new String(payload.getFaceData());
//                        if (!filter.contains("@2x")) {
//                            filter += "@2x";
//                        }
//                        Bitmap bitmap = FaceManager.getCustomBitmap(payload.getIndex(), filter);
//                        if (bitmap == null) {
//                            // 自定义表情没有找到，用emoji再试一次
//                            bitmap = FaceManager.getEmoji(new String(payload.getFaceData()));
//                            if (bitmap == null) {
//                                // TODO 临时找的一个图片用来表明自定义表情加载失败
//                                mIvSmallImage.setImageDrawable(customView.getContext().getResources().getDrawable(com.tencent.qcloud.tim.uikit.R.drawable.face_delete));
//                            } else {
//                                Glide.with(customView).load(bitmap).into(mIvSmallImage);
//                            }
//                        } else {
//                            Glide.with(customView).load(bitmap).into(mIvSmallImage);
//                        }
//                        break;
//                    default:
//                        break;
//                }
//            }
//        }

//        private void setNewGroup(List<CustomGroupUserBean.DataBean> dataBeans, String beginMessage, TextView mTvFormerMsg, SpannableStringBuilder messageText) {
//
//            //最前面得数据
//            if (!TextUtils.isEmpty(beginMessage)) {
//                messageText.append(Html.fromHtml("<font color='#333333'>" + beginMessage + "</font>"));
//            }
//            if (dataBeans != null && dataBeans.size() > 0) {
//                for (int i = 0; i < dataBeans.size(); i++) {
//                    CustomGroupUserBean.DataBean dataBean = dataBeans.get(i);
//                    String account = "";
//                    //账户
//                    if (!TextUtils.isEmpty(dataBean.getUserNickNameOrRemark())) {
//                        account = "@" + dataBean.getUserNickNameOrRemark();
//                    } else {
//                        account = "";
//                    }
//                    //消息
//                    String msg = dataBean.getMsg();
//                    //获取内容
//                    String accountColor = "<font color='#FE4E45'>" + account + " " + "</font>";
//                    String msgColor = "<font color='#333333'>" + msg + "</font>";
//                    messageText.append(Html.fromHtml(accountColor + msgColor));
//                }
//            }
//            setGroupMessageContent(mTvFormerMsg, "", messageText);
//        }


        /**
         * 回复显示图片
         *
         * @param messageInfo
         * @param payload
         * @param mIvSmallImage
         */
//        private void performImage(MessageInfo messageInfo, PayloadBean payload, ImageView mIvSmallImage) {
//            mIvSmallImage.setLayoutParams(getImageParams(mIvSmallImage.getLayoutParams(), messageInfo));
//            final List<TIMImage> imgs = payload.getImageInfoArray();
//            if (!TextUtils.isEmpty(messageInfo.getDataPath())) {
//                mIvSmallImage.setImageURI(Uri.parse(messageInfo.getDataPath()));
//            } else {
//                final TIMImage img = imgs.get(0);
//                final String path = TUIKitConstants.IMAGE_DOWNLOAD_DIR + payload.getUuid();
//                img.getImage(path, new TIMCallBack() {
//                    @Override
//                    public void onError(int code, String desc) {
//                        TUIKitLog.e("MessageListAdapter img getImage", code + ":" + desc);
//                    }
//
//                    @Override
//                    public void onSuccess() {
//                        messageInfo.setDataPath(path);
//                        mIvSmallImage.setImageURI(Uri.parse(messageInfo.getDataPath()));
//                    }
//                });
//            }
//        }

        /**
         * 回复显示视频
         *
         * @param messageInfo
         * @param payload
         * @param mIvSmallImage
         */
//        private void performVideo(MessageInfo messageInfo, PayloadBean payload, ImageView mIvSmallImage) {
//            if (messageInfo.getImgWidth() == 0 && messageInfo.getImgHeight() == 0) {
//                mIvSmallImage.setLayoutParams(getImageParams(mIvSmallImage.getLayoutParams(), (int) payload.getThumbWidth(), (int) payload.getThumbHeight()));
//            } else {
//                mIvSmallImage.setLayoutParams(getImageParams(mIvSmallImage.getLayoutParams(), messageInfo.getImgWidth(), messageInfo.getImgHeight()));
//            }
//
//            if (!TextUtils.isEmpty(messageInfo.getDataPath())) {
//                mIvSmallImage.setImageURI(Uri.parse(messageInfo.getDataPath()));
//            } else {
//                final String path = TUIKitConstants.IMAGE_DOWNLOAD_DIR + payload.getThumbUUID();
//                messageInfo.setDataPath(path);
//                mIvSmallImage.setImageURI(Uri.parse(messageInfo.getDataPath()));
//            }
//        }

        /**
         * 动态设置图片宽高
         *
         * @param params
         * @param width
         * @param height
         * @return
         */
        private ViewGroup.LayoutParams getImageParams(ViewGroup.LayoutParams params, int width, int height) {
            if (width == 0 || height == 0) {
                return params;
            }
            if (width > height) {
                params.width = DEFAULT_MAX_SIZE;
                params.height = DEFAULT_MAX_SIZE * height / width;
            } else {
                params.width = DEFAULT_MAX_SIZE * width / height;
                params.height = DEFAULT_MAX_SIZE;
            }
            return params;
        }

        /**
         * 动态设置图片宽高
         *
         * @param params
         * @param msg
         * @return
         */
        private ViewGroup.LayoutParams getImageParams(ViewGroup.LayoutParams params, final MessageInfo msg) {
            if (msg.getImgWidth() == 0 || msg.getImgHeight() == 0) {
                return params;
            }
            if (msg.getImgWidth() > msg.getImgHeight()) {
                params.width = DEFAULT_MAX_SIZE;
                params.height = DEFAULT_MAX_SIZE * msg.getImgHeight() / msg.getImgWidth();
            } else {
                params.width = DEFAULT_MAX_SIZE * msg.getImgWidth() / msg.getImgHeight();
                params.height = DEFAULT_MAX_SIZE;
            }
            return params;
        }

        /**
         * \n@群成员消息显示
         *
         * @param customGroupUserBean
         * @param mView
         * @param endMsg
         */
        private void setGroup(CustomGroupUserBean customGroupUserBean, TextView mView, String endMsg) {
            if (customGroupUserBean != null) {
                //最前面得数据
                String beginMessage = customGroupUserBean.getBeginMessage();
                SpannableStringBuilder messageText = new SpannableStringBuilder();
                if (!TextUtils.isEmpty(beginMessage)) {
                    messageText.append(Html.fromHtml("<font color='#333333'>" + beginMessage + "</font>"));
                }
                if (customGroupUserBean.getDataBeans() != null && customGroupUserBean.getDataBeans().size() > 0) {
                    for (int i = 0; i < customGroupUserBean.getDataBeans().size(); i++) {
                        CustomGroupUserBean.DataBean dataBean = customGroupUserBean.getDataBeans().get(i);
                        String account = "";
                        //账户
                        if (!TextUtils.isEmpty(dataBean.getUserNickNameOrRemark())) {
                            account = "@" + dataBean.getUserNickNameOrRemark();
                        } else {
                            account = "";
                        }
                        //消息
                        String msg = dataBean.getMsg();
                        //获取内容
                        String accountColor = "<font color='#FE4E45'>" + account + " " + "</font>";
                        String msgColor = "<font color='#333333'>" + msg + "</font>";
                        messageText.append(Html.fromHtml(accountColor + msgColor));
                    }
                }
                setGroupMessageContent(mView, endMsg, messageText);
            } else {
                mView.setText(endMsg);
            }
        }

        /**
         * 测试用例
         *
         * @param parent
         * @param info
         */
//        private static void DisCardCustomView(ICustomMessageViewGroup parent, MessageInfo info) {
//            // 获取到自定义消息的json数据
//            TIMCustomElem elem = (TIMCustomElem) info.getTIMMessage().getElement(0);
//            // 自定义的json数据，需要解析成bean实例
//            CustomMessageData data = null;
//            try {
//                data = new Gson().fromJson(new String(elem.getData()), CustomMessageData.class);
//            } catch (Exception e) {
//                Log.e(TAG, "invalid json: " + new String(elem.getData()));
//            }
//            if (data == null) {
//                Log.e(TAG, "No Custom Data: " + new String(elem.getData()));
//            }
//
//            // 把自定义消息view添加到TUIKit内部的父容器里
//            View view = LayoutInflater.from(MyApplication.getInstance()).inflate(R.layout.test_custom_message_layout1, null, false);
//            parent.addMessageContentView(view);
//
//            // 自定义消息view的实现，这里仅仅展示文本信息，并且实现超链接跳转
//            TextView textView = view.findViewById(R.id.test_custom_message_tv);
//            final String text = "不支持的自定义消息：" + new String(elem.getData());
//            if (data == null) {
//                textView.setText(text);
//            } else {
//                textView.setText(data.text);
//            }
//            final CustomMessageData customMessageData = data;
//            view.setClickable(true);
//            view.setOnClickListener(v -> {
//                if (customMessageData == null) {
//                    Log.e(TAG, "Do what?");
//                    ToastUtil.toastShortMessage(text);
//                    return;
//                }
//                Intent intent = new Intent();
//                intent.setAction("android.intent.action.VIEW");
//                Uri content_url = Uri.parse(customMessageData.link);
//                intent.setData(content_url);
//                MyApplication.getInstance().startActivity(intent);
//            });
//        }
    }

    private static void setGroupMessageContent(TextView mView, String endMsg, SpannableStringBuilder messageText) {
        if (TextUtils.isEmpty(messageText) && messageText.length() <= 0) {
            mView.setText(endMsg);
        } else {
            mView.setText(messageText);
        }
    }

}
