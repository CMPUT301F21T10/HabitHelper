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

import java.io.Serializable;


/**
 * This is a class that represents a Habit object
 * */
public class HabitEvent implements Serializable {

    private String EventTitle;
    private String EventComment;
    private String EventDateCompleted;
    private String EventLocation;
    private String AssociatedHabitId;

    /**
     * Constructor for HabitEvent class
     * @param eventTitle
     * The title/name of the habit event
     * @param eventComment
     * The comment for the habit event
     * @param eventDateCompleted
     * The date the habit event was completed
     */
    public HabitEvent(String eventTitle, String eventComment, String eventDateCompleted) {
        EventTitle = eventTitle;
        EventComment = eventComment;
        EventDateCompleted = eventDateCompleted;
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
     * Set a comment for the habot event
     * @param eventComment
     * The habit event's comment
     */
    public void setEventComment(String eventComment) {
        EventComment = eventComment;
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
     * Returns the id of the event associated with this event
     * @return
     * The id of the habit associated with this event
     */
    public String getAssociatedHabitId() {
        return AssociatedHabitId;
    }

    /**
     * Set the id associated with this event
     * @param associatedHabitId
     * The id of the habit associated with this event
     */
    public void setAssociatedHabitId(String associatedHabitId) {
        AssociatedHabitId = associatedHabitId;
    }
}
