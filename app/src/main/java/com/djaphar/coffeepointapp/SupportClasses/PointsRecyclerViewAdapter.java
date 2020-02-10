package com.djaphar.coffeepointapp.SupportClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.djaphar.coffeepointapp.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PointsRecyclerViewAdapter extends RecyclerView.Adapter<PointsRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Point> points;
    private Context context;

    public PointsRecyclerViewAdapter(ArrayList<Point> points, Context context) {
        this.points = points;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.points_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Point point = points.get(position);
        holder.listPointName.setText(point.getName());
        if (point.isActive()) {
            holder.listPointStatus.setTextColor(context.getResources().getColor(R.color.colorGreen60));
            holder.listPointStatus.setText(R.string.point_status_true);
        } else {
            holder.listPointStatus.setTextColor(context.getResources().getColor(R.color.colorRed60));
            holder.listPointStatus.setText(R.string.point_status_false);
        }
    }

    @Override
    public int getItemCount() {
        return points.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout parentLayout;
        TextView listPointName, listPointStatus;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            parentLayout = itemView.findViewById(R.id.parent_layout_points);
            listPointName = itemView.findViewById(R.id.list_point_name);
            listPointStatus = itemView.findViewById(R.id.list_point_status);
        }
    }
}
