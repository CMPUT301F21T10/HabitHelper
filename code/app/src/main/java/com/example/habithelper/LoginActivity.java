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

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Login Activity handles sign in and initiates sign up of users to the broader application
 */
public class LoginActivity extends AppCompatActivity implements NewUserFragment.OnFragmentInteractionListener{
    static final int REQUEST_IMAGE_CAPTURE = 1;
    Intent mainIntent;
    ImageView imageView;
    FirebaseFirestore db;
    CollectionReference userCollectionReference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        imageView = findViewById(R.id.imageView);

        mainIntent = new Intent(LoginActivity.this, MainActivity.class);

        //Initialize the database
        db = FirebaseFirestore.getInstance();
        userCollectionReference = db.collection("Users");

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        userCollectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    Log.d("USER_DATA", String.valueOf(doc.getData().get("User_Data")));
                }

            }
        });
    }


    /**
     * OnClick event for sign up button
     * Allow the user to generate a new account
     * @param view
     */
    public void onLoginSignUpClick(View view){
        //Show the user sign up fragment
        new NewUserFragment().show(getSupportFragmentManager(), "NEW_USER");
    }

    /**
     * OnClick event for the OK Button in the NewUserFragment
     * Add the user created in the fragment to firestore
     * Need to create a user in both the DB and in the authentication service
     */
    @Override
    public void onOkPressed(User newUser){
        createDBUser(newUser);
        createAuthenticationUser(newUser);
    }

    /**
     * Create a user with the firestore authentication service
     * @param newUser
     */
    public void createAuthenticationUser(User newUser){
        String email = newUser.email;
        String password = newUser.password;
        if (email.length() > 0 && password.length() > 0) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                mainIntent.putExtra("currentUser", user);
                                mainIntent.putExtra("classFrom", LoginActivity.class.toString());
                                startActivity(mainIntent);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.d(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "User creation failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }else{
            Toast.makeText(LoginActivity.this, "Invalid Fields.",
                    Toast.LENGTH_SHORT).show();

        }
    }

    /**
     * Create a user in the firestore database, identifiable by their email
     * Data will be put into collection "User"
     * @param newUser
     */
    public void createDBUser(User newUser){
        //Put the data from the suer object into a specific data structure
        HashMap<String, ArrayList<String>> newUserData = newUser.generateAllDBData();
        userCollectionReference
                .document(newUser.email)
                .set(newUserData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // These are a method which gets executed when the task is succeeded
                        Log.d(TAG, "Data has been added successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // These are a method which gets executed if there’s any problem
                        Log.d(TAG, "Data could not be added!" + e.toString());
                    }
                });
    }
    /**
     * OnClick event for the login button
     * Check if the user has entered valid credentials
     * If they have, send them back to the main activity, otherwise let them know
     * they have entered bad credentials
     * @param view
     */
    public void onLoginSignInClick(View view){
        //If the user has been defined by an ID, send that back to the main
        EditText emailEdit = findViewById(R.id.loginEditEmail);
        String email = emailEdit.getText().toString();
        EditText passwordEdit = findViewById(R.id.loginEditPassword);
        String password = passwordEdit.getText().toString();

        if (password.length() > 0 && email.length() > 0) {
            //Use the firestore authentication service to try to log the user in
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                //If the sign in was successful, return to the main activity
                                mainIntent.putExtra("currentUser", user);
                                mainIntent.putExtra("classFrom", LoginActivity.class.toString());
                                startActivity(mainIntent);
                                Toast.makeText(LoginActivity.this, "Authentication succeeded.",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.d(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }else{
            Toast.makeText(LoginActivity.this, "Authentication Failed.",
                    Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * Simple function to hide the keyboard once it no longer becomes necessary
     */
    public void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    public void onTextClick(View view){
        hideKeyboard();
    }

}