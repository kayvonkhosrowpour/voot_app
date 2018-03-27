package com.voot_austin.voot;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by megan on 3/20/18.
 */

public class Representative implements Serializable {

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("address")
    @Expose
    public String address;

    @SerializedName("office")
    @Expose
    public String office;

    @SerializedName("party")
    @Expose
    public String party;

    @SerializedName("phoneNumber")
    @Expose
    public String phoneNumber;

    @SerializedName("photoURL")
    @Expose
    public String photoURL;



    public String email;
    public String website;
    public HashMap<String, String> channels; // Social Media Links, ex: Twitter -> @myHandle

    public Representative () {
        channels = new HashMap<>();
    }

    public void addChannel (String channelName, String handle) {
        channels.put(channelName, handle);
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getPhoneNumber() {

        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getParty() {

        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getOffice() {

        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
