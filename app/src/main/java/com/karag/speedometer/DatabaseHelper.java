package com.karag.speedometer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "speedingDB.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "Logs";
    public static final String COLUMN_ID="id";
    public static final String COLUMN_TIME="timestamp";
    public static final String COLUMN_LATITUDE="latitude";
    public static final String COLUMN_LONGITUDE="longitude";
    public static final String COLUMN_SPEED="speed";

    SQLiteDatabase sqLiteDatabase;
    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_LATITUDE + " TEXT, " +
                COLUMN_LONGITUDE + " TEXT, " +
                COLUMN_SPEED + " REAL, " +
                COLUMN_TIME + " TEXT);";
        db.execSQL(query);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public void addLog(String  latitude, String longitude, double speed, String timestamp){
        //insertion of speeding data
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_LATITUDE, latitude);
        cv.put(COLUMN_LONGITUDE, longitude);
        cv.put(COLUMN_SPEED, speed);
        cv.put(COLUMN_TIME, timestamp);

        long result = db.insert(TABLE_NAME,null, cv);
        if(result == -1){
            System.out.println("Failed");
        }else {
            System.out.println("Added Successfully!");
        }
    }
    public Cursor readAllData(){
        //retrieve speeding logs
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor readDataPast7days(){
        // retrieve speeding logs for the past 7 days
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        if (db != null) {
            //current LocalDateTime
            LocalDateTime currentDateTime = LocalDateTime.now();

            //calculate the LocalDateTime 7 days ago
            LocalDateTime sevenDaysAgo = currentDateTime.minusDays(7);

            //format the LocalDateTime to match the timestamp format in the database
            String currentDateTimeString = new CustomDatetime(currentDateTime).DatetimeToString();
            String sevenDaysAgoString = new CustomDatetime(sevenDaysAgo).DatetimeToString();

            //build the query with a WHERE clause to filter logs within the past 7 days
            String query = "SELECT * FROM " + TABLE_NAME +
                    " WHERE " + COLUMN_TIME + " BETWEEN '" + sevenDaysAgoString + "' AND '" + currentDateTimeString + "'";

            // Execute the query
            cursor = db.rawQuery(query, null);
        }

        return cursor;
    }
}
