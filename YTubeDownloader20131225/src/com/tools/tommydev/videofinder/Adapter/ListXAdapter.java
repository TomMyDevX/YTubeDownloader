package com.tools.tommydev.videofinder.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tools.tommydev.videofinder.DataBaseHelper.DBClass_Downloader;
import com.tools.tommydev.videofinder.DataBaseHelper.DBClass_favor;
import com.tools.tommydev.videofinder.Object.Mp3List;
import com.tools.tommydev.videofinder.DownloadTask;
import com.tools.tommydev.videofinder.R;
import com.tools.tommydev.videofinder.Test_Downloader_By_service;
import com.tools.tommydev.videofinder.UIHelper.AlertUIHelper;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Created by TomMy on 8/5/13.
 */
public class ListXAdapter extends BaseAdapter {
    positionList positionList;
    positionArrList positionListDownlaod;
    int showpos = -1;
    long downloadId;
    ProgressDialog progressDialog2;
    int count_retry = 0;
    private long enqueue = 1;
    private DownloadManager dm;
    private Activity activity;
    private ArrayList<Mp3List> data;
    private LayoutInflater inflater = null;
    private ProgressDialog mProgressDialog;
    private Handler handler = new Handler();
    private ProgressDialog progressDialog;


    public void ReStartAdapter(){
        notifyDataSetChanged();
    }

    public ListXAdapter(Activity a, ArrayList<Mp3List> d) {
        progressDialog = new ProgressDialog(a);
        progressDialog2 = new ProgressDialog(a);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
       

        activity = a;
        data = d;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        positionList = new positionList();
        positionListDownlaod = new positionArrList();


    }

    @Override
	public int getCount() {
        return data.size();
    }

    @Override
	public Object getItem(int position) {
        return position;
    }

    @Override
	public long getItemId(int position) {
        return position;
    }

    @Override
	public View getView(int position, View convertView, ViewGroup parent) {
        //  View vi = convertView;
        ViewHolder holder = null;// = new ViewHolder();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_row, parent, false);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.title); // title
            holder.artist = (TextView) convertView.findViewById(R.id.artist); // artist name
            holder.duration = (TextView) convertView.findViewById(R.id.duration); // duration
            holder.ready2play = (TextView) convertView.findViewById(R.id.textView_ready2play); // ready2play
            holder.thumb_image = (ImageView) convertView.findViewById(R.id.list_image); // thumb image
            holder.setting_item = (ImageView) convertView.findViewById(R.id.setting_item);
            //holder.dataLL = (LinearLayout) convertView.findViewById(R.id.layout_item_setting);
            holder.dataLL_save = (LinearLayout) convertView.findViewById(R.id.linearLayout_save);
            holder.ImageView_item_cancel = (ImageView) convertView.findViewById(R.id.imageView_item_cancel);
            holder.ImageView_item_preview = (ImageView) convertView.findViewById(R.id.imageView_item_preview);
            holder.ImageView_item_download = (ImageView) convertView.findViewById(R.id.imageView_item_download);
            holder.ImageView_item_favor = (ImageView) convertView.findViewById(R.id.imageView_favor);
            holder.item_favor = (TextView) convertView.findViewById(R.id.textView_favor); // title
            holder.textView_item_download = (TextView) convertView.findViewById(R.id.textView_item_download); // title
            
            
            //  convertView.setTag(holder);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        SharedPreferences   settings = activity.getApplicationContext().getSharedPreferences("Youtube_downloader_tommy", 0);
        boolean is_downloadwithservice=settings.getBoolean("checkBox_download_with_service",false);
        if(is_downloadwithservice){
        	holder.textView_item_download.setText("Queue");
        }else{
        	holder.textView_item_download.setText("Download");
        }

        int hours = data.get(position).getDuration() / 3600;
        int minutes = (data.get(position).getDuration() % 3600) / 60;
        int seconds = data.get(position).getDuration() % 60;
        String timeString = hours + ":" + minutes + ":" + seconds;
        holder.title.setText(data.get(position).getTitle());
        
        
        String dateStr = hours+" "+minutes+" "+seconds; 

        SimpleDateFormat curFormater = new SimpleDateFormat("H M s"); 
        Date dateObj=null;
		try {
			dateObj = curFormater.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        SimpleDateFormat postFormater = new SimpleDateFormat("HH:MM:ss"); 

        String newDateStr = postFormater.format(dateObj); 
        
        
        holder.duration.setText(newDateStr);
        
        
      // SharedPreferences settings = activity.getSharedPreferences("Youtube_downloader_tommy", 0);
        boolean show_thumb = settings.getBoolean("show_thumb", true);

        // Log.e("Images",data.get(position).getThumbnail());
        final int finalx = position;


        ImageLoader.getInstance().displayImage(data.get(position).getThumbnail(), holder.thumb_image);


        //   holder.thumb_image.setImageURI(Uri.parse(Environment.getExternalStorageDirectory() + "/Android/data/"+activity.getClass().getPackage().getName()+"/images/"+data.get(position).getId()));
//
        //  imageLoader.DisplayImage(data.get(position).getSmall_thumbnail(), holder.thumb_image);

        final ViewHolder finalHolder = holder;
        holder.setting_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showpos = finalx;
                positionList.addPostion(finalx);

                DBClass_favor dbClass_favor = new DBClass_favor(activity);
                String[] strings = dbClass_favor.SelectData(data.get(finalx).getId());
                if (strings != null) {
                    finalHolder.ImageView_item_favor.setImageResource(R.drawable.rating_important);
                } else {
                    finalHolder.ImageView_item_favor.setImageResource(R.drawable.rating_not_important);
                }

            }
        });
//
        final ViewHolder finalHolder1 = holder;
        holder.ImageView_item_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                positionList.removePosition(finalx);
                Animation animation;
                animation = AnimationUtils.loadAnimation(activity, R.anim.left_to_right);


            }
        });
////
        holder.ImageView_item_preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DBClass_Downloader dbClass_downloader=new DBClass_Downloader(activity);
                String[] strings = dbClass_downloader.SelectData(data.get(finalx).getId());
                if(strings!=null){
                    File f = new File(strings[3]);
                    if (f.exists()) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(strings[3]));
                        intent.setDataAndType(Uri.parse(strings[3]), "video/*");
                        activity.startActivity(Intent.createChooser(intent, "Choose Action"));
                     
                    } else {
                    	activity.startActivity(Intent.createChooser(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + data.get(finalx).getId())), "Choose Action"));
                   
                    }
                }else{ 
                	activity.startActivity(Intent.createChooser(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + data.get(finalx).getId())), "Choose Action"));
                   
                }

            }
        });
////
////
        final ViewHolder finalHolder2 = holder;
        holder.ImageView_item_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                positionList.removePosition(finalx);
                positionListDownlaod.addPostion(finalx, "start_download");





                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
                Date date = new Date();
                final String datetime=sdf.format(date);


              //  erwe

                String ChkFile=  data.get(finalx).getTitle().replace("\"", "").replace("<", "").replace(">", "").replace("|", "").replace("\\", "").replace("/", "").replace("?", "").replace(":", "").replace("*", "") + ".mp4";

                SharedPreferences   settings = activity.getApplicationContext().getSharedPreferences("Youtube_downloader_tommy", 0);
                String location=settings.getString("disk_save_location", Environment.getExternalStorageDirectory().toString());

                String path = location+"/download/"+ChkFile;
                final File file_Chk=new File(path);
                if(file_Chk.exists()){
                            new AlertDialog.Builder(activity)
                            .setMessage("Do you want to add  old video?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
								public void onClick(DialogInterface dialog, int id) {
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
                                    Date date = new Date();
                                    String datetime=sdf.format(date);
                                    DBClass_Downloader dbClass_downloader=new DBClass_Downloader(activity);
                                    dbClass_downloader.InsertData(data.get(finalx).getId(), data.get(finalx).getTitle(), ".mp4",file_Chk.getAbsolutePath(), file_Chk.length()+"",datetime, "Downloaded");
                                    new AlertUIHelper(activity).show("Video ready on search.",Gravity.CENTER,100);
                                    notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                }else{




                     String pathX =location +"/download/"+data.get(finalx).getId()+".mp4";
                     final File file_ChkX=new File(pathX);
                    if(file_ChkX.exists()){
                        new AlertDialog.Builder(activity)
                                .setMessage("Do you want to add  old video?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
									public void onClick(DialogInterface dialog, int id) {
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
                                        Date date = new Date();
                                        String datetime=sdf.format(date);
                                        DBClass_Downloader dbClass_downloader=new DBClass_Downloader(activity);
                                        dbClass_downloader.InsertData(data.get(finalx).getId(), data.get(finalx).getTitle(), ".mp4",file_ChkX.getAbsolutePath(), file_ChkX.length()+"",datetime, "Downloaded");
                                        new AlertUIHelper(activity).show("Video ready on search.",Gravity.CENTER,100);
                                        notifyDataSetChanged();
                                    }
                                })
                                .setNegativeButton("No", null)
                                .show();


                    }else{
                        DBClass_Downloader dbClass_downloader=new DBClass_Downloader(activity);
                        long in_queue = dbClass_downloader.InsertData(data.get(finalx).getId(), data.get(finalx).getTitle(), "", "", "",datetime, "In_Queue");
                       
                        boolean is_downloadwithservice=settings.getBoolean("checkBox_download_with_service",false);
                        if(is_downloadwithservice){
                        	 if(in_queue<=0){
                             new AlertUIHelper(activity).show("Video in Queue.",Gravity.CENTER,100);
                             }else{
                              new AlertUIHelper(activity).show("Add Video to Queue.",Gravity.CENTER,100);
                              Intent intent=new Intent(activity, Test_Downloader_By_service.class);
                               activity.startService(intent);
                             }
                        }else{
                        	  DownloadTask downloadTask=new DownloadTask(activity,ListXAdapter.this, data.get(finalx).getId(),data.get(finalx).getTitle());
                              downloadTask.execute();
                        }
                        
                       
                    }


                }




            }
        });

        final ViewHolder finalHolder3 = holder;
        holder.ImageView_item_favor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBClass_favor dbClass_favor = new DBClass_favor(activity);
                String[] strings = dbClass_favor.SelectData(data.get(finalx).getId());

                if (strings != null) {
                    dbClass_favor.DeleteData(data.get(finalx).getId());
                    finalHolder3.ImageView_item_favor.setImageResource(R.drawable.rating_not_important);
                    positionList.removePosition(finalx);
                } else {

                    DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                    Date date = new Date();
                    dbClass_favor.InsertData(data.get(finalx).getId(), data.get(finalx).getTitle().replace("\"", "").replace("<", "").replace(">", "").replace("|", "").replace("\\", "").replace("/", "").replace("?", "").replace(":", "").replace("*", "") + ".mp4", data.get(finalx).getId(), data.get(finalx).getId(), data.get(finalx).getId(), dateFormat.format(date), data.get(finalx).getThumbnail());
                    finalHolder3.ImageView_item_favor.setImageResource(R.drawable.rating_important);
                    positionList.removePosition(finalx);
                }

                Animation animation;
                notifyDataSetChanged();

            }
        });
        DBClass_favor dbClass_favor = new DBClass_favor(activity);
        String[] strings = dbClass_favor.SelectData(data.get(finalx).getId());
        if (strings != null) {
            finalHolder3.item_favor.setText("Favorited");
            finalHolder3.ImageView_item_favor.setImageResource(R.drawable.rating_important);
        } else {
            finalHolder3.item_favor.setText("");
            finalHolder3.ImageView_item_favor.setImageResource(R.drawable.rating_not_important);
        }


     
        if (positionListDownlaod.searchPosition(position)) {
            holder.ready2play.setVisibility(View.VISIBLE);
        } else {
            holder.ready2play.setVisibility(View.GONE);
        }


        //DBClass dbClass = new DBClass(activity);
        //boolean s = dbClass.SelectDataWithName(data.get(position).getTitle().replace("\"", "").replace("<", "").replace(">", "").replace("|", "").replace("\\", "").replace("/", "").replace("?", "").replace(":", "").replace("*", "") + ".mp4");
       // String dataarr[] = dbClass.SelectDataWithNameArray(data.get(position).getTitle().replace("\"", "").replace("<", "").replace(">", "").replace("|", "").replace("\\", "").replace("/", "").replace("?", "").replace(":", "").replace("*", "") + ".mp4");
        //    Log.e("R2P",s+"");

        DBClass_Downloader dbClass_downloader=new DBClass_Downloader(activity);
        String[] s = dbClass_downloader.SelectData(data.get(position).getId());
        if (s!=null) {
            if(s[6].equals("Downloaded")){
                holder.ready2play.setVisibility(View.VISIBLE);
                holder.ready2play.setText("Ready to play.");
                holder.dataLL_save.setVisibility(View.GONE);
            }else{
                holder.ready2play.setVisibility(View.GONE);
                holder.ready2play.setText("");
                holder.dataLL_save.setVisibility(View.VISIBLE);
            }
        } else {
            holder.ready2play.setVisibility(View.GONE);
            holder.ready2play.setText("");
            holder.dataLL_save.setVisibility(View.VISIBLE);

        }


//       // Log.e("data_sql",">"+s+"|"+position);
//        if(s){
//          //  Log.e("R2P","onCheckMySql");
//
//            if(dataarr[3].equals("downloaded")){
//
//            }else{
//                holder.ready2play.setText("Downloading...");
//            }
//            holder.dataLL_save.setVisibility(View.GONE);
//        } else {
//            holder.ready2play.setText("");
//            holder.dataLL_save.setVisibility(View.VISIBLE);
//        }


        return convertView;
    }

    public boolean isPackageExists(String targetPackage) {
        List<ApplicationInfo> packages;
        PackageManager pm;
        pm = activity.getPackageManager();
        packages = pm.getInstalledApplications(0);
        for (ApplicationInfo packageInfo : packages) {
            if (packageInfo.packageName.equals(targetPackage)) return true;
        }
        return false;
    }

    public boolean isActivityAvailable(Context context, String packageName, String className) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent();
        intent.setClassName(packageName, className);

        List list = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);

        return list.size() > 0;
    }

    public boolean isIntentAvailable(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List resolveInfo =
                packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (resolveInfo.size() > 0) {
            return true;
        }
        return false;
    }

    static class ViewHolder {
        TextView textView_item_download;
		TextView title;
        TextView artist;
        TextView duration;
        TextView ready2play;
        ImageView thumb_image;
        ImageView setting_item;
      //  LinearLayout dataLL;
        LinearLayout dataLL_save;
        ImageView ImageView_item_cancel;
        ImageView ImageView_item_preview;
        ImageView ImageView_item_download;
        ImageView ImageView_item_favor;
        TextView item_favor;

    }

    public class positionArrList {

        ArrayList<HashMap<String, String>> arraylist;

        public positionArrList() {
            arraylist = new ArrayList<HashMap<String, String>>();
        }

        public void addPostion(int position, String status) {
            Log.e("addPostion", showpos + "");
            HashMap<String, String> temp = new HashMap<String, String>();
            temp.put("position", position + "");
            temp.put("status", status);
            arraylist.add(temp);
        }

        public void removePosition(int position) {
            Log.e("removePosition", showpos + "");
            //  linkedList.remove("" + position);

            for (int i = 0; i < arraylist.size(); i++) {
                if (arraylist.get(i).get("position").equals(position + "")) {
                    arraylist.remove(i);
                }
            }


        }

        public boolean searchPosition(int position) {
            boolean x = false;
            for (int i = 0; i < arraylist.size(); i++) {

                if (arraylist.get(i).get("position").equals("" + position)) {
                    x = true;
                    break;
                }
            }
            return x;
        }

    }

    public class positionList {
        LinkedList<String> linkedList;


        public positionList() {
            linkedList = new LinkedList<String>();
        }

        public void addPostion(int position) {
            linkedList.add(position + "");
        }

        public void removePosition(int position) {
            Log.e("removePosition", showpos + "");
            linkedList.remove("" + position);
        }

        public boolean searchPosition(int position) {
            boolean x = false;
            for (int i = 0; i < linkedList.size(); i++) {
                if (linkedList.get(i).equals("" + position)) {
                    x = true;
                    break;
                }
            }
            return x;
        }

    }




//    public void updateData(String url,final String name,final String type){
//
//        final String file_url = url;
//    	/* Define and configure the progress dialog */
//        final ProgressDialog myProgress = new ProgressDialog(activity);
//        myProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//        myProgress.setMessage(name);
//        myProgress.setTitle("Downloading...");
//        myProgress.setCancelable(false);
//        /* Show the progress dialog. */
//        myProgress.show();
//        new Thread(new Runnable(){
//
//            public void run(){
//             //   try {
//
//                    //create the new connection
//
////                    Log.e("test",file_url);
////
////                    URL url = new URL(file_url);
////                    URLConnection  urlConnection = url.openConnection();
////                    urlConnection.connect();
//
//                    File SDCardRoot =new File(Environment.getExternalStorageDirectory()+"/download/");
//                    SDCardRoot.mkdirs();
//                    //create a new file, specifying the path, and the filename
//                    //which we want to save the file as.
//                    File file = new File(SDCardRoot,name+type);
//
////                    //this will be used to write the downloaded data into the file we created
////                    FileOutputStream fileOutput = new FileOutputStream(file);
////
////                    //this will be used in reading the data from the internet
////                    InputStream inputStream = urlConnection.getInputStream();
////
////                    //this is the total size of the file
////                    int totalSize = urlConnection.getContentLength();
////                    myProgress.setMax(totalSize);
////                    //variable to store total downloaded bytes
////                    int downloadedSize = 0;
////
////                    //create a buffer...
////                    byte[] buffer = new byte[1024];
////                    int bufferLength = 0; //used to store a temporary size of the buffer
////                    int progress = 0;
////                    //now, read through the input buffer and write the contents to the file
////                    while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
////                        //add the data in the buffer to the file in the file output stream (the file on the sd card
////                        fileOutput.write(buffer, 0, bufferLength);
////                        //add up the size so we know how much is downloaded
////                        downloadedSize += bufferLength;
////                        //Here we update the progress
////                        progress = downloadedSize;
////                        myProgress.setProgress(progress);
////                    }   fileOutput.close();
////                    myProgress.dismiss();
////                    //catch some possible errors...
////                } catch (MalformedURLException e) {
////                    e.printStackTrace();
////                } catch (IOException e) {
////                    e.printStackTrace();
////               }
//
//                int count;
//
//                try {
//                    URL url = new URL(file_url);
//                    URLConnection conexion = url.openConnection();
//                    conexion.connect();
//
//                    int lenghtOfFile = conexion.getContentLength();
//                    myProgress.setMax(lenghtOfFile);
//
//                    Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);
//
//                    InputStream input = new BufferedInputStream(url.openStream());
//                    OutputStream output = new FileOutputStream(file);
//
//                    byte data[] = new byte[1024];
//
//                    long total = 0;
//
//                    while ((count = input.read(data)) != -1) {
//                        total += count;
//                        myProgress.setProgress((int)(total));
//
//                       // publishProgress(""+(int)((total*100)/lenghtOfFile));
//                        output.write(data, 0, count);
//                    }
//
//                    output.flush();
//                    output.close();
//                    input.close();
//                } catch (Exception e) {}
//
//
//
//
//            }
//        }).start();
//
//    }

    class DownloadFileAsync extends AsyncTask<String, String, Boolean> {
        String name = "";
        String type = "";
        ProgressDialog myProgress;
        String id;

        DownloadFileAsync(String url, String name, String type, String id) {
            this.name = name;
            this.type = type;
            this.id = id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            myProgress = new ProgressDialog(activity);
            myProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            myProgress.setMessage(name);
            myProgress.setTitle("Downloading...");
            myProgress.setCancelable(false);
            LayoutInflater layoutInflater = myProgress.getLayoutInflater();
            myProgress.setProgressDrawable(activity.getResources().getDrawable(R.drawable.progressbar));
        /* Show the progress dialog. */
            myProgress.show();
        }

        @Override
        protected Boolean doInBackground(String... aurl) {
            int count;

            try {
                URL url = new URL(aurl[0]);
                URLConnection conexion = url.openConnection();
                conexion.setConnectTimeout(60000);
                conexion.connect();

                int lenghtOfFile = conexion.getContentLength();

                myProgress.setMax(lenghtOfFile);

                // Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);
                File SDCardRoot = new File(Environment.getExternalStorageDirectory() + "/download/");
                SDCardRoot.mkdirs();
                //create a new file, specifying the path, and the filename
                //which we want to save the file as.
                File file = new File(SDCardRoot, name.replace("\"", "").replace("<", "").replace(">", "").replace("|", "").replace("\\", "").replace("/", "").replace("?", "").replace(":", "").replace("*", "") + type);

                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream(file);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress("" + (int) ((total)));
                    output.write(data, 0, count);
                }




                output.flush();
                output.close();
                input.close();

//                MusicMetadataSet src_set = null;
//                try {src_set = new MyID3().read(file);} catch (IOException e1) {}
//                if (src_set == null) // perhaps no metadata
//                {
//                    Log.i("NULL", "NULL");
//                } else
//                {
//                    try{
//                        IMusicMetadata metadata = src_set.getSimplified();
//                        String artist = metadata.getArtist();
//                        String album = metadata.getAlbum();
//                        String song_title = metadata.getSongTitle();
//                        Number track_number = metadata.getTrackNumber();
//                        Log.i("artist", artist);
//                        Log.i("album", album);
//                    }catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    File dst = file;
//                    MusicMetadata meta = new MusicMetadata("name");
//                    meta.setSongTitle(name);
//                    //meta.setArtist("CS");
//                    try {
//                        new MyID3().write(file, dst, src_set, meta);
//                    } catch (UnsupportedEncodingException e) {
//
//                    } catch (ID3WriteException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }  // write updated metadata
//                }


//                String pathF=Environment.getExternalStorageDirectory() + "/Android/data/" + activity.getClass().getPackage().getName() + "/imagesHD";
//                new File(pathF).mkdirs();
//                String ImagesHD=Environment.getExternalStorageDirectory() + "/Android/data/" + activity.getClass().getPackage().getName() + "/imagesHD" + id+".png";
//                File file1_ImagesHD=new File(ImagesHD);
//                if(!file1_ImagesHD.exists()){
//                Bitmap bmThumbnail = ThumbnailUtils.createVideoThumbnail(file.getAbsolutePath(), MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
//                File fileHD = new File(pathF,id+".png");
//                try {
//                    FileOutputStream out = new FileOutputStream(fileHD);
//                    bmThumbnail.compress(Bitmap.CompressFormat.PNG, 90, out);
//                    out.flush();
//                    out.close();
//                    System.gc();
//                } catch (Exception e) {
                //                Log.e("ERROR","ERROR",e);
                //           }
                //     }
                return true;
            } catch (Exception e) {
                Log.e("Download", "ERROR", e);
                return false;
            }


        }

        @Override
		protected void onProgressUpdate(String... progress) {
            //Log.d("ANDRO_ASYNC",progress[0]);
            myProgress.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(Boolean unused) {
            myProgress.dismiss();
            if (unused) {
                notifyDataSetChanged();
            } else {
                new AlertUIHelper(activity).show("Can't download.", Gravity.CENTER);
            }

        }
    }


}