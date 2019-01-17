package com.creativeapps.salat_times.UtilityPackage;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.creativeapps.salat_times.R;
public class AdsActivity extends AppCompatActivity {
    static final int RESULT_CLOSE_ALL = 0;
    private SqlDb db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db.open(this);
        getSupportActionBar().setTitle("Install our free apps!");

        ListView listview = (ListView) findViewById(R.id.ad_list);
        Cursor cursor = db.grabAds();

        AdAdapter adapter = new AdAdapter(AdsActivity.this,cursor);
        listview.setAdapter(adapter);

        Button b= (Button)findViewById(R.id.exit);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CLOSE_ALL);
                //   System.exit(0);
                Log.e("res2",""+"exit");
                finish();
            }
        });
        b= (Button)findViewById(R.id.back);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //    setResult(1);
                onBackPressed();
            }
        });
    }
    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        setResult(1);
        super.onBackPressed();
    }
}
