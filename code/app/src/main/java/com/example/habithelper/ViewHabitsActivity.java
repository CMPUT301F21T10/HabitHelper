package com.example.habithelper;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.Serializable;
import java.util.ArrayList;

public class ViewHabitsActivity extends AppCompatActivity implements Serializable {

    EditText editTextTitle,editTextStartDate, editTextReason;
    Bundle extras;
    ArrayList<Habit> habitCreated = new ArrayList<>();
    Habit habitEditing;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_habits);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Habit");

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextStartDate = findViewById(R.id.editTextStartDate);
        editTextReason = findViewById(R.id.editTextReason);

        extras = getIntent().getExtras();
        if (extras != null){
            habitCreated = (ArrayList<Habit>) extras.getSerializable("habit");
        }
        habitEditing = habitCreated.get(habitCreated.size()-1);
        editTextTitle.setText(habitEditing.getTitle());
        editTextStartDate.setText(habitEditing.getDateStarted());
        editTextReason.setText(habitEditing.getReason());
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
                habitCreated.add(new Habit(habitTitle, habitReason, habitStartDate, true));
                for (int i = 0 ; i<habitCreated.size();i++){
                    if (habitCreated.get(i).getTitle().equals(habitEditing.getTitle())
                        && habitCreated.get(i).getReason().equals(habitEditing.getReason())
                        && habitCreated.get(i).getDateStarted().equals(habitEditing.getDateStarted())){
                        habitCreated.remove(i);
                        i--;
                    }
                }
                Intent intent = new Intent(ViewHabitsActivity.this, MainActivity.class);
                intent.putExtra("habitEdited", habitCreated);
                intent.putExtra("classFrom", ViewHabitsActivity.class.toString());
                startActivity(intent);


                return true;
            case R.id.delete:
                for (int i = 0 ; i<habitCreated.size();i++){
                    if (habitCreated.get(i).getTitle().equals(habitEditing.getTitle())
                            && habitCreated.get(i).getReason().equals(habitEditing.getReason())
                            && habitCreated.get(i).getDateStarted().equals(habitEditing.getDateStarted())){
                        habitCreated.remove(i);
                        i--;
                    }
                }

                intent = new Intent(ViewHabitsActivity.this, MainActivity.class);
                intent.putExtra("habitEdited", habitCreated);
                intent.putExtra("classFrom", ViewHabitsActivity.class.toString());
                startActivity(intent);

                return true;

            case R.id.goBack:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}