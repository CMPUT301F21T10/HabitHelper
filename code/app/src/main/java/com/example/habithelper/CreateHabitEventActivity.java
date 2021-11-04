package com.example.habithelper;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;
import java.util.ArrayList;

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.create_habit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        EditText editTextTitle = findViewById(R.id.editTextDateCompleted);
        EditText editTextReason = findViewById(R.id.editTextOptionalComments);
        EditText editTextStartDate = findViewById(R.id.editTextOptionalLocation);



        switch (item.getItemId()){
            case R.id.createHabit:
                Bundle extras = getIntent().getExtras();
                ArrayList<Habit> habitsCreated = (ArrayList<Habit>) extras.getSerializable("habitCreated");
                ArrayList<HabitEvent> habitEventCreated = new ArrayList<>();

                String habitTitle = String.valueOf(editTextTitle.getText());
                String habitReason = String.valueOf(editTextReason.getText());
                String habitStartDate = String.valueOf(editTextStartDate.getText());;
                habitEventCreated.add(new HabitEvent(habitTitle, habitReason, habitStartDate));
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