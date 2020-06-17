package com.woaiwangpai.iwb.message;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tencent.qcloud.tim.uikit.component.gatherimage.UserIconView;
import com.tencent.qcloud.tim.uikit.modules.conversation.base.ConversationInfo;
import com.tencent.qcloud.tim.uikit.modules.conversation.holder.ConversationBaseHolder;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfo;
import com.woaiwangpai.iwb.R;

/**
 * 自定义会话Holder
 */
public class NewConversationCustomHolder extends ConversationBaseHolder {

    protected LinearLayout leftItemLayout;
    protected TextView titleText;
    protected TextView messageText;
    protected TextView timelineText;
    protected TextView unreadText;
    protected UserIconView imageView;


    public NewConversationCustomHolder(View itemView) {
        super(itemView);
        leftItemLayout = rootView.findViewById(R.id.item_left);
        imageView = rootView.findViewById(R.id.conversation_icon);
        titleText = rootView.findViewById(R.id.conversation_title);
        messageText = rootView.findViewById(R.id.conversation_last_msg);
        timelineText = rootView.findViewById(R.id.conversation_time);
        unreadText = rootView.findViewById(R.id.conversation_unread);
    }

    @Override
    public void layoutViews(ConversationInfo conversation, int position) {
        MessageInfo lastMsg = conversation.getLastMessage();
        if (conversation.isTop()) {
            leftItemLayout.setBackgroundColor(rootView.getResources().getColor(R.color.c_E5E5E5));
        } else {
            leftItemLayout.setBackgroundResource(R.drawable.conversation_select);
        }

        //设置头像
        if (conversation.getIconUrlList() != null) {
            imageView.setIconUrls(conversation.getIconUrlList());
        } else {
            //判断是否是群组
            imageView.invokeInformation(conversation.isGroup());
        }

        titleText.setText(conversation.getTitle());
        messageText.setText("");
        timelineText.setText("");
        //设置消息显示数量
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

    }

}
