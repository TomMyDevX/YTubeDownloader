package com.tools.tommydev.videofinder.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tools.tommydev.videofinder.DataBaseHelper.DBClass_Downloader;
import com.tools.tommydev.videofinder.R;

import java.util.List;

/**
 * Created by TomMy on 10/13/13.
 */
public class Download_adapter extends BaseAdapter {
    List<DBClass_Downloader.File_Downloader> file_downloader;
    Activity activity;

    public Download_adapter(Activity activity, List<DBClass_Downloader.File_Downloader> file_downloader) {
        this.activity = activity;
        this.file_downloader = file_downloader;
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
            V = vi.inflate(R.layout.list_download, null);
        }
        DBClass_Downloader.File_Downloader mi = file_downloader.get(i);
        TextView title = (TextView)V.findViewById(R.id.dw_title);
        TextView status = (TextView)V.findViewById(R.id.dw_status);
        title.setText(mi.get_file_title());
        status.setText(mi.get_file_status());
        
     
        return V;
    }
    
	
}
