package com.example.cleanapplication;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class SharedPrefsData {

    String authenticationTypeTag = "AuthType";
    String tokenPreferenceTag = "LoginToken";
    String isLoginPreferenceTag = "isLogin";
    String cartPreferenceTag = "CartList";
    String pickupIDPreferenceTag = "UserSelectedPickupID";
    String pickupDatePreferenceTag = "UserSelectedPickupDate";
    String pickupTimePreferenceTag = "UserSelectedPickupTime";
    String deliverIDPreferenceTag = "UserSelectedDeliverID";
    String deliverDatePreferenceTag = "UserSelectedDeliverDate";
    String deliverTimePreferenceTag = "UserSelectedDeliverTime";
    String passwordResetTag = "PasswordResetTag";
    String pickupDeliverTag = "PickupDeliverTag";
    public String loginUserToken;
    public String passwordResetType;
    public Boolean isLogIn = false;
    public String authType = "";
    public String pickupID = "";
    public String pickupDate = "";
    public String pickupTime = "";
    public String deliverID = "";
    public String deliverDate = "";
    public String deliverTime = "";
    public Boolean pickDeliver = false;
    Context context;

    public SharedPrefsData(Context context) {
        this.context = context;
    }

    public void setValueInPreference(String token, Boolean isLogin, String authType) {
        SharedPreferences.Editor editor = context.getSharedPreferences(String.valueOf(R.string.app_name), MODE_PRIVATE).edit();
        editor.putString(tokenPreferenceTag, token);
        editor.putBoolean(isLoginPreferenceTag, isLogin);
        editor.putString(authenticationTypeTag, authType);
        editor.apply();
    }

    public void getTokenFromPreference() {
        SharedPreferences prefs = context.getSharedPreferences(String.valueOf(R.string.app_name), MODE_PRIVATE);
        String token = prefs.getString(tokenPreferenceTag, "");
        Boolean loginResult = prefs.getBoolean(isLoginPreferenceTag, false);
        String authentication = prefs.getString(authenticationTypeTag, "");
        loginUserToken = token;
        isLogIn = loginResult;
        authType = authentication;
    }

    public void setCartValueInPrefs(String cartData) {
        SharedPreferences.Editor editor = context.getSharedPreferences(String.valueOf(R.string.app_name), MODE_PRIVATE).edit();
        editor.putString(cartPreferenceTag, cartData);
        editor.apply();
    }

    public String getCartValuesFromPrefs() {
        SharedPreferences prefs = context.getSharedPreferences(String.valueOf(R.string.app_name), MODE_PRIVATE);
        String cartString = prefs.getString(cartPreferenceTag, "");
        return cartString;
    }

    public void setPickupID(String id, String date, String time) {
        SharedPreferences.Editor editor = context.getSharedPreferences(String.valueOf(R.string.app_name), MODE_PRIVATE).edit();
        editor.putString(pickupIDPreferenceTag, id);
        editor.putString(pickupDatePreferenceTag, date);
        editor.putString(pickupTimePreferenceTag, time);
        editor.apply();
    }

    public String getPickupID() {
        SharedPreferences prefs = context.getSharedPreferences(String.valueOf(R.string.app_name), MODE_PRIVATE);
        String id = prefs.getString(pickupIDPreferenceTag, "");
        String date = prefs.getString(pickupDatePreferenceTag, "");
        String time = prefs.getString(pickupTimePreferenceTag, "");
        pickupID = id;
        pickupDate = date;
        pickupTime = time;
        return id;
    }

    public void setDeliverID(String id, String date, String time) {
        SharedPreferences.Editor editor = context.getSharedPreferences(String.valueOf(R.string.app_name), MODE_PRIVATE).edit();
        editor.putString(deliverIDPreferenceTag, id);
        editor.putString(deliverDatePreferenceTag, date);
        editor.putString(deliverTimePreferenceTag, time);
        editor.apply();
    }

    public String getDeliverID() {
        SharedPreferences prefs = context.getSharedPreferences(String.valueOf(R.string.app_name), MODE_PRIVATE);
        String id = prefs.getString(deliverIDPreferenceTag, "");
        String date = prefs.getString(deliverDatePreferenceTag, "");
        String time = prefs.getString(deliverTimePreferenceTag, "");
        deliverID = id;
        deliverDate = date;
        deliverTime = time;
        return id;
    }

    public void setPickDeliver(Boolean value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(String.valueOf(R.string.app_name), MODE_PRIVATE).edit();
        editor.putBoolean(pickupDeliverTag, value);
        editor.apply();
    }

    public void getPickDeliver() {
        SharedPreferences prefs = context.getSharedPreferences(String.valueOf(R.string.app_name), MODE_PRIVATE);
        Boolean pickDlver = prefs.getBoolean(pickupDeliverTag, false);
        pickDeliver = pickDlver;
    }


    public void setPasswordResetType(String type) {
        SharedPreferences.Editor editor = context.getSharedPreferences(String.valueOf(R.string.app_name), MODE_PRIVATE).edit();
        editor.putString(passwordResetTag, type);
        editor.apply();
    }

    public String getPasswordResetType() {
        SharedPreferences prefs = context.getSharedPreferences(String.valueOf(R.string.app_name), MODE_PRIVATE);
        String type = prefs.getString(passwordResetTag, "");
        passwordResetType = type;
        return type;
    }

}
