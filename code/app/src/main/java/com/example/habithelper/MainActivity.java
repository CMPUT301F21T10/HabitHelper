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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    public FloatingActionButton floatingActionButton;
    ArrayList<Habit> HabitsList = new ArrayList<>();
    ArrayList<HabitEvent> HabitEventsList = new ArrayList<>();

    FirebaseFirestore db;
    FirebaseUser user;
//    Intent loginIntent;

    //This should only be used for the collectUserData method
    private User userToReturn;
    private Boolean getUserDone = false;
    //This can be used for other purposes
    User currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle extras = getIntent().getExtras();
        ArrayList<Habit> habitCreated;
        ArrayList<HabitEvent> habitEventCreated;

        if (extras != null){
            if (extras.getString("classFrom").equals(ViewHabitsActivity.class.toString())){

                Log.d("normal", "normal part");

                user = (FirebaseUser) extras.get("currentUser");



            }else if (extras.getString("classFrom").equals(CreateHabitActivity.class.toString())){
                Log.d("elsePart", "else part");


                habitCreated = (ArrayList<Habit>) extras.getSerializable("habitCreated");
                Habit newHabit = habitCreated.get(0);
                user = (FirebaseUser) extras.get("currentUser");

                db = FirebaseFirestore.getInstance();

                String email = user.getEmail();
                Log.d("HABIT_TITLE", "onCreate: " + email);
                DocumentReference docRef = db.collection("Habits")
                        .document(email)
                        .collection(email + "_habits")
                        .document(newHabit.getTitle());

                Log.d("HABIT_TITLE", "onCreate: " + newHabit.getTitle());

                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "Document already exists!");
                                Toast.makeText(MainActivity.this, "Habit already exists",
                                        Toast.LENGTH_SHORT).show();

                            } else {
                                Log.d(TAG, "Document does not exist!");

                                // write to db
                                docRef.set(newHabit.generateAllHabitDBData())
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                // These are a method which gets executed when the task is succeeded
                                                Log.d("DATA_ADDED", "Data has been added successfully!");
                                                HabitsList.add(newHabit);

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
                        } else {
                            Log.d(TAG, "Failed with: ", task.getException());
                        }
                    }
                });
                try {
                    TimeUnit.MILLISECONDS.sleep(400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }





            }else if (extras.getString("classFrom").equals(CreateHabitEventActivity.class.toString())){
                Log.d("elsePart", "else part");
                habitCreated = (ArrayList<Habit>) extras.getSerializable("habitCreated");
                for (Habit eachHabit : habitCreated){
                    HabitsList.add(eachHabit);
                }
                habitEventCreated = (ArrayList<HabitEvent>) extras.getSerializable("habitEventCreated");
                for (HabitEvent eachHabit : habitEventCreated){
                    HabitEventsList.add(eachHabit);
                }
                user = (FirebaseUser) extras.get("currentUser");
            } else if (extras.getString("classFrom").equals(LoginActivity.class.toString())){
                Intent intent = getIntent();
//              loginIntent = new Intent(this, LoginActivity.class);

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
//                collectUserData(email);
//            }else{
//                throw new NullPointerException("There is no FirebaseUser!");
//            }
//        }




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

//        Bundle bundle = new Bundle();
//        bundle.putSerializable("habitCreated", HabitsList);
        Fragment fragment1 = new HabitFragment();
//        fragment1.setArguments(bundle);
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
                        fragment = new HabitFragment();
                        fragment.setArguments(bundle);
                        break;

                    case R.id.today_fragment:
                        bundle = new Bundle();
                        bundle.putSerializable("habitCreated", HabitsList);
                        fragment = new TodayFragment();
                        fragment.setArguments(bundle);
                        break;

                    case R.id.events_fragment:
                        bundle = new Bundle();
                        bundle.putSerializable("habitEventCreated", HabitEventsList);
                        fragment = new EventsFragment();
                        fragment.setArguments(bundle);
                        break;

                    case R.id.friends_fragment:
                        bundle = new Bundle();
                        bundle.putSerializable("habitCreated", HabitsList);
                        fragment = new FriendsFragment();
                        fragment.setArguments(bundle);
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, fragment).commit();

                return true;
            }
        });
    }
}