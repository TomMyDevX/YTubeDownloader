package com.tools.tommydev.videofinder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;

import com.tools.tommydev.videofinder.Adapter.ListXAdapter;
import com.tools.tommydev.videofinder.DataBaseHelper.DBClass_Downloader;
import com.tools.tommydev.videofinder.Object.Meta;
import com.tools.tommydev.videofinder.Object.Video;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;
import android.widget.Toast;

public class DownloadTask extends AsyncTask<String, Integer, String[]> {

    private Context context;
    String id;
    String title;
    ListXAdapter listXAdapter;
	private NotificationManager notificationManager;

	private Builder mBuilder;
    public DownloadTask(Context context,ListXAdapter listXAdapter, String id, String title) {
    	  Intent notificationIntent = new Intent(context, Home.class);
          PendingIntent contentIntent = PendingIntent.getActivity(context, 999, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
    	
    	
    	context.getApplicationContext();
		notificationManager = (NotificationManager) context.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
    	mBuilder = new NotificationCompat.Builder(context);
    	mBuilder.setContentTitle(title)
    	    	.setContentText("Download in progress")
    	    	.setSmallIcon(R.drawable.download)
                .setAutoCancel(true)
                .setContentIntent(contentIntent)
                .setOngoing(true)
                .setContentText("");
    
        this.context = context;
        this.id=id;
        this.title=title;
        this.listXAdapter=listXAdapter;
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage(title);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(false);
       
    }
    
    boolean finish = false;
    @SuppressWarnings("resource")
	@Override
    protected String[] doInBackground(String... sUrl) {
        // take CPU lock to prevent CPU from going off if the user 
        // presses the power button during download
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,getClass().getName());
        wl.acquire();
        
        ArrayList<Video> videos = null;
        File file = null ;
        boolean error=false;
        try {
        	 
        	 while (videos == null) {
                 ArrayList<Video> videos_temp = null;
				try {
					videos_temp = getStreamingUrisFromYouTubePage("https://www.youtube.com/watch?v=" + id);
					  if(videos_temp.size()>0){
		                     if(videos_temp.get(0).error_type.equals("-1")){
		                         videos=videos_temp;
		                     }else if(videos_temp.get(0).error_type.equals("Couldn't find any URLs and corresponding signatures")){
		                         videos=null;
		                     }else{
		                         error=true;
		                         break;
		                     }
		                 }
				} catch (IOException e) {
					videos=null;
				}
               
             }
        	
        	 
        	 

             if(error){
                 finish=true;
                 return null;
             }




             if (videos != null && !videos.isEmpty()) {
                 String retVidUrl = null;
                 String type = "";
                 String Quality = "";
                 SharedPreferences settings = context.getSharedPreferences("Youtube_downloader_tommy", 0);
                 String Quality_User = settings.getString("Quality", "mq");
                 Log.e("test", Quality_User);


                 if (videos.size() > 0) {
                     Collections.sort(videos, new Comparator<Video>() {
                         

                         @Override
                         public int compare(Video video, Video video2) {
                             return video.type_id.compareTo(video2.type_id);
                         }

                     });
                 }
                 for (Video video : videos) {
                     Log.e("test", video.ext + "|" + video.type + "|" + video.url);
                 }


                 for (Video video : videos) {
                     Log.e("test", video.type);
                     if (video.ext.toLowerCase().contains("mp4") && video.type.toLowerCase().contains("high") && video.type.endsWith("1920x1080") && Quality_User.equals("fhq")) {
                         Log.e("test", video.type.toLowerCase() + "|" + Quality_User);
                         retVidUrl = video.url;
                         type = ".mp4";
                         Quality = "Full HD Quality";
                         break;
                     }

                     if (video.ext.toLowerCase().contains("mp4") && video.type.toLowerCase().contains("high") && video.type.endsWith("1280x720") && (Quality_User.equals("hq") || Quality_User.equals("fhq"))) {
                         Log.e("test", video.type.toLowerCase() + "|" + Quality_User);
                         retVidUrl = video.url;
                         type = ".mp4";
                         Quality = "HD Quality";
                         break;
                     }

                     if (video.ext.toLowerCase().contains("mp4") && video.type.toLowerCase().contains("medium") && (Quality_User.equals("mq") || Quality_User.equals("hq") || Quality_User.equals("fhq"))) {
                         Log.e("test", video.type.toLowerCase() + "|" + Quality_User);
                         retVidUrl = video.url;
                         type = ".mp4";
                         Quality = "Medium Quality";
                         break;
                     }
                 }
                 if (retVidUrl == null) {
                     for (Video video : videos) {
                         if (video.ext.toLowerCase().contains("3gp")
                                 && video.type.toLowerCase().contains(
                                 "medium") && (Quality_User.equals("lq") || Quality_User.equals("mq") || Quality_User.equals("hq") || Quality_User.equals("fhq"))) {
                             Log.e("test", video.type.toLowerCase() + "|" + Quality_User);
                             retVidUrl = video.url;
                             type = ".3gp";
                             Quality = "Low Quality";
                             break;

                         }
                     }
                 }
                 if (retVidUrl == null) {

                     for (Video video : videos) {
                         if (video.ext.toLowerCase().contains("mp4")
                                 && video.type.toLowerCase().contains("low") && (Quality_User.equals("lq") || Quality_User.equals("mq") || Quality_User.equals("hq") || Quality_User.equals("fhq"))) {
                             Log.e("test", video.type.toLowerCase() + "|" + Quality_User);
                             retVidUrl = video.url;
                             type = ".mp4";
                             Quality = "Low Quality";
                             break;

                         }
                     }
                 }
                 if (retVidUrl == null) {
                     for (Video video : videos) {
                         if (video.ext.toLowerCase().contains("3gp")
                                 && video.type.toLowerCase().contains("low") && (Quality_User.equals("lq") || Quality_User.equals("mq") || Quality_User.equals("hq") || Quality_User.equals("fhq"))) {
                             Log.e("test", video.type.toLowerCase() + "|" + Quality_User);
                             retVidUrl = video.url;
                             type = ".3gp";
                             Quality = "Low Quality";
                             break;
                         }
                     }
                 }

                 //  URL myUrl = new URL(retVidUrl);
                 //  URLConnection urlConnection = myUrl.openConnection();
                 //  urlConnection.connect();
                 // int file_size = urlConnection.getContentLength();





                 settings = context.getSharedPreferences("Youtube_downloader_tommy", 0);
                 String urlToDownload = retVidUrl;
                 String filename = "";
                 if(settings.getBoolean("checkBox_save_video_with_id", false)){
                     filename=id;
                 }else{
                     filename=title.replace("\"", "").replace("<", "").replace(">", "").replace("|", "").replace("\\", "").replace("/", "").replace("?", "").replace(":", "").replace("*", "");
                 }





                 Log.e("FILE_X","["+filename+ "][" + title + "]" + "[" + type + "][" + Quality + "]>>>" + retVidUrl);

                // String filename = file_downloaders.get(i).get_file_vid();
                 String filetype = type;
                 // ResultReceiver receiver = (ResultReceiver) intent.getParcelableExtra("receiver");
                 
        	 
                 String location=settings.getString("disk_save_location", Environment.getExternalStorageDirectory().toString());

                 File SDCardRoot = new File(location + "/download/");
                 SDCardRoot.mkdirs();
                 file = new File(SDCardRoot, filename + filetype);

                 boolean filenameValid = isValidFileName(file.getAbsolutePath());
                 if(filenameValid){
                     filename=title.replace("\"", "").replace("<", "").replace(">", "").replace("|", "").replace("\\", "").replace("/", "").replace("?", "").replace(":", "").replace("*", "");
                 }else{
                     filename=id;
                 }
                 file = new File(SDCardRoot, filename + filetype);

        	
        	
        	
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
                     return new String[]{"-1","Server returned HTTP " + connection.getResponseCode() 
                         + " " + connection.getResponseMessage()};

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
                return new String[]{id,type,file.getAbsolutePath(),fileLength+""};
            } catch (Exception e) {
                return new String[]{"-1",e.toString()};
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
            }}
        } finally {
            wl.release();
        }
        return null;
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
    public ArrayList<Video> getStreamingUrisFromYouTubePage(String ytUrl)
            throws IOException {
        if (ytUrl == null) {
            return null;
        }

        // Remove any query params in query string after the watch?v=<vid> in
        // e.g.
        // http://www.youtube.com/watch?v=0RUPACpf8Vs&feature=youtube_gdata_player
        int andIdx = ytUrl.indexOf('&');
        if (andIdx >= 0) {
            ytUrl = ytUrl.substring(0, andIdx);
        }

        // Get the HTML response
        String userAgent = "Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1667.0 Safari/537.36";
        HttpClient client = new DefaultHttpClient();
        client.getParams().setParameter(CoreProtocolPNames.USER_AGENT,userAgent);
        HttpGet request = new HttpGet(ytUrl);
        HttpResponse response = client.execute(request);
        String html = "";
        InputStream in = response.getEntity().getContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder str = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            str.append(line.replace("\\u0026", "&"));
        }
        in.close();
        html = str.toString();




      //  generateNoteOnSD("urltest.txt",html);









        Log.e("html",html);

        // Parse the HTML response and extract the streaming URIs
        if (html.contains("verify-age-thumb")) {
            Log.e("test", "YouTube is asking for age verification. We can't handle that sorry.");
            ArrayList<Video> videos = new ArrayList<Video>();;
            Video newVideo = new Video("", "", "", "","Error : Tab to cancel.");
            videos.add(newVideo);
            return videos;
        }

        if (html.contains("das_captcha")) {
            Log.e("test", "Captcha found, please try with different IP address.");
            ArrayList<Video> videos = new ArrayList<Video>();
            Video newVideo = new Video("", "", "", "","Error IP Problem : Tab cancel.");
            videos.add(newVideo);
            return videos;
          //  return null;
        }

        Pattern p = Pattern.compile("stream_map\": \"(.*?)?\"");
        // Pattern p = Pattern.compile("/stream_map=(.[^&]*?)\"/");
        Matcher m = p.matcher(html);
        List<String> matches = new ArrayList<String>();
        while (m.find()) {
            matches.add(m.group());
        }

        if (matches.size() != 1) {
            // progressDialog.dismiss();
            //  cancel(true);
            Log.e("test", "Found zero or too many stream maps.");
            ArrayList<Video> videos = new ArrayList<Video>();
            Video newVideo = new Video("", "", "", "","Error : Tab to use internet browser for detect.");
            videos.add(newVideo);
            return videos;
            //return null;
        }

        String urls[] = matches.get(0).split(",");
        HashMap<String, String> foundArray = new HashMap<String, String>();
        for (String ppUrl : urls) {
            String url = URLDecoder.decode(ppUrl, "UTF-8");

            Pattern p1 = Pattern.compile("itag=([0-9]+?)[&]");
            Matcher m1 = p1.matcher(url);
            String itag = null;
            if (m1.find()) {
                itag = m1.group(1);
            }

            Pattern p2 = Pattern.compile("sig=(.*?)[&]");
            Matcher m2 = p2.matcher(url);
            String sig = null;
            if (m2.find()) {
                sig = m2.group(1);
            }

            Pattern p3 = Pattern.compile("url=(.*?)[&]");
            Matcher m3 = p3.matcher(ppUrl);
            String um = null;
            if (m3.find()) {
                um = m3.group(1);
            }

            if (itag != null && sig != null && um != null) {
                foundArray.put(itag, URLDecoder.decode(um, "UTF-8") + "&"
                        + "signature=" + sig);
            }
        }

        if (foundArray.size() == 0) {
            ArrayList<Video> videos = new ArrayList<Video>();
            Log.e("test", "Couldn't find any URLs and corresponding signatures");
            Video newVideo = new Video("", "", "", "","Couldn't find any URLs and corresponding signatures");
            videos.add(newVideo);
            return videos;
        }

        HashMap<String, Meta> typeMap = new HashMap<String, Meta>();
        typeMap.put("13", new Meta("13", "3GP", "Low Quality - 176x144", "n"));
        typeMap.put("17", new Meta("17", "3GP", "Medium Quality - 176x144", "m"));
        typeMap.put("36", new Meta("36", "3GP", "High Quality - 320x240", "l"));
        typeMap.put("5", new Meta("5", "FLV", "Low Quality - 400x226", "k"));
        typeMap.put("6", new Meta("6", "FLV", "Medium Quality - 640x360", "j"));
        typeMap.put("34", new Meta("34", "FLV", "Medium Quality - 640x360", "i"));
        typeMap.put("35", new Meta("35", "FLV", "High Quality - 854x480", "h"));
        typeMap.put("43", new Meta("43", "WEBM", "Low Quality - 640x360", "g"));
        typeMap.put("44", new Meta("44", "WEBM", "Medium Quality - 854x480", "f"));
        typeMap.put("45", new Meta("45", "WEBM", "High Quality - 1280x720", "e"));
        typeMap.put("18", new Meta("18", "MP4", "Medium Quality - 480x360", "d"));
        typeMap.put("22", new Meta("22", "MP4", "High Quality - 1280x720", "c"));
        typeMap.put("37", new Meta("37", "MP4", "High Quality - 1920x1080", "b"));
        typeMap.put("33", new Meta("38", "MP4", "High Quality - 4096x230", "a"));


        ArrayList<Video> videos = new ArrayList<Video>();
        for (String format : typeMap.keySet()) {
            Meta meta = typeMap.get(format);

            if (foundArray.containsKey(format)) {
                Video newVideo = new Video(meta.ext, meta.type, foundArray.get(format), meta.type_id,"-1");
                videos.add(newVideo);
                // Log.e("test", "YouTube Video streaming details: ext:" + newVideo.ext
                //         + ", type:" + newVideo.type + ", url:" + newVideo.url);
            }
        }

        return videos;
    }
    @Override
    protected void onPreExecute() {
    	
        super.onPreExecute();
        mProgressDialog.show();
     // declare the dialog as a member field of your activity
       

        // instantiate it within the onCreate method

        
       /* mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
            	cancel(true);
            }
        });*/
        
    }

    
    int xprogress=0;
    @Override
    protected void onProgressUpdate(Integer... progress) {
    	
    	
        super.onProgressUpdate(progress);
        if (progress[0]%20 == 0&&xprogress!=progress[0]) { // 20 updates every 5%, 10 every 10% and 5 every 20%, etc.
        		xprogress=progress[0];
            	mBuilder.setProgress(100, progress[0], false);
                notificationManager.notify(0, mBuilder.build());
        }
         
        // if we get here, length is known, now set indeterminate to false
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setMax(100);
        mProgressDialog.setProgress(progress[0]);
        
        
        
        if(progress[0]>=100){
        	   Intent notificationIntent = new Intent(context, Home.class);
               PendingIntent contentIntent = PendingIntent.getActivity(context, 999, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
               mBuilder.setContentText("Download complete")
               .setContentIntent(contentIntent)
                      .setProgress(0,0,false);
        	  notificationManager.notify(0, mBuilder.build());
        }
    }

    @Override
    protected void onPostExecute(String []result) {
    	
        mProgressDialog.dismiss();
        

       // Intent ixX = new Intent();
       // ixX.setAction(Test_Downloader_By_service.CUSTOM_INTENT);
       // ixX.putExtra("progress", 100);
        //ixX.putExtra("vid", id);
        //ixX.putExtra("title", title);
       // context.getApplicationContext().sendBroadcast(ixX);
      

        if (result == null){
        	showNotification_webView(title,id,"");
            Toast.makeText(context,"Download error: "+result+"", Toast.LENGTH_LONG).show();
        }else{
        	if(result[0]!="-1"){
        		listXAdapter.ReStartAdapter();
          	  DBClass_Downloader dbClass_downloader = new DBClass_Downloader(context);
          	  dbClass_downloader.UpdateData(result[0], result[1], result[2], result[3] + "");
                dbClass_downloader.UpdateDownloadData(id,"Downloaded");
               
                
                try{
                    MediaScannerConnection.scanFile(context, new String[]{result[2]}, null, null);
                }catch (Exception e){}
              Toast.makeText(context,"File downloaded", Toast.LENGTH_SHORT).show();
        	}else{
        		showNotification_webView(title,id,"");
                Toast.makeText(context,"Download error: "+result+"", Toast.LENGTH_LONG).show();
        	}
              
        }
    }
    
    
    NotificationCompat.Builder mBuilder_webview;
    private NotificationManager notificationManager_webview;
    public void showNotification_webView(String title, String vid, String error_type) {
        Log.e("E_Path",vid+"\n"+vid);
        Intent notificationIntent = new Intent(context, WebView_Boardcast.class);
        notificationIntent.putExtra("vid",vid);
        notificationIntent.putExtra("title",title);
        PendingIntent contentIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder_webview =new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.action_search)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setContentIntent(contentIntent)
                .setOngoing(true)
                .setContentText(error_type);
        mBuilder_webview.setProgress(0,0,false);
        context.getApplicationContext();
		notificationManager_webview = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
      notificationManager_webview.notify(0, mBuilder_webview.build());
    }
}
