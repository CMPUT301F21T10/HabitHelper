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
ViewHabitsActivity hold all the pertinent data for the habit being edited, or deleted.
 */
package com.example.habithelper;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import java.util.Calendar;

public class ViewHabitsActivity extends AppCompatActivity implements Serializable {

    EditText editTextTitle, editTextReason;
    TextView editTextStartDate;
    Bundle extras;
    Habit habitEditing;
    boolean publicStatus;
    FirebaseFirestore db;
    FirebaseUser user;
    final boolean[] days_clicked = {false,false,false,false,false,false,false};
    Button mon_btn, tue_btn, wed_btn, thur_btn, fri_btn, sat_btn, sun_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_habits);
        //Setting up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Habit");

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextStartDate = findViewById(R.id.editTextStartDate);
        editTextReason = findViewById(R.id.editTextReason);
        db = FirebaseFirestore.getInstance();
        //Setting up the days buttons (for selecting days)
        mon_btn = findViewById(R.id.mon_btn);
        tue_btn = findViewById(R.id.tue_btn);
        wed_btn = findViewById(R.id.wed_btn);
        thur_btn = findViewById(R.id.thur_btn);
        fri_btn = findViewById(R.id.fri_btn);
        sat_btn = findViewById(R.id.sat_btn);
        sun_btn = findViewById(R.id.sun_btn);

        extras = getIntent().getExtras();
        //Getting the habit object being viewed, and the current user
        if (extras != null){
            habitEditing = (Habit) extras.getSerializable("habit");
            user = (FirebaseUser) extras.get("currentUser");
        }

        editTextTitle.setText(habitEditing.getTitle());
        editTextStartDate.setText(habitEditing.getDateStarted());
        editTextReason.setText(habitEditing.getReason());
        ToggleButton toggleButton = findViewById(R.id.toggleButton);
        String days = habitEditing.getHabitDays();
        publicStatus = habitEditing.getPublicStatus();

        if(publicStatus){
            toggleButton.setChecked(false);
        }else{
            toggleButton.setChecked(true);
        }


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

        if (days.charAt(0)=='1'){
            mon_btn.setBackgroundColor(getResources().getColor(R.color.accent2));
            days_clicked[0]=true;
        }
        if (days.charAt(1)=='1'){
            tue_btn.setBackgroundColor(getResources().getColor(R.color.accent2));
            days_clicked[1]=true;
        }
        if (days.charAt(2)=='1'){
            wed_btn.setBackgroundColor(getResources().getColor(R.color.accent2));
            days_clicked[2]=true;
        }
        if (days.charAt(3)=='1'){
            thur_btn.setBackgroundColor(getResources().getColor(R.color.accent2));
            days_clicked[3]=true;
        }
        if (days.charAt(4)=='1'){
            fri_btn.setBackgroundColor(getResources().getColor(R.color.accent2));
            days_clicked[4]=true;
        }
        if (days.charAt(5)=='1'){
            sat_btn.setBackgroundColor(getResources().getColor(R.color.accent2));
            days_clicked[5]=true;
        }
        if (days.charAt(6)=='1'){
            sun_btn.setBackgroundColor(getResources().getColor(R.color.accent2));
            days_clicked[6]=true;
        }

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
                DatePickerDialog datePickerDialog = new DatePickerDialog(ViewHabitsActivity.this,
                        finalSetListener, year, month, day);
                datePickerDialog.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.view_habit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.save:

                String habitTitle = String.valueOf(editTextTitle.getText());
                String habitReason = String.valueOf(editTextReason.getText());
                String habitStartDate = String.valueOf(editTextStartDate.getText());;
                StringBuilder day = new StringBuilder();
                String numHabitEvents = habitEditing.getNumHabitEvents();
                //Setting up days
                for (int days=0;days<7;days++){
                    if (days_clicked[days]){
                        day.append("1");
                    }else{
                        day.append("0");
                    }
                }
                Habit newEditedHabit = new Habit(habitTitle, habitReason, habitStartDate, publicStatus, day.toString(), Integer.parseInt(numHabitEvents));
                String emailToEdit = user.getEmail();

                //deleting the old habit from the database
                habitEditing.deleteHabitFromDB(emailToEdit, db);

                //adding the new edited habit to the database
                newEditedHabit.addHabitToDB(emailToEdit, db);

                Intent intent = new Intent(ViewHabitsActivity.this, MainActivity.class);
                intent.putExtra("classFrom", ViewHabitsActivity.class.toString());
                intent.putExtra("currentUser", user);
                startActivity(intent);
                return true;

            case R.id.delete:
                String email = user.getEmail();
                //deleting the habit from the database
                habitEditing.deleteHabitFromDB(email, db);

//                try {
//                    TimeUnit.MILLISECONDS.sleep(400);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                intent = new Intent(ViewHabitsActivity.this, MainActivity.class);
                intent.putExtra("classFrom", ViewHabitsActivity.class.toString());
                intent.putExtra("currentUser", user);
                startActivity(intent);
                return true;

            case R.id.goBack:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
