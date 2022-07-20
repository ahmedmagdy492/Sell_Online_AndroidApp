package com.magdyradwan.sellonline;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ReadOnlyImagesAdapter extends ArrayAdapter<Bitmap> {

    private final Context context;
    private final List<Bitmap> bmps;

    public ReadOnlyImagesAdapter(@NonNull Context context, int resource, @NonNull List<Bitmap> objects) {
        super(context, resource, objects);
        this.context = context;
        bmps = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.readonly_post_image
                    , parent, false);

        ImageView img = convertView.findViewById(R.id.readonly_image);
        img.setImageBitmap(bmps.get(position));

        return convertView;
    }

    @Override
    public int getCount() {
        return bmps.size();
    }
}
