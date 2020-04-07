package com.djaphar.coffeepointapp.SupportClasses.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.djaphar.coffeepointapp.R;
import com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses.Product;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MapPointProductsRecyclerAdapter extends  RecyclerView.Adapter<MapPointProductsRecyclerAdapter.ViewHolder> {

    private ArrayList<Product> products;

    public MapPointProductsRecyclerAdapter(ArrayList<Product> products, String nullProductString) {
        this.products = products;
        if (products.size() == 0) {
            products.add(new Product("", "", nullProductString, ""));
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.map_point_products_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = products.get(position);
        if (product.get_id().equals("")) {
            holder.mapPointProductTypeTv.setVisibility(View.GONE);
        }
        holder.mapPointProductNameTv.setText(product.getName());
        holder.mapPointProductTypeTv.setText(product.getType());
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout parentLayout;
        TextView mapPointProductNameTv, mapPointProductTypeTv;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            parentLayout = itemView.findViewById(R.id.parent_layout_map_point_products);
            mapPointProductNameTv = itemView.findViewById(R.id.map_point_product_name_tv);
            mapPointProductTypeTv = itemView.findViewById(R.id.map_point_product_type_tv);
        }
    }
}
