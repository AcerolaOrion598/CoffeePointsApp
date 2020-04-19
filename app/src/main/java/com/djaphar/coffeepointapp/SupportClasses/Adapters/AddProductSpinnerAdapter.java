package com.djaphar.coffeepointapp.SupportClasses.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.djaphar.coffeepointapp.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AddProductSpinnerAdapter extends ArrayAdapter<String> {

    public AddProductSpinnerAdapter(Context context, ArrayList<String> productTypes) {
        super(context, 0, productTypes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.add_product_spinner_row, parent, false);
        }
        TextView addProductTypeTv = convertView.findViewById(R.id.add_product_type_tv);
        addProductTypeTv.setText(getItem(position));
        return convertView;
    }
}
