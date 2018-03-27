package com.voot_austin.voot;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

// Written by Kayvon Khosrowpour
// on 3/26/18
public class SignupFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ALREADY_ENTERED_USERNAME = "ALREADY_ENTERED_USERNAME";

    // TODO: Rename and change types of parameters
    private String alreadyEnteredUsername;

    public SignupFragment() {
        // Required empty public constructor
    }

    // creates a LoginFragment and returns it for the master activity (GetUserActivity)
    public static SignupFragment newInstance(String alreadyEnteredUsername) {
        SignupFragment fragment = new SignupFragment();

        if (alreadyEnteredUsername != null) {
            Bundle args = new Bundle();
            args.putString(ALREADY_ENTERED_USERNAME, alreadyEnteredUsername);
            fragment.setArguments(args);
        }

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            alreadyEnteredUsername = getArguments().getString(ALREADY_ENTERED_USERNAME);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View signupView = inflater.inflate(R.layout.fragment_sign_up, container, false);
//
//        EditText userEmail = signupView.findViewById(R.id.login_email);
//        if (alreadyEnteredUsername != null)
//            userEmail.setText(alreadyEnteredUsername);
//
//
//
//        return signupView;
        return null; // TODO
    }

}
