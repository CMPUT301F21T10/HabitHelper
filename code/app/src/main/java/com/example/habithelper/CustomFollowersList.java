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

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

// CustomFollowersList is a custom ArrayAdapter for displaying followers/following lists in the friends fragment
// Implements a Filter class that allows the app user to search and sort through the displayed users by username
// only, despite each user being represented by username and email. Filter class is required as simple search bar
// can not sort the users because of the ArrayList<String> representation that contains user email.

public class CustomFollowersList extends ArrayAdapter<ArrayList<String>> implements Filterable{

    private ArrayList<ArrayList<String>> followers;
    private ArrayList<ArrayList<String>> filteredFollowers;
    private CustomFilter cs;
    private Context context;

    public CustomFollowersList(@NonNull Context context, ArrayList<ArrayList<String>> list){
        super(context, 0, list);
        this.followers = list;
        this.filteredFollowers = list;
        this.context = context;
    }

    @Override
    public ArrayList<String> getItem(int i) {
        return followers.get(i);
    }

    @Override
    public int getCount(){
        if(followers != null) {
            return followers.size();
        }else{
            return 0;
        }
    }

    @Override
    public long getItemId(int i){
        return i;
    }


    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ArrayList<String> profile = getItem(position);

        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.user_display_layout, parent,false);
        }

        TextView name = view.findViewById(R.id.addFriendsRecyclerUserItem);

        name.setText(followers.get(position).get(0));

        return view;
    }


    @Override
    public Filter getFilter() {
        if(cs == null){
            cs = new CustomFilter();
        }
        return cs;
    }

    /**
     * CustomFilter performs filtering based on the input provided in the EditText field in friends fragment
     * each user in the full list is checked for the constraint (search) values and added to the filtered list
     * with both the name and the email, so future clicks on the profile can utilize the email as user id to open
     * user profiles. Results are used to update the array adapter and create the filtered look.
     */

    class CustomFilter extends Filter{
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();

            if(constraint != null && constraint.length() > 0) {
                constraint = constraint.toString().toUpperCase();
                ArrayList<ArrayList<String>> filters = new ArrayList<>();

                for (int i = 0; i < filteredFollowers.size(); i++) {
                    if (filteredFollowers.get(i).get(0).toUpperCase().contains(constraint)) {
                        ArrayList<String> profile = new ArrayList<>();
                        profile.add(filteredFollowers.get(i).get(0));
                        profile.add(filteredFollowers.get(i).get(1));
                        filters.add(profile);
                    }
                }

                filterResults.count = filters.size();
                filterResults.values = filters;
            }else{
                filterResults.count = filteredFollowers.size();
                filterResults.values = filteredFollowers;
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            followers = (ArrayList<ArrayList<String>>) filterResults.values;
            notifyDataSetChanged();
        }
    }
}


