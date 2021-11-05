package com.example.habithelper;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.common.data.DataBufferSafeParcelable;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddFriendsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFriendsFragment extends Fragment {
    private View view;
    private ListView allUsers;
    private ArrayList<User> allUsersList;
    private FirebaseFirestore db;
    List<DocumentSnapshot> documentList;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddFriendsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddFriendsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddFriendsFragment newInstance(String param1, String param2) {
        AddFriendsFragment fragment = new AddFriendsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_add_friends, null);
        allUsers = (ListView) view.findViewById(R.id.allUsersList);
        db = FirebaseFirestore.getInstance();
        CollectionReference cdb = db.collection("Users");
        System.out.println("CONTEXT: "+this.getContext());

        cdb.get().addOnCompleteListener((new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                QuerySnapshot d = task.getResult();
                documentList = d.getDocuments();

            }
        }));


        return view;
    }
    /**
     * Get the document information from the DB on the user passed to the function as an email
     * And convert it into a user object
     * @param email
     * @return
     */
    public void collectUserData(String email){
        DocumentReference docRef = db.collection("Users").document(email);
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
                afterUserLoad(newUser);
            }
        });
        return;
    }

    /**
     * Everything to be done after the user has logged in should go here
     * This will only happen after a user has been loaded in
     * @param newUser
     */
    public void afterUserLoad(User newUser){
        //Ensure we do actually have a user
        if (newUser == null){
            throw new NullPointerException("There is no user logged in!");
        }
    }


}