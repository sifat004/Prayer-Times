
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.creativeapps.salat_times.RecieversAndService.AlarmReciever;
import com.creativeapps.salat_times.Model.DataModel;
import com.creativeapps.salat_times.R;
import com.creativeapps.salat_times.Activities.SetAlarmActivity;
import com.creativeapps.salat_times.UtilityPackage.Fonts;

import java.util.ArrayList;

public class Prayer_Times_List_Adapter extends ArrayAdapter<DataModel> {

    private ArrayList<DataModel> dataSet;
    private Context mContext;
    private SharedPreferences sharedPreferences;
    private static final String mypreference = "salat";

    private static class ViewHolder {
        TextView salat_name_text;
        TextView salat_time_text;
        ImageButton alarm_button;
    }

    public Prayer_Times_List_Adapter(ArrayList<DataModel> data, Context context) {
        super(context, R.layout.prayer_list_item, data);

        //remove the haram times from list
        data.remove(7);
        data.remove(4);
        data.remove(2);

        this.dataSet = data;
        this.mContext = context;

    }


    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public DataModel getItem(int position) {
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

        final DataModel dataModel = getItem(position);
        final ViewHolder viewHolder; // view lookup cache stored in tag

        // Check if an existing view is being reused, otherwise inflate the view
//        final View result = roottView;
//        final Date date = dataModel.getAzan_time();
//        final Date date2 = dataModel.getNext_Salat_Time();
//        final long time = date.getTime();
//        final long next_salat_time = date2.getTime();
//        Calendar cal = Calendar.getInstance(); //current date and time
//        cal.set(Calendar.HOUR_OF_DAY, 23); //set hour to last hour
//        cal.set(Calendar.MINUTE, 59); //set minutes to last minute
//        cal.set(Calendar.SECOND, 59); //set seconds to last second
//        final long last_min_millis = cal.getTimeInMillis();


 //       final Animation shake = AnimationUtils.loadAnimation(mContext, R.anim.shake);

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Fonts fonts = new Fonts(mContext);

        if (roottView == null) {
            viewHolder = new ViewHolder();
            roottView = inflater.inflate(R.layout.dummy_prayer_list_item, parent, false);
            viewHolder.salat_name_text = roottView.findViewById(R.id.salat_name);
            viewHolder.salat_time_text = roottView.findViewById(R.id.salat_time);
            viewHolder.alarm_button =    roottView.findViewById(R.id.alarm_button);

            viewHolder.salat_name_text.setTypeface(fonts.bensen());
            viewHolder.salat_time_text.setTypeface(fonts.bensen());
            viewHolder.alarm_button.setTag(position);
            roottView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) roottView.getTag();
        }


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

//        if (get_notificationActive(dataModel.getName())) {
//            viewHolder.alarm_button.setImageResource(R.drawable.ic_notifications_white_36dp);
//
//        }

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


//
//                if (!get_isAlarmActive(dataModel) && !get_everydayActive(dataModel.getName()) && !get_nextdayActive(dataModel.getName())) {
////                    AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
////                    dialog.setTitle("অ্যালার্ম");
////                    dialog.setMessage("এই সালাতের জন্য অ্যালার্ম সেট করুন " );
////                    dialog.setPositiveButton("ঠিক আছে", new DialogInterface.OnClickListener() {
////                        @Override
////                        public void onClick(DialogInterface dialog, int which) {
//                    if (time < System.currentTimeMillis()) {
//                        AlertDialog.Builder dialog2 = new AlertDialog.Builder(v.getContext());
//                        dialog2.setTitle("");
//
////                                if(dataModel.getTag()==6 &&System.currentTimeMillis()<last_min_millis ){
////                                    dialog2.setMessage(viewHolder.salat_name_text.getText()+" এর ওয়াক্ত চলছে ");
////
////                                }
//
//                        if (System.currentTimeMillis() < next_salat_time) {
//                            dialog2.setMessage(viewHolder.salat_name_text.getText() + " এর ওয়াক্ত চলছে ");
//
//                        } else
//                            dialog2.setMessage(viewHolder.salat_name_text.getText() + " এর ওয়াক্ত অতিবাহিত হয়ে গেছে");
//
//                        dialog2.setPositiveButton("পরবর্তী দিনের জন্য ", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                Intent intent = new Intent(mContext, SetAlarmActivity.class);
//                                set_nextdayActive(dataModel.getName(), true);
//                                intent.putExtra("salat_name", viewHolder.salat_name_text.getText());
//                                intent.putExtra("time", time);
//                                intent.putExtra("tag", dataModel.getTag());
//                                intent.putExtra("name", dataModel.getName());
//                                intent.putExtra("nextDay", true);
//                                mContext.startActivity(intent);
//                            }
//                        });
//                        dialog2.setNegativeButton("বাদ দিন", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                            }
//                        });
//                        dialog2.show();
//                    } else {
//
//                        Intent intent = new Intent(mContext, SetAlarmActivity.class);
//                        intent.putExtra("time", time);
//                        intent.putExtra("salat_name", viewHolder.salat_name_text.getText());
//                        intent.putExtra("tag", dataModel.getTag());
//                        intent.putExtra("name", dataModel.getName());
//                        intent.putExtra("nextDay", false);
//                        mContext.startActivity(intent);
//                    }
//                } else {
//
////
//
//                    final AlertDialog.Builder dialog4 = new AlertDialog.Builder(v.getContext());
//                    dialog4.setTitle("");
//                    dialog4.setMessage("অ্যালার্ম টি বাদ দিন");
//                    dialog4.setPositiveButton("হ্যাঁ", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//                            cancel_azan_alarm(dataModel);
//                            set_isAlarmActive(dataModel, false);
//                            set_everydayActive(dataModel.getName(), false);
//                            set_nextdayActive(dataModel.getName(), false);
//
//
//                            Intent intent = ((Activity) mContext).getIntent();
//                            ((Activity) mContext).finish();
//                            mContext.startActivity(intent);
//
//                            ((Activity) mContext).overridePendingTransition(R.anim.fadein, R.anim.fadeout);
//
//
//                        }
//                    });
//                    dialog4.setNeutralButton("পরিবর্তন", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//
//                            sharedPreferences.edit().putBoolean("change", true).apply();
//
//
//                            Intent intent = new Intent(mContext, SetAlarmActivity.class);
//                            intent.putExtra("salat_name", viewHolder.salat_name_text.getText());
//
//                            intent.putExtra("time", time);
//                            intent.putExtra("tag", dataModel.getTag());
//                            intent.putExtra("name", dataModel.getName());
//
//                            Log.e("adapter", "extras " + dataModel.getName() + " " + dataModel.getTag());
//                            if (time < System.currentTimeMillis()) {
//
//                                intent.putExtra("nextDay", true);
//                            } else {
//                                intent.putExtra("nextDay", false);
//
//                            }
//                            mContext.startActivity(intent);
//
//                        }
//                    });
//                    dialog4.setNegativeButton("না", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//                        }
//                    });
//
//
//                    dialog4.show();
//
//
//                }
           }

            });
        //  Log.i("salat_app", "adapter");

        return roottView;
    }

    private void set_alarm_for_five_alarms(DataModel dataModel) {

          Log.e("five alarms", "set alarm");

        if(azanTimeAlreadyPassed(dataModel)){
            set_azan_alarm(dataModel.getAzan_time().getTime()+24*60*60*1000,dataModel.getTag(),dataModel.getName(),getContext());
            set_nextdayActive(dataModel.getName(),true);

        }
        else {
            set_azan_alarm(dataModel.getAzan_time().getTime(),dataModel.getTag(),dataModel.getName(),getContext());

        }
        set_isAlarmActive(dataModel,true);
        set_everydayActive(dataModel.getName(),true);
    }

    private boolean fiveAlarmsActive() {
        return  sharedPreferences.getBoolean("five_alarms",false);
    }

    private void cancelDialog(final DataModel dataModel, final ViewHolder viewHolder) {

//

                    final AlertDialog.Builder cancelDialog = new AlertDialog.Builder(getContext());
                    cancelDialog.setTitle("");
                    cancelDialog.setMessage("অ্যালার্ম টি বাদ দিন");
                    cancelDialog.setPositiveButton("হ্যাঁ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            cancel_azan_alarm(dataModel);




                        }
                    });
//                    cancelDialog.setNeutralButton("পরিবর্তন", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//
//                            sharedPreferences.edit().putBoolean("change", true).apply();
//
//
//                            goToSetAlarm(viewHolder,dataModel);
//
//                        }
//                    });
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


//    public void setNotification(DataModel dataModel) {
//        Intent intentAlarm = new Intent(mContext, AlarmReciever.class);
//        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
//        alarmManager.set(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(mContext, tag, intentAlarm, 0));
//        Toast.makeText(mContext, "", Toast.LENGTH_LONG).show();
//    }

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

//    private void openDialog(final long time, final long next_salat_time, final ViewHolder viewHolder, final DataModel dataModel) {
//        final Dialog dialog = new Dialog(mContext);
//
//
//        dialog.setContentView(R.layout.notification_dialog);
//
//
//        FloatingActionButton alarm_on = dialog.findViewById(R.id.fab_alarm_on);
//        FloatingActionButton noti_on = dialog.findViewById(R.id.fab_noti_on);
//        FloatingActionButton alarm_off = dialog.findViewById(R.id.fab_alarm_off);
//
//
//        alarm_on.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        goToSetAlarm(next_salat_time,viewHolder,dataModel);
//                        viewHolder.alarm_button.setImageResource(R.drawable.alarm_on);
//
//                        dialog.dismiss();
//                    }
//                }
//        );
//
//
//        noti_on.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        setNotification(dataModel);
//                        viewHolder.alarm_button.setImageResource(R.drawable.ic_notifications_white_36dp);
//
//                        dialog.dismiss();
//
//                    }
//                }
//        );
//
//
//        alarm_off.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        cancel_azan_alarm(dataModel);
//                        viewHolder.alarm_button.setImageResource(R.drawable.alarm_off);
//                        dialog.dismiss();
//
//                    }
//                }
//        );
//
//
//        dialog.show();
//
//
//    }


    private void goToSetAlarm( ViewHolder viewHolder, DataModel dataModel) {


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