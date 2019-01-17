package com.creativeapps.salat_times.Model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Sifat Ullah on 5/17/2018.
 */

public class RamadanTimeTable {


    private String ramjanDate;
    private String engDate;
    private  String enngMonth;
    private String day;
    private String sehri_last_time;
    private String iftar_last_gtime;
    private  int ramadan_date_int;

    public String getEnngMonth() {
        return enngMonth;
    }

    public int getRamadan_date_int() {
        return ramadan_date_int;
    }

    public RamadanTimeTable(int ramadan_date, int engDate, int ramadanStartingMonth, String bengaliDay, String sehri_last_time, String iftar_last_gtime) {
        this.ramjanDate = converted_time_string_bengali(String.valueOf(ramadan_date));
        this.engDate = converted_time_string_bengali(String.valueOf(engDate));
        this.enngMonth=getMonthName(ramadanStartingMonth);
        this.day = bengaliDay;
        this.sehri_last_time = sehri_last_time;
        this.iftar_last_gtime = iftar_last_gtime;
        this.ramadan_date_int=ramadan_date;
    }


    public String getDateString(){

        return this.engDate+", "+this.enngMonth;
    }
    private String getMonthName(int ramadanStartingMonth) {

        switch (ramadanStartingMonth){


            case 0: return "জানুয়ারি";
            case 1: return "ফেব্রুয়ারি";
            case 2: return "মার্চ";
            case 3: return "এপ্রিল";
            case 4: return "মে";
            case 5: return "জুন";
            case 6: return "জুলাই";
            case 7: return "আগস্ট";
            case 8: return "সেপ্টেম্বার";
            case 9: return "অক্টোবর";
            case 10: return "নভেম্বার";
            case 11: return "ডিসেম্বার";

            default: return "";

        }
    }



    public String getRamjanDate() {
        return ramjanDate;
    }

    public void setRamjanDate(String ramjanDate) {
        this.ramjanDate = ramjanDate;
    }

    public String getEngDate() {
        return engDate;
    }

    public void setEngDate(String engDate) {
        this.engDate = engDate;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getSehri_last_time() {
        return sehri_last_time;
    }

    public void setSehri_last_time(String sehri_last_time) {
        this.sehri_last_time = sehri_last_time;
    }

    public String getIftar_last_gtime() {
        return iftar_last_gtime;
    }

    public void setIftar_last_gtime(String iftar_last_gtime) {
        this.iftar_last_gtime = iftar_last_gtime;
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
        return string;
    }
}
