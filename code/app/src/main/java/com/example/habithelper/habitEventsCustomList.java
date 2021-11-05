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

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


/**
 * This class is an adapter for the recycler view in events fragment
 */
public class habitEventsCustomList extends RecyclerView.Adapter<habitEventsCustomList.MyEventViewHolder>{

    private ArrayList<HabitEvent> habitEvents_list;
    private Context context;
    private EventClickListener event_clickListener;

    /**
     * Interface to implement method onEventItemClick when a habitEvent is clicked on the recycler view
     */
    public interface EventClickListener {
        public void onEventItemClick(HabitEvent habit_event);
    }

    /**
     * Constructor for habitEventsCustomList
     * @param habitEvents_list
     *  List of all habit events to be displayed
     * @param context
     *  The context to display the habit events
     * @param event_clickListener
     * EventClickListener object to listen to "habitEvent click"
     */
    public habitEventsCustomList(ArrayList<HabitEvent> habitEvents_list, Context context, EventClickListener event_clickListener) {
        this.habitEvents_list = habitEvents_list;
        this.context = context;
        this.event_clickListener = event_clickListener;
    }


    /**
     * Inflate habit_event_row_contents to display a habit object details as a cardview element on recyclerview
     * @param parent
     * @param viewType
     * @return
     *  MyEventViewHolder object
     */
    @NonNull
    @Override
    public MyEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.habit_event_row_contents, parent,false);

        return new MyEventViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyEventViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.habitEventTitle_textView.setText(habitEvents_list.get(position).getEventTitle());
        holder.habitEventComment_textView.setText(habitEvents_list.get(position).getEventComment());
        holder.habitEventDateCompleted_textView.setText(habitEvents_list.get(position).getEventDateCompleted());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                event_clickListener.onEventItemClick(habitEvents_list.get(position));
            }
        });
    }

    /**
     * Returns the count of the number of elements in the recycler view
     * @return
     *  The number of elements in the recycler view
     */
    @Override
    public int getItemCount() {
        return habitEvents_list.size();
    }


    public class MyEventViewHolder extends RecyclerView.ViewHolder {

        TextView habitEventTitle_textView, habitEventComment_textView,habitEventDateCompleted_textView;

        public MyEventViewHolder (View view) {
            super(view);
            habitEventTitle_textView = view.findViewById(R.id.habitEventTitleTextView);
            habitEventComment_textView = view.findViewById(R.id.habitEventComment_textView);
            habitEventDateCompleted_textView = view.findViewById(R.id.habitEventDateCompleted_textView);

        }
    }

}
