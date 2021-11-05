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
CreateHabitActivity hold all the pertinent data for the habit being created.
 */
package com.example.habithelper;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.Calendar;

/**
 * This class is responsible to create a new habit
 */
public class CreateHabitActivity extends AppCompatActivity implements Serializable{
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_habit);
        //Setting up the toolbar with create habit and cancel menu items
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true); removed back button from toolbar
        getSupportActionBar().setTitle("Create Habit");

        db = FirebaseFirestore.getInstance();


        //Setting up the days buttons (for selecting days)
        Button mon_btn = findViewById(R.id.mon_btn);
        final boolean[] mon_clicked = {false};
        Button tue_btn = findViewById(R.id.tue_btn);
        final boolean[] tue_clicked = {false};
        Button wed_btn = findViewById(R.id.wed_btn);
        final boolean[] wed_clicked = {false};
        Button thur_btn = findViewById(R.id.thur_btn);
        final boolean[] thur_clicked = {false};
        Button fri_btn = findViewById(R.id.fri_btn);
        final boolean[] fri_clicked = {false};
        Button sat_btn = findViewById(R.id.sat_btn);
        final boolean[] sat_clicked = {false};
        Button sun_btn = findViewById(R.id.sun_btn);
        final boolean[] sun_clicked = {false};

        //Checking if monday has been clicked
        mon_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mon_clicked[0]){
                    mon_clicked[0] = true;
                    mon_btn.setBackgroundColor(getResources().getColor(R.color.button_clicked));
                }
                else {
                    mon_clicked[0] = false;
                    mon_btn.setBackgroundColor(getResources().getColor(R.color.purple_500));
                }
            }
        });

        //Checking if tuesday has been clicked
        tue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!tue_clicked[0]){
                    tue_clicked[0] = true;
                    tue_btn.setBackgroundColor(getResources().getColor(R.color.button_clicked));
                }
                else {
                    tue_clicked[0] = false;
                    tue_btn.setBackgroundColor(getResources().getColor(R.color.purple_500));
                }
            }
        });

        //Checking if wednesday has been clicked
        wed_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!wed_clicked[0]){
                    wed_clicked[0] = true;
                    wed_btn.setBackgroundColor(getResources().getColor(R.color.button_clicked));
                }
                else {
                    wed_clicked[0] = false;
                    wed_btn.setBackgroundColor(getResources().getColor(R.color.purple_500));
                }
            }
        });

        //Checking if thursday has been clicked
        thur_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!thur_clicked[0]){
                    thur_clicked[0] = true;
                    thur_btn.setBackgroundColor(getResources().getColor(R.color.button_clicked));
                }
                else {
                    thur_clicked[0] = false;
                    thur_btn.setBackgroundColor(getResources().getColor(R.color.purple_500));
                }
            }
        });

        //Checking if friday has been clicked
        fri_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!fri_clicked[0]){
                    fri_clicked[0] = true;
                    fri_btn.setBackgroundColor(getResources().getColor(R.color.button_clicked));
                }
                else {
                    fri_clicked[0] = false;
                    fri_btn.setBackgroundColor(getResources().getColor(R.color.purple_500));
                }
            }
        });

        //Checking if saturday has been clicked
        sat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!sat_clicked[0]){
                    sat_clicked[0] = true;
                    sat_btn.setBackgroundColor(getResources().getColor(R.color.button_clicked));
                }
                else {
                    sat_clicked[0] = false;
                    sat_btn.setBackgroundColor(getResources().getColor(R.color.purple_500));
                }
            }
        });

        //Checking if sunday has been clicked
        sun_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!sun_clicked[0]){
                    sun_clicked[0] = true;
                    sun_btn.setBackgroundColor(getResources().getColor(R.color.button_clicked));
                }
                else {
                    sun_clicked[0] = false;
                    sun_btn.setBackgroundColor(getResources().getColor(R.color.purple_500));
                }
            }
        });

        //Setting up date picker dialogue
        TextView dateStarted = findViewById(R.id.editTextStartDate);
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog.OnDateSetListener setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = year + "-" + month + "-" + dayOfMonth;
                dateStarted.setText(date);
            }
        };
        DatePickerDialog.OnDateSetListener finalSetListener = setListener;
        dateStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateHabitActivity.this,
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
        //Getting the habit title, reason, and start date edit texts of the habit being created
        EditText editTextTitle = findViewById(R.id.editTextTitle);
        EditText editTextReason = findViewById(R.id.editTextReason);
        TextView dateStarted = findViewById(R.id.editTextStartDate);

        switch (item.getItemId()){
            //When create habit is selected in the menu
            case R.id.createHabit:
                //Get the current user from MainActivity to pass it back to MainActivity
                Bundle extras = getIntent().getExtras();
                FirebaseUser user = (FirebaseUser) extras.get("currentUser");

                //Get the habit details of the habit being created
                String habitTitle = String.valueOf(editTextTitle.getText());
                String habitReason = String.valueOf(editTextReason.getText());
                String habitStartDate = String.valueOf(dateStarted.getText());;
                //Create the new habit object
                Habit habitCreated = new Habit(habitTitle, habitReason, habitStartDate, true);

                //adding the new edited habit to the database
                habitCreated.addHabitToDB(user.getEmail(), db);


                Intent intent = new Intent(CreateHabitActivity.this, MainActivity.class);
                intent.putExtra("classFrom", CreateHabitActivity.class.toString());
                intent.putExtra("habitCreated", habitCreated);
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