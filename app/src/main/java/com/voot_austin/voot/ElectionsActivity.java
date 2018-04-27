package com.voot_austin.voot;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import java.io.Serializable;
import java.lang.Object;
import java.util.AbstractMap;
import com.google.api.client.util.GenericData;
import com.google.api.client.json.GenericJson;
import com.google.api.services.civicinfo.model.ElectionsQueryRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import java.net.HttpURLConnection;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;



public class ElectionsActivity extends AppCompatActivity {

    JSONObject jsonContests;
    JSONArray jcontests;
    List<String> stringContests = new ArrayList<String>();
    Integer numOfContests;

    String street;
    String city;
    String state;
    String zipcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elections);

        //retrieveFirebaseEntries();
        String RU2 = "";

        /* String[] stArr = street.split(" ");
        String[] cityArr = city.split(" ");

        int i;

        for(i = 0; i < stArr.length; i++) {
            RU2 += stArr[i];
            RU2 += "%20";
        }

        for(i = 0; i < cityArr.length; i++) {
            RU2 += cityArr[i];
            RU2 += "%20";
        }

        RU2 += state; */

        String RU1 = "https://www.googleapis.com/civicinfo/v2/voterinfo?key=AIzaSyDavSOAQc_B7Gaaj8cnL6EmPG2g9vgwlVU&address=";
        RU2 =        "4600%20Elmont%20Dr.%20Austin%20TX";
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
        ArrayList<Election> arrlElec = stringToElec(stringContests);
        try {
            Intent viewElec = new Intent(getApplicationContext(), ViewElectionsActivity.class);
            viewElec.putExtra("elections", (Serializable) arrlElec);
            startActivity(viewElec);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        ((TextView) findViewById(R.id.text1)).setText("Error 404");
    }


    public void retrieveFirebaseEntries() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            finish();
        } else {
            // get reference to table to store user information
            String userEntry = String.format("%s/%s", DatabaseRefs.USERS_TABLE, user.getUid());
            DatabaseReference userEntryRef = FirebaseDatabase.getInstance().getReference(userEntry);

            userEntryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    VootUser vootUser = dataSnapshot.getValue(VootUser.class);

                    if (vootUser != null) {
                        street = vootUser.street;
                        city = vootUser.city;
                        state = vootUser.state;
                        zipcode = vootUser.zipcode;
                    } else {
                        throw new NullPointerException("Voot user was found to be null!");
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    throw new IllegalStateException("In Poll Activity, could not retrieve user data");
                }
            });

            userEntryRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    VootUser vootUser = dataSnapshot.getValue(VootUser.class);
                    if(vootUser != null) {
                        street = vootUser.street;
                        city = vootUser.city;
                        state = vootUser.state;
                        zipcode = vootUser.zipcode;
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
