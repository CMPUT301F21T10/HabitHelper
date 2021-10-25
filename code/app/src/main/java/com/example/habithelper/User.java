package com.example.habithelper;

import java.util.ArrayList;
import java.security.MessageDigest;

public class User {
    public String userName;
    public String password;
    public String name;
    public Integer ID;
    public ArrayList<Habit> habits;
    public ArrayList<Integer> followers;
    public ArrayList<Integer> following;
    public ArrayList<Integer> requests;

    /**
     * Basic constructor for when a user does not yet have an ID defined in the database
     * @param userName
     * @param password
     * @param name
     */
    public User(String name, String userName, String password) {
        this.userName = userName;
        this.password = password;
        this.name = name;
        this.ID = -1;

        this.followers = new ArrayList<Integer>();
        this.following = new ArrayList<Integer>();
        this.habits = new ArrayList<Habit>();
        this.requests = new ArrayList<Integer>();
    }

    /**
     * Constructor for when the user has an ID already defined in the database
     * @param userName
     * @param password
     * @param name
     * @param ID
     */
    public User(String name, String userName, String password, Integer ID) {
        this.userName = userName;
        this.password = password;
        this.name = name;
        this.ID = ID;

        this.followers = new ArrayList<Integer>();
        this.following = new ArrayList<Integer>();
        this.habits = new ArrayList<Habit>();
        this.requests = new ArrayList<Integer>();
    }

    /**
     * Format the User data in a way usable in Firestore
     * @return An array list with all the necessary data to construct a User object
     */
    public ArrayList<String> generateDBData(){
        ArrayList<String> DBData = new ArrayList<>();
        DBData.add(this.name);
        DBData.add(this.userName);

        //Convert the password to a hash function if the user already exists in the table
        //Safe to assume the password is already hashed if the ID is not -1
        //And if it is hashed we should not hash it again
        if (this.ID == -1){
            /*MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.update(this.password.getBytes());
            String passwordHash = new String(messageDigest.digest());
            DBData.add(passwordHash);*/
        } else{
            DBData.add(this.password);
        }
        return DBData;
    }


}