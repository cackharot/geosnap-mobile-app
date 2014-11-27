package com.example.cackharot.geosnap.lib;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.cackharot.geosnap.model.BaseModel;

import java.util.List;

public class SpinnerAdapter extends ArrayAdapter<BaseModel> {
    private final List<BaseModel> model;
    private final Context context;

    public SpinnerAdapter(Context context, int resource, List<BaseModel> objects) {
        super(context, resource, objects);
        this.context = context;
        model = objects;
    }

    public int getCount() {
        return model.size();
    }

    public BaseModel getItem(int position) {
        return model.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        label.setHeight(80);
        label.setMinHeight(80);
        label.setSingleLine(true);
        label.setPadding(5, 5, 5, 5);
        label.setTextSize(20);
        label.setTextColor(0xff000000);
        label.setText(getItem(position).toString());
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}
