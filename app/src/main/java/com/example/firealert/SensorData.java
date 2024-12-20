package com.example.firealert;

public class SensorData {
    private int flame;
    private double lpg;
    private double suhu;

    // Constructor kosong diperlukan oleh Firebase
    public SensorData() {}

    // Constructor dengan parameter
    public SensorData(int flame, double lpg, double suhu, String timestamp) {
        this.flame = flame;
        this.lpg = lpg;
        this.suhu = suhu;
    }

    // Getter dan Setter
    public int getFlame() {
        return flame;
    }

    public void setFlame(int flame) {
        this.flame = flame;
    }

    public double getLpg() {
        return lpg;
    }

    public void setLpg(double lpg) {
        this.lpg = lpg;
    }

    public double getSuhu() {
        return suhu;
    }

    public void setSuhu(double suhu) {
        this.suhu = suhu;
    }
}

