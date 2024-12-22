package com.example.firealert;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import android.content.pm.PackageManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    private TextView tvDateTime, tvFlame, tvLpg, tvSuhu;
    private Handler handler = new Handler();
    private Runnable updateTimeRunnable;

    private static final String CHANNEL_ID = "my_channel_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Inisialisasi TextView untuk waktu real-time dan sensor
        tvDateTime = findViewById(R.id.tvDateTime);
        tvFlame = findViewById(R.id.tvApiStatus);
        tvLpg = findViewById(R.id.tvGasStatus);
        tvSuhu = findViewById(R.id.tvTemperature);

        // Update waktu secara real-time
        updateTimeRunnable = new Runnable() {
            @Override
            public void run() {
                String currentDateTime = new SimpleDateFormat("EEEE, dd MMMM yyyy\nhh:mm:ss a", Locale.getDefault()).format(new Date());
                tvDateTime.setText(currentDateTime);
                handler.postDelayed(this, 1000); // Perbarui setiap detik
            }
        };
        handler.post(updateTimeRunnable);

        // Ambil data sensor dari Firebase
        fetchSensorData();

        // Tombol Riwayat Notifikasi
        Button btnNotificationHistory = findViewById(R.id.btnNotificationHistory);
        btnNotificationHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pindah ke aktivitas riwayat notifikasi
                Intent intent = new Intent(HomeActivity.this, NotificationHistoryActivity.class);
                startActivity(intent);
            }
        });

        // Ikon Logout
        ImageView logoutIcon = findViewById(R.id.logoutIcon);
        logoutIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pindah ke aktivitas utama (MainActivity)
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Menutup aktivitas saat ini
            }
        });
    }

    // Fungsi untuk mengambil data sensor dari Firebase
    private void fetchSensorData() {
        DatabaseReference sensorsRef = FirebaseDatabase.getInstance().getReference("sensors").child("-OEh1BMjMp-WvVPltf7r");

        sensorsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Ambil nilai sensor dari Firebase
                    int flame = dataSnapshot.child("flame").getValue(Integer.class);
                    double lpg = dataSnapshot.child("lpg").getValue(Double.class);
                    double suhu = dataSnapshot.child("suhu").getValue(Double.class);
                    String timestamp = dataSnapshot.child("timestamp").getValue(String.class);



                    if(flame == 1){

                        showNotification("Terjadi Kebakaran!", "Api Terdeteksi");
                    }else if(lpg >= 50.0){

                        showNotification("Terjadi Kebakaran!", "Kadar Gas Melebihi Batas");
                    }else if(suhu >= 42.0){

                        showNotification("Terjadi Kebakaran!", "Suhu Melebihi Batas");
                    }

                    // Set nilai ke TextView
                    tvFlame.setText("Flame: " + flame);
                    tvLpg.setText("LPG: " + lpg);
                    tvSuhu.setText("Suhu: " + suhu + " °C");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Jika terjadi error
                tvFlame.setText("Flame: Error");
                tvLpg.setText("LPG: Error");
                tvSuhu.setText("Suhu: Error");
            }
        });
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "My Channel";
            String description = "Channel untuk notifikasi sederhana";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // Daftarkan channel ke sistem
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Handle permission
            return;
        }

    }

    private void showNotification(String title, String content) {
        createNotificationChannel();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(HomeActivity.this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info) // Icon bawaan Android
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Tampilkan notifikasi
        notificationManager.notify(1, builder.build());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(updateTimeRunnable);
    }
}
