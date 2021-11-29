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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.habithelper.R;
import com.example.habithelper.User;

import java.util.ArrayList;

public class customUserList extends ArrayAdapter<User> {


    private ArrayList<User> users;
    private ArrayList<User> filteredUsers;
    private customUserList.CustomFilterFriend cs;
    private Context context;

    public customUserList(Context context, ArrayList<User> users) {
        super(context, 0, users);
        this.users = users;
        this.filteredUsers = users;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.user_display_layout, parent, false);
        }

        User user = getItem(position);
        //User user = users.get(position);

        //Text views to display the required attributes in columns of the list.
        TextView userName = view.findViewById(R.id.addFriendsRecyclerUserItem);

        userName.setText(user.getName());
        return view;
    }


    @Override
    public User getItem(int i) {
        return users.get(i);
    }

    @Override
    public int getCount(){
        if(users != null) {
            return users.size();
        }else{
            return 0;
        }
    }

    @Override
    public long getItemId(int i){
        return i;
    }


    @Override
    public Filter getFilter() {
        if(cs == null){
            cs = new CustomFilterFriend();
        }
        return cs;
    }

    class CustomFilterFriend extends Filter{
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();

            if(constraint != null && constraint.length() > 0) {
                constraint = constraint.toString().toUpperCase();
                ArrayList<User> filters = new ArrayList<>();

                for (int i = 0; i < filteredUsers.size(); i++) {
                    if (filteredUsers.get(i).getName().toUpperCase().contains(constraint)) {
                        filters.add(filteredUsers.get(i));
                    }
                }

                filterResults.count = filters.size();
                filterResults.values = filters;
            }else{
                filterResults.count = filteredUsers.size();
                filterResults.values = filteredUsers;
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            users = (ArrayList<User>) filterResults.values;
            notifyDataSetChanged();
        }
    }
}
