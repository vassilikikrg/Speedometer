package com.karag.speedometer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

public class speedLimitActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {
    SeekBar seekBar;
    TextView updatedLimitText,currentLimitText;
    SharedPreferences preferences;
    String currentLimit;
    String updatedLimit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speed_limit);
        updatedLimitText=findViewById(R.id.updatetdLimitText);
        currentLimitText=findViewById(R.id.currentLimitText);
        currentLimit=getIntent().getStringExtra("limit");
        currentLimitText.setText("Current limit is set to "+currentLimit+" km/h");
        seekBar=findViewById(R.id.seekBarLimit);
        seekBar.setOnSeekBarChangeListener(this);
        preferences = getPreferences(MODE_PRIVATE);
    }

    public void saveLimit(View view){

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("limit",updatedLimit);
        editor.apply();
        currentLimit=updatedLimit;
        currentLimitText.setText("Current limit is set to "+updatedLimit+" km/h");

    }
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        updatedLimit=String.valueOf((progress+1)*10);

        updatedLimitText.setText(new StringBuilder().append("The updated limit will be ").append(updatedLimit).append(" km/h"));

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}