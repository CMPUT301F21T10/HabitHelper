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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.io.Serializable;
import java.util.ArrayList;


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

    /**
     * Empty constructor for HabitFragment
     */
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
//        try {
//            TimeUnit.MILLISECONDS.sleep(100);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
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

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        /**
         * On user swipe on a habit item, starts the create habit event activity
         * @param viewHolder
         *      The is the recycler view holder
         * @param direction
         *      This is the swiping direction (to the right)
         */
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

    /**
     * On user click on a habit item, start the view habit event activity
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