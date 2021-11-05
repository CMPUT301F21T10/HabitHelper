package com.example.habithelper;

import static org.junit.Assert.assertEquals;

import android.app.Activity;
import android.text.Editable;
import android.widget.EditText;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/*
* Note: Assumes user already has the following habits before running tests  in the correct order:
* Meditation
* Running
* Rugby
*
* */

public class CreateHabitActivityTest {

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
     * Tests that a user can successfully go to CreateHabitActivity and click on cancel
     */
    @Test
    public void GoToCreateHabitActivityAndCancel(){
        // Asserts that the current activity is the LoginActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);

        solo.enterText((EditText) solo.getView(R.id.loginEditEmail), "fake2@fake.ca");
        solo.enterText((EditText) solo.getView(R.id.loginEditPassword), "123456");

        solo.clickOnButton("Login");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnText("Meditation");
        solo.assertCurrentActivity("Wrong Activity", ViewHabitsActivity.class);

        solo.clickOnView(solo.getView(R.id.goBack));

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }


    /**
     * Tests that a user can successfully go to CreateHabitActivity and click on save to save edited data
     * Assumes that we already have a Meditation habit
     */
    @Test
    public void GoToCreateHabitActivityAndSave(){
        // Asserts that the current activity is the LoginActivity. Otherwise, show “Wrong Activity”
        // logging in
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.loginEditEmail), "fake2@fake.ca");
        solo.enterText((EditText) solo.getView(R.id.loginEditPassword), "123456");
        solo.clickOnButton("Login");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);


        // edit meditation habit and save
        solo.clickOnText("Meditation");
        solo.assertCurrentActivity("Wrong Activity", ViewHabitsActivity.class);

        solo.clearEditText((EditText) solo.getView(R.id.editTextTitle));
        solo.enterText((EditText) solo.getView(R.id.editTextTitle), "Jogging");

        solo.clickOnView(solo.getView(R.id.editTextStartDate));
        solo.setDatePicker(0,2021,2,15);
        solo.clickOnText("OK");

        solo.clearEditText((EditText) solo.getView(R.id.editTextReason));
        solo.enterText((EditText) solo.getView(R.id.editTextReason), "To stay healthy");

        solo.clickOnView(solo.getView(R.id.save));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);


        //Checking if changes has been saved
        solo.clickOnText("Jogging");
        solo.assertCurrentActivity("Wrong Activity", ViewHabitsActivity.class);
        ViewHabitsActivity activity = (ViewHabitsActivity) solo.getCurrentActivity();

        final EditText titleText = activity.editTextTitle;
        String title = titleText.getText().toString();
        assertEquals("Jogging", title);

        final TextView dateText = activity.editTextStartDate;
        String date = dateText.getText().toString();
        assertEquals("2021-3-15", date);

        final EditText reasonText = activity.editTextReason;
        String reason = reasonText.getText().toString();
        assertEquals("To stay healthy", reason);
    }


    /**
     * Tests that a user can successfully go to CreateHabitActivity and click on delete to delete a habit
     * Assumes that we already have a Rugby habit
     */
    @Test
    public void GoToCreateHabitActivityAndDelete(){
        // Asserts that the current activity is the LoginActivity. Otherwise, show “Wrong Activity”
        // logging in
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.loginEditEmail), "fake2@fake.ca");
        solo.enterText((EditText) solo.getView(R.id.loginEditPassword), "123456");
        solo.clickOnButton("Login");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);


        // edit meditation habit and save
        solo.clickOnText("Running");
        solo.assertCurrentActivity("Wrong Activity", ViewHabitsActivity.class);

        solo.clickOnView(solo.getView(R.id.delete));

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        MainActivity activity = (MainActivity) solo.getCurrentActivity();
    }

    /**
     * Checks if habit Running has been deleted by previous test
     */
    @Test
    public void checkIfHabitDeleted() {

        // Asserts that the current activity is the LoginActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);

        solo.enterText((EditText) solo.getView(R.id.loginEditEmail), "fake2@fake.ca");
        solo.enterText((EditText) solo.getView(R.id.loginEditPassword), "123456");

        solo.clickOnButton("Login");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        assertEquals(false, solo.searchText("Running"));

    }

}
