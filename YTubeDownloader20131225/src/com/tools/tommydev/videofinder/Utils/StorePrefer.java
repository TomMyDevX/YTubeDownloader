package com.tools.tommydev.videofinder.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

/**
 * Created by TomMy on 8/8/13.
 */
public class StorePrefer {
    public static void run_first_app(Context context){
        SharedPreferences settings = context.getSharedPreferences("Youtube_downloader_tommy", 0);
        boolean first_app = settings.getBoolean("first_app",true);
        if(first_app){
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("show_thumb", true);
            editor.putBoolean("first_app", false);
            editor.putInt("seek_text_var", 15);
            editor.putString("Quality", "mq");
            editor.putString("disk_save_location", Environment.getExternalStorageDirectory().toString());
            editor.putBoolean("checkBox_download_with_service", true);
            editor.commit();
        }//else{
            //SharedPreferences.Editor editor = settings.edit();
            //settings.getString("disk_save_location",Environment.getExternalStorageDirectory().toString());
            //editor.commit();
       // }
    }

}
