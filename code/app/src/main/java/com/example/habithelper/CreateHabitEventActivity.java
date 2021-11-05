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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

public class CreateHabitEventActivity extends AppCompatActivity implements Serializable {

    TextView textViewHabitName;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_habit_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Create Habit Event");

        textViewHabitName = findViewById(R.id.textViewHabitName);
        Bundle extras = getIntent().getExtras();
        Habit habitCreated;

        if (extras != null){
            habitCreated = (Habit) extras.getSerializable("habit");
            textViewHabitName.setText("Habit Name: "+habitCreated.getTitle());
            user = (FirebaseUser) extras.get("currentUser");

        }

        TextView dateStarted = findViewById(R.id.editTextDateCompleted);
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateHabitEventActivity.this,
                        finalSetListener,
                        year,
                        month,
                        day);
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

        TextView dateStarted = findViewById(R.id.editTextDateCompleted);
        EditText editTextComments = findViewById(R.id.editTextOptionalComments);
        EditText editTextTLocation = findViewById(R.id.editTextOptionalLocation);



        switch (item.getItemId()){
            case R.id.createHabit:
                Bundle extras = getIntent().getExtras();
                ArrayList<Habit> habitsCreated = (ArrayList<Habit>) extras.getSerializable("habitCreated");
                ArrayList<HabitEvent> habitEventCreated = new ArrayList<>();

                String habitLocation = String.valueOf(editTextTLocation.getText());
                String habitComment = String.valueOf(editTextComments.getText());
                String habitStartDate = String.valueOf(dateStarted.getText());;
                habitEventCreated.add(new HabitEvent(habitComment, habitLocation, habitStartDate));
                Intent intent = new Intent(CreateHabitEventActivity.this, MainActivity.class);
                intent.putExtra("classFrom", CreateHabitEventActivity.class.toString());
                intent.putExtra("habitEventCreated", habitEventCreated);
                intent.putExtra("currentUser", user);
                intent.putExtra("habitCreated", habitsCreated);
                startActivity(intent);


                return true;

            case R.id.goBack:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}