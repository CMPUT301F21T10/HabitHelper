package com.example.habithelper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
<<<<<<< HEAD
import android.util.Log;
import android.view.View;
=======
>>>>>>> 2ed747221dcc0bc6e28e120a1116ac5206d27a69

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

<<<<<<< HEAD
//    FirebaseFirestore db;
//    Intent loginIntent;
    public FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("In main activity", "onCreate: haha");

        setContentView(R.layout.activity_homescreen);

//        Intent intent = getIntent();
//        loginIntent = new Intent(this, LoginActivity.class);
//        //If the user has not been logged in yet
//        if (!intent.hasExtra("currentUser")){
//            //Open up the login screen
//           startActivity(loginIntent);
//        }
//        //if a user has been logged in, access their information
//        else{
//            FirebaseUser user = (FirebaseUser) intent.getExtras().get("currentUser");
//            if (user != null) {
//                String email = user.getEmail();
//
//                // The user's ID, unique to the Firebase project. Do NOT use this value to
//                // authenticate with your backend server, if you have one. Use
//                // FirebaseUser.getIdToken() instead.
//                String uid = user.getUid();
//            }else{
//
//            }
//        }

//        //Initialize the database
//        db = FirebaseFirestore.getInstance();
//        final CollectionReference userCollectionReference = db.collection("Users");
=======
    FirebaseFirestore db;
    Intent loginIntent;
    //public FloatingActionButton floatingActionButton;
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
/*
        //Initialize the database
        db = FirebaseFirestore.getInstance();
        final CollectionReference userCollectionReference = db.collection("Users");


        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreateHabit.class);
                startActivity(intent);
            }
        });


>>>>>>> 2ed747221dcc0bc6e28e120a1116ac5206d27a69

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragmentContainerView);
        NavController navController = navHostFragment.getNavController();

        NavigationUI.setupWithNavController(bottomNavigationView, navController);



        bottomNavigationView.setBackground(null);
        BadgeDrawable badge = bottomNavigationView.getOrCreateBadge(R.id.friends_fragment);
        badge.setVisible(true);
        badge.setNumber(10);
        badge.setBadgeGravity(BadgeDrawable.TOP_END);*/

        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreateHabit.class);
                startActivity(intent);
            }
        });





    }

}