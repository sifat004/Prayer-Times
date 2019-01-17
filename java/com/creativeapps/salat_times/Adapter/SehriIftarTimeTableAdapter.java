package com.creativeapps.salat_times.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.creativeapps.salat_times.Model.RamadanTimeTable;
import com.creativeapps.salat_times.R;
import com.creativeapps.salat_times.UtilityPackage.Fonts;

import java.util.ArrayList;

/**
 * Created by Sifat Ullah on 5/17/2018.
 */

public class SehriIftarTimeTableAdapter extends ArrayAdapter<RamadanTimeTable> {

    private ArrayList<RamadanTimeTable> dataSet;
    private Context mContext;
    private SharedPreferences sharedPreferences;
    private static final String mypreference = "salat";

    private static class ViewHolder {
        TextView ramadan_date;
        TextView eng_date;
        TextView beng_day;
        TextView sehri_last_time;
        TextView iftar_time;
        LinearLayout row;


    }

    public SehriIftarTimeTableAdapter(ArrayList<RamadanTimeTable> dataList, Context context) {
        super(context, R.layout.sehri_iftr_list, dataList);

        //remove the haram times from list



        this.dataSet = dataList;
        this.mContext = context;

    }

    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public RamadanTimeTable getItem(int position) {
        return dataSet.get(position);
    }

    @Override
    public long getItemId(int position) {
        return dataSet.hashCode();
    }


    @NonNull
    @Override
    public View getView(final int position, View roottView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        sharedPreferences = mContext.getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

        final RamadanTimeTable dataModel = getItem(position);
        final SehriIftarTimeTableAdapter.ViewHolder viewHolder; // view lookup cache stored in tag


        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Fonts fonts = new Fonts(mContext);

        if (roottView == null) {
            viewHolder = new ViewHolder();
            roottView = inflater.inflate(R.layout.sehri_iftr_list, parent, false);
            viewHolder.ramadan_date = roottView.findViewById(R.id.ramadan_date);
            viewHolder.eng_date = roottView.findViewById(R.id.eng_date);
            viewHolder.beng_day =    roottView.findViewById(R.id.beng_day);
            viewHolder.sehri_last_time = roottView.findViewById(R.id.sehri_last_time);
            viewHolder.iftar_time =    roottView.findViewById(R.id.iftr_time);
            viewHolder.row= roottView.findViewById(R.id.list_item);

            viewHolder.beng_day.setTypeface(fonts.bensen());
            viewHolder.ramadan_date.setTypeface(fonts.bensen());
            viewHolder.eng_date.setTypeface(fonts.bensen());
            viewHolder.sehri_last_time.setTypeface(fonts.bensen());
            viewHolder.iftar_time.setTypeface(fonts.bensen());
            roottView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) roottView.getTag();
        }


        if (dataModel != null) {
            viewHolder.ramadan_date.setText(dataModel.getRamjanDate());
            viewHolder.eng_date.setText(dataModel.getDateString());
            viewHolder.beng_day.setText(dataModel.getDay());
            viewHolder.sehri_last_time.setText(converted_time_string_bengali(dataModel.getSehri_last_time()));
            viewHolder.iftar_time.setText(converted_time_string_bengali(dataModel.getIftar_last_gtime()));

        }


        if (dataSet.lastIndexOf(dataModel)+1==sharedPreferences.getInt("today_ramadan",0)){


            viewHolder.row.setBackgroundColor(mContext.getResources().getColor(R.color.grad_5));
        }






        return roottView;
    }

    private String converted_time_string_bengali(String string) {
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

    @Override
    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }
}
