package com.tools.tommydev.videofinder.TheadHelper;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.tools.tommydev.videofinder.Adapter.ListXAdapter;
import com.tools.tommydev.videofinder.Object.Mp3List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by TomMy on 30/9/2556.
 */
public class downloadJson_Images extends AsyncTask<String, String, ArrayList<Mp3List>> {
    Activity home;
    ListView listview;
    ProgressBar progressBar;
    Boolean iscancel = false;

    public downloadJson_Images(Activity home, ListView listview, ProgressBar progressBar) {
        this.home = home;
        this.listview = listview;
        this.progressBar = progressBar;
    }

    @Override
    protected void onCancelled() {
        iscancel = true;
        super.onCancelled();
    }

    @Override
    protected ArrayList<Mp3List> doInBackground(String... params) {

        ArrayList<Mp3List> mp3Lists = new ArrayList<Mp3List>();
        String result = "";

       // File file = new File(params[0]);
       // if (file.exists()) {
            result = params[0];
      //  }
       // Log.e("result", result);


        try {
            mp3Lists = new ArrayList<Mp3List>();
            ArrayList<String> items = new ArrayList<String>();
            JSONObject json = new JSONObject(result);
            JSONArray jsonArray = json.getJSONObject("data").getJSONArray("items");
// Log.e("JSON", jsonArray.toString() + "");
            String[] datatest = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

//
                String id = jsonObject.getString("id");
                String title = jsonObject.getString("title");
                int duration = jsonObject.getInt("duration");
                String description = jsonObject.getString("description");


                JSONObject jsonobjcontent = jsonObject.getJSONObject("content");


//  String mp3_1=jsonobjcontent.getString("1");


                Mp3List mp3List = new Mp3List();
                mp3List.setId(id);
                mp3List.setTitle(title);
// mp3List.setThumbnail(thumnail);
                mp3List.setDuration(duration);
                mp3List.setDescription(description);
// mp3List.setFilepath(mp3_1);

                JSONObject jsonthumbnail = jsonObject.getJSONObject("thumbnail");
                String thumnail = jsonthumbnail.getString("hqDefault");
                String thumnail1 = jsonthumbnail.getString("sqDefault");
                mp3List.setSmall_thumbnail(thumnail1);
                mp3List.setThumbnail(thumnail);
                mp3Lists.add(mp3List);
                datatest[i] = title;
                items.add(title);


// download(thumnail,id);


//Log.e("JSON",id+"|"+title);
            } // End Loop


//        for(int i=0;i<mp3Lists.size();i++){
//        if(iscancel){break;};
//        download(mp3Lists.get(i).getThumbnail(),mp3Lists.get(i).getId());
//}

            return mp3Lists;
        } catch (JSONException e) {
        	return mp3Lists;
        //    Log.e("ERROR", "ERROR", e);
        }
       
    }

    //
    @Override
    protected void onPostExecute(ArrayList<Mp3List> mp3Lists) {
        //  Search_Fragment.OndownloadComplete();
    	if(listview!=null){
    		listview.setVisibility(View.VISIBLE);
    		if(home!=null){
    			  ListXAdapter listXAdapter = new ListXAdapter(home, mp3Lists);
    	          listview.setAdapter(listXAdapter);
    		}
          
    	}
        if(progressBar!=null){
        	 progressBar.setVisibility(View.GONE);
        }
       
        Log.e("Download_Complete", "55555");


       /* for (int i = 0; i < mp3Lists.size(); i++) {
            Log.e("data", mp3Lists.get(i).getId());
        }*/


// search_listview.smoothScrollToPosition(pos);
// System.gc();
    }


//    public String download(String url,String id) {
//        File file=new File(Environment.getExternalStorageDirectory() + "/Android/data/"+home.getClass().getPackage().getName()+"/images/");
//        file.mkdirs();
//        try {
//            File externalStorageDirectory = Environment.getExternalStorageDirectory();
//            URL urlTmp = new URL(url);
//            String filename = urlTmp.getFile();
//            filename = Environment.getExternalStorageDirectory() + "/Android/data/"+home.getClass().getPackage().getName()+"/images/"+id;
//            if(!new File(filename).exists()){
//                Bitmap bitmap = BitmapFactory.decodeStream(urlTmp.openStream());
//                FileOutputStream fileOutputStream = new FileOutputStream(filename);
//                if (bitmap != null) {
//                    bitmap.compress(Bitmap.CompressFormat.JPEG, 5, fileOutputStream);
//                    BitmapFactory.Options options = new BitmapFactory.Options();
//                    options.inJustDecodeBounds = true;
//                    BitmapFactory.decodeFile(filename, options);
//                    int imageHeight = options.outHeight;
//                    int imageWidth = options.outWidth;
//                    String imageType = options.outMimeType;
//
//
//                    Log.e("tag", "Saved image " + filename+"|imageHeight="+imageHeight+"|imageWidth="+imageWidth+"|imageType"+imageType);
//                    return filename;
//                }
//                fileOutputStream.close();
//            }else{
//                Log.e("tag", "Exists image " + filename);
//            }
//        } catch (MalformedURLException e) {
//            Log.w("tag", "Could not save image with url: " + url, e);
//        } catch (IOException e) {
//            Log.w("tag", "Could not save image with url: " + url, e);
//        }
//        //Log.d("tag", "Failed to save image " + id);
//        return null;
//    }

}