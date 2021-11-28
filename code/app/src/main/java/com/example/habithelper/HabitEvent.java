/*
Copyright 2021 CMPUT301F21T10

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.example.habithelper;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * This is a class that represents a Habit object
 * */
public class HabitEvent implements Serializable {

    private String EventTitle;
    private String EventComment;
    private String EventDateCompleted;
    private String EventLocation;
    private String EventPhoto;
    private String AssociatedHabitTitle;
    private String EventId;
    private Double Lat;
    private Double Long;


    /**
     * This constructor takes in data from the database and converts it into a HabitEvent object
     * @param doc
     *  the data pulled from the DB document
     */
    public HabitEvent(DocumentSnapshot doc) {
        this.EventTitle = (String) doc.get("event_title");
        this.EventComment = (String) doc.get("event_comment");
        this.EventDateCompleted = (String) doc.get("event_date_completed");
        this.EventLocation = (String) doc.get("event_location");
        this.EventPhoto = (String) doc.get("event_photo");
        this.AssociatedHabitTitle = (String) doc.get("event_associated_habit");
        this.EventId = (String) doc.get("event_id");
        this.Lat = Double.valueOf((String) doc.get("lat"));
        this.Long = Double.valueOf((String) doc.get("long"));
    }

    /**
     * Constructor for HabitEvent class
     * @param eventTitle
     *  The title/name of the habit event
     * @param eventComment
     *  The comment for the habit event
     * @param eventDateCompleted
     *  The date the habit event was completed
     * @param eventLocation
     *  The location of the habit event
     * @param Lat
     *  Latitude of event location
     * @param Long
     *  Longitude of event location
     */
    public HabitEvent(String eventTitle, String eventComment, String eventDateCompleted, String associatedHabitTitle,
                      String eventLocation, Double Lat, Double Long) {
        this.EventTitle = eventTitle;
        this.setEventComment(eventComment);
        this.EventDateCompleted = eventDateCompleted;
        this.AssociatedHabitTitle = associatedHabitTitle;
        this.EventLocation = eventLocation;
        this.Lat = Lat;
        this.Long = Long;
        this.EventPhoto = "";
    }


    /**
     * Get the title/name of the habit event
     * @return
     * The title/name of the habit event
     */
    public String getEventTitle() {
        return EventTitle;
    }

    /**
     * Set the title/name of the habit event
     * @param eventTitle
     * The title/name of the habit event
     */
    public void setEventTitle(String eventTitle) {
        EventTitle = eventTitle;
    }

    /**
     * Get the comment for the habit event
     * @return
     * The habit event's comment
     */
    public String getEventComment() {
        return EventComment;
    }

    /**
     * Set a comment for the habot event, ensuring the length is no more than
     * 20 characters
     * @param eventComment
     * The habit event's comment
     */
    public void setEventComment(String eventComment) {
        if (eventComment.length() > 20){
            this.EventComment = eventComment.substring(0, 20);
        }else{
            this.EventComment = eventComment;
        }
    }

    /**
     * Get the date the habit event was completed
     * @return
     * The date the habit event was completed
     */
    public String getEventDateCompleted() {
        return EventDateCompleted;
    }

    /**
     * Set the date the habit event was completed
     * @param eventDateCompleted
     * The date the habit event was completed
     */
    public void setEventDateCompleted(String eventDateCompleted) {
        EventDateCompleted = eventDateCompleted;
    }

    /**
     * Returns the habit event's location
     * @return
     * The habit event's location
     */
    public String getEventLocation() {
        return EventLocation;
    }

    /**
     * Set the habit event's location
     * @param eventLocation
     * The habit event's location
     */
    public void setEventLocation(String eventLocation) {
        EventLocation = eventLocation;
    }

    /**
     * Returns the habit associated with this event
     * @return
     * The id of the habit associated with this event
     */
    public String getAssociatedHabitTitle() {
        return AssociatedHabitTitle;
    }

    /**
     * Set the habit associated with this event
     * @param associatedHabitTitle
     * The id of the habit associated with this event
     */
    public void setAssociatedHabitTitle(String associatedHabitTitle) {
        this.AssociatedHabitTitle = associatedHabitTitle;
    }

    /**
     * Gets the id used in db for this event
     * @return
     *  the id used in db for this event
     */
    public String getEventId() {
        return EventId;
    }

    public String getEventPhoto() {
        return EventPhoto;
    }

    public void setEventPhoto(String eventPhoto) {
        EventPhoto = eventPhoto;
    }

    public Double getLat() {
        return Lat;
    }

    public void setLat(Double lat) {
        Lat = lat;
    }

    public Double getLong() {
        return Long;
    }

    public void setLong(Double aLong) {
        Long = aLong;
    }

    /**
     * Translate all the data into a form usable by the DB
     * @return
     * a hashmap of all the data for this habit Event
     */
    public HashMap<String, String> generateAllHabitEventDBData(String id){
        HashMap<String, String> newHabitData = new HashMap<>();
        newHabitData.put("event_title", this.EventTitle);
        newHabitData.put("event_comment", this.EventComment);
        newHabitData.put("event_date_completed", this.EventDateCompleted);
        newHabitData.put("event_location", this.EventLocation);
        newHabitData.put("event_photo", this.EventPhoto);
        newHabitData.put("event_associated_habit", this.AssociatedHabitTitle);
        newHabitData.put("event_id", id);
        newHabitData.put("lat", String.valueOf(this.Lat));
        newHabitData.put("long", String.valueOf(this.Long));

        return newHabitData;
    }

    /**
     * Add a HabitEvent object to the database
     * @param email
     *      the email of the user for which the HabitEvent is to be added
     * @param db
     *      the firestore instance
     */
    public void addHabitEventToDB(String email, FirebaseFirestore db){
        CollectionReference colRef = db.collection("Habits")
                .document(email)
                .collection(email + "_habits")
                .document(this.AssociatedHabitTitle).collection("HabitEvents");

        String id = colRef.document().getId();
        DocumentReference docRefAdd = colRef.document(id);

        // write to db
        docRefAdd.set(this.generateAllHabitEventDBData(id))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        // These are a method which gets executed when the task is succeeded
                        Log.d("DATA_ADDED", "Data has been added successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // These are a method which gets executed if there’s any problem
                        Log.d(TAG, "Data could not be added!" + e.toString());
                    }
                });
    }


    /**
     * Delete a HabitEvent object to database
     * @param email
     *      the email of the user for which the HabitEvent is to be deleted
     * @param db
     *      the firestore instance
     */
    public void deleteHabitEventFromDB(String email, FirebaseFirestore db){
        DocumentReference docRefDelete = db.collection("Habits")
                .document(email)
                .collection(email + "_habits")
                .document(this.AssociatedHabitTitle).collection("HabitEvents")
                .document(this.EventId);

        docRefDelete.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("DELETED", "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("ERROR_DELETE", "Error deleting document", e);
                    }
                });
    }

}


