package com.example.habithelper;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;


public class LoginActivity extends AppCompatActivity implements NewUserFragment.OnFragmentInteractionListener{

    Intent mainIntent;
    FirebaseFirestore db;
    CollectionReference userCollectionReference;
    Integer maxUserID = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mainIntent = new Intent(this, MainActivity.class);

        db = FirebaseFirestore.getInstance();
        userCollectionReference = db.collection("Users");

        userCollectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    Log.d("USER_DATA", String.valueOf(doc.getData().get("User_Data")));
                }

            }
        });
    }

    /**
     * OnClick event for sign up button
     * Allow the user to generate a new account
     * @param view
     */
    public void onLoginSignUpClick(View view){
        //Show the user sign up fragment
        new NewUserFragment().show(getSupportFragmentManager(), "NEW_USER");
    }

    /**
     * OnClick event for the OK Button in the NewUserFragment
     * Add the user created in the fragment to firestore
     * Note: currently do not provide an ID so firestore randomly makes one, consult with
     * TA if this is appropriate
     */
    @Override
    public void onOkPressed(User newUser){
        HashMap<String, ArrayList<String>> newUserData = new HashMap<>();
        newUserData.put("UserData", newUser.generateDBData());
        newUserData.put("Requests", newUser.generateRequestList());
        newUserData.put("Followers", newUser.generateFollowersList());
        newUserData.put("Following", newUser.generateFollowingList());
        userCollectionReference
                .document()
                .set(newUserData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                // These are a method which gets executed when the task is succeeded
                        Log.d(TAG, "Data has been added successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // These are a method which gets executed if there’s any problem
                        Log.d(TAG, "Data could not be added!" + e.toString());
                    }
                });
    }
    /**
     * OnClick event for the login button
     * Check if the user has entered valid credentials
     * If they have, send them back to the main activity, otherwise let them know
     * they have entered bad credentials
     * @param view
     */
    public void onLoginSignInClick(View view){
        //If the user has been defined by an ID, send that back to the main
        mainIntent.putExtra("currentUserID", 1);
        startActivity(mainIntent);
    }
    /**
     * Simple function to hide the keyboard once it no longer becomes necessary
     */
    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    public void onTextClick(View view){
        hideKeyboard();
    }
}