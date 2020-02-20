package com.djaphar.coffeepointapp.SupportClasses;

import android.content.Context;
import android.view.View;
import android.view.animation.AnimationUtils;

public class ViewDriver {

    public static void showView(View view, int animation, Context context) {
        if (view.getVisibility() == View.INVISIBLE) {
            view.setVisibility(View.VISIBLE);
            view.setAnimation(AnimationUtils.loadAnimation(context, animation));
        }
    }

    public static void hideView(View view, int animation, Context context) {
        if (view.getVisibility() == View.VISIBLE) {
            view.setAnimation(AnimationUtils.loadAnimation(context, animation));
            view.setVisibility(View.INVISIBLE);
        }
    }
}
