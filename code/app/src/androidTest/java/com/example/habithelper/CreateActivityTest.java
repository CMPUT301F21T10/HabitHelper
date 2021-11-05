package com.example.habithelper;

import android.app.Activity;
import android.widget.EditText;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class CreateActivityTest {

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

    /**
     * Create a habit and check if the habit is added
     */
    @Test
    public void checkHabit(){
        //User log in
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.loginEditEmail), "fake2@fake.ca");
        solo.enterText((EditText) solo.getView(R.id.loginEditPassword), "123456");
        solo.clickOnButton("Login");

        //Clicking on add button
        solo.clickOnView(solo.getView(R.id.fab));
        solo.waitForText("Select Days");

        //Entering habit details
        solo.assertCurrentActivity("Wrong Activity", CreateHabitActivity.class);
        solo.enterText((EditText) solo.getView(R.id.editTextTitle), "Running");
        solo.clickOnText("Starting Date");
        solo.setDatePicker(0,2021,2,15);
        solo.clickOnText("OK");
        solo.enterText((EditText) solo.getView(R.id.editTextReason), "To keep fit");
        solo.clickOnView(solo.getView(R.id.createHabit));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        //Checking if habit has been created
        solo.clickOnText("Running");
        solo.assertCurrentActivity("Wrong Activity", ViewHabitsActivity.class);
        solo.waitForText("Running");
    }

}
