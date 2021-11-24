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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class ViewHabitEventsActivity extends AppCompatActivity {


    TextView viewDateCompleted, viewHabitName;
    EditText viewOptionalComments, viewOptionalLocation;
    FirebaseFirestore db;
    FirebaseUser user;
    Bundle extras;
    HabitEvent habitEventEditing;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_habit_events);
        setContentView(R.layout.activity_view_habit_events);
        //Setting up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Habit Events");

        db = FirebaseFirestore.getInstance();

        viewHabitName = findViewById(R.id.viewHabitName);
        viewDateCompleted = findViewById(R.id.viewDateCompleted);
        viewOptionalComments = findViewById(R.id.viewOptionalComments);
        viewOptionalLocation = findViewById(R.id.viewOptionalLocation);

        extras = getIntent().getExtras();
        //Getting the habit object being viewed, and the current user
        if (extras != null){
            habitEventEditing = (HabitEvent) extras.getSerializable("habit");
            user = (FirebaseUser) extras.get("currentUser");
        }

        viewHabitName.setText(habitEventEditing.getEventTitle());
        viewDateCompleted.setText(habitEventEditing.getEventDateCompleted());
        viewOptionalComments.setText(habitEventEditing.getEventComment());
        viewOptionalLocation.setText(habitEventEditing.getEventLocation());

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
                viewDateCompleted.setText(date);
            }
        };
        DatePickerDialog.OnDateSetListener finalSetListener = setListener;
        viewDateCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ViewHabitEventsActivity.this,
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


//                TextView viewHabitName = findViewById(R.id.viewHabitName);
//                viewDateCompleted = findViewById(R.id.viewDateCompleted);
//                EditText viewOptionalComments = findViewById(R.id.viewOptionalComments);
//                EditText viewOptionalLocation = findViewById(R.id.viewOptionalLocation);

//                TextView viewDateCompleted, viewHabitName;
//                EditText viewOptionalComments, viewOptionalLocation;


                String habitEventsTitle = String.valueOf(viewHabitName.getText());
                String habitEventDate = String.valueOf(viewDateCompleted.getText());;
                String habitEventComment = String.valueOf(viewOptionalComments.getText());
                String habitEventLocation = String.valueOf(viewOptionalLocation.getText());

//???????????????                // save does not work for the time being because lat and long added!!!!!
                HabitEvent newEditedHabitEvent = new HabitEvent(habitEventsTitle, habitEventComment, habitEventDate, habitEventEditing.getAssociatedHabitTitle(),
                        habitEventLocation);
                String emailToEdit = user.getEmail();

                //adding the new edited habit to the database
                newEditedHabitEvent.addHabitEventToDB(emailToEdit, db);

                //deleting the old habit from the database
                habitEventEditing.deleteHabitEventFromDB(emailToEdit, db);

                Intent intent = new Intent(ViewHabitEventsActivity.this, MainActivity.class);
                intent.putExtra("classFrom", ViewHabitsActivity.class.toString());
                intent.putExtra("currentUser", user);
                startActivity(intent);
                return true;

            case R.id.delete:
                String email = user.getEmail();
                //deleting the habit from the database
                habitEventEditing.deleteHabitEventFromDB(email, db);
                intent = new Intent(ViewHabitEventsActivity.this, MainActivity.class);
                intent.putExtra("classFrom", ViewHabitEventsActivity.class.toString());
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