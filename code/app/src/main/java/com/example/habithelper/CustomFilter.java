package com.example.habithelper;

import android.widget.Filter;

import java.util.ArrayList;

public class CustomFilter extends Filter {
    private ArrayList<ArrayList<String>> followers;
    private ArrayList<ArrayList<String>> filteredFollowers;

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();

        if(constraint == null || constraint.length() == 0) {
            ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>(followers);
            results.values = list;
            results.count = list.size();
        } else {
            ArrayList<ArrayList<String>> newValues = new ArrayList<ArrayList<String>>();
            for(int i = 0; i < followers.size(); i++) {
                ArrayList<String> e_item = followers.get(i);
                if(e_item.contains(constraint)) {
                    newValues.add(e_item);
                }
            }
            results.values = newValues;
            results.count = newValues.size();
        }
        return results;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        filteredFollowers = (ArrayList<ArrayList<String>>) results.values;
        notifyDataSetChanged();
    }

    private void notifyDataSetChanged() {

    }
}