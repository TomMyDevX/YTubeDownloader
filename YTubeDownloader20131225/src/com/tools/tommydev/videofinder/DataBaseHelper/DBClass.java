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

public class DBClass extends SQLiteOpenHelper {


    // Database Version
    private static final int DATABASE_VERSION =2;

    // Database Name
    private static final String DATABASE_NAME = "youtubedownloader";

    // Table Name
    private static final String TABLE_MEMBER = "favor";

    public DBClass(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        // Create Table Name
        db.execSQL("CREATE TABLE " + TABLE_MEMBER +
                "(MemberID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " Name TEXT(255)," +
                " Filename TEXT(255)," +
                " status TEXT(255)," +
                " Filesize TEXT(255)" +
                ");");

        Log.d("CREATE TABLE","Create Table Successfully.");
    }

    // Insert Data
    public long InsertData(String strName,String Filename,String status,String filesize) {
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
            Val.put("Name", strName);
            Val.put("Filename", Filename);
            Val.put("status", status);
            Val.put("filesize", filesize);

            long rows = db.insert(TABLE_MEMBER, null, Val);

            db.close();
            return rows; // return rows inserted.

        } catch (Exception e) {
            return -1;
        }

    }


    // Select Data
    public String[] SelectData(String strMemberID) {
        // TODO Auto-generated method stub

        try {
            String arrData[] = null;

            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            Cursor cursor = db.query(TABLE_MEMBER, new String[] { "*" },
                    "MemberID=?",
                    new String[] { String.valueOf(strMemberID) }, null, null, null, null);

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
                }
            }
            cursor.close();
            db.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }

    }


    // Select All Data
    public class sMembers {
        String _MemberID;
        String _Name;
        String _Tel;
        String _Status;
        String _Filesize;

        public String get_Filesize() {
            return _Filesize;
        }

        public void set_Filesize(String _Filesize) {
            this._Filesize = _Filesize;
        }

        public String get_Filename() {
            return _Filename;
        }

        public void set_Filename(String _Filename) {
            this._Filename = _Filename;
        }

        String _Filename;

        // Set Value
        public void sMemberID(String vMemberID){
            this._MemberID = vMemberID;
        }
        public void sName(String vName){
            this._Name = vName;
        }


        // Get Value
        public String gMemberID(){
            return _MemberID;
        }
        public String gName(){
            return _Name;
        }

        public String get_Status() {
            return _Status;
        }

        public void set_Status(String _Status) {
            this._Status = _Status;
        }
    }

    public List<sMembers> SelectAllData() {
        // TODO Auto-generated method stub

        try {
            List<sMembers> MemberList = new ArrayList<sMembers>();

            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT  * FROM " + TABLE_MEMBER+" ORDER BY MemberID DESC";
            Cursor cursor = db.rawQuery(strSQL, null);

            if(cursor != null)
            {
                if (cursor.moveToFirst()) {
                    do {
                        sMembers cMember = new sMembers();
                        cMember.sMemberID(cursor.getString(0));
                        cMember.sName(cursor.getString(1));
                        cMember.set_Filename(cursor.getString(2));
                        cMember.set_Status(cursor.getString(3));
                        cMember.set_Filesize(cursor.getString(4));
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
    public long UpdateDownloadData(String filename,String status) {
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
            Val.put("status", status);

            long rows = db.update(TABLE_MEMBER, Val, " Filename = ?",
                    new String[] { String.valueOf(filename) });

            db.close();
            return rows; // return rows updated.

        } catch (Exception e) {
            return -1;
        }

    }

    // Delete Data
    public long DeleteData(String strMemberID) {
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

            long rows =  db.delete(TABLE_MEMBER, "MemberID = ?",
                    new String[] { String.valueOf(strMemberID) });

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

}