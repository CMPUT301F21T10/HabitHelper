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

