package com.magdyradwan.sellonline;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.magdyradwan.sellonline.models.MessageModel;

import java.util.List;

public class MessageRecyclerAdapter extends RecyclerView.Adapter<MessageRecyclerAdapter.MessageViewHolder> {

    private final Context context;
    private final List<MessageModel> msgs;

    public MessageRecyclerAdapter(Context context, List<MessageModel> msgs)
    {
        this.context = context;
        this.msgs = msgs;
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
    }

    @Override
    public int getItemCount() {
        return msgs.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder
    {

        private TextView content;
        private CardView message_container;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            content = itemView.findViewById(R.id.message_content);
            message_container = itemView.findViewById(R.id.message_container);
        }
    }
}
