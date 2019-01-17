package com.creativeapps.salat_times.Activities;

import android.content.SharedPreferences;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.creativeapps.salat_times.R;
import com.creativeapps.salat_times.UtilityPackage.Utility;

public class QiblaActivity extends AppCompatActivity implements SensorEventListener{




    private ImageView compImg,qiblaImg ;
    private SensorManager sensor;
    double latitude,longitude;
    double meccaLatitude = 21.422483;
    double meccaLongitude = 39.826181;
    float qiblaAngle;

    public static final String mypreference = "salat";


    private float currentDegree = 0f;
    private float currentDegreeNeedle = 0f;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_qibla);

            SharedPreferences sharedPreferences = getSharedPreferences(mypreference,
                    MODE_PRIVATE);

            RelativeLayout mainlayout= findViewById(R.id.main_layout);
            mainlayout.setBackground(Utility.getBackgroundGradient(this));
            compImg =  findViewById(R.id.compassImg);
            qiblaImg = findViewById(R.id.qiblaImg);
            sensor = (SensorManager) getSystemService(SENSOR_SERVICE);

            latitude = Double.parseDouble(sharedPreferences.getString("LOCATION_LAT", "23.6850"));
            longitude = Double.parseDouble(sharedPreferences.getString("LOCATION_LON", "90.3563"));
            qiblaAngle = getQiblaAngle(latitude, longitude, meccaLatitude, meccaLongitude);


        }



    @Override
    public void onPause(){

        super.onPause();
        sensor.unregisterListener( this);
    }
    @Override
    public void onResume(){
        super.onResume();
        sensor.registerListener( this, sensor.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);

    }

    //21.422483, 39.826181
    @Override
    public void onSensorChanged(SensorEvent event){
        do_the_rotstion(event);


    }
    private void do_the_rotstion(SensorEvent sensorEvent) {
        float degree = Math.round(sensorEvent.values[0]);
        float head = Math.round(sensorEvent.values[0]);
        Location destinationLoc = new Location("service Provider");
        Location userLoc = new Location("service Provider");

        destinationLoc.setLatitude(21.422487); //kaaba latitude setting
        destinationLoc.setLongitude(39.826206); //kaaba longitude setting

        userLoc.setLatitude(latitude); //kaaba latitude setting
        userLoc.setLongitude(longitude); //kaaba longitude setting
        float bearTo=userLoc.bearingTo(destinationLoc);

        //bearTo = The angle from true north to the destination location from the point we're your currently standing.(asal image k N se destination taak angle )

        //head = The angle that you've rotated your phone from true north. (jaise image lagi hai wo true north per hai ab phone jitne rotate yani jitna image ka n change hai us ka angle hai ye)



        GeomagneticField geoField = new GeomagneticField( Double.valueOf( userLoc.getLatitude() ).floatValue(), Double
                .valueOf( userLoc.getLongitude() ).floatValue(),
                Double.valueOf( userLoc.getAltitude() ).floatValue(),
                System.currentTimeMillis() );
        head -= geoField.getDeclination(); // converts magnetic north into true north

        if (bearTo < 0) {
            bearTo = bearTo + 360;
            //bearTo = -100 + 360  = 260;
        }

//This is where we choose to point it
        float direction = bearTo - head;

// If the direction is smaller than 0, add 360 to get the rotation clockwise.
        if (direction < 0) {
            direction = direction + 360;
        }
        //     tvHeading.setText("Heading: " + Float.toString(degree) + " degrees" );



// create a rotation animation (reverse turn degree degrees)
        RotateAnimation ra = new RotateAnimation(currentDegree, -degree, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

// how long the animation will take place
        ra.setDuration(210);


// set the animation after the end of the reservation status
        ra.setFillAfter(true);

// Start the animation
        compImg.startAnimation(ra);

        currentDegree = -degree;
        // Log.e("bear", String.valueOf(bearTo));


        RotateAnimation raQibla = new RotateAnimation(currentDegreeNeedle, direction, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        raQibla.setDuration(210);
        raQibla.setFillAfter(true);

        qiblaImg.startAnimation(raQibla);

        currentDegreeNeedle = direction;

    }
    public float getQiblaAngle(double lat1,double long1,double lat2,double long2){
        double angle,dy,dx;
        dy = lat2 - lat1;
        dx = Math.cos(Math.PI/ 180 * lat1) * (long2 - long1);
        angle = Math.atan2(dy, dx);
        angle = Math.toDegrees(angle);

        Log.e("qiblaangle", String.valueOf(angle));

        return (float)angle;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not in use

    }



    }

