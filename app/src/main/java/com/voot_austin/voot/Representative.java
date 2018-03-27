package com.voot_austin.voot;

import java.util.HashMap;

/**
 * Created by megan on 3/20/18.
 */

public class Representative {
    public String name;
    public String address;
    public String office;
    public String party;
    public String phoneNumber;
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
