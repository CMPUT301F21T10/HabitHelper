package com.example.habithelper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

public class HabitTest {
    private Habit mockHabit(){
        return new Habit("title", "reason", "dateStarted", true);
    }

    @Test
    public void testGenerateAllHabitDBData(){
        Habit newHabit = mockHabit();

        HashMap<String, String> habitData = newHabit.generateAllHabitDBData();

        //Ensure all the appropriate fields exist
        assertNotEquals(null, habitData.get("habit_title"));
        assertNotEquals(null, habitData.get("habit_reason"));
        assertNotEquals(null, habitData.get("habit_date"));
        assertNotEquals(null, habitData.get("publicStatus"));

        //Test that the data is in the right places
        assertEquals(newHabit.getTitle(), habitData.get("habit_title"));
        assertEquals(newHabit.getReason(), habitData.get("habit_reason"));
        assertEquals(newHabit.getDateStarted(), habitData.get("habit_date"));
        assertEquals(newHabit.getPublicStatus().toString(), habitData.get("publicStatus"));
    }



    @Test
    public void testSetName(){
        Habit newHabit = mockHabit();
        assertEquals("title", newHabit.getTitle());

        newHabit.setTitle("Long long long title that is way too long");
        assertEquals("Long long long title", newHabit.getTitle());
    }

    @Test
    public void testSetReason(){
        Habit newHabit = mockHabit();
        assertEquals("reason", newHabit.getReason());

        newHabit.setTitle("Long long long reason that is way too long");
        assertEquals("Long long long reason that is ", newHabit.getTitle());
    }
}
