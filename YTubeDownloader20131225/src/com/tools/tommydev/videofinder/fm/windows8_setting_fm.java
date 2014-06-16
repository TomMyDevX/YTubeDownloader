package com.tools.tommydev.videofinder.fm;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.margaritov.preference.colorpicker.ColorPickerDialog;
import net.margaritov.preference.colorpicker.ColorPickerDialog.OnColorChangedListener;

import org.apache.commons.lang3.ArrayUtils;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tools.tommydev.videofinder.R;
import com.tools.tommydev.videofinder.Test_Downloader_By_service;

/**
 * Created by TomMy on 9/5/13.
 */

public class windows8_setting_fm extends Fragment{

	int [] textView=new int[]{R.id.windows8_setting_theme_label,
			R.id.windows8_setting_change_bg_color_label,
			R.id.windows8_setting_change_text_color_label,
			R.id.windows8_setting_maxlimits_label,
			R.id.windows8_setting_dwquality_label,
			R.id.disk_save_location,
			R.id.radioButton_fhq,
			R.id.radioButton_hq,
			R.id.radioButton_mq,
			R.id.radioButton_lq,
			R.id.windows8_setting_dwquality_detail_label,
			R.id.windows8_setting_saveto_label,
			R.id.windows8_setting_savewithid_label,
			R.id.windows8_setting_dwwithservices_label};
	
	
	
	
	
    private SharedPreferences settings;
	private CheckBox checkBox_save_video_with_id;
	private CheckBox checkBox_change_log_show;
	private CheckBox checkBox_download_with_service;
	String[] arr = { "Original Theme", "Windows Phone 8"};
	private LinearLayout windows8_root_setting_layout;
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View inflate = inflater.inflate(R.layout.windows8_setting, container, false);
        settings = getActivity().getSharedPreferences("Youtube_downloader_tommy", 0);
        

    	
        windows8_root_setting_layout=((LinearLayout) inflate.findViewById(R.id.windows8_root_setting_layout));
		
		String theme=settings.getString("theme", "Original Theme");
    	
        Button change_theme=((Button) inflate.findViewById(R.id.change_theme));
        change_theme.setText(theme+"");
        change_theme.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final CharSequence[] items = {"Original Theme", "Windows Phone 8"};

				AlertDialog.Builder builder = new AlertDialog.Builder(windows8_setting_fm.this.getActivity());
				builder.setTitle("Change Theme");
				builder.setIcon(R.drawable.iconytube);
				builder.setItems(items, new DialogInterface.OnClickListener() {
				    @Override
					public void onClick(DialogInterface dialog, int item) {
				 Toast.makeText(getActivity(),"Theme will be displayed the next time.", Toast.LENGTH_SHORT).show();
					SharedPreferences settings = getActivity().getSharedPreferences("Youtube_downloader_tommy", 0);
	                SharedPreferences.Editor editor = settings.edit();
	                editor.putString("theme",items[item].toString());
	                editor.commit();
				    }
				});
				AlertDialog alert = builder.create();

				alert.show();
			}
		});
        
        
        
		final int bg_color=settings.getInt("change_bg_color", -16777216);
        final Button change_bg_color=((Button) inflate.findViewById(R.id.change_bg_color));
        change_bg_color.setBackgroundColor(bg_color);
        windows8_root_setting_layout.setBackgroundColor(bg_color);
        change_bg_color.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ColorPickerDialog colorPickerDialog=new ColorPickerDialog(getActivity(),(bg_color));
				colorPickerDialog.setOnColorChangedListener(new OnColorChangedListener() {
					
					@Override
					public void onColorChanged(int color) {
						Log.e("Color",color+"");
						
						SharedPreferences settings = getActivity().getSharedPreferences("Youtube_downloader_tommy", 0);
		                SharedPreferences.Editor editor = settings.edit();
		                editor.putInt("change_bg_color",color);
		                
		                editor.commit();
		                change_bg_color.setBackgroundColor(color);
		                windows8_root_setting_layout.setBackgroundColor(color);
					}
				});
				colorPickerDialog.show();
			}
		});
        
        
        
       
        final int text_color=settings.getInt("change_text_color", -1);
        for(int i=0;i<textView.length;i++){
        	try{
        		((TextView)inflate.findViewById(textView[i])).setTextColor(text_color);
        	}catch(Exception e){
        		((RadioButton)inflate.findViewById(textView[i])).setTextColor(text_color);
        	}
        	
        	
        }
        final Button change_text_color=((Button) inflate.findViewById(R.id.change_text_color));
        change_text_color.setBackgroundColor(text_color);
        change_text_color.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ColorPickerDialog colorPickerDialog=new ColorPickerDialog(getActivity(),(text_color));
				colorPickerDialog.setOnColorChangedListener(new OnColorChangedListener() {
					
					@Override
					public void onColorChanged(int color) {
						Log.e("Color",color+"");
						SharedPreferences settings = getActivity().getSharedPreferences("Youtube_downloader_tommy", 0);
		                SharedPreferences.Editor editor = settings.edit();
		                editor.putInt("change_text_color",color);
		                editor.commit();
		                change_text_color.setBackgroundColor(color);
		             
		                for(int i=0;i<textView.length;i++){
		                	((TextView)getActivity().findViewById(textView[i])).setTextColor(color);
		                }
					}
				});
				colorPickerDialog.show();
			}
		});

		

        
        
        
        ((SeekBar) inflate.findViewById(R.id.seekBar)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                // seek_text_var.setText((i + 1) + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                SharedPreferences settings = getActivity().getSharedPreferences("Youtube_downloader_tommy", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("seek_text_var", seekBar.getProgress() + 1);
                editor.commit();


            }
        });
     
        int seek_text_var = settings.getInt("seek_text_var", 15);
        ((TextView) inflate.findViewById(R.id.seek_text_var)).setText(seek_text_var + "");
        ((SeekBar) inflate.findViewById(R.id.seekBar)).setProgress(seek_text_var - 1);

        ((CheckBox) inflate.findViewById(R.id.show_thumb)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences settings = getActivity().getSharedPreferences("Youtube_downloader_tommy", 0);
                SharedPreferences.Editor editor = settings.edit();
                if (((CheckBox) inflate.findViewById(R.id.show_thumb)).isChecked()) {
                    editor.putBoolean("show_thumb", true);

                } else {
                    editor.putBoolean("show_thumb", false);
                }
                editor.commit();
            }
        });

        String quality = settings.getString("Quality", "mq");
        if (quality.equals("fhq")) {
            ((RadioButton) inflate.findViewById(R.id.radioButton_fhq)).setChecked(true);
        } else if (quality.equals("hq")) {
            ((RadioButton) inflate.findViewById(R.id.radioButton_hq)).setChecked(true);
        } else if (quality.equals("mq")) {
            ((RadioButton) inflate.findViewById(R.id.radioButton_mq)).setChecked(true);
        } else if (quality.equals("lq")) {
            ((RadioButton)inflate. findViewById(R.id.radioButton_lq)).setChecked(true);
        }

        ((RadioButton) inflate.findViewById(R.id.radioButton_fhq)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences settings = getActivity().getSharedPreferences("Youtube_downloader_tommy", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("Quality", "fhq");
                editor.commit();
            }
        });
        ((RadioButton) inflate.findViewById(R.id.radioButton_hq)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences settings = getActivity().getSharedPreferences("Youtube_downloader_tommy", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("Quality", "hq");
                editor.commit();
            }
        });
        ((RadioButton) inflate.findViewById(R.id.radioButton_mq)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences settings = getActivity().getSharedPreferences("Youtube_downloader_tommy", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("Quality", "mq");
                editor.commit();
            }
        });
        ((RadioButton) inflate.findViewById(R.id.radioButton_lq)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences settings = getActivity().getSharedPreferences("Youtube_downloader_tommy", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("Quality", "lq");
                editor.commit();
            }
        });

        checkBox_save_video_with_id= (CheckBox) inflate.findViewById(R.id.checkBox_save_video_with_id);

        SharedPreferences settings = getActivity().getSharedPreferences("Youtube_downloader_tommy", 0);
        checkBox_save_video_with_id.setChecked(settings.getBoolean("checkBox_save_video_with_id", false));

        checkBox_save_video_with_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences settings = getActivity().getSharedPreferences("Youtube_downloader_tommy", 0);
                SharedPreferences.Editor editor = settings.edit();
                    if(checkBox_save_video_with_id.isChecked()){
                        editor.putBoolean("checkBox_save_video_with_id", true);
                        editor.commit();
                    }else{

                        editor.putBoolean("checkBox_save_video_with_id", false);
                        editor.commit();
                    }
            }
        });



        checkBox_change_log_show= (CheckBox) inflate.findViewById(R.id.checkBox_change_log_show);
        checkBox_change_log_show.setChecked(settings.getBoolean("checkBox_disable_change_log_show",false));

        checkBox_change_log_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences settings = getActivity().getSharedPreferences("Youtube_downloader_tommy", 0);
                SharedPreferences.Editor editor = settings.edit();
                if(checkBox_save_video_with_id.isChecked()){
                    editor.putBoolean("checkBox_disable_change_log_show", true);
                    editor.commit();
                }else{
                    editor.putBoolean("checkBox_disable_change_log_show", false);
                    editor.commit();
                }
            }
        });
        
        checkBox_download_with_service= (CheckBox) inflate.findViewById(R.id.checkBox_download_with_service);
        checkBox_download_with_service.setChecked(settings.getBoolean("checkBox_download_with_service",false));

        checkBox_download_with_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	Intent intent = new Intent(windows8_setting_fm.this.getActivity(), Test_Downloader_By_service.class);
                SharedPreferences settings = windows8_setting_fm.this.getActivity().getSharedPreferences("Youtube_downloader_tommy", 0);
                SharedPreferences.Editor editor = settings.edit();
                if(checkBox_download_with_service.isChecked()){
                    editor.putBoolean("checkBox_download_with_service", true);
                    editor.commit();
                    windows8_setting_fm.this.getActivity().startService(intent);
                    
                }else{
                    editor.putBoolean("checkBox_download_with_service", false);
                    editor.commit();
                    windows8_setting_fm.this.getActivity().stopService(intent);
                    
                }
            }
        });
        
        
        TextView disk_save_location=(TextView)inflate.findViewById(R.id.disk_save_location);
        //  SharedPreferences settings = context.getSharedPreferences("Youtube_downloader_tommy", 0);
          disk_save_location.setText(settings.getString("disk_save_location", Environment.getExternalStorageDirectory().toString()));

          Button button_edit_disk_save_location=(Button)inflate.findViewById(R.id.button_edit_disk_save_location);
          button_edit_disk_save_location.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  HashSet storageSet = getStorageSet();
                  Object[] objects = storageSet.toArray();
                  String[] stringArray = Arrays.copyOf(objects, objects.length, String[].class);
                  String[] storageDirectories = getStorageDirectories();
                  String[] both = ArrayUtils.addAll(storageDirectories, stringArray);
                  AlertDialog.Builder builderSingle = new AlertDialog.Builder( windows8_setting_fm.this.getActivity());
                  builderSingle.setIcon(R.drawable.content_save);
                  builderSingle.setTitle("Select Save Location");
                  final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(  windows8_setting_fm.this.getActivity(),R.layout.list_save_location);
                  for(int i=0;i<both.length;i++){
                      arrayAdapter.add(both[i]);
                  }

                  builderSingle.setNegativeButton("Cancel",
                          new DialogInterface.OnClickListener() {

                              @Override
                              public void onClick(DialogInterface dialog, int which) {
                                  dialog.dismiss();
                              }
                          });

                  builderSingle.setAdapter(arrayAdapter,
                          new DialogInterface.OnClickListener() {

                              @Override
                              public void onClick(DialogInterface dialog, int which) {
                                  final String strName = arrayAdapter.getItem(which);
                                  AlertDialog.Builder builderInner = new AlertDialog.Builder( windows8_setting_fm.this.getActivity());
                                  builderInner.setMessage(strName);
                                  builderInner.setTitle("Change Save Location To");
                                  builderInner.setPositiveButton("Ok",
                                          new DialogInterface.OnClickListener() {

                                              @Override
                                              public void onClick( DialogInterface dialog,int which) {
                                                  SharedPreferences settings = windows8_setting_fm.this.getActivity().getApplicationContext().getSharedPreferences("Youtube_downloader_tommy", 0);
                                                  SharedPreferences.Editor editor = settings.edit();
                                                  editor.putString("disk_save_location",strName);
                                                  editor.commit();
                                                  Log.e("disk_save_location",strName);

                                                  settings =  windows8_setting_fm.this.getActivity().getApplicationContext().getSharedPreferences("Youtube_downloader_tommy", 0);
                                                  //settings.getString("disk_save_location", Environment.getExternalStorageDirectory().toString())
                                                  TextView disk_save_location=(TextView)inflate.findViewById(R.id.disk_save_location);
                                                  Log.e("disk_save_location",settings.getString("disk_save_location", Environment.getExternalStorageDirectory().toString()));

                                                  disk_save_location.setText(settings.getString("disk_save_location", Environment.getExternalStorageDirectory().toString()));

                                                 

                                                  dialog.dismiss();
                                              }
                                          });
                                  builderInner.show();
                              }
                          });
                  builderSingle.show();
              }
          });

        
        
        return inflate;
    }

	 public static HashSet getStorageSet(){
	        HashSet storageSet = getStorageSet(new File("/system/etc/vold.fstab"));
	        if (storageSet == null || storageSet.isEmpty()) {
	            storageSet = new HashSet();
	            storageSet.add(Environment.getExternalStorageDirectory().getAbsolutePath());
	        }
	        return storageSet;
	    }

	    public static HashSet getStorageSet(File file) {
	        HashSet storageSet = new HashSet();
	        BufferedReader reader = null;
	        try {
	            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
	            String line;
	            while ((line = reader.readLine()) != null) {
	                HashSet _storage = parse(line);
	                if(_storage==null)
	                    continue;
	                storageSet.addAll(_storage);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }finally{
	            try {
	                reader.close();
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	            reader = null;
	        }
	/*
	* set default external storage
	*/
	        storageSet.add(Environment.getExternalStorageDirectory().getAbsolutePath());
	        return storageSet;
	    }

	    private static HashSet parse(String str) {
	        if (str == null)
	            return null;
	        if (str.length()==0)
	            return null;
	        if (str.startsWith("#"))
	            return null;
	        HashSet storageSet = new HashSet();
	/*
	* dev_mount sdcard /mnt/sdcard auto /devices/platform/msm_sdcc.1/mmc_host
	* dev_mount SdCard /mnt/sdcard/extStorages /mnt/sdcard/extStorages/SdCard auto sd /devices/platform/s3c-sdhci.2/mmc_host/mmc1
	*/
	        Pattern patter1 = Pattern.compile("(/mnt/[^ ]+?)((?=[ ]+auto[ ]+)|(?=[ ]+(\\d*[ ]+)))");
	/*
	* dev_mount ins /mnt/emmc emmc /devices/platform/msm_sdcc.3/mmc_host
	*/
	        Pattern patter2 = Pattern.compile("(/mnt/.+?[ ]+)");
	        Matcher matcher1 = patter1.matcher(str);
	        boolean b1 = matcher1.find();
	        if (b1) {
	            String _group = matcher1.group(1);
	            storageSet.add(_group);
	        }

	        Matcher matcher2 = patter2.matcher(str);
	        boolean b2 = matcher2.find();
	        if (!b1 && b2) {
	            String _group = matcher2.group(1);
	            storageSet.add(_group);
	        }
	        return storageSet;
	    }


	    public static String[] getStorageDirectories()
	    {
	        String[] dirs = null;
	        BufferedReader bufReader = null;
	        try {
	            bufReader = new BufferedReader(new FileReader("/proc/mounts"));
	            ArrayList list = new ArrayList();
	            String line;
	            while ((line = bufReader.readLine()) != null) {
	                if (line.contains("vfat") || line.contains("/mnt")) {
	                    StringTokenizer tokens = new StringTokenizer(line, " ");
	                    String s = tokens.nextToken();
	                    s = tokens.nextToken(); // Take the second token, i.e. mount point

	                    if (s.equals(Environment.getExternalStorageDirectory().getPath())) {
	                        list.add(s);
	                    }
	                    else if (line.contains("/dev/block/vold")) {
	                        if (!line.contains("/mnt/secure") && !line.contains("/mnt/asec") && !line.contains("/mnt/obb") && !line.contains("/dev/mapper") && !line.contains("tmpfs")) {
	                            list.add(s);
	                        }
	                    }
	                }
	            }

	            dirs = new String[list.size()];
	            for (int i = 0; i < list.size(); i++) {
	                dirs[i] = list.get(i).toString();
	            }
	        }
	        catch (FileNotFoundException e) {}
	        catch (IOException e) {}
	        finally {
	            if (bufReader != null) {
	                try {
	                    bufReader.close();
	                }
	                catch (IOException e) {}
	            }

	            return dirs;
	        }}


}
