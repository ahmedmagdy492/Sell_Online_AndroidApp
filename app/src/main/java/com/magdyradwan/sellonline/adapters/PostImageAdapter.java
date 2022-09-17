package com.magdyradwan.sellonline.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.magdyradwan.sellonline.R;
import com.magdyradwan.sellonline.helpers.Base64Converter;
import com.magdyradwan.sellonline.responsemodels.PostImageResponseModel;

import java.util.List;

public class PostImageAdapter extends ArrayAdapter<PostImageResponseModel> {
    private final Context context;
    private final List<PostImageResponseModel> postImages;

    public PostImageAdapter(@NonNull Context context, int resource, @NonNull List<PostImageResponseModel> objects) {
        super(context, resource, objects);
        this.context = context;
        postImages = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.image_upload_item, parent, false);

        ImageView img = convertView.findViewById(R.id.image_view);
        byte[] bytes = Base64Converter.convertFromBase64ToByteArr(postImages.get(position).getImageURL());

        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        img.setImageBitmap(bmp);

        return convertView;
    }

    @Override
    public int getCount() {
        return postImages.size();
    }
}
