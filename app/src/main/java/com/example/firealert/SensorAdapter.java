package com.example.firealert;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class SensorAdapter extends ArrayAdapter<Sensor> {
    private final Context context;
    private final List<Sensor> sensorList;

    public SensorAdapter(@NonNull Context context, @NonNull List<Sensor> sensorList) {
        super(context, R.layout.sensor_item, sensorList);
        this.context = context;
        this.sensorList = sensorList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Menggunakan ViewHolder untuk meningkatkan performa
        ViewHolder viewHolder;

        if (convertView == null) {
            // Inflate layout item sensor
            convertView = LayoutInflater.from(context).inflate(R.layout.sensor_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.nameTextView = convertView.findViewById(R.id.sensorNameTextView);
            viewHolder.valueTextView = convertView.findViewById(R.id.sensorValueTextView);
            convertView.setTag(viewHolder); // Menyimpan ViewHolder di tag
        } else {
            viewHolder = (ViewHolder) convertView.getTag(); // Mengambil ViewHolder dari tag
        }

        // Mendapatkan sensor saat ini
        Sensor currentSensor = sensorList.get(position);

        // Mengatur teks untuk TextView
        viewHolder.nameTextView.setText(currentSensor.getName());
        viewHolder.valueTextView.setText(currentSensor.getValue());

        return convertView;
    }

    // ViewHolder untuk menyimpan referensi ke tampilan
    private static class ViewHolder {
        TextView nameTextView;
        TextView valueTextView;
    }
}