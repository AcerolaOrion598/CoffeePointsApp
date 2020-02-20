package com.djaphar.coffeepointapp.ui.points;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.djaphar.coffeepointapp.MainActivity;
import com.djaphar.coffeepointapp.R;
import com.djaphar.coffeepointapp.SupportClasses.Point;
import com.djaphar.coffeepointapp.SupportClasses.PointsRecyclerViewAdapter;
import com.djaphar.coffeepointapp.ViewModel.MainViewModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PointsFragment extends Fragment {

    private MainViewModel mainViewModel;
    private RecyclerView recyclerView;
    private Context context;
    private RelativeLayout pointListLayout;
    private ConstraintLayout pointEditLayout;
    private ArrayList<Point> points;
    private MainActivity mainActivity;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        View root = inflater.inflate(R.layout.fragment_points, container, false);
        recyclerView = root.findViewById(R.id.points_recycler_view);
        pointListLayout = root.findViewById(R.id.points_list_layout);
        pointEditLayout = root.findViewById(R.id.point_edit_layout);
        mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.setActionBarTitle(getString(R.string.title_points));
        }
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getContext();

        mainViewModel.getPoints().observe(getViewLifecycleOwner(), new Observer<ArrayList<Point>>() {
            @Override
            public void onChanged(ArrayList<Point> mPoints) {
                points = mPoints;
                PointsRecyclerViewAdapter adapter = new PointsRecyclerViewAdapter(points, context, pointListLayout,
                                                                                        pointEditLayout, mainActivity);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            }
        });
    }

    public ConstraintLayout getPointEditLayout() {
        return pointEditLayout;
    }

    public RelativeLayout getPointListLayout() {
        return pointListLayout;
    }
}