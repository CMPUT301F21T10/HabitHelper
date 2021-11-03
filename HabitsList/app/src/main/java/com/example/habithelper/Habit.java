package com.example.habithelper;

import java.io.Serializable;

public class Habit implements Serializable {

    private String title;
    private String reason;
    private String dateStarted;
    private Boolean publicStatus;


    public Habit() {
    }

    public Habit(String title, String reason, String dateStarted, Boolean publicStatus) {
        this.title = title;
        this.reason = reason;
        this.dateStarted = dateStarted;
        this.publicStatus = publicStatus;
    }

    public Habit(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDateStarted() {
        return dateStarted;
    }

    public void setDateStarted(String dateStarted) {
        this.dateStarted = dateStarted;
    }

    public Boolean getPublicStatus() {
        return publicStatus;
    }

    public void setPublicStatus(Boolean publicStatus) {
        this.publicStatus = publicStatus;
    }
}
