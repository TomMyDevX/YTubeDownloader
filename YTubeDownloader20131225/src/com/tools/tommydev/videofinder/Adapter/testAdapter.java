package com.tools.tommydev.videofinder.Adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tools.tommydev.videofinder.Object.Texts_And_Images;

import java.util.ArrayList;

/**
 * Created by TomMy on 9/12/13.
 */
public class testAdapter extends BaseAdapter {
    Activity activity;
    ArrayList<Texts_And_Images> lists;

    public testAdapter(Activity activity, ArrayList<Texts_And_Images> lists) {
        this.activity = activity;
        this.lists = lists;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int i) {
        return lists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TextView textView=new TextView(activity);
        textView.setText(lists.get(i).getText());
        textView.setCompoundDrawablesWithIntrinsicBounds(activity.getResources().getDrawable(lists.get(i).getDrawable()),null,null,null);
        return textView;
    }



}
