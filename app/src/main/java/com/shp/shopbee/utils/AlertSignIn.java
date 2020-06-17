package com.shp.shopbee.utils;


import android.content.DialogInterface;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import com.shp.shopbee.MainActivity;

public class AlertSignIn {

    public AlertSignIn(final MainActivity activity){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Information Message");
        builder.setMessage("You should sign in or sing up");

        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new PopupSignIn(activity);  //open the loginPopup (Utils > PopupSignIn)
            }
        }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.e("Alert Sign In", "sign in alert was closed");
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
