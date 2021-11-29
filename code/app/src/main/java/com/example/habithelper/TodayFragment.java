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
package com.example.habithelper;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Displays a list of habits that needs to be done today
 */
public class TodayFragment extends Fragment implements habitsCustomList.ItemClickListener{

    ArrayList<Habit> HabitsList = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerView.Adapter HabitsAdapter;
    FirebaseUser user;
    FirebaseFirestore db;

    /**
     * Empty constructor for TodayFragment
     */
    public TodayFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get user data from db
        Intent intent = getActivity().getIntent();
        db = FirebaseFirestore.getInstance();
        user = (FirebaseUser) intent.getExtras().get("currentUser");
        String email = user.getEmail();


        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH) + 1;
        final int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        final int today = calendar.get(calendar.DAY_OF_WEEK);
        String today_date = year + "-" + month + "-" + dayOfMonth;

        CollectionReference collectionRef = db.collection("Habits")
                .document(email)
                .collection(email+"_habits");

        // retrieve all habits for current user from database and notify the recyclerview adapter
        collectionRef
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Habit retrievedHabit = new Habit(document);

                                // filter out today's habit only
                                String habitStartDate = retrievedHabit.getDateStarted();

                                Date date1 = null;
                                try {
                                    date1 = new SimpleDateFormat("yyyy-MM-dd").parse(habitStartDate);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                Date date2 = null;
                                try {
                                    date2 = new SimpleDateFormat("yyyy-MM-dd").parse(today_date);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                // check if habit date has already started
                                if (date1.compareTo(date2) <= 0) {
                                    // now check if habit is today
                                    String habitDays = retrievedHabit.getHabitDays();
                                    int index = (today-2) % 7;
                                    //Modified here as it was crashing for specific indices
                                    if (index > 0 && habitDays.length()>index && habitDays.charAt(index) == '1'){
                                        HabitsList.add(retrievedHabit);
                                    }
                                }
                            }
                            HabitsAdapter.notifyDataSetChanged();
                        } else {
                            Log.d("NO_HABITS", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_today, container, false);

        recyclerView = view.findViewById(R.id.today_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        HabitsAdapter = new habitsCustomList(HabitsList, getContext(), this);
        recyclerView.setAdapter(HabitsAdapter);
        return view;

    }

    /**
     * On user click on a habit item, start the view habit event activity
     * @param habit
     *      This is the habit item clicked
     */
    @Override
    public void onItemClick(Habit habit) {
//        Intent intent = new Intent(getContext(), ViewHabitsActivity.class);
//        intent.putExtra("habit", habit);
//        intent.putExtra("currentUser", user);
//        startActivity(intent);
    }
}