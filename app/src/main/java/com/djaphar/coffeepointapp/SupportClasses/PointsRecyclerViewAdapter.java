package com.djaphar.coffeepointapp.SupportClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.djaphar.coffeepointapp.MainActivity;
import com.djaphar.coffeepointapp.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class PointsRecyclerViewAdapter extends RecyclerView.Adapter<PointsRecyclerViewAdapter.ViewHolder> {

    private RelativeLayout pointListLayout;
    private ConstraintLayout pointEditLayout;
    private ArrayList<Point> points;
    private Context context;
    private MainActivity mainActivity;

    public PointsRecyclerViewAdapter(ArrayList<Point> points, Context context, RelativeLayout pointListLayout,
                                     ConstraintLayout pointEditLayout, MainActivity mainActivity) {
        this.pointListLayout = pointListLayout;
        this.pointEditLayout = pointEditLayout;
        this.points = points;
        this.context = context;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.points_list, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);

        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.setActionBarTitle(context.getString(R.string.title_point_edit));
                ViewDriver.showView(pointEditLayout, R.anim.full_screen_show_animation, context);
                pointListLayout.setVisibility(View.INVISIBLE);
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Point point = points.get(position);
        holder.listPointName.setTextColor(context.getResources().getColor(R.color.colorBlack87));
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
