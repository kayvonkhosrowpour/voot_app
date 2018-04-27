package com.voot_austin.voot;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.security.Security;
import java.util.List;

import static android.Manifest.permission.CALL_PHONE;

public class ContactRepActivity extends AppCompatActivity {

    // data
    Representative representative;

    // GUI variables
    private TextView repName, userLocation, repInfo;
    private ImageView portrait;
    private Button callButton, emailButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_rep);

        // get data
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            representative = (Representative) bundle.getSerializable(ViewRepresentativesActivity.REP);
        } else {
            Toast.makeText(getApplicationContext(), "Trouble fetching data. Please try again later.", Toast.LENGTH_SHORT).show();
            finish();
        }

        // get GUI
        repName = findViewById(R.id.rep_name);
        portrait = findViewById(R.id.portrait);
        userLocation = findViewById(R.id.location);
        repInfo = findViewById(R.id.rep_info);
        callButton = findViewById(R.id.call_button);
        emailButton = findViewById(R.id.email_button);

        // connect to firebase and update GUI
        setUserLocation();
        updateGUI();

    }

    private void setUserLocation() {
        // get firebase user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            // get reference to table to store user information
            String userEntry = String.format("%s/%s", DatabaseRefs.USERS_TABLE, user.getUid());
            DatabaseReference userEntryRef = FirebaseDatabase.getInstance().getReference(userEntry);

            userEntryRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    VootUser vootUser = dataSnapshot.getValue(VootUser.class);

                    if (vootUser != null) {
                        userLocation.setText(String.format("%s, %s %s",
                                vootUser.city, vootUser.state, vootUser.zipcode));
                    } else {
                        throw new NullPointerException("Voot user was found to be null!");
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    throw new IllegalStateException("In Main Activity, could not retrieve user data");
                }
            });

        } else {
            throw new NullPointerException("User was null in ContactRepActivity fragment!");
        }
    }

    // assuming GUI variables are non-null, sets up GUI with representative information
    private void updateGUI() {

        // set name
        char partyAbbreviation = representative.getParty().toUpperCase().charAt(0);
        if (partyAbbreviation != 'U' ) // unknown party
            repName.setText(String.format("%s (%c)", representative.getName(), partyAbbreviation));
        else
            repName.setText(String.format("%s", representative.getName()));

        // set portrait
        String portraitURL = representative.getPhotoURL();
        if (portraitURL == null) {
            portrait.setImageResource(R.drawable.default_portrait);
        } else {
            Picasso.with(getApplicationContext()).load(representative.getPhotoURL()).into(portrait);
        }

        // set rep info
        setRepInfo();

        // set call button
        final Activity ref = this;
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                String number = representative.getPhoneNumber().replaceAll("[^\\d.]", "");
                Log.d("NUMBER", number);
                callIntent.setData(Uri.parse(String.format("tel:%s", number)));
                try {
                    ActivityCompat.requestPermissions(ref,new String[]{CALL_PHONE},1);
                    startActivity(callIntent);
                } catch (SecurityException se) {
                    Toast.makeText(getApplicationContext(), "You have not enabled call permissions for Voot", Toast.LENGTH_SHORT).show();
                    se.printStackTrace();
                }
            }
        });

        // set email button
        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto",representative.getEmail(), null));
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subject));
                intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.email_body));
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(Intent.createChooser(intent, "Choose an Email client :"));
            }
        });

    }

    // assuming the represenative and GUI are non null
    private void setRepInfo() {

        StringBuilder builder = new StringBuilder();
        builder.append(representative.getParty()).append('\n');
        builder.append(representative.getOffice()).append('\n');
        builder.append(representative.getWebsite()).append('\n');

        repInfo.setText(builder.toString());

    }

}
