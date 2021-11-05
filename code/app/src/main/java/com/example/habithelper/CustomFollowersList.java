package com.example.habithelper;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CustomFollowersList extends ArrayAdapter<ArrayList<String>> {

    private ArrayList<ArrayList<String>> followers;
    private Context context;

    public CustomFollowersList(Context context, ArrayList<ArrayList<String>> followers){
        super(context, 0, followers);
        this.followers = followers;
        this.context = context;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.user_display_layout, parent,false);
        }

        ArrayList<String> followerNameEmail = followers.get(position);
        TextView name = view.findViewById(R.id.addFriendsRecyclerUserItem);

        name.setText(followerNameEmail.get(0));

        return view;
    }
}
