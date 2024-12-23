package com.example.firealert;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminDashboardActivity extends AppCompatActivity {

    EditText sensorNameEditText;
    Button addSensorButton, deleteSensorButton;
    ListView sensorListView;

    FirebaseDatabase database;
    DatabaseReference sensorsReference;

    ArrayList<Sensor> sensorList;
    SensorAdapter sensorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // Inisialisasi elemen UI
        sensorNameEditText = findViewById(R.id.etSensorName);

        addSensorButton = findViewById(R.id.btnAddSensor);
        sensorListView = findViewById(R.id.lvSensorList);
        deleteSensorButton = findViewById(R.id.btnDeleteSensor);
        // Firebase Database
        database = FirebaseDatabase.getInstance();
        sensorsReference = database.getReference("sensors");

        // Inisialisasi list dan adapter
        sensorList = new ArrayList<>();
        sensorAdapter = new SensorAdapter(this, sensorList);
        sensorListView.setAdapter(sensorAdapter);

        ImageView logoutIcon = findViewById(R.id.logoutIcon);
        logoutIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pindah ke aktivitas utama (MainActivity)
                Intent intent = new Intent(AdminDashboardActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Menutup aktivitas saat ini
            }
        });

        Button btnNotificationHistory = findViewById(R.id.btnNotificationHistory);
        btnNotificationHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pindah ke aktivitas riwayat notifikasi
                Intent intent = new Intent(AdminDashboardActivity.this, NotificationHistoryActivity.class);
                startActivity(intent);
            }
        });

        // Tombol tambah sensor
        addSensorButton.setOnClickListener(v -> {
            String sensorName = sensorNameEditText.getText().toString().trim();

            if (TextUtils.isEmpty(sensorName)) {
                sensorNameEditText.setError("Nama sensor tidak boleh kosong");
                return;
            }

            // Menambahkan sensor ke database
            String sensorId = sensorsReference.push().getKey();  // Generate ID unik
            Sensor sensor = new Sensor(sensorId, sensorName);
            if (sensorId != null) {
                sensorsReference.child(sensorId).setValue(sensor)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(AdminDashboardActivity.this, "Sensor berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                            sensorNameEditText.setText("");
                            sensorsReference.child(sensorId).child("lpg").setValue(0.0);
                            sensorsReference.child(sensorId).child("flame").setValue(0);
                            sensorsReference.child(sensorId).child("suhu").setValue(0);
                        })
                        .addOnFailureListener(e -> Toast.makeText(AdminDashboardActivity.this, "Gagal menambahkan sensor", Toast.LENGTH_SHORT).show());
            }
        });

        // Tombol hapus sensor berdasarkan nama
        deleteSensorButton.setOnClickListener(v -> {
            String sensorName = sensorNameEditText.getText().toString().trim();

            if (TextUtils.isEmpty(sensorName)) {
                sensorNameEditText.setError("Nama sensor tidak boleh kosong");
                return;
            }

            // Mencari dan menghapus sensor
            sensorsReference.orderByChild("name").equalTo(sensorName).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            snapshot.getRef().removeValue();
                        }
                        Toast.makeText(AdminDashboardActivity.this, "Sensor berhasil dihapus", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AdminDashboardActivity.this, "Sensor tidak ditemukan", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(AdminDashboardActivity.this, "Gagal menghapus sensor", Toast.LENGTH_SHORT).show();
                }
            });
        });

        // Mendengarkan perubahan di database untuk menampilkan daftar sensor
        sensorsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sensorList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    Log.d("SNAP", snapshot.toString());
                    Sensor sensor = snapshot.getValue(Sensor.class);
                    if (sensor != null) {
                        sensorList.add(sensor);
                    }
                }
                sensorAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AdminDashboardActivity.this, "Gagal memuat data sensor", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
