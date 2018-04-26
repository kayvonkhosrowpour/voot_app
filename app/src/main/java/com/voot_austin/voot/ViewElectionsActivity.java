package com.voot_austin.voot;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import java.util.List;



public class ViewElectionsActivity extends FragmentActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Election> elecData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_elec_list); // assign XML file

        // Instantiate Recycler View
        recyclerView = findViewById(R.id.elections_recycler_view);

        // improves performance because changes
        // in content do not change layout size
        recyclerView.setHasFixedSize(true);

        // Use a linear layout manager for view
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        elecData = ((List<Election>) getIntent().getExtras().getSerializable("elections"));

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
}