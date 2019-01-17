package com.creativeapps.salat_times.RecieversAndService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.creativeapps.salat_times.Activities.MainActivity;
/**
 * Created by Sifat on 6/21/2017.
 */
public class InternetReciever extends BroadcastReceiver {

    public SharedPreferences sharedPreferences;
    public static final String mypreference = "salat";
    @Override
    public void onReceive(final Context context, final Intent intent) {

        sharedPreferences = context.getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

       if(isActivity_already_open()){

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifiInfo != null && wifiInfo.isConnected()) || (mobileInfo != null && mobileInfo.isConnected())) {
            Log.i("internet_reciever", "internet_on");


            Intent intent1= new Intent(context,MainActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            context.startActivity(intent1);

        }
       }
    }

    public boolean isActivity_already_open() {
        return sharedPreferences.getBoolean("Activity_open", false);

    }
}