package com.voot_austin.voot;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class VootUser {

    public String firstname, lastname;
    public String email;
    public String street;
    public String city;
    public String county;
    public String state;
    public String zipcode;
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

}
