package com.example.firealert;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    EditText editTextNama, editTextPassword;
    Button loginButton;
    TextView signupRedirectTextView;  // Deklarasi TextView untuk redirect ke halaman registrasi
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Menghubungkan elemen UI
        editTextNama = findViewById(R.id.usernameEditText);
        editTextPassword = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        signupRedirectTextView = findViewById(R.id.registerRedirectButton);  // Inisialisasi TextView

        // Aksi tombol login
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextNama.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                // Validasi input
                if (TextUtils.isEmpty(username)) {
                    editTextNama.setError("Nama pengguna tidak boleh kosong");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    editTextPassword.setError("Kata sandi tidak boleh kosong");
                    return;
                }

                // Cek untuk akun admin
                if (username.equals("admin") && password.equals("admin123")) {
                    Toast.makeText(LoginActivity.this, "Login Admin Berhasil!", Toast.LENGTH_SHORT).show();
                    // Arahkan ke halaman Admin Dashboard
                    Intent intent = new Intent(LoginActivity.this, AdminDashboardActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Firebase untuk pengguna biasa
                    database = FirebaseDatabase.getInstance();
                    reference = database.getReference("users");

                    // Mencari pengguna berdasarkan nama
                    reference.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                HelperClass user = dataSnapshot.getValue(HelperClass.class);

                                if (user != null && user.getPassword().equals(password)) {
                                    // Login berhasil untuk pengguna biasa
                                    Toast.makeText(LoginActivity.this, "Login Berhasil!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);  // Ganti ke HomeActivity
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // Password salah
                                    Toast.makeText(LoginActivity.this, "Password salah", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // Pengguna tidak ditemukan
                                Toast.makeText(LoginActivity.this, "Pengguna tidak ditemukan", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(LoginActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        // Aksi untuk teks "Belum punya akun? Daftar"
        signupRedirectTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigasi ke RegisActivity
                Intent intent = new Intent(LoginActivity.this, RegisActivity.class);
                startActivity(intent);
            }
        });
    }
}
