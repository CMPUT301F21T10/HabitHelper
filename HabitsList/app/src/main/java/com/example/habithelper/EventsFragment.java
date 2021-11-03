package com.example.habithelper;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }

        HabitEvents_list.add(new HabitEvent("Exercise event", "Exercised for 30 min", "03/11/2021"));
        HabitEvents_list.add(new HabitEvent("Meditation event", "Meditated for 15 min", "03/11/2021"));
        HabitEvents_list.add(new HabitEvent("Football event", "Played football", "03/11/2021"));
        HabitEvents_list.add(new HabitEvent("Exercise event", "Exercised for 30 min", "03/11/2021"));
        HabitEvents_list.add(new HabitEvent("Exercise event", "Exercised for 30 min", "03/11/2021"));
        HabitEvents_list.add(new HabitEvent("Exercise event", "Exercised for 30 min", "03/11/2021"));
        HabitEvents_list.add(new HabitEvent("Exercise event", "Exercised for 30 min", "03/11/2021"));
        HabitEvents_list.add(new HabitEvent("Exercise event", "Exercised for 30 min", "03/11/2021"));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_events, container, false);

        Event_recyclerView = view.findViewById(R.id.habit_events_recycler_view);

//        if (getArguments() != null){
//            ArrayList<Habit> habits = new ArrayList<>();
//            habits = (ArrayList<Habit>) getArguments().getSerializable("habitCreated");
//            for (Habit eachHabit:habits){
////                Toast.makeText(getContext(), eachHabit.getDateStarted(), Toast.LENGTH_SHORT).show();
//                HabitsList.add(eachHabit);
//            }
//        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        Event_recyclerView.setLayoutManager(layoutManager);

        HabitEventsAdapter = new habitEventsCustomList(HabitEvents_list, getContext(), this);
//        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        Event_recyclerView.setAdapter(HabitEventsAdapter);


        return view;
    }

    @Override
    public void onEventItemClick(HabitEvent habit_event) {
        Log.d("EDIT_EVENT", "onEventItemClick: " + habit_event.getEventTitle());
    }
}