package com.example.firealert;

public class HelperClass {
    private String nama;
    private String email;
    private String password;

    // Konstruktor tanpa parameter (diperlukan oleh Firebase)
    public HelperClass() {
    }

    // Konstruktor dengan parameter
    public HelperClass(String nama, String email, String password) {
        this.nama = nama;
        this.email = email;
        this.password = password;
    }

    // Getter dan Setter
    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
