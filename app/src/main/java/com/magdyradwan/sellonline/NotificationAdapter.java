package com.magdyradwan.sellonline;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.magdyradwan.sellonline.responsemodels.NotificationResponseModel;

import java.util.List;

public class NotificationAdapter extends ArrayAdapter<NotificationResponseModel> {

    private final Context context;
    private final List<NotificationResponseModel> notifications;

    public NotificationAdapter(@NonNull Context context, int resource, @NonNull List<NotificationResponseModel> objects) {
        super(context, resource, objects);
        this.context = context;
        notifications = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        NotificationResponseModel responseModel = notifications.get(position);

        if(convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.notification_item, parent, false);

        TextView title = convertView.findViewById(R.id.noti_title);
        title.setText(responseModel.getTitle());

        TextView content = convertView.findViewById(R.id.noti_content);
        content.setText(responseModel.getContent());

        TextView date = convertView.findViewById(R.id.noti_create_date);
        date.setText(responseModel.getNotificationDate());

        return convertView;
    }

    @Override
    public int getCount() {
        return notifications.size();
    }
}
