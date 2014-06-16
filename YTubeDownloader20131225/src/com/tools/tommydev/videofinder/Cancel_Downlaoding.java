package com.tools.tommydev.videofinder;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.tools.tommydev.videofinder.DataBaseHelper.DBClass_Downloader;

import java.io.File;

/**
 * Created by TomMy on 10/15/13.
 */
public class Cancel_Downlaoding extends BroadcastReceiver {
    @Override
	public void onReceive(Context context, Intent intent) {

     //   if(intent.getIntExtra("progress",0)>=100){
//            DBClass_Downloader dbClass_downloader=new DBClass_Downloader(context);
//            dbClass_downloader.UpdateDownloadData(intent.getStringExtra("vid"),"Downloaded");
//            String ns = Context.NOTIFICATION_SERVICE;
//            NotificationManager nMgr = (NotificationManager) context.getApplicationContext().getSystemService(ns);
//            nMgr.cancel(0);
  //      }else{
           Intent intent1=new Intent(context,Test_Downloader_By_service.class);
           context.stopService(intent1);
            DBClass_Downloader dbClass_downloader=new DBClass_Downloader(context);
            dbClass_downloader.DeleteData(intent.getStringExtra("vid"));
            File filedel=new File(intent.getStringExtra("path_vid"));
            filedel.delete();
            Log.e("FileDownloadCancel",filedel.getAbsolutePath());
            String ns = Context.NOTIFICATION_SERVICE;
            NotificationManager nMgr = (NotificationManager) context.getApplicationContext().getSystemService(ns);
            nMgr.cancel(0);
            try{
            	
            }catch(Exception e){
            	 context.startService(intent1);
            }
          
     //   }


    //MediaScannerConnection.scanFile(context, new String[]{filedel.getAbsolutePath()}, null, null);
    }
}
