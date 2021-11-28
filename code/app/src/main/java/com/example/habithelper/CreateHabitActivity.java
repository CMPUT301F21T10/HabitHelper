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
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.Calendar;

/**
 * This class is responsible to create a new habit
 */
public class CreateHabitActivity extends AppCompatActivity implements Serializable{
    FirebaseFirestore db;
    final boolean[] days_clicked = {false,false,false,false,false,false,false};
    boolean publicStatus = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_habit);
        //Setting up the toolbar with create habit and cancel menu items
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Create Habit");

        db = FirebaseFirestore.getInstance();


        //Setting up the days buttons (for selecting days)
        Button mon_btn = findViewById(R.id.mon_btn);
        Button tue_btn = findViewById(R.id.tue_btn);
        Button wed_btn = findViewById(R.id.wed_btn);
        Button thur_btn = findViewById(R.id.thur_btn);
        Button fri_btn = findViewById(R.id.fri_btn);
        Button sat_btn = findViewById(R.id.sat_btn);
        Button sun_btn = findViewById(R.id.sun_btn);

        ToggleButton toggleButton = findViewById(R.id.toggleButton);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (toggleButton.isChecked()){
                    publicStatus = false;
                }else {
                    publicStatus = true;
                }
            }
        });



        //Checking if monday has been clicked
        mon_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!days_clicked[0]){
                    days_clicked[0] = true;
                    mon_btn.setBackgroundColor(getResources().getColor(R.color.accent2));
                }
                else {
                    days_clicked[0] = false;
                    mon_btn.setBackgroundColor(getResources().getColor(R.color.accent1));
                }
            }
        });

        //Checking if tuesday has been clicked
        tue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!days_clicked[1]){
                    days_clicked[1] = true;
                    tue_btn.setBackgroundColor(getResources().getColor(R.color.accent2));
                }
                else {
                    days_clicked[1] = false;
                    tue_btn.setBackgroundColor(getResources().getColor(R.color.accent1));
                }
            }
        });

        //Checking if wednesday has been clicked
        wed_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!days_clicked[2]){
                    days_clicked[2] = true;
                    wed_btn.setBackgroundColor(getResources().getColor(R.color.accent2));
                }
                else {
                    days_clicked[2] = false;
                    wed_btn.setBackgroundColor(getResources().getColor(R.color.accent1));
                }
            }
        });

        //Checking if thursday has been clicked
        thur_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!days_clicked[3]){
                    days_clicked[3] = true;
                    thur_btn.setBackgroundColor(getResources().getColor(R.color.accent2));
                }
                else {
                    days_clicked[3] = false;
                    thur_btn.setBackgroundColor(getResources().getColor(R.color.accent1));
                }
            }
        });

        //Checking if friday has been clicked
        fri_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!days_clicked[4]){
                    days_clicked[4] = true;
                    fri_btn.setBackgroundColor(getResources().getColor(R.color.accent2));
                }
                else {
                    days_clicked[4] = false;
                    fri_btn.setBackgroundColor(getResources().getColor(R.color.accent1));
                }
            }
        });

        //Checking if saturday has been clicked
        sat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!days_clicked[5]){
                    days_clicked[5] = true;
                    sat_btn.setBackgroundColor(getResources().getColor(R.color.accent2));
                }
                else {
                    days_clicked[5] = false;
                    sat_btn.setBackgroundColor(getResources().getColor(R.color.accent1));
                }
            }
        });

        //Checking if sunday has been clicked
        sun_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!days_clicked[6]){
                    days_clicked[6] = true;
                    sun_btn.setBackgroundColor(getResources().getColor(R.color.accent2));
                }
                else {
                    days_clicked[6] = false;
                    sun_btn.setBackgroundColor(getResources().getColor(R.color.accent1));
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
        StringBuilder day = new StringBuilder();
        //Setting up days
        for (int days=0;days<7;days++){
            if (days_clicked[days]){
                day.append("1");
            }else{
                day.append("0");
            }
        }

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
                Habit habitCreated = new Habit(habitTitle, habitReason, habitStartDate, publicStatus, day.toString(), 0);

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