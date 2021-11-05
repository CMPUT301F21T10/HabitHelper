package com.example.habithelper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class FriendsFragmentTest {

    private Solo solo;

    @Rule
    public ActivityTestRule<LoginActivity> rule = new ActivityTestRule<>(LoginActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    /**
     * Gets the Activity
     * @throws Exception
     */
    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }


    @Test
    public void onFollowingSelectButton(){
        //User log in Hardcoding raj in because old yev test is having issues
        solo.enterText((EditText) solo.getView(R.id.loginEditEmail), "fakeraj@fake.com");
        solo.enterText((EditText) solo.getView(R.id.loginEditPassword), "fakeraj");
        solo.clickOnButton("Login");

        solo.clickOnView(solo.getView(R.id.friends_fragment));
        // click on the Following button and check that the expected list comes up by clicking the
        // user from the list and checking for expected name string

        solo.clickOnButton("Following");
        String followingText = solo.clickInList(0).get(0).getText().toString();
        System.out.println(followingText);
        assertEquals("fakeraj2", followingText);
    }

    @Test
    public void onFollowersSelectButton(){
        //User log in Hardcoding raj in because old yev test is having issues
        solo.enterText((EditText) solo.getView(R.id.loginEditEmail), "fakeraj@fake.com");
        solo.enterText((EditText) solo.getView(R.id.loginEditPassword), "fakeraj");
        solo.clickOnButton("Login");

        solo.clickOnView(solo.getView(R.id.friends_fragment));
        // click on the Followers button and check that the expected list comes up by clicking the
        // user from the list and checking for expected name string

        solo.clickOnButton("Followers");
        String followersText = solo.clickInList(0).get(0).getText().toString();
        assertEquals("Fake2", followersText);
    }

}

