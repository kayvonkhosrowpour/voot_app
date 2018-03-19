package com.voot_austin.voot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.lang.Object;
import java.util.AbstractMap;
import com.google.api.client.util.GenericData;
import com.google.api.client.json.GenericJson;
import com.google.api.services.civicinfo.model.ElectionsQueryRequest;



public class ElectionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elections);

        getSupportActionBar().setTitle("Upcoming Elections");
    }




}
