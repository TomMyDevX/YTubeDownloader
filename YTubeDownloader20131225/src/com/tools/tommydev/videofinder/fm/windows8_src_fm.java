package com.tools.tommydev.videofinder.fm;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tools.tommydev.videofinder.Home;
import com.tools.tommydev.videofinder.R;
import com.tools.tommydev.videofinder.Test_Downloader_By_service;
import com.tools.tommydev.videofinder.Adapter.ListXAdapter;
import com.tools.tommydev.videofinder.DataBaseHelper.DBClass_AutoText;
import com.tools.tommydev.videofinder.DataBaseHelper.DBClass_Downloader;
import com.tools.tommydev.videofinder.Interface.OnDownloadCompleteListner;
import com.tools.tommydev.videofinder.Interface.OnDownloadFiledListner;
import com.tools.tommydev.videofinder.Interface.OnDownloadStartListner;
import com.tools.tommydev.videofinder.TheadHelper.DownloadSreachList;
import com.tools.tommydev.videofinder.TheadHelper.downloadJson_Images;
import com.tools.tommydev.videofinder.UIHelper.AlertUIHelper;

/**
 * Created by TomMy on 9/5/13.
 */
public class windows8_src_fm extends Fragment {

	  private AutoCompleteTextView editText;
	  DownloadSreachList downloadSreachList;
	  ListView listviewx;
	  private downloadJson_Images downloadJson_images;;
	  private ProgressBar progressBar2;
	private SharedPreferences settings;
	AlertUIHelper alertUIHelper;
	@Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		  settings = getActivity().getSharedPreferences("Youtube_downloader_tommy", 0);
		alertUIHelper=new AlertUIHelper(getActivity());
	    IntentFilter intentToReceiveFilter = new IntentFilter();
        intentToReceiveFilter.addAction(Test_Downloader_By_service.CUSTOM_INTENT);
        getActivity().registerReceiver(mIntentReceiver, intentToReceiveFilter, null, mHandler);

        
	    View view = inflater.inflate(R.layout.src_ly,container, false);
	    
	    listviewx=(ListView) view.findViewById(R.id.listviewx);
	    progressBar2=(ProgressBar) view.findViewById(R.id.progressBar2);
	    settings = getActivity().getSharedPreferences("Youtube_downloader_tommy", 0);
	    setSreachAutoText(view);
	    
	    
	    
	    ImageView imageView=(ImageView) view.findViewById(R.id.imageView1);
	    imageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (listviewx.getAdapter() != null) {
                    listviewx.setAdapter(null);
                }
                editText.setText("");
               
                if (downloadSreachList != null) {
                    downloadSreachList.cancel(true);
                }


                if (downloadJson_images != null) {
                    downloadJson_images.cancel(true);
                }
                progressBar2.setVisibility(View.GONE);
			}
		});
	    
       // editText.setText(getIntent().getStringExtra("text"));
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setFocusableInTouchMode(true);
                editText.setFocusable(true);
              

            }
        });
       /* editText.setOnTouchListener(new RightDrawableOnTouchListener(editText) {
            @Override
            public boolean onDrawableTouch(final MotionEvent event) {
                
                
                return false;
            }
        });*/

        editText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (downloadSreachList != null) {
                    downloadSreachList.cancel(true);
                }
                downloadSreachList = new DownloadSreachList(getActivity());
                onSearchData(adapterView.getItemAtPosition(position).toString());
                Log.e("position", position + "");

            }
        });

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (downloadSreachList != null) {
                        downloadSreachList.cancel(true);
                    }
                    downloadSreachList = new DownloadSreachList(getActivity());
                    onSearchData(editText.getText().toString());
                }
                return false;
            }
        });
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if (downloadSreachList != null) {
                        downloadSreachList.cancel(true);
                    }
                    downloadSreachList = new DownloadSreachList(getActivity());
                    onSearchData(editText.getText().toString());
                    return true;
                }
                return false;
            }
        });
	    
      
	     final int bg_color=settings.getInt("change_bg_color", -16777216);
	     view.setBackgroundColor(bg_color);
	    
	    
	    return view;
	  }

	 public void onSearchData(String text) {
		 try{
			 hideSoftKeyboard(getActivity());
		 }catch(Exception e){
			 
		 }
		 	
		 	progressBar2.setVisibility(View.VISIBLE);
	        

	        editText.dismissDropDown();


	        DBClass_AutoText dbClass_autoText = new DBClass_AutoText(getActivity());
	        dbClass_autoText.InsertData(text, text, text);


	       


	        String completeEncolseURI = "";
	        try {
	            completeEncolseURI = URLEncoder.encode(text, "utf-8");
	        } catch (UnsupportedEncodingException e) {
	            completeEncolseURI = text;
	        }

	        downloadSreachList.setOnDownloadStartListner(new OnDownloadStartListner() {
	            @Override
	            public void OnDownloadStart() {

	            }
	        });
	        int seek_text_var = settings.getInt("seek_text_var", 15);
	        downloadSreachList.execute(new String[]{"http://gdata.youtube.com/feeds/api/videos?q=" + completeEncolseURI + "&alt=jsonc&v=2&max-results=" + seek_text_var, completeEncolseURI});

	        downloadSreachList.setOnDownloadFiledListner(new OnDownloadFiledListner() {
	            @Override
	            public void OnDownloadFiledListner(String error) {
	                Log.e("Error", error);
	                if(progressBar2!=null){
	                	progressBar2.setVisibility(View.GONE);
	                }
	                
	                if(listviewx!=null){
	                	 listviewx.setVisibility(View.GONE);
	                }
	                if(alertUIHelper!=null){
	                	 alertUIHelper.show("Search failed.", Gravity.CENTER);
	                }else{
	                	Toast.makeText(getActivity(), "Search failed.", Toast.LENGTH_SHORT).show();
	                }
	               
	                
	               
	               


	            }
	        });
	        downloadSreachList.setOnDownloadCompleteListner(new OnDownloadCompleteListner() {
	            @Override
	            public void OndownloadComplete(String path) {
	                Log.e("path", path);
	                downloadJson_images = new downloadJson_Images(windows8_src_fm.this.getActivity(), listviewx, progressBar2);
	                downloadJson_images.execute(new String[]{path});
	            }
	        });


	    }
	   
	
	
	
	
	
	
	  public  void hideSoftKeyboard(Activity activity) {
	        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
	        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
	    }
	  
	  public void setSreachAutoText(View view) {
	        editText = (AutoCompleteTextView) view.findViewById(R.id.editText_Src);
	        DBClass_AutoText dbClass_autoText = new DBClass_AutoText(getActivity());
	        List<DBClass_AutoText.Bottom_apps> autotext_appses = dbClass_autoText.SelectAllData();
	        String[] data_autotext = new String[autotext_appses.size()];
	        for (int i = 0; i < autotext_appses.size(); i++) {
	            data_autotext[i] = autotext_appses.get(i).get_PackageID();
	        }
	        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.autotextview, data_autotext);
	        editText.setAdapter(adapter);
	        editText.setDropDownBackgroundResource(R.drawable.top_bar_bg);
	        
	        final int text_color=settings.getInt("change_text_color", -1);
	       
	        editText.setTextColor(text_color);
	    }
	  
	  
	  
	  private final Handler mHandler = new Handler();
	    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
	        @Override
	        public void onReceive(Context context, Intent intent) {
	            if (intent.getAction().equals(Test_Downloader_By_service.CUSTOM_INTENT)) {
	                int progress = intent.getIntExtra("progress", 0);
	                String title = intent.getStringExtra("title");
	                String vid = intent.getStringExtra("vid");
	             
	                if (progress == 100) {
	                    DBClass_Downloader dbClass_downloader = new DBClass_Downloader(getActivity());
	                    dbClass_downloader.UpdateDownloadData(vid, "Downloaded");
	                    showNotification(title);
	                    ListXAdapter adapter = (ListXAdapter) listviewx.getAdapter();
	                    if (adapter != null) {
	                        adapter.ReStartAdapter();
	                    }


	                }
	            }
	        }
	    };
	    
	    public void showNotification(String title) {
	        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
	        Intent notificationIntent = new Intent(getActivity(), Home.class);
	        PendingIntent contentIntent = PendingIntent.getActivity(getActivity(), 0, notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
	        NotificationCompat.Builder mBuilder =
	                new NotificationCompat.Builder(windows8_src_fm.this.getActivity())
	                        .setSmallIcon(R.drawable.download)
	                        .setContentTitle(title)
	                        .setAutoCancel(false)
	                        .setContentIntent(contentIntent)
	                        .setContentText("Save Complete.");
	        mBuilder.setSound(soundUri);
	        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(getActivity().getApplicationContext().NOTIFICATION_SERVICE);
	        notificationManager.notify(0, mBuilder.getNotification());

	    }

	    public void cancelNotification(int notificationId) {
	        if (Context.NOTIFICATION_SERVICE != null) {
	            String ns = Context.NOTIFICATION_SERVICE;
	            NotificationManager nMgr = (NotificationManager) getActivity().getApplicationContext().getSystemService(ns);
	            nMgr.cancel(notificationId);
	        }
	    }

	    @Override
	    public void onDestroy() {
	    	getActivity().unregisterReceiver(mIntentReceiver);
	    	super.onDestroy();
	    	
	    }
	    
	    
	    
	    
	} 
