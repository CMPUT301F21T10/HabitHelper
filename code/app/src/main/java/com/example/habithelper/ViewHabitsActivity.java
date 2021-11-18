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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
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
    FirebaseFirestore db;
    FirebaseUser user;

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

        extras = getIntent().getExtras();
        //Getting the habit object being viewed, and the current user
        if (extras != null){
            habitEditing = (Habit) extras.getSerializable("habit");
            user = (FirebaseUser) extras.get("currentUser");
        }

        editTextTitle.setText(habitEditing.getTitle());
        editTextStartDate.setText(habitEditing.getDateStarted());
        editTextReason.setText(habitEditing.getReason());

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
                Habit newEditedHabit = new Habit(habitTitle, habitReason, habitStartDate, true);
                String emailToEdit = user.getEmail();

                //adding the new edited habit to the database
                newEditedHabit.addHabitToDB(emailToEdit, db);

                //deleting the old habit from the database
                habitEditing.deleteHabitFromDB(emailToEdit, db);

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