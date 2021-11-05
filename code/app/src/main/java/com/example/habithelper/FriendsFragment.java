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
    ArrayAdapter<String> followersAdapter;
    ArrayList<String> followersDataList;
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
                    followersDataList.addAll((ArrayList<String>) document.get("Followers"));
                    // notifyDataSetChanged here AND in onclick below to have the listview change correctly
                    followersAdapter.notifyDataSetChanged();
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
                    followersDataList.addAll((ArrayList<String>) document.get("Following"));
                    // notifyDataSetChanged here AND in onclick below to have the listview change correctly
                    followersAdapter.notifyDataSetChanged();
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


        // OnClickListener to call onFollowersSelect and change the recycler view to followers list
        Button followersButton = (Button) view.findViewById(R.id.followersButton);
        Button followingButton = (Button) view.findViewById(R.id.followingButton);
        followersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFollowersSelect();
                //followersAdapter.getFilter().filter(null);
                followersButton.setBackgroundColor(Color.GREEN);
                followingButton.setBackgroundColor(Color.RED);
                followersAdapter.notifyDataSetChanged();
            }
        });

        // OnClickListener to call onFollowingSelect and change the recycler view to following list
        followingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFollowingSelect();
                //followersAdapter.getFilter().filter(null);
                followersButton.setBackgroundColor(Color.RED);
                followingButton.setBackgroundColor(Color.GREEN);
                followersAdapter.notifyDataSetChanged();
            }
        });

        // setup the datalist, addapter and set the adapter for the correct view
        // the same datalist and adapter is used for both followers and following, only
        // the content of the datalist gets changed to avoid using two datalist, adapters and hiding them
        followersDataList = new ArrayList<>();
        followersAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, followersDataList);
        followersListView = (ListView) view.findViewById(R.id.following_List);
        followersListView.setAdapter(followersAdapter);

        followSearch = view.findViewById(R.id.search_bar);
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
                String selectedUser = (String)adapterView.getItemAtPosition(i); //THESE ARE IDS FOR NOW, should be EMAIL
                FragmentTransaction fr = getParentFragmentManager().beginTransaction();

                //CREATE PROFILE OBJECT, pass selectedUserEmail and currentUserEmail. hardcoded for now
                //YEVEHEN RETURN THE ID OF SELECTEDUSER
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

        int requests = 5; //YEVEHEN PUT NUMBER OF REQUESTS HERE.

        if(requests > 0){
            requestAlert.setVisibility(View.VISIBLE);
        }
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
