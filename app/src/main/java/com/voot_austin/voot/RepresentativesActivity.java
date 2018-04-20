package com.voot_austin.voot;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class RepresentativesActivity extends AppCompatActivity {

    FragmentManager fragmentManager;

    EditText userAddress, userCityState, userZipCode;
    Button sendRequest;

    String apiKey = "AIzaSyDavSOAQc_B7Gaaj8cnL6EmPG2g9vgwlVU";
    String address = ""; // "2900%20Hawks%20Swoop%20Trail%20Pflugerville,%20TX%2078660";
    String requestURL = String.format("https://www.googleapis.com/civicinfo/v2/representatives?key=%s&address=%s", apiKey, address);


    // API Key : AIzaSyDavSOAQc_B7Gaaj8cnL6EmPG2g9vgwlVU

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_representatives);

        // initialize Firebase instance for this activity
        FirebaseApp.initializeApp(getApplicationContext());

        // get a FragmentManager to access and manage fragments
        fragmentManager = getSupportFragmentManager();

        // Instantiate EditText fields
        userAddress = findViewById(R.id.address);
        sendRequest = findViewById(R.id.rep_btn);
        userCityState = findViewById(R.id.city);
        userZipCode = findViewById(R.id.zipcode);

        retrieveFirebaseEntries();

    }

    // retrieve the address information from the user profile
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
                    VootUser vootUser = dataSnapshot.getValue(VootUser.class);

                    if (vootUser != null) {
                        userAddress.setText(vootUser.street);
                        userCityState.setText(String.format("%s, %s", vootUser.city, vootUser.state));
                        userZipCode.setText(vootUser.zipcode);
                    } else {
                        throw new NullPointerException("Voot user was found to be null!");
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    throw new IllegalStateException("In Main Activity, could not retrieve user data");
                }
            });
        }

    }

    public void sendRequestForRepresentatives(View view) {
        // Grab address from user text view
        address = userAddress.getText().toString() + "%20" + userCityState.getText().toString() + "%20" + userZipCode.getText().toString();
        address = address.replaceAll("\\s+", "%20");

        // Try to send request to Google Civic API
        if (address.equals(null) || address.equals("null") || address.isEmpty()) {
            //show dialog
            Toast.makeText(getApplicationContext(), "Please Enter an Address to See Representatives", Toast.LENGTH_LONG).show();
        }
        else {
            trySendRequest();
        }

        return;
    }

    // Attempts to send a request to the Google
    // Civic API, and if successful, passes the
    // retrieved data to the ViewRepresentativesActivity
    private void trySendRequest() {
        // Attempt to retrieve representatives
        // data from API using given address
        String requestData = null;
        APIRequest reqTask = new APIRequest();

        // Construct API String
        requestURL = String.format("https://www.googleapis.com/civicinfo/v2/representatives?key=%s&address=%s", apiKey, address);

        // Attempt to run the async task that sends the request
        try {
            requestData = reqTask.execute(requestURL).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (requestData.equals(null) || requestData.equals("FAILED")) {
            Log.i("ERROR", "Request to API Failed");
        }
        // On success -> Open the ViewRepresentativesActivity
        // and pass the list of contructed representatives
        else {
            ArrayList<Representative> representatives = pullRepresentativeData(requestData);

            if (representatives != null) {

                Log.e("ERROR_CHECK", "Attempting to load new activity...");

                // Open View Representatives Activity
                // and pass the retrieved data
                Intent viewReps = new Intent(getApplicationContext(), ViewRepresentativesActivity.class);
                viewReps.putExtra("representatives", representatives);
                // Launch ListView of Representatives
                startActivity(viewReps);
            }
        }
    }

    // Uses the user's address and election ID
    // to get their representative information
    // from the Google Civic Information API
    private class APIRequest extends AsyncTask<String, Integer, String> {
        protected String doInBackground(String... urls) {
            // Send a request to Google Civic API
            HttpResponse response = null;
            try {
                for (String url : urls) {

                    Log.i("STATE", "Sending Request..." + url);

                    URL reqURL = new URL(url);
                    HttpURLConnection connection = (HttpURLConnection) reqURL.openConnection();
                    connection.setRequestMethod("GET");

                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String inLine;
                    StringBuilder resp = new StringBuilder();

                    while((inLine = in.readLine()) != null) {
                        resp.append(inLine);
                    }
                    return resp.toString();
                }
            }
            catch (Exception e) {
                Log.e("FAILED", "FAILED: " + e.getLocalizedMessage());
                return "FAILED";
            }
            return null;
        }
    }

    // Takes the JSONObject and property name
    // we are attempting to add. If the JSONObject
    // has this property, we add it to the representative
    // object
    private void setRepProperty(JSONObject obj, Representative rep, String property) throws JSONException {
        // Check first if the JSON object
        // or array actually exists
        if (obj.has(property)) {
            if (property.equals("name")) {
                rep.setName(obj.getString("name"));
            }
            else if (property.equals("party")) {
                rep.setParty(obj.getString("party"));
            }
            else if (property.equals("photoUrl")) {
                rep.setPhotoURL(obj.getString("photoUrl"));
            }
            else if (property.equals("emails")) {
                rep.setEmail(obj.getJSONArray("emails").get(0).toString());
            }
            else if (property.equals("urls")) {
                rep.setWebsite(obj.getJSONArray("urls").get(0).toString());
            }
            else if (property.equals("address")) {
                JSONArray addressData = obj.getJSONArray("address");
                String address = "";

                // Contruct the address
                for (int i = 0; i < addressData.length(); i++) {
                    address += addressData.get(i).toString() + " ";
                }
                rep.setAddress(address);
            }
            else if (property.equals("phones")) {
                rep.setPhoneNumber(obj.getJSONArray("phones").get(0).toString());
            }
            else if (property.equals("channels")) {
                JSONArray channels = obj.getJSONArray("channels");
                for (int j = 0; j < channels.length(); j++) {
                    rep.addChannel(channels.getJSONObject(j).getString("type"), channels.getJSONObject(j).getString("id"));
                }
            }
        }
        else {
            setNullProperty(rep, property);
        }
    }

    // For properties that don't exist this
    // sets an error / informational message
    private void setNullProperty(Representative rep, String property) {
        if (property.equals("name")) {
            rep.setName("No name given");
        }
        else if (property.equals("party")) {
            rep.setParty("Unknown Party");
        }
        else if (property.equals("emails")) {
            rep.setEmail("No email given");
        }
        else if (property.equals("urls")) {
            rep.setWebsite("No website given");
        }
        else if (property.equals("address")) {
            rep.setAddress("No address given");
        }
        else if (property.equals("phones")) {
            rep.setPhoneNumber("No phone number given");
        }
    }

    // Processes the JSON Object returned by
    // the request sent to the Google Civic API.
    // Returns a list of Representative objects to
    // be displayed to the user.
    private ArrayList<Representative> pullRepresentativeData (String response) {

        try {
            ArrayList<Representative> Representatives = new ArrayList<>();
            JSONObject fullResp = new JSONObject(response);
            JSONArray officials = fullResp.getJSONArray("officials");
            JSONArray offices = fullResp.getJSONArray("offices");
            String[] properties = {"name", "address", "party", "phones", "urls", "photoUrl", "channels", "emails"};

            for (int j = offices.length() - 1; j >= 0; j--) {
                String officeName = offices.getJSONObject(j).getString("name");
                JSONArray officialIndices = offices.getJSONObject(j).getJSONArray("officialIndices");



                for (int i = 0; i < officialIndices.length(); i++) {
                    JSONObject official = officials.getJSONObject(officialIndices.getInt(i));

                    Representative rep = new Representative();
                    rep.setOffice(officeName);

                    for (String property : properties) {
                        setRepProperty(official, rep, property);
                    }

                    // Add the representative to our list
                    Representatives.add(rep);
                }
            }


            return Representatives;

        } catch (JSONException e) {
            Log.i("PARSE", "RESPONSE" + response.toString());
            Log.i("PARSE", e.getMessage());
        }

        return null;
    }

}


