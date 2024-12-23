package com.example.firealert;

public class NotificationItem {
    public String time, status, api, suhu, asap;

    public NotificationItem(){}

    public NotificationItem(String time, String status, String api, String suhu, String asap) {
        this.time = time;
        this.status = status;
        this.api = api;
        this.suhu = suhu;
        this.asap = asap;
    }

    public String getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }

    public String getApi() {
        return api;
    }

    public String getSuhu() {
        return suhu;
    }

    public String getAsap() {
        return asap;
    }
}
