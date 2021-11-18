/*
Copyright 2021 CMPUT301F21T10

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.example.habithelper;
import static android.content.ContentValues.TAG;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * MainActivity sets up all the interfaces for other activities; basically a hub activity.
 */
public class MainActivity extends AppCompatActivity {
    public FloatingActionButton floatingActionButton;
    ArrayList<Habit> HabitsList = new ArrayList<>();
    ArrayList<HabitEvent> HabitEventsList = new ArrayList<>();
    FirebaseFirestore db;
    FirebaseUser user;

    //This should only be used for the collectUserData method
    private User userToReturn;
    private Boolean getUserDone = false;
    //This can be used for other purposes
    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar main_toolbar = findViewById(R.id.main_top_toolbar);
        setSupportActionBar(main_toolbar);

        Bundle extras = getIntent().getExtras();
        ArrayList<Habit> habitCreated;
        ArrayList<HabitEvent> habitEventCreated;

        if (extras != null){
            /**
             * classFrom: the class from which MainActivity is being called
             * currentUser: the current user
             * newHabit: the habit object being parsed to MainActivity
             * email: the current user's email
             */
            if (extras.getString("classFrom").equals(ViewHabitsActivity.class.toString())){
                user = (FirebaseUser) extras.get("currentUser");

            }else if (extras.getString("classFrom").equals(CreateHabitActivity.class.toString())){
                user = (FirebaseUser) extras.get("currentUser");
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }else if (extras.getString("classFrom").equals(CreateHabitEventActivity.class.toString())){
                user = (FirebaseUser) extras.get("currentUser");
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            } else if (extras.getString("classFrom").equals(LoginActivity.class.toString())){
                Intent intent = getIntent();
                //Initialize the database
                db = FirebaseFirestore.getInstance();
                final CollectionReference userCollectionReference = db.collection("Users");
                user = (FirebaseUser) intent.getExtras().get("currentUser");
                if (user != null) {
                    //All data will be attached to the user's email
                    String email = user.getEmail();
                    collectUserData(email);
                }else{
                    throw new NullPointerException("There is no FirebaseUser!");
                }
            }
        }
        setUpInterface();
    }

    /**
     * Get the document information from the DB on the user passed to the function as an email
     * And convert it into a user object
     * @param email
     * @return
     */
    public void collectUserData(String email){
        DocumentReference docRef = db.collection("Users").document(email);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    User newUser = null;
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            newUser = new User(document);
                        } else {
                            throw new RuntimeException("Invalid Firestore ID Given");
                        }
                    } else {
                        throw new RuntimeException("Firestore retrieval failed");
                    }
                    afterUserLoad(newUser);
                }
            });
        return;
    }

    /**
     * Everything to be done after the user has logged in should go here
     * This will only happen after a user has been loaded in
     * @param newUser
     */
    public void afterUserLoad(User newUser){
        //Ensure we do actually have a user
        if (newUser == null){
            throw new NullPointerException("There is no user logged in!");
        }
    }

    /**
     * Set up the bottom interface bar
     */
    public void setUpInterface(){
        // setup the bottom navigation bar and maps with corresponding fragment
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        Fragment fragment1 = new HabitFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, fragment1).commit();
        bottomNavigationView.setSelectedItemId(R.id.habits_fragment);

        // only for testing for the time being, not implemented dynamically yet
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
                intent.putExtra("habitCreated", HabitsList);
                intent.putExtra("currentUser", user);
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
                        bundle.putSerializable("habitCreated", HabitsList);
                        getSupportActionBar().setTitle("HabitHelper");
                        fragment = new HabitFragment();
                        fragment.setArguments(bundle);
                        break;

                    case R.id.today_fragment:
                        bundle = new Bundle();
                        bundle.putSerializable("habitCreated", HabitsList);
                        getSupportActionBar().setTitle("Today");
                        fragment = new TodayFragment();
                        fragment.setArguments(bundle);
                        break;

                    case R.id.events_fragment:
                        bundle = new Bundle();
                        bundle.putSerializable("habitEventCreated", HabitEventsList);
                        getSupportActionBar().setTitle("Habit Events");
                        fragment = new EventsFragment();
                        fragment.setArguments(bundle);
                        break;

                    case R.id.friends_fragment:
                        bundle = new Bundle();
                        bundle.putSerializable("habitCreated", HabitsList);
                        getSupportActionBar().setTitle("Friends");
                        fragment = new FriendsFragment();
                        fragment.setArguments(bundle);
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, fragment).commit();
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_toolbar, menu);

//        return super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            //When create habit is selected in the menu
            case R.id.profile:
                Toast.makeText(getApplicationContext(), "Profile clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.log_out:
                Toast.makeText(getApplicationContext(), "Log out clicked", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}