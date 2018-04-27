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
    public final static String firstname_ref = "firstname";

    @SerializedName("lastname")
    @Expose
    public String lastname;
    public final static String  lastname_ref = "lastname";

    @SerializedName("email")
    @Expose
    public String email;
    public final static String email_ref = "email";

    @SerializedName("street")
    @Expose
    public String street;
    public final static String street_ref = "street";

    @SerializedName("city")
    @Expose
    public String city;
    public final static String city_ref = "city";

    @SerializedName("county")
    @Expose
    public String county;
    public final static String county_ref = "county";

    @SerializedName("state")
    @Expose
    public String state;
    public final static String state_ref = "state";

    @SerializedName("zipcode")
    @Expose
    public String zipcode;
    public final static String zipcode_ref = "zipcode";

    @SerializedName("electionReminders")
    @Expose
    public Boolean electionReminders;
    public final static String electionReminders_ref = "electionReminders";

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

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s", firstname, lastname, email, street, city, street, zipcode, county, electionReminders.toString());    }
}
