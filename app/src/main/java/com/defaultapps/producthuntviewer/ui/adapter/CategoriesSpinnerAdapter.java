package com.defaultapps.producthuntviewer.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.defaultapps.producthuntviewer.R;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


public class CategoriesSpinnerAdapter extends ArrayAdapter<String> {

    private Context context; //App context
    private LayoutInflater layoutInflater;
    private List<String> items;

    @Inject
    public CategoriesSpinnerAdapter(Context context, List<String> items) {
        super(context, R.layout.item_spinner, items);
        this.context = context;
        layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getDropView(position, convertView, parent);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getTitleView(position, convertView, parent);
    }

    public View getDropView(int position, View convertView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.item_spinner, parent, false);
        TextView category = (TextView) view.findViewById(R.id.category);

        category.setText(items.get(position));

        return view;
    }

    private View getTitleView(int position, View converView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.item_title, parent, false);
        ((TextView) view).setText(items.get(position));
        return view;
    }

    public void setData(List<String> items) {
        this.items = new ArrayList<>(items);
        notifyDataSetChanged();
    }
}
