package com.djaphar.coffeepointapp.SupportClasses.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.djaphar.coffeepointapp.Activities.MainActivity;
import com.djaphar.coffeepointapp.Fragments.PointsFragment;
import com.djaphar.coffeepointapp.R;
import com.djaphar.coffeepointapp.SupportClasses.ApiClasses.Point;
import com.djaphar.coffeepointapp.SupportClasses.OtherClasses.ViewDriver;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class PointsRecyclerViewAdapter extends RecyclerView.Adapter<PointsRecyclerViewAdapter.ViewHolder> {

    private ConstraintLayout singlePointInfoContainer;
    private ArrayList<Point> points;
    private MainActivity mainActivity;
    private PointsFragment pointsFragment;
    private EditText pointEditNameFormEd, pointEditAboutFormEd;
    private String statusTrueText, statusFalseText;
    private int statusTrueColor, statusFalseColor;
    private Resources resources;
    private Animation animation;

    public PointsRecyclerViewAdapter(ArrayList<Point> points, String nullPointString, ConstraintLayout singlePointInfoContainer,
                                     MainActivity mainActivity, PointsFragment pointsFragment, EditText pointNameFormEd, EditText pointAboutFormEd) {
        this.singlePointInfoContainer = singlePointInfoContainer;
        this.points = points;
        this.mainActivity = mainActivity;
        this.pointsFragment = pointsFragment;
        this.pointEditNameFormEd = pointNameFormEd;
        this.pointEditAboutFormEd = pointAboutFormEd;
        if (points.size() == 0) {
            points.add(new Point(null, null, null, nullPointString,
                    null, null, null, null));
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.points_list, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);

        resources = context.getResources();
        statusTrueColor = resources.getColor(R.color.colorGreen60);
        statusFalseColor = resources.getColor(R.color.colorRed60);
        statusTrueText = context.getString(R.string.point_status_true);
        statusFalseText = context.getString(R.string.point_status_false);

        viewHolder.parentLayout.setOnClickListener(lView -> {
            Point point = points.get(viewHolder.getAdapterPosition());
            if (point.getCurrentlyNotHere() == null) {
                return;
            }
            pointsFragment.setCheckedPointId(point.get_id());
            pointsFragment.setPointProductRecyclerView(point.getProductList());
            String name = point.getName();
            if (name == null) {
                name = "";
            }
            pointEditNameFormEd.setText(name);
//            pointEditAboutFormEd.setText(point.getAbout());
            mainActivity.setActionBarTitle(context.getString(R.string.title_point_edit));
            singlePointInfoContainer.setTranslationX(resources.getDimension(R.dimen.point_edit_layout_translation_x));
            animation = ViewDriver.showView(singlePointInfoContainer, R.anim.show_right_animation, context);
            if (animation == null) {
                return;
            }
            animation.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationEnd(Animation animation) {
                    singlePointInfoContainer.setClickable(true);
                }

                @Override
                public void onAnimationStart(Animation animation) { }

                @Override
                public void onAnimationRepeat(Animation animation) { }
            });
        });

        viewHolder.pointDeleteBtn.setOnClickListener(lView -> pointsFragment.createProductDeleteDialog(points.get(viewHolder.getAdapterPosition()).get_id()));

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Point point = points.get(position);

        String name = point.getName();
        int color = R.color.colorBlack87;

        if (name == null || name.equals("")) {
            name = point.getPhoneNumber();
        }

        Boolean b = point.getCurrentlyNotHere();
        if (b == null) {
            color = R.color.colorBlack60;
            holder.pointDeleteBtn.setVisibility(View.GONE);
        } else if (b) {
            ViewDriver.setStatusTvOptions(holder.listPointStatus, statusTrueText, statusTrueColor);
        } else {
            ViewDriver.setStatusTvOptions(holder.listPointStatus, statusFalseText, statusFalseColor);
        }
        ViewDriver.setStatusTvOptions(holder.listPointName, name, resources.getColor(color));
    }

    @Override
    public int getItemCount() {
        return points.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout parentLayout;
        TextView listPointName, listPointStatus, pointDeleteBtn;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            parentLayout = itemView.findViewById(R.id.parent_layout_points);
            listPointName = itemView.findViewById(R.id.list_point_name);
            listPointStatus = itemView.findViewById(R.id.list_point_status);
            pointDeleteBtn = itemView.findViewById(R.id.point_delete_btn);
        }
    }
}
