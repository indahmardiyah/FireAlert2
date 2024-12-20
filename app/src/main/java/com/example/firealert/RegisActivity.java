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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisActivity extends AppCompatActivity {

    EditText editTextNama, editTextEmail, editTextPassword, editTextKonfPass;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regis);

        // Menghubungkan elemen UI
        editTextNama = findViewById(R.id.usernameEditText);
        editTextEmail = findViewById(R.id.emailEditText);
        editTextPassword = findViewById(R.id.passwordEditText);
        editTextKonfPass = findViewById(R.id.confirmPasswordEditText);
        Button registerButton = findViewById(R.id.registerButton);
        TextView loginRedirectTextView = findViewById(R.id.loginRedirectButton);

        // Aksi tombol daftar
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mengambil input dari pengguna
                String username = editTextNama.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String confirmPassword = editTextKonfPass.getText().toString().trim();

                // Validasi input
                if (TextUtils.isEmpty(username)) {
                    editTextNama.setError("Nama pengguna tidak boleh kosong");
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    editTextEmail.setError("Email tidak boleh kosong");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    editTextPassword.setError("Kata sandi tidak boleh kosong");
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    editTextKonfPass.setError("Kata sandi tidak cocok");
                    return;
                }

                // Firebase
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("users");

                // Menggunakan nama sebagai key Firebase langsung tanpa perubahan
                String safeNamaKey = username;

                // Helper class untuk menyimpan data
                HelperClass helperClass = new HelperClass(username, email, password);
                reference.child(safeNamaKey).setValue(helperClass)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // Notifikasi dan navigasi ke LoginActivity
                                Toast.makeText(RegisActivity.this, "Registrasi berhasil!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(RegisActivity.this, "Registrasi gagal! Coba lagi.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        // Aksi untuk teks "Masuk"
        loginRedirectTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigasi ke LoginActivity
                Intent intent = new Intent(RegisActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
