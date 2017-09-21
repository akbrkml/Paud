package com.kemendikbud.paud.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by akbar on 11/08/17.
 */

public class NetworkUtils {
    public static boolean isNetworkConnected(Context context){
        try{
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
