//package com.creativeapps.salat_times.Activities;
//
//
//import android.animation.ValueAnimator;
//import android.app.AlarmManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.content.pm.PackageManager;
//import android.location.Address;
//import android.location.Geocoder;
//import android.location.Location;
//import android.location.LocationManager;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.Looper;
//import android.os.SystemClock;
//import android.provider.Settings;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.NavigationView;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.widget.DrawerLayout;
//import android.support.v7.app.ActionBarDrawerToggle;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.animation.LinearInterpolator;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.azan.PrayerTimes;
//import com.azan.TimeCalculator;
//import com.azan.types.PrayersType;
//import com.crashlytics.android.Crashlytics;
//import com.creativeapps.salat_times.Adapter.Prayer_Times_List_Adapter;
//import com.creativeapps.salat_times.Location.GPSPoint;
//import com.creativeapps.salat_times.Location.LocationDetails;
//import com.creativeapps.salat_times.Location.Workable;
//import com.creativeapps.salat_times.Model.DataModel;
//import com.creativeapps.salat_times.Qibla.QiblaActivity;
//import com.creativeapps.salat_times.R;
//import com.creativeapps.salat_times.RecieversAndService.DailyReciever;
//import com.creativeapps.salat_times.RecieversAndService.DismissButtonReceiver;
//import com.creativeapps.salat_times.Tasbih.TasbihActivity;
//import com.creativeapps.salat_times.UtilityPackage.Fonts;
//import com.creativeapps.salat_times.UtilityPackage.ShareActivity;
//import com.creativeapps.salat_times.UtilityPackage.ShowAdInterface;
//import com.creativeapps.salat_times.UtilityPackage.Utility;
//import com.google.android.gms.ads.AdListener;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.location.LocationListener;
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationServices;
//
//import org.joda.time.Chronology;
//import org.joda.time.DateTimeZone;
//import org.joda.time.LocalDate;
//import org.joda.time.chrono.ISOChronology;
//import org.joda.time.chrono.IslamicChronology;
//
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.GregorianCalendar;
//import java.util.List;
//import java.util.Locale;
//import java.util.TimeZone;
//
//import io.fabric.sdk.android.Fabric;
//
//import static com.azan.types.AngleCalculationType.EGYPT;
//import static com.azan.types.AngleCalculationType.ISNA;
//import static com.azan.types.AngleCalculationType.KARACHI;
//import static com.azan.types.AngleCalculationType.MUHAMMADIYAH;
//import static com.azan.types.AngleCalculationType.MWL;
//
////import com.google.android.gms.ads.InterstitialAd;
////import static com.azan.types.AngleCalculationType.UMM_AL_QURA;
//public class MainActivity extends AppCompatActivity implements ShowAdInterface, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, NavigationView.OnNavigationItemSelectedListener {
//    //CurrentLocation currentLocation;
//
//    private Location previous_location;
//    private DrawerLayout mDrawerLayout;
//    private ActionBarDrawerToggle mDrawerToggle;
//    NavigationView navigationView;
//    FloatingActionButton menu, qibla, tasbih, location;
//
//    private static AsyncTask locationAsyncTask;
//
//
//    private final String tag = "Salat_app";
//    private GoogleApiClient googleApiClient;
//    private LocationRequest locationRequest;
//    public final int permission = 7;
//    private double latitude;
//    private double longitude;
//    private double altitude;
//    private Location lastLocation;
//    private String fajr_time_string, sunrise_time_string, zuhr_time_string, asr_time_string, magrib_time_string, isha_time_string,
//            haram1_time_string, haram2_time_string, haram3_time_string, ishrak_time_string, auabin_time_string, tahazzud_time_string;
//    private Date fajr_time, sunrise_time, zuhr_time, asr_time, magrib_time, isha_time,
//            haram1_time, haram2_time, haram3_time, ishrak_time, auabin_time, tahazzud_time;
//    private ArrayList<DataModel> dataModels;
//    private TextView location_txt;
//    private TextView nextSalat_name;
//    private TextView nextSalat_time;
//    private TextView sehri_time, sehri_text;
//    private TextView iftar_time, iftar_text;
//    private ListView listView;
//    private TextView current_salat_state;
//    private TextView hijri_date;
//    private ImageView imageView;
//    private View divider;
//
//
//    LinearLayout linearLayout;
//    int count;
//
//    public boolean gps_allowed;
//    public boolean net_connected;
//
//
//    AlertDialog gpsDialog;
//    AlertDialog internetDialog;
//
//
//    private SharedPreferences sharedPreferences;
//    public static final String mypreference = "salat";
//
//
//    private com.google.android.gms.ads.InterstitialAd mInterstitialAd;
//
//    private com.google.android.gms.ads.AdView mAdView;
//    private AdRequest adRequest;
//    //private com.facebook.ads.AdView adView;
//    AlertDialog dialog;
//    private boolean has_focus;
//
//    @Override
//    protected void onCreate(final Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        FCM();
//        Fabric.with(this, new Crashlytics());
//        setContentView(R.layout.activity_main);
//        //Log.i(tag, "onCreate");
//        sharedPreferences = getSharedPreferences(mypreference,
//                Context.MODE_PRIVATE);
//
//
//
//        appOpeningCount();
//        ADS(azan_running());
//        gps_allowed = check_gps_connection();
//        sharedPreferences.edit().putBoolean("gps", gps_allowed).apply();
//        net_connected = internet_connection(this);
//        sharedPreferences.edit().putBoolean("net", net_connected).apply();
//
//
//
//        if (!isLocation_already_updated_for_first_time()) {
//            setLocation_request_interval(1000);
//            if (!internet_connection(this)) connect_to_internet_dialog();
//            if (!check_gps_connection()) gps_dialog();
//        }
//
//
//        buildGoogleApiClient();
//        googleApiClient.connect();
//
//        sharedPreferences.edit().putBoolean("change", false).apply();
//
//        if (!sharedPreferences.getBoolean("daily_check_set", false))
//            set_alarm_to_check_active_alarms();
//    }
//
//
//
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        Log.e("onresume", ".....");
//
//        drawer();
//        initializeUI();
//
//
////        googleApiClient.disconnect();
////        googleApiClient.connect();
//
////float dimen=getResources().getDimension(R.dimen.linear_layout_margin)/getResources().getDisplayMetrics().density;
//
////Toast.makeText(getApplicationContext(),"dimen: "+dimen+"....density:"+(getResources().getDisplayMetrics().density*160),Toast.LENGTH_LONG).show();
//        has_focus = true;
//        //ADS(azan_running());
//        //Utility.counter(getApplication(),MainActivity.this);
//        if (azan_running()) {
//            create_cancel_option_in_activity();
//            //Log.e("azan ruinning","true");
//        } else {
//            set_azan_off();
//            // Log.e("azan ruinning","false");
//        }
//
//
//        if (check_gps_connection() && !sharedPreferences.getBoolean("gps", true)) {
//
//
//            Log.e("onresume", "gps");
//            //  setLocation_request_interval(1000);
//
//            setLocation_already_updated_for_first_time(false);
//            setLocation_request_interval(1000);
//
//            reConnectGoogleApiClient();
//
//            sharedPreferences.edit().putBoolean("gps", true).apply();
//
//
//            if (gpsDialog != null && gpsDialog.isShowing())
//
//                gpsDialog.dismiss();
//            initializeUI();
//
//        }
//        if (internet_connection(this) && !sharedPreferences.getBoolean("net", true)) {
//            Log.e("onresume", "net");
//
//            setLocation_already_updated_for_first_time(false);
//            setLocation_request_interval(1000);
//            reConnectGoogleApiClient();
//
//
//            sharedPreferences.edit().putBoolean("net", true).apply();
//
//            try {
//                if (internetDialog != null && internetDialog.isShowing())
//                    internetDialog.dismiss();
//            } catch (NullPointerException n) {
//                n.printStackTrace();
//            }
//            initializeUI();
//        }
//
//
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        has_focus = false;
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        if (googleApiClient.isConnected())
//            googleApiClient.disconnect();
//        // setActivity_already_open(false);
//        Log.i(tag, "onStop");
//    }
//
////    void initializeUI() {
////        //MOVE THIS TO A DIFFRENT FUNCTION
////        location_txt = (TextView)        findViewById(R.id.textView2);
////        nextSalat_name = (TextView)      findViewById(R.id.next_salat_name);
////        nextSalat_time = (TextView)      findViewById(R.id.next_salat_time);
////        hijri_date = (TextView)          findViewById(R.id.hijri_date);
////        sehri_time = (TextView)          findViewById(R.id.sehri_time);
////        iftar_time = (TextView)          findViewById(R.id.iftar_time);
////        sehri_text = (TextView)          findViewById(R.id.sehri_text);
////        iftar_text = (TextView)          findViewById(R.id.iftar_text);
////        listView = (ListView)            findViewById(R.id.prayer_time_list);
////        current_salat_state = (TextView) findViewById(R.id.current_state);
////        imageView = (ImageView)          findViewById(R.id.background);
////        previous_location = retriev_location();
////        linearLayout = (LinearLayout)    findViewById(R.id.frameLayout);
////        //linearLayout.getBackground().setAlpha(100);
////        hijri_date.setText(hijri_date());
////        if (previous_location != null) {
////            latitude = previous_location.getLatitude();
////            longitude = previous_location.getLongitude();
////            setCityName();
////
////            set_background();
////            setPrayerTimes();
////            set_header();
////            setArrayList();
////            Prayer_Times_List_Adapter prayer_times_list_adapter = new Prayer_Times_List_Adapter(dataModels, this);
////            listView.setAdapter(prayer_times_list_adapter);
////        }
////        //Animation move = AnimationUtils.loadAnimation(MainActivity.this, R.anim.move);
////        current_salat_state.setText(getCurrent_Salat());
////        final ValueAnimator animator = ValueAnimator.ofFloat(1.0f, -1.0f);
////        animator.setRepeatCount(ValueAnimator.INFINITE);
////        animator.setInterpolator(new LinearInterpolator());
////        animator.setDuration(7000L);
////        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
////            @Override
////            public void onAnimationUpdate(ValueAnimator animation) {
////                final float progress = (float) animation.getAnimatedValue();
////                final float width = current_salat_state.getWidth();
////                final float translationX = width * progress;
////                current_salat_state.setTranslationX(translationX);
////                // second.setTranslationX(translationX - width);
////            }
////        });
////        animator.start();
////
////        location_txt.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(final View v) {
////                                    Intent intent= new Intent(MainActivity.this, TasbihActivity.class);
////                                    intent.putExtra("long",longitude);
////                                    intent.putExtra("lat",latitude);
////                                    startActivity(intent);
////
////                }
////
////        }
////        );
////    }
//
//
//    void initializeUI() {
//        //MOVE THIS TO A DIFFRENT FUNCTION
//
//        Fonts fonts = new Fonts(this);
//
//        qibla = findViewById(R.id.fab_qibla);
//        tasbih = findViewById(R.id.fab_tasbih);
//        location = findViewById(R.id.fab_location);
//
//        location_txt = findViewById(R.id.textView2);
//        nextSalat_name = findViewById(R.id.next_salat_name);
//        nextSalat_time = findViewById(R.id.next_salat_time);
//        hijri_date = findViewById(R.id.hijri_date);
//        sehri_time = findViewById(R.id.sehri_time);
//        iftar_time = findViewById(R.id.iftar_time);
//        sehri_text = findViewById(R.id.sehri_text);
//        iftar_text = findViewById(R.id.iftar_text);
//        listView = findViewById(R.id.prayer_time_list);
//        current_salat_state = findViewById(R.id.current_state);
//        imageView = findViewById(R.id.background);
//        //divider= rootView.findViewById(R.id.divider);
//
//        location_txt.setTypeface(fonts.FutureCndNormal());
//        nextSalat_name.setTypeface(fonts.bensen());
//        nextSalat_time.setTypeface(fonts.bensen());
//        hijri_date.setTypeface(fonts.bensen());
//        sehri_time.setTypeface(fonts.bensen());
//        iftar_time.setTypeface(fonts.bensen());
//        sehri_text.setTypeface(fonts.bensen());
//        iftar_text.setTypeface(fonts.bensen());
//        current_salat_state.setTypeface(fonts.mohananda());
//
//        final Location previous_location = retriev_location();
//        hijri_date.setText(hijri_date());
//        location_txt.setText(sharedPreferences.getString("city", "Not Found"));
//        if (previous_location != null) {
//            latitude = previous_location.getLatitude();
//            longitude = previous_location.getLongitude();
//
//
//            AsyncTask.execute(new Runnable() {
//                @Override
//                public void run() {
//                    setCityName(previous_location);
//                }
//            });
//
//
//            set_background();
//            setPrayerTimes();
//            set_header();
//            setArrayList();
//            Prayer_Times_List_Adapter prayer_times_list_adapter = new Prayer_Times_List_Adapter(dataModels, this);
//            listView.setAdapter(prayer_times_list_adapter);
//        }
//        //Animation move = AnimationUtils.loadAnimation(MainActivity.this, R.anim.move);
//        current_salat_state.setText(getCurrent_Salat());
//        final ValueAnimator animator = ValueAnimator.ofFloat(1.0f, -1.0f);
//        animator.setRepeatCount(ValueAnimator.INFINITE);
//        animator.setInterpolator(new LinearInterpolator());
//        animator.setDuration(7000L);
//        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                final float progress = (float) animation.getAnimatedValue();
//                final float width = current_salat_state.getWidth();
//                final float translationX = width * progress;
//                current_salat_state.setTranslationX(translationX);
//                // second.setTranslationX(translationX - width);
//            }
//        });
//        animator.start();
//
//
//        qibla.setOnClickListener(new View.OnClickListener() {
//                                     @Override
//                                     public void onClick(final View v) {
//                                         Intent intent = new Intent(MainActivity.this, QiblaActivity.class);
//                                         startActivity(intent);
//                                     }
//                                 }
//        );
//
//        tasbih.setOnClickListener(new View.OnClickListener() {
//                                      @Override
//                                      public void onClick(final View v) {
//                                          Intent intent = new Intent(MainActivity.this, TasbihActivity.class);
//                                          startActivity(intent);
//                                      }
//                                  }
//        );
//
//        location.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(final View v) {
//
//                                            if (!internet_connection(MainActivity.this)) connect_to_internet_dialog();
//                                            if (!check_gps_connection()) gps_dialog();
//
//                                            reConnectGoogleApiClient();
//                                            //makeLocationRequest(100);
//
//                                            final GPSPoint[] g = new GPSPoint[1];
//
//                                            LocationDetails locationDetails=new LocationDetails(MainActivity.this,MainActivity.this,sharedPreferences);
//
//
//
//                                            setCityName(retriev_location());
//                                            Toast.makeText(MainActivity.this,"আপনার সর্বশেষ অবস্থান "+sharedPreferences.getString("city","নির্ণয় করা যায় নি"),Toast.LENGTH_LONG).show();
//
//                                        }
//                                    }
//        );
//
//
//    }
//
//    private void set_azan_off() {
//        sharedPreferences.edit().putBoolean("notification", false).apply();
//        try {
//            if (!isFinishing() && dialog.isShowing())
//                dialog.dismiss();
//        } catch (NullPointerException n) {
//        }
//    }
//
//    private boolean azan_running() {
//        return sharedPreferences.getBoolean("notification", false);
//    }
//
//
//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//        Log.i(tag, "onConnected");
////        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
////            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
////                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
////                    android.Manifest.permission.ACCESS_FINE_LOCATION,
////                    android.Manifest.permission.INTERNET,
////                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
////            }, permission);
////            return;
////        }
//
//
////           lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
////           if (lastLocation != null) {
////               Log.i(tag, "lastLocation_found"+lastLocation.toString());
////
////
////               latitude = lastLocation.getLatitude();
////               longitude = lastLocation.getLongitude();
////               altitude = lastLocation.getAltitude();
////              // save_location(lastLocation);
////           }
//        if (internet_connection(this)) {
//
//            makeLocationRequest(getLocation_request_interval());
//            Log.i(tag, "location_request");
//
////           setCityName();
////           // set_header();
////            setPrayerTimes();
////            setArrayList();
////            Prayer_Times_List_Adapter prayer_times_list_adapter = new Prayer_Times_List_Adapter(dataModels, this);
////            listView.setAdapter(prayer_times_list_adapter);
////            Log.i(tag, "adapter-set");
//
//
//
//        }
//    }
//
//    private void makeLocationRequest(final long interval) {
//
//
//
//        AsyncTask.execute(new Runnable() {
//            @Override
//            public void run() {
//
//                if (ActivityCompat.checkSelfPermission(MainActivity.this,
//                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
//                        ActivityCompat.checkSelfPermission(
//                                MainActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
//                {
//                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{
//                            android.Manifest.permission.ACCESS_COARSE_LOCATION,
//                            android.Manifest.permission.ACCESS_FINE_LOCATION,
//                            android.Manifest.permission.INTERNET,
//                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
//                    }, permission);
//                    return;
//                }
//
//
//
//                locationRequest = LocationRequest.create();
//                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//                if(interval==1)
//                    locationRequest.setInterval(interval);
//
//                if (check_googleApiClient_connection()) {
//
//                    LocationServices.FusedLocationApi.requestLocationUpdates(
//                            googleApiClient, locationRequest, MainActivity.this,
//                            Looper.getMainLooper());
//                }
//                else {
//
//                    Log.e(tag, "googleAPIClient not connected");
//                    if (internet_connection(MainActivity.this))
//
//
//                    {
//                        //buildGoogleApiClient();
//
//                        reConnectGoogleApiClient();
//
//                        try {
//                            LocationServices.FusedLocationApi.requestLocationUpdates(
//                                    googleApiClient, locationRequest, MainActivity.this,
//                                    Looper.getMainLooper());
//
//                        } catch (Exception e) {
//
//                            reConnectGoogleApiClient();
//                        }
//                    }
//                }
//            }
//        });
//
//
//    }
//
//    private void reConnectGoogleApiClient() {
//        try {
//            if (check_googleApiClient_connection())
//                googleApiClient.disconnect();
//        } catch (Exception e) {
//            buildGoogleApiClient();
//
//        }
//        googleApiClient.connect();
//
//    }
//
//    private boolean check_googleApiClient_connection() {
//
//        try {
//            return googleApiClient.isConnected();
//        } catch (Exception e) {
//            buildGoogleApiClient();
//        }
//        return googleApiClient.isConnected();
//    }
//
//
//    @Override
//    public void onConnectionSuspended(int i) {
//        Log.i(tag, "connecttion suspended");
//        if (!internet_connection(this)) connect_to_internet_dialog();
//        if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
//                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
//                    android.Manifest.permission.ACCESS_FINE_LOCATION,
//                    //  Manifest.permission.ACCESS_NETWORK_STATE,
//                    android.Manifest.permission.INTERNET,
//                    //  Manifest.permission.SYSTEM_ALERT_WINDOW,
//                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
//            }, permission);
//        }
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//        Log.i(tag, "connecttion failed");
//        if (!internet_connection(this)) connect_to_internet_dialog();
//        if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
//                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
//                    android.Manifest.permission.ACCESS_FINE_LOCATION,
//                    //  Manifest.permission.ACCESS_NETWORK_STATE,
//                    android.Manifest.permission.INTERNET,
//                    //  Manifest.permission.SYSTEM_ALERT_WINDOW,
//                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
//            }, permission);
//        }
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//        Log.e(tag, "location_changed " + location.toString());
//        latitude = location.getLatitude();
//        longitude = location.getLongitude();
//        altitude = location.getAltitude();
//        Location previos_loc = retriev_location();
//        //  Toast.makeText(getApplicationContext(),"loc_changed",Toast.LENGTH_SHORT).show();
//
//        save_location(location);
//
//        // recreate();
//        //  button.setText(location.toString());
//        //setCityName();
//
//        setPrayerTimes();
//        set_header();
//        setArrayList();
//        Prayer_Times_List_Adapter prayer_times_list_adapter = new Prayer_Times_List_Adapter(dataModels, this);
//        listView.setAdapter(prayer_times_list_adapter);
//
//        if (!isLocation_already_updated_for_first_time()) {
//
//
//            setLocation_already_updated_for_first_time(true);
//            setLocation_request_interval(1000 * 60 * 60);
//
//
//            googleApiClient.disconnect();
//            googleApiClient.connect();
//
//        }
//
//    }
//
//
//    protected synchronized void buildGoogleApiClient() {
//        googleApiClient = new GoogleApiClient.Builder(this)
//                .addApi(LocationServices.API)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .build();
//        Log.i(tag, "googleApiClient_build");
//    }
//
//
////    public void setPrayerTimes() {
////        GregorianCalendar date = new GregorianCalendar();
////        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"),
////                Locale.getDefault());
////        Date currentLocalTime = calendar.getTime();
////        DateFormat date1 = new SimpleDateFormat("Z");
////        String localTime = date1.format(currentLocalTime);
////        double tz;
////        try {
////            tz = Double.parseDouble(localTime);
////
////        } catch (NumberFormatException nf) {
////            tz = +0600;   //timezone for dhaka, this is the common way to write tz, not a typo
////
////        }
////        PrayerTimes prayerTimes;
////        int i = getRule();
////        if (i == 0) {
////            prayerTimes = new TimeCalculator().date(date).location(latitude, longitude, tz,
////                    altitude).timeCalculationMethod(MWL).calculateTimes();
////        } else if (i == 1) {
////            prayerTimes = new TimeCalculator().date(date).location(latitude, longitude, tz,
////                    altitude).timeCalculationMethod(ISNA).calculateTimes();
////        } else if (i == 2) {
////            prayerTimes = new TimeCalculator().date(date).location(latitude, longitude, tz,
////                    altitude).timeCalculationMethod(EGYPT).calculateTimes();
////        } else if (i == 3) {
////            prayerTimes = new TimeCalculator().date(date).location(latitude, longitude, tz,
////                    altitude).timeCalculationMethod(MUHAMMADIYAH).calculateTimes();
////        } else {
////            prayerTimes = new TimeCalculator().date(date).location(latitude, longitude, tz,
////                    altitude).timeCalculationMethod(KARACHI).calculateTimes();
////        }
////        fajr_time = prayerTimes.getPrayTime(PrayersType.FAJR);
////        sunrise_time = (prayerTimes.getPrayTime(PrayersType.SUNRISE));
////        zuhr_time = prayerTimes.getPrayTime(PrayersType.ZUHR);
////        asr_time = (prayerTimes.getPrayTime(PrayersType.ASR));
////        magrib_time = (prayerTimes.getPrayTime(PrayersType.MAGHRIB));
////        isha_time = (prayerTimes.getPrayTime(PrayersType.ISHA));
////
////
////
////
////        haram1_time =sunrise_time;
////        ishrak_time=  new Date(sunrise_time.getTime() + (25 *60* 1000));
////
////        haram2_time = new Date(zuhr_time.getTime() - (10 *60* 1000));
////        haram3_time =new Date(magrib_time.getTime() - (10 *60* 1000));
////        auabin_time = new Date(magrib_time.getTime() + (15 *60* 1000));
////        tahazzud_time= new Date(magrib_time.getTime() + (60 *60* 1000));
////
////
////
////        fajr_time_string = getTime12hourFormat(fajr_time);
////        sunrise_time_string = getTime12hourFormat(sunrise_time);
////        haram1_time_string = getTime12hourFormat(haram1_time);
////        ishrak_time_string =getTime12hourFormat(ishrak_time);
////        haram2_time_string =getTime12hourFormat(haram2_time);
////        zuhr_time_string = getTime12hourFormat(zuhr_time);
////        asr_time_string = getTime12hourFormat(asr_time);
////        haram3_time_string =getTime12hourFormat(haram3_time);
////        magrib_time_string = getTime12hourFormat(magrib_time);
////        auabin_time_string =getTime12hourFormat(auabin_time);
////        isha_time_string = getTime12hourFormat(isha_time);
////        tahazzud_time_string =getTime12hourFormat(tahazzud_time);
////
////
////        Log.i(tag, "setPrayerTimes");
////    }
//
//
//    public void setPrayerTimes() {
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
//        PrayerTimes prayerTimes;
//        int i = getRule();
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
//        Log.i(tag, "setPrayerTimes");
//    }
//
//
//    public void setCityName(Location location) {
//
//        Log.e(tag, "Set_city");
//
//        String cityName = "";
//        Geocoder gcd = new Geocoder(this, Locale.getDefault());
//        List<Address> addresses = null;
//        try {
//            addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
//            if (addresses.size() > 0) {
//                cityName = addresses.get(0).getLocality();
//
//                saveCity(cityName);
//                Log.e(tag, "city_string_on");
//
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//
//            saveCity("bangladesh");
//        }
//        String s = cityName;
//        if (s == null) {
//            Log.i(tag, "city_string_null");
//            s = getCity();
//
//
//        }
//
//
//        location_txt.setText(s);
//
//    }
//
//    public void setCityName(GPSPoint gpsPoint) {
//
//        Log.e(tag, "Set_city");
//
//        String cityName = "";
//        Geocoder gcd = new Geocoder(this, Locale.getDefault());
//        List<Address> addresses = null;
//        try {
//            addresses = gcd.getFromLocation(gpsPoint.getLatitude(), gpsPoint.getLatitude(), 1);
//            if (addresses.size() > 0) {
//                cityName = addresses.get(0).getLocality();
//
//                saveCity(cityName);
//                Log.e(tag, "city_string_on");
//
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//
//            saveCity("bangladesh");
//            Log.i(tag, "city_string_error");
//
//        }
//        String s = cityName;
//        if (s == null) {
//            Log.i(tag, "city_string_null");
//            s = getCity();
//
//
//        }
//
//
//        location_txt.setText(s);
//
//    }
////    public void setArrayList() {
////        dataModels = new ArrayList<>();
////        dataModels.add(new DataModel("Fajr", fajr_time_string, fajr_time, 1, sunrise_time));
////        dataModels.add(new DataModel("Sun", sunrise_time_string, sunrise_time, 2, haram1_time));
////        dataModels.add(new DataModel("Zuhr", zuhr_time_string, zuhr_time, 3, asr_time));
////        dataModels.add(new DataModel("Asr", asr_time_string, asr_time, 4, haram3_time));
////        dataModels.add(new DataModel("Magrib", magrib_time_string, magrib_time, 5, auabin_time));
////        dataModels.add(new DataModel("Esha", isha_time_string, isha_time, 6,tahazzud_time ));
////
////        dataModels.add(new DataModel("Haram_1", haram1_time_string, haram1_time, 7, ishrak_time));
////        dataModels.add(new DataModel("Ishrak", ishrak_time_string, ishrak_time, 8, haram2_time));
////        dataModels.add(new DataModel("Haram_2", haram2_time_string, haram2_time, 9, zuhr_time));
////        dataModels.add(new DataModel("Haram_3", haram3_time_string, haram3_time, 10, magrib_time));
////        dataModels.add(new DataModel("Auabin", auabin_time_string, auabin_time, 11, isha_time));
////        dataModels.add(new DataModel("Tahazzud", tahazzud_time_string, tahazzud_time, 12, fajr_time));
////
////
////        Log.i(tag, "setarraylist");
////    }
//
//    public void setArrayList() {
//        dataModels = new ArrayList<>();
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
//        Log.i(tag, "setarraylist");
//    }
//
//
//    public String getTime12hourFormat(Date date) {
//        SimpleDateFormat localDateFormat = new SimpleDateFormat("hh:mm");
//        String time = localDateFormat.format(date);
//        Log.i(tag, "gettime12hourformat");
//        return time;
//    }
//
//    public String converted_time_string_bengali(String string) {
//        string = string.replaceAll("0", "০");
//        string = string.replaceAll("1", "১");
//        string = string.replaceAll("2", "২");
//        string = string.replaceAll("3", "৩");
//        string = string.replaceAll("4", "৪");
//        string = string.replaceAll("5", "৫");
//        string = string.replaceAll("6", "৬");
//        string = string.replaceAll("7", "৭");
//        string = string.replaceAll("8", "৮");
//        string = string.replaceAll("9", "৯");
//        string = string.replaceAll("AM", "");
//        string = string.replaceAll("PM", "");
//        Log.i(tag, "converted_time_string_bengali");
//        return string;
//    }
//
//    public void save_location(Location location) {
//        try {
//            sharedPreferences.edit().putString("LOCATION_LAT", String.valueOf(location.getLatitude())).apply();
//            sharedPreferences.edit().putString("LOCATION_LON", String.valueOf(location.getLongitude())).apply();
//            sharedPreferences.edit().putString("LOCATION_PROVIDER", location.getProvider()).apply();
//            Log.i(tag, "save_location");
//        } catch (Exception e) {
//            sharedPreferences.edit().putString("LOCATION_LAT", "23.6850").apply();
//            sharedPreferences.edit().putString("LOCATION_LON", "90.3563").apply();
//            sharedPreferences.edit().putString("LOCATION_PROVIDER", "myLocationProvider").apply();
//            Log.i(tag, "save_location_failed");
//        }
//    }
//
//    public Location retriev_location() {
//        String lat = sharedPreferences.getString("LOCATION_LAT", "23.6850");
//        String lon = sharedPreferences.getString("LOCATION_LON", "90.3563");
//        Location location;
//        String provider = sharedPreferences.getString("LOCATION_PROVIDER", "myLocationProvider");
//        location = new Location(provider);
//        location.setLatitude(Double.parseDouble(lat));
//        location.setLongitude(Double.parseDouble(lon));
//        Log.i(tag, "retriev_location");
//        return location;
//    }
//
////    public void set_header() {
////        GregorianCalendar date = new GregorianCalendar();
////        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"),
////                Locale.getDefault());
////        Date currentLocalTime = calendar.getTime();
////        DateFormat date1 = new SimpleDateFormat("Z");
////        String localTime = date1.format(currentLocalTime);
////        double tz;
////        try {
////            tz = Double.parseDouble(localTime);
////        } catch (NumberFormatException nf) {
////            tz = +0600;
////        }
////        PrayerTimes prayerTimes;
////        int i = getRule();
////        if (i == 0) {
////            prayerTimes = new TimeCalculator().date(date).location(latitude, longitude, tz,
////                    altitude).timeCalculationMethod(MWL).calculateTimes();
////        } else if (i == 1) {
////            prayerTimes = new TimeCalculator().date(date).location(latitude, longitude, tz,
////                    altitude).timeCalculationMethod(ISNA).calculateTimes();
////        } else if (i == 2) {
////            prayerTimes = new TimeCalculator().date(date).location(latitude, longitude, tz,
////                    altitude).timeCalculationMethod(EGYPT).calculateTimes();
////        } else if (i == 3) {
////            prayerTimes = new TimeCalculator().date(date).location(latitude, longitude, tz,
////                    altitude).timeCalculationMethod(MUHAMMADIYAH).calculateTimes();
////        } else {
////            prayerTimes = new TimeCalculator().date(date).location(latitude, longitude, tz,
////                    altitude).timeCalculationMethod(KARACHI).calculateTimes();
////        }
////        if (System.currentTimeMillis() <= prayerTimes.getPrayTime(PrayersType.FAJR).getTime()) {
////            nextSalat_name.setText("ফজর");
////            nextSalat_time.setText(get_remaining_time(fajr_time.getTime()));
////        } else if (System.currentTimeMillis() <= prayerTimes.getPrayTime(PrayersType.ZUHR).getTime()) {
////            nextSalat_name.setText("যোহর");
////            nextSalat_time.setText(get_remaining_time(zuhr_time.getTime()));
////        } else if (System.currentTimeMillis() <= prayerTimes.getPrayTime(PrayersType.ASR).getTime()) {
////            nextSalat_name.setText("আসর");
////            nextSalat_time.setText(get_remaining_time(asr_time.getTime()));
////        } else if (System.currentTimeMillis() <= prayerTimes.getPrayTime(PrayersType.MAGHRIB).getTime()) {
////            nextSalat_name.setText("মাগরিব");
////            nextSalat_time.setText(get_remaining_time(magrib_time.getTime()));
////        } else if (System.currentTimeMillis() <= prayerTimes.getPrayTime(PrayersType.ISHA).getTime()) {
////            nextSalat_name.setText("ঈশা");
////            nextSalat_time.setText(get_remaining_time(isha_time.getTime()));
////        } else {
////            nextSalat_name.setText("ফজর");
////            nextSalat_time.setText(get_remaining_time(fajr_time.getTime() + (24 * 60 * 60 * 1000)));
////        }
////        sehri_time.setText(converted_time_string_bengali(getTime12hourFormat(prayerTimes.getPrayTime(PrayersType.FAJR))));
////        iftar_time.setText(converted_time_string_bengali(getTime12hourFormat(prayerTimes.getPrayTime(PrayersType.MAGHRIB))));
////        Log.i(tag, "set_header");
////    }
//
//
//    public void set_header() {
//        GregorianCalendar date = new GregorianCalendar();
//        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"),
//                Locale.getDefault());
//        Date currentLocalTime = calendar.getTime();
//        DateFormat date1 = new SimpleDateFormat("Z");
//        String localTime = date1.format(currentLocalTime);
//        double tz;
//        try {
//            tz = Double.parseDouble(localTime);
//        } catch (NumberFormatException nf) {
//            tz = +0600;
//        }
//        PrayerTimes prayerTimes;
//        int i = getRule();
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
//        if (System.currentTimeMillis() <= prayerTimes.getPrayTime(PrayersType.FAJR).getTime()) {
//            nextSalat_name.setText("ফজর");
//            nextSalat_time.setText(get_remaining_time(fajr_time.getTime()));
//        } else if (System.currentTimeMillis() <= prayerTimes.getPrayTime(PrayersType.ZUHR).getTime()) {
//            nextSalat_name.setText("যোহর");
//            nextSalat_time.setText(get_remaining_time(zuhr_time.getTime()));
//        } else if (System.currentTimeMillis() <= prayerTimes.getPrayTime(PrayersType.ASR).getTime()) {
//            nextSalat_name.setText("আসর");
//            nextSalat_time.setText(get_remaining_time(asr_time.getTime()));
//        } else if (System.currentTimeMillis() <= prayerTimes.getPrayTime(PrayersType.MAGHRIB).getTime()) {
//            nextSalat_name.setText("মাগরিব");
//            nextSalat_time.setText(get_remaining_time(magrib_time.getTime()));
//        } else if (System.currentTimeMillis() <= prayerTimes.getPrayTime(PrayersType.ISHA).getTime()) {
//            nextSalat_name.setText("ঈশা");
//            nextSalat_time.setText(get_remaining_time(isha_time.getTime()));
//        } else {
//            nextSalat_name.setText("ফজর");
//            nextSalat_time.setText(get_remaining_time(fajr_time.getTime() + (24 * 60 * 60 * 1000)));
//        }
//        sehri_time.setText(converted_time_string_bengali(getTime12hourFormat(prayerTimes.getPrayTime(PrayersType.FAJR))));
//        iftar_time.setText(converted_time_string_bengali(getTime12hourFormat(prayerTimes.getPrayTime(PrayersType.MAGHRIB))));
//        Log.i(tag, "set_header");
//    }
//
//    public void set_alarm_to_check_active_alarms() {
//        Calendar calendar = Calendar.getInstance();
//        int hour_now = calendar.get(Calendar.HOUR_OF_DAY);
//        int waiting_hours = 24 - hour_now;
//        //calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.set(Calendar.HOUR_OF_DAY, 1);
//        calendar.set(Calendar.MINUTE, 1);
//        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        Intent intent = new Intent(this, DailyReciever.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + (waiting_hours * 60 * 60 * 1000) + (30 * 60 * 1000),
//                AlarmManager.INTERVAL_DAY, pendingIntent);
////        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
////                AlarmManager.INTERVAL_DAY, pendingIntent);
//        sharedPreferences.edit().putBoolean("daily_check_set", true).apply();
//        Log.i(tag, "set_alarm_to_check_active_alarms");
//    }
//
//    public String hijri_date() {
//        DateTimeZone datetimeZone = DateTimeZone.forID("Asia/Dhaka");
//        Chronology iso = ISOChronology.getInstance(datetimeZone);
//        Chronology hijri = IslamicChronology.getInstance(datetimeZone);
//        LocalDate todayIso = new LocalDate(LocalDate.now(), iso);
//        LocalDate todayHijri = new LocalDate(todayIso.toDateTimeAtStartOfDay(),
//                hijri);
//        int date = todayHijri.getDayOfMonth();
//        int month = todayHijri.getMonthOfYear();
//        int year = todayHijri.getYear();
//        String month_name = null;
//        switch (month) {
//            case 1:
//                month_name = "মুহররম";
//                break;
//            case 2:
//                month_name = "সফর";
//                break;
//            case 3:
//                month_name = "রবিউল আউয়াল";
//                break;
//            case 4:
//                month_name = "রবিউস সানি";
//                break;
//            case 5:
//                month_name = "জমাদিউল আউয়াল";
//                break;
//            case 6:
//                month_name = "জমাদিউস সানি";
//                break;
//            case 7:
//                month_name = "রজব";
//                break;
//            case 8:
//                month_name = "শাবান";
//                break;
//            case 9:
//                month_name = "রমজান";
//                break;
//            case 10:
//                month_name = "শাওয়াল";
//                break;
//            case 11:
//                month_name = "জিলক্বদ";
//                break;
//            case 12:
//                month_name = "জিলহজ্জ";
//                break;
//        }
//
//        String d = converted_time_string_bengali(Integer.toString(date));
//        String y = converted_time_string_bengali(Integer.toString(year));
//        String hijri_date_string = d + "-" + month_name + ", " + y + " হিজরি";
//        Log.i(tag, "current_salat_state");
//
//        return hijri_date_string;
//    }
//
//    public void set_background() {
//        Calendar calendar = Calendar.getInstance();
//        int hour = calendar.get(Calendar.HOUR_OF_DAY);
//        if (hour >= 0 && hour < 3) {
//            imageView.setImageResource(R.drawable.masjid1);
//            listView.setBackground(getResources().getDrawable(R.drawable.gradient_1));
//            sharedPreferences.edit().putInt("background_grad",1).apply();
//            // divider.setBackgroundColor(getResources().getColor(R.color.grad_1));
//
//        } else if (hour >= 3 && hour < 6) {
//            imageView.setImageResource(R.drawable.masjid2);
//            listView.setBackground(getResources().getDrawable(R.drawable.gradient_2));
//            sharedPreferences.edit().putInt("background_grad",2).apply();
//
//            //divider.setBackgroundColor(getResources().getColor(R.color.grad_2));
//        } else if (hour >= 6 && hour < 9) {
//            imageView.setImageResource(R.drawable.masjid3);
//            listView.setBackground(getResources().getDrawable(R.drawable.gradient_3));
//            sharedPreferences.edit().putInt("background_grad",3).apply();
//
//            //divider.setBackgroundColor(getResources().getColor(R.color.grad_8));
//        } else if (hour >= 9 && hour < 11) {
//            imageView.setImageResource(R.drawable.masjid4);
//            listView.setBackground(getResources().getDrawable(R.drawable.gradient_4));
//            sharedPreferences.edit().putInt("background_grad",4).apply();
//
//            // divider.setBackgroundColor(getResources().getColor(R.color.grad_4));
//
//        } else if (hour >= 11 && hour < 13) {
//            imageView.setImageResource(R.drawable.masjid5);
//            listView.setBackground(getResources().getDrawable(R.drawable.gradient_5));
//            sharedPreferences.edit().putInt("background_grad",5).apply();
//
//            // divider.setBackgroundColor(getResources().getColor(R.color.grad_5));
//
//        } else if (hour >= 13 && hour < 15) {
//            imageView.setImageResource(R.drawable.masjid6);
//            listView.setBackground(getResources().getDrawable(R.drawable.gradient_6));
//            sharedPreferences.edit().putInt("background_grad",6).apply();
//
//            // divider.setBackgroundColor(getResources().getColor(R.color.grad_6));
//        } else if (hour >= 15 && hour < 17) {
//            imageView.setImageResource(R.drawable.masjid7);
//            listView.setBackground(getResources().getDrawable(R.drawable.gradient_7));
//            sharedPreferences.edit().putInt("background_grad",7).apply();
//
//            // divider.setBackgroundColor(getResources().getColor(R.color.grad_7));
//        } else if (hour >= 17 && hour < 19) {
//            imageView.setImageResource(R.drawable.masjid8);
//            listView.setBackground(getResources().getDrawable(R.drawable.gradient_8));
//            sharedPreferences.edit().putInt("background_grad",8).apply();
//
//            // divider.setBackgroundColor(getResources().getColor(R.color.grad_8));
//        } else if (hour >= 19 && hour < 21) {
//            imageView.setImageResource(R.drawable.masjid9);
//            listView.setBackground(getResources().getDrawable(R.drawable.gradient_9));
//            sharedPreferences.edit().putInt("background_grad",9).apply();
//
//            // divider.setBackgroundColor(getResources().getColor(R.color.grad_9));
//        } else if (hour >= 21 && hour < 24) {
//            imageView.setImageResource(R.drawable.masjid10);
//            listView.setBackground(getResources().getDrawable(R.drawable.gradient_10));
//            sharedPreferences.edit().putInt("background_grad",10).apply();
//
//            // divider.setBackgroundColor(getResources().getColor(R.color.grad_10));
//        } else {
//            imageView.setImageResource(R.drawable.masjid1);
//            listView.setBackground(getResources().getDrawable(R.drawable.gradient_1));
//            sharedPreferences.edit().putInt("background_grad",1).apply();
//
//            // divider.setBackgroundColor(getResources().getColor(R.color.grad_1));
//        }
//        Log.i(tag, "set_background");
//    }
//
//    public boolean internet_connection(Context context) {
//        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//        NetworkInfo mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//        if ((wifiInfo != null && wifiInfo.isConnected()) || (mobileInfo != null && mobileInfo.isConnected())) {
//            Log.i(tag, "internet_on");
//            return true;
//        } else {
//            Log.i(tag, "internet_off");
//            return false;
//        }
//    }
//
//    private void connect_to_internet_dialog() {
//        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage("যথাযথ সময় জানার জন্য ইন্টারনেট এ সংযোগ করুন")
//                .setCancelable(false)
//                .setPositiveButton("ওয়াইফাই", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
//                        dialog.dismiss();
//                    }
//                })
//                .setNeutralButton("ডাটা সংযোগ", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        startActivity(new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS));
//                        dialog.dismiss();
//                    }
//                })
//                .setNegativeButton("বাদ দিন", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.dismiss();
//                    }
//                });
//        internetDialog = builder.create();
//        internetDialog.show();
//        Log.i(tag, "connect_internet_dialog");
//    }
//
//    private boolean check_gps_connection() {
//        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        boolean gps_enabled = false;
//        boolean network_enabled = false;
//        try {
//            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
//        } catch (Exception ex) {
//        }
////        try {
////            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
////        } catch(Exception ex) {}
//        return gps_enabled;
//    }
//
//    private void gps_dialog() {
//        // Log.i(tag, "gps_dialog");
//
//        gpsDialog = new AlertDialog.Builder(MainActivity.this)
//                .setMessage("আপনার অবস্থান জানতে জিপিএস চালু করুন")
//                .setPositiveButton("ঠিক আছে", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Intent gpsOptionsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                        startActivity(gpsOptionsIntent);
//
//
//                    }
//                })
//                .setNegativeButton("বাদ দিন", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//
//                    }
//                })
//                .show();
//
//
//        //  builder.show();
//
//        //  Log.i(tag, "connect_gps_dialog");
//    }
//
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//        if (id == R.id.nav_rating) {
//            try {
//                Uri uri = Uri.parse("market://details?id=" + Utility.PACKAGENAME);
//                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
//                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
//                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
//                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
//                startActivity(goToMarket);
//            } catch (android.content.ActivityNotFoundException anfe) {
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + Utility.PACKAGENAME)));
//            }
//        } else if (id == R.id.nav_share) {
//            Intent intent = new Intent(this, ShareActivity.class);
//            startActivity(intent);
//        } else if (id == R.id.nav_fb_page) {
//            String url = Utility.FACEBOOK_PAGE;
//            Log.e("fb uri", url);
//            Intent i = Utility.newFacebookIntent(getPackageManager(), url);//new Intent(Intent.ACTION_VIEW);
//            //i.setData(Uri.parse(url));
//            startActivity(i);
//        } else if (id == R.id.nav_app_store) {
//            try {
//                Uri uri = Uri.parse("market://search?q=pub:" + Utility.PUBLISHER_NAME);
//                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
//                // To count with Play market backstack, After pressing back button,
//                // to taken back to our application, we need to add following flags to intent.
//                startActivity(goToMarket);
//            } catch (android.content.ActivityNotFoundException anfe) {
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + Utility.PACKAGE_NAME)));
//            }
//        } else if (id == R.id.quran_app) {
//            try {
//                Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.creativeapps.banglaquran");
//                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
//                // To count with Play market backstack, After pressing back button,
//                // to taken back to our application, we need to add following flags to intent.
//                startActivity(goToMarket);
//            } catch (android.content.ActivityNotFoundException anfe) {
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.creativeapps.banglaquran")));
//            }
//        } else if (id == R.id.talking_clock) {
//            try {
//                Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.creativeapps.talking_clock");
//                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
//                // To count with Play market backstack, After pressing back button,
//                // to taken back to our application, we need to add following flags to intent.
//                startActivity(goToMarket);
//            } catch (android.content.ActivityNotFoundException anfe) {
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.creativeapps.talking_clock")));
//            }
//        } else if (id == R.id.settings) {
//            final AlertDialog.Builder builderSingle = new AlertDialog.Builder(MainActivity.this);
//            builderSingle.setTitle("নিয়ম");
//            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.select_dialog_singlechoice);
//            arrayAdapter.add("মুস্লিম ওয়ার্ল্ড লীগ");
//            arrayAdapter.add("I.S.N.A. (ইস্লামিক সোসাইটি অফ নর্থ আমেরিকা)");
//            arrayAdapter.add("ইজিপসিয়ান জেনারেল অথরিটি অফ সার্ভ");
//            arrayAdapter.add("উম্মুল কুরা ইউনিভার্সিটি,মক্কা");
//            arrayAdapter.add("ইউনিভার্সিটি অফ ইসলামিক স্টাডিজ, করাচী");
//            builderSingle.setSingleChoiceItems(arrayAdapter, getRule(), new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    Log.e("Which", which + "");
//                    setRule(which);
//                }
//            });
//            builderSingle.setNegativeButton("ঠিক আছে", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(intent);
//                }
//            });
//            // Toast.makeText(this, String.valueOf(getRule()), Toast.LENGTH_LONG).show();
////
////                    builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
////                        @Override
////                        public void onClick(DialogInterface dialog, int which) {
////                            Log.e("Which",which+"");
////
////                            setRule(which);
////                           // setRule(rule);
////                            /*
////                            AlertDialog.Builder builderInner = new AlertDialog.Builder(MainActivity.this);
////                            builderInner.setMessage(arrayAdapter.getItem(rule));
////                            builderInner.setPositiveButton("ঠিক আছে", new DialogInterface.OnClickListener() {
////                                @Override
////                                public void onClick(DialogInterface dialog, int which) {
////
////                                    setRule(rule);
////                                }
////                            });
////                            builderInner.setNegativeButton("বাদ দিন", new DialogInterface.OnClickListener() {
////                                @Override
////                                public void onClick(DialogInterface dialog, int which) {
////                                    setRule(rule);
////                                }
////                            });
////                            builderInner.show();
////*/
////
////                        }
////                    });
//            builderSingle.show();
//        }
//        Log.i(tag, "navigation_item_selected");
//        return true;
//    }
//
//    void drawer() {
//        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
//                R.string.drawer_open, R.string.drawer_close) {
//            public void onDrawerClosed(View view) {
//                //syncActionBarArrowState();
//                try {
//                    getActionBar();
//                } catch (Exception e) {
//                }
//            }
//
//            public void onDrawerOpened(View drawerView) {
//                mDrawerToggle.setDrawerIndicatorEnabled(true);
//                try {
//                    getActionBar().hide();
//                } catch (Exception e) {
//                }
//            }
//        };
//        mDrawerLayout.addDrawerListener(mDrawerToggle);
//        navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(MainActivity.this);
//        // Log.i(tag, "drawer");
//        menu = findViewById(R.id.fab_menu);
//        menu.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(final View v) {
//                                        mDrawerLayout.openDrawer(Gravity.LEFT);
//                                    }
//                                }
//        );
//
//
//    }
//
//    @Override
//    public void onBackPressed() {
//        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
//            mDrawerLayout.closeDrawers();
//            return;
//        }
//        SharedPreferences pref = getSharedPreferences(Utility.SHARED_PREF_NAME, MODE_PRIVATE);
//        int my_counter = pref.getInt("count_ads", 0);
//        SharedPreferences.Editor editor = pref.edit();
//        my_counter++;
//        editor.putInt("count_ads", my_counter);
//        editor.apply();
//        //if(getSharedPreferences(Utility.SHARED_PREF_NAME,MODE_PRIVATE).getBoolean("initialized",false) &&my_counter%3==0 ){
//        /*if(my_counter%3==0){
//
//            Intent intent = new Intent(this, AdActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//
//            startActivityForResult(intent,1);
//
//            //finish();
//
//            return;
//        }*/
//        Log.e("my counter", "" + my_counter);
//
//        AlertDialog.Builder alert = new AlertDialog.Builder(
//                MainActivity.this);
//        if (my_counter % 3 != 0) {
//            alert.setTitle("আমাদের অ্যাপ স্টোর");
//            alert.setIcon(R.mipmap.ic_launcher); //app icon here
//            alert.setMessage("আকর্ষণীয় ফ্রী অ্যাপস পেতে স্টোর ভিজিট করুন!");
//            alert.setPositiveButton("ফিরে যান",
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog,
//                                            int whichButton) {
//                            //Do nothing
//                        }
//                    });
//            alert.setNegativeButton("বন্ধ করুন",
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            finish();
//                        }
//                    });
//
//            alert.setNeutralButton("আমাদের স্টোর",
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//
//                            try {
//                                Uri uri = Uri.parse("market://search?q=pub:" + Utility.PUBLISHER_NAME);
//                                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
//                                // To count with Play market backstack, After pressing back button,
//                                // to taken back to our application, we need to add following flags to intent.
//
//                                startActivity(goToMarket);
//                            } catch (android.content.ActivityNotFoundException anfe) {
//                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + Utility.PACKAGENAME)));
//                            }
//                        }
//                    });
//            if (!isFinishing())
//                alert.show();
//
//        } else {
//            alert.setTitle("রেটিং দিন");
//            alert.setIcon(R.mipmap.ic_launcher); //app icon here
//            alert.setMessage(Utility.RATE_MESSAGE);
//            alert.setPositiveButton("ফিরে যান",
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog,
//                                            int whichButton) {
//                            //Do nothing
//                        }
//                    });
//            alert.setNegativeButton("বন্ধ করুন",
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            finish();
//                        }
//                    });
//
//            alert.setNeutralButton("রেটিং দিন",
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//
//                            try {
//                                startActivity(new Intent(Intent.ACTION_VIEW,
//                                        Uri.parse("market://details?id=" + Utility.PACKAGENAME
//                                        )));
//                            } catch (android.content.ActivityNotFoundException anfe) {
//                                startActivity(new Intent(
//                                        Intent.ACTION_VIEW,
//                                        Uri.parse("http://play.google.com/store/apps/details?id=" + Utility.PACKAGENAME)));
//                            }
//                        }
//                    });
//            if (!isFinishing())
//                alert.show();
//        }
//    }
//
//    public int getRule() {
//        //Log.i(tag, "get_rule" + sharedPreferences.getInt("rule", 4));
//        return sharedPreferences.getInt("rule", 4);
//    }
//
//    public void setRule(int rule) {
//        // Log.i(tag, "set_rule" + rule);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putInt("rule", rule);
//        editor.apply();
//    }
//
//    public void saveCity(String city) {
//        // Log.i(tag, "save_city" + city);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("city", city);
//        editor.apply();
//    }
//
//    public String getCity() {
//        //Log.i(tag, "get_rule" + sharedPreferences.getString("city", null));
//
//        try {
//            return sharedPreferences.getString("city", null);
//
//        } catch (Exception e) {
//            return "bangladesh";
//        }
//    }
//
//    public int getLocation_request_interval() {
//        return sharedPreferences.getInt("interval", 1000);
//    }
//
//    public void setLocation_request_interval(int interval) {
//        Log.i(tag, "save_interval" + interval);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putInt("interval", interval);
//        editor.apply();
//    }
//
//    public boolean isLocation_already_updated_for_first_time() {
//        return sharedPreferences.getBoolean("Location_already_updated_for_first_time", false);
//    }
//
//    public void setLocation_already_updated_for_first_time(boolean bool) {
//        Log.i(tag, "Location_updated" + bool);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putBoolean("Location_already_updated_for_first_time", bool);
//        editor.apply();
//    }
//
//    public boolean isActivity_already_open() {
//        return sharedPreferences.getBoolean("Activity_open", false);
//    }
//
//    public void setActivity_already_open(boolean activity_open) {
//        Log.i(tag, "Activity_open" + activity_open);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putBoolean("Activity_open", activity_open);
//        editor.apply();
//    }
//
//    public String get_remaining_time(long nextSalatTime) {
//        long millis = nextSalatTime - System.currentTimeMillis();
//        int hours = (int) (millis / (1000 * 60 * 60));
//        int minutes = (int) (millis / (1000 * 60)) - hours * 60;
//        String remaining = hours + " ঘণ্টা " + minutes + " মিনিট পর ";
//        return converted_time_string_bengali(remaining);
//    }
//
//    public String getCurrent_Salat() {
//        String current;
//        int gap_after_fajr = 15 * 60 * 1000;
//        int gap_after_zuhr = 15 * 60 * 1000;
//        int gap_after_asr = 15 * 60 * 1000;
//        int gap_after_magrib = 15 * 60 * 1000;
//        int gap_after_isha = 15 * 60 * 1000;
//        long current_time = System.currentTimeMillis();
//        long fajr_last_time = sunrise_time.getTime() - gap_after_fajr;
//        long sunrise_last_time = sunrise_time.getTime() + gap_after_fajr;
//        long zuhr_last_time = asr_time.getTime() - gap_after_zuhr;
//        long asr_last_time = magrib_time.getTime() - gap_after_asr;
//        long magrib_last_time = isha_time.getTime() - gap_after_magrib;
//        long isha_last_time = fajr_time.getTime() + (24 * 60 * 60 * 1000) - gap_after_isha;
//        if (current_time >= fajr_time.getTime() && current_time <= fajr_last_time) {
//            current = "ফজরের ওয়াক্ত চলছে ";
//        } else if (current_time > fajr_last_time && current_time < sunrise_last_time) {
//            current = "সূর্যোদয় হচ্ছে";
//        } else if (current_time >= sunrise_last_time && current_time < zuhr_time.getTime()) {
//            current = "পরবর্তী সালাত যোহর";
//        } else if (current_time >= zuhr_time.getTime() && current_time < zuhr_last_time) {
//            current = "যোহরের ওয়াক্ত চলছে ";
//        } else if (current_time >= zuhr_last_time && current_time < asr_time.getTime()) {
//            current = "আসরের ওয়াক্ত সন্নিকটে ";
//        } else if (current_time >= asr_time.getTime() && current_time < asr_last_time) {
//            current = "আসরের ওয়াক্ত চলছে ";
//        } else if (current_time >= asr_last_time && current_time < magrib_time.getTime()) {
//            current = "সূর্যাস্তের সময় চলছে, মাগরিব সন্নিকটে ";
//        } else if (current_time >= magrib_time.getTime() && current_time < magrib_last_time) {
//            current = "মাগরিবের ওয়াক্ত চলছে ";
//        } else if (current_time >= magrib_last_time && current_time < isha_time.getTime()) {
//            current = "এশার ওয়াক্ত সন্নিকটে   ";
//        } else if (current_time >= isha_time.getTime() && current_time < isha_last_time) {
//            current = "এশার ওয়াক্ত চলছে ";
//        } else if (current_time >= isha_time.getTime() - (24 * 60 * 60 * 1000) && current_time < isha_last_time) {
//            current = "এশার ওয়াক্ত চলছে ";
//        } else if (current_time >= isha_last_time && current_time < fajr_time.getTime()) {
//            current = "ফজরের ওয়াক্ত সন্নিকটে  ";
//        } else current = "";
//        return current;
//    }
//
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case permission: {
//                // If request is cancelled, the result arrays are empty.
//                Log.e(tag, "request permisssion");
//
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    if (internet_connection(this)) {
////                        setLocation_request_interval(60 * 1000);
////                        locationRequest = LocationRequest.create();
////                        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
////                        locationRequest.setInterval(getLocation_request_interval());
////                        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
////                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
////                                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
////                                    android.Manifest.permission.ACCESS_FINE_LOCATION,
////                                    android.Manifest.permission.INTERNET,
////                                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
////                            }, permission);
////                            return;
////                        }
////
////
////                        //  buildGoogleApiClient();
////
////
////                        //TODO if-else conditions added to fix the error occuring beacause of googleApiClient connecton
////                        if (check_googleApiClient_connection()) {
////                            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
////                            //Log.e(tag, "googleAPIClient connected");
////                            Log.e(tag, "googleAPIClient  connected");
////
////
////                        } else {
////                            Log.e(tag, "googleAPIClient not connected");
////
////
////                            if (internet_connection(this))
////
////
////                            {
//////                                buildGoogleApiClient();
////                                googleApiClient.connect();
////
////                                if (check_googleApiClient_connection()) {
////                                    try {
////                                        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
////
////                                    } catch (Exception e) {
////                                        buildGoogleApiClient();
////                                        googleApiClient.connect();
////                                    }
////                                }
////                            }
////                        }
//                        makeLocationRequest(60*1000);
//                        Log.i(tag, "location_request");
//                    } else {
//                        // permission denied, boo! Disable the
//                        // functionality that depends on this permission.
//                    }
//                    return;
//                }
//                // other 'case' lines to check for other
//                // permissions this app might request
//            }
//        }
//    }
//
//    void FCM() {
//        //notification fcm
//        if (getIntent().getExtras() != null) {
//            String pack = (String) getIntent().getExtras().get("1");
//            if (pack != null) {
//                Log.e("fcm main pack", pack);
//                try {
//                    Uri uri = Uri.parse("market://details?id=" + pack);
//                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
//                    // To count with Play market backstack, After pressing back button,
//                    // to taken back to our application, we need to add following flags to intent.
//                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
//                            Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
//                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
//                    startActivity(goToMarket);
//                } catch (android.content.ActivityNotFoundException anfe) {
//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + pack)));
//                }
//            }
//        } else Log.e("fcm main", "null");
//    }
//
////    void ADS(final boolean app_is_run_from_notification) {
////
//////        mInterstitialAd = new InterstitialAd(this);
//////        mInterstitialAd.setAdUnitId(Utility.INTERSTITIAL);
//////        mInterstitialAd.setAdListener(new AdListener() {
//////            @Override
//////            public void onAdClosed() {
//////                //requestNewInterstitial();
//////            }
//////
//////            @Override
//////            public void onAdLoaded() {
//////                super.onAdLoaded();
//////                //showAd();
//////                Utility.counter(getApplication(), (ShowAdInterface) MainActivity.this);
//////            }
//////        });
////
////
////       //Todo fb interstitial
////        fb_interstitialAd = new InterstitialAd(this, "YOUR_PLACEMENT_ID");
////
////        fb_interstitialAd.setAdListener(new InterstitialAdListener() {
////            @Override
////            public void onInterstitialDisplayed(Ad ad) {
////                // Interstitial displayed callback
////            }
////
////            @Override
////            public void onInterstitialDismissed(Ad ad) {
////                // Interstitial dismissed callback
////            }
////
////            @Override
////            public void onError(Ad ad, AdError adError) {
////                // Ad error callback
//////                Toast.makeText(MainActivity.this, "Error: " + adError.getErrorMessage(),
//////                        Toast.LENGTH_LONG).show();
////            }
////
////            @Override
////            public void onAdLoaded(Ad ad) {
////                // Show the ad when it's done loading.
////                //todo fb inter showed here
////
////                    fb_interstitialAd.show();
////
////
////            }
////
////            @Override
////            public void onAdClicked(Ad ad) {
////                // Ad clicked callback
////            }
////
////            @Override
////            public void onLoggingImpression(Ad ad) {
////                // Ad impression logged callback
////            }
////        });
////
////        // For auto play video ads, it's recommended to load the ad
////        // at least 30 seconds before it is shown
////        //fb_interstitialAd.loadAd();
////
////
////        if(!app_is_run_from_notification && count%2==0){
////            //todo fb inter requested here
////
////            requestNewInterstitial();
////        }
////
////
////
////
////        /*DisplayMetrics displayMetrics = new DisplayMetrics();
////        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
////        int height = displayMetrics.heightPixels;
////        int width = displayMetrics.widthPixels;
////        Log.e("metrics","height:"+height+"...width:"+width);
////        //fb banner
////        if(width>=720)
////            adView = new AdView(this, Utility.FACEBOOK_BANNER, AdSize.BANNER_HEIGHT_90);
////        else
////            adView = new AdView(this, Utility.FACEBOOK_BANNER, AdSize.BANNER_HEIGHT_50);
////        */
//////        if(getResources().getDisplayMetrics().density>2)//xhdpi
//////            adView = new AdView(this, Utility.FACEBOOK_BANNER, AdSize.BANNER_HEIGHT_90);
//////        else
//////            adView = new AdView(this, Utility.FACEBOOK_BANNER, AdSize.BANNER_HEIGHT_50);
//////
//////        // Find the Ad Container
//////        LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container);
//////        // Add the ad view to your activity layout
//////        adContainer.addView(adView);
//////        // Request an ad
//////        adView.loadAd();
////
////        mAdView = (com.google.android.gms.ads.AdView) findViewById(R.id.adView);
////
////        adRequest = new AdRequest.Builder().build();
////
////
////        mAdView.loadAd(adRequest);
////    }
//
//    void ADS(final boolean app_is_run_from_notification) {
//
//
//        mInterstitialAd = new com.google.android.gms.ads.InterstitialAd(this);
//        mInterstitialAd.setAdUnitId(Utility.INTERSTITIAL);
//        mInterstitialAd.setAdListener(new AdListener() {
//            @Override
//            public void onAdClosed() {
//                //requestNewInterstitial();
//            }
//
//            @Override
//            public void onAdLoaded() {
//                super.onAdLoaded();
//                //showAd();
//                Utility.counter(getApplication(), MainActivity.this);
//            }
//
//
//        });
//
//        if (!app_is_run_from_notification)
//
//            requestNewInterstitial();
//
//
//        mAdView = (com.google.android.gms.ads.AdView) findViewById(R.id.adView);
//
//        adRequest = new AdRequest.Builder().build();
//
//
//        mAdView.loadAd(adRequest);
//
//    }
//
//
//    private void requestNewInterstitial() {
//        AdRequest adRequest = new AdRequest.Builder()
//                ///  . addTestDevice("5729751E6F9F32A4DAB0DD2BC87D7E15")
//                .build();
//
//        mInterstitialAd.loadAd(adRequest);
//        //     fb_interstitialAd.loadAd();
//    }
//
//
//    @Override
//    public boolean showAd() {
//        if (mInterstitialAd.isLoaded() && !isFinishing() && has_focus) {
//            mInterstitialAd.show();
//            Log.e("inter", "has shown");
//            return true;
//        } else
//            Log.e("inter", "not shown");
//
//        return false;
//    }
//
//
//    @Override
//    public void onDestroy() {
////        if (mAdView != null) {
////            mAdView.destroy();
////        }
//
//
//        super.onDestroy();
//        setVisible(false);
//        //set_azan_off();
//    }
//
//    private void create_cancel_option_in_activity() {
//
//
//        if (dialog != null && dialog.isShowing())
//            return;
//
//        dialog = new AlertDialog.Builder(this)
//
//                .setTitle("আজান চলছে । আপনি কি আজান বন্ধ করতে চান?")
//                .setPositiveButton("হ্যাঁ", null) //Set to null. We override the onclick
//                .setNegativeButton("না", null)
//                .create();
//
//        dialog.setCancelable(false);
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
//
//            @Override
//            public void onShow(final DialogInterface dialog) {
//
//                Button yes = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
//                yes.setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View view) {
//
//                        Intent dismiss_button_Intent = new Intent(MainActivity.this, DismissButtonReceiver.class);
//                        dismiss_button_Intent.putExtra("notificationId", sharedPreferences.getInt("notificationId", 0));
//                        sendBroadcast(dismiss_button_Intent);
//                        set_azan_off();
//
//                        dialog.dismiss();
//                        //onResume();
//
//
//                        //finish();
//                    }
//                });
//
//                Button no = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
//                no.setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View view) {
//                        dialog.dismiss();
//
//                    }
//                });
//            }
//        });
//
//
//        if (!azan_running()) {
//            dialog.dismiss();
//        } else if (!isFinishing())
//            dialog.show();
//
//
//    }
//
//
//    private void appOpeningCount() {
//
//        count = sharedPreferences.getInt("count", 0);
//        count++;
//        sharedPreferences.edit().putInt("count", count).apply();
//    }
//}
