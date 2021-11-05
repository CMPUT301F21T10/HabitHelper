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

public class ViewHabitsActivity extends AppCompatActivity implements Serializable {

    EditText editTextTitle,editTextStartDate, editTextReason;
    Bundle extras;
    ArrayList<Habit> habitCreated = new ArrayList<>();
    Habit habitEditing;
    FirebaseUser user;
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
            user = (FirebaseUser) extras.get("currentUser");
        }
        habitEditing = habitCreated.get(habitCreated.size()-1);
        editTextTitle.setText(habitEditing.getTitle());
        editTextStartDate.setText(habitEditing.getDateStarted());
        editTextReason.setText(habitEditing.getReason());

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
                intent.putExtra("currentUser", user);
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