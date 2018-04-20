package com.voot_austin.voot;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class RepParcel implements Parcelable {


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }

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

    @SerializedName("email")
    @Expose
    public String email;

    @SerializedName("website")
    @Expose
    public String website;

    @Expose
    public HashMap<String, String> channels; // Social Media Links, ex: Twitter -> @myHandle

    public RepParcel() {}

    public RepParcel(android.os.Parcel in) {
        name = in.readString();
        address = in.readString();
        office = in.readString();
        party = in.readString();
        phoneNumber = in.readString();
        photoURL = in.readString();
        email = in.readString();
        website = in.readString();
    }

    public static final Creator<RepParcel> CREATOR = new Creator<RepParcel>() {
        @Override
        public RepParcel createFromParcel(android.os.Parcel in) {
            return new RepParcel(in);
        }

        @Override
        public RepParcel[] newArray(int size) {
            return new RepParcel[size];
        }
    };

    public void addChannel(String channelName, String handle) {
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

    public HashMap<String, String> getChannels() {
        return channels;
    }

    public void setChannels(HashMap<String, String> channels) {
        this.channels = channels;
    }
}
