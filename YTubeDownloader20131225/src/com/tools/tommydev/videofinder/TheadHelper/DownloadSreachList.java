package com.tools.tommydev.videofinder.TheadHelper;

import android.app.Activity;
import android.os.AsyncTask;
import com.tools.tommydev.videofinder.IOHelper.StreamUtils;
import com.tools.tommydev.videofinder.Interface.OnDownloadCompleteListner;
import com.tools.tommydev.videofinder.Interface.OnDownloadFiledListner;
import com.tools.tommydev.videofinder.Interface.OnDownloadStartListner;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

/**
 * Created by TomMy on 9/10/13.
 */
public class DownloadSreachList extends AsyncTask<String, String, String> {

    Activity activity;

    public DownloadSreachList(Activity activity) {
        this.activity = activity;
    }

    private OnDownloadStartListner onDownloadStartListner=null;
    private OnDownloadCompleteListner onDownloadCompleteListner =null;
    private OnDownloadFiledListner onDownloadFiledListner=null;

    public void setOnDownloadStartListner(OnDownloadStartListner onDownloadStartListner) {
        this.onDownloadStartListner = onDownloadStartListner;
    }

    public void setOnDownloadCompleteListner(OnDownloadCompleteListner onDownloadCompleteListner) {
        this.onDownloadCompleteListner = onDownloadCompleteListner;
    }

    public void setOnDownloadFiledListner(OnDownloadFiledListner onDownloadFiledListner) {
        this.onDownloadFiledListner = onDownloadFiledListner;
    }


    @Override
    protected void onPreExecute() {

        onDownloadStartListner.OnDownloadStart();

    }



    

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    /**
     *
     *
     *
     * @param0 url
     * @param1 file_cache
     *
     * @return
     */

    @Override
    protected String doInBackground(String... params) {
        String filename = params[1];
        try {

            String url_select = params[0];

           // Log.e("JSON", url_select);

            HttpParams httpParameters = new BasicHttpParams();
            int timeoutConnection = 9000;
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            int timeoutSocket = 10000;
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
            HttpClient client = new DefaultHttpClient(httpParameters);
            HttpUriRequest request = new HttpGet(url_select);
            HttpResponse response = client.execute(request);
            String result = StreamUtils.convertToString(response.getEntity().getContent());


           // String directory = Environment.getExternalStorageDirectory() + "/Android/data/"+activity.getClass().getPackage().getName()+"/file/";
           /// File files =new File(directory);
           // files.mkdirs();

         //   File file = new File(Environment.getExternalStorageDirectory() + "/Android/data/"+activity.getClass().getPackage().getName()+"/file", filename);
        //    FileOutputStream fos;
        //    byte[] data = new String(result).getBytes();

        //    fos = new FileOutputStream(file);
        //    fos.write(data);
        //    fos.flush();
        //    fos.close();



            return result;
        } catch (Exception e) {
            this.e=e.toString();
            return "";

        }

    }


    String e="";

    @Override
    protected void onPostExecute(String aVoid) {
        if(aVoid.equals("")){
            onDownloadFiledListner.OnDownloadFiledListner("StringBuilding & BufferedReader "+"Error converting result " + e.toString());
            this.cancel(true);
        }else{
            onDownloadCompleteListner.OndownloadComplete(aVoid);
        }

    }
}
