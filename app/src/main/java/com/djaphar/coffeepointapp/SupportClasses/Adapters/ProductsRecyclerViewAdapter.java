package com.djaphar.coffeepointapp.SupportClasses.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.djaphar.coffeepointapp.Fragments.ProfileFragment;
import com.djaphar.coffeepointapp.R;
import com.djaphar.coffeepointapp.SupportClasses.LocalDataClasses.Product;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductsRecyclerViewAdapter extends RecyclerView.Adapter<ProductsRecyclerViewAdapter.ViewHolder> {

    private List<Product> products;
    private ProfileFragment profileFragment;

    public ProductsRecyclerViewAdapter(List<Product> products, ProfileFragment profileFragment) {
        this.products = products;
        this.profileFragment = profileFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.products_list, parent,false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.productDeleteBtn.setOnClickListener(lView -> profileFragment.createDeleteProductDialog(products.get(viewHolder.getAdapterPosition()).get_id()));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = products.get(position);
        holder.productNameTv.setText(product.getName());
        holder.productTypeTv.setText(product.getType());
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout parentLayout;
        TextView productNameTv, productTypeTv, productDeleteBtn;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            parentLayout = itemView.findViewById(R.id.parent_layout_products);
            productNameTv = itemView.findViewById(R.id.product_name_tv);
            productTypeTv = itemView.findViewById(R.id.product_type_tv);
            productDeleteBtn = itemView.findViewById(R.id.product_delete_btn);
        }
    }
}
