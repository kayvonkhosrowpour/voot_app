package com.voot_austin.voot;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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

public class PollLocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll_location);

        getSupportActionBar().setTitle("Poll Locations");

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Africa.
     */
    @Override
    public void onMapReady(GoogleMap map) {
        //map.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        String requestURL = "https://www.googleapis.com/civicinfo/v2/voterinfo?key=AIzaSyDavSOAQc_B7Gaaj8cnL6EmPG2g9vgwlVU&address=4600%20Elmont%20Dr.%20Austin%20TX&electionId=2000";
        ArrayList<String> address = new ArrayList<>();

        try{
            String response = new GetUrlContentTask().execute(requestURL).get();
            address = extractAddVars(response);
            updateVars();
        }
        catch(Exception e) {
            //nothing lol...
            updateVarsError();
        }

        //use arraylist address to call make geocoding call and make marker

        String call1 = "https://maps.googleapis.com/maps/api/geocode/json?address=";
        String call2 = "";
        String call3 = "&key=AIzaSyADaSJfPtEY9UeK2I_TsVSk9OEvDOx9iig";

        String inputTemp = "test test test";

        String[] split = inputTemp.split(" ");

        int i;
        for(i = 0; i < split.length; i++) {
            call2 = call2 + split[i];
            call2 = call2 + "+";
        }

        requestURL = call1 + call2 + call3;

        try{
            String response = new GetUrlContentTask().execute(requestURL).get();
            ArrayList<String> pollLocations = extractPollVars(response);
            updateVars();
        }
        catch(Exception e) {
            //nothing lol...
            updateVarsError();
        }



    }

    public ArrayList<String> extractAddVars(String data) {
        ArrayList<String> resp = new ArrayList<>();

        try {

        }
        catch(Exception e){
            //nothing lol
            updateVarsError();
        }

        return resp;
    }

    public ArrayList<String> extractPollVars(String data) {
        ArrayList<String> resp = new ArrayList<>();

        try {
            JSONObject jsonResp = new JSONObject(data);
            JSONArray jsonPoll = jsonResp.getJSONArray("pollingLocations");
            JSONObject jsonAdd = jsonPoll.getJSONObject(0);
            resp.add(jsonAdd.getString("locationName"));
            resp.add(jsonAdd.getString("line1"));
            resp.add(jsonAdd.getString("city"));
            resp.add(jsonAdd.getString("state"));
            resp.add(jsonAdd.getString("zip"));
        }
        catch(Exception e){
            //nothing lol
            updateVarsError();
        }

        return resp;
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

    public void updateVars() {

    }

    public void updateVarsError() {
        ((TextView) findViewById(R.id.text1)).setText("Error 404");
    }

}
