package com.example.habithelper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db;
    Intent loginIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        loginIntent = new Intent(this, LoginActivity.class);
        //If the user has not been logged in yet
        if (!intent.hasExtra("currentUser")){
            //Open up the login screen
           startActivity(loginIntent);
        }
        //if a user has been logged in, access their information
        else{
            FirebaseUser user = (FirebaseUser) intent.getExtras().get("currentUser");
            if (user != null) {
                String email = user.getEmail();

                // The user's ID, unique to the Firebase project. Do NOT use this value to
                // authenticate with your backend server, if you have one. Use
                // FirebaseUser.getIdToken() instead.
                String uid = user.getUid();
            }else{
                
            }
        }

        //Initialize the database
        db = FirebaseFirestore.getInstance();
        final CollectionReference userCollectionReference = db.collection("Users");
    }

}