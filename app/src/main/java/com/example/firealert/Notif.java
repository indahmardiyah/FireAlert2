package com.example.firealert;

public class Notif {
    public long timestamp = 0;
    public String content;
    private String title;

    public Notif(){}

    public Notif(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public long getId() {
        return timestamp;
    }

    public void setId(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return content;
    }

    public void setMessage(String content) {
        this.content = content;
    }
}
