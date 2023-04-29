package org.example.cronoplanv2.model;

public class Task {
    private int id;
    private String title;
    private String description;
    private int status;

    public Task(String title, String description, int status) {
        this.title = title;
        this.description = description;
        this.status = status;
    }
    public Task(String title, String description, int status,int id) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.id = id;
    }

    public int getId(){return id;}
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getStatus() {
        return status;
    }
}
