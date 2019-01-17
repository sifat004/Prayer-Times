package com.creativeapps.salat_times.UtilityPackage;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import com.google.android.gms.plus.PlusShare;
import com.creativeapps.salat_times.R;
public class ShareActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
       // takeScreenshot();
//        getActionBar().setDisplayShowTitleEnabled(true);
        Log.e("share","create");



        Button fb = (Button) findViewById(R.id.facebook);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                String show= Utility.APP_DESC+'\n'+Utility.APP_LINK;
                sendIntent.putExtra(Intent.EXTRA_TEXT, show);
                sendIntent.setType("text/plain");


                startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));

            }
        });

        Button gplus = (Button) findViewById(R.id.gplus);
        gplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new PlusShare.Builder(ShareActivity.this)
                        .setType("text/plain")
                        .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=" + Utility.PACKAGENAME))
                        .getIntent();

                startActivityForResult(shareIntent, 0);
            }
        });


    }

    public static Intent newFacebookIntent(PackageManager pm, String url) {
        Uri uri = Uri.parse(url);
        try {
            ApplicationInfo applicationInfo = pm.getApplicationInfo("com.facebook.katana", 0);
            if (applicationInfo.enabled) {
                // http://stackoverflow.com/a/24547437/1048340
                uri = Uri.parse("fb://facewebmodal/f?href=" + url);
            }
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return new Intent(Intent.ACTION_VIEW, uri);
    }

}
