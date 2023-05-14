package org.example.cronoplanv2.model;


public class Settings {
    private int time;
    private String user_id;
    private int chart_ammount;
    private int chart_time_interval;

    public Settings(int time, String user_id, int chart_ammount, int chart_time_interval) {
        this.time = time;
        this.user_id = user_id;
        this.chart_ammount = chart_ammount;
        this.chart_time_interval = chart_time_interval;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getChart_ammount() {
        return chart_ammount;
    }

    public void setChart_ammount(int chart_ammount) {
        this.chart_ammount = chart_ammount;
    }

    public int getChart_time_interval() {
        return chart_time_interval;
    }

    public void setChart_time_interval(int chart_time_interval) {
        this.chart_time_interval = chart_time_interval;
    }
}
