package com.example.habithelper;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

//RUN AFTER SENDFOLLOWTEST
public class AcceptFollowTest {
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
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
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
     * Tests that a user can successfully send a follow request
     */
    @Test
    public void AcceptFollowRequestTest(){

        // Asserts that the current activity is the LoginActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);

        solo.enterText((EditText) solo.getView(R.id.loginEditEmail), "fakeraj2@fake.com");
        solo.enterText((EditText) solo.getView(R.id.loginEditPassword), "fakeraj");
        solo.clickOnButton("Login");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.clickOnView(solo.getView(R.id.friends_fragment));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.clickOnView(solo.getView(R.id.requestAlert));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.clickOnView(solo.getView(R.id.fragRequestsListView));
        solo.clickOnText("accept");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.clickOnView(solo.getView(R.id.friends_fragment));
        solo.clickOnButton("Followers");
        String followersText = solo.clickInList(0).get(0).getText().toString();
        assertEquals("fakeraj", followersText);

    }


}
