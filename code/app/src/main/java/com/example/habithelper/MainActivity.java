package com.example.habithelper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    public FloatingActionButton floatingActionButton;
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
                //All data will be attached to the user's email
                String email = user.getEmail();

            }else{

            }
        }

        //Initialize the database
        db = FirebaseFirestore.getInstance();
        final CollectionReference userCollectionReference = db.collection("Users");

        //NOTE: What does this do
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragmentContainerView);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        //Draw a notification badge over the friends icon
        bottomNavigationView.setBackground(null);
        BadgeDrawable badge = bottomNavigationView.getOrCreateBadge(R.id.friends_fragment);
        badge.setVisible(true);
        badge.setNumber(10);
        badge.setBadgeGravity(BadgeDrawable.TOP_END);

        //Set the floating action button to open up new habit
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