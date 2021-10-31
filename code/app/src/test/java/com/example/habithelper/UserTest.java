package com.example.habithelper;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.ArrayList;

public class UserTest {
    private User mockUser(){
        return new User("johnd77", "AA", "John Doe");
    }

    @Test
    public void testGenerateDBData(){
        User user = mockUser();
        ArrayList<String> data = user.generateDBData();
        assertEquals(user.name, data.get(0));
        assertEquals(user.email, data.get(1));
    }
}
