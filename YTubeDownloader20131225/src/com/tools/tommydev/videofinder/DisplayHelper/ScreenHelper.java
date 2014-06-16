package com.tools.tommydev.videofinder.DisplayHelper;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * Created by TomMy on 9/5/13.
 */
public class ScreenHelper {
    int width=0;

    public ScreenHelper(Activity activity) {
        getScreenWidth(activity);
    }

    public  void getScreenWidth(Activity activity){
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
        setWidth(width);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
