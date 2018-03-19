package com.voot_austin.voot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ElectionsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elections);

        getSupportActionBar().setTitle("Upcoming Elections");
    }
}
