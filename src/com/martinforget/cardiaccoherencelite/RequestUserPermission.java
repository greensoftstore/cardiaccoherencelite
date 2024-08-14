package com.martinforget.cardiaccoherencelite;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import android.util.Log;

import io.monedata.Monedata;

public class RequestUserPermission {

    private Activity activity;
    // Storage Permissions
    public static final int REQUEST_CAMERA = 1;
    public static final int REQUEST_POSITION = 2;
    public static String[] CAMERA_PERMISSIONS = {
            CAMERA
    };
    public static String[] LOCATION_PERMISSIONS = {
            ACCESS_FINE_LOCATION
    };



    public RequestUserPermission(Activity activity) {
        this.activity = activity;
    }

    public  boolean verifyCameraPermissions() {
        // Check if we have Camera permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
        Log.d("Permission:", "Camera check");
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    CAMERA_PERMISSIONS,
                    REQUEST_CAMERA

            );
            return false;
        }
        return true;
    }

    public  boolean verifyPositionPermissions() {
        // Check if we have Position permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
        int permission2 = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
        Log.d("Permission:", "Location check");
        if (permission != PackageManager.PERMISSION_GRANTED && permission2 != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    LOCATION_PERMISSIONS,
                    REQUEST_POSITION

            );


            return false;
        }
        return true;
    }

}