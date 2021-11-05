package com.example.habithelper;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class DifferentProfileFragment extends Fragment {

    private FirebaseFirestore db;
    private Intent loginIntent;
    private TextView name;
    private View view;
    private Button acceptRequest;
    private Button declineRequest;
    private Button sendRequest;
    private TextView Hobbies;
    private User selectedUser;
    private User currentUser;
    private String currentUserEmail;
    private String selectedUserEmail;
    private ArrayList<String> selectedUserData = new ArrayList<>();


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DifferentProfileFragment(String selectedUserEmail, String currentUserEmail) {
        // Required empty public constructor
        this.selectedUserEmail = selectedUserEmail;
        this.currentUserEmail = currentUserEmail;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        //All data will be attached to the user's email
        System.out.println("Selected User Email: " + selectedUserEmail);
        System.out.println("current User Email: " + currentUserEmail);

        Intent intent = getActivity().getIntent();
        loginIntent = new Intent(getActivity(), LoginActivity.class);
        db = FirebaseFirestore.getInstance();

        System.out.println(selectedUserEmail);
        collectUserData(currentUserEmail, selectedUserEmail);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = LayoutInflater.from(getActivity()).inflate(R.layout.different_profile_fragment, null);
        name = view.findViewById(R.id.profileDisplayName);
        acceptRequest = (Button) view.findViewById(R.id.acceptRequest);
        declineRequest = (Button) view.findViewById(R.id.declineRequest);
        sendRequest = (Button) view.findViewById(R.id.sendRequest);
        Hobbies = view.findViewById(R.id.userHobbies); //WILL BE CHANGED

        return view;
    }
    /**
     * Get the document information from the DB on the user passed to the function as an email
     * And convert it into a user object
     * @param currentUserEmail
     * @return
     */
    User currentNewUser = null;
    public void collectUserData(String currentUserEmail, String selectedUserEmail){
        DocumentReference currentDocRef = db.collection("Users").document(currentUserEmail);
        DocumentReference selectedDocRef = db.collection("Users").document(selectedUserEmail);
        currentDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        currentNewUser = new User(document);
                    } else {
                    }
                } else {
                }
                selectedDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        User selectedNewUser = null;
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                selectedNewUser = new User(document);
                            } else {
                            }
                        } else {
                        }
                        afterUserLoad(currentNewUser, selectedNewUser);
                    }
                });
            }
        });

        return;
    }

    public void afterUserLoad(User currentNewUser, User selectedNewUser){
        System.out.println("====================================");
        currentUser = currentNewUser;
        name.setText(selectedNewUser.getName());
        name.setVisibility(View.VISIBLE);
        System.out.println("SELECTED USER EMAIL: "+selectedUserEmail);

        //Remove all elements first



        //If current user is gollowing selected user, show the hobbies.
        for (int i = 0; i < currentUser.getFollowing().size(); i++){
            System.out.println("FOLLOWING FROM FRAGMENT: " + currentUser.getFollowing().get(i) + currentUser.getFollowing().indexOf(selectedUserEmail));
        }
        if (currentUser.getFollowing().indexOf(selectedUserEmail) >= 0) {
            Hobbies.setVisibility(View.VISIBLE);
        }
        if (currentUser.getRequestsReceived().indexOf(selectedUserEmail) >= 0) {
            //If current user received request from
            //selectedUser, show accept and decline buttons.
            acceptRequest.setVisibility(View.VISIBLE);
            declineRequest.setVisibility(View.VISIBLE);
        }
        if (currentUser.getRequestsSent().indexOf(selectedUserEmail) >= 0) {
            //If current user sent a request to the
            //selectedUser, show accept and decline buttons.
            sendRequest.setEnabled(false);
            sendRequest.setText("Request Sent");
            sendRequest.setVisibility(View.VISIBLE);
        }
        if (currentUser.getFollowing().indexOf(selectedUserEmail) < 0){

            sendRequest.setVisibility(View.VISIBLE);
        }

        //Call the accept request method with true value for the User when the Accept button is pressed.
        acceptRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentUser.acceptRequest(selectedUserEmail, true);
                acceptRequest.setVisibility(View.INVISIBLE);
                declineRequest.setVisibility(View.INVISIBLE);
                Hobbies.setVisibility(View.VISIBLE);
            }
        });

        //Call the accept request method with false for the User when the Accept button is pressed.
        declineRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentUser.acceptRequest(selectedUserEmail, false);
                acceptRequest.setVisibility(View.INVISIBLE);
                declineRequest.setVisibility(View.INVISIBLE);
            }
        });

        //Call the send request method of the currentUser when the user presses the Send Request button.
        sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentUser.sendRequest(selectedUserEmail);
                sendRequest.setText("Request Sent");
                sendRequest.setEnabled(false);
            }
        });
    }
}