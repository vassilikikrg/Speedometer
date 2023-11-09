package com.karag.speedometer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;
    private ArrayList log_id,log_latitude,log_longitude,log_speed,log_timestamp;

    public CustomAdapter(Context context, ArrayList log_id, ArrayList log_latitude, ArrayList log_longitude, ArrayList log_speed,
                         ArrayList log_timestamp) {
        this.context = context;
        this.log_id = log_id;
        this.log_latitude = log_latitude;
        this.log_longitude = log_longitude;
        this.log_speed = log_speed;
        this.log_timestamp = log_timestamp;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //access data from each array based on current position
        holder.log_latitude_txt.setText( "lat: "+log_latitude.get(position));
        holder.log_longitude_txt.setText("long:"+log_longitude.get(position));
        holder.log_speed_txt.setText(log_speed.get(position)+" km/h");
        holder.log_time_txt.setText(String.valueOf(log_timestamp.get(position)));

    }

    @Override
    public int getItemCount() {
        return log_id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView log_latitude_txt, log_longitude_txt, log_speed_txt,log_time_txt;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //access textView items in order to fill them later with data from the app's db
            log_latitude_txt=itemView.findViewById(R.id.log_latitude_txt);
            log_longitude_txt = itemView.findViewById(R.id.log_longitude_txt);
            log_speed_txt=itemView.findViewById(R.id.log_speed_txt);
            log_time_txt = itemView.findViewById(R.id.log_time_txt);
        }
    }
}
