package com.example.habithelper;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import org.junit.Test;
import java.util.HashMap;

public class HabitTest {
    private Habit mockHabit(){
        return new Habit("title", "reason", "date", true, "1100001",0);
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
        assertNotEquals(null, habitData.get("days"));
        assertNotEquals(null, habitData.get("numHabitEvents"));

        //Test that the data is in the right places
        assertEquals(newHabit.getTitle(), habitData.get("habit_title"));
        assertEquals(newHabit.getReason(), habitData.get("habit_reason"));
        assertEquals(newHabit.getDateStarted(), habitData.get("habit_date"));
        assertEquals(newHabit.getPublicStatus().toString(), habitData.get("publicStatus"));
        assertEquals(newHabit.getHabitDays(), habitData.get("days"));
        assertEquals(newHabit.getNumHabitEvents(), habitData.get("numHabitEvents"));
    }

    @Test
    public void testSetName(){
        Habit newHabit = mockHabit();
        assertEquals("title", newHabit.getTitle());

        newHabit.setTitle("Long long long title that is way too long");
        assertEquals(20, newHabit.getTitle().length());
    }

    @Test
    public void testSetReason(){
        Habit newHabit = mockHabit();
        assertEquals("reason", newHabit.getReason());

        newHabit.setReason("Long long long reason that is way too long and I cannot believe it is this long");
        assertEquals(30, newHabit.getReason().length());
    }


    @Test
    public void testSetDate(){
        Habit newHabit = mockHabit();
        newHabit.setDateStarted("2021-11-11");
        assertEquals("2021-11-11", newHabit.getDateStarted().toString());
    }

    @Test
    public void testSetPrivacyStatus(){
        Habit newHabit = mockHabit();
        newHabit.setPublicStatus(false);
        assertFalse(newHabit.getPublicStatus());
    }

    @Test
    public void testHabitDays(){
        Habit newHabit = mockHabit();
        newHabit.setHabitDays("1010101");
        assertEquals("1010101", newHabit.getHabitDays());
    }
}
