package com.voot_austin.voot;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class RemindersActivity extends AppCompatActivity {

    private CheckBox electionReminders;
    private EditText firstName, lastName, street, city, zipCode, county, state;
    private ArrayList<EditText> editTextEntries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);

        // retrieve Firebase instance
        FirebaseApp.initializeApp(this);

        retrieveGUI();
        retrieveFirebaseEntries();

        setOnClick();

    }

    // set the on click of button to update user in database
    private void setOnClick() {

        Button button = findViewById(R.id.save_and_update_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = 0;
                while (!isNetworkAvailable() && i < 10) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ie) {
                        throw new RuntimeException("Interrupted in SignupFragment");
                    }
                    i++;
                }

                if (i >= 10)
                    return;

                final Map<String, Object> entryMap = getTextEntries();

                // update user
                if (entryMap != null) {

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if (user != null) {

                        String userEntry = String.format("%s/%s", DatabaseRefs.USERS_TABLE, user.getUid());
                        DatabaseReference userEntryRef = FirebaseDatabase.getInstance().getReference(userEntry);
                        userEntryRef.updateChildren(entryMap);

                    } else {
                        Toast.makeText(getApplicationContext(), "Please log in", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                }

            }
        });

    }

    // build gui
    private void retrieveGUI() {

        editTextEntries = new ArrayList<>();

        electionReminders = findViewById(R.id.upcoming_election_reminders_checkbox);
        editTextEntries.add(firstName = findViewById(R.id.first_name));
        editTextEntries.add(lastName = findViewById(R.id.last_name));
        editTextEntries.add(street = findViewById(R.id.street));
        editTextEntries.add(city = findViewById(R.id.city));
        editTextEntries.add(zipCode = findViewById(R.id.zip_code));
        editTextEntries.add(county = findViewById(R.id.county));
        editTextEntries.add(state = findViewById(R.id.state));

    }

    // assuming the EditText and CheckBoxes are retrieved from
    private void retrieveFirebaseEntries() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            finish();
        } else {
            // get reference to table to store user information
            String userEntry = String.format("%s/%s", DatabaseRefs.USERS_TABLE, user.getUid());
            DatabaseReference userEntryRef = FirebaseDatabase.getInstance().getReference(userEntry);

            userEntryRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    VootUser vootUser = dataSnapshot.getValue(VootUser.class);

                    if (vootUser != null) {
                        electionReminders.setChecked(vootUser.electionReminders);
                        firstName.setText(vootUser.firstname);
                        lastName.setText(vootUser.lastname);
                        street.setText(vootUser.street);
                        city.setText(vootUser.city);
                        state.setText(vootUser.state);
                        county.setText(vootUser.county);
                        zipCode.setText(vootUser.zipcode);
                    } else {
                        throw new NullPointerException("Voot user was found to be null!");
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    throw new IllegalStateException("In Reminders Activity, could not retrieve user data");
                }
            });
        }

    }

    // returns a map of editTexts to valid entries
    // returns null if the entered data is invalid
    private Map<String, Object> getTextEntries() {
        if (!isNetworkAvailable())
            return null;

        // none should be null or empty
        for (EditText editText : editTextEntries)
            if (editText == null)
                throw new NullPointerException("One of the edit texts was null");
            else if (editText.getText().toString().trim().matches(""))
                return null;

        // Map to return
        Map<String, Object> entryMap = new HashMap<>();

        // validate name
        String firstName_str = firstName.getText().toString().trim();
        String lastName_str = lastName.getText().toString().trim();
        if (!firstName_str.matches("[a-zA-Z]*") || !lastName_str.matches("[a-zA-Z]*")) {
            return null;
        } else {
            entryMap.put("firstname", firstName_str);
            entryMap.put("lastname", lastName_str);
        }

        // validate street, city, county, zipCode
        String street_str = street.getText().toString().trim();
        String city_str = city.getText().toString().trim();
        String county_str = county.getText().toString().trim();
        String zipCode_str = zipCode.getText().toString().trim();
        String state_str = state.getText().toString().trim();
        if (street_str.matches("[a-zA-Z\\s0-9]*") &&
                city_str.matches("[a-zA-Z\\s]*") &&
                county_str.matches("[a-zA-Z]*") &&
                zipCode_str.matches("[0-9]{5,6}") &&
                state_str.matches("[a-zA-Z]{2}"))
        {
            entryMap.put("street", street_str);
            entryMap.put("city", city_str);
            entryMap.put("county", county_str);
            entryMap.put("zipcode", zipCode_str);
            entryMap.put("state", state_str);
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.address_invalid), Toast.LENGTH_SHORT).show();
            return null;
        }

        entryMap.put("electionReminders", electionReminders.isChecked());

        return entryMap;
    }

    // returns true if the device is connected to the network, false otherwise
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        } else {
            return false;
        }

    }

}
