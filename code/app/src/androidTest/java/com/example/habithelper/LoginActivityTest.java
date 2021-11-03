package com.example.habithelper;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;

import android.app.Activity;
import android.app.Fragment;
import android.util.Log;
import android.widget.EditText;

import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.atomic.AtomicBoolean;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule =
            new ActivityTestRule<>(LoginActivity.class);

    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @BeforeClass
    public static void setupProfileTest() {
        Intents.init();

        User newUser = new User("NAME", "EMAIL", "PASSWORD");
    }

    @AfterClass
    public static void tearDownProfileTest() {

    }

    /**
     * Gets the Activity
     * @throws Exception
     */
//    @Test
//    public void start() throws Exception{
//        Activity activity = rule.getActivity();
//    }

    /**
     * Tests that a user can successfully login and is sent back to the main activity
     */
    @Test
    public void testSuccessfulLogin() throws InterruptedException {
        //Make sure we are on the right activity
        //solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        //intended(hasComponent(LoginActivity.class.getName()));


        //onView(withId(R.id.loginEditEmail)).perform(typeText("test@fake.fake"));
        //onView(withId(R.id.loginEditPassword)).perform(typeText("password"));

        //onView(withId(R.id.loginButtonLogin)).perform(click());

        //intended(hasComponent(MainActivity.class.getName()));
    }

//    /**
//     * Tests that on a failed login the user stays on the current page
//     */
//    @Test
//    public void testFailedLogin() throws InterruptedException {
//        //Make sure we are on the right activity
//        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
//
//        solo.enterText((EditText) solo.getView(R.id.loginEditEmail), "test@fake.fake");
//        solo.enterText((EditText) solo.getView(R.id.loginEditPassword), "p");
//
//        solo.clickOnButton("Login");
//
//        solo.waitForText("Authentication failed.");
//        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
//    }
//
//    /**
//     * Test that on clicking the sign up button we open the sign up fragment
//     */
//    @Test
//    public void testSignUpClick(){
//        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
//
//        //Working on testing that a fragment is visible
//        /*solo.clickOnText("Sign Up", -1);
//        Log.d("TESTING", "@string/newUser");
//
//        Fragment fragment =
//                solo.getCurrentActivity().getFragmentManager().findFragmentById(R.id.fragment_container_view_tag);
//
//        assertEquals(true, fragment.isVisible());*/
//    }

}

