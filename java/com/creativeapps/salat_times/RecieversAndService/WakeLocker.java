package com.creativeapps.salat_times.RecieversAndService;
/**
 * Created by Sifat on 5/30/2017.
 */
import android.content.Context;
import android.os.PowerManager;


public abstract class WakeLocker {
    private static PowerManager.WakeLock wakeLock;

    public static void acquire(Context ctx) {
        if (wakeLock != null) wakeLock.release();

        PowerManager pm = (PowerManager) ctx.getSystemService(Context.POWER_SERVICE);
        if (pm != null) {
            wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK |
                    PowerManager.ACQUIRE_CAUSES_WAKEUP |
                    PowerManager.ON_AFTER_RELEASE,"ALARM");
            wakeLock.acquire();
        }

    }

    public static void release() {
        if (wakeLock != null) wakeLock.release(); wakeLock = null;
    }
}