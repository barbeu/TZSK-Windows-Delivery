package com.example.tzadmin.tzsk_windows.Location;

import android.content.Context;
import android.location.Location;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;

/**
 * Created by tzadmin on 04.05.17.
 */

public class MyLocation {

    public static String Latitude = null;
    public static String Longitude = null;

    public static void SetListener(Context context) {
        SmartLocation.with(context).location()
                .start(new OnLocationUpdatedListener() {
                    @Override
                    public void onLocationUpdated(Location location) {
                        Longitude = String.valueOf(location.getLongitude());
                        Latitude = String.valueOf(location.getLatitude());
                    }
                });
    }
}
