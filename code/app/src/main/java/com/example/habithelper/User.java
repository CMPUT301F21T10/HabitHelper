package com.example.habithelper;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class User {
    public String userName;
    public String password;
    public String name;
    public String ID;
    public ArrayList<Habit> habits;
    public ArrayList<String> followers;
    public ArrayList<String> following;
    public ArrayList<String> requests;

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
        this.ID = "-1";

        this.followers = new ArrayList<String>();
        this.following = new ArrayList<String>();
        this.habits = new ArrayList<Habit>();
        this.requests = new ArrayList<String>();
    }

    /**
     * Constructor for when the user has an ID already defined in the database
     * @param userName
     * @param password
     * @param name
     * @param ID
     */
    public User(String name, String userName, String password, String ID) {
        this.userName = userName;
        this.password = password;
        this.name = name;
        this.ID = ID;

        this.followers = new ArrayList<String>();
        this.following = new ArrayList<String>();
        this.habits = new ArrayList<Habit>();
        this.requests = new ArrayList<String>();
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
        if (this.ID == "-1"){
            /*MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.update(this.password.getBytes());
            String passwordHash = new String(messageDigest.digest());
            DBData.add(passwordHash);*/
        } else{
            DBData.add(this.password);
        }
        return DBData;
    }

    /**
     * Format the Request data in a way usable in Firestore
     * @return An array list of the requests to this user
     */
    public ArrayList<String> generateRequestList(){
        ArrayList<String> DBData = new ArrayList<>();
        //Convert the password to a hash function if the user already exists in the table
        //Safe to assume the password is already hashed if the ID is not -1
        //And if it is hashed we should not hash it again
        if (this.ID == "-1"){
            /*MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.update(this.password.getBytes());
            String passwordHash = new String(messageDigest.digest());
            DBData.add(passwordHash);*/
        } else{
            DBData.add(this.password);
        }
        return DBData;
    }

    /**
     * Format the followers data in a way usable in Firestore
     * @return An array list of the followers of this user
     */
    public ArrayList<String> generateFollowersList(){
        ArrayList<String> DBData = new ArrayList<>();
        DBData.add("Raj");
        DBData.add("Yevhen");
        DBData.add("Emily");
        //Convert the password to a hash function if the user already exists in the table
        //Safe to assume the password is already hashed if the ID is not -1
        //And if it is hashed we should not hash it again
        if (this.ID == "-1"){
            /*MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.update(this.password.getBytes());
            String passwordHash = new String(messageDigest.digest());
            DBData.add(passwordHash);*/
        } else{
            DBData.add(this.password);
        }
        return DBData;
    }

    /**
     * Format the following data in a way usable in Firestore
     * @return An array list of the user that user is following
     */
    public ArrayList<String> generateFollowingList(){
        ArrayList<String> DBData = new ArrayList<>();
        //Convert the password to a hash function if the user already exists in the table
        //Safe to assume the password is already hashed if the ID is not -1
        //And if it is hashed we should not hash it again
        if (this.ID == "-1"){
            /*MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.update(this.password.getBytes());
            String passwordHash = new String(messageDigest.digest());
            DBData.add(passwordHash);*/
        } else{
            DBData.add(this.password);
        }
        return DBData;
    }

    /**
     * @param FollowingId: Id of the user who sent the follow request
     * Sends request by adding FollowingId to list of requests of the receiver's Requests list.
     */
    public void sendRequest(String FollowingId){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Users").document(FollowingId);
        docRef.update("Requests", FieldValue.arrayUnion(FollowingId));
    }

    /**
     * @param FollowerId: Id of the user who received the request.
     * @param accept
     */
    public void acceptRequest(String FollowerId, boolean accept){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Users").document(FollowerId);
        docRef.update("Requests", FieldValue.arrayRemove(FollowerId));
        if(accept){
            docRef.update("Following", FieldValue.arrayUnion(FollowerId));
            docRef.update("Followers", FieldValue.arrayUnion(this.ID));
        }
    }
}