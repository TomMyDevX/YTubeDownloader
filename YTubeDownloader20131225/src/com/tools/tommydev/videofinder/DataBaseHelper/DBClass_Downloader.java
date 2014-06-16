package com.tools.tommydev.videofinder.DataBaseHelper;

/**
 * Created by TomMy on 8/8/13.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class DBClass_Downloader extends SQLiteOpenHelper {


    // Database Version
    private static final int DATABASE_VERSION =1;

    // Database Name
    private static final String DATABASE_NAME = "youtubedownloader_Q";

    // Table Name
    private static final String TABLE_MEMBER = "downloader";

    public DBClass_Downloader(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        // Create Table Name
        db.execSQL("CREATE TABLE " + TABLE_MEMBER +
                "(file_vid TEXT(255) PRIMARY KEY," +
                " file_title TEXT(255)," +
                " file_type TEXT(255)," +
                " file_save_path TEXT(255)," +
                " file_size TEXT(255)," +
                " file_create TEXT(255)," +
                " file_status TEXT(255)" +
                ");");

        Log.e("CREATE TABLE","Create Table Successfully.");
    }



    // Insert Data
    public long InsertData(String file_vid,String file_title,String file_type,String file_save_path,String file_size,String file_create,String file_status) {
        // TODO Auto-generated method stub

        try {
            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            /**
             *  for API 11 and above
             SQLiteStatement insertCmd;
             String strSQL = "INSERT INTO " + TABLE_MEMBER
             + "(MemberID,Name) VALUES (?,?)";

             insertCmd = db.compileStatement(strSQL);
             insertCmd.bindString(1, strMemberID);
             insertCmd.bindString(2, strName);
             return insertCmd.executeInsert();
             */





            ContentValues Val = new ContentValues();
            Val.put("file_vid", file_vid);
            Val.put("file_title", file_title);
            Val.put("file_type", file_type);
            Val.put("file_save_path", file_save_path);
            Val.put("file_size", file_size);
            Val.put("file_create", file_create);
            Val.put("file_status", file_status);

            Log.e("Add_Queue",file_vid+"::"+file_title+"::"+file_type+"::"+file_save_path+"::"+file_size+"::"+file_create+"::"+file_status);

            long rows = db.insert(TABLE_MEMBER, null, Val);

            db.close();
            return rows; // return rows inserted.

        } catch (Exception e) {
            Log.e("Add_Queue","ERROR",e);
            return -1;
        }

    }


    // Select Data
    public String[] SelectData(String file_vid) {

        try {
            String arrData[] = null;

            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            Cursor cursor = db.query(TABLE_MEMBER, new String[] { "*" },
                    "file_vid=?",
                    new String[] { String.valueOf(file_vid) }, null, null, null, null);

            if(cursor != null)
            {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getColumnCount()];
                    /***
                     *  0 = MemberID
                     *  1 = Name
                     *  2 = Tel
                     */
                    arrData[0] = cursor.getString(0);
                    arrData[1] = cursor.getString(1);
                    arrData[2] = cursor.getString(2);
                    arrData[3] = cursor.getString(3);
                    arrData[4] = cursor.getString(4);
                    arrData[5] = cursor.getString(5);
                    arrData[6] = cursor.getString(6);
                }
            }
            cursor.close();
            db.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }

    }
    // TODO Auto-generated method stub


    // Select All Data
    public class File_Downloader {



        String _file_vid;
        String _file_title;
        String _file_type;
        String _file_save_path;
        String _file_size;
        String _file_create;
        String _file_status;

        public String get_file_create() {
            return _file_create;
        }

        public void set_file_create(String _file_create) {
            this._file_create = _file_create;
        }

        public String get_file_vid() {
            return _file_vid;
        }

        public void set_file_vid(String _file_vid) {
            this._file_vid = _file_vid;
        }

        public String get_file_title() {
            return _file_title;
        }

        public void set_file_title(String _file_title) {
            this._file_title = _file_title;
        }

        public String get_file_type() {
            return _file_type;
        }

        public void set_file_type(String _file_type) {
            this._file_type = _file_type;
        }

        public String get_file_save_path() {
            return _file_save_path;
        }

        public void set_file_save_path(String _file_save_path) {
            this._file_save_path = _file_save_path;
        }

        public String get_file_size() {
            return _file_size;
        }

        public void set_file_size(String _file_size) {
            this._file_size = _file_size;
        }

        public String get_file_status() {
            return _file_status;
        }

        public void set_file_status(String _file_status) {
            this._file_status = _file_status;
        }
    }



    public List<File_Downloader> SelectAllData(String condition) {
        // TODO Auto-generated method stub

        try {
            List<File_Downloader> MemberList = new ArrayList<File_Downloader>();

            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT  * FROM " + TABLE_MEMBER+" where "+condition+" ORDER BY file_create DESC";
            Cursor cursor = db.rawQuery(strSQL, null);

            if(cursor != null)
            {
                if (cursor.moveToFirst()) {
                    do {
                        File_Downloader cMember = new File_Downloader();
                        cMember.set_file_vid(cursor.getString(0));
                        cMember.set_file_title(cursor.getString(1));
                        cMember.set_file_type(cursor.getString(2));
                        cMember.set_file_save_path(cursor.getString(3));
                        cMember.set_file_size(cursor.getString(4));
                        cMember.set_file_create(cursor.getString(5));
                        cMember.set_file_status(cursor.getString(6));
                        MemberList.add(cMember);
                    } while (cursor.moveToNext());
                }
            }
            cursor.close();
            db.close();
            return MemberList;

        } catch (Exception e) {
            return null;
        }

    }

//    // Update Data
    public long UpdateData(String file_vid,String file_type,String file_save_path,String file_size){
        // TODO Auto-generated method stub

        try {

            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data
            ContentValues Val = new ContentValues();
            Val.put("file_type", file_type);
            Val.put("file_save_path", file_save_path);
            Val.put("file_size", file_size);

            long rows = db.update(TABLE_MEMBER, Val, " file_vid = ?",
                    new String[] { String.valueOf(file_vid) });
            Log.e("ChangeFileStatus","file_vid:"+file_vid+"|"+"file_type:"+file_type+"|"+"file_save_path:"+file_save_path+"|"+"file_size:"+file_size);
            db.close();
            return rows; // return rows updated.

        } catch (Exception e) {
            return -1;
        }

    }


    //    // Update Data
    public long UpdateDownloadData(String file_vid,String file_status) {
        // TODO Auto-generated method stub

        try {

            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            /**
             *  for API 11 and above
             SQLiteStatement insertCmd;
             String strSQL = "UPDATE " + TABLE_MEMBER
             + " SET Name = ? "
             + " , Tel = ? "
             + " WHERE MemberID = ? ";

             insertCmd = db.compileStatement(strSQL);
             insertCmd.bindString(1, strName);
             insertCmd.bindString(2, strTel);
             insertCmd.bindString(3, strMemberID);

             return insertCmd.executeUpdateDelete();
             *
             */
            ContentValues Val = new ContentValues();
            Val.put("file_status", file_status);

            long rows = db.update(TABLE_MEMBER, Val, " file_vid = ?",
                    new String[] { String.valueOf(file_vid) });
            Log.e("ChangeFileStatus",file_vid+":"+file_status);
            db.close();
            return rows; // return rows updated.

        } catch (Exception e) {
            return -1;
        }

    }



    // Delete Data
    public long DeleteData(String file_vid) {
        // TODO Auto-generated method stub

        try {

            SQLiteDatabase db;
            db = this.getWritableDatabase(); // Write Data

            /**
             * for API 11 and above
             SQLiteStatement insertCmd;
             String strSQL = "DELETE FROM " + TABLE_MEMBER
             + " WHERE MemberID = ? ";

             insertCmd = db.compileStatement(strSQL);
             insertCmd.bindString(1, strMemberID);

             return insertCmd.executeUpdateDelete();
             *
             */

            long rows =  db.delete(TABLE_MEMBER, "file_vid = ?",
                    new String[] { String.valueOf(file_vid) });

            db.close();
            return rows; // return rows delete.

        } catch (Exception e) {
            return -1;
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEMBER);

        // Re Create on method  onCreate
        onCreate(db);
    }



    public boolean SelectDataWithName(String Filename) {
        // TODO Auto-generated method stub
    boolean hasFile=false;
      //  Log.e("R2P",Filename);



        String path = Environment.getExternalStorageDirectory().toString()+"/download/"+Filename;
        File f = new File(path);
        hasFile=f.exists();
        if(hasFile){
            if(f.length()<=0){
                hasFile=false;
            }else{
                hasFile=true;
            }

        }else{
            hasFile=false;
        }



    return  hasFile;

    }

    public String[] SelectDataWithNameArray(String Filename) {
        // TODO Auto-generated method stub

        try {
            String arrData[] = null;

            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            Cursor cursor = db.query(TABLE_MEMBER, new String[] { "*" },
                    "Filename=?",
                    new String[] { String.valueOf(Filename) }, null, null, null, null);

            if(cursor != null)
            {
                if (cursor.moveToFirst()) {
                    arrData = new String[cursor.getColumnCount()];
                    /***
                     *  0 = MemberID
                     *  1 = Name
                     *  2 = Tel
                     */
                    arrData[0] = cursor.getString(0);
                    arrData[1] = cursor.getString(1);
                    arrData[2] = cursor.getString(2);
                    arrData[3] = cursor.getString(3);
                    arrData[4] = cursor.getString(4);
                }}
            cursor.close();
            db.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }

    }

    
    public String SelectAllDataJSON(String condition) {
        // TODO Auto-generated method stub
    	String x="";
        try {
          

            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT  * FROM " + TABLE_MEMBER+" where "+condition+" ORDER BY file_create DESC";
            Cursor cursor = db.rawQuery(strSQL, null);

            if(cursor != null)
            {
                if (cursor.moveToFirst()) {
                	
                	JSONArray jsonArray=new JSONArray();
                	
                    do {
                    	
                    	JSONObject jsonObject=new JSONObject();
                    	jsonObject.put("file_vid", cursor.getString(0));
                    	jsonObject.put("file_title",cursor.getString(1));
                    	jsonObject.put("set_file_type", cursor.getString(2));
                    	jsonObject.put("set_file_save_path",cursor.getString(3));
                    	jsonObject.put("set_file_size",cursor.getString(4));
                    	jsonObject.put("set_file_create",cursor.getString(5));
                    	jsonObject.put("set_file_status", cursor.getString(6));
                    	jsonArray.put((jsonObject));

                    } while (cursor.moveToNext());
                    x=jsonArray.toString();
                }
            }
            cursor.close();
            db.close();
            return x;

        } catch (Exception e) {
            return null;
        }

    }

    
    
}