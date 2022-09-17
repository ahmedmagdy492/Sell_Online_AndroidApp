package com.magdyradwan.sellonline.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.magdyradwan.sellonline.R;
import com.magdyradwan.sellonline.dto.ProfileOptionDTO;

import java.util.List;

public class ProfileAdapter extends ArrayAdapter<ProfileOptionDTO> {

    private List<ProfileOptionDTO> profileOptionDTOS;
    private Context context;

    public ProfileAdapter(@NonNull Context context, int resource, @NonNull List<ProfileOptionDTO> objects) {
        super(context, resource, objects);
        profileOptionDTOS = objects;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ProfileOptionDTO current = profileOptionDTOS.get(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.profile_option_item, parent, false);
        }

        TextView optionName = convertView.findViewById(R.id.option_name);
        TextView title = convertView.findViewById(R.id.txt_title);
        title.setText(current.getTitle());
        optionName.setText(current.getItemName());
        return convertView;
    }

    @Override
    public int getCount() {
        return profileOptionDTOS.size();
    }
}
