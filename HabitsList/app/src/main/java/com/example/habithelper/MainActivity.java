package com.example.habithelper;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public FloatingActionButton floatingActionButton;
    ArrayList<String> HabitsList = new ArrayList<>();

    int count  = 0;



//    FirebaseFirestore db;
//    Intent loginIntent;
//
//    //This should only be used for the collectUserData method
//    private User userToReturn;
//    //This can be used for other purposes
//    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle extras = getIntent().getExtras();
        String habitTitle;

        if (extras != null){
            habitTitle = extras.getString("habitsTitle");
            HabitsList.add(habitTitle);
        }
        HabitsList.add("HelloFromMain");


//        Intent intent = getIntent();
//        loginIntent = new Intent(this, LoginActivity.class);
//
//        //Initialize the database
//        db = FirebaseFirestore.getInstance();
//        final CollectionReference userCollectionReference = db.collection("Users");
//
//        //If the user has not been logged in yet
//        if (!intent.hasExtra("currentUser")){
//            //Open up the login screen
//            startActivity(loginIntent);
//        }
//        //if a user has been logged in, access their information
//        else{
//            FirebaseUser user = (FirebaseUser) intent.getExtras().get("currentUser");
//            if (user != null) {
//                //All data will be attached to the user's email
//                String email = user.getEmail();
//                currentUser = collectUserData(email);
//
//            }else{
//
//            }
//        }

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // setup the bottom navigation bar and maps with corresponding fragment
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);

        Bundle bundle = new Bundle();
        bundle.putStringArrayList("habitsTitle", HabitsList);
        Fragment fragment1 = new HabitFragment();
        fragment1.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, fragment1).commit();
        bottomNavigationView.setSelectedItemId(R.id.habits_fragment);


//        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.fragmentContainerView);
//        NavController navController = navHostFragment.getNavController();
//        NavigationUI.setupWithNavController(bottomNavigationView, navController);

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
                Intent intent = new Intent(MainActivity.this, CreateHabitActivity.class);
                startActivity(intent);
            }
        });


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;

                switch (item.getItemId()) {
                    case R.id.habits_fragment:
                        Bundle bundle = new Bundle();

                        bundle.putStringArrayList("habitsTitle", HabitsList);
                        fragment = new HabitFragment();
                        fragment.setArguments(bundle);
                        break;

                    case R.id.today_fragment:
                        bundle = new Bundle();

                        bundle.putStringArrayList("habitsTitle", HabitsList);
                        fragment = new TodayFragment();
                        fragment.setArguments(bundle);
                        break;

                    case R.id.events_fragment:
                        bundle = new Bundle();

                        bundle.putStringArrayList("habitsTitle", HabitsList);
                        fragment = new EventsFragment();
                        fragment.setArguments(bundle);
                        break;

                    case R.id.friends_fragment:
                        bundle = new Bundle();

                        bundle.putStringArrayList("habitsTitle", HabitsList);
                        fragment = new FriendsFragment();
                        fragment.setArguments(bundle);
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, fragment).commit();

                return true;
            }
        });
    }

    /**
     * Get the document information from the DB on the user passed to the function as an email
     * And convert it into a user object
     * @param email
     * @return
     */
//    public User collectUserData(String email){
//        DocumentReference docRef = db.collection("Users").document(email);
//        userToReturn = null;
//        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
//                        userToReturn = new User(document);
//                    } else {
//                        Log.d(TAG, "No such document");
//
//                    }
//                } else {
//                    Log.d(TAG, "get failed with ", task.getException());
//                }
//            }
//        });
//        return userToReturn;
//    }


}