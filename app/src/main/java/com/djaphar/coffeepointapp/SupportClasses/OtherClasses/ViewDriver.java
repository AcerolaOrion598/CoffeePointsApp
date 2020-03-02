package com.djaphar.coffeepointapp.SupportClasses.OtherClasses;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;

public class ViewDriver {

    public static Animation showView(View view, int animationResource, Context context) {
        Animation animation = AnimationUtils.loadAnimation(context, animationResource);

        if (view.getVisibility() == View.INVISIBLE || view.getVisibility() == View.GONE) {
            view.setVisibility(View.VISIBLE);
            view.startAnimation(animation);
        }

        return animation;
    }

    public static void hideView(View view, int animationResource, Context context) {
        Animation animation = AnimationUtils.loadAnimation(context, animationResource);

        if (view.getVisibility() == View.VISIBLE) {
            view.startAnimation(animation);
            view.setVisibility(View.INVISIBLE);
        }
    }

    public static void toggleViewInHalf(View view, int animationResource, Context context) {
        Animation animation = AnimationUtils.loadAnimation(context, animationResource);
        animation.setFillAfter(true);
        view.startAnimation(animation);
    }

    public static void setStatusTvOptions(TextView tv, String text, int color) {
        tv.setTextColor(color);
        tv.setText(text);
    }

    public static void setSwitchAndLabel(SwitchCompat switchCompat, TextView textView, String text, int color, boolean state) {
        setStatusTvOptions(textView, text, color);
        switchCompat.setChecked(state);
    }
}
