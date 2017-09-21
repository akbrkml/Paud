package com.kemendikbud.paud.util;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;
import java.util.UUID;

/**
 * Created by akbar on 29/08/17.
 */

public class Constant {
    public static final String BASE_URL = "https://app.paud-dikmas.kemdikbud.go.id/intern/api-paud/";

    public static final String URL_PICTURES = BASE_URL + "uploads/";

    public static final int SPLASH_DELAY = 3000;

    public static String getDate(){
        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String date = df.format(c.getTime());

        Log.d("Current time => ", date);

        return date;
    }

    public static String getDateTime(){
        Calendar c = Calendar.getInstance();

        SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.mmm");
        String dateTime = d.format(c.getTime());

        Log.d("Current time => ", dateTime);

        return dateTime;
    }

    public static String uniqueID() {
        return UUID.randomUUID().toString();
    }
}
