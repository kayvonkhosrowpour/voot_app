package com.voot_austin.voot;

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

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by megan on 3/26/18.
 */

public class ViewRepresentativesActivity extends FragmentActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Representative> repData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_rep_list); // assign XML file

        // Instantiate Recycler View
        recyclerView = findViewById(R.id.representatives_recycler_view);

        // improves performance because changes
        // in content do not change layout size
        recyclerView.setHasFixedSize(true);

        // Use a linear layout manager for view
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Grab Representative Data from last intent
        repData = ((List<Representative>) getIntent().getExtras().getSerializable("representatives"));

        // Set Adapter for View
        adapter = new RepresentativeAdapter(repData);
        recyclerView.setAdapter(adapter);
    }

    private class RepresentativeAdapter extends RecyclerView.Adapter<RepresentativeAdapter.RepViewHolder> {

        private List<Representative> repDataset;

        public class RepViewHolder extends RecyclerView.ViewHolder {
            CardView repCard;
            TextView name;
            TextView office;
            ImageView repPic;

            public RepViewHolder (View repView) {
                super(repView);
                repCard = repView.findViewById(R.id.repCard);
                name = repView.findViewById(R.id.repName);
                office = repView.findViewById(R.id.repPos);
                repPic = repView.findViewById(R.id.repPhoto);
            }
        }

        public RepresentativeAdapter (List<Representative> representatives) {
            this.repDataset = representatives;
        }

        @Override
        public int getItemCount( ) {
            return repDataset.size();
        }

        @Override
        public RepViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rep_card, viewGroup, false);
            RepViewHolder repView = new RepViewHolder(view);
            return repView;
        }

        @Override
        public void onBindViewHolder (RepViewHolder repViewHolder, int i) {
            repViewHolder.name.setText(repDataset.get(i).getName());

            if (repDataset.get(i).getEmail() != null) {
                repViewHolder.office.setText(repDataset.get(i).getEmail());
            }
            else if (repDataset.get(i).getPhoneNumber() != null) {
                repViewHolder.office.setText(repDataset.get(i).getPhoneNumber());
            }
            else {
                repViewHolder.office.setText(repDataset.get(i).getParty());
            }
            if (repDataset.get(i).getPhotoURL() == null) {
                repViewHolder.repPic.setImageResource(R.drawable.default_portrait);
            }
            else {
                Picasso.with(getApplicationContext()).load(repDataset.get(i).getPhotoURL()).into(repViewHolder.repPic);
            }
            repViewHolder.repPic.setImageResource(R.drawable.default_portrait);
        }

        @Override
        public void onAttachedToRecyclerView (RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }
    }
}
