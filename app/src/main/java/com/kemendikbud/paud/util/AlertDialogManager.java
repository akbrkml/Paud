package com.kemendikbud.paud.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.kemendikbud.paud.R;

/**
 * Created by akbar on 13/08/17.
 */

public class AlertDialogManager {

    public static void showAlertDialog(Context context, String title, String message,
                                Boolean status){
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        if(status != null)
            // Setting alert dialog icon
            alertDialog.setIcon((status) ? R.drawable.ic_done : R.drawable.ic_error_outline);

        // Setting OK Button
        alertDialog.setButton(-1, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        // Showing Alert Message
        alertDialog.show();

    }
}
