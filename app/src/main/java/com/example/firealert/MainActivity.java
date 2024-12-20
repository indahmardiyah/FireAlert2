package com.example.firealert;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inisialisasi tombol
        Button buttonMasuk = findViewById(R.id.btnMasuk);
        Button buttonDaftar = findViewById(R.id.btnDaftar);

        // Event untuk tombol Masuk
        buttonMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tampilkan pesan Toast
                Toast.makeText(MainActivity.this, "Masuk ditekan!", Toast.LENGTH_SHORT).show();

                // Arahkan ke LoginActivity
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        // Event untuk tombol Daftar
        buttonDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tampilkan pesan Toast
                Toast.makeText(MainActivity.this, "Daftar ditekan!", Toast.LENGTH_SHORT).show();

                // Arahkan ke RegisActivity
                Intent intent = new Intent(MainActivity.this, RegisActivity.class);
                startActivity(intent);
            }
        });
    }
}
