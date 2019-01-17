package com.creativeapps.salat_times.Activities;


import android.Manifest;
import android.animation.ValueAnimator;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.azan.PrayerTimes;
import com.azan.TimeCalculator;
import com.azan.types.PrayersType;
import com.crashlytics.android.Crashlytics;
import com.creativeapps.salat_times.Adapter.Prayer_Times_List_Adapter;
import com.creativeapps.salat_times.BuildConfig;
import com.creativeapps.salat_times.Location.LocationDetails;
import com.creativeapps.salat_times.Location.LocationUpdateListener;
import com.creativeapps.salat_times.Model.ArabicDate;
import com.creativeapps.salat_times.Model.DataModel;
import com.creativeapps.salat_times.R;
import com.creativeapps.salat_times.RecieversAndService.AlarmReciever;
import com.creativeapps.salat_times.RecieversAndService.DailyReciever;
import com.creativeapps.salat_times.RecieversAndService.DismissButtonReceiver;
import com.creativeapps.salat_times.UtilityPackage.Fonts;
import com.creativeapps.salat_times.MuslimCompanion.HanafiAsr;
import com.creativeapps.salat_times.UtilityPackage.ShareActivity;
import com.creativeapps.salat_times.UtilityPackage.ShowAdInterface;
import com.creativeapps.salat_times.UtilityPackage.Utility;
import com.facebook.CallbackManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.plus.PlusShare;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.chrono.IslamicChronology;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import io.fabric.sdk.android.Fabric;

import static com.azan.types.AngleCalculationType.EGYPT;
import static com.azan.types.AngleCalculationType.ISNA;
import static com.azan.types.AngleCalculationType.KARACHI;
import static com.azan.types.AngleCalculationType.MUHAMMADIYAH;
import static com.azan.types.AngleCalculationType.MWL;
import static com.creativeapps.salat_times.UtilityPackage.Utility.PACKAGENAME;


public class MainActivity extends AppCompatActivity implements ShowAdInterface, NavigationView.OnNavigationItemSelectedListener {


    private static final int SEHRI_GUARD_MINS = 5 ;
    private static final int SUNRISE_GUARD_MINS = 5;
    private final String TAG = "Salat_app";

    public final int permission = 7;


    private String fajr_time_string, sunrise_time_string, zuhr_time_string, asr_time_string, magrib_time_string, isha_time_string,
            haram1_time_string, haram2_time_string, haram3_time_string, ishrak_time_string, auabin_time_string, tahazzud_time_string, midnight_time_string;
    private Date fajr_time, sunrise_time, zuhr_time, asr_time, magrib_time, isha_time,
            haram1_time, haram2_time, haram3_time, ishrak_time, auabin_time, tahazzud_time;

    private RelativeLayout mainLayout;

    CallbackManager callbackManager;


    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private Fonts fonts;


    private AlertDialog gpsDialog;
    private AlertDialog internetDialog;


    private SharedPreferences sharedPreferences;
    public static final String mypreference = "salat";

    public CountDownTimer countDownTimer=null;

    private com.google.android.gms.ads.InterstitialAd mInterstitialAd;

    //private com.facebook.ads.AdView adView;
    AlertDialog dialog;
    private boolean has_focus;
    private long A_DAY_IN_MILLISECONDS=(24 * 60 * 60 * 1000);
    private long fireBaseRemoteConfigCacheExpirationSecond=12;
    private java.lang.String HIJRI_CORRECTION="correction_hijri";
    private FirebaseRemoteConfig mFirebaseRemoteConfig;


    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FCM();
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);


       firebaseRemoteConfig();

        set_daily_checker();


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.INTERNET,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, Utility.permission);
            return;
        }

        appOpeningCount();


        sharedPreferences.edit().putBoolean("gps", check_gps_connection()).apply();
        sharedPreferences.edit().putBoolean("net", internet_connection(this)).apply();



        if (!isLocation_already_updated_for_first_time()) {
            if (!internet_connection(this)) connect_to_internet_dialog();
            if (!check_gps_connection()) gps_dialog();

        }
        sharedPreferences.edit().putBoolean("change", false).apply();

        fab_paramsSet(false);

        //if (!sharedPreferences.getBoolean("daily_check_set", false))
            //set_alarm_to_check_active_alarms();



    }

    private void firebaseRemoteConfig() {

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);

        mFirebaseRemoteConfig.fetch(fireBaseRemoteConfigCacheExpirationSecond)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
//                            Toast.makeText(MainActivity.this, "Fetch Succeeded",
//                                    Toast.LENGTH_SHORT).show();

                            // After config data is successfully fetched, it must be activated before newly fetched
                            // values are returned.
                            mFirebaseRemoteConfig.activateFetched();
                            setHijriCorrectionFromFirebase();
                            initializeUI();
                        } else {
//                            Toast.makeText(MainActivity.this, "Fetch Failed",
//                                    Toast.LENGTH_SHORT).show();
                        }
                     //   setHijriCorrectionFromFirebase();


                    }
                });
    }


    private void setHijriCorrectionFromFirebase() {

       int  hijri_correction = (int) mFirebaseRemoteConfig.getLong(HIJRI_CORRECTION);
        sharedPreferences.edit().putInt(HIJRI_CORRECTION,hijri_correction).apply();


    }

    private int getHijriCorrectionFromFirebase(){
       return sharedPreferences.getInt(HIJRI_CORRECTION,0);

    }
    @Override
    protected void onResume() {
        super.onResume();



        ADS(azan_running());

       // if (getCount()%5==0) showAd();

        Log.e("onresume", "count"+Utility.getCount(getApplication(),MainActivity.this));

        initializeUI();


        has_focus = true;

        if (azan_running()) {
            create_cancel_option_in_activity();
        }

//        else {
//            set_azan_off();
//        }


        if (check_gps_connection() && !sharedPreferences.getBoolean("gps", true)) {


            Log.e("onresume", "gps");



            sharedPreferences.edit().putBoolean("gps", true).apply();


            if (gpsDialog != null && gpsDialog.isShowing()) gpsDialog.dismiss();

            initializeUI();

        }
        if (internet_connection(this) && !sharedPreferences.getBoolean("net", true)) {
            Log.e("onresume", "net");



            sharedPreferences.edit().putBoolean("net", true).apply();

            try {
                if (internetDialog != null && internetDialog.isShowing())
                    internetDialog.dismiss();
            } catch (NullPointerException n) {
                n.printStackTrace();
            }
            initializeUI();
        }


    }

    private int getCount() {
      return   sharedPreferences.getInt("int_count",0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        has_focus = false;
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)){

            mDrawerLayout.closeDrawer(Gravity.LEFT);
        }

        countDownTimer.cancel();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }


    void initializeUI() {


        fonts = new Fonts(this);
        final Location lastLocation;




            final LocationUpdateListener locationUpdateListener = new LocationUpdateListener() {
                @Override
                public void onLocationUpdated(Location location, FusedLocationProviderClient mFusedLocationClient, LocationCallback locationCallback) {
                    mFusedLocationClient.removeLocationUpdates(locationCallback);

                    if (!isLocation_already_updated_for_first_time()) {
                        setLocation_already_updated_for_first_time(true);

                    }

                }
            };
            LocationDetails locationDetails = new LocationDetails(MainActivity.this, MainActivity.this, sharedPreferences, locationUpdateListener);

            lastLocation = locationDetails.getLocation(sharedPreferences);




        ListView listView;
        listView = findViewById(R.id.prayer_time_list);
      //  RelativeLayout backGroundLayout= findViewById(R.id.listview_linear_layout);


        setTopButtons();
        set_background(listView);

        drawer();
        setPrayerTimes(lastLocation);
        set_header(lastLocation);

        setCityName(lastLocation);
        setRunningText();



       Prayer_Times_List_Adapter prayer_times_list_adapter = new Prayer_Times_List_Adapter(setArrayList(), this);
       listView.setAdapter(prayer_times_list_adapter);


    }


    public void set_background(ListView mainLayout) {

        ImageView imageView = findViewById(R.id.background);

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour >= 0 && hour < 3) {
            imageView.setImageResource(R.drawable.masjid1);
            mainLayout.setBackground(getResources().getDrawable(R.drawable.gradient_1));
            sharedPreferences.edit().putInt("background_grad", 1).apply();
            // divider.setBackgroundColor(getResources().getColor(R.color.grad_1));

        } else if (hour >= 3 && hour < 6) {
            imageView.setImageResource(R.drawable.masjid2);
            mainLayout.setBackground(getResources().getDrawable(R.drawable.gradient_2));
            sharedPreferences.edit().putInt("background_grad", 2).apply();

        } else if (hour >= 6 && hour < 9) {
            imageView.setImageResource(R.drawable.masjid3);
            mainLayout.setBackground(getResources().getDrawable(R.drawable.gradient_3));
            sharedPreferences.edit().putInt("background_grad", 3).apply();

        } else if (hour >= 9 && hour < 12) {
            imageView.setImageResource(R.drawable.masjid4);
            mainLayout.setBackground(getResources().getDrawable(R.drawable.gradient_4));
            sharedPreferences.edit().putInt("background_grad", 4).apply();


        }
//        else if (hour >= 11 && hour < 13) {
//            imageView.setImageResource(R.drawable.masjid5);
//            mainLayout.setBackground(getResources().getDrawable(R.drawable.gradient_5));
//            sharedPreferences.edit().putInt("background_grad", 5).apply();
//
//
//        }

        else if (hour >= 12 && hour < 16) {
            imageView.setImageResource(R.drawable.masjid6);
            mainLayout.setBackground(getResources().getDrawable(R.drawable.gradient_6));
            sharedPreferences.edit().putInt("background_grad", 6).apply();

        }
//        else if (hour >= 15 && hour < 17) {
//            imageView.setImageResource(R.drawable.masjid7);
//            mainLayout.setBackground(getResources().getDrawable(R.drawable.gradient_7));
//            sharedPreferences.edit().putInt("background_grad", 7).apply();
//
//        }
        else if (hour >= 16 && hour < 19) {
            imageView.setImageResource(R.drawable.masjid8);
            mainLayout.setBackground(getResources().getDrawable(R.drawable.gradient_8));
            sharedPreferences.edit().putInt("background_grad", 8).apply();

        } else if (hour >= 19 && hour < 22) {
            imageView.setImageResource(R.drawable.masjid9);
            mainLayout.setBackground(getResources().getDrawable(R.drawable.gradient_9));
            sharedPreferences.edit().putInt("background_grad", 9).apply();

        } else if (hour >= 22 && hour < 24) {
            imageView.setImageResource(R.drawable.masjid10);
            mainLayout.setBackground(getResources().getDrawable(R.drawable.gradient_10));
            sharedPreferences.edit().putInt("background_grad", 10).apply();

        } else {
            imageView.setImageResource(R.drawable.masjid1);
            mainLayout.setBackground(getResources().getDrawable(R.drawable.gradient_1));
            sharedPreferences.edit().putInt("background_grad", 1).apply();

        }
        Log.i(TAG, "set_background");
    }


    public void setPrayerTimes(Location location) {

        PrayerTimes prayerTimes = getPrayerTimesObject(location);


        fajr_time = prayerTimes.getPrayTime(PrayersType.FAJR);
        sunrise_time = (prayerTimes.getPrayTime(PrayersType.SUNRISE));
        haram1_time = new Date(sunrise_time.getTime()-(SUNRISE_GUARD_MINS*60*1000));
        ishrak_time = new Date(sunrise_time.getTime() + (25 * 60 * 1000));
        zuhr_time = prayerTimes.getPrayTime(PrayersType.ZUHR);
        haram2_time = new Date(zuhr_time.getTime() - (10 * 60 * 1000));

        if (getAsrRule()==0){
            HanafiAsr hanafiAsr= new HanafiAsr(Calendar.getInstance(),location.getLatitude(),location.getLongitude());
            asr_time=new Date(hanafiAsr.getAsrTimestamp());

            asr_time= getActualAsrTimeInMillis(asr_time);
            //asr_time = (prayerTimes.getPrayTime(PrayersType.ASR));

        }

        else
        asr_time = (prayerTimes.getPrayTime(PrayersType.ASR));



        magrib_time = (prayerTimes.getPrayTime(PrayersType.MAGHRIB));
        haram3_time = new Date(magrib_time.getTime() - (10 * 60 * 1000));

        auabin_time = new Date(magrib_time.getTime() + (15 * 60 * 1000));
        isha_time = (prayerTimes.getPrayTime(PrayersType.ISHA));
        tahazzud_time = new Date(isha_time.getTime() + (60 * 60 * 1000));



        fajr_time_string = getTime12hourFormat(fajr_time);
        sunrise_time_string = getTime12hourFormat(sunrise_time);
        haram1_time_string = getTime12hourFormat(haram1_time);
        ishrak_time_string = getTime12hourFormat(ishrak_time);
        haram2_time_string = getTime12hourFormat(haram2_time);
        zuhr_time_string = getTime12hourFormat(zuhr_time);
        asr_time_string = getTime12hourFormat(asr_time);
        haram3_time_string = getTime12hourFormat(haram3_time);
        magrib_time_string = getTime12hourFormat(magrib_time);
        auabin_time_string = getTime12hourFormat(auabin_time);
        isha_time_string = getTime12hourFormat(isha_time);
        tahazzud_time_string = getTime12hourFormat(tahazzud_time);
        Log.i(TAG, "setPrayerTimes");
    }

    public void set_header(Location location) {


        TextView sehri_time, sehri_text;
        TextView iftar_time, iftar_text;
        TextView hijri_date;
        final TextView nextSalat_name_tv;
        final TextView nextSalatTime_hr_min_tv,nextSalatTime_sec_tv;
        final TextView nextSalat_head_tv;

        final ImageButton sehri_alarm,iftar_alarm;

        sehri_alarm= findViewById(R.id.sehri_alarm);
        iftar_alarm= findViewById(R.id.iftar_alarm);

        sehri_time = findViewById(R.id.sehri_time);
        iftar_time = findViewById(R.id.iftar_time);
        sehri_text = findViewById(R.id.sehri_text);
        iftar_text = findViewById(R.id.iftar_text);
        hijri_date = findViewById(R.id.hijri_date);
        nextSalat_name_tv = findViewById(R.id.next_salat_name);
        nextSalatTime_hr_min_tv = findViewById(R.id.next_salat_time);
        nextSalatTime_sec_tv= findViewById(R.id.next_salat_time_2);
        nextSalat_head_tv = findViewById(R.id.next_salat_head);


        nextSalat_name_tv.setTypeface(fonts.bensen());
        nextSalatTime_hr_min_tv.setTypeface(fonts.bensen());
        nextSalat_head_tv.setTypeface(fonts.bensen());

        sehri_time.setTypeface(fonts.bensen());
        iftar_time.setTypeface(fonts.bensen());
        sehri_text.setTypeface(fonts.bensen());
        iftar_text.setTypeface(fonts.bensen());

        sehri_text.setText(R.string.seheri_text);
        iftar_text.setText(R.string.iftar_text);


        hijri_date.setTypeface(fonts.bensen());

        if (getHijriCorrectionFromUser()==100)
        hijri_date.setText(toHijriDate(getHijriCorrectionFromFirebase()));

        else
            hijri_date.setText(toHijriDate(getHijriCorrectionFromUser()));


        //PrayerTimes prayerTimes = getPrayerTimesObject(location);

        final long sehri_time_mills= fajr_time.getTime()-SEHRI_GUARD_MINS*60*1000;
        final long iftar_time_mills= magrib_time.getTime();

        if (System.currentTimeMillis() <= fajr_time.getTime()) {
//            nextSalat_name_tv.setText("ফজর");
//            nextSalatTime_hr_min_tv.setText(get_remaining_time(fajr_time.getTime()));
            Calendar calendar=Calendar.getInstance();
            if (calendar.get(Calendar.HOUR_OF_DAY)>=2 && System.currentTimeMillis() <=sehri_time_mills){
                countDownTimer=getCountDownTimer(sehri_time_mills,
                        fajr_time.getTime(),
                        "সেহেরির শেষ সময়",
                        "ফজর",
                        nextSalat_head_tv,
                        nextSalat_name_tv,
                        nextSalatTime_hr_min_tv,
                        nextSalatTime_sec_tv
                );


            }
            else{
            countDownTimer=getCountDownTimer(fajr_time.getTime(),
                    zuhr_time.getTime(),
                    "ফজর",
                    "যোহর",
                    nextSalat_head_tv,
                    nextSalat_name_tv,
                    nextSalatTime_hr_min_tv,
                    nextSalatTime_sec_tv
            );
            }
        } else if (System.currentTimeMillis() <= zuhr_time.getTime()) {
//            nextSalat_name_tv.setText("যোহর");
//            nextSalatTime_hr_min_tv.setText(get_remaining_time(zuhr_time.getTime()));
           countDownTimer= getCountDownTimer(zuhr_time.getTime(),
                    asr_time.getTime(),
                    "যোহর",
                    "আসর",
                    nextSalat_head_tv,
                    nextSalat_name_tv,
                    nextSalatTime_hr_min_tv,
                    nextSalatTime_sec_tv
            );
        } else if (System.currentTimeMillis() <= asr_time.getTime()) {
//            nextSalat_name_tv.setText("আসর");
//            nextSalatTime_hr_min_tv.setText(get_remaining_time(asr_time.getTime()));

           countDownTimer= getCountDownTimer(asr_time.getTime(),
                    magrib_time.getTime(),
                    "আসর",
                    "মাগরিব",
                    nextSalat_head_tv,
                    nextSalat_name_tv,
                    nextSalatTime_hr_min_tv,
                    nextSalatTime_sec_tv
            );
        } else if (System.currentTimeMillis() <= magrib_time.getTime()) {
//            nextSalat_head_tv.setVisibility(View.GONE);
//            nextSalatTime_sec_tv.setVisibility(View.VISIBLE);
//            nextSalatTime_sec_tv.setTypeface(fonts.bensen());
//
//            nextSalat_name_tv.setText("মাগরিব/ইফতার");
//            new CountDownTimer(magrib_time.getTime()-System.currentTimeMillis(), 1000) {
//
//                public void onTick(long millisUntilFinished) {
//
//                    nextSalatTime_hr_min_tv.setText(get_remaining_time_hour_minute(magrib_time.getTime()));
//                    nextSalatTime_sec_tv.setText(get_remaining_seconds(magrib_time.getTime()));
//                }
//
//                public void onFinish() {
//                    nextSalatTime_hr_min_tv.setText(get_remaining_time(isha_time.getTime()));
//                    nextSalat_name_tv.setText("ঈশা");
//                    nextSalat_head_tv.setVisibility(View.VISIBLE);
//                    nextSalatTime_sec_tv.setVisibility(View.GONE);
//                }
//            }.start();
            countDownTimer=getCountDownTimer(magrib_time.getTime(),
                              isha_time.getTime(),
                             "মাগরিব/ইফতার",
                             "ঈশা",
                              nextSalat_head_tv,
                              nextSalat_name_tv,
                              nextSalatTime_hr_min_tv,
                              nextSalatTime_sec_tv
            );

        } else if (System.currentTimeMillis() <= isha_time.getTime()) {
//            nextSalat_name_tv.setText("ঈশা");
//            nextSalatTime_hr_min_tv.setText(get_remaining_time(isha_time.getTime()));
            countDownTimer=getCountDownTimer(isha_time.getTime(),
                    fajr_time.getTime()+A_DAY_IN_MILLISECONDS,
                    "ঈশা",
                    "ফজর",
                    nextSalat_head_tv,
                    nextSalat_name_tv,
                    nextSalatTime_hr_min_tv,
                    nextSalatTime_sec_tv
            );
        } else {
//            nextSalat_name_tv.setText("ফজর");
//            nextSalatTime_hr_min_tv.setText(get_remaining_time(fajr_time.getTime() + A_DAY_IN_MILLISECONDS));

            countDownTimer=getCountDownTimer(fajr_time.getTime()+A_DAY_IN_MILLISECONDS,
                    zuhr_time.getTime()+A_DAY_IN_MILLISECONDS,
                    "ফজর",
                    "যোহর",
                    nextSalat_head_tv,
                    nextSalat_name_tv,
                    nextSalatTime_hr_min_tv,
                    nextSalatTime_sec_tv
            );

        }

        countDownTimer.start();


        sehri_time.setText(converted_time_string_bengali(getTime12hourFormat(sehri_time_mills)));
        iftar_time.setText(converted_time_string_bengali(getTime12hourFormat(iftar_time_mills)));


        if (sehri_active())  sehri_alarm.setImageResource(R.drawable.alarm_on);
        else  sehri_alarm.setImageResource(R.drawable.alarm_off);
        if (ifatr_active())  iftar_alarm.setImageResource(R.drawable.alarm_on);
        else  iftar_alarm.setImageResource(R.drawable.alarm_off);


        sehri_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {


                if (sehri_active()) {
                    {


                        cancelSehriAlarmDialog(sehri_alarm);
                    }
                } else
                {


                        setSehriAlarmDialog(sehri_time_mills,sehri_alarm);
                }

            }

        });

        iftar_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {


                if (ifatr_active()) {
                    {


                        cancelIftarAlarmDialog(iftar_alarm);
                    }
                } else
                {


                    setIftarAlarmDialog(iftar_time_mills,iftar_alarm);
                }

            }

        });

       // Log.i(TAG, "set_header");
    }


    private void setTopButtons() {
        final FloatingActionButton qibla,cal, tasbih, location,menu,share;

        menu = findViewById(R.id.fab_menu);
        qibla = findViewById(R.id.fab_qibla);
        //cal= findViewById(R.id.fab_cal);
        tasbih = findViewById(R.id.fab_tasbih);
        location = findViewById(R.id.fab_location);
        share=findViewById(R.id.fab_share);



        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP && !isParamsSet()) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) qibla.getLayoutParams();

            params.setMargins(
                    params.leftMargin- ((int) (6 * 3.1)),
                    params.topMargin -((int) (3 * 3.1)),      //Approximate factor to shrink the extra
                    params.rightMargin - ((int) (6* 3.1)),    //spacing by the shadow  to the actual
                    params.bottomMargin- ((int) (3* 3.1)));  //size of the FAB without a shadow

            menu.setLayoutParams(params);
            qibla.setLayoutParams(params);
          //  cal.setLayoutParams(params);
            tasbih.setLayoutParams(params);
            location.setLayoutParams(params);
            share.setLayoutParams(params);
            fab_paramsSet(true);

        }
        menu.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(final View v) {
                                        mDrawerLayout.openDrawer(Gravity.LEFT);
                                    }
                                }
        );

//
//        cal.setOnClickListener(new View.OnClickListener() {
//                                     @Override
//                                     public void onClick(final View v) {
//
//                                        // Intent intent = new Intent(MainActivity.this, RamadanCalendarActivity.class);
//
//                                         Intent intent = new Intent(MainActivity.this, RamadanTabs.class);
//
//                                         startActivity(intent);
//                                         overridePendingTransition(R.anim.fadein,R.anim.fadeout);
//                                     }
//                                 }
//        );


        qibla.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(final View v) {
                                       Intent intent = new Intent(MainActivity.this, QiblaActivity.class);
                                       startActivity(intent);
                                       overridePendingTransition(R.anim.fadein,R.anim.fadeout);
                                   }
                               }
        );

        tasbih.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(final View v) {
                                          Intent intent = new Intent(MainActivity.this, TasbihActivity.class);
                                          startActivity(intent);
                                          overridePendingTransition(R.anim.fadein,R.anim.fadeout);
                                      }
                                  }
        );

        share.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(final View v) {
                                          final String textToShow= "নামাজের সঠিক সময় জানতে ডাউনলোড করুন ";

                                          shareLink(textToShow);
                                      }
                                  }
        );

        location.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(final View v) {


                                            if (!isLocation_already_updated_for_first_time()) {


                                                if (!internet_connection(MainActivity.this))
                                                    connect_to_internet_dialog();
                                                //reConnectGoogleApiClient();
                                                //makeLocationRequest(100);
                                            }
                                            else  if (!check_gps_connection()){
                                                gps_dialog();
                                             }

                                            else {
                                                LocationUpdateListener locationUpdateListener = new LocationUpdateListener() {
                                                    @Override
                                                    public void onLocationUpdated(Location location, FusedLocationProviderClient mFusedLocationClient, LocationCallback locationCallback) {
                                                        mFusedLocationClient.removeLocationUpdates(locationCallback);

                                                        if (!isLocation_already_updated_for_first_time()) {
                                                            setLocation_already_updated_for_first_time(true);

                                                        }
                                                        initializeUI();

                                                    }
                                                };

                                                LocationDetails locationDetails = new LocationDetails(MainActivity.this, MainActivity.this, sharedPreferences, locationUpdateListener);
                                                setCityName(locationDetails.getLocation(sharedPreferences));

                                                Toast.makeText(MainActivity.this, "আপনার সর্বশেষ অবস্থান " + sharedPreferences.getString("city", "নির্ণয় করা হয়েছে "), Toast.LENGTH_LONG).show();
                                            }


                                        }
                                    }
        );
    }



    private void fab_paramsSet(boolean b) {
        sharedPreferences.edit().putBoolean("params_set",b).apply();
    }

    private  boolean isParamsSet(){

      return   sharedPreferences.getBoolean("params_set",false);
    }


    private void setRunningText() {
        final TextView current_salat_state;
        current_salat_state = findViewById(R.id.current_state);
        current_salat_state.setTypeface(fonts.bensen());
        current_salat_state.setText(getCurrent_Salat());
        final ValueAnimator animator = ValueAnimator.ofFloat(1.0f, -1.0f);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(7000L);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final float progress = (float) animation.getAnimatedValue();
                final float width = current_salat_state.getWidth();
                final float translationX = width * progress;
                current_salat_state.setTranslationX(translationX);
                // second.setTranslationX(translationX - width);
            }
        });
        animator.start();

    }

    public void setCityName(Location location) {

        TextView location_txt;

        location_txt = findViewById(R.id.textView2);

        location_txt.setTypeface(fonts.FutureCndNormal());


        Log.e(TAG, "Set_city");

        String cityName;
        Geocoder gcd = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses.size() > 0) {
                cityName = addresses.get(0).getLocality();

                saveCity(cityName);
                Log.e(TAG, "city_string_on");

            } else {
                cityName = sharedPreferences.getString("city", "Not Found");
            }

        } catch (Exception e) {
            e.printStackTrace();
            cityName = getCity();

        }
        String s = cityName;
        if (s == null) {
            Log.i(TAG, "city_string_null");
            s = getCity();


        }


        location_txt.setText(s);

    }

    public PrayerTimes getPrayerTimesObject(Location location) {
        GregorianCalendar date = new GregorianCalendar();
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"),
                Locale.getDefault());
        Date currentLocalTime = calendar.getTime();
        DateFormat date1 = new SimpleDateFormat("Z");
        String localTime = date1.format(currentLocalTime);
        double tz;
        try {
            tz = Double.parseDouble(localTime);

        } catch (NumberFormatException nf) {
            tz = +0600;   //timezone for dhaka, this is the common way to write tz, not a typo

        }


        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        double altitude = location.getAltitude();

        int i = getRule();
        PrayerTimes prayerTimes;
        if (i == 0) {
            prayerTimes = new TimeCalculator().date(date).location(latitude, longitude, tz,
                    altitude).timeCalculationMethod(MWL).calculateTimes();
        } else if (i == 1) {
            prayerTimes = new TimeCalculator().date(date).location(latitude, longitude, tz,
                    altitude).timeCalculationMethod(ISNA).calculateTimes();
        } else if (i == 2) {
            prayerTimes = new TimeCalculator().date(date).location(latitude, longitude, tz,
                    altitude).timeCalculationMethod(EGYPT).calculateTimes();
        } else if (i == 3) {
            prayerTimes = new TimeCalculator().date(date).location(latitude, longitude, tz,
                    altitude).timeCalculationMethod(MUHAMMADIYAH).calculateTimes();
        } else {
            prayerTimes = new TimeCalculator().date(date).location(latitude, longitude, tz,
                    altitude).timeCalculationMethod(KARACHI).calculateTimes();
        }
        return prayerTimes;
    }

    public ArrayList<DataModel> setArrayList() {
        ArrayList<DataModel> dataModels = new ArrayList<>();
        dataModels.add(new DataModel("Fajr", fajr_time_string, fajr_time, 1, sunrise_time));
        dataModels.add(new DataModel("Sun", sunrise_time_string, sunrise_time, 2, zuhr_time));
        dataModels.add(new DataModel("Haram_1", haram1_time_string, haram1_time, 3, ishrak_time));
        dataModels.add(new DataModel("Ishrak", ishrak_time_string, ishrak_time, 4, haram2_time));
        dataModels.add(new DataModel("Haram_2", haram2_time_string, haram2_time, 5, zuhr_time));
        dataModels.add(new DataModel("Zuhr", zuhr_time_string, zuhr_time, 6, asr_time));
        dataModels.add(new DataModel("Asr", asr_time_string, asr_time, 7, magrib_time));
        dataModels.add(new DataModel("Haram_3", haram3_time_string, haram3_time, 8, magrib_time));
        dataModels.add(new DataModel("Magrib", magrib_time_string, magrib_time, 9, isha_time));
        dataModels.add(new DataModel("Auabin", auabin_time_string, auabin_time, 10, isha_time));
        dataModels.add(new DataModel("Esha", isha_time_string, isha_time, 11, fajr_time));
        dataModels.add(new DataModel("Tahazzud", tahazzud_time_string, tahazzud_time, 12, fajr_time));
        Log.i(TAG, "setarraylist");
        return dataModels;
    }


    private void set_azan_off() {
        sharedPreferences.edit().putBoolean("notification", false).apply();
        try {
            if (!isFinishing() && dialog.isShowing())
                dialog.dismiss();
        } catch (NullPointerException n) {
            n.printStackTrace();
        }
    }

    private boolean azan_running() {
        return sharedPreferences.getBoolean("notification", false);
    }

    public String getTime12hourFormat(Date date) {
        SimpleDateFormat localDateFormat = new SimpleDateFormat("hh:mm", Locale.US);
        String time = localDateFormat.format(date);
        Log.i(TAG, "gettime12hourformat");
        return time;
    }

    public Date getActualAsrTimeInMillis(Date asr_time){

        SimpleDateFormat localDateFormat = new SimpleDateFormat("hh:mm", Locale.US);

       Calendar calendar= Calendar.getInstance();
        int hr= asr_time.getHours();
        int minute= asr_time.getMinutes();

        calendar.set(Calendar.HOUR_OF_DAY,hr);
        calendar.set(Calendar.MINUTE,minute);

        Log.i(TAG, "gettimemillis");

        return  calendar.getTime();


    }

    public String getTime12hourFormat(long mills) {
        SimpleDateFormat localDateFormat = new SimpleDateFormat("hh:mm", Locale.US);
        String time = localDateFormat.format(mills);
        Log.i(TAG, "gettime12hourformat");
        return time;
    }

    public String converted_time_string_bengali(String string) {
        string = string.replaceAll("0", "০");
        string = string.replaceAll("1", "১");
        string = string.replaceAll("2", "২");
        string = string.replaceAll("3", "৩");
        string = string.replaceAll("4", "৪");
        string = string.replaceAll("5", "৫");
        string = string.replaceAll("6", "৬");
        string = string.replaceAll("7", "৭");
        string = string.replaceAll("8", "৮");
        string = string.replaceAll("9", "৯");
        string = string.replaceAll("AM", "");
        string = string.replaceAll("PM", "");
        Log.i(TAG, "converted_time_string_bengali");
        return string;
    }


    public void set_alarm_to_check_active_alarms() {
        Calendar calendar = Calendar.getInstance();
        int hour_now = calendar.get(Calendar.HOUR_OF_DAY);
        int waiting_hours = 24 - hour_now;
        //calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 1);
        calendar.set(Calendar.MINUTE, 1);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, DailyReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + (waiting_hours * 60 * 60 * 1000) + (30 * 60 * 1000),
                AlarmManager.INTERVAL_DAY, pendingIntent);
//        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
//                AlarmManager.INTERVAL_DAY, pendingIntent);
        sharedPreferences.edit().putBoolean("daily_check_set", true).apply();
        Log.i(TAG, "set_alarm_to_check_active_alarms");
    }

    public void set_daily_checker() {

        Log.e(TAG, "set_alarm_to_check_active_alarms");

        Calendar calendar = Calendar.getInstance();
        long time;
        int tag=0;


        if (calendar.get(Calendar.HOUR_OF_DAY)<1|| (calendar.get(Calendar.HOUR_OF_DAY)==1 && calendar.get(Calendar.MINUTE)<1) )

        {
            calendar.set(Calendar.HOUR_OF_DAY, 1);
            calendar.set(Calendar.MINUTE, 1);

            time = calendar.getTimeInMillis();


        }

        else {

            calendar.set(Calendar.HOUR_OF_DAY, 1);
            calendar.set(Calendar.MINUTE, 1);

            time = calendar.getTimeInMillis()+24*60*60*1000;

        }

        Intent intentAlarm = new Intent(this, DailyReciever.class);


        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(this, tag, intentAlarm, 0));
            }

            else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(this, tag, intentAlarm, 0));
            }

            else {

                alarmManager.set(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(this, tag, intentAlarm, 0));

            }

            sharedPreferences.edit().putBoolean("daily_check_set", true).apply();

        }
     //   Toast.makeText(this, "অ্যালার্ম সেট করা হয়েছে", Toast.LENGTH_SHORT).show();


    }


    public String toHijriDate(int correction){
        ArabicDate arabicDate= new ArabicDate(correction);

        return converted_time_string_bengali(arabicDate.getArabDateString());

    }

    public String hijri_date() {
        DateTimeZone datetimeZone = DateTimeZone.forID("Asia/Dhaka");
        Chronology iso = ISOChronology.getInstance(datetimeZone);
        Chronology hijri = IslamicChronology.getInstance(datetimeZone,
                IslamicChronology.LEAP_YEAR_INDIAN);
        LocalDate todayIso = new LocalDate(LocalDate.now(), iso);
        Log.i(TAG, todayIso.toString());


        LocalDate todayHijri = new LocalDate(todayIso.toDateTimeAtStartOfDay(),
                hijri);
        int date = todayHijri.getDayOfMonth();
        int month = todayHijri.getMonthOfYear();
        int year = todayHijri.getYear();
        String month_name = null;
        switch (month) {
            case 1:
                month_name = "মুহররম";
                break;
            case 2:
                month_name = "সফর";
                break;
            case 3:
                month_name = "রবিউল আউয়াল";
                break;
            case 4:
                month_name = "রবিউস সানি";
                break;
            case 5:
                month_name = "জমাদিউল আউয়াল";
                break;
            case 6:
                month_name = "জমাদিউস সানি";
                break;
            case 7:
                month_name = "রজব";
                break;
            case 8:
                month_name = "শাবান";
                break;
            case 9:
                month_name = "রমজান";
                break;
            case 10:
                month_name = "শাওয়াল";
                break;
            case 11:
                month_name = "জিলক্বদ";
                break;
            case 12:
                month_name = "জিলহজ্জ";
                break;
        }

        String d = converted_time_string_bengali(Integer.toString(date));
        String y = converted_time_string_bengali(Integer.toString(year));
        String hijri_date_string = d + "-" + month_name + ", " + y + " হিজরি";
        Log.i(TAG, "current_salat_state");

        return hijri_date_string;
    }


    public boolean internet_connection(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = null;
        NetworkInfo mobileInfo = null;
        if (connectivityManager != null) {
            wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        }
        if ((wifiInfo != null && wifiInfo.isConnected()) || (mobileInfo != null && mobileInfo.isConnected())) {
            Log.i(TAG, "internet_on");
            return true;
        } else {
            Log.i(TAG, "internet_off");
            return false;
        }
    }

    private void connect_to_internet_dialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("যথাযথ সময় জানার জন্য ইন্টারনেট এ সংযোগ করুন")
                .setCancelable(false)
                .setPositiveButton("ওয়াইফাই", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        dialog.dismiss();
                    }
                })
                .setNeutralButton("ডাটা সংযোগ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS));
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("বাদ দিন", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        internetDialog = builder.create();
        internetDialog.show();
        Log.i(TAG, "connect_internet_dialog");
    }

    private boolean check_gps_connection() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        try {
            if (lm != null) {
                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return gps_enabled;
    }

    private void gps_dialog() {
        // Log.i(TAG, "gps_dialog");

       try {
           gpsDialog = new AlertDialog.Builder(MainActivity.this)
                   .setMessage("আপনার অবস্থান জানতে জিপিএস চালু করুন")
                   .setPositiveButton("ঠিক আছে", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           Intent gpsOptionsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                           startActivity(gpsOptionsIntent);


                       }
                   })
                   .setNegativeButton("বাদ দিন", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {


                       }
                   })
                   .show();
       }

       catch (Exception e){
           e.printStackTrace();
       }

        //  builder.show();

        //  Log.i(TAG, "connect_gps_dialog");
    }


    void drawer() {
        mDrawerLayout =  findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
               R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                //syncActionBarArrowState();
                try {
                    getActionBar();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void onDrawerOpened(View drawerView) {
                mDrawerToggle.setDrawerIndicatorEnabled(true);
                try {
                    getActionBar().hide();
                } catch (Exception e) {
                }
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        NavigationView navigationView =  findViewById(R.id.nav_view);
        navigationView.setBackground(Utility.getBackgroundGradient(this));

        navigationView.setNavigationItemSelectedListener(MainActivity.this);
        // Log.i(TAG, "drawer");

    }
    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.START)) {
            mDrawerLayout.closeDrawers();
            return;
        }
        SharedPreferences pref = getSharedPreferences(Utility.SHARED_PREF_NAME, MODE_PRIVATE);
        int my_counter = pref.getInt("count_ads", 0);
        SharedPreferences.Editor editor = pref.edit();
        my_counter++;
        editor.putInt("count_ads", my_counter);
        editor.apply();
        //if(getSharedPreferences(Utility.SHARED_PREF_NAME,MODE_PRIVATE).getBoolean("initialized",false) &&my_counter%3==0 ){
        /*if(my_counter%3==0){

            Intent intent = new Intent(this, AdActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


            startActivityForResult(intent,1);

            //finish();

            return;
        }*/
        Log.e("my counter", "" + my_counter);

        AlertDialog.Builder alert = new AlertDialog.Builder(
                MainActivity.this);
        if (my_counter % 3 != 0) {
            alert.setTitle("আমাদের অ্যাপ স্টোর");
            alert.setIcon(R.mipmap.ic_launcher); //app icon here
            alert.setMessage("আকর্ষণীয় ফ্রী অ্যাপস পেতে স্টোর ভিজিট করুন!");
            alert.setPositiveButton("ফিরে যান",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            //Do nothing
                        }
                    });
            alert.setNegativeButton("বন্ধ করুন",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });

            alert.setNeutralButton("আমাদের স্টোর",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            try {
                                Uri uri = Uri.parse("market://search?q=pub:" + Utility.PUBLISHER_NAME);
                                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                                // To count with Play market backstack, After pressing back button,
                                // to taken back to our application, we need to add following flags to intent.

                                startActivity(goToMarket);
                            } catch (android.content.ActivityNotFoundException anfe) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + PACKAGENAME)));
                            }
                        }
                    });
            if (!isFinishing())
                alert.show();

        } else {
            alert.setTitle("রেটিং দিন");
            alert.setIcon(R.mipmap.ic_launcher); //app icon here
            alert.setMessage(Utility.RATE_MESSAGE);
            alert.setPositiveButton("ফিরে যান",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            //Do nothing
                        }
                    });
            alert.setNegativeButton("বন্ধ করুন",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });

            alert.setNeutralButton("রেটিং দিন",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW,
                                        Uri.parse("market://details?id=" + PACKAGENAME
                                        )));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                startActivity(new Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("http://play.google.com/store/apps/details?id=" + PACKAGENAME)));
                            }
                        }
                    });
            if (!isFinishing())
                alert.show();
        }
    }

    public int getRule() {
        //Log.i(TAG, "get_rule" + sharedPreferences.getInt("rule", 4));
        return sharedPreferences.getInt("rule", 4);
    }

    public void setRule(int rule) {
        // Log.i(TAG, "set_rule" + rule);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("rule", rule);
        editor.apply();
    }

    public int getAsrRule() {
        //Log.i(TAG, "get_rule" + sharedPreferences.getInt("rule", 4));
        return sharedPreferences.getInt("asr_rule", 0);
    }

    public void setAsrRule(int rule) {
        // Log.i(TAG, "set_rule" + rule);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("asr_rule", rule);
        editor.apply();
    }

    public void saveCity(String city) {
        // Log.i(TAG, "save_city" + city);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("city", city);
        editor.apply();
    }

    public String getCity() {
        //Log.i(TAG, "get_rule" + sharedPreferences.getString("city", null));

        try {
            return sharedPreferences.getString("city", null);

        } catch (Exception e) {
            return "bangladesh";
        }
    }


    public boolean isLocation_already_updated_for_first_time() {
        return sharedPreferences.getBoolean("Location_already_updated_for_first_time", false);
    }

    public void setLocation_already_updated_for_first_time(boolean bool) {
        Log.i(TAG, "Location_updated" + bool);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("Location_already_updated_for_first_time", bool);
        editor.apply();
    }


    public String get_remaining_time(long nextSalatTime) {
        long millis = nextSalatTime - System.currentTimeMillis();
        int hours = (int) (millis / (1000 * 60 * 60));
        int minutes = (int) (millis / (1000 * 60)) - hours * 60;

        String remaining;
        if (hours==0)   remaining =  minutes + " মিনিট পর ";

        else remaining = hours + " ঘণ্টা " + minutes + " মিনিট পর ";

        return converted_time_string_bengali(remaining);
    }

    public String get_remaining_time_hour_minute(long nextSalatTime) {
        long millis = nextSalatTime - System.currentTimeMillis();
        int hours = (int) (millis / (1000 * 60 * 60));
        int minutes = (int) (millis / (1000 * 60)) - hours * 60;

        String remaining;
        if (hours==0)   remaining =  minutes + " মিনিট ";

        else remaining = hours + " ঘণ্টা " + minutes + " মিনিট ";

        return converted_time_string_bengali(remaining);
    }

    public String get_remaining_seconds(long nextSalatTime) {
        long millis = nextSalatTime - System.currentTimeMillis();
        int hours = (int) (millis / (1000 * 60 * 60));
        int minutes = (int) (millis / (1000 * 60)) - hours * 60;
        int seconds= (int) (millis / (1000 )) - minutes*60-hours*60*60;

        String remaining;
//        if (hours==0)   remaining =  minutes + " মিনিট " + seconds+" সেকেন্ড পর ";
//
//        else remaining = hours + " ঘণ্টা " + minutes + " মিনিট " +seconds+ " সেকেন্ড পর ";
        if (hours==0)   remaining = seconds+" সেকেন্ড পর ";

        else remaining = seconds+ " সেকেন্ড পর ";

        return converted_time_string_bengali(remaining);
    }

    public String getCurrent_Salat() {
        String current;
        int gap = 15 * 60 * 1000;


        long current_time = System.currentTimeMillis();
        long tahazzud_last_time;
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.HOUR,23);
        calendar.set(Calendar.MINUTE,59);
        calendar.set(Calendar.SECOND,59);
        long midnight=calendar.getTimeInMillis();


        tahazzud_last_time = fajr_time.getTime()  - gap;


        if (current_time >= fajr_time.getTime() && current_time <= haram1_time.getTime()) {
            current = "ফজরের ওয়াক্ত চলছে ";
        } else if (current_time > haram1_time.getTime() && current_time < ishrak_time.getTime()) {
            current = "সূর্যোদয় হচ্ছে,নামাজ পড়া নিষিদ্ধ ";
        } else if (current_time >= ishrak_time.getTime() && current_time < haram2_time.getTime()) {
            current = "ইশরাক এর ওয়াক্ত চলছে ";
        } else if (current_time >= haram2_time.getTime() && current_time < zuhr_time.getTime()) {
            current = "এই মুহূর্তে নামাজ পড়া নিষিদ্ধ,পরবর্তী সালাত যোহর ";
        } else if (current_time >= zuhr_time.getTime() && current_time < asr_time.getTime() - gap) {
            current = "যোহরের ওয়াক্ত চলছে ";
        } else if (current_time >= asr_time.getTime() - gap && current_time < asr_time.getTime()) {
            current = "আসরের ওয়াক্ত সন্নিকটে ";
        } else if (current_time >= asr_time.getTime() && current_time < haram3_time.getTime()) {
            current = "আসরের ওয়াক্ত চলছে ";
        } else if (current_time >= haram3_time.getTime() && current_time < magrib_time.getTime()) {
            current = "সূর্যাস্তের সময় চলছে,নামাজ পড়া নিষিদ্ধ,মাগরিব সন্নিকটে ";
        } else if (current_time >= magrib_time.getTime() && current_time < auabin_time.getTime()) {
            current = "মাগরিবের ওয়াক্ত চলছে ";
        } else if (current_time >= auabin_time.getTime() && current_time < isha_time.getTime() - gap) {
            current = "মাগরিবের ওয়াক্ত চলছে, আউয়াবিন পড়ে নিতে পারেন ";
        } else if (current_time >= isha_time.getTime() - gap && current_time < isha_time.getTime()) {
            current = "এশার ওয়াক্ত সন্নিকটে ";
        } else if (current_time >= isha_time.getTime() && current_time < isha_time.getTime()+3*60*60*1000) {
            current = "এশার ওয়াক্ত চলছে ";
        } else if (current_time >= isha_time.getTime()+3*60*60*1000 && current_time <= midnight) {
            current = "তাহাজ্জুদ পড়ার উত্তম সময় চলছে ";
        } else if (current_time < tahazzud_last_time) {
            current = "তাহাজ্জুদ পড়ার উত্তম সময় চলছে ";
        } else if (current_time >= tahazzud_last_time && current_time < fajr_time.getTime()) {
            current = "ফজরের ওয়াক্ত সন্নিকটে  ";
        } else current = "";

        Log.e("problem", zuhr_time.getTime()+" asr "+asr_time.getTime()+" magrib"+magrib_time.getTime()+" ");

        return current;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case permission: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    LocationUpdateListener locationUpdateListener = new LocationUpdateListener() {
                        @Override
                        public void onLocationUpdated(Location location, FusedLocationProviderClient mFusedLocationClient, LocationCallback locationCallback) {
                            mFusedLocationClient.removeLocationUpdates(locationCallback);


                            if (!isLocation_already_updated_for_first_time()) {
                                setLocation_already_updated_for_first_time(true);

                            }
                            initializeUI();
                        }
                    };

                    if (!isLocation_already_updated_for_first_time()) {

                        if (!internet_connection(this)) connect_to_internet_dialog();
                        if (!check_gps_connection()) gps_dialog();
                    }
                    LocationDetails locationDetails = new LocationDetails(MainActivity.this, MainActivity.this, sharedPreferences, locationUpdateListener);
                    setCityName(locationDetails.getLocation(sharedPreferences));

                    Log.e("req", "granted");


                }
            }


        }
    }


    void FCM() {
        //notification fcm
        if (getIntent().getExtras() != null) {
            String pack = (String) getIntent().getExtras().get("1");
            if (pack != null) {
                Log.e("fcm main pack", pack);
                try {
                    Uri uri = Uri.parse("market://details?id=" + pack);
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    // To count with Play market backstack, After pressing back button,
                    // to taken back to our application, we need to add following flags to intent.
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                              startActivity(goToMarket);

                    }

                    else {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + pack)));

                    }
                } catch (ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + pack)));
                }
            }
        } else Log.e("fcm main", "null");
    }

//    void ADS(final boolean app_is_run_from_notification) {
//
////        mInterstitialAd = new InterstitialAd(this);
////        mInterstitialAd.setAdUnitId(Utility.INTERSTITIAL);
////        mInterstitialAd.setAdListener(new AdListener() {
////            @Override
////            public void onAdClosed() {
////                //requestNewInterstitial();
////            }
////
////            @Override
////            public void onAdLoaded() {
////                super.onAdLoaded();
////                //showAd();
////                Utility.counter(getApplication(), (ShowAdInterface) MainActivity.this);
////            }
////        });
//
//
//       //Todo fb interstitial
//        fb_interstitialAd = new InterstitialAd(this, "YOUR_PLACEMENT_ID");
//
//        fb_interstitialAd.setAdListener(new InterstitialAdListener() {
//            @Override
//            public void onInterstitialDisplayed(Ad ad) {
//                // Interstitial displayed callback
//            }
//
//            @Override
//            public void onInterstitialDismissed(Ad ad) {
//                // Interstitial dismissed callback
//            }
//
//            @Override
//            public void onError(Ad ad, AdError adError) {
//                // Ad error callback
////                Toast.makeText(MainActivity.this, "Error: " + adError.getErrorMessage(),
////                        Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onAdLoaded(Ad ad) {
//                // Show the ad when it's done loading.
//                //todo fb inter showed here
//
//                    fb_interstitialAd.show();
//
//
//            }
//
//            @Override
//            public void onAdClicked(Ad ad) {
//                // Ad clicked callback
//            }
//
//            @Override
//            public void onLoggingImpression(Ad ad) {
//                // Ad impression logged callback
//            }
//        });
//
//        // For auto play video ads, it's recommended to load the ad
//        // at least 30 seconds before it is shown
//        //fb_interstitialAd.loadAd();
//
//
//        if(!app_is_run_from_notification && count%2==0){
//            //todo fb inter requested here
//
//            requestNewInterstitial();
//        }
//
//
//
//
//        /*DisplayMetrics displayMetrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        int height = displayMetrics.heightPixels;
//        int width = displayMetrics.widthPixels;
//        Log.e("metrics","height:"+height+"...width:"+width);
//        //fb banner
//        if(width>=720)
//            adView = new AdView(this, Utility.FACEBOOK_BANNER, AdSize.BANNER_HEIGHT_90);
//        else
//            adView = new AdView(this, Utility.FACEBOOK_BANNER, AdSize.BANNER_HEIGHT_50);
//        */
////        if(getResources().getDisplayMetrics().density>2)//xhdpi
////            adView = new AdView(this, Utility.FACEBOOK_BANNER, AdSize.BANNER_HEIGHT_90);
////        else
////            adView = new AdView(this, Utility.FACEBOOK_BANNER, AdSize.BANNER_HEIGHT_50);
////
////        // Find the Ad Container
////        LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container);
////        // Add the ad view to your activity layout
////        adContainer.addView(adView);
////        // Request an ad
////        adView.loadAd();
//
//        mAdView = (com.google.android.gms.ads.AdView) findViewById(R.id.adView);
//
//        adRequest = new AdRequest.Builder().build();
//
//
//        mAdView.loadAd(adRequest);
//    }

    void ADS(final boolean app_is_run_from_notification) {

        Log.e("onresume", "in it add");

        mInterstitialAd = new com.google.android.gms.ads.InterstitialAd(this);
        mInterstitialAd.setAdUnitId(Utility.INTERSTITIAL);
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                //requestNewInterstitial();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();

                showAd();



            }


        });


        if (!app_is_run_from_notification && getCount()%5==0)
        {

            requestNewInterstitial();


        }

        sharedPreferences.edit().putInt("int_count",getCount()+1).apply();



        com.google.android.gms.ads.AdView mAdView =  findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest
                .Builder()
                .addTestDevice("4B5A675D586A6E33634EE7EF503BDE73")
                .build();



        mAdView.loadAd(adRequest);

    }


    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest
                .Builder()
                .addTestDevice("4B5A675D586A6E33634EE7EF503BDE73")
                .build();
        mInterstitialAd.loadAd(adRequest);
        //     fb_interstitialAd.loadAd();
    }


    @Override
    public boolean showAd() {
        if (mInterstitialAd.isLoaded() && !isFinishing() && has_focus ) {
            mInterstitialAd.show();
            Log.e("inter", "has shown");
            return true;
        } else
            Log.e("inter", "not shown");

        return false;
    }


    @Override
    public void onDestroy() {
//        if (mAdView != null) {
//            mAdView.destroy();
//        }


        super.onDestroy();
        setVisible(false);
        //set_azan_off();
    }

    private void create_cancel_option_in_activity() {


        if (dialog != null && dialog.isShowing())
            return;

        dialog = new AlertDialog.Builder(this)

                .setTitle("আজান চলছে । আপনি কি আজান বন্ধ করতে চান?")
                .setPositiveButton("হ্যাঁ", null) //Set to null. We override the onclick
                .setNegativeButton("না", null)
                .create();

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(final DialogInterface dialog) {

                Button yes = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                yes.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        Intent dismiss_button_Intent = new Intent(MainActivity.this, DismissButtonReceiver.class);
                        dismiss_button_Intent.putExtra("notificationId", sharedPreferences.getInt("notificationId", 0));
                        sendBroadcast(dismiss_button_Intent);
                        set_azan_off();

                        dialog.dismiss();
                        //onResume();


                        //finish();
                    }
                });

                Button no = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
                no.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();

                    }
                });
            }
        });


        if (!azan_running()) {
            dialog.dismiss();
        } else if (!isFinishing())
            dialog.show();


    }


    private void appOpeningCount() {


        int count = sharedPreferences.getInt("count", 0);
        count++;
        sharedPreferences.edit().putInt("count", count).apply();
    }
    public Location getLocation(SharedPreferences sharedPreferences) {
        String lat = sharedPreferences.getString("LOCATION_LAT", "23.6850");
        String lon = sharedPreferences.getString("LOCATION_LON", "90.3563");
        Location location;
        String provider = sharedPreferences.getString("LOCATION_PROVIDER", "myLocationProvider");
        location = new Location(provider);
        location.setLatitude(Double.parseDouble(lat));
        location.setLongitude(Double.parseDouble(lon));

        Log.e(TAG,"getLocation"+ location.toString());

        return location;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_rating) {
            try {
                Uri uri = Uri.parse("market://details?id=" + PACKAGENAME);
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                startActivity(goToMarket);
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + PACKAGENAME)));
            }
        } else if (id == R.id.nav_share) {
            Intent intent = new Intent(this, ShareActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_fb_page) {

            try {
                String url = Utility.HASBUNALLAH_GROUP;
                Log.e("fb uri", url);
                Intent i = Utility.newFacebookIntent(getPackageManager(), url);//new Intent(Intent.ACTION_VIEW);
                //i.setData(Uri.parse(url));
                startActivity(i);
            } catch (android.content.ActivityNotFoundException anfe) {
                anfe.printStackTrace();
            }

        } else if (id == R.id.nav_app_store) {
            try {
                Uri uri = Uri.parse("market://search?q=pub:" + Utility.PUBLISHER_NAME);
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                startActivity(goToMarket);
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + Utility.PACKAGE_NAME)));
            }
        }
        else if (id == R.id.hasbunallah) {
            try {
                String url = Utility.HASBUNALLAH_PAGE;
                Intent i = Utility.newFacebookIntent(getPackageManager(), url);//new Intent(Intent.ACTION_VIEW);
                startActivity(i);
            } catch (android.content.ActivityNotFoundException anfe) {
                anfe.printStackTrace();
            }
        }

        else if (id == R.id.quran_app) {
            try {
                Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.creativeapps.banglaquran");
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                startActivity(goToMarket);
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.creativeapps.banglaquran")));
            }
        } else if (id == R.id.talking_clock) {
            try {
                Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.creativeapps.talking_clock");
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                startActivity(goToMarket);
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.creativeapps.talking_clock")));
            }
        } else if (id == R.id.settings) {
            final AlertDialog.Builder builderSingle = new AlertDialog.Builder(MainActivity.this);
            builderSingle.setTitle("নিয়ম");
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.select_dialog_singlechoice);

            arrayAdapter.add("মুস্লিম ওয়ার্ল্ড লীগ");
            arrayAdapter.add("I.S.N.A. (ইস্লামিক সোসাইটি অফ নর্থ আমেরিকা)");
            arrayAdapter.add("ইজিপসিয়ান জেনারেল অথরিটি অফ সার্ভ");
            arrayAdapter.add("উম্মুল কুরা ইউনিভার্সিটি,মক্কা");
            arrayAdapter.add("ইউনিভার্সিটি অফ ইসলামিক স্টাডিজ, করাচী");

            builderSingle.setSingleChoiceItems(arrayAdapter, getRule(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.e("Which", which + "");


                    setRule(which);

                }
            });
            builderSingle.setNegativeButton("ঠিক আছে", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();


                    onResume();
                    mDrawerLayout.closeDrawers();

                }
            });




            builderSingle.show();
        }

        else if (id == R.id.asr_settings) {
            final AlertDialog.Builder builderSingle = new AlertDialog.Builder(MainActivity.this);
            builderSingle.setTitle("পদ্ধতি");
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.select_dialog_singlechoice);

            arrayAdapter.add("হানাফি মাযহাব");
            arrayAdapter.add("শাফেয়ী, হাম্বলী, মালিকী মাযহাব");


                    builderSingle.setSingleChoiceItems(arrayAdapter, getAsrRule(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                  //  Log.e("Which", which + "");


                    setAsrRule(which);


                }
            });
            builderSingle.setNegativeButton("ঠিক আছে", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();


                    onResume();
                    mDrawerLayout.closeDrawers();

                }
            });






            builderSingle.show();
        }

        else if (id == R.id.hijri_fix) {
            final AlertDialog.Builder builderSingle = new AlertDialog.Builder(MainActivity.this);
            builderSingle.setTitle("হিজরি তারিখ সংশোধন করুন");
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.select_dialog_singlechoice);

            arrayAdapter.add("১ দিন যোগ করুন");
            arrayAdapter.add("২ দিন যোগ করুন");
            arrayAdapter.add("যেমন আছে তেমন ই থাকবে");
            arrayAdapter.add("১ দিন বিয়োগ করুন");
            arrayAdapter.add("২ দিন বিয়োগ করুন");



            builderSingle.setSingleChoiceItems(arrayAdapter, getHijriCorrectionIndex(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //  Log.e("Which", which + "");


                    setHijriCorrectionFromUser(which);


                }
            });






            builderSingle.setNegativeButton("ঠিক আছে", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();


                    onResume();
                    mDrawerLayout.closeDrawers();

                }
            });






            builderSingle.show();
        }

        else if (id==R.id.nav_five_alarms){




            new AlertDialog.Builder(MainActivity.this)
                    .setMessage("পাঁচ ওয়াক্ত ফরজ সালাত এর জন্য দৈনিক আজান সেট করুন")
                    .setPositiveButton("ঠিক আছে", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            sharedPreferences.edit().putBoolean("five_alarms",true).apply();

                            Toast.makeText(MainActivity.this, "পাঁচ ওয়াক্তের জন্য আজান সেট করা হয়েছে ", Toast.LENGTH_LONG).show();

                            onResume();
                            mDrawerLayout.closeDrawers();

                        }
                    })
                    .setNegativeButton("বাদ দিন", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();







        }

      else  if (id==R.id.nav_alarm_repeat){


            final AlertDialog.Builder builderSingle = new AlertDialog.Builder(MainActivity.this);
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.select_dialog_singlechoice);

            arrayAdapter.add("১ বার");
            arrayAdapter.add("২ বার");
            arrayAdapter.add("৩ বার");
            arrayAdapter.add("৪ বার");
            arrayAdapter.add("৫ বার");

            builderSingle.setSingleChoiceItems(arrayAdapter, getRepeat(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                   // Log.e("Which", which + "");

                                setRepeat(which);
                    Toast.makeText(MainActivity.this, (which+1)+" বার রিপিট সেট করা হয়েছে ", Toast.LENGTH_LONG).show();

                }
            });
            builderSingle.setNegativeButton("ঠিক আছে", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
//                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(intent);

                    onResume();
                    mDrawerLayout.closeDrawers();

                }
            });



            builderSingle.show();



        }


     //   Log.i(TAG, "navigation_item_selected");
        return false;
    }





    private void shareLink(String textToShow)
    {
//

        String show= textToShow+Utility.APP_LINK+"\n";

        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");


        share.putExtra(Intent.EXTRA_TEXT, show);

        startActivity(Intent.createChooser(share, "Share App"));


    }

    private void shareOnFacebook(String textToShare) {
        try {
            ShareDialog shareDialog = new ShareDialog(this);

            if (ShareDialog.canShow(ShareLinkContent.class)) {
                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=" + PACKAGENAME))
                        // .setContentTitle(textToShare)
                        .setQuote(textToShare)
                        .build();
                shareDialog.show(linkContent);
            }
            //dialog1.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openShareDialog() {
        final Dialog dialog = new Dialog(this);
        final String textToShow= "নামাজের সঠিক সময় জানতে ডাউনলোড করুন";



        dialog.setContentView(R.layout.dialog_share);
        //dialog.setTitle(“BMI calculator”);




        Button facebook=dialog.findViewById(R.id.share_fb);
        Button gplus=dialog.findViewById(R.id.share_gplus);
        Button share=dialog.findViewById(R.id.share_others);


        facebook.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        shareOnFacebook(textToShow);

                        dialog.dismiss();

                    }
                }
        );


        gplus.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent shareIntent = new PlusShare.Builder(MainActivity.this)
                                .setType("text/plain")
                                .setText(textToShow)
                                .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=" + PACKAGENAME))
                                .getIntent();

                        startActivityForResult(shareIntent, 0);
                        dialog.dismiss();

                    }
                }
        );

        share.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Utility.shareToAnyThing(textToShow,"Share With Friends",MainActivity.this);
                        shareLink(textToShow);
                        dialog.dismiss();

                    }
                }
        );

        dialog.show();

    }


    public int getRepeat() {

        return  sharedPreferences.getInt("repeat",4);


    }
    public void setRepeat(int i) {

        sharedPreferences.edit().putInt("repeat",i).apply();



    }

    private void set_tone(String name, int tone) {
        sharedPreferences.edit().putInt(name + "tone", tone).apply();
        // Log.e("set_alarm_activity","tone_set="+tone+" get_tone="+get_tone(name));
    }
    public void set_sehri_alarm(long time) {
        String name="sehri";
        int tone=5;
        int tag=130;


        Calendar calendar= Calendar.getInstance();
        if (calendar.getTimeInMillis()>time){
            time=time+24*60*60*1000;
        }

        Intent intentAlarm = new Intent(this, AlarmReciever.class);
        intentAlarm.putExtra("tag", tag);
        intentAlarm.putExtra("tone", tone);
        intentAlarm.putExtra("name", name);
        intentAlarm.putExtra("time", time);


        set_tone(name,tone);

        // Log.e("setAlarm","tone_set="+5+" tag="+tag+" name="+name);


        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(this, tag, intentAlarm, 0));
            }

            else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(this, tag, intentAlarm, 0));
            }

            else {

                alarmManager.set(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(this, tag, intentAlarm, 0));

            }


        }
        Toast.makeText(this, "সেহরির অ্যালার্ম সেট করা হয়েছে", Toast.LENGTH_SHORT).show();

        sharedPreferences.edit().putBoolean("sehri_active",true).apply();

    }

    public void set_iftar_alarm(long time) {
        String name="iftar";
        int tone=5;
        int tag=131;


        Calendar calendar= Calendar.getInstance();
        if (calendar.getTimeInMillis()>time){
            time=time+24*60*60*1000;
        }

        Intent intentAlarm = new Intent(this, AlarmReciever.class);
        intentAlarm.putExtra("tag", tag);
        intentAlarm.putExtra("tone", tone);
        intentAlarm.putExtra("name", name);
        intentAlarm.putExtra("time", time);


        set_tone(name,tone);

        // Log.e("setAlarm","tone_set="+5+" tag="+tag+" name="+name);


        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(this, tag, intentAlarm, 0));
            }

            else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(this, tag, intentAlarm, 0));
            }

            else {

                alarmManager.set(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(this, tag, intentAlarm, 0));

            }


        }

        sharedPreferences.edit().putBoolean("iftar_active",true).apply();

        Toast.makeText(this, "ইফতার এর অ্যালার্ম সেট করা হয়েছে", Toast.LENGTH_SHORT).show();


    }

    private void cancel_sehri_alarm() {

        int tag= 130;
        Intent intentAlarm = new Intent(this, AlarmReciever.class);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, tag, intentAlarm, 0);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }

        sharedPreferences.edit().putBoolean("sehri_active",false).apply();

        Toast.makeText(this, "অ্যালার্ম বন্ধ করা হয়েছে", Toast.LENGTH_LONG).show();
    }

    private void cancel_iftar_alarm() {

        int tag= 131;
        Intent intentAlarm = new Intent(this, AlarmReciever.class);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, tag, intentAlarm, 0);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }

        sharedPreferences.edit().putBoolean("iftar_active",false).apply();

        Toast.makeText(this, "অ্যালার্ম বন্ধ করা হয়েছে", Toast.LENGTH_LONG).show();
    }

    private boolean sehri_active(){

        return sharedPreferences.getBoolean("sehri_active",false);
    }

    private boolean ifatr_active(){

        return sharedPreferences.getBoolean("iftar_active",false);
    }



    private void setSehriAlarmDialog(final long time, final ImageButton sehri_alarm) {
        final android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(this);
        dialog.setTitle("");
        dialog.setMessage(" সেহরি অ্যালার্ম সেট করুন");
        dialog.setPositiveButton("হ্যাঁ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                set_sehri_alarm(time);
                sehri_alarm.setImageResource(R.drawable.alarm_on);


            }
        });

        dialog.setNegativeButton("না", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        dialog.show();
    }
    private void cancelSehriAlarmDialog(final ImageButton sehri_alarm) {
     final android.app.AlertDialog.Builder cancelDialog = new android.app.AlertDialog.Builder(this);
        cancelDialog.setTitle("");
        cancelDialog.setMessage(" সেহরি অ্যালার্ম বাদ দিন");
        cancelDialog.setPositiveButton("হ্যাঁ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

               cancel_sehri_alarm();
                sehri_alarm.setImageResource(R.drawable.alarm_off);


            }
        });

        cancelDialog.setNegativeButton("না", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        cancelDialog.show();
    }



    private void setIftarAlarmDialog(final long time, final ImageButton iftar_alarm) {
        final android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(this);
        dialog.setTitle("");
        dialog.setMessage("ইফাতার অ্যালার্ম সেট করুন");
        dialog.setPositiveButton("হ্যাঁ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                set_iftar_alarm(time);
                iftar_alarm.setImageResource(R.drawable.alarm_on);

            }
        });

        dialog.setNegativeButton("না", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        dialog.show();
    }

    private void cancelIftarAlarmDialog(final ImageButton iftar_alarm) {
        final android.app.AlertDialog.Builder cancelDialog = new android.app.AlertDialog.Builder(this);
        cancelDialog.setTitle("");
        cancelDialog.setMessage(" ইফাতার এর অ্যালার্ম বাদ দিন");
        cancelDialog.setPositiveButton("হ্যাঁ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                cancel_iftar_alarm();
                iftar_alarm.setImageResource(R.drawable.alarm_off);


        }
        });

        cancelDialog.setNegativeButton("না", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        cancelDialog.show();
    }


    private CountDownTimer getCountDownTimer(final long nextSalatTime, final long nextToNextSalatTime, String nextSalatNameString, final String nextToNextSalatNameString, final TextView nextSalatHeading_tv, final TextView nextSalatName_tv, final TextView nextSalatTime_hr_min_tv, final TextView nextSalatTime_sec_tv){

        nextSalatHeading_tv.setVisibility(View.GONE);
        nextSalatTime_sec_tv.setVisibility(View.VISIBLE);
        nextSalatTime_sec_tv.setTypeface(fonts.bensen());

        nextSalatName_tv.setText(nextSalatNameString);

       CountDownTimer countDownTimer= new CountDownTimer(nextSalatTime - System.currentTimeMillis(), 1000) {

            public void onTick(long millisUntilFinished) {

                nextSalatTime_hr_min_tv.setText(get_remaining_time_hour_minute(nextSalatTime));
                nextSalatTime_sec_tv.setText(get_remaining_seconds(nextSalatTime));
            }

            public void onFinish() {
                nextSalatTime_hr_min_tv.setText(get_remaining_time(nextToNextSalatTime));
                nextSalatName_tv.setText(nextToNextSalatNameString);
                nextSalatHeading_tv.setVisibility(View.VISIBLE);
                nextSalatTime_sec_tv.setVisibility(View.GONE);
            }
        };

       return countDownTimer;

    }

    public int getHijriCorrectionFromUser(){

        return sharedPreferences.getInt("hijri_correction_user",100);
    }

    public int getHijriCorrectionIndex(){

        return sharedPreferences.getInt("hijri_correction_index",2);
    }
    public void setHijriCorrectionFromUser(int hijriFix) {


        sharedPreferences.edit().putInt("hijri_correction_index",hijriFix).apply();

        switch (hijriFix){

            case 0:
                sharedPreferences.edit().putInt("hijri_correction_user",1).apply();
                break;

            case 1:
                sharedPreferences.edit().putInt("hijri_correction_user",2).apply();
                break;

            case 2:
                sharedPreferences.edit().putInt("hijri_correction_user",0).apply();
                break;

            case 3:
                sharedPreferences.edit().putInt("hijri_correction_user",-1).apply();
                break;

            case 4:
                sharedPreferences.edit().putInt("hijri_correction_user",-2).apply();
                break;

            default:
                sharedPreferences.edit().putInt("hijri_correction_user",0).apply();
                break;



        }

    }
}