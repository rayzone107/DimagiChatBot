package com.rachitgoyal.dimagichatbot.modules.chat;

import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rachitgoyal.dimagichatbot.R;
import com.rachitgoyal.dimagichatbot.helper.Helper;
import com.rachitgoyal.dimagichatbot.model.Message;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OutputURLViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.message_tv)
    TextView mMessageTV;

    @BindView(R.id.timestamp_tv)
    TextView mTimestampTV;

    @BindView(R.id.url_iv)
    ImageView mUrlIV;

    OutputURLViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    void bindData(final Message message, final InputMessageViewHolder.OnItemClickListener itemClickListener) {

        String videoID = extractYTId(message.getUrl());
        if (videoID == null || videoID.isEmpty()) {
            mUrlIV.setVisibility(View.GONE);
        } else {
            Glide.with(mUrlIV.getContext())
                    .load("http://img.youtube.com/vi/" + videoID + "/0.jpg")
                    .into(mUrlIV);
        }

        mMessageTV.setText(message.getMessage());
        mMessageTV.setMovementMethod(LinkMovementMethod.getInstance());
        mTimestampTV.setText(Helper.convertLongToFormattedTime(message.getTimestamp()));
    }

    public static String extractYTId(String ytUrl) {
        String vId = null;

        String pattern = "(?<=watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";

        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(ytUrl);

        if(matcher.find()){
            vId= matcher.group();
        }
        return vId;
    }

    public interface OnItemClickListener {
        void onItemClick(Message message);
    }
}
