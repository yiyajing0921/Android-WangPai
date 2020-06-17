package com.tencent.qcloud.tim.uikit.modules.conversation.holder;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.tencent.qcloud.tim.uikit.modules.conversation.base.ConversationInfo;
import com.tencent.qcloud.tim.uikit.modules.conversation.interfaces.IConversationAdapter;

public abstract class ConversationBaseHolder extends RecyclerView.ViewHolder {

    protected View rootView;
    protected IConversationAdapter mAdapter;

    public ConversationBaseHolder(View itemView) {
        super(itemView);
        rootView = itemView;
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        mAdapter = (IConversationAdapter) adapter;
    }

    public abstract void layoutViews(ConversationInfo conversationInfo, int position);

}
