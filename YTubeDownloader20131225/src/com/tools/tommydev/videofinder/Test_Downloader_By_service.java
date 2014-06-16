package com.tools.tommydev.videofinder;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.tools.tommydev.videofinder.DataBaseHelper.DBClass_Downloader;
import com.tools.tommydev.videofinder.Object.Meta;
import com.tools.tommydev.videofinder.Object.Video;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by TomMy on 10/13/13.
 */
public class Test_Downloader_By_service extends Service {
    public static final String CUSTOM_INTENT = "com.tools.tommydev.videofinder.ProgressReceiver";
    private final IBinder mBinder = new LocalBinder();
    private Download_Thread thread;

    @Override
    public void onCreate() {
        thread = new Download_Thread();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("ServiceStart", "ServiceStart");
        if (!thread.isAlive()) {
        	try{
        		thread.start();
        	}catch(Exception e){
        		Log.e("onStartCommand","ServiceStart",e);
        	}
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.e("ServiceonDestroy", "ServiceonDestroy");
        thread.finish = true;
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(ns);
        nMgr.cancel(0);
      //  Intent ixX = new Intent();
      //  ixX.setAction(CUSTOM_INTENT);
       // ixX.putExtra("progress", 0);
       // ixX.putExtra("vid", "");
      //  ixX.putExtra("title", "No Download in Queue.");
     //   Test_Downloader_By_service.this.getApplicationContext().sendBroadcast(ixX);
        System.gc();
        System.gc();
        super.onDestroy();

    }

    @Override
	public IBinder onBind(Intent intent) {
        return mBinder;
    }

   
    //    private final Random mGenerator = new Random();
    public class LocalBinder extends Binder {
        Test_Downloader_By_service getService() {
            // Return this instance of LocalService so clients can call public methods
            return Test_Downloader_By_service.this;
        }
    }
    int count_thred=0;
    private class Download_Thread extends Thread {
//
        boolean finish = false;

        @Override
        public void run() {


            while (true) {
                try {
                    DBClass_Downloader dbClass_downloader = new DBClass_Downloader(Test_Downloader_By_service.this);
                    boolean dbClass_downloader_have_size = dbClass_downloader.SelectAllData(" file_status='In_Queue'").size() > 0;
                    if (dbClass_downloader_have_size) {
                        count_thred=0;
                        final List<DBClass_Downloader.File_Downloader> file_downloaders = dbClass_downloader.SelectAllData("file_status='In_Queue'");
                        int downloader_size = file_downloaders.size();

                        for (int i = 0; i < downloader_size; i++) {
                            showNotification(file_downloaders.get(i).get_file_title(),file_downloaders.get(i).get_file_vid(),"",0);
                            int progressX = 0;
                            ArrayList<Video> videos = null;
                            ArrayList<Video> videos_error = null;
                            final int retry_count=0;
                            boolean error=false;
                            while (videos == null) {
                            	
                            	if(file_downloaders.get(i).get_file_vid().startsWith("http://"))return;
                            	
                                if(finish)return;
                                ArrayList<Video> videos_temp = getStreamingUrisFromYouTubePage("https://www.youtube.com/watch?v=" + file_downloaders.get(i).get_file_vid());
                                if(videos_temp.size()>0){
                                    if(videos_temp.get(0).error_type.equals("-1")){
                                        videos=videos_temp;
                                    }else if(videos_temp.get(0).error_type.equals("Couldn't find any URLs and corresponding signatures")){
                                        videos=null;
                                    }else{
                                        videos_error=videos_temp;
                                        error=true;
                                        break;
                                    }
                                }
                            }


                            if(error){
                                showNotification_webView( file_downloaders.get(i).get_file_title(),file_downloaders.get(i).get_file_vid(),videos_error.get(0).error_type);
                                finish=true;
                                return;
                            }




                            if (videos != null && !videos.isEmpty()) {
                                String retVidUrl = null;
                                String type = "";
                                String Quality = "";
                                SharedPreferences settings = getSharedPreferences("Youtube_downloader_tommy", 0);
                                String Quality_User = settings.getString("Quality", "mq");
                                Log.e("test", Quality_User);


                                if (videos.size() > 0) {
                                    Collections.sort(videos, new Comparator<Video>() {
                                        int type_screen;

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





                                settings = Test_Downloader_By_service.this.getApplicationContext().getSharedPreferences("Youtube_downloader_tommy", 0);
                                String urlToDownload = retVidUrl;
                                String filename = "";
                                if(settings.getBoolean("checkBox_save_video_with_id", false)){
                                    filename=file_downloaders.get(i).get_file_vid();
                                }else{
                                    filename=file_downloaders.get(i).get_file_title().replace("\"", "").replace("<", "").replace(">", "").replace("|", "").replace("\\", "").replace("/", "").replace("?", "").replace(":", "").replace("*", "");
                                }





                                Log.e("FILE_X","["+filename+ "][" + file_downloaders.get(i).get_file_title() + "]" + "[" + type + "][" + Quality + "]>>>" + retVidUrl);

                               // String filename = file_downloaders.get(i).get_file_vid();
                                String filetype = type;
                                // ResultReceiver receiver = (ResultReceiver) intent.getParcelableExtra("receiver");
                                try {
                                    URL url = new URL(urlToDownload);
                                    URLConnection connection = url.openConnection();
                                    connection.setConnectTimeout(60000);
                                    connection.connect();

                                    // this will be useful so that you can show a typical 0-100% progress bar
                                    final int fileLength = connection.getContentLength();

                                    // download the file



                                    String location=settings.getString("disk_save_location", Environment.getExternalStorageDirectory().toString());

                                    File SDCardRoot = new File(location + "/download/");
                                    SDCardRoot.mkdirs();
                                    File file = new File(SDCardRoot, filename + filetype);

                                    boolean filenameValid = isValidFileName(file.getAbsolutePath());
                                    if(filenameValid){
                                        filename=file_downloaders.get(i).get_file_title().replace("\"", "").replace("<", "").replace(">", "").replace("|", "").replace("\\", "").replace("/", "").replace("?", "").replace(":", "").replace("*", "");
                                    }else{
                                        filename=file_downloaders.get(i).get_file_vid();
                                    }
                                    file = new File(SDCardRoot, filename + filetype);

                                    dbClass_downloader.UpdateData(file_downloaders.get(i).get_file_vid(), type, file.getAbsolutePath(), fileLength + "");


                                    showNotification(file_downloaders.get(i).get_file_title(),file_downloaders.get(i).get_file_vid(),file.getAbsolutePath(),0);

                                    InputStream input = new BufferedInputStream(url.openStream());
                                    OutputStream output = new FileOutputStream(file);

                                    byte data[] = new byte[1024];
                                    long total = 0;
                                    int count;
                                    while ((count = input.read(data)) != -1) {
                                        total += count;
                                        if( thread.finish){
                                           break;
                                        }

                                        // publishing the progress....

                                        if (progressX != (int) (total * 100 / fileLength)) {
                                            progressX = (int) (total * 100 / fileLength);
                                            // Bundle resultData = new Bundle();
                                            // resultData.putString("title", file_downloaders.get(i).get_file_title());
                                            // resultData.putInt("progress", (int) (total * 100 / fileLength));
                                            //Log.e("Writedata",(int) (total * 100 / fileLength)+"");
                                            // receiver.send(UPDATE_PROGRESS, resultData);
                                            
                                            Intent intent = new Intent(CUSTOM_INTENT);
                                            intent.putExtra("Progress", (int) (total * 100 / fileLength));
                                            sendBroadcast(intent);
                                            
                                            final int finalI = i;
                                            final long finalTotal = total;
                                            final int finalI1 = i;
                                            mHandler.post(new Runnable() {
                                                @Override
												public void run() {
                                                    int i1 = (int) (finalTotal * 100 / fileLength);
                                                   
                                                    
                                                    if(i1>=100){
                                                        mBuilder.setContentText("[Complete]");
                                                        mBuilder.setOngoing(true);
                                                        mBuilder.setProgress(100,(int) (finalTotal * 100 / fileLength),false);
                                                        notificationManager.notify(0, mBuilder.build());
                                                        Toast.makeText(Test_Downloader_By_service.this, "Compeleted :"+file_downloaders.get(finalI1).get_file_title(),2000).show();
                                                        String ns = Context.NOTIFICATION_SERVICE;
                                                        NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(ns);
                                                        nMgr.cancel(0);
                                                        
                                                        Intent notificationIntent = new Intent(Test_Downloader_By_service.this, Home.class);
                                                        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 999, notificationIntent, PendingIntent.FLAG_ONE_SHOT);
                                                        mBuilder =new NotificationCompat.Builder(Test_Downloader_By_service.this)
                                                                        .setSmallIcon(R.drawable.download)
                                                                        .setContentTitle(file_downloaders.get(finalI1).get_file_title())
                                                                        .setAutoCancel(true)
                                                                        .setContentIntent(contentIntent)
                                                                        .setOngoing(true)
                                                                        .setContentText("[Complete] :: "+file_downloaders.get(finalI1).get_file_title());
                                                        mBuilder.setProgress(0,0,false);
                                                        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                                        notificationManager.notify(0, mBuilder.build());
                                                        
                                                    }else{
                                                    	
                                                    	
                                                        mBuilder.setContentText("[Downloading] :: Tab to Cancel.");
                                                        mBuilder.setOngoing(true);
                                                        mBuilder.setProgress(100,(int) (finalTotal * 100 / fileLength),false);
                                                        notificationManager.notify(0, mBuilder.build());
                                                    }

                                                  }
                                            });
                                            // publishProgress((int) (total * 100 / fileLength));
                                        }
                                        output.write(data, 0, count);
                                    }


                                    output.flush();
                                    output.close();
                                    input.close();
                                    if(!thread.finish){
                                        //Intent ixX = new Intent();
                                      //  ixX.setAction(CUSTOM_INTENT);
                                      //  ixX.putExtra("progress", 100);
                                      //  ixX.putExtra("vid", file_downloaders.get(i).get_file_vid());
                                     //   ixX.putExtra("title", file_downloaders.get(i).get_file_title());
                                      //  Test_Downloader_By_service.this.getApplicationContext().sendBroadcast(ixX);
                                        
                                        
                                        dbClass_downloader.UpdateDownloadData(file_downloaders.get(i).get_file_vid(),"Downloaded");
                                        Intent intent = new Intent(CUSTOM_INTENT);
                                        intent.putExtra("Progress", 0);
                                        sendBroadcast(intent);
                                        
                                        // try{
                                       //     MediaScannerConnection.scanFile(Test_Downloader_By_service.this, new String[]{file.getAbsolutePath()}, null, null);
                                      //  }catch (Exception e){}



                                    }else{
                                       // Intent ixX = new Intent();
                                       // ixX.setAction(CUSTOM_INTENT);
                                      //  ixX.putExtra("progress", 0);
                                      //  ixX.putExtra("vid", file_downloaders.get(i).get_file_vid());
                                      //  ixX.putExtra("title", "No Download in Queue.");
                                     //   Test_Downloader_By_service.this.getApplicationContext().sendBroadcast(ixX);
                                       /* try{
                                            MediaScannerConnection.scanFile(Test_Downloader_By_service.this, new String[]{file.getAbsolutePath()}, null, null);
                                        }catch (Exception e){
                                        	
                                        }*/
                                    }
                                } catch (IOException e) {
                                    Log.e("ERROR", "ERROR", e);
                                }

                                //Bundle resultData = new Bundle();
                                //resultData.putInt("progress", 100);
                                // resultData.putString("vid", file_downloaders.get(i).get_file_vid());
                                // resultData.putString("title", file_downloaders.get(i).get_file_title());
                                //  receiver.send(UPDATE_PROGRESS, resultData);

                                //return new String[]{retVidUrl, params[1], type, Quality, file_size + ""};
                                // publishProgress(100);
                            }


                        }


                    }else{
                        count_thred++;
                    }


                } catch (Exception e) {
                    Log.e("ERROR", "ERROR", e);
                }


                try {
                    if(count_thred>=5){
                       // Log.e("Ytube Free", "Stop Service");
                       // onDestroy();
                    }
                    Log.e("Ytube", "Service");
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                if (finish) {
                    return;
                }
            }
        }
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
//    public int getRandomNumber() {
//        return mGenerator.nextInt(100);
//    }
    private Handler mHandler = new Handler();
    NotificationCompat.Builder mBuilder;
    private NotificationManager notificationManager;
    public void showNotification(String title,String vid,String path_vid,int Progress) {
        Log.e("E_Path",vid+"\n"+path_vid);
        Intent notificationIntent = new Intent(this, Cancel_Downlaoding.class);
        notificationIntent.putExtra("vid",vid);
        notificationIntent.putExtra("path_vid",path_vid);
        PendingIntent contentIntent = PendingIntent.getBroadcast(this, 0, notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder =new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.download)
                        .setContentTitle(title)
                        .setAutoCancel(true)
                        .setContentIntent(contentIntent)
                        .setOngoing(true)
                        .setContentText("[Starting] :: Tab to Cancel.");
       mBuilder.setProgress(0,0,false);
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, mBuilder.build());
    }
    
    
    
    

    NotificationCompat.Builder mBuilder_webview;
    private NotificationManager notificationManager_webview;
    public void showNotification_webView(String title, String vid, String error_type) {
        Log.e("E_Path",vid+"\n"+vid);
        Intent notificationIntent = new Intent(this, WebView_Boardcast.class);
        notificationIntent.putExtra("vid",vid);
        notificationIntent.putExtra("title",title);
        PendingIntent contentIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder_webview =new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.action_search)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setContentIntent(contentIntent)
                .setOngoing(true)
                .setContentText(error_type);
        mBuilder_webview.setProgress(0,0,false);
        notificationManager_webview = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
      notificationManager_webview.notify(0, mBuilder_webview.build());
    }







    public void generateNoteOnSD(String sFileName, String sBody){
        try
        {
            File root = new File(Environment.getExternalStorageDirectory(), "YTube Downloader");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
///            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        }
        catch(IOException e)
        {
            e.printStackTrace();

        }
    }
    
    
    
    
    
    
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

}
