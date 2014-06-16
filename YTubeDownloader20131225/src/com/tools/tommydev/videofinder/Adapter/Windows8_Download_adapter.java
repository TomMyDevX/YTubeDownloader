package com.tools.tommydev.videofinder.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tools.tommydev.videofinder.DataBaseHelper.DBClass_Downloader;
import com.tools.tommydev.videofinder.DataBaseHelper.DBClass_Downloader.File_Downloader;
import com.tools.tommydev.videofinder.R;

import java.util.List;

/**
 * Created by TomMy on 10/13/13.
 */
public class Windows8_Download_adapter extends BaseAdapter {
    List<DBClass_Downloader.File_Downloader> file_downloader;
    Activity activity;
    boolean modeActivity=false;
	private SharedPreferences settings;
    public Windows8_Download_adapter(Activity activity, boolean b) {
        this.activity = activity;
        modeActivity=b;
        DBClass_Downloader dbClass_downloader = new DBClass_Downloader(activity);
	    List<DBClass_Downloader.File_Downloader> file_downloaders = dbClass_downloader.SelectAllData("file_status='Downloaded'");
	    this.file_downloader = file_downloaders;
    }

    @Override
    public void notifyDataSetChanged() {
    	
    	super.notifyDataSetChanged();
    	
    	
    }
    
    public Windows8_Download_adapter(Activity activity2,List<File_Downloader> file_downloaders, boolean b) {
    	 this.activity = activity2;
         modeActivity=b;
 	     this.file_downloader = file_downloaders;
	}

	@Override
    public int getCount() {
        return file_downloader.size();
    }

    @Override
    public Object getItem(int i) {
        return file_downloader.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View V = view;

        if(V == null) {
            LayoutInflater vi = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            V = vi.inflate(R.layout.windows8_list_download, null);
        }
        
        
        DBClass_Downloader.File_Downloader mi = file_downloader.get(i);
        TextView title = (TextView)V.findViewById(R.id.dw_title);
      
        title.setText(mi.get_file_title());
        settings =activity.getSharedPreferences("Youtube_downloader_tommy", 0);
         int bg_color=settings.getInt("change_text_color", -1);
      
        if(modeActivity){
        	 title.setTextColor(bg_color);
        	 title.setSelected(true);
        	((TextView)V.findViewById(R.id.textView_title)).setVisibility(View.GONE);
        	((TextView)V.findViewById(R.id.textView)).setVisibility(View.GONE);
        	((TextView)V.findViewById(R.id.dw_status)).setVisibility(View.GONE);
        }else{
        	 title.setTextColor(bg_color);
        	((TextView)V.findViewById(R.id.textView_title)).setVisibility(View.VISIBLE);
        	((TextView)V.findViewById(R.id.textView)).setVisibility(View.VISIBLE);
        	  TextView status = (TextView)V.findViewById(R.id.dw_status);
              status.setText(mi.get_file_status());
              status.setVisibility(View.VISIBLE);
        }
      
        
       
        
   
        return V;
    }
    
	
}
