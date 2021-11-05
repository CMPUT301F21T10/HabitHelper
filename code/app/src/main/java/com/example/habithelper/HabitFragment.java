package com.example.habithelper;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.os.Parcelable;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HabitFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HabitFragment extends Fragment implements Serializable, habitsCustomList.ItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ArrayList<Habit> HabitsList = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerView.Adapter HabitsAdapter;
    FirebaseUser user;
    FirebaseFirestore db;

    public HabitFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Habits.
     */
    // TODO: Rename and change types and number of parameters
    public static HabitFragment newInstance(String param1, String param2) {
        HabitFragment fragment = new HabitFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }

//        HabitsList.add(new Habit("Exercise", "To improve cardio", "09/11/2021", true));
//        HabitsList.add(new Habit("Meditation", "To relax", "13/11/2021", true));

        Intent intent = getActivity().getIntent();
        db = FirebaseFirestore.getInstance();
        user = (FirebaseUser) intent.getExtras().get("currentUser");

        String email = user.getEmail();

        CollectionReference collectionRef = db.collection("Habits")
                .document(email)
                .collection(email+"_habits");

        collectionRef
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("HAVE_HABIT", "haha");
                                Habit retrievedHabit = new Habit(document);
                                Log.d("HAVE", "onComplete: " + retrievedHabit.getTitle());
                                HabitsList.add(retrievedHabit);
                                Log.d("SIZE", "onComplete: " + HabitsList.size());
                            }
                            HabitsAdapter.notifyDataSetChanged();
                        } else {
                            Log.d("NO_HABITS", "Error getting documents: ", task.getException());
                        }
                    }
                });
//
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
        View view = inflater.inflate(R.layout.fragment_habits, container, false);

//        initRecyclerView(view);

        recyclerView = view.findViewById(R.id.habits_recycler_view);


        Log.d("SIZE2", "onCreateView: " + HabitsList.size());
        for (Habit eachHabit:HabitsList){
            Log.d("LOLOLO", "onCreateView: "+ eachHabit.getTitle());
        }



//        if (getArguments() != null){
////            ArrayList<Habit> habits = new ArrayList<>();
////            habits = (ArrayList<Habit>) getArguments().getSerializable("habitCreated");
////
////            for (Habit eachHabit:habits){
//////                Toast.makeText(getContext(), eachHabit.getDateStarted(), Toast.LENGTH_SHORT).show();
////                HabitsList.add(eachHabit);
////            }
//        }


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        HabitsAdapter = new habitsCustomList(HabitsList, getContext(), this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(HabitsAdapter);

        return view;
    }


    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            Intent intent = new Intent(getContext(), CreateHabitEventActivity.class);
            Habit habitEvent = HabitsList.get(viewHolder.getAdapterPosition());
            intent.putExtra("habit", habitEvent);
            intent.putExtra("currentUser", user);
            intent.putExtra("habitCreated", HabitsList);
            startActivity(intent);
            HabitsAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public void onItemClick(Habit habit) {
        Intent intent = new Intent(getContext(), ViewHabitsActivity.class);
        intent.putExtra("habit", habit);
        intent.putExtra("currentUser", user);
        startActivity(intent);
    }
}