package com.tools.tommydev.videofinder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class Webview_new_detector extends Activity {
	EditText et;
	private WebView wv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview_new_detector);
		
		
		wv=(WebView)findViewById(R.id.web_view);
    	WebSettings settings=wv.getSettings();
    	settings.setJavaScriptEnabled(true);
    	settings.setBuiltInZoomControls(true);
    	wv.setWebViewClient(new MyWebViewClient());
        et=(EditText)findViewById(R.id.url_field);
        wv.loadUrl("http://www.google.com");
        wv.clearHistory();
        wv.clearCache(true);
    	et.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (event != null && event.getAction() != KeyEvent.ACTION_DOWN) {
			        return false;
			    } else if (actionId == EditorInfo.IME_ACTION_GO|| event == null|| event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
			    	String url=et.getText().toString().trim();
			    	wv.loadUrl("http://"+url);
			    }
				return false;
			}
		});
    	
    	
		
    	
    	
    	
    	
    	
		
	}

	private class MyWebViewClient extends WebViewClient
    {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			return false;
		}
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			
		}
		@Override
		public void onPageFinished(WebView view, String url) {
			new XAsyncTask().execute(url);
		}
    	//"http://cdna.mobile.youjizz.com/videos/5/e/8/3/c/5e83c82f7d92d68c7ad32708e7ae486e1376406039-640-360-1000-h264.mp4?nvb=20140117030554&nva=20140119030554&int=3%25&sr=1440&hash=0c91a15e11e99025c5286"
    }
	
	class XAsyncTask extends AsyncTask<String, String, String[]>{

		@Override
		protected String[] doInBackground(String... params) {
			URL url;
			String [] data=null;
			try {
			url = new URL(params[0]);
			URLConnection conn = url.openConnection();
			Map<String, List<String>> headerFields = conn.getHeaderFields();

			Set<String> headerFieldsSet = headerFields.keySet();
			Iterator<String> hearerFieldsIter = headerFieldsSet.iterator();
			
			
			boolean found=false;String type="";
			while (hearerFieldsIter.hasNext()) {
				
				 String headerFieldKey = hearerFieldsIter.next();
				 List<String> headerFieldValue = headerFields.get(headerFieldKey);
				 
				 StringBuilder sb = new StringBuilder();
				 for (String value : headerFieldValue) {
					 sb.append(value);
					 sb.append("");
				}
				 System.out.println(headerFieldKey + "=" + sb.toString());
				if(headerFieldKey!=null){
				 if(headerFieldKey.equals("Content-Type")){
					 if(sb.toString().startsWith("video/")){
						 type=sb.toString();
						 System.out.println(headerFieldKey + "=" + sb.toString());
						 found=true;
						 break;
					 }
				 }
				}
			}
			if(found){
				data=new String[2];
				data[0]=params[0];
				data[1]=type;
			}
			
			
			
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return data;
		}
		@Override
		protected void onPostExecute(final String[] result) {
			if(result!=null){
				
				
                final EditText input = new EditText(Webview_new_detector.this);
                final AlertDialog.Builder X = new AlertDialog.Builder(Webview_new_detector.this)
                        .setTitle("User comfirm")
                        .setMessage(result[0])
                        .setView(input)
                        .setPositiveButton("Download", new DialogInterface.OnClickListener() {
                            @Override
							public void onClick(DialogInterface dialog, int whichButton) {
                            	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            					String currentDateandTime = sdf.format(new Date());
            					new DownloadTask_for_WebviewDetecter(Webview_new_detector.this, currentDateandTime, result[0], "."+result[1].split("/")[1]).execute();
            					System.out.println("File will download::"+result[0]);
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
							public void onClick(DialogInterface dialog, int whichButton) {
                                // Do nothing.
                            }
                        });
                X.show();
				
				
				
				
				
				
					
					
			}
		}
		
	}
	
	
	


}
