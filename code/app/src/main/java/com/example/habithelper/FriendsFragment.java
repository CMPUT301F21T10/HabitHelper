package com.example.habithelper;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

    ListView followersListView;
    ArrayAdapter<String> followersAdapter;
    ArrayList<String> followersDataList;
    //ArrayList<String> Followers;

    //This should only be used for the collectUserData method
    private User userToReturn;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        Intent intent = getActivity().getIntent();
        loginIntent = new Intent(getActivity(), LoginActivity.class);
        db = FirebaseFirestore.getInstance();

        FirebaseUser user = (FirebaseUser) intent.getExtras().get("currentUser");
        System.out.println(user.getEmail());
        if (user != null) {
            //All data will be attached to the user's email
            String email = user.getEmail();
            followersDataList = new ArrayList<>();

            DocumentReference docRef = db.collection("Users").document(email);

            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        System.out.println("This is the TEST LINE IN LOOP: " + (ArrayList<String>) document.get("Followers"));
                        followersDataList.addAll((ArrayList<String>) document.get("Followers"));
                        System.out.println("This is the TEST LINE IN LOOP AFTER ADDALL: " + followersDataList);
                        System.out.println("Adapter " + followersAdapter);
                        System.out.println("DataList " + followersDataList);
                        followersAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_friends, container, false);


        followersAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, followersDataList);
        followersListView = (ListView) view.findViewById(R.id.following_List);
        followersListView.setAdapter(followersAdapter);
        return view;
    }
}
