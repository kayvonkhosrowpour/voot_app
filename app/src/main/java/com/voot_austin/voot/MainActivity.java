package com.voot_austin.voot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO: this shouldn't be the first screen, it should be a log-in screen

        initButtons();
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
