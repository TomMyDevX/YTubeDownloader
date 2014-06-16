package com.tools.tommydev.videofinder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import com.tools.tommydev.videofinder.DataBaseHelper.DBClass_Downloader;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

public class DownloadTask_for_WebviewDetecter extends AsyncTask<String, Integer, String> {

    private Context context;
   
    String title;

    String url;
    String type;
    public DownloadTask_for_WebviewDetecter(Context context ,String title,String url,String type) {
        this.context = context;
       
        this.title=title;
        this.url=url;
        this.type=type;

        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage(title);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgressDrawable(context.getResources().getDrawable(R.drawable.progressbar));
        mProgressDialog.show();
    }
    boolean finish = false;
    @SuppressWarnings("resource")
	@Override
    protected String doInBackground(String... sUrl) {
        // take CPU lock to prevent CPU from going off if the user 
        // presses the power button during download
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,getClass().getName());
        wl.acquire();
        
       
    	 File file = null ;
      
        try {
        	 
 
             /*if(error){
                 showNotification_webView( file_downloaders.get(i).get_file_title(),file_downloaders.get(i).get_file_vid(),videos_error.get(0).error_type);
                 finish=true;
              
             }*/

        		SharedPreferences settings = context.getSharedPreferences("Youtube_downloader_tommy", 0);


         

                 //  URL myUrl = new URL(retVidUrl);
                 //  URLConnection urlConnection = myUrl.openConnection();
                 //  urlConnection.connect();
                 // int file_size = urlConnection.getContentLength();





                 settings = context.getSharedPreferences("Youtube_downloader_tommy", 0);
                 String urlToDownload = url;
                 String filename = "";
                 //if(settings.getBoolean("checkBox_save_video_with_id", false)){
                  //   filename=id;
                // }else{
                     filename=title.replace("\"", "").replace("<", "").replace(">", "").replace("|", "").replace("\\", "").replace("/", "").replace("?", "").replace(":", "").replace("*", "");
                //}





                 Log.e("FILE_X","["+filename+ "][" + title + "]" + "[" + type + "][" + "No_Quality_" + "]>>>" + url);

                // String filename = file_downloaders.get(i).get_file_vid();
                 String filetype = type;
                 // ResultReceiver receiver = (ResultReceiver) intent.getParcelableExtra("receiver");
                 
        	 
                 String location=settings.getString("disk_save_location", Environment.getExternalStorageDirectory().toString());

                 File SDCardRoot = new File(location + "/download/");
                 SDCardRoot.mkdirs();
                 file = new File(SDCardRoot, filename + filetype);

                // boolean filenameValid = isValidFileName(file.getAbsolutePath());
                 //if(filenameValid){
                     filename=title.replace("\"", "").replace("<", "").replace(">", "").replace("|", "").replace("\\", "").replace("/", "").replace("?", "").replace(":", "").replace("*", "");
                // }else{
                  //   filename=id;
                //}
                 file = new File(SDCardRoot, filename + filetype);
                 
                 SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
                 Date date = new Date();
                 String datetime=sdf.format(date);
                 DBClass_Downloader dbClass_downloader=new DBClass_Downloader(context);
                 dbClass_downloader.InsertData(url, title, "."+type,file.getAbsolutePath(), file.length()+"",datetime, "In_Queue");

        	
        	
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(urlToDownload);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // expect HTTP 200 OK, so we don't mistakenly save error report 
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
                     return "Server returned HTTP " + connection.getResponseCode() 
                         + " " + connection.getResponseMessage();

                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();

                // download the file
                input = connection.getInputStream();
                output = new FileOutputStream(file);

                byte data[] = new byte[1024];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled())
                        return null;
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } 
                catch (IOException ignored) { }

                if (connection != null)
                    connection.disconnect();
            }
        } finally {
            wl.release();
        }
        return file.getAbsolutePath();
    }
    
    public boolean isValidFileName(final String aFileName) {
        final File aFile = new File(aFileName);
        boolean isValid = true;
        try {
            if (aFile.createNewFile()) {
                aFile.delete();
            }
        } catch (IOException e) {
            isValid = false;
        }
        return isValid;
    }
    
    ProgressDialog mProgressDialog;
 
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        
       
        
     // declare the dialog as a member field of your activity
       

        // instantiate it within the onCreate method

        
       /* mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
            	cancel(true);
            }
        });*/
        
    }

    
    
    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
        // if we get here, length is known, now set indeterminate to false
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setMax(100);
        mProgressDialog.setProgress(progress[0]);
    }

    @Override
    protected void onPostExecute(String result) {
    	
        mProgressDialog.dismiss();
       // listXAdapter.ReStartAdapter();
       // Intent ixX = new Intent();
       // ixX.setAction(Test_Downloader_By_service.CUSTOM_INTENT);
       // ixX.putExtra("progress", 100);
        //ixX.putExtra("vid", id);
        //ixX.putExtra("title", title);
       // context.getApplicationContext().sendBroadcast(ixX);
        DBClass_Downloader dbClass_downloader = new DBClass_Downloader(context);
        dbClass_downloader.UpdateDownloadData(url,"Downloaded");
        try{
            MediaScannerConnection.scanFile(context, new String[]{result}, null, null);
        }catch (Exception e){}

        if (result == null)
            Toast.makeText(context,"Download error: "+result, Toast.LENGTH_LONG).show();
        else
            Toast.makeText(context,"File downloaded", Toast.LENGTH_SHORT).show();
    }
}
