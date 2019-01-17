package com.creativeapps.salat_times.RecieversAndService;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import com.creativeapps.salat_times.Activities.MainActivity;

/**
 * Created by Sifat on 5/29/2017.
 */
public class DismissButtonReceiver extends BroadcastReceiver {
    public SharedPreferences sharedPreferences;
    public static final String mypreference = "salat";

    @Override
    public void onReceive(Context context, Intent intent) {

        sharedPreferences = context.getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        set_azan_off();
        int notificationId;
//        if(intent.hasExtra("notificationId")) {
//            notificationId = intent.getIntExtra("notificationId", 1);
//        }
//
       // else
            notificationId = sharedPreferences.getInt("notificationId",1);



        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(notificationId);



        Intent intent1=new Intent(context,MediaPlayerService.class);
      // intent1.setFlags(Intent.F);

        context.stopService(intent1);

    }


    private void set_azan_off() {
        sharedPreferences.edit().putBoolean("notification",false).apply();

    }
}