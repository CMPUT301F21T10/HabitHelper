package com.example.habithelper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class LoginActivity extends AppCompatActivity {

    Intent mainIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mainIntent = new Intent(this, MainActivity.class);
    }

    /**
     * OnClick event for sign up button
     * Allow the user to generate a new account
     * @param view
     */
    public void onLoginSignUpClick(View view){
        //Show the user sign up fragment
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
}