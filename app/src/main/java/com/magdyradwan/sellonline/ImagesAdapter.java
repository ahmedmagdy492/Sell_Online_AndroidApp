package com.magdyradwan.sellonline;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.magdyradwan.sellonline.dto.ImageUploadDTO;

import java.util.List;

public class ImagesAdapter extends ArrayAdapter<ImageUploadDTO> {

    private final Context context;
    private final List<ImageUploadDTO> imageUploadDTOS;

    public ImagesAdapter(@NonNull Context context, int resource, @NonNull List<ImageUploadDTO> objects) {
        super(context, resource, objects);
        this.context = context;
        imageUploadDTOS = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null)
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.image_upload_item, parent, false);

        ImageView image = convertView.findViewById(R.id.image_view);
        image.setImageURI(imageUploadDTOS.get(position).getImage());

        return convertView;
    }

    @Override
    public int getCount() {
        return imageUploadDTOS.size();
    }
}
