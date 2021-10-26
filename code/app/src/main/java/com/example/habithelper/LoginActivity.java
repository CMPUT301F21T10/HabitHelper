package com.example.habithelper;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.HashMap;


public class LoginActivity extends AppCompatActivity implements NewUserFragment.OnFragmentInteractionListener{

    Intent mainIntent;
    Intent testIntent;
    FirebaseFirestore db;
    CollectionReference userCollectionReference;
    Integer maxUserID = 2;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mainIntent = new Intent(this, MainActivity.class);


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

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            //reload();
        }
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
     * Note: currently do not provide an ID so firestore randomly makes one, consult with
     * TA if this is appropriate
     */
    @Override
    public void onOkPressed(User newUser){
        /*
        //Note from Emily : May need to remove this later, yet to be determined
        //Currently commented out for testing purposes
        HashMap<String, ArrayList<String>> newUserData = new HashMap<>();
        newUserData.put("UserData", newUser.generateDBData());
        newUserData.put("Requests", newUser.generateRequestList());
        newUserData.put("Followers", newUser.generateFollowersList());
        newUserData.put("Following", newUser.generateFollowingList());
        userCollectionReference
                .document()
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
                });*/
        createAuthenticationUser(newUser);
    }

    public void createAuthenticationUser(User newUser){
        String email = newUser.userName;
        String password = newUser.password;
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            mainIntent.putExtra("currentUser", user);
                            startActivity(mainIntent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
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
        /*
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
                            startActivity(mainIntent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });*/

    }
    /**
     * Simple function to hide the keyboard once it no longer becomes necessary
     */
    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    public void onTextClick(View view){
        hideKeyboard();
    }

    public void onTestFollowers(View view){
        testIntent = new Intent(this, FollowersActivity.class);
        startActivity(testIntent);
    }
}