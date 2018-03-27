package com.voot_austin.voot;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

// Written by Kayvon Khosrowpour
// on 3/26/18
public class SignupFragment extends Fragment {

    private EditText userEmail, userPassword, confirmPassword, firstName,
                     lastName, street, city, zipCode, county, state;
    private ArrayList<EditText> editTextEntries;

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

        final FirebaseAuth mAuth = FirebaseAuth.getInstance();

        // Inflate the layout for this fragment
        View signupView = inflater.inflate(R.layout.fragment_signup, container, false);

        editTextEntries.add(userEmail = signupView.findViewById(R.id.login_email));
        if (alreadyEnteredUsername != null)
            userEmail.setText(alreadyEnteredUsername);

        editTextEntries.add(userPassword = signupView.findViewById(R.id.password));
        editTextEntries.add(confirmPassword = signupView.findViewById(R.id.confirm_password));
        editTextEntries.add(firstName = signupView.findViewById(R.id.first_name));
        editTextEntries.add(lastName = signupView.findViewById(R.id.last_name));
        editTextEntries.add(street = signupView.findViewById(R.id.street));
        editTextEntries.add(city = signupView.findViewById(R.id.city));
        editTextEntries.add(zipCode = signupView.findViewById(R.id.zip_code));
        editTextEntries.add(county = signupView.findViewById(R.id.county));
        editTextEntries.add(state = signupView.findViewById(R.id.state));

        Button signUpButton = signupView.findViewById(R.id.sign_up_button);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<EditText, String> entryMap = getTextEntries();
                if (entryMap != null) {
                    mAuth.createUserWithEmailAndPassword(entryMap.get(userEmail), entryMap.get(userPassword))
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "createUserWithEmail:success");
                                        Toast.makeText(getActivity(), getString(R.string.signup_success),
                                                Toast.LENGTH_SHORT).show();

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(getActivity(), getString(R.string.auth_failed_error),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }

        });


        return signupView;
    }

    // returns a map of editTexts to valid entries
    // returns null if the entered data is invalid
    private Map<EditText, String> getTextEntries() {
        if (!isNetworkAvailable())
            return null;

        // none should be null or empty
        for (EditText editText : editTextEntries)
            if (editText == null)
                throw new NullPointerException("One of the edit texts was null");
            else if (editText.getText().toString().trim().matches(""))
                return null;

        // Map to return
        Map<EditText, String> entryMap = new HashMap<>();

        // validate user email
        String userEmail_str = userEmail.getText().toString().trim();
        if (userEmail_str.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
            entryMap.put(userEmail, userEmail_str);
        } else {
            Toast.makeText(getActivity(), getString(R.string.invalid_email), Toast.LENGTH_SHORT).show();
            return null;
        }
        // validate userPassword
        String userPassword_str = userPassword.getText().toString();
        boolean noTrailingWhiteSpace = userPassword_str.trim().length() == userPassword_str.length();
        boolean containsUppercase = !userPassword_str.equals(userPassword_str.toUpperCase());
        boolean containsLowercase = !userPassword_str.equals(userPassword_str.toLowerCase());
        boolean isAtLeast8 = userPassword_str.length() >= 8;
        boolean hasSpecial = !userPassword_str.matches("[A-Za-z0-9 ]*");
        boolean confirmEquals = userEmail_str.equals(confirmPassword.getText().toString());
        boolean pwdHasAllConditions = noTrailingWhiteSpace && containsUppercase
                                        && containsLowercase && isAtLeast8
                                        && hasSpecial && confirmEquals;
        if (pwdHasAllConditions) {
            entryMap.put(userPassword, userPassword_str);
        } else {
            String errorMsg;
            if (!noTrailingWhiteSpace)
                errorMsg = getString(R.string.no_trailing_white_space);
            else if (!containsUppercase)
                errorMsg = getString(R.string.must_contain_at_least_one_uppercase_char);
            else if (!containsLowercase)
                errorMsg = getString(R.string.must_contain_at_least_one_lowercase_char);
            else if (!isAtLeast8)
                errorMsg = getString(R.string.must_by_at_least_8_chars_long);
            else if (!hasSpecial)
                errorMsg = getString(R.string.must_contain_at_least_1_special_char);
            else if (!confirmEquals)
                errorMsg = getString(R.string.passwords_do_not_match);
            else
                errorMsg = getString(R.string.invalid_input);

            Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_SHORT).show();
            return null;
        }

        // validate name
        String firstName_str = firstName.getText().toString().trim();
        String lastName_str = lastName.getText().toString().trim();
        if (!firstName_str.matches("[a-zA-Z]*") || !lastName_str.matches("[a-zA-Z]*")) {
            return null;
        } else {
            entryMap.put(firstName, firstName_str);
            entryMap.put(lastName, lastName_str);
        }

        // validate street, city, county, zipCode
        String street_str = street.getText().toString().trim();
        String city_str = city.getText().toString().trim();
        String county_str = county.getText().toString().trim();
        String zipCode_str = zipCode.getText().toString().trim();
        String state_str = state.getText().toString().trim();
        if (street_str.matches("[a-zA-Z]*") &&
                city_str.matches("[a-zA-Z]*") &&
                county_str.matches("[a-zA-Z]*") &&
                zipCode_str.matches("[0-9]{6}") &&
                state_str.matches("[a-zA-Z]{2}]"))
        {
            entryMap.put(street, street_str);
            entryMap.put(city, city_str);
            entryMap.put(county, county_str);
            entryMap.put(zipCode, zipCode_str);
            entryMap.put(state, state_str);
        } else {
            Toast.makeText(getActivity(), getString(R.string.address_invalid), Toast.LENGTH_SHORT).show();
            return null;
        }

        return entryMap;
    }

    // returns true if the device is connected to the network, false otherwise
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        } else {
            return false;
        }

    }

}
