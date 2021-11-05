package com.example.habithelper;

import static org.junit.Assert.assertEquals;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;

import androidx.fragment.app.FragmentManager;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class SendFollowTest {
    private Solo solo;
    FirebaseFirestore db;

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
    public void SendFollowTest(){
        db = FirebaseFirestore.getInstance();
        DocumentReference senderDocRef = db.collection("Users").document("fakeraj@fake.com");
        DocumentReference receiverDocRef = db.collection("Users").document("fakeraj2@fake.com");
        senderDocRef.update("RequestsSent", FieldValue.arrayRemove("fakeraj2@fake.com"));
        receiverDocRef.update("RequestsReceived", FieldValue.arrayRemove("fakeraj@fake.com"));
        senderDocRef.update("Following", FieldValue.arrayRemove("fakeraj2@fake.com"));
        receiverDocRef.update("Followers", FieldValue.arrayRemove("fakeraj@fake.com"));

        // Asserts that the current activity is the LoginActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);

        solo.enterText((EditText) solo.getView(R.id.loginEditEmail), "fakeraj@fake.com");
        solo.enterText((EditText) solo.getView(R.id.loginEditPassword), "fakeraj");
        solo.clickOnButton("Login");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.clickOnView(solo.getView(R.id.friends_fragment));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.clickOnButton("Add friends");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.clickOnText("fakeraj2");
        solo.clickOnButton("send request");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.goBack();
        AcceptFollowRequestTest();
    }

    public void AcceptFollowRequestTest(){

        // Asserts that the current activity is the LoginActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.loginEditEmail), "");
        solo.enterText((EditText) solo.getView(R.id.loginEditPassword), "");

        solo.enterText((EditText) solo.getView(R.id.loginEditEmail), "fakeraj2@fake.com");
        solo.enterText((EditText) solo.getView(R.id.loginEditPassword), "fakeraj");
        solo.clickOnButton("Login");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.clickOnView(solo.getView(R.id.friends_fragment));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.clickOnView(solo.getView(R.id.requestAlert));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.clickOnView(solo.getView(R.id.fragRequestsListView));
        solo.clickOnButton("Accept");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.clickOnView(solo.getView(R.id.friends_fragment));
        solo.clickOnButton("Followers");
        String followersText = solo.clickInList(0).get(0).getText().toString();
        assertEquals("fakeraj", followersText);

    }
}
