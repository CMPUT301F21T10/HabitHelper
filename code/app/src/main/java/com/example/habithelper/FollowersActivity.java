package com.example.habithelper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;

public class FollowersActivity extends AppCompatActivity {
    ListView followersListView;
    ArrayAdapter<String> followersAdapter;
    ArrayList<String> followersDataList;

    FirebaseFirestore db;
    CollectionReference userCollectionReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_list);

        db = FirebaseFirestore.getInstance();
        userCollectionReference = db.collection("Users");

        followersListView = findViewById(R.id.following_List);
        followersDataList = new ArrayList<>();

        DocumentReference docRef = db.collection("Users").document("fX5pvzWewCVnRKDM7nJV"); //Hardcoded ID for now

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    ArrayList<String> Followers = (ArrayList<String>) document.get("Followers");
                    if (document.exists()) {
                        followersDataList.addAll(Followers);
                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });


        //String[] names = {"Raj", "Yevhen", "Emily", "Yuvan", "Tokyo"};
        //followersDataList = new ArrayList<>(Arrays.asList(names));


        //String value = document.getString("username");


        followersAdapter = new ArrayAdapter<>(this, R.layout.followers_content, followersDataList);
        followersListView.setAdapter(followersAdapter);


//        userCollectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
//                    FirebaseFirestoreException error) {
//                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
//                {
//                    Log.d("USER_DATA", String.valueOf(doc.getData().get("Followers")));
//                }
//
//            }
//        });

    }
}
