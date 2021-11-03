package com.example.habithelper;

import java.io.Serializable;

public class HabitEvent implements Serializable {

    private String EventTitle;
    private String EventComment;
    private String EventDateCompleted;
    private String EventLocation;
    private String AssociatedHabitId;
    private String EventPhotograph;

    public HabitEvent(String eventTitle, String eventComment, String eventDateCompleted) {
        EventTitle = eventTitle;
        EventComment = eventComment;
        EventDateCompleted = eventDateCompleted;
    }

    public String getEventTitle() {
        return EventTitle;
    }

    public void setEventTitle(String eventTitle) {
        EventTitle = eventTitle;
    }

    public String getEventComment() {
        return EventComment;
    }

    public void setEventComment(String eventComment) {
        EventComment = eventComment;
    }

    public String getEventDateCompleted() {
        return EventDateCompleted;
    }

    public void setEventDateCompleted(String eventDateCompleted) {
        EventDateCompleted = eventDateCompleted;
    }

    public String getEventLocation() {
        return EventLocation;
    }

    public void setEventLocation(String eventLocation) {
        EventLocation = eventLocation;
    }

    public String getAssociatedHabitId() {
        return AssociatedHabitId;
    }

    public void setAssociatedHabitId(String associatedHabitId) {
        AssociatedHabitId = associatedHabitId;
    }

    public String getEventPhotograph() {
        return EventPhotograph;
    }

    public void setEventPhotograph(String eventPhotograph) {
        EventPhotograph = eventPhotograph;
    }
}
