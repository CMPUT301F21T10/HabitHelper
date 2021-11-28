package com.example.habithelper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

public class UserTest {
    private User mockUser(){
        return new User("John Doe","JohnD@test.ca", "password");
    }

    /**
     * Ensure the generated db data is added in the right order
     */
    @Test
    public void testGenerateDBData(){
        User user = mockUser();
        ArrayList<String> data = user.generateDBData();
        assertEquals(user.getName(), data.get(0));
        assertEquals(user.getEmail(), data.get(1));
    }

    /**
     * Ensure all the relevant data is included in the generated hash function
     */
    @Test
    public void testGenerateAllDBData(){
        User user = mockUser();
        user.addFollower("TEST1");
        user.addFollowing("TEST3");
        user.addRequest("TEST4");

        HashMap<String, ArrayList<String>> userData = user.generateAllDBData();

        //Ensure all the appropriate fields exist
        assertNotEquals(null, userData.get("UserData"));
        assertNotEquals(null, userData.get("Followers"));
        assertNotEquals(null, userData.get("Following"));
        assertNotEquals(null, userData.get("Requests"));

        //Test that the data is in the right places
        assertEquals(user.getName(), userData.get("UserData").get(0));
        assertEquals(user.getEmail(), userData.get("UserData").get(1));
        assertEquals("TEST1", userData.get("Followers").get(0));
        assertEquals("TEST3", userData.get("Following").get(0));
        assertEquals("TEST4", userData.get("Requests").get(0));
    }

    /**
     * Test the addFollower function works
     * It should lengthen the list and actually put the desired string in the list
     */
    @Test
    public void testAddFollower(){
        User user = mockUser();
        assertEquals(0, (int) user.countFollowers());
        user.addFollower("TEST1");
        assertEquals(1, (int) user.countFollowers());
        assertEquals("TEST1", user.getFollowers().get(0));
    }
    /**
     * Test the addFollowing function works
     * It should lengthen the list and actually put the desired string in the list
     */
    @Test
    public void testAddFollowing(){
        User user = mockUser();
        assertEquals(0, (int) user.countFollowing());
        user.addFollowing("TEST1");
        assertEquals(1, (int) user.countFollowing());
        assertEquals("TEST1", user.getFollowing().get(0));
    }
    /**
     * Test the addUserHabit function works
     * It should lengthen the list and actually put the desired string in the list
     */
    @Test
    public void testAddUserHabit(){
        User user = mockUser();
        assertEquals(0, (int) user.countHabits());
        user.addUserHabit(new Habit("TEST1"));
        assertEquals(1, (int) user.countHabits());
        //assertEquals("TEST1", user.getHabits.get(0).getTitle());
    }



}
