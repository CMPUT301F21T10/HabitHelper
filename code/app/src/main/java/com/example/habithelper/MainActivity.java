package com.example.habithelper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
        if (!intent.hasExtra("currentUserID")){
            //Open up the login screen
           startActivity(loginIntent);
        }

        //Initialize the database
        db = FirebaseFirestore.getInstance();
        final CollectionReference userCollectionReference = db.collection("Users");
    }

}