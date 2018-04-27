package com.voot_austin.voot;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity (tableName = "user")
@IgnoreExtraProperties
public class VootUser implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "firstname")
    @SerializedName("firstname")
    @Expose
    public String firstname;

    @ColumnInfo(name = "lastname")
    @SerializedName("lastname")
    @Expose
    public String lastname;

    @ColumnInfo(name = "email")
    @SerializedName("email")
    @Expose
    public String email;

    @ColumnInfo(name = "street")
    @SerializedName("street")
    @Expose
    public String street;

    @ColumnInfo(name = "city")
    @SerializedName("city")
    @Expose
    public String city;

    @ColumnInfo(name = "county")
    @SerializedName("county")
    @Expose
    public String county;

    @ColumnInfo(name = "state")
    @SerializedName("state")
    @Expose
    public String state;

    @ColumnInfo(name = "zipcode")
    @SerializedName("zipcode")
    @Expose
    public String zipcode;

    @ColumnInfo(name = "electionReminders")
    @SerializedName("electionReminders")
    @Expose
    public Boolean electionReminders;

    public VootUser() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    @Ignore
    public VootUser(String firstname, String lastname, String email, String street, String city, String county, String state, String zipcode, Boolean electionReminders) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.street = street;
        this.city = city;
        this.county = county;
        this.state = state;
        this.zipcode = zipcode;
        this.electionReminders = electionReminders;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public Boolean getElectionReminders() {
        return electionReminders;
    }

    public void setElectionReminders(Boolean electionReminders) {
        this.electionReminders = electionReminders;
    }
}
