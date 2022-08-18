package com.magdyradwan.sellonline;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.magdyradwan.sellonline.models.ChatModel;

import java.util.List;

public class ChatsAdapter extends ArrayAdapter<ChatModel> {

    private final Context context;
    private List<ChatModel> chats;

    public ChatsAdapter(@NonNull Context context, int resource, @NonNull List<ChatModel> objects) {
        super(context, resource, objects);
        this.context = context;
        chats = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ChatModel cur = chats.get(position);

        if(convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.chat_item, parent, false);
        }

        TextView txtView = convertView.findViewById(R.id.chat_name);
        if(!cur.getSenderName().equals(""))
        {
            txtView.setText(cur.getSenderName());
        }
        else {
            txtView.setText(cur.getReceiverName());
        }

        return convertView;
    }

    @Override
    public int getCount() {
        return chats.size();
    }
}
