package com.voot_austin.voot;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class RepresentativesActivity extends FragmentActivity {

    FragmentManager fragmentManager;

    Button sendRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_representatives);

        // get a FragmentManager to access and manage fragments
        fragmentManager = getSupportFragmentManager();

        sendRequest = findViewById(R.id.rep_btn);

        sendRequest.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Sending Request to Google API", Toast.LENGTH_LONG).show();

                // TODO: Send Request to Google API
            }

        });

        // TODO: exchange and create the appropriate fragments
        // TODO: should have one fragment to list all representatives
        // TODO: should have one fragment that displays rep/contact info

    }



}
