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
/*
CreateHabitEventActivity hold all the pertinent data for the habit event being created.
 */
package com.example.habithelper;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;


/**
 * This class is responsible to create new habit events
 */
public class CreateHabitEventActivity extends AppCompatActivity implements Serializable {

    TextView textViewHabitName;
    Button button_location_picker;
    TextView locationText;
    TextView editTextDateCompleted;
    EditText editTextComments;
    TextView selectedLocationText;

    FirebaseFirestore db;
    FirebaseUser user;
    Habit habit_to_create_event;


    String address = "";
    Double Lat = 0.0, Long = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_habit_event);
        //Setting up toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Create Habit Event");

        db = FirebaseFirestore.getInstance();

        button_location_picker = findViewById(R.id.buttonAddLocation);
        locationText = findViewById(R.id.textView_addedLocation);
        selectedLocationText = findViewById(R.id.textView_selectedLocation);
        textViewHabitName = findViewById(R.id.textViewHabitName);
        editTextDateCompleted = findViewById(R.id.editTextDateCompleted);
        editTextComments = findViewById(R.id.editTextOptionalComments);


        Bundle extras = getIntent().getExtras();
        if (extras != null){
            Log.d("CLASS_FROM", "onCreate: " + extras.getString("classFrom"));

            if (extras.getString("classFrom").equals(LocationPickerActivity.class.toString())){

                address = extras.getString("address");
                Log.d("ADDRESS", "onCreate: " + address);
                String date = extras.getString("date");
                String comment = extras.getString("comment");
                Lat = extras.getDouble("lat");
                Long = extras.getDouble("long");

                Toast.makeText(getApplicationContext(), "Lat: " + Lat + " Long: " + Long, Toast.LENGTH_SHORT).show();

                //Get the habit for which a habit event is to be created
                habit_to_create_event = (Habit) extras.getSerializable("habit");
                textViewHabitName.setText("Habit Name: "+habit_to_create_event.getTitle());
                user = (FirebaseUser) extras.get("currentUser");

                editTextDateCompleted.setText(date);
                editTextComments.setText(comment);
                locationText.setText(address);
                locationText.setVisibility(View.VISIBLE);
                selectedLocationText.setVisibility(View.VISIBLE);
            }
            else{ // from main activity (habit fragment)

                //Get the habit for which a habit event is to be created
                habit_to_create_event = (Habit) extras.getSerializable("habit");
                textViewHabitName.setText("Habit Name: "+habit_to_create_event.getTitle());
                user = (FirebaseUser) extras.get("currentUser");
            }



        }

        button_location_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mapIntent = new Intent(CreateHabitEventActivity.this, LocationPickerActivity.class);
                mapIntent.putExtra("habit", habit_to_create_event);
                mapIntent.putExtra("currentUser", user);

                // store data already input to display again when back to this activity
                String date = editTextDateCompleted.getText().toString();
                String comment = editTextComments.getText().toString();

                mapIntent.putExtra("date", date);
                mapIntent.putExtra("comment", comment);

                startActivity(mapIntent);
            }
        });


        //Setting up date picker dialogue
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog.OnDateSetListener setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = year + "-" + month + "-" + dayOfMonth;
                editTextDateCompleted.setText(date);
            }
        };
        DatePickerDialog.OnDateSetListener finalSetListener = setListener;
        editTextDateCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateHabitEventActivity.this,
                        finalSetListener, year, month, day);
                datePickerDialog.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.create_habit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //Getting the habit event details edit texts of the habit event being created
        editTextDateCompleted = findViewById(R.id.editTextDateCompleted);
        editTextComments = findViewById(R.id.editTextOptionalComments);

        switch (item.getItemId()){
            //When create is selected in the menu
            case R.id.createHabit:

                //Get the current user from MainActivity to pass it back to MainActivity
                Bundle extras = getIntent().getExtras();
                user = (FirebaseUser) extras.get("currentUser");

                //Get the habit details of the habit being created
                String associatedHabitTitle = habit_to_create_event.getTitle();
                String EventTitle = associatedHabitTitle + " Event";
                String EventComment = editTextComments.getText().toString();
                String EventDateCompleted = editTextDateCompleted.getText().toString();
                String EventLocation = locationText.getText().toString();

                //Create the new habit event object
                HabitEvent newHabitEvent = new HabitEvent(EventTitle, EventComment, EventDateCompleted, associatedHabitTitle,
                        EventLocation, Lat, Long);

                //adding the new habit event to the database
                newHabitEvent.addHabitEventToDB(user.getEmail(), db);

                Intent intent = new Intent(CreateHabitEventActivity.this, MainActivity.class);
                intent.putExtra("classFrom", CreateHabitEventActivity.class.toString());
                intent.putExtra("habitEventCreated", newHabitEvent);
                intent.putExtra("currentUser", user);
                startActivity(intent);
                return true;

            //When cancel is selected in the menu
            case R.id.goBack:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}