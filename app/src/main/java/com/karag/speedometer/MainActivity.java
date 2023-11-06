package com.karag.speedometer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LocationListener {
    LocationManager locationManager;
    TextView currentSpeedText;
    Button startButton;
    Button stopButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currentSpeedText=findViewById(R.id.textViewCurrentSpeed);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        startButton=findViewById(R.id.startButton);
        stopButton =findViewById( R.id.stopButton );
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
        //replace start button with stop button
        startButton.setVisibility(View.GONE);
        stopButton.setVisibility(View.VISIBLE);
        //Inform user by displaying a toast
        Toast.makeText(getApplicationContext(),"Speed Tracking started",Toast.LENGTH_SHORT).show();
    }
    public void stopTracking(View view){

        locationManager.removeUpdates(this); // stop requesting user's location updates
        //replace stop button with start button
        stopButton.setVisibility(View.GONE);
        startButton.setVisibility(View.VISIBLE);
        //Inform user by displaying a toast
        Toast.makeText(getApplicationContext(),"Speed Tracking stopped",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        currentSpeedText.setText(new StringBuilder().append(location.getSpeed()).append(" m/sec !"));

    }
}