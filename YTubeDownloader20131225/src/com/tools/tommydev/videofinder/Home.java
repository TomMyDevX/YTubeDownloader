package com.tools.tommydev.videofinder;
import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v4.widget.SlidingPaneLayout.PanelSlideListener;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.inmobi.commons.InMobi;
import com.inmobi.commons.InMobi.LOG_LEVEL;
import com.inmobi.monetization.IMBanner;
import com.inmobi.monetization.IMBannerListener;
import com.inmobi.monetization.IMErrorCode;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tools.tommydev.videofinder.Adapter.MainPagerAdapter;
import com.tools.tommydev.videofinder.DataBaseHelper.DBClass_Downloader;
import com.tools.tommydev.videofinder.DataBaseHelper.DBClass_favor;
import com.tools.tommydev.videofinder.Fragment.Apps_Fragment;
import com.tools.tommydev.videofinder.TheadHelper.DownloadSreachList;
import com.tools.tommydev.videofinder.Utils.StorePrefer;
import com.tools.tommydev.videofinder.fm.download_fm;
import com.tools.tommydev.videofinder.fm.setting_fm;
import com.tools.tommydev.videofinder.fm.src_fm;
import com.tools.tommydev.videofinder.themewin8.Windows8_home;
import com.viewpagerindicator.CirclePageIndicator;

public class Home extends FragmentActivity  {


	private ImageView 			btn_src;
	private ImageView 			btn_setting;
	private ImageView 			btn_download;


	SharedPreferences 			settings;
	private SlidingPaneLayout 	mSlidingLayout;
	DownloadSreachList 			downloadSreachList;
	private MainPagerAdapter 	pagerAdapter;
	boolean 					p_state;
	IMBanner 					imbanner;
	private int 				data_size;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.a_start_app);
		StorePrefer.run_first_app(getApplicationContext());
	    settings = getSharedPreferences("Youtube_downloader_tommy", 0);
		
	    
	    // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.tools.tommydev.videofinder", 
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                }
        } catch (NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        
        
        
	   

	    

	    	//SyncUtils.CreateSyncAccount(this);
			String theme=settings.getString("theme", "Original Theme");
			if(theme.equals("Original Theme")){
				
			}else if(theme.equals("Windows Phone 8")){
				startActivity(new Intent(this, Windows8_home.class));
				finish();
			}
       
	    
	    
	    
	    
	    
		
		
		
		
		

		 mSlidingLayout = (SlidingPaneLayout) findViewById(R.id.sliding_pane_layout);
	     mSlidingLayout.openPane();
	     mSlidingLayout.setParallaxDistance(200);
	     
	     
	   //  if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){
	     
	  
	    	// mSlidingLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.ios_wallpaper_reddit));
	   //  }
	    
	     

			btn_src=(ImageView) findViewById(R.id.btn_src);
			btn_setting=(ImageView) findViewById(R.id.btn_setting);
			btn_download=(ImageView) findViewById(R.id.btn_download);
		

			
	
			btn_src.setOnClickListener(clickListener);
			btn_setting.setOnClickListener(clickListener);
			
		
	        if (findViewById(R.id.frview_a) != null) {
	            if (savedInstanceState != null) {
	                return;
	            }
	            src_fm firstFragment = new src_fm();
	            firstFragment.setArguments(getIntent().getExtras());
	            getSupportFragmentManager().beginTransaction().add(R.id.frview_a, firstFragment).commit();
	        }
			
			
			
			btn_download.setOnClickListener(clickListener);

		/*btn_facebook.setOnClickListener(clickListener);
		 	ImageView mImageView = (ImageView) findViewById(R.id.iv_photo);
		 	mImageView.setScaleType(ScaleType.CENTER_CROP);
		    final WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
	        final Drawable wallpaperDrawable = wallpaperManager.getDrawable();
	        mImageView.setImageDrawable(wallpaperDrawable);
	        //mAttacher = new PhotoViewAttacher(mImageView);
	       // mAttacher.setScaleType(ScaleType.CENTER_CROP);
	        * 
	        * 
	        * 
	        * 
*/
			
			  DBClass_favor dbClass_favor = new DBClass_favor(Home.this);
		      final List<DBClass_favor.sMembers> sMemberses = dbClass_favor.SelectAllData();
		      data_size=sMemberses.size();
			
			 pager = (ViewPager) findViewById(R.id.viewpager);
			 pagerAdapter = new MainPagerAdapter();
		     pager.setAdapter(pagerAdapter);
		     runOnUiThread(new Runnable() {
		            @Override
		            public void run() {
		                System.gc();
		                initialisePaging();


		            }
		        });
		 
		 
		     
		     mSlidingLayout.setPanelSlideListener(new PanelSlideListener() {
				
				@Override
				public void onPanelSlide(View arg0, float arg1) {
					// TODO Auto-generated method stub
				//	 System.out.println("Panel sliding : :");
				}
				
				@Override
				public void onPanelOpened(View arg0) {
					p_state=true;
					// TODO Auto-generated method stub
					 System.out.println("Panel opened");
					  DBClass_favor dbClass_favor = new DBClass_favor(Home.this);
				      final List<DBClass_favor.sMembers> sMemberses = dbClass_favor.SelectAllData();

					 if(data_size!=sMemberses.size()){
						  runOnUiThread(new Runnable() {
					            @Override
					            public void run() {
					                System.gc();
					                initialisePaging();
					                data_size=sMemberses.size();
					            }
					        });
					 }
						 
					
					  
					  
					  arg0.getId();
				}
				
				@Override
				public void onPanelClosed(View arg0) {
					p_state=false;
					// TODO Auto-generated method stub
					 System.out.println("Panel closed");
				}
			});
		     
		     new CountDownTimer(5000,1000) {
				
				@Override
				public void onTick(long millisUntilFinished) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onFinish() {
					     	InMobi.initialize(Home.this, "f7278216aa3b4cc394ad820f3b211e49");
					        imbanner = new IMBanner(Home.this, "f7278216aa3b4cc394ad820f3b211e49", IMBanner.INMOBI_AD_UNIT_320X50);
					        //RlativeLayout.LayoutParams params=new LayoutParams(728,60);
					        //imbanner.setLayoutParams(params);
					       
						    imbanner.loadBanner();
						   
					        InMobi.setLogLevel(LOG_LEVEL.DEBUG);
					        imbanner.setIMBannerListener(new IMBannerListener() {
								
								@Override
								public void onShowBannerScreen(IMBanner arg0) {
									// TODO Auto-generated method stub
									
								}
								
								@Override
								public void onLeaveApplication(IMBanner arg0) {
									// TODO Auto-generated method stub
									
								}
								
								@Override
								public void onDismissBannerScreen(IMBanner arg0) {
									// TODO Auto-generated method stub
									
								}
								
								@Override
								public void onBannerRequestSucceeded(IMBanner arg0) {
									// TODO Auto-generated method stub
									
								}
								
								@Override
								public void onBannerRequestFailed(IMBanner arg0, IMErrorCode arg1) {
									new CountDownTimer(15000,1000) {
										
										@Override
										public void onTick(long millisUntilFinished) {
											// TODO Auto-generated method stub
											
										}
										
										@Override
										public void onFinish() {
											imbanner.loadBanner();
										}
									}.start();
									   
									   
								}
								
								@Override
								public void onBannerInteraction(IMBanner arg0, Map<String, String> arg1) {
									// TODO Auto-generated method stub
									
								}
							});
					     }
				
			}.start();
		     
		     
		     
		     

	}
	
	   CirclePageIndicator circlePageIndicator;
	    ViewPager pager;
		private int backcount;
	
		
		
		
	@Override
	public void onBackPressed() {
		
		if(pager!=null&&circlePageIndicator!=null){
			
			if(pager.getChildCount()>=0){
				
			
		
	
		
	/*	if(p_state==false){
			mSlidingLayout.openPane();
			   return;
		}
		*/
		 if(mSlidingLayout!=null)
		 	 mSlidingLayout.openPane();
			 if (pager.getCurrentItem() >0) {
		            circlePageIndicator.setCurrentItem(0);
		            pager.setCurrentItem(0, true);
		            backcount = 0;
		            return;
		        }
			 
		        	
			 
			 
			 
			 
			 
		        
		                  if (backcount >= 2) { 
		                      super.onBackPressed();
		                      return;
		                  }
		              	backcount++;
		        
			
			}
		}
			
		
		
	}
	
	boolean x=false;
	String Fg_Name="";
	
	View.OnClickListener clickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			 
			btn_src=(ImageView) findViewById(R.id.btn_src);
			btn_setting=(ImageView) findViewById(R.id.btn_setting);
			btn_download=(ImageView) findViewById(R.id.btn_download);
			
		//	Log.e("Test",""+getResources().getResourceEntryName(v.getId()));
			
			if(v.getId()==R.id.btn_src&&Fg_Name!="src_fm"){
				//removeViewInFrame();
			
				btn_setting.setImageResource(R.drawable.generic_settings);
				btn_download.setImageResource(R.drawable.download);
				
				Fg_Name="src_fm";
				src_fm firstFragment = new src_fm();
	            firstFragment.setArguments(getIntent().getExtras());
	            getSupportFragmentManager().beginTransaction().replace(R.id.frview_a, firstFragment).commit();
			}else if(v.getId()==R.id.btn_setting&&Fg_Name!="setting_fm"){
				//removeViewInFrame();
				btn_setting.setImageResource(R.drawable.generic_settings_ck);
				btn_download.setImageResource(R.drawable.download);
				Fg_Name="setting_fm";
				setting_fm firstFragment = new setting_fm();
	            firstFragment.setArguments(getIntent().getExtras());
	            getSupportFragmentManager().beginTransaction().replace(R.id.frview_a, firstFragment).commit();
			}else if(v.getId()==R.id.btn_download&&Fg_Name!="webviewdownload_fm"){
				//removeViewInFrame();
				btn_download.setImageResource(R.drawable.download_ck);
				btn_setting.setImageResource(R.drawable.generic_settings);
				Fg_Name="webviewdownload_fm";
				download_fm firstFragment = new download_fm();
	            firstFragment.setArguments(getIntent().getExtras());
	            getSupportFragmentManager().beginTransaction().replace(R.id.frview_a, firstFragment).commit();
			}else if(v.getId()==R.id.btn_impo&&Fg_Name!="favor"){
				
				//removeViewInFrame();
				Fg_Name="favor";
				mSlidingLayout.closePane();
				mSlidingLayout.openPane();
			}
		
		}
	};
	
	public void removeViewInFrame(){
		FrameLayout frameLayout_a=(FrameLayout) findViewById(R.id.frview_a);
		frameLayout_a.removeAllViews();
	}
	
	 private void initialisePaging() {

	     


	        while (pagerAdapter.getCount() > 0) {
	            pagerAdapter.removeView(pager, 0);
	        }


	        int index = 0;
	        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        DBClass_favor dbClass_favor = new DBClass_favor(this);
	        final List<DBClass_favor.sMembers> sMemberses = dbClass_favor.SelectAllData();

	        List<Fragment> fragments = new Vector<Fragment>();
	     


	        int count = sMemberses.size() / 2;
	        Log.e("count", sMemberses.size() + "|" + count + "");

	//
//	        for(int i=0;i<sMemberses.size();i++){
//	            Log.e("count", sMemberses.get(i).get_datex()+":"+sMemberses.get(i).get_Status()+":"+sMemberses.get(i).get_Filename()+":"+sMemberses.get(i).get_Filesize()+":"+sMemberses.get(i).get_datex());
//	        }

	        for (int i = 0; i < count; i++) {


	            final int start = i * 2;
	            final int end = (i * 2) + 1;
	            fragments.add(new Apps_Fragment(sMemberses.get(start), sMemberses.get(end), this));

	            final View inflate = inflater.inflate(R.layout.fragment_sppa, null);
	            ImageView imageView1 = (ImageView) inflate.findViewById(R.id.imageView1);
	            ImageView imageView2 = (ImageView) inflate.findViewById(R.id.imageView2);
	            TextView textView1 = (TextView) inflate.findViewById(R.id.textView1);
	            TextView textView2 = (TextView) inflate.findViewById(R.id.textView2);
	            textView1.setText(sMemberses.get(start).get_Filename());
	            textView2.setText(sMemberses.get(end).get_Filename());


	            ImageLoader.getInstance().displayImage(sMemberses.get(start).get_img(), imageView1);
	            ImageLoader.getInstance().displayImage(sMemberses.get(end).get_img(), imageView2);

	            ImageView imageView1_play = (ImageView) inflate.findViewById(R.id.imageView1_play);
	            imageView1_play.setOnClickListener(new View.OnClickListener() {
	                @Override
	                public void onClick(View view) {
	                    DBClass_Downloader dbClass_downloader = new DBClass_Downloader(Home.this);
	                    String[] strings = dbClass_downloader.SelectData(sMemberses.get(start).get_Status());

	                    //  DBClass dbClass = new DBClass(Home.this);
	                    //  String path = Environment.getExternalStorageDirectory().toString() + "/download/" + sMemberses.get(start).get_Filename().replace("\"", "").replace("<", "").replace(">", "").replace("|", "").replace("\\", "").replace("/", "").replace("?", "").replace(":", "").replace("*", "");
	                  if(strings!=null){
	                    File f = new File(strings[3]);
						if (f.exists()) {
						    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(strings[3]));
						    intent.setDataAndType(Uri.parse(strings[3]), "video/*");
						    startActivity(Intent.createChooser(intent, "Choose Media Player"));
						} else {
						    startActivity(Intent.createChooser(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + sMemberses.get(start).get_Filesize())), "Choose Action"));
						}
	                  }else{
	                	  startActivity(Intent.createChooser(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + sMemberses.get(start).get_Filesize())), "Choose Action"));
	                  }
	                }
	            });
	            ImageView imageView2_play = (ImageView) inflate.findViewById(R.id.imageView2_play);
	            imageView2_play.setOnClickListener(new View.OnClickListener() {
	                @Override
	                public void onClick(View view) {
	                	if(sMemberses.size()>0){
	                    DBClass_Downloader dbClass_downloader = new DBClass_Downloader(Home.this);
	                    String[] strings = dbClass_downloader.SelectData(sMemberses.get(end).get_Status());

	                  
	                    //DBClass dbClass = new DBClass(Home.this);
	                    // String path = Environment.getExternalStorageDirectory().toString() + "/download/" + sMemberses.get(end).get_Filename().replace("\"", "").replace("<", "").replace(">", "").replace("|", "").replace("\\", "").replace("/", "").replace("?", "").replace(":", "").replace("*", "");
	                    //Log.e("path", path);
	                   if(strings!=null){
	                	   
	                 
	                    File f = new File(strings[3]);
						if (f.exists()) {
							  Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(strings[3]));
						      intent.setDataAndType(Uri.parse(strings[3]), "video/*");
						      startActivity(Intent.createChooser(intent, "Choose Media Player"));
						    
						} else {
						    startActivity(Intent.createChooser(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + sMemberses.get(end).get_Filesize())), "Choose Action"));
						}
	                   		}else{
	                   		  startActivity(Intent.createChooser(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + sMemberses.get(end).get_Filesize())), "Choose Action"));
	                   		}

	                }
	                }
	            });

	            imageView1.setOnLongClickListener(new View.OnLongClickListener() {
	                @Override
	                public boolean onLongClick(View view) {
	                    AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
	                    builder.setMessage("Do you want remove it?")
	                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	                                @Override
	                                public void onClick(DialogInterface dialogInterface, int ix) {
	                                    DBClass_favor dbClass_favor = new DBClass_favor(Home.this);
	                                    dbClass_favor.DeleteData(sMemberses.get(start).get_Filesize());
	                                    circlePageIndicator.setCurrentItem(0);
	                                    initialisePaging();
	                                }
	                            })
	                            .setNegativeButton("No", dialogClickListener).show();
	                    return false;
	                }
	            });
	            imageView2.setOnLongClickListener(new View.OnLongClickListener() {
	                @Override
	                public boolean onLongClick(View view) {
	                    AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
	                    builder.setMessage("Do you want remove it?")
	                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	                                @Override
	                                public void onClick(DialogInterface dialogInterface, int ix) {
	                                    DBClass_favor dbClass_favor = new DBClass_favor(Home.this);
	                                    dbClass_favor.DeleteData(sMemberses.get(end).get_Filesize());
	                                    circlePageIndicator.setCurrentItem(0);
	                                    initialisePaging();
	                                }
	                            })
	                            .setNegativeButton("No", dialogClickListener).show();
	                    return false;
	                }
	            });
	            pagerAdapter.addView(inflate, index++);


	        }

	        if (sMemberses.size() % 2 == 1) {
	            fragments.add(new Apps_Fragment(sMemberses.get(sMemberses.size() - 1), Home.this));
	            View inflate = inflater.inflate(R.layout.fragment_sppa, null);
	            ImageView imageView = (ImageView) inflate.findViewById(R.id.imageView1);
	            TextView textView1 = (TextView) inflate.findViewById(R.id.textView1);
	            textView1.setText(sMemberses.get(sMemberses.size() - 1).get_Filename());

	            ImageView imageView2_play = (ImageView) inflate.findViewById(R.id.imageView2_play);
	            imageView2_play.setVisibility(View.GONE);
	            ImageLoader.getInstance().displayImage(sMemberses.get(sMemberses.size() - 1).get_img(), imageView);
	            ImageView imageView1_play = (ImageView) inflate.findViewById(R.id.imageView1_play);
	            imageView1_play.setOnClickListener(new View.OnClickListener() {
	                @Override
	                public void onClick(View view) {
	                	if(sMemberses.size()>0){
	                    DBClass_Downloader dbClass_downloader = new DBClass_Downloader(Home.this);
	                    String[] strings = dbClass_downloader.SelectData(sMemberses.get(sMemberses.size() - 1).get_Status());


	                    // DBClass dbClass = new DBClass(Home.this);
	                    // String path = Environment.getExternalStorageDirectory().toString() + "/download/" + sMemberses.get(sMemberses.size() - 1).get_Filename().replace("\"", "").replace("<", "").replace(">", "").replace("|", "").replace("\\", "").replace("/", "").replace("?", "").replace(":", "").replace("*", "");
	                    // Log.e("path", path);

	                    if(strings!=null){
	                        File f = new File(strings[3]);
	                        if (f.exists()) {
	                        	  Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(strings[3]));
	                              intent.setDataAndType(Uri.parse(strings[3]), "video/*");
	                              startActivity(Intent.createChooser(intent, "Choose Media Player"));
	                        } else {
	                            startActivity(Intent.createChooser(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + sMemberses.get(sMemberses.size() - 1).get_Filesize())), "Choose Action"));
	                        }
	                   }else{
	                	   startActivity(Intent.createChooser(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + sMemberses.get(sMemberses.size() - 1).get_Filesize())), "Choose Action"));
	                   }

	                }}
	            });

	            imageView.setOnLongClickListener(new View.OnLongClickListener() {
	                @Override
	                public boolean onLongClick(View view) {
	                    AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
	                    builder.setMessage("Do you want remove it?")
	                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	                                @Override
	                                public void onClick(DialogInterface dialogInterface, int ix) {
	                                    DBClass_favor dbClass_favor = new DBClass_favor(Home.this);
	                                    dbClass_favor.DeleteData(sMemberses.get(sMemberses.size() - 1).get_Filesize());
	                                    circlePageIndicator.setCurrentItem(0);
	                                    initialisePaging();

	                                }
	                            })
	                            .setNegativeButton("No", dialogClickListener).show();
	                    return false;
	                }
	            });

	            pagerAdapter.addView(inflate, index++);
	        }
	        ;

	        circlePageIndicator = (CirclePageIndicator) findViewById(R.id.titles);
	        circlePageIndicator.setViewPager(pager);
	        
	        circlePageIndicator.setCurrentItem(0);
	        if (sMemberses.size() > 0) {
	            circlePageIndicator.setVisibility(View.VISIBLE);
	        } else {
	            circlePageIndicator.setVisibility(View.GONE);
	        }


	        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
	            @Override
	            public void onPageScrolled(int i, float v, int i2) {
	            	 Log.e("PageonPageScrolled", i + "|" + pagerAdapter.getCount());
	            }

	            @Override
	            public void onPageSelected(int i) {
	              
	                Log.e("PageonPageSelected", i + "|" + pagerAdapter.getCount());
	                circlePageIndicator.setCurrentItem(i);
	            }

	            @Override
	            public void onPageScrollStateChanged(int i) {
	            	 Log.e("PageonPageScrollStateChanged", i + "|" + pagerAdapter.getCount());
	            }
	        });
	    }
	 DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	            switch (which) {
	                case DialogInterface.BUTTON_POSITIVE:
	                    //Yes button clicked
	                    break;

	                case DialogInterface.BUTTON_NEGATIVE:
	                    //No button clicked
	                    break;
	            }
	        }
	    };
	    int page_index=0;
	    
	    @Override
	    protected void onResume() {
	        Log.e("onResume", "onResume");
	        super.onResume();
	        
	       
	        
	        new CountDownTimer(5000,1000) {
				
				@Override
				public void onTick(long millisUntilFinished) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onFinish() {
				    	  if(imbanner!=null){
				    		   imbanner.loadBanner();
				    	  }
				}
			}.start();
	        
	      
	      
	        
	      
	        
	        
	        Intent intent = new Intent(Home.this, Test_Downloader_By_service.class);
	        SharedPreferences settings = Home.this.getSharedPreferences("Youtube_downloader_tommy", 0);
	        boolean editor = settings.getBoolean("checkBox_download_with_service", false);
	        if(editor){
	        	   if (!isMyServiceRunning()) {
	                   Log.e("isMyServiceRunning", "false");
	                   Intent i = new Intent(Home.this, Test_Downloader_By_service.class);
	                   startService(i);
	               } else {
	                   Log.e("isMyServiceRunning", "true");
	               }
	            //startService(intent);
	        }else{
	        	   if (isMyServiceRunning()) {
	        		   stopService(intent);
	        	   }
	           
	        }

	        
	    
	        
	    }
	    private boolean isMyServiceRunning() {
	        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
	        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	            // Log.e("Service",Test_Downloader_By_service.class.getName()+"::"+service.service.getClassName());
	            if (Test_Downloader_By_service.class.getName().equals(service.service.getClassName())) {
	                return true;
	            }
	        }
	        return false;
	    }
	    
	    public static Bitmap cropImage(Bitmap img, Bitmap templateImage, int width, int height) {
	        // Merge two images together.
	        Bitmap bm = Bitmap.createBitmap(img.getWidth(), img.getHeight(), Bitmap.Config.ARGB_8888);
	        Canvas combineImg = new Canvas(bm);
	        combineImg.drawBitmap(img, 0f, 0f, null);
	        combineImg.drawBitmap(templateImage, 0f, 0f, null);
	        
	        // Create new blank ARGB bitmap.
	        Bitmap finalBm = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
	        
	        // Get the coordinates for the middle of combineImg.
	        int hMid = bm.getHeight() / 2;
	        int wMid = bm.getWidth() / 2;
	        int hfMid = finalBm.getHeight() / 2;
	        int wfMid = finalBm.getWidth() / 2;
	      
	        int y2 = hfMid;
	        int x2 = wfMid;
	        
	        // Top half of the template.
	        for (int y = hMid; y >= 0; y--) {
	            boolean template = false;
	            // Check Upper-left section of combineImg.
	            for (int x = wMid; x >= 0; x--) {
	                if (x2 < 0) {
	                    break;
	                }
	      
	                int px = bm.getPixel(x, y);
	                if (Color.red(px) == 234 && Color.green(px) == 157 && Color.blue(px) == 33) {
	                    template = true;
	                    finalBm.setPixel(x2, y2, Color.TRANSPARENT);
	                } else if (template) {
	                    finalBm.setPixel(x2, y2, Color.TRANSPARENT);
	                } else {
	                    finalBm.setPixel(x2, y2, px);
	                }
	                x2--;
	            }
	          
	            // Check upper-right section of combineImage.
	            x2 = wfMid;
	            template = false;
	            for (int x = wMid; x < bm.getWidth(); x++) {
	                if (x2 >= finalBm.getWidth()) {
	                    break;  
	                }
	        
	                int px = bm.getPixel(x, y);
	                if (Color.red(px) == 234 && Color.green(px) == 157 && Color.blue(px) == 33) {
	                    template = true;
	                    finalBm.setPixel(x2, y2, Color.TRANSPARENT);
	                } else if (template) {
	                    finalBm.setPixel(x2, y2, Color.TRANSPARENT);
	                } else {
	                    finalBm.setPixel(x2, y2, px);
	                }
	                x2++;
	            }
	      
	            // Once we reach the top-most part on the template line, set pixel value transparent
	            // from that point on.
	            int px = bm.getPixel(wMid, y);
	            if (Color.red(px) == 234 && Color.green(px) == 157 && Color.blue(px) == 33) {
	                for (int y3 = y2; y3 >= 0; y3--) {
	                    for (int x3 = 0; x3 < finalBm.getWidth(); x3++) {
	                        finalBm.setPixel(x3, y3, Color.TRANSPARENT);
	                    }
	                }
	                break;
	            }
	            x2 = wfMid;
	            y2--;
	        }
	      
	        x2 = wfMid;
	        y2 = hfMid;
	        // Bottom half of the template.
	        for (int y = hMid; y <= bm.getHeight(); y++) {
	            boolean template = false;
	            // Check bottom-left section of combineImage.
	            for (int x = wMid; x >= 0; x--) {
	                if (x2 < 0) {
	                    break;
	                }
	     
	                int px = bm.getPixel(x, y);
	                if (Color.red(px) == 234 && Color.green(px) == 157 && Color.blue(px) == 33) {
	                    template = true;
	                    finalBm.setPixel(x2, y2, Color.TRANSPARENT);
	                } else if (template) {
	                    finalBm.setPixel(x2, y2, Color.TRANSPARENT);
	                } else {
	                    finalBm.setPixel(x2, y2, px);
	                }
	                x2--;
	            }
	     
	            // Check bottom-right section of combineImage.
	            x2 = wfMid;
	            template = false;
	            for (int x = wMid; x < bm.getWidth(); x++) {
	                if (x2 >= finalBm.getWidth()) {
	                    break;
	                }
	      
	                int px = bm.getPixel(x, y);
	                if (Color.red(px) == 234 && Color.green(px) == 157 && Color.blue(px) == 33) {
	                    template = true;
	                    finalBm.setPixel(x2, y2, Color.TRANSPARENT);
	                } else if (template) {
	                    finalBm.setPixel(x2, y2, Color.TRANSPARENT);
	                } else {
	                    finalBm.setPixel(x2, y2, px);
	                }
	                x2++;
	            }
	     
	          // Once we reach the bottom-most part on the template line, set pixel value transparent
	          // from that point on.
	          int px = bm.getPixel(wMid, y);
	          if (Color.red(px) == 234 && Color.green(px) == 157 && Color.blue(px) == 33) {
	              for (int y3 = y2; y3 < finalBm.getHeight(); y3++) {
	                  for (int x3 = 0; x3 < finalBm.getWidth(); x3++) {
	                      finalBm.setPixel(x3, y3, Color.TRANSPARENT);
	                  }
	              }
	              break;
	          }
	     
	          x2 = wfMid;
	          y2++;
	      }
	      return finalBm;
	    }
	    

	    @Override
	    public void onPause() {
	        super.onPause();
	       
	    }
	    
	 

	    
}

