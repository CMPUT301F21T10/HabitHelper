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

        solo.enterText((EditText) solo.getView(R.id.loginEditEmail), "kaznovsk@mail.com");
        solo.enterText((EditText) solo.getView(R.id.loginEditPassword), "yevpass123");
        solo.clickOnButton("Login");

        solo.clickOnView(solo.getView(R.id.friends_fragment));

        solo.clickOnButton("Following");

        String followersText = solo.clickInList(0).get(0).getText().toString();
        assertEquals("Raj Shreyas", followersText);
    }

    @Test
    public void onFollowersSelectButton(){

        solo.enterText((EditText) solo.getView(R.id.loginEditEmail), "kaznovsk@mail.com");
        solo.enterText((EditText) solo.getView(R.id.loginEditPassword), "yevpass123");
        solo.clickOnButton("Login");

        solo.clickOnView(solo.getView(R.id.friends_fragment));
        // click on the Followers button and check that the expected list comes up by clicking the
        // user from the list after using the search bar to filter for exact name, and checking for
        // expected name string

        solo.clickOnButton("Followers");
        solo.enterText((EditText) solo.getView(R.id.search_bar), "A");
        solo.enterText((EditText) solo.getView(R.id.search_bar), "q");
        solo.enterText((EditText) solo.getView(R.id.search_bar), "i");
        solo.enterText((EditText) solo.getView(R.id.search_bar), "l");

        String followingText = solo.clickInList(0).get(0).getText().toString();
        assertEquals("Aqil", followingText);
    }

    @Test
    public void onFollowingUserProfileSelect(){
        solo.enterText((EditText) solo.getView(R.id.loginEditEmail), "kaznovsk@mail.com");
        solo.enterText((EditText) solo.getView(R.id.loginEditPassword), "yevpass123");
        solo.clickOnButton("Login");

        solo.clickOnView(solo.getView(R.id.friends_fragment));

        solo.clickOnButton("Followers");
        String followingText = solo.clickInList(0).get(0).getText().toString();
        solo.waitForText("Aqil");
        solo.waitForText("SEND REQUEST");
    }
}

