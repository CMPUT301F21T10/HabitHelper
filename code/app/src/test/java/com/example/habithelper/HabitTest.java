package com.example.habithelper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

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
}
