package com.voot_austin.voot;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.Object;
import java.util.AbstractMap;
import com.google.api.client.util.GenericData;
import com.google.api.client.json.GenericJson;
import com.google.api.services.civicinfo.model.ElectionsQueryRequest;

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


    JSONObject jsonElections;
    JSONArray jelections;
    String fElectName;
    String fElectDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elections);

        getSupportActionBar().setTitle("Upcoming Elections");

        String requestURL = "https://www.googleapis.com/civicinfo/v2/elections?key=AIzaSyDavSOAQc_B7Gaaj8cnL6EmPG2g9vgwlVU";

        try{
            String elections = new GetUrlContentTask().execute(requestURL).get();
            extractVars(elections);
            updateVars();
        }
        catch(Exception e) {
            //nothing lol...
            String elections = "fail";
        }
    }

    public void extractVars(String data) {
        try {
            jsonElections = new JSONObject(data);
            jelections = jsonElections.getJSONArray("elections");
            fElectName = jelections.getJSONObject(0).get("name").toString();
            fElectDate = jelections.getJSONObject(0).get("electionDay").toString();
        }
        catch(Exception e){
            //nothing lol
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

    public void updateVars() {
        //print it on screen
        ((TextView) findViewById(R.id.text1)).setText(fElectName + "\n" + fElectDate);
    }

}
