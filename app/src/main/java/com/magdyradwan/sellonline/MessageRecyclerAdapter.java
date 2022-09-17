package com.magdyradwan.sellonline;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.magdyradwan.sellonline.models.MessageModel;

import java.util.List;

public class MessageRecyclerAdapter extends RecyclerView.Adapter<MessageRecyclerAdapter.MessageViewHolder> {

    private final Context context;
    private final List<MessageModel> msgs;
    private final String userId;

    public MessageRecyclerAdapter(Context context, List<MessageModel> msgs, String userId)
    {
        this.context = context;
        this.msgs = msgs;
        this.userId = userId;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.message_item, parent, false);
        return new MessageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        MessageModel msg = msgs.get(position);
        holder.content.setText(msg.getContent());

        RelativeLayout.LayoutParams relativeParams = (RelativeLayout.LayoutParams)
                holder.message_container.getLayoutParams();

        if(msg.getSenderID().equals(userId))
        {
            Log.d("TAG", "onBindViewHolder: sender = userid");
            relativeParams.removeRule(RelativeLayout.ALIGN_PARENT_END);
            relativeParams.removeRule(RelativeLayout.ALIGN_PARENT_START);
            relativeParams.addRule(RelativeLayout.ALIGN_PARENT_END);
            holder.message_container.setBackgroundColor(Color.GRAY);
        }
        else {
            relativeParams.removeRule(RelativeLayout.ALIGN_PARENT_END);
            relativeParams.removeRule(RelativeLayout.ALIGN_PARENT_START);
            relativeParams.addRule(RelativeLayout.ALIGN_PARENT_START);
            holder.message_container.setBackgroundColor(Color.LTGRAY);
        }
        holder.message_container.setLayoutParams(relativeParams);
    }

    @Override
    public int getItemCount() {
        return msgs.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder
    {

        private final TextView content;
        private final CardView message_container;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            content = itemView.findViewById(R.id.message_content);
            message_container = itemView.findViewById(R.id.message_container);
        }
    }
}
