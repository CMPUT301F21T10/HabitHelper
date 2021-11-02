package com.example.habithelper;

import java.util.ArrayList;
import java.util.Date;

public class Habit {

    public String title;
    public String reason;
    public Date startDate;
    public ArrayList<String> daysActive;
    public Integer frequency;
    public String comment;
    //public eventList arrayList<HabitEvent>

    public Habit(String title) {
        this.title = title;
    }

    /**
     * Put the object into a form usable in the DB
     * @return the habit object in a form usable by the DB
     */
    public String generateDBData(){
        ArrayList<String> DBData = new ArrayList<>();
        DBData.add(this.title);

        return this.title;
    }
}
