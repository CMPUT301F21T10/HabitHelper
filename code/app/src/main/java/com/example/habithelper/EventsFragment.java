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
EventsFragment hold all the pertinent data for all habit events of a user.
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventsFragment extends Fragment implements habitEventsCustomList.EventClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ArrayList<HabitEvent> HabitEvents_list = new ArrayList<>();
    RecyclerView Event_recyclerView;
    RecyclerView.Adapter HabitEventsAdapter;
    FirebaseUser user;
    FirebaseFirestore db;

    public EventsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Events.
     */
    // TODO: Rename and change types and number of parameters
    public static EventsFragment newInstance(String param1, String param2) {
        EventsFragment fragment = new EventsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get user data from db
        Intent intent = getActivity().getIntent();
        db = FirebaseFirestore.getInstance();
        user = (FirebaseUser) intent.getExtras().get("currentUser");
        String email = user.getEmail();


        CollectionReference collectionRef = db.collection("Habits")
                .document(email)
                .collection(email+"_habits");


        // for each habit for current user, retrieve all habit events from database and notify the recyclerview adapter
        collectionRef
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Habit retrievedHabit = new Habit(document);
                                Log.d("DOC_ID", "onComplete: " + document.getId());

                                CollectionReference eventCollectionRef = collectionRef
                                        .document(document.getId())
                                        .collection("HabitEvents");
                                //bug here to fix!
                                eventCollectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task2) {

                                        if (task2.isSuccessful()) {
                                            for (QueryDocumentSnapshot eventDoc : task2.getResult()) {
                                                HabitEvent retrievedHabitEvent = new HabitEvent(eventDoc);
                                                Log.d("HAVE2", "onComplete: " + retrievedHabitEvent.getEventTitle()
                                                        + " " + retrievedHabitEvent.getEventId());
                                                HabitEvents_list.add(retrievedHabitEvent);
                                                HabitEventsAdapter.notifyDataSetChanged();
                                            }
                                        }
                                    }
                                });
                            }
                        } else {
                            Log.d("NO_HABITS_EVENTS", "Error getting documents: ", task.getException());
                        }
                    }
                });

//        try {
//            TimeUnit.MILLISECONDS.sleep(100);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_events, container, false);

        Event_recyclerView = view.findViewById(R.id.habit_events_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        Event_recyclerView.setLayoutManager(layoutManager);

        HabitEventsAdapter = new habitEventsCustomList(HabitEvents_list, getContext(), this);
        Event_recyclerView.setAdapter(HabitEventsAdapter);
        return view;
    }

    @Override
    public void onEventItemClick(HabitEvent habit_event) {
        Log.d("EDIT_EVENT", "onEventItemClick: " + habit_event.getEventTitle());
        Intent intent = new Intent(getContext(), ViewHabitEventsActivity.class);
        intent.putExtra("habit", habit_event);
        intent.putExtra("currentUser", user);
        intent.putExtra("classFrom", MainActivity.class.toString());
        startActivity(intent);
    }
}