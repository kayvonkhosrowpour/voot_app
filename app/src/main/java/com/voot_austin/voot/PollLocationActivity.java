package com.voot_austin.voot;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PollLocationActivity extends AppCompatActivity implements OnMapReadyCallback {


    JSONObject jsonResp;
    JSONArray jsonPoll;
    JSONObject jsonAdd;
    JSONObject jsonAddd;
    ArrayList<String> resp = new ArrayList<>();

    ArrayList<String> resp2 = new ArrayList<>();
    JSONObject response;
    JSONArray results;
    JSONObject geometry;
    JSONObject location;
    String respLat;
    String respLong;

    double lati;
    double longi;
    String addSnippet = "";

    private FirebaseAuth firebaseAuth;

    private VootUser vootUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll_location);

        FirebaseApp.initializeApp(getApplicationContext());

        firebaseAuth = FirebaseAuth.getInstance();

        vootUser = VootUserFetcher.loadSharedPreferences(this);

        createMarker();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Africa.
     */
    @Override
    public void onMapReady(GoogleMap map) {
        LatLng point = new LatLng(lati, longi);
        map.addMarker(new MarkerOptions().position(point).title(resp.get(0)).snippet(addSnippet));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 14));
    }

    public void extractAddVars(String data) {

        try {
            jsonResp = new JSONObject(data);
            jsonPoll = jsonResp.getJSONArray("pollingLocations");
            jsonAdd = jsonPoll.getJSONObject(0);
            jsonAddd = jsonAdd.getJSONObject("address");
            resp.add(jsonAddd.getString("locationName"));
            String line1 = jsonAddd.getString("line1");
            String[] l1 = line1.split(" ");
            int i;
            for(i = 0; i < l1.length; i++) {
                resp.add(l1[i]);
            }
            String city = jsonAddd.getString("city");
            String[] cityArray = city.split(" ");
            for(i = 0; i < cityArray.length; i++) {
                resp.add(cityArray[i]);
            }

            resp.add(jsonAddd.getString("state"));
            resp.add(jsonAddd.getString("zip"));
        }
        catch(Exception e){
            //nothing
        }
    }

    public void extractPollVars(String data) {
        try {
            response = new JSONObject(data);
            results = response.getJSONArray("results");
            geometry = results.getJSONObject(0).getJSONObject("geometry");
            location = geometry.getJSONObject("location");
            respLat = location.getString("lat");
            respLong = location.getString("lng");
            resp2.add(respLat);
            resp2.add(respLong);

        }
        catch(Exception e){
            //nothing
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
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return content;
        }
    }

    private class GetUrlContentTask2 extends AsyncTask<String, Integer, String> {
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
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return content;
        }
    }

    private void createMarker() {
        String RU2 = "";
        respLat = "";
        respLong = "";
        addSnippet = "";
        resp.clear();


        String[] strSplit = vootUser.street.split(" ");
        String[] ctySplit = vootUser.city.split(" ");

        int i;
        for(i = 0; i < strSplit.length; i++) {
            RU2 = RU2 + strSplit[i];
            RU2 = RU2 + "%20";
        }
        for(i = 0; i < ctySplit.length; i++) {
            RU2 = RU2 + ctySplit[i];
            RU2 = RU2 + "%20";
        }
        RU2 = RU2 + vootUser.state;
        RU2 = RU2 + "%20";
        RU2 = RU2 + vootUser.zipcode;

        String RU1 = "https://www.googleapis.com/civicinfo/v2/voterinfo?key=AIzaSyDavSOAQc_B7Gaaj8cnL6EmPG2g9vgwlVU&address=";
        //"4600%20Elmont%20Dr.%20Austin%20TX"
        String RU3 = "&electionId=2000";

        String requestURL = RU1 + RU2 + RU3;
        System.out.println(requestURL);

        try{
            String response = new GetUrlContentTask().execute(requestURL).get();
            extractAddVars(response);
        }
        catch(Exception e) {
            //nothing
        }

        //use arraylist address to call make geocoding call and make marker

        String call1 = "https://maps.googleapis.com/maps/api/geocode/json?address=";
        String call2 = "";
        String call3 = "&key=AIzaSyADaSJfPtEY9UeK2I_TsVSk9OEvDOx9iig";

        for(i = 1; i < resp.size(); i++) {
            call2 = call2 + resp.get(i);
            if(i < resp.size() - 1) {
                call2 += "+";
            }

        }

        for(i = 1; i < resp.size(); i++) {
            addSnippet += resp.get(i);
            if(i < resp.size() - 1) {
                addSnippet += " ";
            }
        }

        String requestURL2 = call1 + call2 + call3;

        try{
            String response2 = new GetUrlContentTask2().execute(requestURL2).get();
            extractPollVars(response2);
        }
        catch(Exception e) {
            //nothing
        }

        //map.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));

        lati = Double.parseDouble(resp2.get(0));
        longi = Double.parseDouble(resp2.get(1));
    }

}