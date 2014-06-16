package com.tools.tommydev.videofinder.Fragment;


import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tools.tommydev.videofinder.DataBaseHelper.DBClass_favor;
import com.tools.tommydev.videofinder.R;

/**
 * Created by TomMy on 9/5/13.
 */
public class Apps_Fragment extends Fragment {

    DBClass_favor.sMembers sMembers1;
    String title;

    public Apps_Fragment(String title) {
        this.title = title;
    }
    Activity activity;


    DBClass_favor.sMembers sMembers;



    public Apps_Fragment(DBClass_favor.sMembers sMembers, DBClass_favor.sMembers sMembers1, Activity activity) {
        this.sMembers=sMembers;
        this.sMembers1=sMembers1;
        this.activity=activity;
    }
    public Apps_Fragment(DBClass_favor.sMembers sMembers, Activity activity) {
        this.sMembers=sMembers;
        this.activity=activity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View inflate = inflater.inflate(R.layout.fragment_sppa, container, false);
        TextView textView1= (TextView) inflate.findViewById(R.id.textView1);
        TextView textView2= (TextView) inflate.findViewById(R.id.textView2);
        try{
            textView1.setText(sMembers.get_Filename());
        }catch (Exception e){}
        ImageView imageView1= (ImageView) inflate.findViewById(R.id.imageView1);
        try{
            imageView1.setImageURI(Uri.parse(Environment.getExternalStorageDirectory() + "/Android/data/" + activity.getClass().getPackage().getName() + "/images/" + sMembers.get_Filesize()));
        }catch (Exception e){};

        try{
            textView2.setText(sMembers1.get_Filename());
        }catch (Exception e){}


            ImageView imageView2= (ImageView) inflate.findViewById(R.id.imageView2);
            try{
            imageView2.setImageURI(Uri.parse(Environment.getExternalStorageDirectory() + "/Android/data/" + activity.getClass().getPackage().getName() + "/images/" + sMembers1.get_Filesize()));
            }catch (Exception e){};


                return inflate;
    }


}
