package org.example.cronoplanv2.model;


public class Settings {
    private int time;
    private String user_id;

    public Settings(int time,String user_id) {
        this.time = time;
        this.user_id = user_id;
    }

    public void setTime(int time){this.time = time;}

    public int getTime(){return time;}
}
