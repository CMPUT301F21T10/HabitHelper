package com.example.habithelper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import java.util.HashMap;

public class HabitEventTest {
    private HabitEvent mockHabitEvent(){
        return new HabitEvent(
                "eventTitle",
                "eventComment",
                "eventDateCompleted",
                "associatedHabitTitle",
                "eventLocation",
                0.0,
                1.0);
    }

    /**
     * Ensure that the data we will be passing to firestore is in the right format
     * and everything we want to include is included
     */
    @Test
    public void testGenerateAllHabitEventDBData(){
        HabitEvent event = mockHabitEvent();

        HashMap<String, String> eventData = event.generateAllHabitEventDBData("id");

        //Ensure all the right fields are populated
        assertNotEquals(null, eventData.get("event_title"));
        assertNotEquals(null, eventData.get("event_comment"));
        assertNotEquals(null, eventData.get("event_date_completed"));
        assertNotEquals(null, eventData.get("event_location"));
        assertNotEquals(null, eventData.get("event_photo"));
        assertNotEquals(null, eventData.get("event_associated_habit"));
        assertNotEquals(null, eventData.get("event_id"));
        assertNotEquals(null, eventData.get("lat"));
        assertNotEquals(null, eventData.get("long"));

        //Ensure the fields all have the right data
        assertEquals(event.getEventTitle(), eventData.get("event_title"));
        assertEquals(event.getEventComment(), eventData.get("event_comment"));
        assertEquals(event.getEventDateCompleted(), eventData.get("event_date_completed"));
        assertEquals(event.getEventLocation(), eventData.get("event_location"));
        assertEquals(event.getEventPhoto(), eventData.get("event_photo"));
        assertEquals(event.getAssociatedHabitTitle(), eventData.get("event_associated_habit"));
        assertEquals("id", eventData.get("event_id"));
        assertEquals(event.getLat().toString(), eventData.get("lat"));
        assertEquals(event.getLong().toString(), eventData.get("long"));

    }

    @Test
    public void testSetEventComment(){
        HabitEvent newEvent = mockHabitEvent();
        assertEquals("eventComment", newEvent.getEventComment());

        newEvent.setEventComment("Long long long comment that is way too long and I cannot believe it is this long");
        assertEquals(20, newEvent.getEventComment().length());
    }
}
