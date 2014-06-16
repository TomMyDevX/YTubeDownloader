package com.tools.tommydev.videofinder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

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

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

public class Create_Acc_Acitivty extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_create_acc);
		
		
	}

	
	private EditText settings_content_login_username_edittext;
	private EditText settings_content_login_password_edittext;
	private EditText settings_content_login_password_edittext1;
    
    public void execLogin(){
    	settings_content_login_username_edittext=(EditText)findViewById(R.id.settings_content_login_username_edittext);
		settings_content_login_password_edittext=(EditText)findViewById(R.id.settings_content_login_password_edittext);
		settings_content_login_password_edittext1=(EditText)findViewById(R.id.settings_content_login_password_edittext1);
    
		if(settings_content_login_username_edittext.length()<6){
			settings_content_login_username_edittext.setFocusableInTouchMode(true);
			settings_content_login_username_edittext.requestFocus();
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Data Required.");
			builder.setMessage("This field is required data to complete. Please fill up.");
			builder.setPositiveButton("OK", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					dialog.dismiss();
				}
			});
			builder.show();
			return;
		}else if(settings_content_login_password_edittext.length()<6){
			settings_content_login_password_edittext.setFocusableInTouchMode(true);
			settings_content_login_password_edittext.requestFocus();
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Data Required.");
			builder.setMessage("This field is required data to complete. Please fill up.");
			builder.setPositiveButton("OK", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					dialog.dismiss();
				}
			});
			builder.show();
			return;
		}else if((settings_content_login_password_edittext.length()<6&&settings_content_login_password_edittext1.length()<6)&&(!settings_content_login_password_edittext.getText().toString().equals(settings_content_login_password_edittext1.getText().toString()))){
			settings_content_login_password_edittext.setFocusableInTouchMode(true);
			settings_content_login_password_edittext.requestFocus();
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Password mismatch.");
			builder.setMessage("This field is required data to complete. Please fill up.");
			builder.setPositiveButton("OK", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					dialog.dismiss();
				}
			});
			builder.show();
			return;
		}
		
		
		
		
		
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
					Toast.makeText(getApplicationContext(), "Waiting to login", Toast.LENGTH_SHORT).show();
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
	
	
	

}
