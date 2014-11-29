package com.example.cackharot.geosnap.lib;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Base64;

import com.example.cackharot.geosnap.LoginActivity;
import com.example.cackharot.geosnap.model.User;

import org.bson.types.ObjectId;

public class UserSessionManager {
    SharedPreferences pref;
    Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared pref file name
    private static final String PREFER_NAME = "GeoSnapPref";

    // All Shared Preferences Keys
    private static final String IS_USER_LOGIN = "IsUserLoggedIn";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    private static final String KEY_API_KEY = "api_key";

    // Constructor
    public UserSessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    //Create login session
    public void createUserLoginSession(User user) {
        editor.putBoolean(IS_USER_LOGIN, true);
        editor.putString(KEY_NAME, user.name);
        editor.putString(KEY_EMAIL, user.email);
        editor.commit();
    }

    public void setApiKey(String api_key) {
        api_key = Base64.encodeToString(api_key.getBytes(), Base64.DEFAULT);
        editor.putString(KEY_API_KEY, api_key);
        editor.commit();
    }

    public String getApiKey() {
        return pref.getString(KEY_API_KEY, null);
    }

    /**
     * Check login method will check user login status
     * If false it will redirect user to login page
     * Else do anything
     */
    public boolean checkLogin() {
        // Check login status
        if (!this.isUserLoggedIn()) {
            Intent i = new Intent(_context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
            return true;
        }
        return false;
    }


    /**
     * Get stored session data
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        user.put(KEY_API_KEY, pref.getString(KEY_API_KEY, null));
        return user;
    }

    /**
     * Clear session details
     */
    public void logoutUser() {
        // Clearing all user data from Shared Preferences
        clear();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(intent);
    }

    public void clear() {
        editor.clear();
        editor.commit();
    }


    // Check for login
    public boolean isUserLoggedIn() {
        return pref.getBoolean(IS_USER_LOGIN, false);
    }

    public void setDefaultsValues(ObjectId distributor, ObjectId district, String center, String dealer) {
        editor.putString("default_distributor", distributor.toString());
        editor.putString("default_district", district.toString());
        editor.putString("default_center", center);
        editor.putString("default_dealer", dealer);
        editor.commit();
    }

    public String[] getDefaultsValues() {
        String[] args = new String[4];
        args[0] = pref.getString("default_distributor", null);
        args[1] = pref.getString("default_district", null);
        args[2] = pref.getString("default_center", null);
        args[3] = pref.getString("default_dealer", null);
        return args;
    }

    public ObjectId getSelectedDistributor() {
        String default_distributor = pref.getString("default_distributor", null);
        return default_distributor == null ? new ObjectId() : new ObjectId(default_distributor);
    }

    public ObjectId getSelectedDistrict() {
        String default_district = pref.getString("default_district", null);
        return default_district == null ? new ObjectId() : new ObjectId(default_district);
    }

    public String getSelectedConsumptionCenter() {
        String default_center = pref.getString("default_center", null);
        return default_center;
    }

    public String getSelectedDealer() {
        String default_dealer = pref.getString("default_dealer", null);
        return default_dealer;
    }
}