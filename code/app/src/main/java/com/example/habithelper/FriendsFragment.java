package com.example.habithelper;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FriendsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendsFragment extends Fragment {
    FirebaseFirestore db;
    Intent loginIntent;

    SearchView followSearch;
    ListView followersListView;
    CustomFollowersList followersAdapter;
    ArrayList<ArrayList<String>> followersDataList;
    ArrayList<String> followersEmailList;
    ArrayList<String> followersDataName;
    Button addFriendsButton;
    FloatingActionButton requestAlert;
    //ArrayList<String> Followers;

    //This should only be used for the collectUserData method
    FirebaseUser userInfo;
    //This can be used for other purposes
    User currentUser;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FriendsFragment() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Friends.
     */
    // TODO: Rename and change types and number of parameters
    public static FriendsFragment newInstance(String param1, String param2) {
        FriendsFragment fragment = new FriendsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;

    }

    /**
     * onCreate method gets the currentUser that was passed in the intent extras
     * checks if the user is not null, and sets up the followers/following fragment
     * and starts by displaying the followers list first.
     * @param savedInstanceState bundle
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        Intent intent = getActivity().getIntent();
        db = FirebaseFirestore.getInstance();
        FirebaseUser user = (FirebaseUser) intent.getExtras().get("currentUser");
        if (user != null) {
            onFollowersSelect();
        }
    }


    /**
     * onFollowersSelect retrieves the current user of the app from the database
     * and takes a DocumentSnapshot to set the DataList to the content of the
     * Followers collection on the Firebase, then updates the adapter for changes.
     *
     * This method is called during onCreate and anytime the Followers button is pressed.
     */
    public void onFollowersSelect(){

        Intent intent = getActivity().getIntent();
        db = FirebaseFirestore.getInstance();

        FirebaseUser user = (FirebaseUser) intent.getExtras().get("currentUser");
        String email = user.getEmail();
        DocumentReference docRef = db.collection("Users").document(email);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    followersDataList.clear();
                    followersAdapter = new CustomFollowersList(getContext(), followersDataList);
                    followersListView.setAdapter(followersAdapter);

                    followersEmailList = (ArrayList<String>) document.get("Followers");
                    for(String userEmail : followersEmailList) {
                        DocumentReference docRefUsers = db.collection("Users").document(userEmail);
                        docRefUsers.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    followersDataList.add((ArrayList<String>) document.get("UserData"));
                                    followersAdapter.notifyDataSetChanged();
                                    System.out.println(followersDataList);
                                }
                            }
                        });
                    }
                }
            }
        });

    }

    /**
     * onFollowingSelect retrieves the current user of the app from the database
     * and takes a DocumentSnapshot to set the DataList to the content of the
     * Following collection on the Firebase, then updates the adapter for changes.
     *
     * This method is called when the Following button is pressed and not in onCreate.
     */
    public void onFollowingSelect(){

        Intent intent = getActivity().getIntent();
        db = FirebaseFirestore.getInstance();

        FirebaseUser user = (FirebaseUser) intent.getExtras().get("currentUser");
        String email = user.getEmail();
        DocumentReference docRef = db.collection("Users").document(email);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    followersDataList.clear();
                    followersAdapter = new CustomFollowersList(getContext(), followersDataList);
                    followersListView.setAdapter(followersAdapter);

                    followersEmailList = (ArrayList<String>) document.get("Following");
                    for(String userEmail : followersEmailList) {
                        DocumentReference docRefUsers = db.collection("Users").document(userEmail);
                        docRefUsers.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    followersDataList.add((ArrayList<String>) document.get("UserData"));
                                    followersAdapter.notifyDataSetChanged();
                                    System.out.println(followersDataList);
                                }
                            }
                        });
                    }
                }
            }
        });
    }


    /**
     * Sets up the initial layout, button onClickListeners and initializes the arraylist and its adapter.
     * Allows the user to click back and forth between the lists and changes the content of the
     * view based on the database follower/following information of the current user.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return fragment_friends view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        followSearch = view.findViewById(R.id.search_bar);

        // OnClickListener to call onFollowersSelect and change the recycler view to followers list
        Button followersButton = (Button) view.findViewById(R.id.followersButton);
        Button followingButton = (Button) view.findViewById(R.id.followingButton);

        //initial colors for the buttons (represents the friends tab starting on followers first)
        followersButton.setBackgroundColor(Color.parseColor("#FF3700B3"));
        followingButton.setBackgroundColor(Color.parseColor("#FFBB86FC"));

        followersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // clear search bar on button click, get followers and set button colors
                onFollowersSelect();
                followersButton.setBackgroundColor(Color.parseColor("#FF3700B3"));
                followingButton.setBackgroundColor(Color.parseColor("#FFBB86FC"));
                followersAdapter.notifyDataSetChanged();
                followSearch.setQuery("", false);
            }
        });

        // OnClickListener to call onFollowingSelect and change the recycler view to following list
        followingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // clear search bar on button click, get following and set button colors
                onFollowingSelect();
                followingButton.setBackgroundColor(Color.parseColor("#FF3700B3"));
                followersButton.setBackgroundColor(Color.parseColor("#FFBB86FC"));
                followersAdapter.notifyDataSetChanged();
                followSearch.setQuery("", false);
            }
        });

        // setup the datalist, addapter and set the adapter for the correct view
        // the same datalist and adapter is used for both followers and following, only
        // the content of the datalist gets changed to avoid using two datalist, adapters and hiding them
        followersDataList = new ArrayList<>();
        followersEmailList = new ArrayList<>();
        followersDataName = new ArrayList<>();
        //followersAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, followersDataList);
        followersAdapter = new CustomFollowersList(getContext(), followersDataList);
        followersListView = (ListView) view.findViewById(R.id.following_List);
        followersListView.setAdapter(followersAdapter);


        // implement search bar and filter the list of followers/following by entry
        followSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                followersAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                followersAdapter.getFilter().filter(newText);
                return false;
            }
        });

        followersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = getActivity().getIntent();
                db = FirebaseFirestore.getInstance();
                FirebaseUser user = (FirebaseUser) intent.getExtras().get("currentUser");
                String currentUserEmail = user.getEmail();

                followersDataName = (ArrayList<String>) adapterView.getItemAtPosition(i);
                String selectedUser = followersDataName.get(1);
                FragmentTransaction fr = getParentFragmentManager().beginTransaction();

                fr.replace(R.id.fragmentContainerView, new DifferentProfileFragment(selectedUser, currentUserEmail));
                fr.commit();
            }
        });

        addFriendsButton = view.findViewById(R.id.addFriendsButton);
        addFriendsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fr = getParentFragmentManager().beginTransaction();
                fr.replace(R.id.fragmentContainerView, new AddFriendsFragment());
                fr.commit();
            }
        });

        requestAlert = view.findViewById(R.id.requestAlert);

        Intent intent = getActivity().getIntent();
        db = FirebaseFirestore.getInstance();

        FirebaseUser user = (FirebaseUser) intent.getExtras().get("currentUser");
        String email = user.getEmail();
        DocumentReference docRef = db.collection("Users").document(email);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    int requests = ((ArrayList<String>) document.get("RequestsReceived")).size();
                    if(requests > 0){
                        requestAlert.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        requestAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("requests button clicked");
                FragmentTransaction fr = getParentFragmentManager().beginTransaction();
                fr.replace(R.id.fragmentContainerView, new RequestsListFragment());
                fr.commit();
            }
        });
        return view;
    }
}
