package com.creativeapps.salat_times.Location;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.creativeapps.salat_times.UtilityPackage.Utility;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;

/**
 * Created by Sifat Ullah on 5/3/2018.
 */

public class LocationDetails {


    private static final String TAG = LocationDetails.class.getSimpleName();

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private LocationSettingsRequest locationSettingsRequest;
    private LocationUpdateListener locationUpdateListener;

    long INTERVAL = 1000;
    long FASTEST_INTERVAL = 1000;


    public LocationDetails(Context mContext, Activity mActivity, final SharedPreferences sharedPreferences, final LocationUpdateListener locationUpdateListener) {
        this.locationRequest = LocationRequest.create();
        this.locationRequest.setInterval(INTERVAL);
        this.locationRequest.setFastestInterval(FASTEST_INTERVAL);
        this.locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        this.locationUpdateListener = locationUpdateListener;

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(this.locationRequest);
        this.locationSettingsRequest = builder.build();

        this.locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult); // why? this. is. retarded. Android.
                Location currentLocation = locationResult.getLastLocation();

                save_location(currentLocation, sharedPreferences);
                locationUpdateListener.onLocationUpdated(currentLocation, mFusedLocationClient, locationCallback);


            }
        };

        this.mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext);


        requestLocationUpdates(mContext, mActivity);
    }


    private void requestLocationUpdates(Context mContext, Activity mActivity) {


        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            this.mFusedLocationClient.requestLocationUpdates(this.locationRequest,
                    this.locationCallback, Looper.myLooper());
        }

    }




    public LocationSettingsRequest getLocationSettingsRequest() {
        return this.locationSettingsRequest;
    }

    public void stop() {
        Log.i(TAG, "stop() Stopping location tracking");
        this.mFusedLocationClient.removeLocationUpdates(this.locationCallback);

    }



    private void save_location(Location location, SharedPreferences sharedPreferences) {
        try {
            sharedPreferences.edit().putString("LOCATION_LAT", String.valueOf(location.getLatitude())).apply();
            sharedPreferences.edit().putString("LOCATION_LON", String.valueOf(location.getLongitude())).apply();
            sharedPreferences.edit().putString("LOCATION_PROVIDER", location.getProvider()).apply();
        } catch (Exception e) {
            sharedPreferences.edit().putString("LOCATION_LAT", "23.7925").apply();
            sharedPreferences.edit().putString("LOCATION_LON", "90.4078").apply();
            sharedPreferences.edit().putString("LOCATION_PROVIDER", "myLocationProvider").apply();
        }
        Log.e(TAG,"saveLocation"+ location.toString());

        this.stop();
    }

    public Location getLocation(SharedPreferences sharedPreferences) {
        String lat = sharedPreferences.getString("LOCATION_LAT", "23.7925");
        String lon = sharedPreferences.getString("LOCATION_LON", "90.4078");
        Location location;
        String provider = sharedPreferences.getString("LOCATION_PROVIDER", "myLocationProvider");
        location = new Location(provider);
        location.setLatitude(Double.parseDouble(lat));
        location.setLongitude(Double.parseDouble(lon));


       //Log.e(TAG,"getLocation"+ location.toString());

        return location;
    }

}