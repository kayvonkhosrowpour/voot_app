package com.voot_austin.voot;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

public class RepresentativesActivity extends FragmentActivity {

    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_representatives);

        // get a FragmentManager to access and manage fragments
        fragmentManager = getSupportFragmentManager();

        // TODO: exchange and create the appropriate fragments
            // TODO: should have one fragment to list all representatives
            // TODO: should have one fragment that displays rep/contact info

    }

}
