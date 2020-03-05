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
import com.djaphar.coffeepointapp.R;
import com.djaphar.coffeepointapp.SupportClasses.ApiClasses.Point;
import com.djaphar.coffeepointapp.SupportClasses.OtherClasses.ViewDriver;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class PointsRecyclerViewAdapter extends RecyclerView.Adapter<PointsRecyclerViewAdapter.ViewHolder> {

    private RelativeLayout pointListLayout;
    private ConstraintLayout pointEditLayout;
    private ArrayList<Point> points;
    private MainActivity mainActivity;
    private EditText pointNameFormEd, pointAboutFormEd;
    private TextView pointActiveSwitchFormTv;
    private SwitchCompat pointActiveSwitchForm;
    private String statusTrueText, statusFalseText;
    private int statusTrueColor, statusFalseColor;
    private Resources resources;
    private Animation animation;

    public PointsRecyclerViewAdapter(ArrayList<Point> points, RelativeLayout pointListLayout,
                                     ConstraintLayout pointEditLayout, MainActivity mainActivity, EditText pointNameFormEd,
                                     EditText pointAboutFormEd, TextView pointActiveSwitchFormTv, SwitchCompat pointActiveSwitchForm) {
        this.pointListLayout = pointListLayout;
        this.pointEditLayout = pointEditLayout;
        this.points = points;
        this.mainActivity = mainActivity;
        this.pointNameFormEd = pointNameFormEd;
        this.pointAboutFormEd = pointAboutFormEd;
        this.pointActiveSwitchFormTv = pointActiveSwitchFormTv;
        this.pointActiveSwitchForm = pointActiveSwitchForm;
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

        viewHolder.parentLayout.setOnClickListener(view1 -> {
            Point point = points.get(viewHolder.getAdapterPosition());
            pointNameFormEd.setText(point.getName());
            pointAboutFormEd.setText(point.getAbout());
            if (point.isActive()) {
                ViewDriver.setSwitchAndLabel(pointActiveSwitchForm, pointActiveSwitchFormTv, statusTrueText, statusTrueColor, true);
            } else {
                ViewDriver.setSwitchAndLabel(pointActiveSwitchForm, pointActiveSwitchFormTv, statusFalseText, statusFalseColor, false);
            }
            mainActivity.setActionBarTitle(context.getString(R.string.title_point_edit));
            pointEditLayout.setTranslationX(resources.getDimension(R.dimen.point_edit_layout_translation_x));
            animation = ViewDriver.showView(pointEditLayout, R.anim.full_screen_show_animation, context);
            if (animation == null) {
                return;
            }
            animation.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationEnd(Animation animation) {
                    pointListLayout.setVisibility(View.INVISIBLE);
                    pointEditLayout.setClickable(true);
                }

                @Override
                public void onAnimationStart(Animation animation) { }

                @Override
                public void onAnimationRepeat(Animation animation) { }
            });
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Point point = points.get(position);
        ViewDriver.setStatusTvOptions(holder.listPointName, point.getName(), resources.getColor(R.color.colorBlack87));
        if (point.isActive()) {
            ViewDriver.setStatusTvOptions(holder.listPointStatus, statusTrueText, statusTrueColor);
        } else {
            ViewDriver.setStatusTvOptions(holder.listPointStatus, statusFalseText, statusFalseColor);
        }
    }

    @Override
    public int getItemCount() {
        return points.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
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
