package com.creativeapps.salat_times.RecieversAndService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import com.creativeapps.salat_times.Activities.MainActivity;
import com.creativeapps.salat_times.R;
/**
 * Created by Sifat on 5/29/2017.
 */
public class MediaPlayerService extends Service {
    private static final String TAG = "MediaPlayerService";
    MediaPlayer player;
    int tone;
    int i=0;

    public SharedPreferences sharedPreferences;
    public static final String mypreference = "salat";


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
      //  Toast.makeText(this, "My Service Created", Toast.LENGTH_LONG).show();
        Log.d(TAG, "mediaplayerservice");

        sharedPreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

    }

    @Override
    public void onDestroy() {
      //  Toast.makeText(this, "My Service Stopped", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onDestroy");
        player.stop();
        player.release();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
         super.onStartCommand(intent, flags, startId);

        sharedPreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

        try{
            tone= get_tone_for_reciever();
        }
        catch (Exception e) {
            tone=0;
            Log.e(TAG,"tone_not_found");
        }

        // Log.e(TAG,"tone="+tone);
        if (tone == 10) player = MediaPlayer.create(this, R.raw.tone2);
        else if (tone==2) player= MediaPlayer.create(this, R.raw.fajr_azan);

//        else if (tone == 1) player = MediaPlayer.create(this, R.raw.tone1);
//        else if (tone == 2)  player = MediaPlayer.create(this, R.raw.tone2);
//        else if (tone == 3)  player = MediaPlayer.create(this, R.raw.tone3);
        else player = MediaPlayer.create(this, R.raw.azan);


        //player.setAudioStreamType(AudioManager.STREAM_ALARM);



        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){

            @Override
            public void onCompletion(MediaPlayer mp) {
                i++;
                if(i<=getRepeat()){
                    try {
                        player.start();

                        Log.e(TAG,"player_loop="+i);
                    }

                    catch (IllegalStateException ise)
                    {

                    }

                }

                else
                {
                    try
                    {
                        player.stop();

                    }
                    catch (IllegalStateException ise)
                    {

                    }
                    set_azan_off();
//                    Intent dismiss_button_Intent = new Intent(MediaPlayerService.this, DismissButtonReceiver.class);
//                    dismiss_button_Intent.putExtra("notificationId", sharedPreferences.getInt("notificationId", 0));
//                    sendBroadcast(dismiss_button_Intent);

                }

            }});

        //  player.setLooping(true); // Set looping
        Log.e(TAG, "onStart"+"tone="+tone);
        player.start();

        return START_STICKY;
    }

    @Override
    public void onStart(Intent intent, int startid) {


    }

    public int get_tone_for_reciever() {
        return sharedPreferences.getInt("player_tone", 5);
    }
    private void set_azan_off() {
        sharedPreferences.edit().putBoolean("notification",false).apply();

    }


    public int getRepeat() {

        return  sharedPreferences.getInt("repeat",5);


    }

}