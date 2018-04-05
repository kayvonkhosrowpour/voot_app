package com.voot_austin.voot;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
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
import java.util.*;

import java.net.HttpURLConnection;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;



public class ElectionsActivity extends AppCompatActivity {


    JSONObject jsonContests;
    JSONArray jcontests;
    String fOffice;
    //String fElectDate;
    List<String> stringContests = new ArrayList<String>();
    Integer numOfContests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elections);

        TextView textView = (TextView) findViewById(R.id.text1);
        textView.setMovementMethod(new ScrollingMovementMethod());

        getSupportActionBar().setTitle("Upcoming Elections");

        String requestURL = "https://www.googleapis.com/civicinfo/v2/voterinfo?key=AIzaSyDavSOAQc_B7Gaaj8cnL6EmPG2g9vgwlVU&address=4600%20Elmont%20Dr.%20Austin%20TX&electionId=2000";

        try{
            String elections = new GetUrlContentTask().execute(requestURL).get();
            extractVars(elections);
            updateVars();
        }
        catch(Exception e) {
            //nothing lol...
            updateVarsError();
        }
    }

    public void extractVars(String data) {
        try {
            jsonContests = new JSONObject(data);
            jcontests = jsonContests.getJSONArray("contests");
            fOffice = jcontests.getJSONObject(0).get("office").toString();
            //fElectDate = jelections.getJSONObject(0).get("electionDay").toString();

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
            //nothing lol
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
        String longString = "";

        int i;

        for(i = 0; i < stringContests.size(); i++) {
            longString += stringContests.get(i);
            longString += "\n\n";
        }

        String longerString = "Number of elections: " + numOfContests + "\n\n" + longString;

        ((TextView) findViewById(R.id.text1)).setText(longerString);
    }

    public void updateVarsError() {
        ((TextView) findViewById(R.id.text1)).setText("Error 404");
    }

}
