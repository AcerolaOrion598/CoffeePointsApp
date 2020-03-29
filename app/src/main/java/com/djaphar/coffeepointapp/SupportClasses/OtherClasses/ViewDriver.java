package com.djaphar.coffeepointapp.SupportClasses.OtherClasses;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class ViewDriver {

    public static Animation showView(View view, int animationResource, Context context) {
        Animation animation = null;

        if (view.getVisibility() == View.INVISIBLE || view.getVisibility() == View.GONE) {
            view.setVisibility(View.VISIBLE);
            animation = animateView(view, animationResource, context);
        }

        return animation;
    }

    public static Animation hideView(View view, int animationResource, Context context) {
        Animation animation = null;

        if (view.getVisibility() == View.VISIBLE) {
            animation = animateView(view, animationResource, context);
            view.setVisibility(View.GONE);
        }

        return animation;
    }

    public static void setStatusTvOptions(TextView tv, String text, int color) {
        tv.setTextColor(color);
        tv.setText(text);
    }

    private static Animation animateView(View view, int animationResource, Context context) {
        Animation animation = AnimationUtils.loadAnimation(context, animationResource);
        view.startAnimation(animation);
        return animation;
    }
}
