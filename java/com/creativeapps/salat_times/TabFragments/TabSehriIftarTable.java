package com.creativeapps.salat_times.TabFragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.azan.PrayerTimes;
import com.azan.TimeCalculator;
import com.azan.types.PrayersType;
import com.creativeapps.salat_times.Adapter.SehriIftarTimeTableAdapter;
import com.creativeapps.salat_times.Location.LocationDetails;
import com.creativeapps.salat_times.Location.LocationUpdateListener;
import com.creativeapps.salat_times.Model.RamadanTimeTable;
import com.creativeapps.salat_times.R;
import com.creativeapps.salat_times.UtilityPackage.Fonts;
import com.creativeapps.salat_times.UtilityPackage.Utility;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import static android.content.Context.MODE_PRIVATE;
import static com.azan.types.AngleCalculationType.EGYPT;
import static com.azan.types.AngleCalculationType.ISNA;
import static com.azan.types.AngleCalculationType.KARACHI;
import static com.azan.types.AngleCalculationType.MUHAMMADIYAH;
import static com.azan.types.AngleCalculationType.MWL;

/**
 * Created by Sifat Ullah on 5/17/2018.
 */

public class TabSehriIftarTable extends android.support.v4.app.Fragment {

    Context context;
    Activity rootActivity;
    View rootView;


    private SharedPreferences sharedPreferences;
    public static final String mypreference = "salat";

    private static final int SEHRI_GUARD_MINS =5 ;
    private  static int FIRST_DAY_OF_RAMADAN= 4 ;     //0 for sat,1 for sun......... ,6 for fri
    private  static int RAMADAN_STARTING_MONTH = 4 ;  //0 for jan, 1 for feb......., 11 for dec
    private  static int RAMADAN_ENDING_MONTH = 5 ;
    private  static int RAMADAN_STARTING_DATE = 18 ;
    private  static int RAMADAN_29_DATE  ;
    private  static int ENGLISH_YEAR=2018  ;
    private  static int HIJRI_YEAR=1439 ;
    private static final int RAMADAN_STARTING_MONTH_TOTALDAYS =31 ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        rootView = inflater.inflate(R.layout.fragment_sehri_iftar, container, false);
        rootActivity = getActivity();
        context = rootView.getContext();
        sharedPreferences = rootView.getContext().getSharedPreferences(mypreference,
                MODE_PRIVATE);

        LinearLayout main_background= rootView.findViewById(R.id.main);
        main_background.setBackground(Utility.getBackgroundGradient(context));


        LinearLayout listhead= rootView.findViewById(R.id.list_head);
        listhead.setBackgroundColor(context.getResources().getColor(R.color.grad_3));

        String city=sharedPreferences.getString("city", "");
        TextView locationTv = rootView.findViewById(R.id.loc);

        if (city.equals("")){
            locationTv.setVisibility(View.INVISIBLE);
        }

        else {

            String s= "আপনার অবস্থানঃ "+ city;
            locationTv.setVisibility(View.VISIBLE);

            locationTv.setText(s);

        }

        TextView ramadan_date;
        TextView eng_date;
        TextView beng_day;
        TextView sehri_last_time;
        TextView iftar_time;

       ramadan_date = rootView.findViewById(R.id.ramadan_date);
        eng_date = rootView.findViewById(R.id.eng_date);
        beng_day =    rootView.findViewById(R.id.beng_day);
      sehri_last_time = rootView.findViewById(R.id.sehri_last_time);
        iftar_time =    rootView.findViewById(R.id.iftr_time);

        Fonts fonts= new Fonts(context);

        beng_day.setTypeface(fonts.bensen());
        ramadan_date.setTypeface(fonts.bensen());
        eng_date.setTypeface(fonts.bensen());
        sehri_last_time.setTypeface(fonts.bensen());
        iftar_time.setTypeface(fonts.bensen());
        locationTv.setTypeface(fonts.bensen());

                setTable(rootView);




        return rootView;
    }



    private void setTable(View rootView) {

        ListView listView = rootView.findViewById(R.id.sehri_iftar_table);
        SehriIftarTimeTableAdapter sehriIftarTimeTableAdapter=new SehriIftarTimeTableAdapter(ramadanTimeTableArrayList() ,context);
        listView.setAdapter(sehriIftarTimeTableAdapter);
    }

    private ArrayList<RamadanTimeTable> ramadanTimeTableArrayList() {

        LocationDetails locationDetails=new LocationDetails(context, rootActivity, sharedPreferences,
                new LocationUpdateListener() {
                    @Override
                    public void onLocationUpdated(Location location, FusedLocationProviderClient mFusedLocationClient, LocationCallback locationCallback) {

                    }
                });

        GregorianCalendar date = new GregorianCalendar();

        ArrayList<RamadanTimeTable> ramadanTimeTablesList=new ArrayList<>();
        RamadanTimeTable ramadanTimeTable;
        int ramadan_date=0;
        long sehri_time_millis,iftar_time_millis;


        for (int i=RAMADAN_STARTING_DATE;i<=RAMADAN_STARTING_MONTH_TOTALDAYS;i++){



            ramadan_date++;
            date.set(ENGLISH_YEAR,RAMADAN_STARTING_MONTH,i);

            PrayerTimes prayerTimes = getPrayerTimesObject(locationDetails.getLocation(sharedPreferences),date);
            sehri_time_millis=prayerTimes.getPrayTime(PrayersType.FAJR).getTime()- SEHRI_GUARD_MINS *60*1000;

            iftar_time_millis=prayerTimes.getPrayTime(PrayersType.MAGHRIB).getTime();

            ramadanTimeTable=new RamadanTimeTable(ramadan_date,i,RAMADAN_STARTING_MONTH,getDayInfo(date.get(Calendar.DAY_OF_WEEK)),getTime12hourFormat(sehri_time_millis),getTime12hourFormat(iftar_time_millis));

            ramadanTimeTablesList.add(ramadanTimeTable);


        }


        for (int i=1;i<30-(RAMADAN_STARTING_MONTH_TOTALDAYS-RAMADAN_STARTING_DATE);i++){



            ramadan_date++;
            date.set(ENGLISH_YEAR,RAMADAN_ENDING_MONTH,i);

            PrayerTimes prayerTimes = getPrayerTimesObject(locationDetails.getLocation(sharedPreferences),date);
            sehri_time_millis=prayerTimes.getPrayTime(PrayersType.FAJR).getTime()- SEHRI_GUARD_MINS *60*1000;

            iftar_time_millis=prayerTimes.getPrayTime(PrayersType.MAGHRIB).getTime();

            ramadanTimeTable=new RamadanTimeTable(ramadan_date,i,RAMADAN_ENDING_MONTH,getDayInfo(date.get(Calendar.DAY_OF_WEEK)),getTime12hourFormat(sehri_time_millis),getTime12hourFormat(iftar_time_millis));

            ramadanTimeTablesList.add(ramadanTimeTable);
        }


        return ramadanTimeTablesList;



    }


    public PrayerTimes getPrayerTimesObject(Location location,GregorianCalendar date) {
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

    public int getRule() {
        //Log.i(TAG, "get_rule" + sharedPreferences.getInt("rule", 4));
        return sharedPreferences.getInt("rule", 4);
    }


    public String getTime12hourFormat(long millis) {
        SimpleDateFormat localDateFormat = new SimpleDateFormat("hh:mm", Locale.US);




        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);

        String time = localDateFormat.format(millis);


//        int mYear = calendar.get(Calendar.YEAR);
//        int mMonth = calendar.get(Calendar.MONTH);
//        int mDay = calendar.get(Calendar.DAY_OF_MONTH);



        return time;
    }


    private String getDayInfo(int i) {

        switch (i) {

            case 7:
                return "শনি";
            case 1:
                return "রবি";
            case 2:
                return "সোম";
            case 3:
                return "মঙ্গল";
            case 4:
                return "বুধ";
            case 5:
                return "বৃহঃ";
            case 6:
                return "শুক্র";
            default:
                return "";


        }
    }

}