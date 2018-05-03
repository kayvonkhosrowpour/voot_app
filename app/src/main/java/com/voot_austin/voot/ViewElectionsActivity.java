package com.voot_austin.voot;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;



public class ViewElectionsActivity extends AppCompatActivity {

    private TextView location;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Election> elecData;

    private VootUser vootUser;

    JSONObject jsonContests;
    JSONArray jcontests;
    List<String> stringContests = new ArrayList<String>();
    Integer numOfContests;
    ArrayList<Election> arrayListElections = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_elec_list); // assign XML file

        // get voot user
        vootUser = VootUserFetcher.loadSharedPreferences(this);


        // setup on change for voot user
        retrieveFirebaseEntries();

        // setup strings for API call
        String RU2 = "";

        String[] stArr = vootUser.street.split(" ");
        String[] cityArr = vootUser.city.split(" ");

        int i;

        for(i = 0; i < stArr.length; i++) {
            RU2 += stArr[i];
            RU2 += "%20";
        }

        for(i = 0; i < cityArr.length; i++) {
            RU2 += cityArr[i];
            RU2 += "%20";
        }

        RU2 += vootUser.state;

        String RU1 = "https://www.googleapis.com/civicinfo/v2/voterinfo?key=AIzaSyDavSOAQc_B7Gaaj8cnL6EmPG2g9vgwlVU&address=";
        String RU3 = "&electionId=2000";

        String requestURL = RU1 + RU2 + RU3;

        try{
            String elections = new GetUrlContentTask().execute(requestURL).get();
            extractVars(elections);
            updateVars();
        }
        catch(Exception e) {
            updateVarsError();
        }



        // Instantiate Recycler View
        recyclerView = findViewById(R.id.elections_recycler_view);
        location = findViewById(R.id.location);

        // set location
        location.setText(String.format(" %s, %s %s", vootUser.city, vootUser.state, vootUser.zipcode));

        // improves performance because changes
        // in content do not change layout size
        recyclerView.setHasFixedSize(true);

        // Use a linear layout manager for view
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        elecData = arrayListElections;

        // Set Adapter for View
        adapter = new ElectionAdapter(elecData);
        recyclerView.setAdapter(adapter);
    }

    private class ElectionAdapter extends RecyclerView.Adapter<ElectionAdapter.ElecViewHolder> {

        private List<Election> elecDataset;

        public class ElecViewHolder extends RecyclerView.ViewHolder {
            CardView elecCard;
            TextView name;

            public ElecViewHolder (View elecView) {
                super(elecView);
                name = elecView.findViewById(R.id.elecName);
            }
        }

        public ElectionAdapter (List<Election> elections) {
            this.elecDataset = elections;
        }

        @Override
        public int getItemCount( ) {
            return elecDataset.size();
        }

        @Override
        public ElecViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.elec_card, viewGroup, false);
            ElecViewHolder ElecView = new ElecViewHolder(view);
            return ElecView;
        }

        @Override
        public void onBindViewHolder (ElecViewHolder ElecViewHolder, int i) {
            ElecViewHolder.name.setText(elecDataset.get(i).getName());
        }

        @Override
        public void onAttachedToRecyclerView (RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }
    }





    public void extractVars(String data) {
        try {
            jsonContests = new JSONObject(data);
            jcontests = jsonContests.getJSONArray("contests");

            int i;
            numOfContests = jcontests.length();
            for(i = 0; i < jcontests.length(); i++) {
                String type = jcontests.getJSONObject(i).get("type").toString();
                if(type.equals("Referendum")) {
                    stringContests.add(jcontests.getJSONObject(i).get("referendumTitle").toString());
                } else {
                    stringContests.add(jcontests.getJSONObject(i).get("office").toString());
                }
            }

        }
        catch(Exception e){
            updateVarsError();
        }
    }

    private class GetUrlContentTask extends AsyncTask<String, Integer, String> {
        protected String doInBackground(String... urls) {
            String content = "";
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inLine;
                StringBuilder response = new StringBuilder();

                while((inLine = in.readLine()) != null) {
                    response.append(inLine);
                }

                return response.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return content;
        }
    }

    public void updateVars() {
        arrayListElections = stringToElec(stringContests);
    }

    private ArrayList<Election> stringToElec(List<String> stringElections) {
        ArrayList<Election> arrlElec = new ArrayList<>();
        for(int i = 0; i < stringElections.size(); i++) {
            Election e = new Election();
            e.setName(stringElections.get(i));
            arrlElec.add(e);
        }
        return arrlElec;
    }

    public void updateVarsError() {
        //In case of error
    }


    public void retrieveFirebaseEntries() {

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
                    VootUser receivedVootUser = dataSnapshot.getValue(VootUser.class);
                    if (receivedVootUser != null) {
                        vootUser = receivedVootUser;
                        VootUserFetcher.saveSharedPreferences(getApplicationContext(), vootUser);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    throw new IllegalStateException("In Poll Activity, could not retrieve user data");
                }
            });

        }

    }


























}