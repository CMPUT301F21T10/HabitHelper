package com.example.habithelper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class LoginActivityTest {

    private Solo solo;

    @Rule
    public ActivityTestRule<LoginActivity> rule =
            new ActivityTestRule<>(LoginActivity.class, true, true);

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

    /**
     * Tests that a user can successfully login and is sent back to the main activity
     */
    @Test
    public void testSuccessfulLogin() throws InterruptedException {
        //Make sure we are on the right activity
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);

        solo.enterText((EditText) solo.getView(R.id.loginEditEmail), "emily@email.ca");
        solo.enterText((EditText) solo.getView(R.id.loginEditPassword), "password");

        solo.clickOnButton("Login");

        solo.waitForText("Authentication Succeeded", 1, 3000);
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }

    /**
     * Tests that on a failed login the user stays on the current page
     */
    @Test
    public void testFailedLogin() throws InterruptedException {
        //Make sure we are on the right activity
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);

        solo.enterText((EditText) solo.getView(R.id.loginEditEmail), "test@test.com");
        solo.enterText((EditText) solo.getView(R.id.loginEditPassword), "p");

        solo.clickOnButton("Login");

        solo.waitForText("Authentication failed.", 1, 3000);
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
    }

    /**
     * Test that on clicking the sign up button we open the sign up fragment
     */
    @Test
    public void testSignUpClick(){
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);

        //Get a hold of the fragment we want to check up on
        Activity activity = rule.getActivity();
        FragmentManager fragmentManager = activity.getFragmentManager();
        Fragment noFragment = fragmentManager.findFragmentById(R.id.newUserFragment);
        //Make sure there is no fragment yet
        assertEquals(null, noFragment);

        //Actually move to open up the fragment now
        solo.clickOnView(solo.getView(R.id.loginButtonSignUp));

        //Check that the fragment has been created
        //Make sure the appropriate text appears somewhere on screen
        Boolean fragmentLoaded = solo.waitForText("New User") && solo.waitForText("Cancel");
        assertEquals(true, fragmentLoaded);

    }

}