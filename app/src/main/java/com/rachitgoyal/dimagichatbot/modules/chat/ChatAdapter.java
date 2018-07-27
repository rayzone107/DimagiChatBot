package com.rachitgoyal.dimagichatbot.modules.chat;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rachitgoyal.dimagichatbot.R;
import com.rachitgoyal.dimagichatbot.helper.Constants;
import com.rachitgoyal.dimagichatbot.model.Message;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int INPUT_TYPE = 0;
    private static final int TEXT_OUTPUT_TYPE = 1;
    private static final int URL_OUTPUT_TYPE = 2;

    private List<Message> mMessages;
    private InputMessageViewHolder.OnItemClickListener mItemClickListener;

    ChatAdapter(List<Message> messages, InputMessageViewHolder.OnItemClickListener onItemClickListener) {
        mMessages = messages;
        mItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case INPUT_TYPE:
                View inputView = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_input, parent, false);
                return new InputMessageViewHolder(inputView);
            case TEXT_OUTPUT_TYPE:
                View outputTextView = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_text_output, parent, false);
                return new OutputTextViewHolder(outputTextView);
            case URL_OUTPUT_TYPE:
                View outputUrlView = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_url_output, parent, false);
                return new OutputURLViewHolder(outputUrlView);
            default:
                inputView = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_input, parent, false);
                return new InputMessageViewHolder(inputView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case INPUT_TYPE:
                ((InputMessageViewHolder) holder).bindData(mMessages.get(position), mItemClickListener);
                break;
            case TEXT_OUTPUT_TYPE:
                ((OutputTextViewHolder) holder).bindData(mMessages.get(position), mItemClickListener);
                break;
            case URL_OUTPUT_TYPE:
                ((OutputURLViewHolder) holder).bindData(mMessages.get(position), mItemClickListener);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mMessages == null || mMessages.size() <= 0 ? 0 : mMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        switch (mMessages.get(position).getType()) {
            case Constants.MESSAGE_TYPE.INPUT:
                return INPUT_TYPE;
            case Constants.MESSAGE_TYPE.TEXT_OUTPUT:
                return TEXT_OUTPUT_TYPE;
            case Constants.MESSAGE_TYPE.URL_OUTPUT:
                return URL_OUTPUT_TYPE;
        }
        return RecyclerView.NO_POSITION;
    }

    void addMessage(Message message) {
        if (mMessages == null) {
            mMessages = new ArrayList<>();
        }
        mMessages.add(0, message);
        notifyItemInserted(getItemCount());
    }

    void addMessages(List<Message> messages) {
        if (mMessages == null) {
            mMessages = new ArrayList<>();
        }
        mMessages.addAll(0, messages);
        notifyItemRangeInserted(getItemCount(), mMessages.size());
    }

    void clear() {
        if (mMessages != null && mMessages.size() > 0) {
            mMessages.clear();
            notifyDataSetChanged();
        }
    }
}
