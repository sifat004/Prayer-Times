package com.creativeapps.salat_times.Model;

import android.util.Log;

import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.chrono.IslamicChronology;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Sifat Ullah on 2/10/2018.
 */

public class ArabicDate {


    private String[] hijri_months = new  String[]{"মুহররম", "সফর", "রবিউল আউয়াল", "রবিউস সানি", "জমাদিউল আউয়াল", "জমাদিউস সানি", "রজব", "শাবান", "রমজান", "শাওয়াল", "জিলক্বদ", "জিলহজ্জ"};

    private Date date;
    public ArabicDate( int hijri_correction) {




        Calendar calendar_instance= Calendar.getInstance();
        calendar_instance.set(Calendar.SECOND,calendar_instance.get(Calendar.SECOND)+hijri_correction*24*60 *60);

        date= calendar_instance.getTime();

        // date= new Date( calendar_instance.get(Calendar.SECOND)+(hijri_correction*24*60 *60* 1000));


    }

    private LocalDate hijri_date() {
        DateTimeZone datetimeZone = DateTimeZone.forID("Asia/Dhaka");
        Chronology iso = ISOChronology.getInstance(datetimeZone);
        Chronology hijri = IslamicChronology.getInstance(datetimeZone);
        LocalDate todayIso = new LocalDate(date, iso);
        LocalDate todayHijri = new LocalDate(todayIso.toDateTimeAtStartOfDay(),
                hijri);


        return todayHijri;
    }


    private String getArabDate() {


        return String.valueOf(hijri_date().getDayOfMonth());

    }



    private String getArabMonth() {

        return String.valueOf(hijri_months[hijri_date().getMonthOfYear()-1]);
    }


    private String getArabYear() {
        return String.valueOf(hijri_date().getYear());

    }

    public String getArabDateString() {
        return getArabDate()+"-"+getArabMonth()+", "+getArabYear()+" হিজরি";

    }
}
