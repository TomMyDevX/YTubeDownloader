package com.tools.tommydev.videofinder.themewin8;

import com.tools.tommydev.videofinder.R;
import com.tools.tommydev.videofinder.fm.windows8_download_fm;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class Windows8_download extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_windows8__setting_);
		
		windows8_download_fm firstFragment = new windows8_download_fm();
        firstFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout1, firstFragment).commit();
        
        
        
	}
	
	@Override
	public void onBackPressed() {
		finish();
	}

}
