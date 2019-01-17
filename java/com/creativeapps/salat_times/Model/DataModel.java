package com.creativeapps.salat_times.Model;
import java.util.Date;
/**
 * Created by Sifat on 4/18/2017.
 */
public class DataModel {
    String name;
    String Azan_time_String;
    java.util.Date Azan_time;
    java.util.Date Next_Salat_Time;

    int tag;
    public static final String MyPREFERENCES = "MyPrefs" ;


    public DataModel(String name, String Azan_time_String, java.util.Date Azan_time, int tag,java.util.Date Next_Salat_Time) {
        this.name = name;
        this.Azan_time_String = Azan_time_String;
        this.Azan_time = Azan_time;
        this.Next_Salat_Time= Next_Salat_Time;

        this.tag = tag;
    }

    public String getName() {

        return name;
    }

    public String getAzan_time_String() {

        return Azan_time_String;
    }

    public java.util.Date getAzan_time() {

        return Azan_time;
    }

    public int getTag() {
        return tag;
    }

    public Date getNext_Salat_Time() {
        return Next_Salat_Time;
    }
}