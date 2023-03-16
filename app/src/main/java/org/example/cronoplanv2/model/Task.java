package org.example.cronoplanv2.model;

public class Task {
    private String title;
    private String description;
    private int status;

    public Task(String title, String description, int status) {
        this.title = title;
        this.description = description;
        this.status = status;
    }

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
