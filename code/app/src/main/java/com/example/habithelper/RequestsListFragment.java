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
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RequestsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RequestsListFragment extends Fragment {
    protected View view;

    private ListView requestsListView;
    private ArrayList<String> requestsIdsList = new ArrayList<>();
    private ArrayList<User> requestsList = new ArrayList<>();
    private FirebaseFirestore db;
    private ArrayAdapter<User> requestsArrayAdapter;
    private Intent loginIntent;
    private String selectedUserEmail;
    private String currentUserEmail;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RequestsListFragment() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RequestsListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RequestsListFragment newInstance(String param1, String param2) {
        RequestsListFragment fragment = new RequestsListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    /**
     * Set up the view for use by the user Including interfaces, database, and onClick events
     * Pulls the information about the current user's followers requests and add them to a list to be
     * displayed. Allows to perform onItemClick which will open the selected user's profile page and
     * allow the user to accept/decline the request and send one back if they wish to do so, or do nothing.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     *      The created view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_requests_list, null);
        requestsListView = view.findViewById(R.id.fragRequestsListView);
        db = FirebaseFirestore.getInstance();
        Intent intent = getActivity().getIntent();
        loginIntent = new Intent(getActivity(), LoginActivity.class);


        FirebaseUser user = (FirebaseUser) intent.getExtras().get("currentUser");
        String currentUserEmail = user.getEmail();
        System.out.println("CURRENT USER EMAIL: "+currentUserEmail);
        generateRequestsList(currentUserEmail); //Populate RequestsList and call adapter



        requestsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = getActivity().getIntent();
                db = FirebaseFirestore.getInstance();
                FirebaseUser user = (FirebaseUser) intent.getExtras().get("currentUser");
                String currentUserEmail = user.getEmail();
                User selectedUser = (User)adapterView.getItemAtPosition(i); //THESE ARE IDS FOR NOW, should be EMAIL
                FragmentTransaction fr = getParentFragmentManager().beginTransaction();
                //CREATE PROFILE OBJECT, pass selectedUserEmail and currentUserEmail. hardcoded for now
                System.out.println(selectedUser.getEmail() + "  " + currentUserEmail );
                fr.replace(R.id.fragmentContainerView, new DifferentProfileFragment(selectedUser.getEmail(), currentUserEmail));
                fr.commit();
            }
        });

        return view;
    }

    /**
     * Populates the requestList array with the ids of the requests this user got.
     * @param email
     *      email of user we want to check requests for
     * @return
     *      nothing
     */
    public void generateRequestsList(String email){
        DocumentReference docRef = db.collection("Users").document(email);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                User newUser = null;
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    requestsIdsList.addAll((ArrayList<String>) document.get("RequestsReceived"));
                }
                for(String i : requestsIdsList){
                    System.out.println("-------REQUESTS IDS--------"+i);
                }
                afterPopulating(requestsIdsList);
            }

        });
    }

    /**
     * Collect the information on each user
     * @param ids
     *      Users to get information on
     */
    public void afterPopulating(ArrayList<String> ids){
        System.out.println(requestsIdsList.size());
        Context context = this.getContext();
        for(String i : requestsIdsList){
            DocumentReference docRef = db.collection("Users").document(i);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    User newUser = null;
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            newUser = new User(document);
                        } else {
                            throw new RuntimeException("Invalid Firestore ID Given");
                        }
                    } else {
                        throw new RuntimeException("Firestore retrieval failed");
                    }
                    requestsList.add(newUser);
                    requestsArrayAdapter = new customUserList(context, requestsList);
                    requestsListView.setAdapter(requestsArrayAdapter);

                }

            });
        }
    }


}