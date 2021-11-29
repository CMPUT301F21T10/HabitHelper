package com.example.habithelper;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.robotium.solo.Solo;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
/**
 * NOTE: PLEASE RUN THE TESTS ONE BY ONE IN ORDER. OTHERWISE TESTS WILL/MIGHT FAIL.
 * ASSUMES THAT "Running" HABIT OBJECT ALREADY EXISTS
 */
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
        solo.enterText((EditText) solo.getView(R.id.loginEditEmail), "test@test.ca");
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
        solo.setDatePicker(0,2020,12,14);
        solo.clickOnText("OK");
        solo.enterText((EditText) solo.getView(R.id.editTextOptionalComments), "Ran for 64 hours");
        solo.clickOnView(solo.getView(R.id.createHabit));

        //Check if habit event has been added
        solo.clickOnView(solo.getView(R.id.events_fragment));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.waitForText("Running event");
    }

    /**
     * CANCEL EDITING A HABIT EVENT
     * Tests that a user can successfully go to ViewHabitEventActivity and click on cancel
     */
    @Test
    public void GoToViewHabitEventActivityAndCancel(){
        // Asserts that the current activity is the LoginActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.loginEditEmail), "test@test.ca");
        solo.enterText((EditText) solo.getView(R.id.loginEditPassword), "123456");
        solo.clickOnButton("Login");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        //Go to events fragment
        solo.clickOnView(solo.getView(R.id.events_fragment));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnText("Running Event");

        //Cancelling
        solo.clickOnView(solo.getView(R.id.goBack));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }

    /**
     * Delete a habit event
     */
    @Test
    public void deleteHabitEvent(){
        //User log in
        solo.enterText((EditText) solo.getView(R.id.loginEditEmail), "test@test.ca");
        solo.enterText((EditText) solo.getView(R.id.loginEditPassword), "123456");
        solo.clickOnButton("Login");

        //Go to events fragment
        solo.clickOnView(solo.getView(R.id.events_fragment));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnText("Running Event");
        solo.clickOnView(solo.getView(R.id.delete));
    }

    /**
     * Check if the habit event has been deleted
     */
    @Test
    public void checkIfDeleted(){
        //User log in
        solo.enterText((EditText) solo.getView(R.id.loginEditEmail), "test@test.ca");
        solo.enterText((EditText) solo.getView(R.id.loginEditPassword), "123456");
        solo.clickOnButton("Login");

        //Go to events fragment
        solo.clickOnView(solo.getView(R.id.events_fragment));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        assertFalse(solo.searchText("Running Event"));
    }


    /**
     * CANCEL CREATING A HABIT EVENT
     * Tests that a user can successfully go to CreateHabitEventActivity and click on cancel
     */
    @Test
    public void GoToCreateHabitEventActivityAndCancel(){
        // Asserts that the current activity is the LoginActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.loginEditEmail), "test@test.ca");
        solo.enterText((EditText) solo.getView(R.id.loginEditPassword), "123456");
        solo.clickOnButton("Login");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

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

        //Cancelling
        solo.clickOnView(solo.getView(R.id.goBack));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }
}
