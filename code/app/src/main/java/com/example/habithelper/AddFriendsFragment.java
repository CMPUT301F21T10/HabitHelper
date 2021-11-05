package com.example.habithelper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.data.DataBufferSafeParcelable;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
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

import javax.crypto.AEADBadTagException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddFriendsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFriendsFragment extends Fragment {
    protected View view;
    protected ListView allUsersListView;
    protected ArrayList<User> allUsersList = new ArrayList<>();
    protected FirebaseFirestore db;
    protected ArrayAdapter<User> userArrayAdapter;

    private ArrayList<String> allUserIds = new ArrayList<>();
    User userToShow;
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
        allUsersListView = (ListView) view.findViewById(R.id.allUsersList);
        db = FirebaseFirestore.getInstance();
        CollectionReference cdb = db.collection("Users");
        System.out.println("CONTEXT: "+this.getContext());
        Context context = this.getContext();


        cdb.get().addOnCompleteListener((new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                QuerySnapshot d = task.getResult();
                documentList = d.getDocuments();
                System.out.println("IDDD:"+documentList.get(0).getId());

                for(DocumentSnapshot dox : documentList){
                  userToShow = new User(dox);
                  allUsersList.add(userToShow);
                }
                for(User i : allUsersList){
                    System.out.println("WORKING!!:"+i.getName());
                }
                userArrayAdapter = new customUserList(context, allUsersList);
                allUsersListView.setAdapter(userArrayAdapter);

            }
        }));

        allUsersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
}