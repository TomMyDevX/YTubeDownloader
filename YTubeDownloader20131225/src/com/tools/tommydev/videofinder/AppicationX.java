package com.tools.tommydev.videofinder;

import android.app.Application;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.tools.tommydev.videofinder.Object.Mp3List;

import java.util.ArrayList;

/**
 * Created by TomMy on 10/5/13.
 */
public class AppicationX extends Application {
	
	public static  String Prefer_Save="Youtube_downloader_tommy";
    ArrayList<Mp3List> mp3Lists;
	public static int text_color;
	public static int bg;
    @Override
    public void onCreate() {
        super.onCreate();
      
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.iconytube)
                .showImageForEmptyUri(R.drawable.iconytube)
                .resetViewBeforeLoading(true)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .delayBeforeLoading(500)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT) // default
                .build();

//		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
//        .cacheInMemory(true)
//        .cacheOnDisc(true)
//        .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
//        .defaultDisplayImageOptions(defaultOptions)
                .threadPriority(Thread.MIN_PRIORITY)
                .threadPoolSize(5)
                .defaultDisplayImageOptions(options)
                .build();
        ImageLoader.getInstance().init(config);
        
        
        String theme="windows8_color_black_white_";
        bg=getStringResourceByName(theme+"bg", "color");
        text_color=(getStringResourceByName(theme+"text_color", "color"));
        
    }

    public ArrayList<Mp3List> getMp3Lists() {
        return mp3Lists;
    }

    public void setMp3Lists(ArrayList<Mp3List> mp3Lists) {
        this.mp3Lists = mp3Lists;
    }

    @Override
    public void onLowMemory() {
        System.gc();
        super.onLowMemory();
    }
    
	private int getStringResourceByName(String aString,String type) {
	      String packageName = getPackageName();
	      int resId = getResources().getIdentifier(aString,type, packageName);
	      return resId;
	    }


}
