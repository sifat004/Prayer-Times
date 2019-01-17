//package com.creativeapps.salat_times.Activities;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.location.Location;
//import android.support.constraint.ConstraintLayout;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.widget.ListView;
//
//import com.azan.PrayerTimes;
//import com.azan.TimeCalculator;
//import com.azan.types.PrayersType;
//import com.creativeapps.salat_times.Adapter.SehriIftarTimeTableAdapter;
//import com.creativeapps.salat_times.Location.LocationDetails;
//import com.creativeapps.salat_times.Location.LocationUpdateListener;
//import com.creativeapps.salat_times.Model.RamadanTimeTable;
//import com.creativeapps.salat_times.R;
//import com.creativeapps.salat_times.UtilityPackage.Utility;
//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationCallback;
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
//public class SehriIftarActivity extends AppCompatActivity {
//
//    private static final int SEHRI_GUARD =6 ;
//    private  static int FIRST_DAY_OF_RAMADAN= 4 ;     //0 for sat,1 for sun......... ,6 for fri
//    private  static int RAMADAN_STARTING_MONTH = 4 ;  //0 for jan, 1 for feb......., 11 for dec
//    private  static int RAMADAN_ENDING_MONTH = 5 ;
//    private  static int RAMADAN_STARTING_DATE = 18 ;
//    private  static int RAMADAN_29_DATE  ;
//    private  static int ENGLISH_YEAR=2018  ;
//    private  static int HIJRI_YEAR=1439 ;
//    private static final int RAMADAN_STARTING_MONTH_TOTALDAYS =31 ;
//
//
//    private SharedPreferences sharedPreferences;
//    public static final String mypreference = "salat";
//
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_sehri_iftar);
//
//        sharedPreferences = getSharedPreferences(mypreference,
//                Context.MODE_PRIVATE);
//
//       ConstraintLayout main_background= findViewById(R.id.main);
//        main_background.setBackground(Utility.getBackgroundGradient(this));
//
//        setTable();
//
//
//    }
//
//        private void setTable() {
//
//        ListView listView = findViewById(R.id.sehri_iftar_table);
//        SehriIftarTimeTableAdapter sehriIftarTimeTableAdapter=new SehriIftarTimeTableAdapter(ramadanTimeTableArrayList() ,this);
//        listView.setAdapter(sehriIftarTimeTableAdapter);
//    }
//
//    private ArrayList<RamadanTimeTable> ramadanTimeTableArrayList() {
//
//        LocationDetails locationDetails=new LocationDetails(this, this, sharedPreferences,
//                new LocationUpdateListener() {
//                    @Override
//                    public void onLocationUpdated(Location location, FusedLocationProviderClient mFusedLocationClient, LocationCallback locationCallback) {
//
//                    }
//                });
//
//        GregorianCalendar date = new GregorianCalendar();
//
//        ArrayList<RamadanTimeTable> ramadanTimeTablesList=new ArrayList<>();
//        RamadanTimeTable ramadanTimeTable;
//        int ramadan_date=0;
//        long sehri_time_millis,iftar_time_millis;
//        for (int i=RAMADAN_STARTING_DATE;i<=RAMADAN_STARTING_MONTH_TOTALDAYS;i++){
//
//
//
//            ramadan_date++;
//            date.set(ENGLISH_YEAR,RAMADAN_STARTING_MONTH,i);
//
//            PrayerTimes prayerTimes = getPrayerTimesObject(locationDetails.getLocation(sharedPreferences),date);
//            sehri_time_millis=prayerTimes.getPrayTime(PrayersType.FAJR).getTime()-SEHRI_GUARD*60*1000;
//
//            iftar_time_millis=prayerTimes.getPrayTime(PrayersType.MAGHRIB).getTime();
//
//            ramadanTimeTable=new RamadanTimeTable(ramadan_date,i,RAMADAN_STARTING_MONTH,getDayInfo(date.get(Calendar.DAY_OF_WEEK)),getTime12hourFormat(sehri_time_millis),getTime12hourFormat(iftar_time_millis));
//
//            ramadanTimeTablesList.add(ramadanTimeTable);
//        }
//
//
//        for (int i=1;i<30-(RAMADAN_STARTING_MONTH_TOTALDAYS-RAMADAN_STARTING_DATE);i++){
//
//
//
//            ramadan_date++;
//            date.set(ENGLISH_YEAR,RAMADAN_ENDING_MONTH,i);
//
//            PrayerTimes prayerTimes = getPrayerTimesObject(locationDetails.getLocation(sharedPreferences),date);
//            sehri_time_millis=prayerTimes.getPrayTime(PrayersType.FAJR).getTime()-SEHRI_GUARD*60*1000;
//
//            iftar_time_millis=prayerTimes.getPrayTime(PrayersType.MAGHRIB).getTime();
//
//            ramadanTimeTable=new RamadanTimeTable(ramadan_date,i,RAMADAN_ENDING_MONTH,getDayInfo(date.get(Calendar.DAY_OF_WEEK)),getTime12hourFormat(sehri_time_millis),getTime12hourFormat(iftar_time_millis));
//
//            ramadanTimeTablesList.add(ramadanTimeTable);
//        }
//
//
//        return ramadanTimeTablesList;
//
//
//
//    }
//
//
//    public PrayerTimes getPrayerTimesObject(Location location,GregorianCalendar date) {
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
//
//    public int getRule() {
//        //Log.i(TAG, "get_rule" + sharedPreferences.getInt("rule", 4));
//        return sharedPreferences.getInt("rule", 4);
//    }
//
//
//    public String getTime12hourFormat(long millis) {
//        SimpleDateFormat localDateFormat = new SimpleDateFormat("hh:mm", Locale.US);
//
//
//
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(millis);
//
//        String time = localDateFormat.format(millis);
//
//
////        int mYear = calendar.get(Calendar.YEAR);
////        int mMonth = calendar.get(Calendar.MONTH);
////        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
//
//
//
//        return time;
//    }
//
//
//    private String getDayInfo(int i) {
//
//        switch (i) {
//
//            case 1:
//                return "শনি";
//            case 2:
//                return "রবি";
//            case 3:
//                return "সোম";
//            case 4:
//                return "মঙ্গল";
//            case 5:
//                return "বুধ";
//            case 6:
//                return "বৃহঃ";
//            case 7:
//                return "শুক্র";
//            default:
//                return "";
//
//
//        }
//    }
//
//
//
//}
