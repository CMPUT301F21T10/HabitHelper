package com.example.habithelper;

import java.util.ArrayList;

public class User {
    public String userName;
    public String password;
    public String name;
    public Integer ID;
    public ArrayList<Habit> habits;
    public ArrayList<Integer> followers;
    public ArrayList<Integer> following;

    /**
     * Basic constructor for when a user does not yet have an ID defined in the database
     * @param userName
     * @param password
     * @param name
     */
    public User(String userName, String password, String name) {
        this.userName = userName;
        this.password = password;
        this.name = name;
        this.ID = -1;

        this.followers = new ArrayList<Integer>();
        this.following = new ArrayList<Integer>();
        this.habits = new ArrayList<Habit>();
    }

    /**
     * Constructor for when the user has an ID already defined in the database
     * @param userName
     * @param password
     * @param name
     * @param ID
     */
    public User(String userName, String password, String name, Integer ID) {
        this.userName = userName;
        this.password = password;
        this.name = name;
        this.ID = ID;

        this.followers = new ArrayList<Integer>();
        this.following = new ArrayList<Integer>();
        this.habits = new ArrayList<Habit>();
    }


}
