package com.tencent.qcloud.tim.uikit.modules.conversation.holder;

import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tencent.qcloud.tim.uikit.R;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationListAdapter;
import com.tencent.qcloud.tim.uikit.modules.conversation.base.ConversationIconView;
import com.tencent.qcloud.tim.uikit.modules.conversation.base.ConversationInfo;

/**
 * 自定义会话Holder
 */
public class ConversationCustomHolder extends ConversationBaseHolder {

    protected LinearLayout leftItemLayout;
    protected TextView titleText;
    protected TextView messageText;
    protected TextView timelineText;
    protected TextView unreadText;
    protected ConversationIconView conversationIconView;


    public ConversationCustomHolder(View itemView) {
        super(itemView);
        leftItemLayout = rootView.findViewById(R.id.item_left);
        conversationIconView = rootView.findViewById(R.id.conversation_icon);
        titleText = rootView.findViewById(R.id.conversation_title);
        messageText = rootView.findViewById(R.id.conversation_last_msg);
        timelineText = rootView.findViewById(R.id.conversation_time);
        unreadText = rootView.findViewById(R.id.conversation_unread);
    }

    @Override
    public void layoutViews(ConversationInfo conversation, int position) {
        if (conversation.isTop()) {
            leftItemLayout.setBackgroundColor(rootView.getResources().getColor(R.color.conversation_top_color));
        } else {
            leftItemLayout.setBackgroundColor(Color.WHITE);
        }
        conversationIconView.setConversation(conversation);

        titleText.setText(conversation.getTitle());
        messageText.setText("");
        timelineText.setText("");

        if (conversation.getUnRead() > 0) {
            unreadText.setVisibility(View.VISIBLE);
            if (conversation.getUnRead() > 99) {
                unreadText.setText("99+");
            } else {
                unreadText.setText("" + conversation.getUnRead());
            }
        } else {
            unreadText.setVisibility(View.GONE);
        }

        if (((ConversationListAdapter) mAdapter).getItemDateTextSize() != 0) {
            timelineText.setTextSize(((ConversationListAdapter) mAdapter).getItemDateTextSize());
        }
        if (((ConversationListAdapter) mAdapter).getItemBottomTextSize() != 0) {
            messageText.setTextSize(((ConversationListAdapter) mAdapter).getItemBottomTextSize());
        }
        if (((ConversationListAdapter) mAdapter).getItemTopTextSize() != 0) {
            titleText.setTextSize(((ConversationListAdapter) mAdapter).getItemTopTextSize());
        }

    }

}
