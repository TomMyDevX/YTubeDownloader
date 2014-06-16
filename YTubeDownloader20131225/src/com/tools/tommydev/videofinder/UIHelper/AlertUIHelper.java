package com.tools.tommydev.videofinder.UIHelper;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tools.tommydev.videofinder.R;

/**
 * Created by TomMy on 17/9/2556.
 */
public class AlertUIHelper {
    LayoutInflater inflater;Activity activity;

    public AlertUIHelper(Activity activity) {
        inflater = activity.getLayoutInflater();
        this.activity=activity;
    }
    public void show(String textui,int Gravity) {
        View layout = inflater.inflate(R.layout.alertui,(ViewGroup)activity.findViewById(R.id.toast_layout_root));
        ImageView image = (ImageView) layout.findViewById(R.id.image);

        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(textui);
        Toast toast = new Toast(activity);
        toast.setGravity(Gravity, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    public void show(String textui,int Gravity,int timemill) {
        View layout = inflater.inflate(R.layout.alertui,(ViewGroup)activity.findViewById(R.id.toast_layout_root));
        ImageView image = (ImageView) layout.findViewById(R.id.image);

        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(textui);
        Toast toast = new Toast(activity);

        toast.setGravity(Gravity, 0, 0);
        toast.setDuration(timemill);
        toast.setView(layout);
        toast.show();
    }
}
