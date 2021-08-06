package com.dbtechprojects.addressbookapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.text.Layout;
import android.view.View;
import android.widget.Toast;

import com.dbtechprojects.addressbookapp.R;
import com.google.android.material.snackbar.Snackbar;

public class Constants {
    public static final int REQUEST_PHONE_CODE = 22;

    public static void showToast(String message, Context context){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showSettingsSnackBar(View layout, Activity activity){
        Snackbar snackbar = Snackbar.make(layout, R.string.permission_denied, Snackbar.LENGTH_LONG);
        View snackbarLayout = snackbar.getView();
        snackbarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open permission settings
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                intent.setData(uri);
                activity.startActivity(intent);
            }
        });
        snackbar.show();
    }

}
