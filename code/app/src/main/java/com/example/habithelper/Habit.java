package com.example.habithelper;

import com.google.firebase.firestore.DocumentSnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This is a class that represents a Habit object
 * */
public class Habit implements Serializable {

    private String title;
    private String reason;
    private String dateStarted;
    private Boolean publicStatus;

    /**
     * This constructor takes in data from the database and converts it into a Habit object
     * @param doc
     *  the data pulled from the DB document
     */
    public Habit(DocumentSnapshot doc) {
        ArrayList<String> DBData = new ArrayList<>();

        this.title = (String) doc.get("habit_title");
        this.reason = (String) doc.get("habit_reason");
        this.dateStarted = (String) doc.get("habit_date");

        String publicStatus_str = (String) doc.get("publicStatus");
        if (publicStatus_str == "true"){
            this.publicStatus = true;
        }
        else{
            this.publicStatus = false;
        }

    }

    /**
     * Empty constructor for the Habit class
     * */
    public Habit() {
    }

    /**
     * Constructor for the Habit class that has all the attributes as parameters
     * @param title
     * The habit title/name
     * @param reason
     * The reason of doing the habit
     * @param dateStarted
     * The date the habit started
     * @param publicStatus
     * Denotes whether the habit is public or not
     */
    public Habit(String title, String reason, String dateStarted, Boolean publicStatus) {
        this.title = title;
        this.reason = reason;
        this.dateStarted = dateStarted;
        this.publicStatus = publicStatus;
    }

    /**
     * Constructor for Habit class, with only title as parameter
     * @param title
     * The habit title/name
     */
    public Habit(String title) {
        this.title = title;
    }

    /**
     * Returns the title of a habit object
     * @return
     */
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

//    /**
//     * Put the object into a form usable in the DB
//     * @return the habit object in a form usable by the DB
//     */
//    public String generateDBData(){
//        ArrayList<String> DBData = new ArrayList<>();
//        DBData.add(this.title);
//
//        return this.title;
//    }

    /**
     * Translate all the data into a form usable by the DB
     * @return a hashmap of all the data for this user
     */
    public HashMap<String, String> generateAllHabitDBData(){
        HashMap<String, String> newHabitData = new HashMap<>();
        newHabitData.put("habit_title", this.title);
        newHabitData.put("habit_reason", this.reason);
        newHabitData.put("habit_date", this.dateStarted);
        newHabitData.put("publicStatus", this.publicStatus.toString());

        return newHabitData;
    }


}
