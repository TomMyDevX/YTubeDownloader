package com.tools.tommydev.videofinder;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.tools.tommydev.videofinder.DataBaseHelper.DBClass_Downloader;
import com.tools.tommydev.videofinder.Object.Meta;
import com.tools.tommydev.videofinder.Object.Video;
import com.tools.tommydev.videofinder.UIHelper.AlertUIHelper;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
 * Created by TomMy on 10/17/13.
 */



public class Webview_Detecter extends Activity {
    WebView webview;
    ProgressDialog  myProgress;

	
	
	@Override
	protected void onStop() {
		 //Intent intent_service=new Intent(this,Test_Downloader_By_service.class);
	    // stopService(intent_service);
		super.onStop();
	}
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.webview_detecter);
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        webview = new WebView(this);
        setContentView(webview);
        //webview.setVisibility(View.VISIBLE);



        webview.getSettings().setUserAgentString("Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1667.0 Safari/537.36");
        webview.getSettings().setJavaScriptEnabled(true);
        webview.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
        
        
        final Activity activity = this;
        webview.setWebChromeClient(new WebChromeClient() {
            @Override
			public void onProgressChanged(WebView view, int progress) {
                activity.setProgress(progress * 1000);
            }
        });
        webview.setWebViewClient(new WebViewClient() {
            @Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity, "Oh no! " + description, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
            	
           
            	
            	
            	
            	//if(login){
            		 webview.loadUrl("javascript:window.HTMLOUT.showHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
            	//}else{
            		
            	//}
            	
            	
            	
                //webview.setVisibility(View.GONE);
            //	webview.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
               
            }
            
          
            	    
        });
		
			if(getIntent().getStringExtra("vid").equals("null")){
				 webview.loadUrl("http://www.youtube.com");
			}else{
				 webview.loadUrl("http://www.youtube.com/watch?v="+getIntent().getStringExtra("vid"));
			}
		
       

    }



   public class MyJavaScriptInterface {

     


      
    	
		@JavascriptInterface
        public void showHTML(String html) {

            try {
                ArrayList<Video> videos = getStreamingUrisFromYouTubePage(html.replace("\\u0026", "&"));

                if (videos != null && !videos.isEmpty()) {

                    myProgress = new ProgressDialog(Webview_Detecter.this);
                    myProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    myProgress.setMessage(getIntent().getStringExtra("title"));
                    myProgress.setTitle("Downloading...");
                    myProgress.setCancelable(false);
                    myProgress.setProgressDrawable(Webview_Detecter.this.getResources().getDrawable(R.drawable.progressbar));
        /* Show the progress dialog. */
                    myProgress.show();


                   // setTheme(R.style.Transparent);
                  //  webview.setVisibility(View.GONE);
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
                    Log.e("FILE_X","[" + type + "][" + Quality + "]>>>" + retVidUrl);
                    Log.e("Progress","FILE_X");
                    final String finalType = type;
                    
                    
                    
                  
                    
                    
                    
                   // new DownloadTask_for_WebviewDetecter(getApplicationContext(), getIntent().getStringExtra("title"), retVidUrl, finalType);
                     
                    new AsyncTask<String,String,Boolean>(){

                    	@JavascriptInterface
                        @Override
                        protected void onPreExecute() {
                        	Log.e("Progress","onPreExecute");
                            //webview.setVisibility(View.GONE);
                        }
                    	@JavascriptInterface
                        @Override
                        protected void onProgressUpdate(String... progress) {
                    		// Log.e("Progress","onProgressUpdate");
                            myProgress.setProgress(Integer.parseInt(progress[0]));
                            
                            
                           
                           
                        }
                    	@JavascriptInterface
                        @Override
                        protected void onPostExecute(Boolean s) {
                        	  Log.e("Progress","onPostExecute");
                            if(s){
                                webview.setVisibility(View.GONE);
                                DBClass_Downloader dbClass_downloader=new DBClass_Downloader(Webview_Detecter.this);
                                dbClass_downloader.UpdateDownloadData(getIntent().getStringExtra("vid"),"Downloaded");
                                String ns = Context.NOTIFICATION_SERVICE;
                                NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(ns);
                                nMgr.cancel(0);
                                
                                
                                NotificationCompat.Builder mBuilder;
                                NotificationManager notificationManager;
                                   Intent notificationIntent = new Intent(Webview_Detecter.this, Home.class);
                                   PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 999, notificationIntent, PendingIntent.FLAG_ONE_SHOT);
                                   mBuilder =new NotificationCompat.Builder(getApplicationContext())
                                                   .setSmallIcon(R.drawable.download)
                                                   .setContentTitle(getIntent().getStringExtra("title"))
                                                   .setAutoCancel(true)
                                                   .setContentIntent(contentIntent)
                                                   .setOngoing(true)
                                                   .setContentText("[Complete] :: "+getIntent().getStringExtra("title"));
                                   mBuilder.setProgress(0,0,false);
                                   notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                   notificationManager.notify(0, mBuilder.build());
                                
                                
                                
                                myProgress.dismiss();
                                new AlertUIHelper(Webview_Detecter.this).show("Downloaded : "+getIntent().getStringExtra("title"), Gravity.BOTTOM,200);
                                finish();
                                Intent intent_service=new Intent(Webview_Detecter.this,Test_Downloader_By_service.class);
                                startService(intent_service);
                            }else{
                               // finish();
                               // Intent intent=getIntent();
                                //startActivity(intent);

                                   
                                   
                                webview.setVisibility(View.VISIBLE);
                                myProgress.dismiss();
                                new AlertUIHelper(Webview_Detecter.this).show("Error : "+getIntent().getStringExtra("title"), Gravity.BOTTOM,200);
                            }


                        }
                    	@JavascriptInterface
                        @Override
                        protected Boolean doInBackground(String... aurl) {
                           
                            Log.e("Progress","doInBackground");
                            try {
                                URL url = new URL(aurl[0]);
                                URLConnection conexion = url.openConnection();
                                conexion.setConnectTimeout(60000);
                                conexion.connect();

                                int lenghtOfFile = conexion.getContentLength();

                                myProgress.setMax(lenghtOfFile);

                                // Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);

                                //create a new file, specifying the path, and the filename
                                //which we want to save the file as.
                                SharedPreferences settings = getApplicationContext().getSharedPreferences("Youtube_downloader_tommy", 0);

                                String location=settings.getString("disk_save_location", Environment.getExternalStorageDirectory().toString());



                             //   settings = Webview_Detecter.this.getApplicationContext().getSharedPreferences("Youtube_downloader_tommy", 0);
//                                String urlToDownload = aurl[0];
                               String filename = "";
//                                if(settings.getBoolean("checkBox_save_video_with_id", false)){
//                                    filename=getIntent().getStringExtra("vid");
//                                }else{
//                                    filename=getIntent().getStringExtra("title").replace("\"", "").replace("<", "").replace(">", "").replace("|", "").replace("\\", "").replace("/", "").replace("?", "").replace(":", "").replace("*", "");
//                                }





                                File SDCardRoot = new File(location + "/download/");
                                SDCardRoot.mkdirs();
                                File file = new File(SDCardRoot, getIntent().getStringExtra("title") + finalType);

                                boolean filenameValid = isValidFileName(file.getAbsolutePath());
                                if(filenameValid){
                                    filename=getIntent().getStringExtra("title").replace("\"", "").replace("<", "").replace(">", "").replace("|", "").replace("\\", "").replace("/", "").replace("?", "").replace(":", "").replace("*", "");
                                }else{
                                    filename=getIntent().getStringExtra("vid");
                                }
                                file = new File(SDCardRoot, filename + finalType);
                                DBClass_Downloader dbClass_downloader=new DBClass_Downloader(Webview_Detecter.this);
                                dbClass_downloader.UpdateData(getIntent().getStringExtra("vid"), finalType, file.getAbsolutePath(), lenghtOfFile + "");











                                //File file = new File(SDCardRoot, "TestDownlaod" + finalType);

                                InputStream input = new BufferedInputStream(url.openStream());
                                OutputStream output = new FileOutputStream(file);

                                byte data[] = new byte[1024];

                                long total = 0;
                                int count;
                                while ((count = input.read(data)) != -1) {
                                    total += count;
                                    publishProgress("" + (int) ((total)));
                                    output.write(data, 0, count);
                                }
                                output.flush();
                                output.close();
                                input.close();

                                return true;
                            } catch (Exception e) {
                                webview.setVisibility(View.VISIBLE);
                                webview.loadUrl("http://www.youtube.com/watch?v=" + getIntent().getStringExtra("vid"));
                                return false;
                            }
                        }

                    }.execute(retVidUrl);
                }else{
                 
               	 // webview.loadUrl("http://www.youtube.com/watch?v="+getIntent().getStringExtra("vid"));
                	
                	
                  //  webview.loadUrl("http://www.youtube.com/watch?v="+getIntent().getStringExtra("vid"));
                    //webview.setVisibility(View.VISIBLE);
                    //setTheme(R.style.noAnimTheme);


                }






//                for(int i=0;i<streamingUrisFromYouTubePage.size();i++){
//                    Log.e("streamingUrisFromYouTubePage",streamingUrisFromYouTubePage.get(i).ext+":"+streamingUrisFromYouTubePage.get(i).type);
//                }

            } catch (IOException e) {
                webview.loadUrl("http://www.youtube.com/watch?v=" + getIntent().getStringExtra("vid"));
            }
        }

    }


   @JavascriptInterface
    public ArrayList<Video> getStreamingUrisFromYouTubePage(String texthtml)throws IOException {
       

        // Remove any query params in query string after the watch?v=<vid> in
        // e.g.
        // http://www.youtube.com/watch?v=0RUPACpf8Vs&feature=youtube_gdata_player
      

        // Get the HTML response

        String html = texthtml.toString();

        // Parse the HTML response and extract the streaming URIs
        if (html.contains("verify-age-thumb")) {
            Log.e("test", "YouTube is asking for age verification. We can't handle that sorry.");
            return null;
        }

        if (html.contains("das_captcha")) {
            Log.e("test", "Captcha found, please try with different IP address.");
            return null;
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
//            finish();
//            Intent intent=getIntent();
//            startActivity(intent);
            Log.e("test", "Found zero or too many stream maps.");
            //webview.loadUrl("http://www.youtube.com/watch?v="+getIntent().getStringExtra("vid"));
            return null;
        }else{
        	
        	 
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
            webview.loadUrl("http://www.youtube.com/watch?v=" + getIntent().getStringExtra("vid"));
            Log.e("test", "Couldn't find any URLs and corresponding signatures");
            return null;
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
                Video newVideo = new Video(meta.ext, meta.type, foundArray.get(format), meta.type_id,"");
                videos.add(newVideo);
                // Log.e("test", "YouTube Video streaming details: ext:" + newVideo.ext
                //         + ", type:" + newVideo.type + ", url:" + newVideo.url);
            }
        }

        return videos;
    }
   @JavascriptInterface
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
}