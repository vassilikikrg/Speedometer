package com.karag.speedometer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

public class SpeedLimitActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {
    SeekBar seekBar;
    TextView updatedLimitText,currentLimitText;
    SharedPreferences preferences;
    int currentLimit;
    int updatedLimit;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speed_limit);
        updatedLimitText=findViewById(R.id.updatetdLimitText);
        currentLimitText=findViewById(R.id.currentLimitText);
        seekBar=findViewById(R.id.seekBarLimit);
        toolbar=findViewById(R.id.topappbar);
        setSupportActionBar(toolbar);

        currentLimit=getIntent().getIntExtra("limit",30);
        currentLimitText.setText("Current limit is set to "+currentLimit+" km/h");

        seekBar.setOnSeekBarChangeListener(this);
        //seekbar starts from 0 and goes to 12 ,while the limit should be from 10 to 130 km/h
        int seekBarProgress=(currentLimit/10)-1; //find starting point based on current limit
        seekBar.setProgress(seekBarProgress); //set starting point

        preferences = getSharedPreferences("MyPreferences",MODE_PRIVATE);
    }

    public void saveLimit(View view){

        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("limit",updatedLimit);
        editor.apply();
        finish();

    }
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        updatedLimit=(progress+1)*10;
        updatedLimitText.setText(new StringBuilder().append("The updated limit will be ").append(updatedLimit).append(" km/h"));

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}