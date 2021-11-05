package com.example.habithelper;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class ViewHabitsActivity extends AppCompatActivity implements Serializable {

    EditText editTextTitle,editTextStartDate, editTextReason;
    Bundle extras;
    Habit habitEditing;
    FirebaseFirestore db;
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

        db = FirebaseFirestore.getInstance();

        extras = getIntent().getExtras();
        if (extras != null){
            habitEditing = (Habit) extras.getSerializable("habit");
            user = (FirebaseUser) extras.get("currentUser");
        }

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

                Habit newEditedHabit = new Habit(habitTitle, habitReason, habitStartDate, true);

                String habitToEdit_title = habitEditing.getTitle();
                String emailToEdit = user.getEmail();

                DocumentReference docRefAdd = db.collection("Habits")
                        .document(emailToEdit)
                        .collection(emailToEdit + "_habits")
                        .document(newEditedHabit.getTitle());


                // write to db
                docRefAdd.set(newEditedHabit.generateAllHabitDBData())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                // These are a method which gets executed when the task is succeeded
                                Log.d("DATA_ADDED", "Data has been added successfully!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // These are a method which gets executed if there’s any problem
                                Log.d(TAG, "Data could not be added!" + e.toString());
                            }
                        });


                DocumentReference docRefDelete = db.collection("Habits")
                        .document(emailToEdit)
                        .collection(emailToEdit + "_habits")
                        .document(habitToEdit_title);

                docRefDelete.delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d("DELETED", "DocumentSnapshot successfully deleted!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("ERROR_DELETE", "Error deleting document", e);
                            }
                        });

                Intent intent = new Intent(ViewHabitsActivity.this, MainActivity.class);
                intent.putExtra("classFrom", ViewHabitsActivity.class.toString());
                intent.putExtra("currentUser", user);
                startActivity(intent);


                return true;
            case R.id.delete:

                String habitToDelete_title = habitEditing.getTitle();
                Log.d("TO_DELETE", "onOptionsItemSelected: " + habitToDelete_title);
                String email = user.getEmail();
                DocumentReference docRef = db.collection("Habits")
                        .document(email)
                        .collection(email + "_habits")
                        .document(habitToDelete_title);

                docRef.delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d("DELETED", "DocumentSnapshot successfully deleted!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("ERROR_DELETE", "Error deleting document", e);
                            }
                        });

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