package com.tools.tommydev.videofinder.DataBaseHelper;

/**
 * Created by TomMy on 8/8/13.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBClass_AutoText extends SQLiteOpenHelper {


    // Database Version
    private static final int DATABASE_VERSION =2;

    // Database Name
    private static final String DATABASE_NAME = "ytube_autotext";

    // Table Name
    private static final String TABLE_MEMBER = "data";

    public DBClass_AutoText(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        // Create Table Name
        db.execSQL("CREATE TABLE " + TABLE_MEMBER +
                "(PackageID TEXT PRIMARY KEY," +
                " Name TEXT(255)," +
                " Position TEXT(255)" +
                ");");

        Log.d("CREATE TABLE","Create Table Successfully.");
    }

    // Insert Data
    public long InsertData(String PackageID,String Name,String Position) {
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
            Val.put("PackageID", PackageID);
            Val.put("Name", Name);
            Val.put("Position", Position);


            long rows = db.insert(TABLE_MEMBER, null, Val);

            db.close();
            return rows; // return rows inserted.

        } catch (Exception e) {
           // return DeleteData(PackageID);
            return -1;
        }

    }


    // Select Data
    public String[] SelectData(String PackageID) {
        // TODO Auto-generated method stub

        try {
            String arrData[] = null;

            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            Cursor cursor = db.query(TABLE_MEMBER, new String[] { "*" },
                    "PackageID=?",
                    new String[] { String.valueOf(PackageID) }, null, null, null, null);

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

                }
            }
            cursor.close();
            db.close();
            return arrData;

        } catch (Exception e) {
            return null;
        }

    }




    public class Bottom_apps {
        String _PackageID;
        String _Name;
        String _Position;

        public String get_PackageID() {
            return _PackageID;
        }

        public void set_PackageID(String _PackageID) {
            this._PackageID = _PackageID;
        }

        public String get_Name() {
            return _Name;
        }

        public void set_Name(String _Name) {
            this._Name = _Name;
        }

        public String get_Position() {
            return _Position;
        }

        public void set_Position(String _Position) {
            this._Position = _Position;
        }
    }


    public List<Bottom_apps> SelectAllData() {
        // TODO Auto-generated method stub

        try {
            List<Bottom_apps> MemberList = new ArrayList<Bottom_apps>();

            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data

            String strSQL = "SELECT  * FROM " + TABLE_MEMBER+" ORDER BY Position DESC";
            Cursor cursor = db.rawQuery(strSQL, null);

            if(cursor != null)
            {
                if (cursor.moveToFirst()) {
                    do {
                        Bottom_apps cMember = new Bottom_apps();
                        cMember.set_PackageID(cursor.getString(0));
                        cMember.set_Name(cursor.getString(1));
                        cMember.set_Position(cursor.getString(2));
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



    // Delete Data
    public long DeleteData(String PackageID) {
        // TODO Auto-generated method stub
        Log.e("DeleteData",PackageID);
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

            long rows =  db.delete(TABLE_MEMBER, "PackageID = ?",
                    new String[] { String.valueOf(PackageID) });

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

}