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
EventsFragment hold all the pertinent data for all habits of a user.
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
import java.util.ArrayList;
import java.util.Collections;


/**
 * A simple {@link Fragment} subclass.
 * Displays the list of all habits
 */
public class HabitFragment extends Fragment implements Serializable, habitsCustomList.ItemClickListener {

    ArrayList<Habit> HabitsList = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerView.Adapter HabitsAdapter;
    FirebaseUser user;
    FirebaseFirestore db;

    /**
     * Empty constructor for HabitFragment
     */
    public HabitFragment() {
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
                                Log.d("HAVE", "onComplete: " + retrievedHabit.getTitle());
                                HabitsList.add(retrievedHabit);
                            }
                            HabitsAdapter.notifyDataSetChanged();
                        } else {
                            Log.d("NO_HABITS", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


    /**
     * Setting up the recycler view to the habits adapter
     * Inflating the view
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     *      The view to be returned
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_habits, container, false);

        recyclerView = view.findViewById(R.id.habits_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        HabitsAdapter = new habitsCustomList(HabitsList, getContext(), this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(HabitsAdapter);
        return view;
    }


    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP|
            ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, ItemTouchHelper.RIGHT) {

        /**
         * On user hold and drag on a habit item, user can reorder items in the list
         * @param recyclerView
         *      an reference to the recyclerView
         * @param viewHolder
         *      This is the recycler view holder
         * @param target
         *      a reference to the target position where the item is released
         */
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();

            Collections.swap(HabitsList, fromPosition, toPosition);

            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);

            return false;
        }

        /**
         * On user swipe on a habit item, starts the create habit event activity
         * @param viewHolder
         *      This is the recycler view holder
         * @param direction
         *      This is the swiping direction (to the right)
         */
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            Intent intent = new Intent(getContext(), CreateHabitEventActivity.class);
            Habit habit_to_create_event = HabitsList.get(viewHolder.getAdapterPosition());
            intent.putExtra("habit", habit_to_create_event);
            intent.putExtra("currentUser", user);
            intent.putExtra("habitCreated", HabitsList);
            intent.putExtra("classFrom", MainActivity.class.toString());
            startActivity(intent);
            HabitsAdapter.notifyDataSetChanged();
        }
    };


    /**
     * On user click on a habit item, start the view habit activity
     * @param habit
     *      This is the habit item clicked
     */
    @Override
    public void onItemClick(Habit habit) {
        Intent intent = new Intent(getContext(), ViewHabitsActivity.class);
        intent.putExtra("habit", habit);
        intent.putExtra("currentUser", user);
        startActivity(intent);
    }
}