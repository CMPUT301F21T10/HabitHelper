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

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * NewUserFragment holds the fields for the user to input data when creating a new account
 */
public class NewUserFragment extends DialogFragment{

    private OnFragmentInteractionListener listener;
    private EditText editName;
    private EditText editUserName;
    private EditText editPassword;

    NewUserFragment(){
    }

    public interface OnFragmentInteractionListener{
        void onOkPressed(User newUser);
    }


    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener){
            listener = (OnFragmentInteractionListener) context;
        }else{
            throw new RuntimeException(context.toString()
                    + "must implement OnFragmentInteractionListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_new_user, null);
        editName = view.findViewById(R.id.loginNewName);
        editUserName = view.findViewById(R.id.loginNewUsername);
        editPassword = view.findViewById(R.id.loginNewPassword);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("New User")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Sign Up", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                            //Create new user from the data the user has entered
                            User newUser = new User(editName.getText().toString(),
                                    editUserName.getText().toString(),
                                    editPassword.getText().toString());
                            listener.onOkPressed(newUser);
                        }
                }).create();
    }
}

