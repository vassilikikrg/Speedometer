package com.karag.speedometer;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;

public class SpeedingLogsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseHelper dbHelper;
    ArrayList<String> log_id,log_latitude,log_longitude,log_speed,log_timestamp;
    ArrayList<String> log_id_7days,log_latitude_7days,log_longitude_7days,log_speed_7days,log_timestamp_7days;
    CustomAdapter customAdapter_all,customAdapter_7days;
    Toolbar toolbar;
    RadioGroup radioGroup;
    RadioButton radioButton,radio_all,radio_7days;
    Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speeding_logs);
        recyclerView=findViewById(R.id.recyclerView);
        radioGroup=findViewById(R.id.radioGroup);
        toolbar=findViewById(R.id.topappbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //back button displayed in order to return to parent activity

        dbHelper=new DatabaseHelper(this);
        log_id=new ArrayList<>();
        log_latitude=new ArrayList<>();
        log_longitude=new ArrayList<>();
        log_speed=new ArrayList<>();
        log_timestamp=new ArrayList<>();

        log_id_7days=new ArrayList<>();
        log_latitude_7days=new ArrayList<>();
        log_longitude_7days=new ArrayList<>();
        log_speed_7days=new ArrayList<>();
        log_timestamp_7days=new ArrayList<>();

        storeAllDataInArrays(); //fetch data and add to corresponding arrays
        store7DaysDataInArrays();
        //set up custom adapter for all data
        customAdapter_all=new CustomAdapter(this,log_id,log_latitude,log_longitude,log_speed,log_timestamp);
        //set up custom adapter for data of the past 7 days
        customAdapter_7days=new CustomAdapter(this,log_id_7days,log_latitude_7days,log_longitude_7days,log_speed_7days,log_timestamp_7days);
        setRecyclerAdapter(customAdapter_all,SpeedingLogsActivity.this); //initial state
    }

    void storeAllDataInArrays(){
        Cursor cursor=dbHelper.readAllData(); ///initialization

        if(cursor.getCount()==0){

        }
        else {
            while (cursor.moveToNext()){
                log_id.add(cursor.getString(0));
                log_latitude.add(cursor.getString(1));
                log_longitude.add(cursor.getString(2));
                log_speed.add(cursor.getString(3));
                log_timestamp.add(cursor.getString(4));
            }
            cursor.close();
        }

    }
    void store7DaysDataInArrays(){
        Cursor cursor=dbHelper.readDataPast7days(); //initialization

        if(cursor.getCount()==0){

        }
        else {
            while (cursor.moveToNext()){
                log_id_7days.add(cursor.getString(0));
                log_latitude_7days.add(cursor.getString(1));
                log_longitude_7days.add(cursor.getString(2));
                log_speed_7days.add(cursor.getString(3));
                log_timestamp_7days.add(cursor.getString(4));
            }
            cursor.close();
        }

    }

    public void checkRadioButton(View view){
        int radioId=radioGroup.getCheckedRadioButtonId();
        radioButton=findViewById(radioId);
        if(radioId==R.id.radioButtonAll){
            setRecyclerAdapter(customAdapter_all,SpeedingLogsActivity.this);
        }else{
            setRecyclerAdapter(customAdapter_7days,SpeedingLogsActivity.this);
        }
    }
    public void setRecyclerAdapter(CustomAdapter customAdapter,Context context){
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

}