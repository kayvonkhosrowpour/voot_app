package com.voot_austin.voot;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private boolean DEBUG = false;

    // voot user information
    private VootUser vootUser;
    public static final String VOOT_USER = "vootUser";

    // UI Elements
    private TextView userGreeting;
    private TextView location;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // DO NOT REMOVE: set theme of entire app, once
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        // Initialize Firebase
        FirebaseApp.initializeApp(this);

        getUIElements();

        // load shared preferences and display them
        this.vootUser = VootUserFetcher.loadSharedPreferences(this);
        updateFields();

        // connect to firebase
        firebaseAuth = FirebaseAuth.getInstance();

        // redirect to log-in screen or fill UI with user info
        establishLogin();

        // build UI for navigation TODO: change this to something pretty
        initButtons();

    }

    // update fields
    private void updateFields() {

        if (this.vootUser == null)
            throw new IllegalStateException("Voot user was null, couldn't update fields");

        this.userGreeting.setText(String.format("Hello, %s", vootUser.firstname));
        this.location.setText(String.format(" %s, %s %s", vootUser.city, vootUser.state, vootUser.zipcode));
    }


    // get the UI elements from XML
    private void getUIElements() {
        this.userGreeting = findViewById(R.id.user_greeting);
        this.location = findViewById(R.id.location);
    }

    @Override
    protected void onStart() {
        super.onStart();
        establishLogin();
    }

    @Override
    protected void onResume() {
        super.onResume();
        establishLogin(); // double-check that we have a user
    }

    // re-direct to login if not logged in, otherwise update UI
    private void establishLogin() {

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(this, GetUserActivity.class);
            startActivity(intent);
        } else {

            // get reference to table to store user information
            String userEntry = String.format("%s/%s", DatabaseRefs.USERS_TABLE, currentUser.getUid());
            DatabaseReference userEntryRef = FirebaseDatabase.getInstance().getReference(userEntry);

            userEntryRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    VootUser receivedVootUser = dataSnapshot.getValue(VootUser.class);

                    if (receivedVootUser != null) {

                        vootUser = receivedVootUser;
                        VootUserFetcher.saveSharedPreferences(getApplicationContext(), vootUser);
                        updateFields();
                        Log.d("POJO", "Set voot user");

                    } else {
                        throw new NullPointerException("Voot user was found to be null!");
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    throw new IllegalStateException("In Main Activity, could not retrieve user data");
                }
            });

        }

    }

    private void initButtons() {

        // Representatives button
        findViewById(R.id.representatives_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RepresentativesActivity.class);
                startActivity(intent);
            }
        });

        // Elections button
        findViewById(R.id.elections_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ViewElectionsActivity.class);
                startActivity(intent);
            }
        });

        // Polling locations button
        findViewById(R.id.poll_locations_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), getString(R.string.loading), Toast.LENGTH_LONG).show();

                // send voot user
                Bundle bundle = new Bundle();
                bundle.putSerializable(VOOT_USER, vootUser);

                Intent intent = new Intent(getApplicationContext(), PollLocationActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        // Reminders button
        findViewById(R.id.reminders_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RemindersActivity.class);
                startActivity(intent);
            }
        });

    }

    // inflate the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;

    }

    // Sets up the action bar menu actions (logout)
     // item is the item that was selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_sign_out:
                // user chose to sign out
                launchSignOutRequest();
                return true;
            default:
                // user access not recognized
                Log.d("MenuItemError", String.format("Item %s not recognized",item.toString()));
                return super.onOptionsItemSelected(item);

        }
    }

    // sign user out of Voot
    private void launchSignOutRequest() {
        FirebaseAuth.getInstance().signOut();
        recreate();
    }

}
