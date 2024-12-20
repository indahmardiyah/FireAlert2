package com.example.firealert;

public class Sensor {
    private String id;    // ID sensor
    private String name;  // Nama sensor
    private String value; // Nilai sensor

    // Konstruktor default diperlukan untuk Firebase
    public Sensor() {
    }

    // Konstruktor dengan parameter
    public Sensor(String id, String name, String value) {
        this.id = id;
        this.name = name;
        this.value = value;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}