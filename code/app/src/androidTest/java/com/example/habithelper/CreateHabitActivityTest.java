package com.example.habithelper;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import android.app.Activity;
import android.widget.EditText;
import android.widget.TextView;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.robotium.solo.Solo;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * NOTE: PLEASE RUN THE TESTS ONE BY ONE IN ORDER. OTHERWISE TESTS WILL/MIGHT FAIL.
 */

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
     * CREATING A HABIT
     * Create a habit and check if the habit is added
     */
    @Test
    public void checkHabit(){
        //User log in
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.loginEditEmail), "test@test.ca");
        solo.enterText((EditText) solo.getView(R.id.loginEditPassword), "123456");
        solo.clickOnButton("Login");

        //Clicking on add button
        solo.clickOnView(solo.getView(R.id.fab));
        solo.waitForText("Select Days");

        //Entering habit details
        solo.assertCurrentActivity("Wrong Activity", CreateHabitActivity.class);
        solo.enterText((EditText) solo.getView(R.id.editTextTitle), "Meditation");
        solo.clickOnText("Starting Date");
        solo.setDatePicker(0,2021,2,15);
        solo.clickOnText("OK");
        solo.enterText((EditText) solo.getView(R.id.editTextReason), "Reduce stress of android class");
        CreateHabitActivity activity = (CreateHabitActivity) solo.getCurrentActivity();
        //Selecting days: MON, TUE, FRI, SAT
        solo.clickOnView(solo.getView(R.id.mon_btn));
        solo.clickOnView(solo.getView(R.id.tue_btn));
        solo.clickOnView(solo.getView(R.id.fri_btn));
        solo.clickOnView(solo.getView(R.id.sat_btn));
        solo.clickOnView(solo.getView(R.id.createHabit));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        //Checking if habit has been created
        solo.clickOnText("Meditation");
        solo.assertCurrentActivity("Wrong Activity", ViewHabitsActivity.class);
        solo.waitForText("Meditation");
    }

    /**
     * CANCEL EDITING A HABIT
     * Tests that a user can successfully go to CreateHabitActivity and click on cancel
     * NOTE: ASSUMES THAT MEDITATION HABIT EXISTS. WILL FAIL OTHERWISE; PLEASE ADD MEDITATION HABIT.
     */
    @Test
    public void GoToCreateHabitActivityAndCancel(){
        // Asserts that the current activity is the LoginActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);

        solo.enterText((EditText) solo.getView(R.id.loginEditEmail), "test@test.ca");
        solo.enterText((EditText) solo.getView(R.id.loginEditPassword), "123456");

        solo.clickOnButton("Login");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnText("Meditation");
        solo.assertCurrentActivity("Wrong Activity", ViewHabitsActivity.class);

        solo.clickOnView(solo.getView(R.id.goBack));

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }


    /**
     * EDITING A HABIT
     * Tests that a user can successfully go to CreateHabitActivity and click on save to save edited data
     * Assumes that we already have a Meditation habit
     */
    @Test
    public void GoToCreateHabitActivityAndSave(){
        // Asserts that the current activity is the LoginActivity. Otherwise, show “Wrong Activity”
        // logging in
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.loginEditEmail), "test@test.ca");
        solo.enterText((EditText) solo.getView(R.id.loginEditPassword), "123456");
        solo.clickOnButton("Login");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        //Edit meditation habit and save
        solo.clickOnText("Meditation");
        solo.assertCurrentActivity("Wrong Activity", ViewHabitsActivity.class);
        ViewHabitsActivity activity = (ViewHabitsActivity) solo.getCurrentActivity();

        //Editing habit title
        solo.clearEditText((EditText) solo.getView(R.id.editTextTitle));
        solo.enterText((EditText) solo.getView(R.id.editTextTitle), "Jogging");

        //Setting Date
        solo.clickOnView(solo.getView(R.id.editTextStartDate));
        solo.setDatePicker(0,2021,10,10);
        solo.clickOnText("OK");

        //Selecting days: MON, WED, FRI
        if (!activity.days_clicked[0]) solo.clickOnView(solo.getView(R.id.mon_btn));
        if (activity.days_clicked[1]) solo.clickOnView(solo.getView(R.id.tue_btn));
        if (!activity.days_clicked[2]) solo.clickOnView(solo.getView(R.id.wed_btn));
        if (activity.days_clicked[3]) solo.clickOnView(solo.getView(R.id.thur_btn));
        if (!activity.days_clicked[4]) solo.clickOnView(solo.getView(R.id.fri_btn));
        if (activity.days_clicked[5]) solo.clickOnView(solo.getView(R.id.sat_btn));
        if (activity.days_clicked[6]) solo.clickOnView(solo.getView(R.id.sun_btn));

        solo.clearEditText((EditText) solo.getView(R.id.editTextReason));
        solo.enterText((EditText) solo.getView(R.id.editTextReason), "To stay healthy");
        solo.assertCurrentActivity("Wrong Activity", ViewHabitsActivity.class);
        solo.clickOnView(solo.getView(R.id.save));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        //Checking if changes has been saved
        solo.clickOnText("Jogging");
        solo.assertCurrentActivity("Wrong Activity", ViewHabitsActivity.class);
        activity = (ViewHabitsActivity) solo.getCurrentActivity();

        final EditText titleText = activity.editTextTitle;
        String title = titleText.getText().toString();
        assertEquals("Jogging", title);

        final TextView dateText = activity.editTextStartDate;
        String date = dateText.getText().toString();
        assertEquals("2021-11-10", date);

        final EditText reasonText = activity.editTextReason;
        String reason = reasonText.getText().toString();
        assertEquals("To stay healthy", reason);

        //Checking if MON, WED, FRI is clicked
        assertTrue(activity.days_clicked[0]);
        assertTrue(activity.days_clicked[2]);
        assertTrue(activity.days_clicked[4]);

        //Checking that the rest of days are not clicked
        assertFalse(activity.days_clicked[1]);
        assertFalse(activity.days_clicked[3]);
        assertFalse(activity.days_clicked[5]);
        assertFalse(activity.days_clicked[6]);
    }


    /**
     * DELETING A HABIT
     * Tests that a user can successfully go to CreateHabitActivity and click on delete to delete a habit
     * Assumes that we already have a Jogging habit
     */
    @Test
    public void GoToCreateHabitActivityAndDelete(){
        // Asserts that the current activity is the LoginActivity. Otherwise, show “Wrong Activity”
        // logging in
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.loginEditEmail), "test@test.ca");
        solo.enterText((EditText) solo.getView(R.id.loginEditPassword), "123456");
        solo.clickOnButton("Login");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.clickOnText("Jogging");
        solo.assertCurrentActivity("Wrong Activity", ViewHabitsActivity.class);
        solo.clickOnView(solo.getView(R.id.delete));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }


    /**
     * CHECK IF HABIT HAS BEEN DELTED
     */
    @Test
    public void CheckIfDeleted(){
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.loginEditEmail), "test@test.ca");
        solo.enterText((EditText) solo.getView(R.id.loginEditPassword), "123456");
        solo.clickOnButton("Login");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        //CHECK IF JOGGING HAS BEEN DELETED
        assertFalse(solo.searchText("Jogging"));
    }
}
