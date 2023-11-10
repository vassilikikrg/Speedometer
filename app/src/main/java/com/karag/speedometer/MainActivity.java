package com.karag.speedometer;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MainActivity extends AppCompatActivity implements LocationListener {
    LocationManager locationManager;
    TextView currentSpeedText,infoText,speedLimitText;
    Button startButton,stopButton;
    ImageView imageRun;
    SharedPreferences preferences;
    int limit;
    CustomTTS customTTS;
    boolean isInformed=false;
    DatabaseHelper dbHelper;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentSpeedText=findViewById(R.id.textViewCurrentSpeed);
        startButton=findViewById(R.id.startButton);
        stopButton =findViewById( R.id.stopButton );
        imageRun=findViewById(R.id.imageRunning);
        infoText=findViewById(R.id.infoText);
        speedLimitText=findViewById(R.id.speedLimitText);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        preferences = getSharedPreferences("MyPreferences",MODE_PRIVATE); //access shared preferences
        limit=readLimit();
        speedLimitText.setText(String.format("Speed limit is %s km/h ",limit));

        customTTS=new CustomTTS(this);
        dbHelper=new DatabaseHelper(this);
    }
    @Override
    protected void onResume() {
        limit=readLimit();
        speedLimitText.setText(String.format("Speed limit is %s km/h ",limit));
        super.onResume();
    }

    public int readLimit(){
        return preferences.getInt("limit",30);
    }
    public void startTracking(View view) {
        //if permission for location is not granted
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //request it from the user
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},111);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        startUIChanges(view);
    }

    public void stopTracking(View view){

        locationManager.removeUpdates(this); // stop requesting user's location updates
        stopUIChanges(view);
    }

    public void startUIChanges(View view){
        //replace start button with stop button
        startButton.setVisibility(View.GONE);
        stopButton.setVisibility(View.VISIBLE);
        //Inform user that tracking started by displaying a toast
        Toast.makeText(getApplicationContext(),"Speed Tracking started",Toast.LENGTH_SHORT).show();
        //replace info message and image with the current speed text
        currentSpeedText.setVisibility(View.VISIBLE);
        speedLimitText.setVisibility(View.VISIBLE);
        infoText.setVisibility(View.GONE);
        imageRun.setVisibility(View.GONE);

    }

    public void stopUIChanges(View view){
        setActivityBackgroundColor(R.color.white);
        //replace stop button with start button
        stopButton.setVisibility(View.GONE);
        startButton.setVisibility(View.VISIBLE);
        //Inform user that tracking stopped by displaying a toast
        Toast.makeText(getApplicationContext(),"Speed Tracking stopped",Toast.LENGTH_SHORT).show();
        //replace the current speed text
        currentSpeedText.setVisibility(View.GONE);
        currentSpeedText.setText("Loading...");
        speedLimitText.setVisibility(View.GONE);
        infoText.setVisibility(View.VISIBLE);
        imageRun.setVisibility(View.VISIBLE);

    }
    public void changeLimit(View view){
        Intent intent = new Intent(this, SpeedLimitActivity.class);
        intent.putExtra("limit",limit);
        startActivity(intent);
    }
    @Override
    public void onLocationChanged(@NonNull Location location) {
        // 1 m/s=3.6 km/h
        double speed=location.getSpeed()*3.6;
        currentSpeedText.setText(new StringBuilder().append("Current speed:\n").append(String.format("%.2f", speed)).append("\nkm/h"));
        if(isSpeeding(speed,limit)){
            setActivityBackgroundColor(R.color.speedingColor);
            if(!isInformed){ //user is informed only the time when he exceeds the speed limit
            customTTS.speak("Speed limit exceeded");
            isInformed=true;
            //registration of the speed limit violation incident
            insertSpeedingLog(location.getLatitude(),location.getLongitude(),speed);
            }
        }
        else{
            setActivityBackgroundColor(R.color.white);
            isInformed=false;
        }
    }
    public void setActivityBackgroundColor(int colorResourceId) {
        int color = ContextCompat.getColor(this, colorResourceId);
        ConstraintLayout layout = findViewById(R.id.constraintLayoutMain);
        layout.setBackgroundColor(color);
    }

    public boolean isSpeeding(double currentSpeed,int speedLimit){
        if(currentSpeed>speedLimit) return true;
        return false;
    }

    public void viewSpeedingLogs(View view){
        Intent intent = new Intent(this, SpeedingLogsActivity.class);
        startActivity(intent);
    }
    CustomDatetime customDatetime;
    public void insertSpeedingLog(double  latitude, double longitude, double speed){
        customDatetime=new CustomDatetime(LocalDateTime.now());
        dbHelper.addLog(String.valueOf(latitude),String.valueOf(longitude),doubleToFloatRounded(speed),customDatetime.DatetimeToString());
    }

    public float doubleToFloatRounded(double doubleValue){
        float floatValue=(float) doubleValue;
        //round the float to two decimal points
        int decimalPoints = 2;
        BigDecimal roundedValue = new BigDecimal(floatValue).setScale(decimalPoints, RoundingMode.HALF_UP);
        return roundedValue.floatValue();
    }
}