package com.creativeapps.salat_times.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.creativeapps.salat_times.R;
import com.creativeapps.salat_times.UtilityPackage.Utility;

public class TasbihActivity extends AppCompatActivity {


    int scoreCount = 0;
  //  int totalCount = 0;

    private SharedPreferences sharedPreferences;
    public static final String mypreference = "salat";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasbih);
        sharedPreferences = getSharedPreferences(mypreference,
                MODE_PRIVATE);
        sharedPreferences = getSharedPreferences(mypreference,
                MODE_PRIVATE);

        RelativeLayout mainlayout= findViewById(R.id.main_layout);
        mainlayout.setBackground(Utility.getBackgroundGradient(this));

       scoreCount=sharedPreferences.getInt("current_score",0);
        if (scoreCount<1000) display_current(scoreCount);



//        totalCount=sharedPreferences.getInt("total_score",0);
//        display_total(totalCount);


        ImageView counter= findViewById(R.id.digital_counter);
        ImageButton reset=       findViewById(R.id.reset);


        counter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                addOne();
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                reset();
            }
        });

    }



    /**
     * Displays the given score for Counter
     */
    public void display_current(int number) {
        TextView rightDigit = findViewById(R.id.right_digit);
        TextView leftDigit = findViewById(R.id.left_digit);
        TextView middleDigit = findViewById(R.id.middle_digit);


        if (number<=0 || number>999)

        {
            rightDigit.setText(String.valueOf(0));
            middleDigit.setText(String.valueOf(0));
            leftDigit.setText(String.valueOf(0));
        }
        else if (number<10)
        {
            middleDigit.setText(String.valueOf(0));
            leftDigit.setText(String.valueOf(0));
            rightDigit.setText(String.valueOf(number));

        }
        else if (number<100 )
        {
            leftDigit.setText(String.valueOf(0));

            rightDigit.setText(String.valueOf(number%10));
            middleDigit.setText(String.valueOf(number/10));
        }

        else if (number<1000 )

        {
            rightDigit.setText(String.valueOf(number%10));
            middleDigit.setText(String.valueOf((number/10)%10));
            leftDigit.setText(String.valueOf(number/100));
        }




    }

    public void display_total(int number) {
        //TextView scoreView = findViewById(R.id.total);

       // scoreView.setText(String.valueOf(number));
    }

    /**
     * This method is called when the addOne button is clicked.
     */
    public void addOne() {
        scoreCount += 1;
        //totalCount+=1;

        sharedPreferences.edit().putInt("current_score",scoreCount).apply();
       // sharedPreferences.edit().putInt("total_score",totalCount).apply();

        if (scoreCount<1000) display_current(scoreCount);

        else {
            sharedPreferences.edit().putInt("current_score", 1).apply();
            scoreCount=sharedPreferences.getInt("current_score",0);
            display_current(scoreCount);
        }


      //  display_total(totalCount);

// Get instance of Vibrator from current Context
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

// Vibrate for 400 milliseconds
        assert v != null;
        v.vibrate(50);
    }

    /**
     * This method is called when the reset button is clicked.
     */
    public void reset() {
        scoreCount = 0;
        //totalCount = 0;

        sharedPreferences.edit().putInt("current_score",scoreCount).apply();
       // sharedPreferences.edit().putInt("total_score",totalCount).apply();

        display_current(scoreCount);
     //   display_total(totalCount);


    }
}
