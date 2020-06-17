package com.woaiwangpai.iwb.message;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMElem;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.qcloud.tim.uikit.component.face.CustomGroupUserBean;
import com.tencent.qcloud.tim.uikit.component.gatherimage.UserIconView;
import com.tencent.qcloud.tim.uikit.modules.conversation.base.ConversationInfo;
import com.tencent.qcloud.tim.uikit.modules.conversation.holder.ConversationBaseHolder;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfo;
import com.tencent.qcloud.tim.uikit.utils.DateTimeUtil;
import com.woaiwangpai.iwb.R;
import com.woaiwangpai.iwb.constant.Constant;
import com.woaiwangpai.iwb.utils.SharedPreferenceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewConversationCommonHolder extends ConversationBaseHolder {

    public LinearLayout leftItemLayout;
    protected TextView titleText;
    protected TextView messageText;
    protected TextView timelineText;
    protected TextView unreadText;
    public UserIconView conversationIconView;
    public ImageView conversationIcon;
    private Context context;
    public ImageView conversationHead;
    private int version;
    private CustomGroupUserBean customGroupUserBean;

    public NewConversationCommonHolder(View itemView, Context context) {
        super(itemView);
        this.context = context;
        leftItemLayout = rootView.findViewById(R.id.item_left);
        conversationIconView = rootView.findViewById(R.id.conversation_icon);
        conversationHead = rootView.findViewById(R.id.conversation_icon_head);
        conversationIcon = rootView.findViewById(R.id.conversation_icon2);
        titleText = rootView.findViewById(R.id.conversation_title);
        messageText = rootView.findViewById(R.id.conversation_last_msg);
        timelineText = rootView.findViewById(R.id.conversation_time);
        unreadText = rootView.findViewById(R.id.conversation_unread);
    }

    public void layoutViews(ConversationInfo conversation, int position) {
        boolean isClick = false;
        //获取会话扩展实例
        TIMConversationType type = conversation.isGroup() ? TIMConversationType.Group : TIMConversationType.C2C;
        TIMConversation con = TIMManager.getInstance().getConversation(type, conversation.getId());
        MessageInfo lastMsg = conversation.getLastMessage();
        String str = "";
        if (null != con && con.hasDraft() && lastMsg != null) {
            str = SharedPreferenceUtil.getStringData(Constant.DRAFTSTRING + conversation.getId());
            String messageHtml = "<font color='#FE4E45'>[草稿]</font>";
            lastMsg.setExtra(Html.fromHtml(messageHtml) + "  " + str);
        } else {
            if (lastMsg != null && lastMsg.getStatus() == MessageInfo.MSG_STATUS_REVOKE) {
                if (lastMsg.isSelf()) {
                    lastMsg.setExtra("您撤回了一条消息");
                } else if (lastMsg.isGroup()) {
                    if (!lastMsg.getTIMMessage().getSender().equals(lastMsg.getFromUser())) {
                        String fromUser = lastMsg.getFromUser() + ": ";
                        String source = "<font color='#338BFF'>" + fromUser + "</font>";
                        Spanned spanned = Html.fromHtml(source + "   ");
                        lastMsg.setExtra(spanned + "撤回了一条消息");

                    } else {
                        List<String> identifiers = new ArrayList<>();
                        identifiers.add(lastMsg.getTIMMessage().getSender());
                        TIMFriendshipManager.getInstance().getUsersProfile(identifiers, true, new TIMValueCallBack<List<TIMUserProfile>>() {
                            @Override
                            public void onError(int code, String s) {
                                String fromUser = lastMsg.getFromUser() + ": ";
                                String source = "<font color='#338BFF'>" + fromUser + "</font>";
                                Spanned spanned = Html.fromHtml(source + "   ");
                                lastMsg.setExtra(spanned + "撤回了一条消息");
                            }

                            @Override
                            public void onSuccess(List<TIMUserProfile> timUserProfiles) {
                                int size = timUserProfiles.size();
                                if (size > 0) {
                                    if (null != timUserProfiles.get(0).getNickName()) {
                                        if (timUserProfiles.get(0).getNickName().length() > 0) {
                                            String fromUser = timUserProfiles.get(0).getNickName() + ": ";
                                            String source = "<font color='#338BFF'>" + fromUser + "</font>";
                                            Spanned spanned = Html.fromHtml(source + "   ");
                                            lastMsg.setExtra(spanned + "撤回了一条消息");
                                        }
                                    }
                                } else {
                                    String fromUser = lastMsg.getFromUser() + ": ";
                                    String source = "<font color='#338BFF'>" + fromUser + "</font>";
                                    Spanned spanned = Html.fromHtml(source + "   ");
                                    lastMsg.setExtra(spanned + "撤回了一条消息");
                                }
                            }
                        });
                    }
                } else {
                    lastMsg.setExtra("对方撤回了一条消息");
                }
            }
        }

        //是否置顶
        if (conversation.isTop()) {
            leftItemLayout.setBackgroundColor(rootView.getResources().getColor(R.color.c_E5E5E5));
        } else {
            leftItemLayout.setBackgroundResource(R.drawable.conversation_select);
        }

        //是否显示VIP--是否群组--是的话隐藏
        if (conversation.isGroup()) {
            conversationIcon.setVisibility(View.GONE);
        } else {
            conversationIcon.setVisibility(conversation.isShowVip() ? View.VISIBLE : View.GONE);
        }

        //设置头像挂件
        String cUrl = conversation.getHead();
        if (!TextUtils.isEmpty(conversation.getHead())) {
            conversationHead.setVisibility(View.VISIBLE);
            Glide.with(context).load(cUrl).diskCacheStrategy(DiskCacheStrategy.ALL).into(conversationHead);
        } else {
            conversationHead.setVisibility(View.GONE);
        }
        //设置title
        titleText.setText(conversation.getTitle());
        //设置消息内容
        if (TextUtils.isEmpty(messageText.getText().toString())) {
            messageText.setText("");
        }
        //设置消息时间
        timelineText.setText("");
        if (lastMsg != null) {
            Log.i("12345678", "layoutViews: " + lastMsg.getExtra());
            if (lastMsg.getExtra() != null) {
                if (lastMsg.getStatus() != MessageInfo.MSG_STATUS_REVOKE && str.length() <= 0 && conversation.isGroup()) {
                    String lastMsgSender;
                    if (!TextUtils.isEmpty(conversation.getLastMsgSender())) {
                        lastMsgSender = conversation.getLastMsgSender();
                    } else {
                        if ("@TIM#SYSTEM".equals(conversation.getLastMessage().getFromUser())) {
                            lastMsgSender = "系统消息: ";
                        } else {
                            lastMsgSender = conversation.getLastMessage().getFromUser() + ": ";
                        }

                    }

                    //@群成员显示样式
                    Spanned spanned;
                    if (lastMsg.getExtra().toString().contains("@全体成员")) {
                        if (lastMsg.isRead() || conversation.getUnRead() == 0) {
                            spanned = Html.fromHtml(lastMsgSender + lastMsg.getExtra().toString());
                        } else {
                            spanned = Html.fromHtml("<font color='#FE4E45'>" + "[@全体成员]" + "</font>" + lastMsgSender + lastMsg.getExtra().toString());
                        }
                    } else if (lastMsg.getExtra().toString().contains("@")) {
                        //解析当前消息
                        TIMElem element = lastMsg.getTIMMessage().getElement(0);
                        if (element instanceof TIMCustomElem) {
                            TIMCustomElem customElem = (TIMCustomElem) element;
                            String filter = new String(customElem.getData());
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(filter);
                                version = jsonObject.getInt("version");
                                if (version == 3) {
                                    customGroupUserBean = new Gson().fromJson(filter, CustomGroupUserBean.class);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if (version == 3) {
                            String loginUser = TIMManager.getInstance().getLoginUser();
                            if (customGroupUserBean != null) {
                                for (int i = 0; i < customGroupUserBean.getDataBeans().size(); i++) {
                                    CustomGroupUserBean.DataBean dataBean = customGroupUserBean.getDataBeans().get(i);
                                    String userName = dataBean.getUserName();
                                    if (loginUser.equals(userName)) {
                                        //如果@消息中包含了本人，就显示有人@我 + 消息，否则就只显示发送的消息
                                        isClick = true;
                                    }
                                }

                                if (isClick) {
                                    if (lastMsg.isRead() || conversation.getUnRead() == 0) {
                                        spanned = Html.fromHtml(lastMsgSender + lastMsg.getExtra().toString());
                                    } else {
                                        spanned = Html.fromHtml("<font color='#FE4E45'>" + "[有人@我]" + "</font>" + lastMsgSender + lastMsg.getExtra().toString());
                                    }
                                } else {
                                    spanned = Html.fromHtml(lastMsgSender + lastMsg.getExtra().toString());
                                }
                            } else {
                                spanned = Html.fromHtml(lastMsgSender + lastMsg.getExtra().toString());
                            }

                        } else {
                            spanned = Html.fromHtml(lastMsgSender + lastMsg.getExtra().toString());
                        }


                    } else {
                        spanned = Html.fromHtml(lastMsgSender + lastMsg.getExtra().toString());
                    }

                    messageText.setText(spanned);
                } else {
                    Spanned spanned;
                    if (lastMsg.getExtra().toString().contains("[草稿]")) {
                        spanned = Html.fromHtml("<font color='#FE4E45'>[草稿]</font>" + "    " + str);
                        messageText.setText(spanned);
                    } else {
                        String text = lastMsg.getExtra().toString();
                        messageText.setText(text);
                    }


                }
                messageText.setTextColor(rootView.getResources().getColor(R.color.list_bottom_text_bg));
            }
            if (str.length() <= 0) {
                long msgTime = lastMsg.getMsgTime();
                String timeFormatTextNew = DateTimeUtil.getTimeFormatTextNew(new Date(msgTime));
                if (timeFormatTextNew.contains("昨天")) {
                    timelineText.setText("昨天");
                } else {
                    timelineText.setText(timeFormatTextNew);
                }
            }
        }

        //未读消息数
        if (conversation.getUnRead() > 0) {
            String unreadStr = "" + conversation.getUnRead();
            if (conversation.getUnRead() > 99) {
                unreadStr = "99+";
            }
            unreadText.setVisibility(View.VISIBLE);
            unreadText.setText(unreadStr);
        } else {
            unreadText.setVisibility(View.GONE);
        }

        if (((ConversationListAdapter) mAdapter).mDateTextSize != 0) {
            timelineText.setTextSize(((ConversationListAdapter) mAdapter).mDateTextSize);
        }
        if (((ConversationListAdapter) mAdapter).mBottomTextSize != 0) {
            messageText.setTextSize(((ConversationListAdapter) mAdapter).mBottomTextSize);
        }
        if (((ConversationListAdapter) mAdapter).mTopTextSize != 0) {
            titleText.setTextSize(((ConversationListAdapter) mAdapter).mTopTextSize);
        }

        //设置头像
        if (conversation.getIconUrlList() != null) {
            conversationIconView.setIconUrls(conversation.getIconUrlList());
        } else {
            //判断是否是群组
            conversationIconView.invokeInformation(conversation.isGroup());
        }

        //// 由子类设置指定消息类型的views
        layoutVariableViews(conversation, position);
    }

    public void layoutVariableViews(ConversationInfo conversationInfo, int position) {
    }
}
