package com.voot_austin.voot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class GetUserActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    public boolean isRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_user);


        FirebaseApp.initializeApp(this);

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            Log.d("DEBUG", "LOADING LOGIN FRAGMENT\n\n\n");

            // Create a new Fragment to be placed in the activity layout
            LoginFragment loginFragment = new LoginFragment();

            // OPTIONAL:
            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            // loginFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, loginFragment, "root")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
            isRoot = true;

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            finish();
        }
    }

    @Override
    public void onBackPressed(){

        int count = getSupportFragmentManager().getBackStackEntryCount();
        //Toast.makeText(this, Integer.toString(count), Toast.LENGTH_SHORT).show();

        // control how fragments are managed when pressing "back"
        // note that if on login screen, do nothing
        // if on signup page, then exit out of sign up and return to login screen
        if (count == 1) {
            getSupportFragmentManager().popBackStackImmediate("signup",FragmentManager.POP_BACK_STACK_INCLUSIVE);
            super.onBackPressed();
        } else if (count > 1) {
            // if deeper, come back out
            super.onBackPressed();
        }

    }

}
