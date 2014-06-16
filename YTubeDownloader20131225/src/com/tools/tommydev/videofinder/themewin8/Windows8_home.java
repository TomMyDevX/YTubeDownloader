package com.tools.tommydev.videofinder.themewin8;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import net.margaritov.preference.colorpicker.ColorPickerDialog;
import net.margaritov.preference.colorpicker.ColorPickerDialog.OnColorChangedListener;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.tools.tommydev.videofinder.AppicationX;
import com.tools.tommydev.videofinder.R;
import com.tools.tommydev.videofinder.Test_Downloader_By_service;
import com.tools.tommydev.videofinder.Webview_new_detector;
import com.tools.tommydev.videofinder.Adapter.Windows8_Download_adapter;
import com.tools.tommydev.videofinder.DataBaseHelper.DBClass_Downloader;

public class Windows8_home extends Activity implements OnClickListener, OnLongClickListener{
    private RelativeLayout root;
	private LinearLayout buttom_layout;
	private ImageView windows8_setting;
	private ImageView windows_8_youtube;

	private ImageView windows_8_people;
	private ImageView windows_8_twitter;
	private ImageView windows_8_bookmark;

	private ImageView windows_8_download;
	private ListView windows_8_download_list;
	private TextView windows_8_download_total;
	private SharedPreferences settings;

	private EditText settings_content_login_username_edittext;
	private EditText settings_content_login_password_edittext;



	private ImageView windows_8_webview_dect;
	
	
	int [] textView=new int[]{R.id.textView8,
			R.id.textView7,
			R.id.textView10,
			R.id.textView9,
			R.id.windows_8_download_total,
			};
	


	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        
        setContentView(R.layout.windows8_home);
      
      

     
        
     
        
        root=(RelativeLayout)findViewById(R.id.root);
        windows8_setting=(ImageView)findViewById(R.id.windows8_setting);
        windows_8_youtube=(ImageView)findViewById(R.id.windows_8_youtube);
        windows_8_people=(ImageView)findViewById(R.id.windows_8_people);
        windows_8_twitter=(ImageView)findViewById(R.id.windows_8_twitter);
        windows_8_bookmark=(ImageView)findViewById(R.id.windows_8_bookmark);
        windows_8_download=(ImageView)findViewById(R.id.windows_8_download);
        windows_8_download_list=(ListView)findViewById(R.id.windows_8_download_list);
        windows_8_download_total=(TextView)findViewById(R.id.windows_8_download_total);

        windows_8_webview_dect=(ImageView)findViewById(R.id.windows_8_webview_dect);
        
        

        
		settings_content_login_username_edittext=(EditText)findViewById(R.id.settings_content_login_username_edittext);
		settings_content_login_password_edittext=(EditText)findViewById(R.id.settings_content_login_password_edittext);
		
        
        settings = getSharedPreferences("Youtube_downloader_tommy", 0);
        int bg_color=settings.getInt("change_bg_color", -16777216);
        
        root.setBackgroundColor(bg_color);
        
        
        
        
        
        
        //windows_8_youtube.setOnTouchListener(onTouchListener);
        windows_8_people.setOnTouchListener(onTouchListener);

        windows_8_twitter.setOnTouchListener(onTouchListener);
     

        
        
        windows_8_download.setOnClickListener(this);
        windows_8_bookmark.setOnClickListener(this);
        windows8_setting.setOnClickListener(this);
  
        windows_8_webview_dect.setOnClickListener(this);
        windows_8_youtube.setOnClickListener(this);
        
        windows_8_download.setOnLongClickListener(this);
        windows_8_bookmark.setOnLongClickListener(this);
        windows8_setting.setOnLongClickListener(this);
        windows_8_youtube.setOnLongClickListener(this);
        windows_8_webview_dect.setOnLongClickListener(this);
     
        
      
        Download_adapter = new Windows8_Download_adapter(Windows8_home.this,true);
        windows_8_download_list.setAdapter(Download_adapter);
        
       
	     
        DBClass_Downloader dbClass_downloader = new DBClass_Downloader(this);
	    List<DBClass_Downloader.File_Downloader> file_downloaders = dbClass_downloader.SelectAllData("file_status='Downloaded'");
        
        windows_8_download_list.setDivider(null);
        windows_8_download_total.setText(file_downloaders.size() +" Files");
        
        mHandler.post(mUpdateUI);
        
        
        
   

 
        findViewById(R.id.youtube_x).setBackgroundColor(settings.getInt(getResources().getResourceName(R.id.youtube_x),Color.RED));
        findViewById(R.id.webview_x).setBackgroundColor(settings.getInt(getResources().getResourceName(R.id.webview_x),Color.parseColor("#61dcff")));
        findViewById(R.id.windows_8_layout_setting).setBackgroundColor(settings.getInt(getResources().getResourceName(R.id.windows_8_layout_setting),Color.parseColor("#545454")));
        findViewById(R.id.windows_8_layout_bookmark).setBackgroundColor(settings.getInt(getResources().getResourceName(R.id.windows_8_layout_bookmark),Color.parseColor("#9f9f9f")));
        findViewById(R.id.windows_8_layout_download).setBackgroundColor(settings.getInt(getResources().getResourceName(R.id.windows_8_layout_download),Color.parseColor("#3fd0ff")));
        
        
        final int text_color=settings.getInt("change_text_color", -1);for(int i=0;i<textView.length;i++){try{((TextView)findViewById(textView[i])).setTextColor(text_color);}catch(Exception e){}}
        
        
    }
	
	@Override
	public void onClick(View v) {
		 if(v==windows_8_webview_dect){
			startActivity(new Intent(this,Webview_new_detector.class));
		}else if(v==windows8_setting){
			startActivityForResult(new Intent(getApplicationContext(),Windows8_Setting_Activity.class),1004);
		}else if(v==windows_8_bookmark){
			startActivity(new Intent(getApplicationContext(),Windows8_bookmark_activity.class));
		}else if(v==windows_8_youtube){
			startActivity(new Intent(getApplicationContext(),Windows8_finder.class));
    	}else if(v==windows_8_download){
    		startActivity(new Intent(getApplicationContext(),Windows8_download.class));
    	}
		
	}
	@Override
	public boolean onLongClick(View v) {
		if(v==windows_8_youtube){
			showpickColor(Color.RED,R.id.youtube_x);
		}else if(v==windows_8_webview_dect){
			showpickColor(Color.parseColor("#61dcff"),R.id.webview_x);
		}else if(v==windows8_setting){
			showpickColor(Color.parseColor("#545454"),R.id.windows_8_layout_setting);
		}else if(v==windows_8_bookmark){
			showpickColor(Color.parseColor("#9f9f9f"),R.id.windows_8_layout_bookmark);
		}else if(v==windows_8_download){
			showpickColor(Color.parseColor("#3fd0ff"),R.id.windows_8_layout_download);
		}

		return false;
	}
	 
	
	
	
	 final Handler mHandler = new Handler();

	private final Runnable mUpdateUI = new Runnable() {
	    @Override
		public void run() {
	    	if(countItem>=Download_adapter.getCount()){
        		countItem=0;
        	}
        	if(countItem<Download_adapter.getCount()){
        		Log.e("setSelection",countItem+"");
        		windows_8_download_list.setSelection(countItem++);
        	}
        	
        	
        	DBClass_Downloader dbClass_downloader = new DBClass_Downloader(Windows8_home.this);
		    List<DBClass_Downloader.File_Downloader> file_downloaders = dbClass_downloader.SelectAllData("file_status='Downloaded'");
        	
        	Download_adapter = new Windows8_Download_adapter(Windows8_home.this,file_downloaders,true);
            windows_8_download_list.setAdapter(Download_adapter);
        	
	     	
	     	
	        windows_8_download_total.setText(file_downloaders.size() +" Files");
	        mHandler.postDelayed(mUpdateUI, 6000); // 1 second
	        }
	    };


	
	
	int countItem=0;
	Windows8_Download_adapter Download_adapter ;

	private int getStringResourceByName(String aString,String type) {
	      String packageName = getPackageName();
	      int resId = getResources().getIdentifier(aString,type, packageName);
	      return resId;
	    }

	
	@SuppressLint("NewApi")
	private void setOnClickDrawable(View v){
		v.setAlpha(0.75f);
	
		
	}
	@SuppressLint("NewApi")
	private void removeOnClickDrawable(View v){
		v.setAlpha(1f);
		
	}

	
	
	OnTouchListener onTouchListener=new OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			int eventaction = event.getAction();

		    switch (eventaction) {
		        case MotionEvent.ACTION_DOWN: 
		        	setOnClickDrawable(v);
		            break;

		        case MotionEvent.ACTION_MOVE:
		        	setOnClickDrawable(v);
		            break;
		        case MotionEvent.ACTION_CANCEL:
		        	removeOnClickDrawable(v);
		            break;
		            
		        case MotionEvent.ACTION_UP:   
		        	if(v==windows8_setting){
		        		Log.e("Click","windows8_setting");
		        		
		        	}
		        	removeOnClickDrawable(v);
		            break;
		    }

		    
			
			return true;
		}
	};
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public void execLogin(){
		new AsyncTask<String, String, String>() {
			@Override
			protected String doInBackground(String... va) {
				String md5Hex = getMd5Hash(settings_content_login_password_edittext.getText().toString());
				
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("username", settings_content_login_username_edittext.getText().toString()));
				params.add(new BasicNameValuePair("password", md5Hex));
				String resultServer  = getHttpPost("http://thailandappsdata.com/android/apis/login.php",params);
				
				
				
				
				
				Log.e("HttpResponse",resultServer.toString());
				
				
				return resultServer.toString();
			}
			@Override
			protected void onPostExecute(String result) {
				
					try {
					Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
					JSONArray array = new JSONArray(result);
					if(array.getString(0).equals("Successfully")){
					JSONObject jsonObject=array.getJSONObject(1);
					SharedPreferences example = getSharedPreferences(AppicationX.Prefer_Save, 0);
					Editor editor = example.edit();
					editor.putBoolean("login", true);
					editor.putString("uid", jsonObject.getString("uid"));
					editor.putString("username", jsonObject.getString("username"));
					editor.commit();
					
					}else{
						Toast.makeText(getApplicationContext(), "Can't login", Toast.LENGTH_SHORT).show();
					}
					} catch (JSONException e) {
						Log.e("JSONException","JSONException",e);
					}
				
			};
		}.execute();
	}
	
	
	
	

	public String getHttpPost(String url,List<NameValuePair> params) {
			StringBuilder str = new StringBuilder();
			HttpClient client = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			
			try {
			httpPost.setEntity(new UrlEncodedFormEntity(params));
			HttpResponse response = client.execute(httpPost);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) { // Status OK
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					str.append(line);
				}
			} else {
				Log.e("Log", "Failed to download result..");
			}
			} catch (ClientProtocolException e) {
			e.printStackTrace();
			} catch (IOException e) {
			e.printStackTrace();
			}
			return str.toString();
	}
	
	
	
	
	public static String getMd5Hash(String input) {
	    try {
	        MessageDigest md = MessageDigest.getInstance("MD5");
	        byte[] messageDigest = md.digest(input.getBytes());
	        BigInteger number = new BigInteger(1, messageDigest);
	        String md5 = number.toString(16);

	        while (md5.length() < 32)
	            md5 = "0" + md5;

	        return md5;
	    } catch (NoSuchAlgorithmException e) {
	        Log.e("MD5", e.getLocalizedMessage());
	        return null;
	    }
	}



	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		mHandler.removeCallbacks(mUpdateUI);
		super.onBackPressed();
	}
	
	 @Override
	  public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	 
	    if(requestCode==1004){
	        final int text_color=settings.getInt("change_text_color", -1);
	        for(int i=0;i<textView.length;i++){
	        	try{
	        		((TextView)findViewById(textView[i])).setTextColor(text_color);
	        	}catch(Exception e){
	        		
	        	}
	        	
	        	
	        }
	    }
	  }

	 
	 

	 @SuppressWarnings("deprecation")
	public static int getHeight(Context mContext){
		 
		 int resId = mContext.getResources().getIdentifier("status_bar_height","dimen", "android");
		 int result =0;
		    if (resId > 0) {
		         result = mContext.getResources().getDimensionPixelSize(resId);
		    } 
		    
		    int height=0;
		    WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		    Display display = wm.getDefaultDisplay();
		    if(Build.VERSION.SDK_INT>12){               
		        Point size = new Point();
		        display.getSize(size);
		        height = size.y;
		    }else{          
		        height = display.getHeight();  // deprecated
		    }
		    return height-result;      
		}

@Override
protected void onResume() {
	// TODO Auto-generated method stub
	super.onResume();
	 registerReceiver(receiver, new IntentFilter(Test_Downloader_By_service.CUSTOM_INTENT));
	 
      
	
}
@Override
protected void onPause() {
  super.onPause();
  unregisterReceiver(receiver);
 
}


private BroadcastReceiver receiver = new BroadcastReceiver() {

    @Override
    public void onReceive(Context context, Intent intent) {
      Bundle bundle = intent.getExtras();
      if (bundle != null) {
        int Progress = bundle.getInt("Progress");
        Log.e("Progress",Progress+"");
        
        
         if(windows_8_download!=null){
        	
        	 if(Progress==1){
        		 windows_8_download.setImageResource(R.drawable.windows_8_dw_p1);
        	 }else if(Progress==2){
        		 windows_8_download.setImageResource(R.drawable.windows_8_dw_p2);
         	 }else if(Progress==3){
         		windows_8_download.setImageResource(R.drawable.windows_8_dw_p3);
          	 }else if(Progress==4){
          		windows_8_download.setImageResource(R.drawable.windows_8_dw_p4);
          	 }else if(Progress==5){
          		windows_8_download.setImageResource(R.drawable.windows_8_dw_p5);
          	 }else if(Progress==6){
          		windows_8_download.setImageResource(R.drawable.windows_8_dw_p6);
          	 }else if(Progress==7){
          		windows_8_download.setImageResource(R.drawable.windows_8_dw_p7);
          	 }else if(Progress==8){
          		windows_8_download.setImageResource(R.drawable.windows_8_dw_p8);
          	 }else if(Progress==9){
          		windows_8_download.setImageResource(R.drawable.windows_8_dw_p9);
          	 }else if(Progress==10){
          		windows_8_download.setImageResource(R.drawable.windows_8_dw_p10);
          	 }else if(Progress==11){
          		windows_8_download.setImageResource(R.drawable.windows_8_dw_p11);
          	 }else if(Progress==12){
          		windows_8_download.setImageResource(R.drawable.windows_8_dw_p12);
          	 }else if(Progress==13){
          		windows_8_download.setImageResource(R.drawable.windows_8_dw_p13);
          	 }else if(Progress==14){
          		windows_8_download.setImageResource(R.drawable.windows_8_dw_p14);
          	 }else if(Progress==15){
          		windows_8_download.setImageResource(R.drawable.windows_8_dw_p15);
          	 }else if(Progress==16){
          		windows_8_download.setImageResource(R.drawable.windows_8_dw_p16);
          	 }else if(Progress==17){
          		windows_8_download.setImageResource(R.drawable.windows_8_dw_p17);
          	 }else if(Progress==18){
          		windows_8_download.setImageResource(R.drawable.windows_8_dw_p18);
          	 }else if(Progress==19){
          		windows_8_download.setImageResource(R.drawable.windows_8_dw_p19);
          	 }else if(Progress==20){
          		windows_8_download.setImageResource(R.drawable.windows_8_dw_p20);
          	 }else if(Progress==21){
          		windows_8_download.setImageResource(R.drawable.windows_8_dw_p21);
          	 }else if(Progress==22){
          		windows_8_download.setImageResource(R.drawable.windows_8_dw_p22);
          	 }else if(Progress==23){
          		windows_8_download.setImageResource(R.drawable.windows_8_dw_p23);
          	 }else if(Progress==24){
          		windows_8_download.setImageResource(R.drawable.windows_8_dw_p24);
          	 }else if(Progress==25){
          		windows_8_download.setImageResource(R.drawable.windows_8_dw_p25);
          	 }else if(Progress==26){
          		windows_8_download.setImageResource(R.drawable.windows_8_dw_p26);
          	 }else if(Progress==27){
          		windows_8_download.setImageResource(R.drawable.windows_8_dw_p27);
          	 }else if(Progress==28){
          		windows_8_download.setImageResource(R.drawable.windows_8_dw_p28);
          	 }else if(Progress==29){
          		windows_8_download.setImageResource(R.drawable.windows_8_dw_p29);
          	 }else if(Progress==30){
          		windows_8_download.setImageResource(R.drawable.windows_8_dw_p30);
          	 }else if(Progress==32){
          		windows_8_download.setImageResource(R.drawable.windows_8_dw_p32);
          	 }else if(Progress==34){
          		windows_8_download.setImageResource(R.drawable.windows_8_dw_p34);
          	 }else if(Progress==36){
          		windows_8_download.setImageResource(R.drawable.windows_8_dw_p36);
          	 }else if(Progress==38){
          		windows_8_download.setImageResource(R.drawable.windows_8_dw_p38);
          	 }else if(Progress==40){
          		windows_8_download.setImageResource(R.drawable.windows_8_dw_p40);
          	 }else if(Progress==42){
          		windows_8_download.setImageResource(R.drawable.windows_8_dw_p42);
          	 }else if(Progress==44){
          		windows_8_download.setImageResource(R.drawable.windows_8_dw_p44);
          	 }else if(Progress==46){
          		windows_8_download.setImageResource(R.drawable.windows_8_dw_p46);
          	 }else if(Progress==48){
          		windows_8_download.setImageResource(R.drawable.windows_8_dw_p48);
          	 }else if(Progress==50){
          		windows_8_download.setImageResource(R.drawable.windows_8_dw_p50);
          	 }else if(Progress==53){
          		windows_8_download.setImageResource(R.drawable.windows_8_dw_p53);
          	 }else if(Progress==54){
          		windows_8_download.setImageResource(R.drawable.windows_8_dw_p54);
          	 }else if(Progress==57){
          		windows_8_download.setImageResource(R.drawable.windows_8_dw_p57);
          	 }else if(Progress==60){
          		windows_8_download.setImageResource(R.drawable.windows_8_dw_p60);
          	 }else if(Progress==63){
          		windows_8_download.setImageResource(R.drawable.windows_8_dw_p63);
          	 }else if(Progress==65){
          		windows_8_download.setImageResource(R.drawable.windows_8_dw_p65);
          	 }else if(Progress==68){
          		windows_8_download.setImageResource(R.drawable.windows_8_dw_p68);
          	 }else if(Progress==70){
          		windows_8_download.setImageResource(R.drawable.windows_8_dw_p70);
          	 }else if(Progress==72){
          		windows_8_download.setImageResource(R.drawable.windows_8_dw_p72);
          	 }else if(Progress==76){
          		windows_8_download.setImageResource(R.drawable.windows_8_dw_p76);
          	 }else if(Progress==80){
          		windows_8_download.setImageResource(R.drawable.windows_8_dw_p80);
          	 }else if(Progress==84){
          		windows_8_download.setImageResource(R.drawable.windows_8_dw_p48);
          	 }else if(Progress==88){
          		windows_8_download.setImageResource(R.drawable.windows_8_dw_p88);
          	 }else if(Progress==92){
          		windows_8_download.setImageResource(R.drawable.windows_8_dw_p92);
          	 }else if(Progress==96){
          		windows_8_download.setImageResource(R.drawable.windows_8_dw_p96);
          	 }else if(Progress==100){
          		windows_8_download.setImageResource(R.drawable.windows_8_dw_p100);
          		
          		new CountDownTimer(1000,1000) {
					
					@Override
					public void onTick(long arg0) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onFinish() {
						windows_8_download.setImageResource(R.drawable.windows_8_download);
					}
				};
          		
          		
          	 }
        	 	 
        
         }
      }
    }
  };


public void showpickColor(int defaultColor,final int ResID){
	ColorPickerDialog colorPickerDialog=new ColorPickerDialog(this,defaultColor);
	colorPickerDialog.setOnColorChangedListener(new OnColorChangedListener() {
		@Override
		public void onColorChanged(int color) {
			Log.e("Color",color+"");
			SharedPreferences settings =getSharedPreferences("Youtube_downloader_tommy", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt(getResources().getResourceName(ResID),color);
            editor.commit();
            findViewById(ResID).setBackgroundColor(color);
		}
	});
	colorPickerDialog.show();
}
}
