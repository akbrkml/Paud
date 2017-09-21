package com.kemendikbud.paud.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.View;

import com.kemendikbud.paud.App;
import com.kemendikbud.paud.Main;
import com.kemendikbud.paud.R;

/**
 * Created by akbar on 26/07/17.
 */

public class ViewUtils {

    public static void showSnackbar(String message, View view) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }

    public static void showError(String message){
        new AlertDialog.Builder(App.getContext())
                .setTitle(App.getContext().getString(R.string.app_name))
                .setMessage(message)
                .setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
    }

    public static void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(App.getContext())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    public static int numberOfColumns(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Main)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // You can change this divider to adjust the size of the poster
        int widthDivider = 800;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 1) return 1;
        return nColumns;
    }
}
