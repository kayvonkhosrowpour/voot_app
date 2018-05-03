package com.voot_austin.voot;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

/**
 * Created by 97ale on 5/3/2018.
 */

public class Candidate {
    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("party")
    @Expose
    public String party;



    public String getParty() {

        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }



    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

}
