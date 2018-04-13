package com.voot_austin.voot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private boolean DEBUG = true;

    // UI Elements
    private TextView userGreeting;
    private TextView location;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getUIElements();

        if (DEBUG) {
            FirebaseAuth.getInstance().signOut();
        }

        // connect to firebase
        firebaseAuth = FirebaseAuth.getInstance();

        // redirect to log-in screen or fill UI with user info
        establishLogin();

        // build UI for navigation TODO: change this to something pretty
        initButtons();
    }

    // get the UI elements from XML
    private void getUIElements() {
        this.userGreeting = findViewById(R.id.user_greeting);
        this.location = findViewById(R.id.location);
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
            Toast.makeText(this, String.format("Welcome, %s!", currentUser.getEmail()), Toast.LENGTH_LONG).show();
            // get firebase user
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            if (user != null) {
                // get reference to table to store user information
                String userEntry = String.format("%s/%s", DatabaseRefs.USERS_TABLE, user.getUid());
                DatabaseReference userEntryRef = FirebaseDatabase.getInstance().getReference(userEntry);

                // TODO: attach this reference with the UI elements name, location
                userEntryRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        VootUser vootUser = dataSnapshot.getValue(VootUser.class);

                        if (vootUser != null) {
                            userGreeting.setText(String.format("Hello, %s", vootUser.firstname));
                            location.setText(String.format("%s, %s, %s, %s",
                                    vootUser.street, vootUser.city, vootUser.state, vootUser.zipcode));
                        } else {
                            throw new NullPointerException("Voot user was found to be null!");
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        throw new IllegalStateException("In Main Activity, could not retrieve user data");
                    }
                });

            } else {
                throw new NullPointerException("User was null in signup fragment!");
            }
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
                Intent intent = new Intent(getApplicationContext(), ElectionsActivity.class);
                startActivity(intent);
            }
        });

        // Polling locations button
        findViewById(R.id.poll_locations_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PollLocationActivity.class);
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

}
