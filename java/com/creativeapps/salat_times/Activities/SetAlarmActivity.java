package com.creativeapps.salat_times.Activities;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.creativeapps.salat_times.RecieversAndService.AlarmReciever;
import com.creativeapps.salat_times.R;
import com.creativeapps.salat_times.UtilityPackage.Fonts;
import com.creativeapps.salat_times.UtilityPackage.Utility;

import com.google.android.gms.ads.AdRequest;

public class SetAlarmActivity extends AppCompatActivity {
    public Button button_normal_alarm, button_azan_alarm, button_before, button_after, button_silent_alarm;
    Button button_save;
    public Switch everyday;
    public EditText set_time_before_or_after;
    public TextView salat_status,set_tone_text,set_alarm_text,minute;
    boolean before;
    boolean after;
    long time;
    public SharedPreferences sharedPreferences;
    public int tag;
    public String name;
    public static final String mypreference = "salat";
    public boolean nextDay;
    int tone = 5;
    int count;


    @Override
    public void onBackPressed() {

//        Intent intent = new Intent(SetAlarmActivity.this, MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);
        super.onBackPressed();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);


        setContentView(R.layout.set_alarm);
        sharedPreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);


        ADS();



        RelativeLayout mainlayout= findViewById(R.id.main_layout);
        mainlayout.setBackground(Utility.getBackgroundGradient(this));
        count=sharedPreferences.getInt("count",0);


        button_normal_alarm = findViewById(R.id.normal_alarm);
        button_azan_alarm =  findViewById(R.id.azan_alarm);
        button_silent_alarm= findViewById(R.id.silent_alarm);
        button_before = findViewById(R.id.before);
        button_after =  findViewById(R.id.after);
        button_save = findViewById(R.id.save);

        set_time_before_or_after =  findViewById(R.id.editText);

        set_time_before_or_after.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                set_time_before_or_after.setText("");
                set_time_before_or_after.setCursorVisible(true);


            }});
        everyday =  findViewById(R.id.everyday);
        time = getIntent().getLongExtra("time", -1);
        tag = getIntent().getIntExtra("tag", -1);
        name = getIntent().getStringExtra("name");
        nextDay = getIntent().getBooleanExtra("nextDay", false);
        before = true;
        after = false;
//        button_normal_alarm.setBackground(Utility.getBackgroundGradient(this));
//        button_azan_alarm.setBackground(Utility.getBackgroundGradient(this));
        button_azan_alarm.setAlpha((float) 1);
        button_normal_alarm.setAlpha((float) 0.3);
        button_silent_alarm.setAlpha((float) 0.3);
        button_azan_alarm.setPressed(true);
        button_normal_alarm.setPressed(false);
        button_normal_alarm.setBackground(getResources().getDrawable(R.drawable.button));
        button_azan_alarm.setBackground(getResources().getDrawable(R.drawable.button));
        button_silent_alarm.setBackground(getResources().getDrawable(R.drawable.button));

        button_save.setBackground(getResources().getDrawable(R.drawable.button));

        button_before.setBackground(getResources().getDrawable(R.drawable.button));
        button_after.setBackground(getResources().getDrawable(R.drawable.button));
        button_after.setAlpha((float) 0.3);
        button_before.setAlpha((float) 1);
        button_before.setPressed(true);
        button_after.setPressed(false);

        salat_status= findViewById(R.id.salat_status);
        salat_status.setText(getIntent().getStringExtra("salat_passed_string"));
        set_tone_text=findViewById(R.id.textView);
        set_alarm_text=findViewById(R.id.textView4);
        minute=findViewById(R.id.textView5);



        Fonts fonts=new Fonts(this);
        button_azan_alarm.setTypeface(fonts.bensen());
        button_before.setTypeface(fonts.bensen());
        button_after.setTypeface(fonts.bensen());
        button_normal_alarm.setTypeface(fonts.bensen());
        set_time_before_or_after.setTypeface(fonts.bensen());
        everyday.setTypeface(fonts.bensen());
        salat_status.setTypeface(fonts.bensen());
        set_tone_text.setTypeface(fonts.bensen());
        set_alarm_text.setTypeface(fonts.bensen());
        minute.setTypeface(fonts.bensen());



        // Log.e("setAlarmActivity","tag  "+tag+"name "+ name );

        if(get_everydayActive(name)){

            everyday.setChecked(true);

        }

        if(!sharedPreferences.getBoolean("change",false)){


          //  set_everydayActive(name,false);
            set_isAlarmActive(name,false);
            set_nextdayActive(name,false);
        }




        set_tone(name,5);
//        tone=get_tone(name);
//        button_normal_alarm.setOnClickListener(new View.OnClickListener() {
//                                                   @Override
//                                                   public void onClick(final View v) {
//                                                       final AlertDialog.Builder builderSingle = new AlertDialog.Builder(SetAlarmActivity.this);
//                                                       builderSingle.setTitle("টোন বাছাই করুন");
//                                                       final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SetAlarmActivity.this, android.R.layout.select_dialog_singlechoice);
//                                                       arrayAdapter.add("সাইরেন");
//                                                       arrayAdapter.add("টোন ১");
//                                                       arrayAdapter.add("টোন ২");
//                                                       arrayAdapter.add("টোন ৩");
//                                                       builderSingle.setNegativeButton("বাদ দিন", new DialogInterface.OnClickListener() {
//                                                           @Override
//                                                           public void onClick(DialogInterface dialog, int which) {
//                                                               dialog.dismiss();
//                                                           }
//                                                       });
//                                                       builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
//                                                           @Override
//                                                           public void onClick(DialogInterface dialog, int which) {
//                                                               if (which == 0) {
//                                                                   try {
//                                                                       mediaPlayer[0].stop();
//                                                                   } catch (Exception e) {
//                                                                   }
//                                                                   mediaPlayer[0] = MediaPlayer.create(SetAlarmActivity.this, R.raw.siren);
//                                                                   mediaPlayer[0].start();
//                                                                   tone = 0;
//                                                               } else if (which == 3) {
//                                                                   try {
//                                                                       mediaPlayer[0].stop();
//                                                                   } catch (Exception e) {
//                                                                   }
//                                                                   mediaPlayer[0] = MediaPlayer.create(SetAlarmActivity.this, R.raw.tone3);
//                                                                   mediaPlayer[0].start();
//                                                                   tone = 3;
//                                                               } else if (which == 2) {
//                                                                   try {
//                                                                       mediaPlayer[0].stop();
//                                                                   } catch (Exception e) {
//                                                                   }
//                                                                   mediaPlayer[0] = MediaPlayer.create(SetAlarmActivity.this, R.raw.tone2);
//                                                                   mediaPlayer[0].start();
//                                                                   tone = 2;
//                                                               } else if (which == 1) {
//                                                                   try {
//                                                                       mediaPlayer[0].stop();
//                                                                   } catch (Exception e) {
//                                                                   }
//                                                                   mediaPlayer[0] = MediaPlayer.create(SetAlarmActivity.this, R.raw.tone1);
//                                                                   mediaPlayer[0].start();
//                                                                   tone = 1;
//                                                               }
//
//                                                               else tone = 5;
//
//                                                               AlertDialog.Builder builderInner = new AlertDialog.Builder(SetAlarmActivity.this);
//                                                               builderInner.setMessage("এই টোন টি সিলেক্ট করুন");
//                                                               builderInner.setPositiveButton("ঠিক আছে", new DialogInterface.OnClickListener() {
//                                                                   @Override
//                                                                   public void onClick(DialogInterface dialog, int which) {
//                                                                       mediaPlayer[0].stop();
//                                                                       set_tone(name, tone);
//                                                                       dialog.dismiss();
//                                                                   }
//                                                               });
//                                                               builderInner.setNegativeButton("বাদ দিন", new DialogInterface.OnClickListener() {
//                                                                   @Override
//                                                                   public void onClick(DialogInterface dialog, int which) {
//                                                                       mediaPlayer[0].stop();
//                                                                       dialog.dismiss();
//                                                                       builderSingle.show();
//                                                                   }
//                                                               });
//                                                               builderInner.show();
//                                                           }
//                                                       });
//                                                       builderSingle.show();
//                                                       button_normal_alarm.setBackgroundColor(getResources().getColor(R.color.holo_blue_light));
//                                                       button_azan_alarm.setBackgroundColor(getResources().getColor(R.color.hairline));
//                                                       button_normal_alarm.setPressed(true);
//                                                   }
//                                               }
//        );
        button_azan_alarm.setOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(final View v) {
                                                     set_tone(name, 5);
                                                     tone=5;

                                                     button_azan_alarm.setPressed(true);
                                                     button_normal_alarm.setPressed(false);


                                                     button_azan_alarm.setAlpha((float) 1);
                                                     button_normal_alarm.setAlpha((float) 0.3);
                                                     button_silent_alarm.setAlpha((float) 0.3);

                                                 }
                                             }
        );

        button_normal_alarm.setOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(final View v) {
                                                     set_tone(name, 10);
                                                     tone=10;

                                                     button_normal_alarm.setPressed(true);
                                                     button_azan_alarm.setPressed(false);


                                                     button_azan_alarm.setAlpha((float) 0.3);
                                                     button_normal_alarm.setAlpha((float) 1);
                                                     button_silent_alarm.setAlpha((float) 0.3);

                                                 }
                                             }
        );

        button_silent_alarm.setOnClickListener(new View.OnClickListener() {
                                                   @Override
                                                   public void onClick(final View v) {
                                                       set_tone(name, 0);
                                                       tone=0;

                                                       button_normal_alarm.setPressed(true);
                                                       button_azan_alarm.setPressed(false);


                                                       button_azan_alarm.setAlpha((float) 0.3);
                                                       button_normal_alarm.setAlpha((float) 0.3);
                                                       button_silent_alarm.setAlpha((float) 1);

                                                   }
                                               }
        );

        button_before.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(final View v) {
                                                 before = true;
                                                 after = false;

                                                 button_after.setAlpha((float) 0.3);
                                                 button_before.setAlpha((float) 1);

                                                 button_before.setPressed(true);
                                                 button_after.setPressed(false);
                                             }
                                         }
        );
        button_after.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(final View v) {
                                                after = true;
                                                before = false;

                                                button_after.setPressed(true);
                                                button_before.setPressed(false);

                                                button_before.setAlpha((float) 0.3);
                                                button_after.setAlpha((float) 1);


                                            }
                                        }
        );
        everyday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    set_everydayActive(name, true);
                    set_isAlarmActive(name,true);
                    if (before && set_time_before_or_after.getText()!=null){

                        try{
                            sharedPreferences.edit().putLong(name+"time_adjust",- ((Long.parseLong(set_time_before_or_after.getText().toString().trim())) * 60 * 1000)).apply();

                        }

                        catch (NumberFormatException n){
                            sharedPreferences.edit().putLong(name+"time_adjust",0).apply();
                            set_time_before_or_after.setText("0");

                        }

                    }

                    else if (after && set_time_before_or_after.getText()!=null){

                        try{
                            sharedPreferences.edit().putLong(name+"time_adjust",((Long.parseLong(set_time_before_or_after.getText().toString().trim())) * 60 * 1000)).apply();

                        }
                        catch (NumberFormatException n){
                            sharedPreferences.edit().putLong(name+"time_adjust",0).apply();
                            set_time_before_or_after.setText("0");


                        }

                    }
                } else {
                    set_everydayActive(name, false);
                }
            }
        });
        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                    button_save.setPressed(true);
                if(sharedPreferences.getBoolean("change",false)){


                    Log.e("setAlarm","change detected");
                    cancel_azan_alarm(name,tag);
                    set_isAlarmActive(name, false);
                    //set_everydayActive(name, false);


                    sharedPreferences.edit().putBoolean("change",false).apply();
                }

                if (before) {

                    try {
                        time = time - ((Long.parseLong(set_time_before_or_after.getText().toString().trim())) * 60 * 1000);

                    }
                    catch (NumberFormatException nf){
                        nf.printStackTrace();
                    }
                }
                else if (after) {
                    try {
                        time =time+ ((Long.parseLong(set_time_before_or_after.getText().toString().trim())) * 60 * 1000);


                    }
                    catch (NumberFormatException nf){
                        nf.printStackTrace();

                    }
                }
                if (nextDay) {
                    set_azan_alarm(time + (24 * 60 * 60 * 1000), tag, name);
                    set_nextdayActive(name, true);
                } else {
                    set_azan_alarm(time, tag, name);
                    set_nextdayActive(name, false);
                }
                set_isAlarmActive(name, true);
                Intent intent = new Intent(SetAlarmActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    public void set_azan_alarm(long time, int tag, String name) {
        Intent intentAlarm = new Intent(this, AlarmReciever.class);
        intentAlarm.putExtra("tag", tag);
        intentAlarm.putExtra("tone", get_tone(name));
        intentAlarm.putExtra("name", name);
        intentAlarm.putExtra("time", time);


        Log.e("setAlarm","tone_set="+get_tone(name)+" tag="+tag+" name="+name);


        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(this, tag, intentAlarm, 0));
            }

            else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(this, tag, intentAlarm, 0));
                }

                else {

                alarmManager.set(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(this, tag, intentAlarm, 0));

            }


        }
        Toast.makeText(this, "অ্যালার্ম সেট করা হয়েছে", Toast.LENGTH_SHORT).show();


    }

    public void set_isAlarmActive(String name, boolean isAlarmActive) {
        sharedPreferences.edit().putBoolean(name + "active", isAlarmActive).apply();
    }



    public void set_everydayActive(String name, boolean everydayActive) {
        sharedPreferences.edit().putBoolean(name + "everyday", everydayActive).apply();
    }

    public boolean get_everydayActive(String name) {
        return sharedPreferences.getBoolean(name + "everyday", false);
    }

    public void set_nextdayActive(String name, boolean everydayActive) {
        sharedPreferences.edit().putBoolean(name + "next_day", everydayActive).apply();
    }


    public void set_tone(String name, int tone) {
        sharedPreferences.edit().putInt(name + "tone", tone).apply();
        Log.e("set_alarm_activity","tone_set="+tone+" get_tone="+get_tone(name));
    }

    public int get_tone(String name) {
        Log.e("set_alarm_activity"," get_tone="+sharedPreferences.getInt(name + "tone", 5));

        return sharedPreferences.getInt(name + "tone", 0);

    }


    public void cancel_azan_alarm(String name,int tag) {
        Intent intentAlarm = new Intent(this, AlarmReciever.class);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, tag, intentAlarm, 0);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
        set_everydayActive(name, false);
        set_isAlarmActive(name, false);
        set_nextdayActive(name,false);

        sharedPreferences.edit().putLong(name+"time_adjust",0).apply();


        //Toast.makeText(this, "অ্যালার্ম বন্ধ করা হয়েছে", Toast.LENGTH_LONG).show();
    }



    void ADS()
    {



//todo fb banner commented


        com.google.android.gms.ads.AdView mAdView = findViewById(R.id.adView2);

        AdRequest adRequest = new AdRequest.Builder().build();


        mAdView.loadAd(adRequest);
        Log.e("set_alarm_activity"," add");


    }
}
