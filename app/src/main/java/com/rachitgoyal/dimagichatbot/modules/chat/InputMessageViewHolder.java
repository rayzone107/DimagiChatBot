package com.rachitgoyal.dimagichatbot.modules.chat;

import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.rachitgoyal.dimagichatbot.R;
import com.rachitgoyal.dimagichatbot.helper.Helper;
import com.rachitgoyal.dimagichatbot.model.Message;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InputMessageViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.message_tv)
    TextView mMessageTV;

    @BindView(R.id.timestamp_tv)
    TextView mTimestampTV;

    InputMessageViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    void bindData(final Message message, final OnItemClickListener itemClickListener) {
        mMessageTV.setText(message.getMessage());
        mMessageTV.setMovementMethod(LinkMovementMethod.getInstance());
        mTimestampTV.setText(Helper.convertLongToFormattedTime(message.getTimestamp()));
    }

    public interface OnItemClickListener {
        void onItemClick(Message message);
    }
}
