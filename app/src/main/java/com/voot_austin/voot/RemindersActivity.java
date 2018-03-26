package com.voot_austin.voot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class RemindersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);

        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(getString(R.string.reminders));
    }
}
