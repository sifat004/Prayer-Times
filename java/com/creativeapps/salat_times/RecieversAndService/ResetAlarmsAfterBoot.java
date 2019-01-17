//package com.creativeapps.salat_times.RecieversAndService;
//import android.app.AlarmManager;
//import android.app.PendingIntent;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.location.Location;
//import android.os.Build;
//import android.os.SystemClock;
//import android.util.Log;
//
//import com.azan.PrayerTimes;
//import com.azan.TimeCalculator;
//import com.azan.types.PrayersType;
//import com.creativeapps.salat_times.Model.DataModel;
//
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.GregorianCalendar;
//import java.util.Locale;
//import java.util.TimeZone;
//
//import static com.azan.types.AngleCalculationType.EGYPT;
//import static com.azan.types.AngleCalculationType.ISNA;
//import static com.azan.types.AngleCalculationType.KARACHI;
//import static com.azan.types.AngleCalculationType.MUHAMMADIYAH;
//import static com.azan.types.AngleCalculationType.MWL;
//
///**
// * Created by Sifat on 6/9/2017.
// */
//public class ResetAlarmsAfterBoot extends BroadcastReceiver {
//    public SharedPreferences sharedPreferences;
//    public static final String mypreference = "salat";
//
//
//    private double latitude;
//    private double longitude;
//    private double altitude;
//    public Location known_location;
//    private String fajr_time_string, sunrise_time_string, zuhr_time_string, asr_time_string, magrib_time_string, isha_time_string,
//            haram1_time_string, haram2_time_string, haram3_time_string, ishrak_time_string, auabin_time_string, tahazzud_time_string, midnight_time_string;
//    private Date fajr_time, sunrise_time, zuhr_time, asr_time, magrib_time, isha_time,
//            haram1_time, haram2_time, haram3_time, ishrak_time, auabin_time, tahazzud_time;
//
//    ArrayList<DataModel> dataModels;
//
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//
//        Log.e("reboot_receiver", "recieved receiver");
//
//        sharedPreferences = context.getSharedPreferences(mypreference,
//                Context.MODE_PRIVATE);
//
//        //boolean b= sharedPreferences.getBoolean("daily_check_set",false);
//        //if(!b)
//        //set_alarm_to_check_active_alarms(context);
//
//
//        //  Toast.makeText(context, "after_boot_check_done", Toast.LENGTH_LONG).show();
//
//        // WakeLocker.acquire(context);
//
//        WakeLocker.acquire(context);
//
//        set_azan_to_check(context);
//
//        known_location = retriev_location();
//        latitude = known_location.getLatitude();
//        longitude = known_location.getLongitude();
//        altitude = known_location.getAltitude();
//        setPrayerTimes();
//        dataModels = setArrayList();
//        DataModel dataModel;
//
//        for (int i = 0; i < dataModels.size(); i++) {
//            dataModel = dataModels.get(i);
//            if (get_everydayActive(dataModel.getName()) || get_nextdayActive(dataModel.getName())) {
//                cancel_azan_alarm(dataModel.getTag(), context);
//                set_isAlarmActive(dataModel.getName(), true);
//                set_azan_alarm(dataModel.getAzan_time().getTime(), dataModel.getTag(), dataModel.getName(), context);
//            } else {
//                cancel_azan_alarm(dataModel.getTag(), context);
//                set_isAlarmActive(dataModel.getName(), false);
//            }
//
//
//        }
//
//
////
////        if (get_everydayActive("Fajr")||get_nextdayActive("Fajr")){
////            cancel_azan_alarm(1,context);
////            set_isAlarmActive("Fajr",true);
////            set_azan_alarm(fajr_time.getTime(),1,context,"Fajr");
////        }
////
////        else {
////            cancel_azan_alarm(1,context);
////            set_isAlarmActive("Fajr",false);
////        }
////
////
////        if (get_everydayActive("Sun")||get_nextdayActive("Sun")){
////            cancel_azan_alarm(2,context);
////            set_isAlarmActive("Sun",true);
////            set_azan_alarm(sunrise_time.getTime(),2,context,"Sun");
////        }
////
////        else {
////            cancel_azan_alarm(2,context);
////            set_isAlarmActive("Sun",false);
////        }
////        if (get_everydayActive("Zuhr")||get_nextdayActive("Zuhr")){
////            cancel_azan_alarm(3,context);
////            set_isAlarmActive("Zuhr",true);
////            set_azan_alarm(zuhr_time.getTime(),3,context,"Zuhr");
////
////        }
////        else {
////            cancel_azan_alarm(3,context);
////            set_isAlarmActive("Zuhr",false);
////        }
////        if (get_everydayActive("Asr")||get_nextdayActive("Asr")){
////            cancel_azan_alarm(4,context);
////            set_isAlarmActive("Asr",true);
////            set_azan_alarm(asr_time.getTime(),4,context,"Asr");
////        }
////        else {
////            cancel_azan_alarm(4,context);
////            set_isAlarmActive("Asr",false);
////        }
////        if (get_everydayActive("Magrib")||get_nextdayActive("Magrib")){
////            cancel_azan_alarm(5,context);
////            set_isAlarmActive("Magrib",true);
////            set_azan_alarm(magrib_time.getTime(),5,context,"Magrib");
////        }
////        else {
////            cancel_azan_alarm(5,context);
////            set_isAlarmActive("Magrib",false);
////        }
////        if (get_everydayActive("Esha")||get_nextdayActive("Esha")){
////            cancel_azan_alarm(6,context);
////            set_isAlarmActive("Esha",true);
////            set_azan_alarm(isha_time.getTime(),6,context,"Esha");
////        }
////        else {
////            cancel_azan_alarm(6,context);
////            set_isAlarmActive("Esha",false);
////        }
//
//      //  sharedPreferences.edit().putBoolean("daily_check_set", false).apply();
//
//           WakeLocker.release();
//    }
//
//
////        if ((get_everydayActive("Fajr")||get_isAlarmActive("Fajr")) && fajr_time.getTime()>System.currentTimeMillis() ){
////
////            set_azan_alarm(fajr_time.getTime(),1,context,"Fajr");
////
////            Log.e("reboot_receiver", "fajr alarm set");
////        }
////
////
////        if ((get_everydayActive("Sun")||get_isAlarmActive("Sun")) && sunrise_time.getTime()>System.currentTimeMillis()){
////
////            set_azan_alarm(sunrise_time.getTime(),2,context,"Sun");
////
////            Log.e("reboot_receiver", "sun alarm set");
////
////        }
////
////
////        if ((get_everydayActive("Zuhr")||get_isAlarmActive("Zuhr" ))&& zuhr_time.getTime()>System.currentTimeMillis()){
////
////            set_azan_alarm(zuhr_time.getTime(),3,context,"Zuhr");
////
////            Log.e("reboot_receiver", "zuhr alarm set");
////
////
////        }
////
////        if ((get_everydayActive("Asr")||get_isAlarmActive("Asr"))&& asr_time.getTime()>System.currentTimeMillis()){
////
////            set_azan_alarm(asr_time.getTime(),4,context,"Asr");
////
////            Log.e("reboot_receiver", "asr alarm set");
////
////        }
////
////        if ((get_everydayActive("Magrib")||get_isAlarmActive("Magrib"))&& magrib_time.getTime()>System.currentTimeMillis()){
////
////            set_azan_alarm(magrib_time.getTime(),5,context,"Magrib");
////
////            Log.e("reboot_receiver", "magrib alarm set");
////
////        }
////
////        if ((get_everydayActive("Esha")||get_isAlarmActive("Esha"))&& isha_time.getTime()>System.currentTimeMillis()){
////
////            set_azan_alarm(isha_time.getTime(),6,context,"Esha");
////
////            Log.e("reboot_receiver", "esha alarm set");
////
////        }
//
//
//    //sharedPreferences.edit().putBoolean("daily_check_set", false).apply();
//
//    // WakeLocker.release();
//
//
////}
//
//
//    public void set_alarm_to_check_active_alarms(Context context) {
//        Calendar calendar = Calendar.getInstance();
//
//        int hour_now=calendar.get(Calendar.HOUR_OF_DAY);
//        int waiting_hours= 24-hour_now;
//        //calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.set(Calendar.HOUR_OF_DAY, 1);
//        calendar.set(Calendar.MINUTE, 1);
//        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(context, DailyReciever.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()+(waiting_hours*60*60*1000)+(30*60*1000),
//                AlarmManager.INTERVAL_DAY, pendingIntent);
//
////        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
////                AlarmManager.INTERVAL_DAY, pendingIntent);
//         sharedPreferences.edit().putBoolean("daily_check_set", true).apply();
//
//        Log.e("reboot_receiver", "set alarm to check alarm set");
//
//    }
//
//
//
//
//    public Location retriev_location() {
//        String lat = sharedPreferences.getString("LOCATION_LAT", "23.6850");
//        String lon = sharedPreferences.getString("LOCATION_LON", "90.3563");
//        Location location ;
//
//        String provider = sharedPreferences.getString("LOCATION_PROVIDER", "myLocationProvider");
//        location = new Location(provider);
//        location.setLatitude(Double.parseDouble(lat));
//        location.setLongitude(Double.parseDouble(lon));
//
//        return location;
//    }
//
//    public PrayerTimes getPrayerTimesObject(Location location) {
//        GregorianCalendar date = new GregorianCalendar();
//        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"),
//                Locale.getDefault());
//        Date currentLocalTime = calendar.getTime();
//        DateFormat date1 = new SimpleDateFormat("Z");
//        String localTime = date1.format(currentLocalTime);
//        double tz;
//        try {
//            tz = Double.parseDouble(localTime);
//
//        } catch (NumberFormatException nf) {
//            tz = +0600;   //timezone for dhaka, this is the common way to write tz, not a typo
//
//        }
//
//
//        double latitude = location.getLatitude();
//        double longitude = location.getLongitude();
//        double altitude = location.getAltitude();
//
//        int i = getRule();
//        PrayerTimes prayerTimes;
//        if (i == 0) {
//            prayerTimes = new TimeCalculator().date(date).location(latitude, longitude, tz,
//                    altitude).timeCalculationMethod(MWL).calculateTimes();
//        } else if (i == 1) {
//            prayerTimes = new TimeCalculator().date(date).location(latitude, longitude, tz,
//                    altitude).timeCalculationMethod(ISNA).calculateTimes();
//        } else if (i == 2) {
//            prayerTimes = new TimeCalculator().date(date).location(latitude, longitude, tz,
//                    altitude).timeCalculationMethod(EGYPT).calculateTimes();
//        } else if (i == 3) {
//            prayerTimes = new TimeCalculator().date(date).location(latitude, longitude, tz,
//                    altitude).timeCalculationMethod(MUHAMMADIYAH).calculateTimes();
//        } else {
//            prayerTimes = new TimeCalculator().date(date).location(latitude, longitude, tz,
//                    altitude).timeCalculationMethod(KARACHI).calculateTimes();
//        }
//        return prayerTimes;
//    }
//    public int getRule() {
//        //Log.i(tag, "get_rule" + sharedPreferences.getInt("rule", 4));
//        return sharedPreferences.getInt("rule", 4);
//    }
//
//    public void setPrayerTimes() {
//
//        Location location= retriev_location();
//        PrayerTimes prayerTimes = getPrayerTimesObject(location);
//
//
//        fajr_time = prayerTimes.getPrayTime(PrayersType.FAJR);
//        sunrise_time = (prayerTimes.getPrayTime(PrayersType.SUNRISE));
//        haram1_time = sunrise_time;
//        ishrak_time = new Date(sunrise_time.getTime() + (25 * 60 * 1000));
//        zuhr_time = prayerTimes.getPrayTime(PrayersType.ZUHR);
//        haram2_time = new Date(zuhr_time.getTime() - (10 * 60 * 1000));
//
//        asr_time = (prayerTimes.getPrayTime(PrayersType.ASR));
//        magrib_time = (prayerTimes.getPrayTime(PrayersType.MAGHRIB));
//        haram3_time = new Date(magrib_time.getTime() - (10 * 60 * 1000));
//
//        auabin_time = new Date(magrib_time.getTime() + (15 * 60 * 1000));
//        isha_time = (prayerTimes.getPrayTime(PrayersType.ISHA));
//        tahazzud_time = new Date(isha_time.getTime() + (60 * 60 * 1000));
//
//
//
//        fajr_time_string = getTime12hourFormat(fajr_time);
//        sunrise_time_string = getTime12hourFormat(sunrise_time);
//        haram1_time_string = getTime12hourFormat(haram1_time);
//        ishrak_time_string = getTime12hourFormat(ishrak_time);
//        haram2_time_string = getTime12hourFormat(haram2_time);
//        zuhr_time_string = getTime12hourFormat(zuhr_time);
//        asr_time_string = getTime12hourFormat(asr_time);
//        haram3_time_string = getTime12hourFormat(haram3_time);
//        magrib_time_string = getTime12hourFormat(magrib_time);
//        auabin_time_string = getTime12hourFormat(auabin_time);
//        isha_time_string = getTime12hourFormat(isha_time);
//        tahazzud_time_string = getTime12hourFormat(tahazzud_time);
//        // Log.i(tag, "setPrayerTimes");
//    }
//
//    public String getTime12hourFormat(Date date) {
//        SimpleDateFormat localDateFormat = new SimpleDateFormat("hh:mm", Locale.US);
//        String time = localDateFormat.format(date);
//        // Log.i(tag, "gettime12hourformat");
//        return time;
//    }
//
//    public void set_azan_alarm(long time, int tag,Context context,String name) {
//        Intent intentAlarm = new Intent(context, AlarmReciever.class);
//
//        intentAlarm.putExtra("tag", tag);
//        intentAlarm.putExtra("tone", get_tone(name));
//        intentAlarm.putExtra("name", name);
//        AlarmManager alarmManager = (AlarmManager) context .getSystemService(Context.ALARM_SERVICE);
//        alarmManager.set(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(context, tag, intentAlarm, 0));
//
//        Log.e("reboot_receiver", "alarm time "+time);
//
//
//    }
//
//
//
//    public void set_azan_alarm(long time, int tag, String name,Context context) {
//        Intent intentAlarm = new Intent(context, AlarmReciever.class);
//        intentAlarm.putExtra("tag", tag);
//        intentAlarm.putExtra("tone", get_tone(name));
//        intentAlarm.putExtra("name", name);
//        Log.e("reboot","tone_set="+get_tone(name)+" tag="+tag+" name="+name);
//
//
//        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        if (alarmManager != null) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(context, tag, intentAlarm, 0));
//            }
//
//            else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(context, tag, intentAlarm, 0));
//            }
//
//            else {
//
//                alarmManager.set(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(context, tag, intentAlarm, 0));
//
//            }
//
//
//        }
//        //Toast.makeText(this, "অ্যালার্ম সেট করা হয়েছে", Toast.LENGTH_SHORT).show();
//
//
//    }
//
//    public void cancel_azan_alarm(int tag,Context mContext) {
//        Intent intentAlarm = new Intent(mContext, AlarmReciever.class);
//        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, tag, intentAlarm, 0);
//        if (alarmManager != null) {
//            alarmManager.cancel(pendingIntent);
//        }
//
//
//    }
//
//    public boolean get_everydayActive(String name) {
//
//        return  sharedPreferences.getBoolean(name+"everyday", false);
//    }
//
//
//    public void set_nextdayActive(String name, boolean everydayActive) {
//        sharedPreferences.edit().putBoolean(name+"next_day", everydayActive).apply();
//    }
//
//    public boolean get_nextdayActive(String name) {
//        return sharedPreferences.getBoolean(name+"next_day", false);
//    }
//
//
//    public void set_isAlarmActive(String name, boolean isAlarmActive) {
//        sharedPreferences.edit().putBoolean(name+"active", isAlarmActive).apply();
//    }
//
//    public boolean get_isAlarmActive(String name) {
//        return sharedPreferences.getBoolean(name+"active", false);
//    }
//
//    public int get_tone(String name) {
//        return sharedPreferences.getInt(name + "tone", 5);
//    }
//
//
//    public void set_tone(String name, int tone) {
//        sharedPreferences.edit().putInt(name + "tone", tone).apply();
//        Log.e("set_alarm_activity","tone_set="+tone+" get_tone="+get_tone(name));
//    }
//
//    public ArrayList<DataModel> setArrayList() {
//        ArrayList<DataModel> dataModels = new ArrayList<>();
//        dataModels.add(new DataModel("Fajr", fajr_time_string, fajr_time, 1, sunrise_time));
//        dataModels.add(new DataModel("Sun", sunrise_time_string, sunrise_time, 2, zuhr_time));
//        dataModels.add(new DataModel("Haram_1", haram1_time_string, haram1_time, 3, ishrak_time));
//        dataModels.add(new DataModel("Ishrak", ishrak_time_string, ishrak_time, 4, haram2_time));
//        dataModels.add(new DataModel("Haram_2", haram2_time_string, haram2_time, 5, zuhr_time));
//        dataModels.add(new DataModel("Zuhr", zuhr_time_string, zuhr_time, 6, asr_time));
//        dataModels.add(new DataModel("Asr", asr_time_string, asr_time, 7, magrib_time));
//        dataModels.add(new DataModel("Haram_3", haram3_time_string, haram3_time, 8, magrib_time));
//        dataModels.add(new DataModel("Magrib", magrib_time_string, magrib_time, 9, isha_time));
//        dataModels.add(new DataModel("Auabin", auabin_time_string, auabin_time, 10, isha_time));
//        dataModels.add(new DataModel("Esha", isha_time_string, isha_time, 11, fajr_time));
//        dataModels.add(new DataModel("Tahazzud", tahazzud_time_string, tahazzud_time, 12, fajr_time));
//        // Log.i(tag, "setarraylist");
//        return dataModels;
//
//
//
//    }
//
//    public void set_azan_to_check(Context mContext) {
//
//        Calendar calendar = Calendar.getInstance();
//
//        //calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.set(Calendar.HOUR_OF_DAY, 1);
//        calendar.set(Calendar.MINUTE, 1);
//        long time = calendar.getTimeInMillis()+24*60*60*1000;
//        int tag=0;
//
//        Intent intentAlarm = new Intent(mContext, DailyReciever.class);
//
//
//        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
//        if (alarmManager != null) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(mContext, tag, intentAlarm, 0));
//            }
//
//            else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(mContext, tag, intentAlarm, 0));
//            }
//
//            else {
//
//                alarmManager.set(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(mContext, tag, intentAlarm, 0));
//
//            }
//
//            sharedPreferences.edit().putBoolean("daily_check_set", true).apply();
//
//        }
//        //   Toast.makeText(this, "অ্যালার্ম সেট করা হয়েছে", Toast.LENGTH_SHORT).show();
//
//
//    }
//
//}
