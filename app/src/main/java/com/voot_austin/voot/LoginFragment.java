package com.voot_austin.voot;

import android.content.Context;
import android.content.Intent;
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
import android.widget.FrameLayout;
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

        Log.d("LOGIN FRAG MSG", "\n\n\n\n\nWE ARE CREATING THE LOGIN FRAGMENT\n\n\n\n\n");

        // Inflate the layout for this fragment
        View loginView = inflater.inflate(R.layout.fragment_login, container, false);

        Button loginButton = loginView.findViewById(R.id.login_button);
        Button signupButton = loginView.findViewById(R.id.sign_up_button);
        final EditText etEmail = loginView.findViewById(R.id.login_email);
        final EditText etPassword = loginView.findViewById(R.id.login_password);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("LOGIN BUTTON DEBUG MSG", "\n\n\n\n\nWE HAVE ENTERED THE LOGIN BUTTON ON CLICK\n\n\n\n\n");

                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                if (email.trim().isEmpty() || password.trim().isEmpty())
                    return;

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

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Make the signup fragment
                String email = etEmail.getText().toString();
                SignupFragment signupFragment = SignupFragment.newInstance(email);

                // Add the fragment to the 'fragment_container' FrameLayout
                if (getActivity().findViewById(R.id.fragment_container) != null) {
                    FrameLayout frameLayout = getActivity().findViewById(R.id.fragment_container);
                    frameLayout.removeAllViews();
                    getActivity().getSupportFragmentManager().beginTransaction()
                                 .replace(R.id.fragment_container, signupFragment).addToBackStack("Signup Frag").commit();
                } else {
                    throw new IllegalStateException("IN LOGIN FRAGMENT: fragment container is null");
                }

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
