package com.example.firealert;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NotificationHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerViewNotifications;
    private Button buttonPantauanKondisi;

    SensorAdapter sensorAdapter;

    DatabaseReference notifReference;
    FirebaseDatabase database;

    List<NotificationItem> notificationList;
    NotificationAdapter notifAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_history);

        database = FirebaseDatabase.getInstance();
        notifReference = database.getReference("notifikasi");

        recyclerViewNotifications = findViewById(R.id.recyclerViewNotifications);
        buttonPantauanKondisi = findViewById(R.id.buttonPantauanKondisi);

        // Dummy data for notifications
        notificationList = new ArrayList<>();
//        notificationList.add(new NotificationItem("", "", "Tidak terdeteksi", "30°C", "80ppm"));
//        notificationList.add(new NotificationItem("1 Juli 2024, 12:33 PM", "Terjadi Kebakaran", "Terdeteksi", "60°C", "400ppm"));
//        notificationList.add(new NotificationItem("9 Juni 2024, 2:05 PM", "Tidak Kebakaran", "Tidak terdeteksi", "35°C", "100ppm"));
//        notificationList.add(new NotificationItem("31 Januari 2024, 1:15 PM", "Terjadi Kebakaran", "Terdeteksi", "70°C", "450ppm"));
//        notificationList.add(new NotificationItem("3 Agustus 2023, 3:15 PM", "Terjadi Kebakaran", "Terdeteksi", "55°C", "350ppm"));

        // Set up RecyclerView
        notifAdapter = new NotificationAdapter(notificationList);
        recyclerViewNotifications.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewNotifications.setAdapter(notifAdapter);

        // Button listener for navigating to the monitoring screen
        buttonPantauanKondisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotificationHistoryActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

       notifReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notificationList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    Log.d("SNAP", snapshot.toString());
                    NotificationItem notifItem  = snapshot.getValue(NotificationItem.class);
                    if (notifItem != null) {
                        notificationList.add(notifItem);
                    }
                }
                notifAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(NotificationHistoryActivity.this, "Gagal memuat data notifikasi", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
