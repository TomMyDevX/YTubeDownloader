package com.tools.tommydev.videofinder;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by TomMy on 10/17/13.
 */
public class WebView_Boardcast extends BroadcastReceiver {
    @Override
	public void onReceive(Context context, Intent intent) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) context.getApplicationContext().getSystemService(ns);
        nMgr.cancel(0);
        Intent intent1=new Intent(context,Webview_Detecter.class);
        intent1.putExtra("vid",intent.getStringExtra("vid"));
        intent1.putExtra("title",intent.getStringExtra("title"));
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);
        Intent intent_service=new Intent(context,Test_Downloader_By_service.class);
        context.stopService(intent_service);
    }
}
