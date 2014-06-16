package com.tools.tommydev.videofinder.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tools.tommydev.videofinder.R;

/**
 * Created by TomMy on 9/10/13.
 */
public class AutoTextAdapter extends ArrayAdapter<String>{
    Context context;
    String []data;


    public AutoTextAdapter(Context context, int textViewResourceId,String [] objects) {
        super(context, textViewResourceId, objects);
        this.context=context;
        this.data=objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        if (row == null)
        {

            LayoutInflater inflater = ((Activity)context).getLayoutInflater ();  // we get a reference to the activity
            row = inflater.inflate (R.layout.autotextview, parent, false);
        }
        TextView tv1 = (TextView) row.findViewById (R.id.textView);
        tv1.setBackgroundColor (Color.TRANSPARENT);

        return  row;
    }
}
