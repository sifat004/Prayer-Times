package com.creativeapps.salat_times.TabFragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.creativeapps.salat_times.Model.DateCell;
import com.creativeapps.salat_times.R;
import com.creativeapps.salat_times.UtilityPackage.Fonts;
import com.creativeapps.salat_times.UtilityPackage.Utility;

import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Sifat Ullah on 5/17/2018.
 */

public class TabCalendarFragment extends android.support.v4.app.Fragment {

    Context context;
    Activity rootActivity;
    View rootView;


    private SharedPreferences sharedPreferences;
    public static final String mypreference = "salat";

    private static final int SEHRI_GUARD =6 ;
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


        rootView = inflater.inflate(R.layout.calendar, container, false);
        rootActivity = getActivity();
        context = rootView.getContext();
        sharedPreferences = rootView.getContext().getSharedPreferences(mypreference,
                MODE_PRIVATE);


        LinearLayout main_background= rootView.findViewById(R.id.main_background);
        main_background.setBackground(Utility.getBackgroundGradient(context));

        setCalendar(rootView);




        return rootView;
    }



    private void setCalendar(View rootView) {
        ArrayList<DateCell> dateCells = new ArrayList<>();

        LinearLayout cell;
        LinearLayout day;

        TextView engDate,  arbDate,engMonth,month_name,download;
        month_name= rootView.findViewById(R.id.month_name);
        download= rootView.findViewById(R.id.get_calendar);
        FloatingActionButton downloadCal= rootView.findViewById(R.id.fab_down);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) downloadCal.getLayoutParams();

            params.setMargins(
                    params.leftMargin- ((int) (5 * 3.1)),
                    params.topMargin -((int) (3 * 3.1)),      //Approximate factor to shrink the extra
                    params.rightMargin - ((int) (5* 3.1)),    //spacing by the shadow  to the actual
                    params.bottomMargin- ((int) (3* 3.1)));  //size of the FAB without a shadow

            downloadCal.setLayoutParams(params);


        }
        downloadCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                try {
                    Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.amaderlab.amar_calendar");
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    // To count with Play market backstack, After pressing back button,
                    // to taken back to our application, we need to add following flags to intent.
                    startActivity(goToMarket);
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.amaderlab.amar_calendar")));
                }

            }});

        RelativeLayout boundingBox;
        DateCell dateCell;
        Fonts fonts =new Fonts(context);

        month_name.setTypeface(fonts.bensen());
        download.setTypeface(fonts.bensen());



        for (int i = 0; i < 35; i++) {

            cell = (LinearLayout) rootView.findViewById(getCellId(i + 1));
            boundingBox = (RelativeLayout) cell.findViewById(R.id.date_element);

            engDate = (TextView) boundingBox.findViewById(R.id.eng_date);
            arbDate = (TextView) boundingBox.findViewById(R.id.hijri_date);
            engMonth = (TextView) boundingBox.findViewById(R.id.eng_new_month_name);



            dateCell = new DateCell(engDate, arbDate,engMonth, boundingBox);
            dateCells.add(dateCell);
        }

        for (int i = 0; i < 7; i++) {

            day = (LinearLayout) rootView.findViewById(getDayId(i + 1));
            TextView dayName = (TextView) day.findViewById(R.id.eng_date);
            dayName.setText(getDayInfo(i + 1));
            dayName.setTypeface(fonts.bensen());
            // dayName.setTextColor(mContext.getResources().getColor(R.color.background6));
            dayName.setTextSize(14);


        }



        int no_of_ramadan_days_passed=0;
        TextView tv_engDate, tv_arbDate, tv_engMonth;
        RelativeLayout dateElement;
        Calendar calendar=Calendar.getInstance();
        for (int i=FIRST_DAY_OF_RAMADAN;i<=29+FIRST_DAY_OF_RAMADAN;i++){

            tv_arbDate= dateCells.get(i).getArbDate();
            tv_engMonth=dateCells.get(i).getEngMonth();
            tv_engDate= dateCells.get(i).getEngDate();
            dateElement=dateCells.get(i).getBoundingBox();


            tv_engDate.setTypeface(fonts.FutureCndNormal());
            tv_arbDate.setTypeface(fonts.bensen());
            tv_engMonth.setTypeface(fonts.FutureCndNormal());

            tv_arbDate.setText(converted_string_bengali(String.valueOf(no_of_ramadan_days_passed+1)));
            tv_engDate.setText(String.valueOf(getEngDateFromArab(no_of_ramadan_days_passed).get(Calendar.DAY_OF_MONTH)));


            if (i==FIRST_DAY_OF_RAMADAN||getEngDateFromArab(no_of_ramadan_days_passed).get(Calendar.DAY_OF_MONTH)==1){

                if (getEngDateFromArab(no_of_ramadan_days_passed).get(Calendar.MONTH)==4)
                    tv_engMonth.setText("MAY");
                else if (getEngDateFromArab(no_of_ramadan_days_passed).get(Calendar.MONTH)==5)
                    tv_engMonth.setText("JUNE");




            }


            if (Integer.parseInt(tv_engDate.getText().toString())==calendar.get(Calendar.DAY_OF_MONTH) &&
                    getEngDateFromArab(no_of_ramadan_days_passed).get(Calendar.MONTH)==calendar.get(Calendar.MONTH))

            {
                dateElement.setBackgroundColor(getResources().getColor(R.color.grad_1));
                sharedPreferences.edit().putInt("today_ramadan",Integer.parseInt(tv_arbDate.getText().toString())).apply();
            }

            no_of_ramadan_days_passed++;


        }
    }

    private Calendar getEngDateFromArab(int no_of_ramadan_day_passed) {

        int days=RAMADAN_STARTING_DATE+ no_of_ramadan_day_passed;
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.YEAR,ENGLISH_YEAR);
        calendar.set(Calendar.MONTH,RAMADAN_STARTING_MONTH);

        if (days> calendar.getActualMaximum(Calendar.DAY_OF_MONTH)){
            days=days-calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

            calendar.set(Calendar.MONTH,RAMADAN_STARTING_MONTH+1);

        }

        calendar.set(Calendar.DAY_OF_MONTH,days);

        return calendar;

    }


    private int getCellId(int i) {

        //  Log.e("calendar","getID");

        switch (i) {
            case 1:
                return R.id.cell_1;
            case 2:
                return R.id.cell_2;
            case 3:
                return R.id.cell_3;
            case 4:
                return R.id.cell_4;
            case 5:
                return R.id.cell_5;
            case 6:
                return R.id.cell_6;
            case 7:
                return R.id.cell_7;
            case 8:
                return R.id.cell_8;
            case 9:
                return R.id.cell_9;
            case 10:
                return R.id.cell_10;
            case 11:
                return R.id.cell_11;
            case 12:
                return R.id.cell_12;
            case 13:
                return R.id.cell_13;
            case 14:
                return R.id.cell_14;
            case 15:
                return R.id.cell_15;
            case 16:
                return R.id.cell_16;
            case 17:
                return R.id.cell_17;
            case 18:
                return R.id.cell_18;
            case 19:
                return R.id.cell_19;
            case 20:
                return R.id.cell_20;
            case 21:
                return R.id.cell_21;
            case 22:
                return R.id.cell_22;
            case 23:
                return R.id.cell_23;
            case 24:
                return R.id.cell_24;
            case 25:
                return R.id.cell_25;
            case 26:
                return R.id.cell_26;
            case 27:
                return R.id.cell_27;
            case 28:
                return R.id.cell_28;
            case 29:
                return R.id.cell_29;
            case 30:
                return R.id.cell_30;
            case 31:
                return R.id.cell_31;
            case 32:
                return R.id.cell_32;
            case 33:
                return R.id.cell_33;
            case 34:
                return R.id.cell_34;
            case 35:
                return R.id.cell_35;


            default:
                return 0;


        }

    }

    private int getDayId(int i) {


        switch (i) {


            case 1:
                return R.id.day1;
            case 2:
                return R.id.day2;
            case 3:
                return R.id.day3;
            case 4:
                return R.id.day4;
            case 5:
                return R.id.day5;
            case 6:
                return R.id.day6;
            case 7:
                return R.id.day7;

            default:
                return 0;
        }

    }

    private String getDayInfo(int i) {

        switch (i) {

            case 1:
                return "শনি";
            case 2:
                return "রবি";
            case 3:
                return "সোম";
            case 4:
                return "মঙ্গল";
            case 5:
                return "বুধ";
            case 6:
                return "বৃহঃ";
            case 7:
                return "শুক্র";
            default:
                return "";


        }
    }

    public String converted_string_bengali(String string) {
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
        return string;
    }


}