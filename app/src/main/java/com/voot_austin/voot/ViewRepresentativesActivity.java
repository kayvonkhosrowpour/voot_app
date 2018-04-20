package com.voot_austin.voot;


import android.content.Intent;
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
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

// Author of base code: Megan Cooper
// Additional edits made by: Kayvon Khosrowpour

public class ViewRepresentativesActivity extends AppCompatActivity {

    // references for intents
    public static final String REP = "REP";

    // GUI
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    // data
    private List<Representative> repData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // inflate view
        setContentView(R.layout.fragment_rep_list);

        Toast.makeText(getApplicationContext(), "OnCreateCalled", Toast.LENGTH_LONG).show();

        // inflate Recycler View
        recyclerView = findViewById(R.id.representatives_recycler_view);

        // improves performance because changes
        // in content do not change layout size
        recyclerView.setHasFixedSize(true);

        // Use a linear layout manager for view
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Grab Representative Data from last intent
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && repData == null)
            repData = ((List<Representative>) bundle.getSerializable("representatives"));

        if (repData == null) {
            throw new NullPointerException("Representative Data is null!");
        }

        // Set Adapter for View
        adapter = new RepresentativeAdapter(repData);
        recyclerView.setAdapter(adapter);
    }

    private class RepresentativeAdapter extends RecyclerView.Adapter<RepresentativeAdapter.RepViewHolder> {

        private List<Representative> repDataset;

        RepresentativeAdapter (List<Representative> representatives) {
            this.repDataset = representatives;
        }

        @Override
        public int getItemCount( ) {
            return repDataset.size();
        }

        @Override
        public RepViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rep_card, viewGroup, false);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // open ViewIndividualRepActivity
                    Intent intent = new Intent(getApplicationContext(), ContactRepActivity.class);
                    int itemPosition = recyclerView.getChildLayoutPosition(view);
                    intent.putExtra(ViewRepresentativesActivity.REP, repDataset.get(itemPosition));
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                }
            });

            return new RepViewHolder(view);
        }

        @Override
        public void onBindViewHolder (RepViewHolder repViewHolder, int i) {
            repViewHolder.name.setText(repDataset.get(i).getName());

            // Set the text information for the representative card
            repViewHolder.office.setText(repDataset.get(i).getOffice());
            repViewHolder.party.setText(repDataset.get(i).getParty());

            // Set the photo for the representative card
            if (repDataset.get(i).getPhotoURL() == null) {
                repViewHolder.repPic.setImageResource(R.drawable.default_portrait);
            }
            else {
                Picasso.with(getApplicationContext()).load(repDataset.get(i).getPhotoURL()).into(repViewHolder.repPic);
            }
            repViewHolder.repPic.setImageResource(R.drawable.default_portrait);
        }

        class RepViewHolder extends RecyclerView.ViewHolder {
            CardView repCard;
            TextView name;
            TextView office;
            TextView party;
            ImageView repPic;

            RepViewHolder (View repView) {
                super(repView);
                repCard = repView.findViewById(R.id.repCard);
                name = repView.findViewById(R.id.repName);
                office = repView.findViewById(R.id.repPos);
                repPic = repView.findViewById(R.id.repPhoto);
                party = repView.findViewById(R.id.repParty);
            }
        }

    }

}
