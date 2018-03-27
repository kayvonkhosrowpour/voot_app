package com.voot_austin.voot;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

// Written by Kayvon Khosrowpour
// on 3/26/18
public class LoginFragment extends Fragment {

    public LoginFragment() {
        // Required empty public constructor
    }

    // creates a LoginFragment and returns it for the master activity (GetUserActivity)
    public static LoginFragment newInstance() {
        // optional: initialize LoginFragment with data from the activity
        return new LoginFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Create FirebaseAuth
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();

        // Inflate the layout for this fragment
        View loginView = inflater.inflate(R.layout.fragment_login, container, false);

        Button loginButton = loginView.findViewById(R.id.login_button);
        final EditText etEmail = loginView.findViewById(R.id.login_email);
        final EditText etPassword = loginView.findViewById(R.id.login_password);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("LOGIN", "signInWithEmail:success");

                                    Toast.makeText(getActivity(), getString(R.string.auth_success),
                                            Toast.LENGTH_SHORT).show();
                                    // go back to previous activity
                                    getActivity().finish();

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("LOGIN", "signInWithEmail:failure", task.getException());
                                    Toast.makeText(getActivity(), getString(R.string.auth_failed_error),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        return loginView;
    }

    //       // TODO: optional override of this method to retrieve bundle args (probably won't need)
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//
//        super.onCreate(savedInstanceState);
//
//
//    }


}
