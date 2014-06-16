package com.tools.tommydev.videofinder.themewin8;

import com.tools.tommydev.videofinder.R;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class Windows8_bookmark_activity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_windows8__setting_);
		
		
		
		Windows8_bookmark_fm firstFragment = new Windows8_bookmark_fm();
        firstFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout1, firstFragment).commit();
        
        
        
	}
	
	@Override
	public void onBackPressed() {
		finish();
	}


}
