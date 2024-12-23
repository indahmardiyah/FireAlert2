package com.example.firealert;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.content.pm.PackageManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    private TextView tvDateTime, tvFlame, tvLpg, tvSuhu, tvStatus;
    private Handler handler = new Handler();
    private Runnable updateTimeRunnable;

    private static final String CHANNEL_ID = "my_channel_id";
    private static final String CHANNEL_NAME = "my_channel_name";


    boolean fireAlarm = false;

    boolean lpgAlarm = false;
    
    boolean suhuAlarm = false;

    String currentDateTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Inisialisasi TextView untuk waktu real-time dan sensor
        tvDateTime = findViewById(R.id.tvDateTime);
        tvFlame = findViewById(R.id.tvApiStatus);
        tvLpg = findViewById(R.id.tvGasStatus);
        tvSuhu = findViewById(R.id.tvTemperature);
        tvStatus = findViewById(R.id.tvStatus);

        // Update waktu secara real-time
        updateTimeRunnable = new Runnable() {
            @Override
            public void run() {
                currentDateTime = new SimpleDateFormat("EEEE, dd MMMM yyyy\nhh:mm:ss a", Locale.getDefault()).format(new Date());
                tvDateTime.setText(currentDateTime);
//                Notifikasi(HomeActivity.this,"Terjadi Kebakaran", "Api Terdeteksi", "flame");
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

                    if(flame == 1){

                        if(fireAlarm == false){
                            Notifikasi(HomeActivity.this,"Terjadi Kebakaran!", "Api Terdeteksi", "flame");
                            writeNotif("Api Terdeteksi", flame, lpg, suhu);
                        }
                        fireAlarm = true;
                        Log.d("FLAME", "Api");
//

                    }else{
                        fireAlarm = false;
                    }
                    if(lpg >= 50.0){
                        Log.d("LPG", "Gas");

                        if(lpgAlarm == false){
                            Notifikasi(HomeActivity.this,"Terjadi Kebakaran!", "Kadar Gas Melebihi Batas", "lpg");
                            writeNotif("Kadar Gas Melebihi Batas", flame, lpg, suhu);
                        }
                        lpgAlarm = true;

                    }else{
                        lpgAlarm = false;
                    }

                    if(suhu >= 42.0){
                        Log.d("SUHU", "Suhu");
                        if(suhuAlarm == false){
                            Notifikasi(HomeActivity.this, "Terjadi Kebakaran!", "Suhu Melebihi Batas", "suhu");
                            writeNotif("Suhu Melebihi Batas", flame, lpg, suhu);
                        }
                        suhuAlarm = true;

                    }else{
                        suhuAlarm = false;
                    }

                    if(!fireAlarm && !suhuAlarm && !lpgAlarm){
                        tvStatus.setText("Kondisi Aman");
                    }else{
                        tvStatus.setText("Terjadi Kebakaran!");
                    }


                    if(flame == 1){
                        tvFlame.setText("Api Terdeteksi" );
                    }else{
                        tvFlame.setText("Tidak Ada Api" );
                    }

                    // Set nilai ke TextView

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


    public void Notifikasi(Context context, String title, String content, String kondisi) {



        // Tampilkan notifikasi
        // Membuat notifikasi baru
        Notif newNotification = new Notif(title, content);
        newNotification.setMessage(content);
        newNotification.setId(System.currentTimeMillis());  // Simpan waktu sekarang



        // Tampilkan notifikasi
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title) // Gunakan kondisi sebagai judul notifikasi
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            builder.setChannelId(CHANNEL_ID);
            notificationManager.createNotificationChannel(channel);

        }

        // Tampilkan notifikasi
        if (ActivityCompat.checkSelfPermission(HomeActivity.this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Handle permission
            return;
        }
        Log.d("NOTIF", "ON");
        notificationManager.notify(getNotificationId(kondisi), builder.build());

        // Simpan waktu terakhir notifikasi ditampilkan ke Firebase Database


    }


    private int getNotificationId(String kondisi) {
        switch (kondisi) {
            case "suhu":
                return 1;
            case "flame":
                return 2;
            case "lpg":
                return 3;
            default:
                return 0; // Atur sesuai kebutuhan, pastikan semua ID notifikasi berbeda
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(updateTimeRunnable);
    }

    private void writeNotif(String message, long flame, double lpg, double suhu){
        DatabaseReference notifRef = FirebaseDatabase.getInstance().getReference("notifikasi");
        String notifKey = notifRef.push().getKey();
        notifRef.child(notifKey).child("time").setValue(currentDateTime);
        notifRef.child(notifKey).child("api").setValue(String.valueOf(flame));
        notifRef.child(notifKey).child("suhu").setValue(String.valueOf(suhu));
        notifRef.child(notifKey).child("asap").setValue(String.valueOf(lpg));
        notifRef.child(notifKey).child("status").setValue(String.valueOf(message));
    }
}
