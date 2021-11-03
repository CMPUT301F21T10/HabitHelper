package com.example.habithelper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class habitsCustomList extends RecyclerView.Adapter<habitsCustomList.MyViewHolder> {

    private ArrayList<Habit> habits_list;
    private Context context;
    private ItemClickListener clickListener;


    public interface ItemClickListener {
        public void onItemClick(Habit habit);
    }

    public habitsCustomList(ArrayList<Habit> habits_list, Context context, ItemClickListener clickListener) {
        this.habits_list = habits_list;
        this.context = context;
        this.clickListener = clickListener;
    }

//    public habitsCustomList(List<Habit> habits_list) {
//        this.habits_list = habits_list;
//    }

    @NonNull
    @Override
    public habitsCustomList.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.habit_row_contents, parent,false);

        View view = LayoutInflater.from(context).inflate(R.layout.habit_row_contents, parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull habitsCustomList.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.habitTitle_textView.setText(habits_list.get(position).getTitle());
        holder.habitComment_textView.setText(habits_list.get(position).getReason());
        holder.habitDate_textView.setText(habits_list.get(position).getDateStarted());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onItemClick(habits_list.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return habits_list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView habitTitle_textView, habitComment_textView,habitDate_textView;

        public MyViewHolder (View view) {
            super(view);
            habitTitle_textView = view.findViewById(R.id.habitTitleTextView);
            habitComment_textView = view.findViewById(R.id.habitComment_textView);
            habitDate_textView = view.findViewById(R.id.habitDate_textView);

        }
    }
}
