package com.itobuz.android.easybmicalculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;
import android.provider.SyncStateContract;
import android.telecom.Call;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Debasis on 26/9/16.
 */

public class DatabaseHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "bmi_calculation";
    public static final String DATABASE_TABLE_NAME = "BmiPersons";
    private static final int DATABASE_VERSION = 1;

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_AGE = "age";
    public static final String COLUMN_SEX = "sex";
    public static final String COLUMN_WEIGHT = "weight";
    public static final String COLUMN_WEIGHT_UNIT = "weightunit";
    public static final String COLUMN_HEIGHT= "height";
    public static final String COLUMN_HEIGHT_INCH= "heightinch";
    public static final String COLUMN_HEIGHT_UNIT = "heightunit";
    public static final String COLUMN_RESULT= "result";
    public static final String COLUMN_DATE = "created_at";
    public static final String COLUMN_STATUS= "status";

    // Database table create sql string.
    private static final String DATABASE_CREATE =
            "CREATE TABLE IF NOT EXISTS "+ DATABASE_TABLE_NAME + "("+
                    COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    COLUMN_AGE +" VARCHAR, "+
                    COLUMN_SEX +" VARCHAR, "+
                    COLUMN_WEIGHT +" INTEGER, "+
                    COLUMN_WEIGHT_UNIT +" VARCHAR, "+
                    COLUMN_HEIGHT +" INTEGER, "+
                    COLUMN_HEIGHT_INCH +" INTEGER, "+
                    COLUMN_HEIGHT_UNIT +" VARCHAR, "+
                    COLUMN_RESULT +" INTEGER, "+
                    COLUMN_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                    COLUMN_STATUS +" INTEGER);";


    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private int lastId;
    private float lastResult;
    private String lastAge;
    private String lastWeight;
    private String lastWeightUnit;
    private String lastHeight;
    private String lastHeightUnit;
    private String lastHeightInch;
    private String lastRadio;

    private Cursor cursor;

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
//        db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE_NAME);
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE_NAME);
        onCreate(db);
    }

    /**
     * Insert data in database by insertBmiRow
     * @param age
     * @param sex
     * @param weight
     * @param weightunit
     * @param height
     * @param heightinch
     * @param heightunit
     * @param result
     * @param date
     * @param status
     * @return boolean
     */
    public boolean insertBmiRow(String age, String sex, double weight, String weightunit, double height, double heightinch, String heightunit, double result, String date, int status){
        mDb = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //content values to put in db
        contentValues.put(COLUMN_AGE, age);
        contentValues.put(COLUMN_SEX, sex);
        contentValues.put(COLUMN_WEIGHT, weight);
        contentValues.put(COLUMN_WEIGHT_UNIT, weightunit);
        contentValues.put(COLUMN_HEIGHT, height);
        contentValues.put(COLUMN_HEIGHT_INCH, heightinch);
        contentValues.put(COLUMN_HEIGHT_UNIT, heightunit);
        contentValues.put(COLUMN_RESULT, result);
        contentValues.put(COLUMN_DATE, date);
        contentValues.put(COLUMN_STATUS, status);
        //db insert
        mDb.insert(DATABASE_TABLE_NAME, null, contentValues);
        return true;
    }


    // Getting single value
    public DataProvider getBmi(int id) {
        mDb = this.getReadableDatabase();

        cursor = mDb.query(DATABASE_TABLE_NAME, new String[] { COLUMN_ID, COLUMN_AGE, COLUMN_SEX, COLUMN_WEIGHT, COLUMN_WEIGHT_UNIT, COLUMN_HEIGHT, COLUMN_HEIGHT_INCH, COLUMN_HEIGHT_UNIT, COLUMN_RESULT, COLUMN_DATE, COLUMN_STATUS  }, COLUMN_ID +"=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        DataProvider dataProvider = new DataProvider(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), Double.parseDouble(cursor.getString(3)), cursor.getString(4), Double.parseDouble(cursor.getString(5)), Double.parseDouble(cursor.getString(6)), cursor.getString(7), Float.parseFloat(cursor.getString(8)), cursor.getString(9), Integer.parseInt(cursor.getString(10)));
        // return dataProvider
        return dataProvider;
    }


    // Getting Bmi History Count

    public int getBmiHistoryCount() {
        String countQuery = "SELECT  * FROM " + DATABASE_TABLE_NAME;
        mDb = this.getReadableDatabase();
        Cursor cursor = mDb.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        return cnt;
    }

    /**
     * Method lastBmiId
     * @return int lastId
     */

    public int lastBmiId(){
        String status="1";
        String query = "SELECT " + COLUMN_ID +" FROM "+ DATABASE_TABLE_NAME+" WHERE " + COLUMN_STATUS +" = '" + status + "' ORDER BY "+COLUMN_ID+"  DESC limit 1";
        mDb = this.getReadableDatabase();
        cursor = mDb.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            lastId = cursor.getInt(0); //The 0 is the column index, we only have 1 column, so the index is 0
        }
        return lastId;
    }

    /**
     * Method lastBmiResult
     * @return float lastResult
     */
    public float lastBmiResult(){
        String status="1";
        String query = "SELECT " + COLUMN_RESULT +" FROM "+ DATABASE_TABLE_NAME+" WHERE " + COLUMN_STATUS +" = '" + status + "' ORDER BY "+COLUMN_ID+"  DESC limit 1";
        mDb = this.getReadableDatabase();
        cursor = mDb.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            lastResult = cursor.getInt(0); //The 0 is the column index, we only have 1 column, so the index is 0
        }
        return lastResult;
    }

    /**
     * Method LastRecords
     * @return cursor
     */
    public Cursor lastRecords( ){
        String getlastquery = "SELECT " + COLUMN_AGE +", "+ COLUMN_SEX +", " + COLUMN_WEIGHT +", " + COLUMN_WEIGHT_UNIT +", " + COLUMN_HEIGHT+", " + COLUMN_HEIGHT_INCH +", " + COLUMN_HEIGHT_UNIT +", " + COLUMN_RESULT +" from "+ DATABASE_TABLE_NAME +";";
        mDb = this.getReadableDatabase();
        cursor = mDb.rawQuery(getlastquery, null);
        return cursor;
    }

    /**
     * Method getBmiInformation
     * @return cursor
     */
    public Cursor getBmiInformation(){
        String status="1";
        String allQuery = "SELECT "+COLUMN_ID+", "+COLUMN_DATE+", "+COLUMN_AGE+" , "+COLUMN_RESULT+" FROM " + DATABASE_TABLE_NAME+" WHERE "+ COLUMN_STATUS +" = '" + status + "' ORDER BY "+COLUMN_ID+"  DESC";
        mDb = this.getReadableDatabase();
        cursor = mDb.rawQuery(allQuery, null);
        return cursor;
    }

    /**
     * Method removeBmiItem to remove single row
     * @param id
     */
    public void removeBmiItem(int id){
//        String sId = String.valueOf(id);
//        String status="1";
        mDb.execSQL("DELETE FROM "+DATABASE_TABLE_NAME+" WHERE "+COLUMN_ID+" = " + id ); // + "' AND " + COLUMN_STATUS +" = '" + status + "'"
    }


}