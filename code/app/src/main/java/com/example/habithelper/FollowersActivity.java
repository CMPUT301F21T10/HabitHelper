package com.example.habithelper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
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
        //FirebaseFirestore db = FirebaseFirestore.getInstance();
        //DocumentReference docRef = db.collection("Users").document("fX5pvzWewCVnRKDM7nJV");

        String[] names = {"Raj", "Yevhen", "Emily", "Yuvan", "Tokyo"};
        ArrayList<String> followersDataList = new ArrayList<>(Arrays.asList(names));

        followersAdapter = new ArrayAdapter<>(this, R.layout.followers_content, followersDataList);
        followersListView.setAdapter(followersAdapter);
        System.out.println("TRASH");

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
