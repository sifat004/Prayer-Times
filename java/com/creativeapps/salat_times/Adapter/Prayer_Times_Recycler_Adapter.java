
/**
 * Created by Sifat on 4/16/2017.
 */

package com.creativeapps.salat_times.Adapter;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.creativeapps.salat_times.Activities.SetAlarmActivity;
import com.creativeapps.salat_times.Model.DataModel;
import com.creativeapps.salat_times.R;
import com.creativeapps.salat_times.RecieversAndService.AlarmReciever;
import com.creativeapps.salat_times.UtilityPackage.Fonts;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class Prayer_Times_Recycler_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {


    private ArrayList<DataModel> dataSet;
    private Context mContext;
    private SharedPreferences sharedPreferences;
    private static final String mypreference = "salat";

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dummy_prayer_list_item, parent, false);
        SalatItemViewHolder viewHolder = new SalatItemViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        sharedPreferences = mContext.getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

        final DataModel dataModel = dataSet.get(position);
        final SalatItemViewHolder viewHolder=(SalatItemViewHolder) holder; // view lookup cache stored in tag




        Fonts fonts = new Fonts(mContext);



            viewHolder.salat_name_text.setTypeface(fonts.bensen());
            viewHolder.salat_time_text.setTypeface(fonts.bensen());
            viewHolder.alarm_button.setTag(position);
           // roottView.setTag(viewHolder);
//        } else {
//            viewHolder = (Prayer_Times_List_Adapter.ViewHolder) roottView.getTag();
//        }


        if (fiveAlarmsActive()){

            switch (dataModel.getTag()){

                case 1:
                case 6:
                case 7:
                case 9:
                    set_alarm_for_five_alarms(dataModel);
                    break;
                case 11:

                    set_alarm_for_five_alarms(dataModel);
                    sharedPreferences.edit().putBoolean("five_alarms",false).apply();

                    break;
                default:
                    break;


            }




        }



        if (get_everydayActive(dataModel.getName()) || get_nextdayActive(dataModel.getName()) || get_isAlarmActive(dataModel)) {
            viewHolder.alarm_button.setImageResource(R.drawable.alarm_on);
            //  viewHolder.alarm_button.startAnimation(shake);
        } else viewHolder.alarm_button.setImageResource(R.drawable.alarm_off);


        if (dataModel.getTag() == 1) viewHolder.salat_name_text.setText("ফজর");


        else if (dataModel.getTag() == 2) viewHolder.salat_name_text.setText("সূর্যোদয়");
        else if (dataModel.getTag() == 3) viewHolder.salat_name_text.setText("হারাম");
        else if (dataModel.getTag() == 4) viewHolder.salat_name_text.setText("ইশরাক");
        else if (dataModel.getTag() == 5) viewHolder.salat_name_text.setText("হারাম");

        else if (dataModel.getTag() == 6) viewHolder.salat_name_text.setText("যোহর");
        else if (dataModel.getTag() == 7) viewHolder.salat_name_text.setText("আছর");
        else if (dataModel.getTag() == 8) viewHolder.salat_name_text.setText("হারাম");

        else if (dataModel.getTag() == 9) viewHolder.salat_name_text.setText("মাগরিব");
        else if (dataModel.getTag() == 10) viewHolder.salat_name_text.setText("আউয়াবিন");

        else if (dataModel.getTag() == 11) viewHolder.salat_name_text.setText("ঈশা");


        else if (dataModel.getTag() == 12) viewHolder.salat_name_text.setText("তাহাজ্জুদ");

        viewHolder.salat_time_text.setText(converted_time_string_bengali(dataModel.getAzan_time_String()));
        viewHolder.alarm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                // openDialog(time,next_salat_time,viewHolder,dataModel);

                if (get_everydayActive(dataModel.getName()) || get_nextdayActive(dataModel.getName()) || get_isAlarmActive(dataModel)) {
                    //  viewHolder.alarm_button.startAnimation(shake);
                    {


                        cancelDialog(dataModel,viewHolder);
                        //cancel_azan_alarm(dataModel);
                        viewHolder.alarm_button.setImageResource(R.drawable.alarm_off);
                    }
                } else
                {

                    goToSetAlarm(viewHolder,dataModel);
                    viewHolder.alarm_button.setImageResource(R.drawable.alarm_on);

                }


            }

        });


    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }


    private static class SalatItemViewHolder extends RecyclerView.ViewHolder {
        TextView salat_name_text;
        TextView salat_time_text;
        ImageButton alarm_button;

        public SalatItemViewHolder(View itemView) {
            super(itemView);

            salat_name_text = itemView.findViewById(R.id.salat_name);
            salat_time_text = itemView.findViewById(R.id.salat_time);
            alarm_button =    itemView.findViewById(R.id.alarm_button);
        }
    }



    private void set_alarm_for_five_alarms(DataModel dataModel) {

        Log.e("five alarms", "set alarm");

        if(azanTimeAlreadyPassed(dataModel)){
            set_azan_alarm(dataModel.getAzan_time().getTime()+24*60*60*1000,dataModel.getTag(),dataModel.getName(),mContext);
            set_nextdayActive(dataModel.getName(),true);

        }
        else {
            set_azan_alarm(dataModel.getAzan_time().getTime(),dataModel.getTag(),dataModel.getName(),mContext);

        }
        set_isAlarmActive(dataModel,true);
        set_everydayActive(dataModel.getName(),true);
    }

    private boolean fiveAlarmsActive() {
        return  sharedPreferences.getBoolean("five_alarms",false);
    }

    private void cancelDialog(final DataModel dataModel, final SalatItemViewHolder viewHolder) {


        final AlertDialog.Builder cancelDialog = new AlertDialog.Builder(mContext);
        cancelDialog.setTitle("");
        cancelDialog.setMessage("অ্যালার্ম টি বাদ দিন");
        cancelDialog.setPositiveButton("হ্যাঁ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                cancel_azan_alarm(dataModel);




            }
        });

        cancelDialog.setNegativeButton("না", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        cancelDialog.show();
    }


    private void setNotification(DataModel dataModel) {
        Intent intentAlarm = new Intent(mContext, AlarmReciever.class);
        intentAlarm.putExtra("tag", dataModel.getTag());
        intentAlarm.putExtra("tone", 0);
        intentAlarm.putExtra("name", dataModel.getName());

        set_tone(dataModel.getName(),0);
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);

        int tag= dataModel.getTag();

        long noti_time= dataModel.getAzan_time().getTime();
        if (azanTimeAlreadyPassed(dataModel)) {
            noti_time+=24*60*60*1000;
            set_nextdayActive(dataModel.getName(),true);
        }
        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, noti_time, PendingIntent.getBroadcast(mContext, tag, intentAlarm, 0));
            }

            else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, noti_time, PendingIntent.getBroadcast(mContext, tag, intentAlarm, 0));
            }

            else {

                alarmManager.set(AlarmManager.RTC_WAKEUP, noti_time, PendingIntent.getBroadcast(mContext, tag, intentAlarm, 0));

            }


        }

        set_NotificationActive(dataModel,true);
        set_everydayActive(dataModel.getName(),true);

        Toast.makeText(mContext, "নীরব নোটিফিকেশন সেট করা হয়েছে", Toast.LENGTH_SHORT).show();


    }




    private void cancel_azan_alarm(DataModel dataModel) {
        Intent intentAlarm = new Intent(mContext, AlarmReciever.class);
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, dataModel.getTag(), intentAlarm, 0);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
        set_everydayActive(dataModel.getName(), false);
        set_isAlarmActive(dataModel, false);
        set_nextdayActive(dataModel.getName(), false);
        set_NotificationActive(dataModel,false);
        sharedPreferences.edit().putLong(dataModel.getName()+"time_adjust",0).apply();

        Toast.makeText(mContext, "অ্যালার্ম বন্ধ করা হয়েছে", Toast.LENGTH_LONG).show();
    }



    private boolean get_notificationActive(String name) {
        return sharedPreferences.getBoolean(name + "noti_active", false);

    }


    private void set_NotificationActive(DataModel dataModel, boolean bool) {
        // dataModel.setAlarmActive(isAlarmActive);
        sharedPreferences.edit().putBoolean(dataModel.getName() + "noti_active", bool).apply();
    }

    private void set_isAlarmActive(DataModel dataModel, boolean isAlarmActive) {
        // dataModel.setAlarmActive(isAlarmActive);
        sharedPreferences.edit().putBoolean(dataModel.getName() + "active", isAlarmActive).apply();
    }


    private boolean get_isAlarmActive(DataModel dataModel) {
        return sharedPreferences.getBoolean(dataModel.getName() + "active", false);
    }
    private void set_everydayActive(String name, boolean everydayActive) {
        sharedPreferences.edit().putBoolean(name + "everyday", everydayActive).apply();
    }

    private boolean get_everydayActive(String name) {
        return sharedPreferences.getBoolean(name + "everyday", false);
    }

    private void set_nextdayActive(String name, boolean everydayActive) {
        sharedPreferences.edit().putBoolean(name + "next_day", everydayActive).apply();
    }

    private boolean get_nextdayActive(String name) {
        return sharedPreferences.getBoolean(name + "next_day", false);
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




    private void goToSetAlarm(SalatItemViewHolder viewHolder, DataModel dataModel) {


        long time=dataModel.getAzan_time().getTime();
        Intent intent = new Intent(mContext, SetAlarmActivity.class);
        String string ;


        if (azanTimeAlreadyPassed(dataModel)) {


            if (System.currentTimeMillis() < dataModel.getNext_Salat_Time().getTime()) {
                string = viewHolder.salat_name_text.getText() + " এর ওয়াক্ত চলছে, পরবর্তী দিনের জন্য অ্যালার্ম সেট করুন ";

            } else string = viewHolder.salat_name_text.getText() + " এর ওয়াক্ত অতিবাহিত হয়ে গেছে, পরবর্তী দিনের জন্য অ্যালার্ম সেট করুন ";


            set_nextdayActive(dataModel.getName(), true);

            intent.putExtra("nextDay", true);
            intent.putExtra("salat_passed_string", string);

        } else {


            intent.putExtra("nextDay", false);
            intent.putExtra("salat_passed_string", "");

        }

        intent.putExtra("time", time);
        intent.putExtra("salat_name", viewHolder.salat_name_text.getText());
        intent.putExtra("tag", dataModel.getTag());
        intent.putExtra("name", dataModel.getName());

        mContext.startActivity(intent);
        set_NotificationActive(dataModel,false);


    }

    private boolean azanTimeAlreadyPassed(DataModel dataModel){
        return dataModel.getAzan_time().getTime() < System.currentTimeMillis();

    }

    private void set_tone(String name, int tone) {
        sharedPreferences.edit().putInt(name + "tone", tone).apply();
        // Log.e("set_alarm_activity","tone_set="+tone+" get_tone="+get_tone(name));
    }

    public void set_azan_alarm(long time, int tag, String name,Context context) {
        Intent intentAlarm = new Intent(context, AlarmReciever.class);
        intentAlarm.putExtra("tag", tag);
        intentAlarm.putExtra("tone", 5);
        intentAlarm.putExtra("name", name);
        intentAlarm.putExtra("time", time);


        set_tone(name,5);

        // Log.e("setAlarm","tone_set="+5+" tag="+tag+" name="+name);


        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(context, tag, intentAlarm, 0));
            }

            else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(context, tag, intentAlarm, 0));
            }

            else {

                alarmManager.set(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(context, tag, intentAlarm, 0));

            }


        }
        //Toast.makeText(this, "অ্যালার্ম সেট করা হয়েছে", Toast.LENGTH_SHORT).show();


    }



}