package com.example.homescreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class CreateHabit extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_habit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create Habit");

        Button mon_btn = findViewById(R.id.mon_btn);
        final boolean[] mon_clicked = {false};

        Button tue_btn = findViewById(R.id.tue_btn);
        final boolean[] tue_clicked = {false};

        Button wed_btn = findViewById(R.id.wed_btn);
        final boolean[] wed_clicked = {false};

        Button thur_btn = findViewById(R.id.thur_btn);
        final boolean[] thur_clicked = {false};

        Button fri_btn = findViewById(R.id.fri_btn);
        final boolean[] fri_clicked = {false};

        Button sat_btn = findViewById(R.id.sat_btn);
        final boolean[] sat_clicked = {false};

        Button sun_btn = findViewById(R.id.sun_btn);
        final boolean[] sun_clicked = {false};

        mon_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mon_clicked[0]){
                    mon_clicked[0] = true;
                    mon_btn.setBackgroundColor(getResources().getColor(R.color.button_clicked));
                }
                else {
                    mon_clicked[0] = false;
                    mon_btn.setBackgroundColor(getResources().getColor(R.color.purple_500));
                }
            }
        });

        tue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!tue_clicked[0]){
                    tue_clicked[0] = true;
                    tue_btn.setBackgroundColor(getResources().getColor(R.color.button_clicked));
                }
                else {
                    tue_clicked[0] = false;
                    tue_btn.setBackgroundColor(getResources().getColor(R.color.purple_500));
                }
            }
        });

        wed_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!wed_clicked[0]){
                    wed_clicked[0] = true;
                    wed_btn.setBackgroundColor(getResources().getColor(R.color.button_clicked));
                }
                else {
                    wed_clicked[0] = false;
                    wed_btn.setBackgroundColor(getResources().getColor(R.color.purple_500));
                }
            }
        });

        thur_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!thur_clicked[0]){
                    thur_clicked[0] = true;
                    thur_btn.setBackgroundColor(getResources().getColor(R.color.button_clicked));
                }
                else {
                    thur_clicked[0] = false;
                    thur_btn.setBackgroundColor(getResources().getColor(R.color.purple_500));
                }
            }
        });

        fri_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!fri_clicked[0]){
                    fri_clicked[0] = true;
                    fri_btn.setBackgroundColor(getResources().getColor(R.color.button_clicked));
                }
                else {
                    fri_clicked[0] = false;
                    fri_btn.setBackgroundColor(getResources().getColor(R.color.purple_500));
                }
            }
        });

        sat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!sat_clicked[0]){
                    sat_clicked[0] = true;
                    sat_btn.setBackgroundColor(getResources().getColor(R.color.button_clicked));
                }
                else {
                    sat_clicked[0] = false;
                    sat_btn.setBackgroundColor(getResources().getColor(R.color.purple_500));
                }
            }
        });

        sun_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!sun_clicked[0]){
                    sun_clicked[0] = true;
                    sun_btn.setBackgroundColor(getResources().getColor(R.color.button_clicked));
                }
                else {
                    sun_clicked[0] = false;
                    sun_btn.setBackgroundColor(getResources().getColor(R.color.purple_500));
                }
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
        switch (item.getItemId()){
            case R.id.createHabit:
                Toast.makeText(this, "Habit Created", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(CreateHabit.this, MainActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}