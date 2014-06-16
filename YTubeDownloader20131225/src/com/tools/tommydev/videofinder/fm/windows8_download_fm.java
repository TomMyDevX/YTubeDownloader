package com.tools.tommydev.videofinder.fm;


import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tools.tommydev.videofinder.Adapter.Windows8_Download_adapter;
import com.tools.tommydev.videofinder.DataBaseHelper.DBClass_Downloader;
import com.tools.tommydev.videofinder.UIHelper.AlertUIHelper;
import com.tools.tommydev.videofinder.R;


/**
 * Created by TomMy on 9/5/13.
 */
public class windows8_download_fm extends Fragment{




    ProgressBar progressBar;
	private EditText url;
	private Button button_go;
	private WebView webview;
	private ListView list_download;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.windows8_download_fm, container, false);
        
        list_download = (ListView) inflate.findViewById(R.id.list_download);
        
        
        
        

        DBClass_Downloader dbClass_downloader = new DBClass_Downloader(windows8_download_fm.this.getActivity());
        final List<DBClass_Downloader.File_Downloader> file_downloaders = dbClass_downloader.SelectAllData("1");
        Windows8_Download_adapter Download_adapter = new Windows8_Download_adapter(windows8_download_fm.this.getActivity(), file_downloaders,false);

       
            list_download.setAdapter(Download_adapter);
            //x_frame.setVisibility(View.GONE);
            list_download.setVisibility(View.VISIBLE);
            list_download.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    View viewx=view;
                    TextView textview=(TextView)view.findViewById(R.id.dw_title);
                    textview.setSelected(true);
                }
            });
            list_download.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(windows8_download_fm.this.getActivity());
                    builder.setTitle(file_downloaders.get(i).get_file_title())
                            .setItems(R.array.colors_array, new DialogInterface.OnClickListener() {
                                @Override
								public void onClick(DialogInterface dialog, int which) {
                                    // The 'which' argument contains the index position
                                    // of the selected item
                                    if (which == 0) {

                                        final EditText input = new EditText(windows8_download_fm.this.getActivity());
                                        final AlertDialog.Builder X = new AlertDialog.Builder(windows8_download_fm.this.getActivity())
                                                .setTitle("Rename To..")
                                                .setMessage(file_downloaders.get(i).get_file_title())
                                                .setView(input)
                                                .setPositiveButton("Rename", new DialogInterface.OnClickListener() {
                                                    @Override
													public void onClick(DialogInterface dialog, int whichButton) {
                                                        Editable value = input.getText();
                                                        Pattern pattern = Pattern.compile("[^/./\\:*?\"<>|]");
                                                        boolean b = pattern.matcher(value.toString()).find();
                                                        if (b) {

                                                            File from = new File(file_downloaders.get(i).get_file_save_path());
                                                            File to = new File(file_downloaders.get(i).get_file_save_path().replaceAll(file_downloaders.get(i).get_file_vid(), value.toString()));
                                                            from.renameTo(to);
                                                            DBClass_Downloader dbClass_downloader1 = new DBClass_Downloader(windows8_download_fm.this.getActivity());
                                                            dbClass_downloader1.UpdateData(file_downloaders.get(i).get_file_vid(), file_downloaders.get(i).get_file_type(), to.getAbsolutePath(), file_downloaders.get(i).get_file_size());
                                                            Log.e("Rename", "Rename");
                                                        } else {
                                                            new AlertUIHelper(windows8_download_fm.this.getActivity()).show("Can't rename with [^/./\\:*?\"<>|].", Gravity.BOTTOM, 500);
                                                        }

                                                    }
                                                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                    @Override
													public void onClick(DialogInterface dialog, int whichButton) {
                                                        // Do nothing.
                                                    }
                                                });
                                        X.show();
                                    } else if (which == 1) {
                                        new AlertDialog.Builder(windows8_download_fm.this.getActivity())
                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                .setTitle("Deleting..")
                                                .setMessage("Are you sure you want to delete " + file_downloaders.get(i).get_file_title() + " ?")
                                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        DBClass_Downloader dbClass_downloader1 = new DBClass_Downloader(windows8_download_fm.this.getActivity());
                                                        String[] strings = dbClass_downloader1.SelectData(file_downloaders.get(i).get_file_vid());
                                                        if (strings != null) {

                                                            File file_del = new File(strings[3]);
                                                            file_del.delete();
                                                            //MediaScannerConnection.scanFile(download_fm.this.getActivity(), new String[]{strings[3]}, null, null);
                                                            dbClass_downloader1.DeleteData(file_downloaders.get(i).get_file_vid());
                                                            DBClass_Downloader dbClass_downloader = new DBClass_Downloader(windows8_download_fm.this.getActivity());
                                                            final List<DBClass_Downloader.File_Downloader> file_downloaders = dbClass_downloader.SelectAllData("1");
                                                            Windows8_Download_adapter Download_adapter = new Windows8_Download_adapter(windows8_download_fm.this.getActivity(), file_downloaders,false);
                                                            list_download.setAdapter(Download_adapter);


                                                        } else {
                                                            new AlertUIHelper(windows8_download_fm.this.getActivity()).show("Error on delete.", Gravity.CENTER, 20);
                                                        }
                                                    }

                                                })
                                                .setNegativeButton("No", null)
                                                .show();
                                    } else if (which == 2) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(windows8_download_fm.this.getActivity());
                                        TextView textView = new TextView(windows8_download_fm.this.getActivity());
                                        textView.setText("[" + file_downloaders.get(i).get_file_save_path() + "]");

                                        builder.setView(textView)
                                                .setTitle("Specification Path")
                                                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        // sign in the user ...
                                                    }
                                                });

                                        builder.create();
                                        builder.show();
                                    } else if(which==3) {
                                        DBClass_Downloader dbClass_downloader=new DBClass_Downloader(windows8_download_fm.this.getActivity());
                                        String[] strings = dbClass_downloader.SelectData(file_downloaders.get(i).get_file_vid());
                                        if(strings!=null){
                                            File f = new File(strings[3]);
                                            if (f.exists()) {
                                            	try{
                                            		 Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(strings[3]));
                                                     intent.setDataAndType(Uri.parse(strings[3]), "video/*");
                                                     startActivity(intent);
                                            	}catch(Exception exception){
                                            		Toast.makeText(windows8_download_fm.this.getActivity(), "Not found media player.", Toast.LENGTH_SHORT).show();
                                            	}
                                               
                                            } else {
                                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + file_downloaders.get(i).get_file_vid())));
                                            }
                                        }else{
                                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + file_downloaders.get(i).get_file_vid())));
                                        }
                                    }

                                    Log.e("Seleteed", which + "");
                                }
                            });
                    builder.create().show();
                    return false;
                }


            });
        return inflate;
    }
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }


}
