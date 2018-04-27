package com.voot_austin.voot;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@IgnoreExtraProperties
public class VootUser implements Serializable {

    @SerializedName("firstname")
    @Expose
    public String firstname;

    @SerializedName("lastname")
    @Expose
    public String lastname;

    @SerializedName("email")
    @Expose
    public String email;

    @SerializedName("street")
    @Expose
    public String street;

    @SerializedName("city")
    @Expose
    public String city;

    @SerializedName("county")
    @Expose
    public String county;

    @SerializedName("state")
    @Expose
    public String state;

    @SerializedName("zipcode")
    @Expose
    public String zipcode;

    @SerializedName("electionReminders")
    @Expose
    public Boolean electionReminders;

    public VootUser() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

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
