package com.tools.tommydev.videofinder;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.util.Log;

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

public class DownloadService extends IntentService {
    public static final int UPDATE_PROGRESS = 8344;

    public DownloadService() {
        super("DownloadService");
    }

    @SuppressLint("DefaultLocale")
	@Override
    protected void onHandleIntent(final Intent intent) {


        new Thread(new Runnable() {
            @Override
			public void run() {
                // TODO Auto-generated method stub
                while (true) {


                    try {
                        DBClass_Downloader dbClass_downloader = new DBClass_Downloader(DownloadService.this);
                        boolean dbClass_downloader_have_size = dbClass_downloader.SelectAllData("file_status='In_Queue'").size() > 0;
                        if (dbClass_downloader_have_size) {

                            List<DBClass_Downloader.File_Downloader> file_downloaders = dbClass_downloader.SelectAllData("file_status='In_Queue'");
                            int downloader_size = file_downloaders.size();

                            for (int i = 0; i < downloader_size; i++) {
                                int progressX=0;
                                ArrayList<Video> videos = null;
                                while (videos == null) {
                                    videos = getStreamingUrisFromYouTubePage("https://www.youtube.com/watch?v=" + file_downloaders.get(i).get_file_vid());
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

                                    Log.e("FILE_X", "[" + file_downloaders.get(i).get_file_title() + "]" + "[" + type + "][" + Quality + "]>>>" + retVidUrl);


                                    String urlToDownload = retVidUrl;
                                    String filename = file_downloaders.get(i).get_file_vid();
                                    String filetype = type;
                                    ResultReceiver receiver = (ResultReceiver) intent.getParcelableExtra("receiver");
                                    try {
                                        URL url = new URL(urlToDownload);
                                        URLConnection connection = url.openConnection();
                                        connection.setConnectTimeout(60000);
                                        connection.connect();

                                        // this will be useful so that you can show a typical 0-100% progress bar
                                        int fileLength = connection.getContentLength();

                                        // download the file


                                        File SDCardRoot = new File(Environment.getExternalStorageDirectory() + "/download/");
                                        SDCardRoot.mkdirs();
                                        File file = new File(SDCardRoot, filename + filetype);

                                        dbClass_downloader.UpdateData(file_downloaders.get(i).get_file_vid(), type, file.getAbsolutePath(), fileLength + "");

                                        InputStream input = new BufferedInputStream(url.openStream());
                                        OutputStream output = new FileOutputStream(file);

                                        byte data[] = new byte[1024];
                                        long total = 0;
                                        int count;
                                        while ((count = input.read(data)) != -1) {
                                            total += count;


                                            // publishing the progress....

                                            if(progressX!=(int) (total * 100 / fileLength)){
                                                progressX=(int) (total * 100 / fileLength);
                                                Bundle resultData = new Bundle();
                                                resultData.putString("title", file_downloaders.get(i).get_file_title());
                                                resultData.putInt("progress", (int) (total * 100 / fileLength));
                                                //Log.e("Writedata",(int) (total * 100 / fileLength)+"");
                                                receiver.send(UPDATE_PROGRESS, resultData);
                                            }
                                            output.write(data, 0, count);
                                        }

                                        output.flush();
                                        output.close();
                                        input.close();
                                    } catch (IOException e) {
                                        Log.e("ERROR", "ERROR", e);
                                    }

                                    Bundle resultData = new Bundle();
                                    resultData.putInt("progress", 100);
                                    resultData.putString("vid", file_downloaders.get(i).get_file_vid());
                                    resultData.putString("title", file_downloaders.get(i).get_file_title());
                                    receiver.send(UPDATE_PROGRESS, resultData);

                                    //return new String[]{retVidUrl, params[1], type, Quality, file_size + ""};
                                }


                            }


                        }

                    } catch (Exception e) {
                        Log.e("ERROR", "ERROR", e);
                    }


                    try {

                        Thread.sleep(3000);
                        Log.e("LOOPER", "LOOPER");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //REST OF CODE HERE//
                }

            }
        }).start();


    }


//
//    public class YouTubePageStreamUriGetter extends
//            AsyncTask<String, String, String[]> {
//        int position;
//        TextView readytoplay;
//        LinearLayout save_layout;
//        String[] Gobalparam;
//        String filepath_to_sql;
//        String filename_to_sql;
//        String id;
//
//        public YouTubePageStreamUriGetter(int position, TextView readytoplay, LinearLayout save_layout, String id) {
//
//            this.position = position;
//            this.readytoplay = readytoplay;
//            this.save_layout = save_layout;
//            this.id = id;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//
//            // mProgressDialog.setMessage("Downloading\n"+mp3Lists.get(position).getTitle());
//            progressDialog2.setCancelable(false);
//            progressDialog2.setIndeterminate(false);
//            progressDialog2.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            progressDialog2.setMessage("Checking...");
//            progressDialog2.show();
//
//        }
//
//        @Override
//        protected String[] doInBackground(String... params) {
//            String url = params[0];
//            Gobalparam = params;
//            try {
//
//
//            } catch (Exception e) {
//                Log.e("test", "Couldn't get YouTube streaming URL", e);
//                return new String[]{"null", "null", "null", "null", "null"};
//            }
//            Log.e("test", "Couldn't get stream URI for " + url);
//            return new String[]{"null", "null", "null", "null", "null"};
//            //  return null;
//        }
//
//        @Override
//        protected void onPostExecute(String streamingUrl[]) {
//            super.onPostExecute(streamingUrl);
//            if (!streamingUrl[0].equals("null")) {
//                if (streamingUrl != null) {
//                    Log.e("test", streamingUrl[3] + ":[" + streamingUrl[2] + "]:" + streamingUrl[1] + ":" + streamingUrl[0]);
//                    String filename = streamingUrl[1].replace("\"", "").replace("<", "").replace(">", "").replace("|", "").replace("\\", "").replace("/", "").replace("?", "").replace(":", "").replace("*", "") + streamingUrl[2];
//                    filepath_to_sql = Environment.getExternalStorageDirectory() + "/download/" + filename;
//                    filename_to_sql = filename;
//
//
//                    DBClass dbClass = new DBClass(activity);
//                    dbClass.InsertData(filepath_to_sql, filename_to_sql, "start_download", streamingUrl[4]);
//                    SharedPreferences settings = activity.getSharedPreferences("Youtube_downloader_tommy", 0);
//
//                    if (settings.getBoolean("DownloadManager", false)) {
//                        dm = (DownloadManager) activity.getSystemService(activity.getApplicationContext().DOWNLOAD_SERVICE);
//                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(streamingUrl[0]));
//                        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
//                                .setAllowedOverRoaming(false)
//                                .setTitle(filename)
//                                .setDescription("")
//                                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);
//
//                        enqueue = dm.enqueue(request);
//
//                        progressDialog2.dismiss();
//                        progressDialog.setMessage("Downloading...\n" + filename);
//                        progressDialog.show();
//                    } else {
//                        progressDialog2.dismiss();
//                        // updateData(streamingUrl[0], streamingUrl[1],streamingUrl[2]);
//                        new DownloadFileAsync(streamingUrl[0], streamingUrl[1], streamingUrl[2], id).execute(new String[]{streamingUrl[0],});
//                    }
//                    //  progressDialog.setCancelable(false);
//                    //  progressDialog.setIndeterminate(false);
//                    //  progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//
//
//                }
//            } else {
//                if (count_retry >= 5) {
//                    progressDialog2.dismiss();
//                    new AlertUIHelper(activity).show("Can't get video.", Gravity.CENTER);
//                    count_retry = 0;
//                } else {
//                    ++count_retry;
//                    new YouTubePageStreamUriGetter(position, readytoplay, save_layout, id).execute(Gobalparam);
//                }
//
//
//            }
//
//        }
//
//
//
//
//    }
//
//
//
@Override
public int onStartCommand(Intent intent, int flags, int startId) {
    super.onStartCommand(intent, flags, startId);
    return START_STICKY;
}

    public class LocalBinder extends Binder {
        DownloadService getService() {
            // Return this instance of LocalService so clients can call public methods
            return DownloadService.this;
        }
    }

    private final IBinder mBinder = new LocalBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
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
        String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:8.0.1)";
        HttpClient client = new DefaultHttpClient();
        client.getParams().setParameter(CoreProtocolPNames.USER_AGENT,
                userAgent);
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
            Log.e("test", "Found zero or too many stream maps.");
            return null;
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


}