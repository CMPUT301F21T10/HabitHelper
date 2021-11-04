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

public class habitEventsCustomList extends RecyclerView.Adapter<habitEventsCustomList.MyEventViewHolder>{

    private ArrayList<HabitEvent> habitEvents_list;
    private Context context;
    private EventClickListener event_clickListener;


    public interface EventClickListener {
        public void onEventItemClick(HabitEvent habit_event);
    }

    public habitEventsCustomList(ArrayList<HabitEvent> habitEvents_list, Context context, EventClickListener event_clickListener) {
        this.habitEvents_list = habitEvents_list;
        this.context = context;
        this.event_clickListener = event_clickListener;
    }


    @NonNull
    @Override
    public MyEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.habit_row_contents, parent,false);

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
