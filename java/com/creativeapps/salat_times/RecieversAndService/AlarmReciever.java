package com.creativeapps.salat_times.RecieversAndService;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.creativeapps.salat_times.Activities.MainActivity;
import com.creativeapps.salat_times.R;

import java.util.Calendar;

import static io.fabric.sdk.android.Fabric.TAG;
/**
 * Created by Sifat on 5/6/2017.
 */
public class AlarmReciever extends BroadcastReceiver {
    private static final int MY_NOTIFICATION_ID = 1;
    NotificationManager notificationManager;
    Notification myNotification;
    int tag, tone;
    long time;
    String name, contentText, alarmTone;
    public SharedPreferences sharedPreferences;
    public static final String mypreference = "salat";
    MediaPlayer player;
    int i = 0;
    Calendar calendar;

    @Override
    public IBinder peekService(Context myContext, Intent service) {
        return super.peekService(myContext, service);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        sharedPreferences = context.getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

        calendar= Calendar.getInstance();
        WakeLocker.acquire(context);
        //  Toast.makeText(context, "Alarm received!", Toast.LENGTH_LONG).show();
        Intent resultIntent = new Intent(context, MainActivity.class);
        resultIntent.setAction("android.intent.action.MAIN");
        resultIntent.addCategory("android.intent.category.LAUNCHER");
        tag = intent.getIntExtra("tag", -1);
        Log.e("Alarm Reciever", "tag=" + tag);
        name = intent.getStringExtra("name");

         Log.e("Alarm Reciever", "name=" + name);

//        if (intent.hasExtra("time"))
//         time= intent.getLongExtra("time",calendar.getTimeInMillis());
//        Log.e("Alarm Reciever", "time=" + time);
//        Log.e("Alarm Reciever", "current time=" + calendar.getTimeInMillis());


        //tone = intent.getIntExtra("tone", 5);  Log.e("Alarm Reciever", "tone="+tone);
//       if (intent.hasExtra("tone")){
//           tone = intent.getIntExtra("tag", 0);
//       }
//
//
//        if (azanTimePassed())
//
//        {
//
//
//            Log.e("Alarm Reciever", "not azanTimeNotPassed" + time);
//
//
//            return;
//
//        }


        tone = get_tone(name);
        alarmTone="azan.mp3";
        if (tag == 1 ) contentText = "ফজর এর ওয়াক্ত শুরু হয়েছে";



        else if (tag == 2) contentText = "সূর্যোদয় এর সময় হয়েছে";
        else if (tag == 3) contentText = "নামাজ নিষিদ্ধ";
        else if (tag == 4) contentText = "ইশরাক এর ওয়াক্ত শুরু হয়েছে";
        else if (tag == 5) contentText = "নামাজ নিষিদ্ধ";
        else if (tag == 6) contentText = "যোহর এর ওয়াক্ত শুরু হয়েছে";
        else if (tag == 7) contentText = "আছর এর ওয়াক্ত শুরু হয়েছে";
        else if (tag == 8) contentText = "নামাজ নিষিদ্ধ";
        else if (tag == 9) contentText = "মাগরিব এর ওয়াক্ত শুরু হয়েছে";
        else if (tag == 10) contentText = "আউয়াবিন এর ওয়াক্ত শুরু হয়েছে";
        else if (tag == 11) contentText = "ইশার ওয়াক্ত শুরু হয়েছে";
        else if (tag == 12) contentText = "তাহাজ্জুদ এর ওয়াক্ত শুরু হয়েছে";
        else if (tag == 131) contentText = "ইফতারের সময় হয়েছে";
        else if (tag == 130) contentText = "সেহেরির সময় হয়েছে";


        else contentText = "নামাজ এর ওয়াক্ত হয়েছে";
        set_isAlarmActive(name, false);
        set_nextdayActive(name, false);


       if (tone!=0) {
           Intent mediaPlayerIntent = new Intent(context, MediaPlayerService.class);
           if (tag==1 && tone==5 || tag==130 && tone==5)
               set_tone_for_reciever(2);

           else
               set_tone_for_reciever(tone);

           // mediaPlayerIntent.setFlags(Intent.FLAG_RECEIVER_FOREGROUND);

           try {
               if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                   context.startForegroundService(mediaPlayerIntent);
               }

               else context.startService(mediaPlayerIntent);
           }

           catch (IllegalStateException ie){
               ie.printStackTrace();
           }

       }

       // playAzan(context);


        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Intent dismiss_button_Intent = new Intent(context, DismissButtonReceiver.class);
        dismiss_button_Intent.putExtra("notificationId", MY_NOTIFICATION_ID);
        sharedPreferences.edit().putInt("notificationId", MY_NOTIFICATION_ID).apply();
        PendingIntent btPendingIntent = PendingIntent.getBroadcast(context, 0, dismiss_button_Intent, 0);
        NotificationCompat.Action action = new NotificationCompat.Action.Builder(R.drawable.cross, "Cancel", btPendingIntent).build();
        myNotification = new NotificationCompat.Builder(context)
                .setContentTitle("সালাত")
                .setContentText(contentText)
                .setTicker("")
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.azaan_icon)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(false)
                .setPriority(Notification.PRIORITY_MAX)
                // .addAction(R.drawable.cross,"",btPendingIntent)
                .addAction(action)
                .setOngoing(true)
                .setDeleteIntent(btPendingIntent)
                .build();
        set_azan_on();


      //  myNotification.sound =Uri.parse("android.resource://"+context.getPackageName()+"/"+R.raw.azan);
       //myNotification.flags = Notification.FLAG_ONLY_ALERT_ONCE;
        // myNotification.flags = Notification.FLAG_INSISTENT;
       myNotification.flags = Notification.FLAG_NO_CLEAR;

        notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(MY_NOTIFICATION_ID, myNotification);
        }
//        if (!sharedPreferences.getBoolean("daily_check_set", false))
//            set_alarm_to_check_active_alarms(context);
    }

    private boolean azanTimePassed() {
        return calendar.getTimeInMillis()> time+30*60*1000 ;


    }

    public void set_isAlarmActive(String name, boolean isAlarmActive) {
        sharedPreferences.edit().putBoolean(name + "active", isAlarmActive).apply();
    }

    public void set_nextdayActive(String name, boolean everydayActive) {
        sharedPreferences.edit().putBoolean(name + "next_day", everydayActive).apply();
    }

    public void set_everydayActive(String name, boolean everydayActive) {
        sharedPreferences.edit().putBoolean(name + "everyday", everydayActive).apply();
    }

//    public void set_alarm_to_check_active_alarms(Context context) {
//        Calendar calendar = Calendar.getInstance();
//        //calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.set(Calendar.HOUR_OF_DAY, 1);
//        calendar.set(Calendar.MINUTE, 1);
//        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
//        Intent intent = new Intent(context, DailyReciever.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
//                24 * 60 * 60 * 1000, pendingIntent);
//        sharedPreferences.edit().putBoolean("daily_check_set", true).apply();
//    }

    public int get_tone(String name) {
        return sharedPreferences.getInt(name + "tone", 0);
    }

    public void set_tone_for_reciever(int tone) {
        sharedPreferences.edit().putInt("player_tone", tone).apply();
    }

    private void set_azan_on() {
        sharedPreferences.edit().putBoolean("notification", true).apply();
    }

    private void set_azan_off() {
        sharedPreferences.edit().putBoolean("notification", false).apply();
    }

    private void playAzan(Context context) {
        if (tone == 10) player = MediaPlayer.create(context, R.raw.tone2);
        else player = MediaPlayer.create(context, R.raw.azan);
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                i++;
                if (i < 5) {
                    player.start();
                    //  Log.e(TAG,"player_loop="+i);
                } else {
                    player.stop();
                    set_azan_off();
                }
            }
        });
        //  player.setLooping(true); // Set looping
        Log.e(TAG, "onStart" + "tone=" + tone);
        player.start();
    }

    private  void  stopAzan(){
        player.stop();
        player.release();
    }
}
