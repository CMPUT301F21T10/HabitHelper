package com.example.habithelper;

import static org.junit.Assert.assertEquals;

import android.app.Activity;
import android.app.Fragment;
import android.util.Log;
import android.widget.EditText;

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

        solo.enterText((EditText) solo.getView(R.id.loginEditEmail), "test@fake.fake");
        solo.enterText((EditText) solo.getView(R.id.loginEditPassword), "password");

        solo.clickOnButton("Login");

        solo.waitForText("signInWithEmail:success");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }

    /**
     * Tests that on a failed login the user stays on the current page
     */
    @Test
    public void testFailedLogin() throws InterruptedException {
        //Make sure we are on the right activity
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);

        solo.enterText((EditText) solo.getView(R.id.loginEditEmail), "test@fake.fake");
        solo.enterText((EditText) solo.getView(R.id.loginEditPassword), "p");

        solo.clickOnButton("Login");

        solo.waitForText("Authentication failed.");
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
    }

    /**
     * Test that on clicking the sign up button we open the sign up fragment
     */
    @Test
    public void testSignUpClick(){
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);

        //Working on testing that a fragment is visible
        /*solo.clickOnText("Sign Up", -1);
        Log.d("TESTING", "@string/newUser");

        Fragment fragment =
                solo.getCurrentActivity().getFragmentManager().findFragmentById(R.id.fragment_container_view_tag);

        assertEquals(true, fragment.isVisible());*/
    }

}

