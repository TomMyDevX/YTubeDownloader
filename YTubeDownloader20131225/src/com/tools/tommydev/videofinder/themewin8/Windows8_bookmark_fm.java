package com.tools.tommydev.videofinder.themewin8;

import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;
import com.tools.tommydev.videofinder.R;
import com.tools.tommydev.videofinder.Adapter.CustomGridViewAdapter;
import com.tools.tommydev.videofinder.DataBaseHelper.DBClass_favor;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class Windows8_bookmark_fm extends Fragment{
	
	


	private CustomGridViewAdapter customGridAdapter;
	private SharedPreferences settings;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View inflate = inflater.inflate(R.layout.activity_windows8_bookmark_fm, container, false);
		GridView gridView=(GridView) inflate.findViewById(R.id.gridView1);
		
		 DBClass_favor dbClass_favor = new DBClass_favor(getActivity());
	     List<DBClass_favor.sMembers> sMemberses = dbClass_favor.SelectAllData();
	     gridView.setAdapter(new CustomGridViewAdapter(getActivity(),getActivity(), R.layout.windows8_grid_row_grid, sMemberses));

	     gridView.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), true, true));
	     
	     settings = getActivity().getSharedPreferences("Youtube_downloader_tommy", 0);
	     final int bg_color=settings.getInt("change_bg_color", -16777216);
	     inflate.setBackgroundColor(bg_color);
		
		return inflate;
	}

	
	 
}
