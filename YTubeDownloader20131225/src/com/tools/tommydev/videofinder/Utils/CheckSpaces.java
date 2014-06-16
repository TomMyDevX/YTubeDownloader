package com.tools.tommydev.videofinder.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.StatFs;

import com.tools.tommydev.videofinder.CustomView.TextProgressBar;

/**
 * Created by TomMy on 10/13/13.
 */
public class CheckSpaces {
    public  static  void getFreeSpace(Context context,TextProgressBar disk_space,boolean showText){


//        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
//       // double sdAvailSize = (double)stat.getAvailableBlocks()*(double)stat.getBlockSize();
//        int Total = (statFs.getBlockCount() * statFs.getBlockSize()) / 1048576;
//        int Free  = (statFs.getAvailableBlocks() * statFs.getBlockSize()) / 1048576;

        SharedPreferences settings = context.getSharedPreferences("Youtube_downloader_tommy", 0);
        String disk_save_location = settings.getString("disk_save_location", Environment.getExternalStorageDirectory().toString());
        StatFs statFs = new StatFs(disk_save_location);
        long blockSize = statFs.getBlockSize();
        long totalSize = statFs.getBlockCount()*blockSize;
        long availableSize = statFs.getAvailableBlocks()*blockSize;
        long freeSize = statFs.getFreeBlocks()*blockSize;


    //    Log.e("Space",totalSize+":"+(totalSize-availableSize));
     //   Log.e("Space",100+":"+(totalSize-availableSize)*100/totalSize);
        try{
            if(totalSize>0){
                int uesd= (int) ((totalSize-availableSize)*100/totalSize);
                disk_space.setMax(100);
                disk_space.setProgress(uesd);
                disk_space.setText(uesd+" % used");
                disk_space.setShowText(showText);
            }else{
                disk_space.setText("Can't get availableSize on sdcard.");
            }
        }catch (Exception e){
                disk_space.setText("Can't get availableSize on sdcard.");
        }


    }

}
