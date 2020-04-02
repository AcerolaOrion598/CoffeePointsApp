package com.djaphar.coffeepointapp.SupportClasses.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.djaphar.coffeepointapp.R;
import com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses.Product;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PointProductsRecyclerViewAdapter extends RecyclerView.Adapter<PointProductsRecyclerViewAdapter.ViewHolder> {

    private List<Product> products;

    public PointProductsRecyclerViewAdapter(List<Product> products, String nullProductString) {
        this.products = products;
        if (products.size() == 0) {
            products.add(new Product("", nullProductString, "", ""));
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.point_products_list, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = products.get(position);
        if (product.get_id().equals("")) {
            holder.pointProductNameTv.setVisibility(View.GONE);
        }
        holder.pointProductNameTv.setText(product.getName());
        holder.pointProductTypeTv.setText(product.getType());
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout parentLayout;
        TextView pointProductNameTv, pointProductTypeTv;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            parentLayout = itemView.findViewById(R.id.parent_layout_point_products);
            pointProductNameTv = itemView.findViewById(R.id.point_product_name_tv);
            pointProductTypeTv = itemView.findViewById(R.id.point_product_type_tv);
        }
    }
}
