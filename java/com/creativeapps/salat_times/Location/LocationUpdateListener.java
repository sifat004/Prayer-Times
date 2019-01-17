package com.creativeapps.salat_times.Location;

import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;

/**
 * Created by Sifat Ullah on 5/3/2018.
 */

public interface LocationUpdateListener {

    public void onLocationUpdated(Location location, FusedLocationProviderClient mFusedLocationClient, LocationCallback locationCallback);
}
