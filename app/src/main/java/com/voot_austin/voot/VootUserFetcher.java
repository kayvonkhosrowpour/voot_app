package com.voot_austin.voot;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.voot_austin.voot.MainActivity.VOOT_USER;

public class VootUserFetcher {

    // load shared preferences, the values from VootUser
    public static VootUser loadSharedPreferences(Context context) {

        VootUser vootUser = new VootUser();

        SharedPreferences prefs = context.getSharedPreferences(VOOT_USER, MODE_PRIVATE);

        vootUser.firstname = prefs.getString(VootUser.firstname_ref, null);
        vootUser.lastname = prefs.getString(VootUser.lastname_ref, null);
        vootUser.email = prefs.getString(VootUser.email_ref, null);
        vootUser.street = prefs.getString(VootUser.street_ref, null);
        vootUser.city = prefs.getString(VootUser.city_ref, null);
        vootUser.county = prefs.getString(VootUser.county_ref, null);
        vootUser.state = prefs.getString(VootUser.state_ref, null);
        vootUser.zipcode = prefs.getString(VootUser.zipcode_ref, null);
        vootUser.electionReminders = prefs.getBoolean(VootUser.electionReminders_ref, false);

        Log.d("POJO", "Loaded vootUser in preferences");
        Log.d("POJO", vootUser.toString());

        return vootUser;
    }

    // save the voot user in shared preferences
    public static void saveSharedPreferences(Context context, VootUser vootUser) {

        SharedPreferences.Editor sharedPref = context.getSharedPreferences(VOOT_USER, MODE_PRIVATE).edit();

        sharedPref.putString(VootUser.firstname_ref,          vootUser.firstname);
        sharedPref.putString(VootUser.lastname_ref,           vootUser.lastname);
        sharedPref.putString(VootUser.email_ref,              vootUser.email);
        sharedPref.putString(VootUser.street_ref,             vootUser.street);
        sharedPref.putString(VootUser.city_ref,               vootUser.city);
        sharedPref.putString(VootUser.county_ref,             vootUser.county);
        sharedPref.putString(VootUser.state_ref,              vootUser.state);
        sharedPref.putString(VootUser.zipcode_ref,            vootUser.zipcode);
        sharedPref.putBoolean(VootUser.electionReminders_ref, vootUser.electionReminders);

        sharedPref.apply();

        Log.d("POJO", "Saved vootUser in preferences");
        Log.d("POJO", vootUser.toString());

    }

}
