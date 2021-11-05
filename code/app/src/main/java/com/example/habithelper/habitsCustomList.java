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
 * This class is an adapter for the recycler view in habits fragment
 */
public class habitsCustomList extends RecyclerView.Adapter<habitsCustomList.MyViewHolder> {

    private ArrayList<Habit> habits_list;
    private Context context;
    private ItemClickListener clickListener;

    /**
     * Interface to implement method onItemClick when a habit is clicked on the recycler view
     */
    public interface ItemClickListener {
        public void onItemClick(Habit habit);
    }

    /**
     * Constructor for habitsCustomList
     * @param habits_list
     * List of all the habit objects to be displayed
     * @param context
     * The context to display the habits
     * @param clickListener
     * ItemClickListener object to listen to "habit click"
     */
    public habitsCustomList(ArrayList<Habit> habits_list, Context context, ItemClickListener clickListener) {
        this.habits_list = habits_list;
        this.context = context;
        this.clickListener = clickListener;
    }


    /**
     * Inflate habit_row_contents to display a habit object details as a cardview element on recyclerview
     * @param parent
     * @param viewType
     * @return
     *  MyViewHolder object
     */
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.habit_row_contents, parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
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

    /**
     * Returns the count of the number of elements in the recycler view
     * @return
     *  The number of elements in the recycler view
     */
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
