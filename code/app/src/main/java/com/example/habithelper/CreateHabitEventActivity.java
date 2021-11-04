package com.example.habithelper;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.Serializable;

public class CreateHabitEventActivity extends AppCompatActivity implements Serializable {

    TextView textViewHabitName;
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
//            case R.id.createHabit:
//                Bundle extras = getIntent().getExtras();
//                ArrayList<Habit> habitCreated = new ArrayList<>();
//
//                if (extras != null){
//                    habitCreated = (ArrayList<Habit>) extras.getSerializable("habitCreated");
//                }
//
//
//                String habitTitle = String.valueOf(editTextTitle.getText());
//                String habitReason = String.valueOf(editTextReason.getText());
//                String habitStartDate = String.valueOf(editTextStartDate.getText());;
//                habitCreated.add(new Habit(habitTitle, habitReason, habitStartDate, true));
//                Intent intent = new Intent(CreateHabitActivity.this, MainActivity.class);
//                intent.putExtra("habitCreated", habitCreated);
//                startActivity(intent);


//                return true;

            case R.id.goBack:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}