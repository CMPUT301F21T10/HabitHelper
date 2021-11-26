package com.example.habithelper;

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

public class CreateHabitEventActivitytest {
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
     * Check if swiping on habit works
     * Create a habit event
     * Check if the habit event has been added
     */
    @Test
    public void createHabitEvent(){
        //User log in
        solo.enterText((EditText) solo.getView(R.id.loginEditEmail), "fake2@fake.ca");
        solo.enterText((EditText) solo.getView(R.id.loginEditPassword), "123456");
        solo.clickOnButton("Login");

        //Swiping
        int fromX, toX, fromY, toY;
        int[] location = new int[2];
        View row = solo.getText("Running");
        row.getLocationInWindow(location);
        if (location.length == 0) fail("Could not find habit: Running");
        fromX = location[0];
        fromY = location[1];
        toX = location[0]+1000;
        toY = fromY;
        solo.drag(fromX, toX, fromY, toY, 10);

        //Add habit event details
        solo.clickOnText("Date Completed");
        solo.setDatePicker(0,2021,12,14);
        solo.clickOnText("OK");
        solo.enterText((EditText) solo.getView(R.id.editTextOptionalComments), "Ran for 64 hours");
        //solo.enterText((EditText) solo.getView(R.id.editTextOptionalLocation), "Anfield Stadium");
        solo.waitForText("Mount Everest Peak");
        solo.clickOnView(solo.getView(R.id.createHabit));

        //Check if habit event has been added
        solo.clickOnView(solo.getView(R.id.events_fragment));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }
}
