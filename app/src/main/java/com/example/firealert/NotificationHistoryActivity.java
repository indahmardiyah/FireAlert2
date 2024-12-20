package com.example.firealert;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NotificationHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerViewNotifications;
    private Button buttonPantauanKondisi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_history);

        recyclerViewNotifications = findViewById(R.id.recyclerViewNotifications);
        buttonPantauanKondisi = findViewById(R.id.buttonPantauanKondisi);

        // Dummy data for notifications
        List<NotificationItem> notificationList = new ArrayList<>();
        notificationList.add(new NotificationItem("", "", "Tidak terdeteksi", "30°C", "80ppm"));
        notificationList.add(new NotificationItem("1 Juli 2024, 12:33 PM", "Terjadi Kebakaran", "Terdeteksi", "60°C", "400ppm"));
        notificationList.add(new NotificationItem("9 Juni 2024, 2:05 PM", "Tidak Kebakaran", "Tidak terdeteksi", "35°C", "100ppm"));
        notificationList.add(new NotificationItem("31 Januari 2024, 1:15 PM", "Terjadi Kebakaran", "Terdeteksi", "70°C", "450ppm"));
        notificationList.add(new NotificationItem("3 Agustus 2023, 3:15 PM", "Terjadi Kebakaran", "Terdeteksi", "55°C", "350ppm"));

        // Set up RecyclerView
        NotificationAdapter adapter = new NotificationAdapter(notificationList);
        recyclerViewNotifications.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewNotifications.setAdapter(adapter);

        // Button listener for navigating to the monitoring screen
        buttonPantauanKondisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotificationHistoryActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}
