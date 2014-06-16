package com.tools.tommydev.videofinder.Adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tools.tommydev.videofinder.R;
import com.tools.tommydev.videofinder.DataBaseHelper.DBClass_Downloader;
import com.tools.tommydev.videofinder.DataBaseHelper.DBClass_favor;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 
 * @author manish.s
 *
 */
public class CustomGridViewAdapter extends ArrayAdapter<DBClass_favor.sMembers> {
 Context context;
 int layoutResourceId;
 List<DBClass_favor.sMembers> data = new ArrayList<DBClass_favor.sMembers>();
private SharedPreferences settings; 
Activity activity;
 public CustomGridViewAdapter(Activity activity,Context context, int layoutResourceId,List<DBClass_favor.sMembers> data) {
  super(context, layoutResourceId, data);
  this.layoutResourceId = layoutResourceId;
  this.context = context;
  this.data = data;
  this.activity=activity;
 }

 @Override
 public View getView(final int position, View convertView, ViewGroup parent) {
  View row = convertView;
  RecordHolder holder = null;

  if (row == null) {
   LayoutInflater inflater = ((Activity) context).getLayoutInflater();
   row = inflater.inflate(layoutResourceId, parent, false);

   holder = new RecordHolder();
   holder.txtTitle = (TextView) row.findViewById(R.id.textView1);
   holder.imageItem = (ImageView) row.findViewById(R.id.imageView1);
   holder.imagePlay = (ImageView) row.findViewById(R.id.imageView1_play);
   row.setTag(holder);
  } else {
   holder = (RecordHolder) row.getTag();
  }
  
  DisplayMetrics displaymetrics = new DisplayMetrics();
  activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
  int width = displaymetrics.widthPixels;
  
  RelativeLayout.LayoutParams params=new  RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT,(int)(width*0.8));
  holder.imageItem.setLayoutParams(params);
  
  
  DBClass_favor.sMembers  members=data.get(position);
  
  settings =context.getSharedPreferences("Youtube_downloader_tommy", 0);
  final int bg_color=settings.getInt("change_text_color", -1);
  holder.txtTitle.setTextColor(bg_color);
  
  
  holder.txtTitle.setText(members.get_Filename());
  
  
  ImageLoader.getInstance().displayImage(members.get_img(),  holder.imageItem);

  holder.imagePlay.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		DBClass_Downloader dbClass_downloader = new DBClass_Downloader(context);
		  String[] strings = dbClass_downloader.SelectData(data.get(position).get_Status());

		  //  DBClass dbClass = new DBClass(Home.this);
		  //  String path = Environment.getExternalStorageDirectory().toString() + "/download/" + sMemberses.get(start).get_Filename().replace("\"", "").replace("<", "").replace(">", "").replace("|", "").replace("\\", "").replace("/", "").replace("?", "").replace(":", "").replace("*", "");
		if(strings!=null){
		  File f = new File(strings[3]);
			if (f.exists()) {
			    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(strings[3]));
			    intent.setDataAndType(Uri.parse(strings[3]), "video/*");
			    context.startActivity(Intent.createChooser(intent, "Choose Media Player"));
			} else {
				 context.startActivity(Intent.createChooser(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + data.get(position).get_Filesize())), "Choose Action"));
			}
		}else{
			 context.startActivity(Intent.createChooser(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + data.get(position).get_Filesize())), "Choose Action"));
		}
	}
	});
  
  
  
  return row;

 }

 static class RecordHolder {
   ImageView imagePlay;
TextView txtTitle;
  ImageView imageItem;

 }
}