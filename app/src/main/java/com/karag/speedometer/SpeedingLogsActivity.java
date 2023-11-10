package com.karag.speedometer;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import java.util.ArrayList;

public class SpeedingLogsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseHelper dbHelper;
    ArrayList<String> log_id,log_latitude,log_longitude,log_speed,log_timestamp;
    CustomAdapter customAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speeding_logs);
        recyclerView=findViewById(R.id.recyclerView);

        dbHelper=new DatabaseHelper(this);
        log_id=new ArrayList<>();
        log_latitude=new ArrayList<>();
        log_longitude=new ArrayList<>();
        log_speed=new ArrayList<>();
        log_timestamp=new ArrayList<>();
        storeDataInArrays(); //fetch data and add to corresponding arrays
        //set up custom adapter
        customAdapter=new CustomAdapter(this,log_id,log_latitude,log_longitude,log_speed,log_timestamp);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(SpeedingLogsActivity.this));
    }
    void storeDataInArrays(){
        Cursor cursor=dbHelper.readAllData(); //SELECT query
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
}