package com.example.firealert;

public class Sensor {
    private String id;    // ID sensor
    private String name;  // Nama sensor

    // Konstruktor default diperlukan untuk Firebase
    public Sensor() {
    }

    // Konstruktor dengan parameter
    public Sensor(String id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getter dan Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}