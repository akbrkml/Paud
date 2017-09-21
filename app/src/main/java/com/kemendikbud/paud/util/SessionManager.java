package com.kemendikbud.paud.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.kemendikbud.paud.App;
import com.kemendikbud.paud.model.User;

/**
 * Created by akbar on 12/08/17.
 */

public class SessionManager {
    private static final String TAG = "SessionManager";

    private static SharedPreferences getPref(){
        return PreferenceManager.getDefaultSharedPreferences(App.getContext());
    }

    public static void putUser(Context context, String key, User user) {
        Gson gson = new Gson();
        String json = gson.toJson(user);
        putString(context, key, json);
    }

    public static User getUser(Context context, String key) {
        Gson gson = new Gson();
        String json = getString(context, key);
        User user = gson.fromJson(json, User.class);
        return user;
    }

    public static void putString(Context context, String key, String value) {
        getPref().edit().putString(key, value).apply();
    }

    public static String getString(Context context, String key) {
        return getPref().getString(key, null);
    }

    public static void clear(Context context) {
        getPref().edit().clear().apply();
    }

    public static void save(String key, String value){
        Log.d(TAG, "saveCache: " + value);
        getPref().edit().putString(key, value).apply();
    }

    public static void save(String key, int value){
        Log.d(TAG, "saveCache: " + value);
        getPref().edit().putInt(key, value).apply();
    }

    public static void save(String key, Boolean value){
        Log.d(TAG, "saveCache: " + value);
        getPref().edit().putBoolean(key, value).apply();
    }

    public static Boolean checkExist(String key){
        return getPref().contains(key);
    }

    public static String grabString(String key){
        return getPref().getString(key, null);
    }

    public static Boolean grabBoolean(String key){
        return getPref().getBoolean(key, false);
    }

    public static int grabInt(String key){
        return getPref().getInt(key, 0);
    }
}
