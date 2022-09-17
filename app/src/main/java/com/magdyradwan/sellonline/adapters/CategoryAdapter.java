package com.magdyradwan.sellonline.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.magdyradwan.sellonline.R;
import com.magdyradwan.sellonline.responsemodels.PostCategory;

import java.util.List;

public class CategoryAdapter extends ArrayAdapter<PostCategory> {

    private final List<PostCategory> categories;
    private final Context context;
    private final LayoutInflater inflater;

    public CategoryAdapter(@NonNull Context context, int resource,
                           @NonNull List<PostCategory> objects) {
        super(context, resource, objects);

        categories = objects;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View holder = inflater.inflate(R.layout.spinner_item, null, true);

        TextView textView = holder.findViewById(R.id.category_name_spinner);
        textView.setText(categories.get(position).getName());

        TextView idTextView = holder.findViewById(R.id.category_id_spinner);
        idTextView.setText(categories.get(position).getId() + "");

        return holder;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null)
            convertView = inflater.inflate(R.layout.spinner_item, parent, false);

        TextView textView = convertView.findViewById(R.id.category_name_spinner);
        textView.setText(categories.get(position).getName());

        TextView idTextView = convertView.findViewById(R.id.category_id_spinner);
        idTextView.setText(categories.get(position).getId() + "");

        return convertView;
    }
}
