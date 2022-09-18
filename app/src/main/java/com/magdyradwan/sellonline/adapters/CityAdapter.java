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

import java.util.List;

public class CityAdapter extends ArrayAdapter<String> {

    private final List<String> countries;
    private final Context context;
    private final LayoutInflater inflater;

    public CityAdapter(@NonNull Context context, int resource,
                       @NonNull List<String> objects) {
        super(context, resource, objects);

        countries = objects;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View holder = inflater.inflate(R.layout.spinner_item, null, true);

        TextView textView = holder.findViewById(R.id.category_name_spinner);
        textView.setText(countries.get(position));

        return holder;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null)
            convertView = inflater.inflate(R.layout.spinner_item, parent, false);

        TextView textView = convertView.findViewById(R.id.category_name_spinner);
        textView.setText(countries.get(position));

        return convertView;
    }
}
