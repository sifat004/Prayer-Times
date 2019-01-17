package com.creativeapps.salat_times.UtilityPackage;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;

import com.creativeapps.salat_times.BuildConfig;
import com.creativeapps.salat_times.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import static android.content.Context.MODE_PRIVATE;

;

/*
* /api
Route::any('api/newspaper/names', 'NewsController@names');
Route::any('api/newspaper/categories', 'NewsController@categories');

* **/
public class Utility {


    public static final String FACEBOOK_BANNER="1418441671536942_1419391274775315";
    //public static final String FACEBOOK_INTERSTITIAL="806838229483000_815664355267054";

    public static  String APPLICATION_ID="ca-app-pub-3262923942162561~6158494739";
    public static  String PACKAGE_NAME= BuildConfig.APPLICATION_ID;

    public static final int RESULT_CLOSE_ALL = 0;
    public static final int REQUEST_ADS=1234;
    public static final String AD_SERVER_URL="http://bdbasics.com/api/ad/";

    public static  final  String FACEBOOK_PAGE="https://www.facebook.com/Creative-Apps-BD-925997510834702";
    public static  final  String HASBUNALLAH_PAGE="https://www.facebook.com/hasbunallah.bd/";
    public static  final  String HASBUNALLAH_GROUP="https://www.facebook.com/groups/2198323040197722/";


    public static final String PACKAGENAME=PACKAGE_NAME;
    public static final String APP_LINK="https://play.google.com/store/apps/details?id="+PACKAGE_NAME;

    public static final String PUBLISHER_NAME="Creative Apps BD";
    public static final String PUBLISHER_ID="ca-app-pub-3262923942162561~6158494739";
    public static final String INTERSTITIAL="ca-app-pub-3262923942162561/6559216297";

    public static final String SHARED_PREF_NAME="myprefs6";
    public static final String RATE_MESSAGE=" আমাদের ৫ স্টার রেটিং দিবেন :)";

    public static final  String SHARE_TEXT="বই পড়ুয়া";
    public static final  String APP_DESC="যে কোন অঞ্চলের নামাজের সময়সূচি জানতে ডাউনলোড করুন";

    public static final String BASE_URL="http://bdbasics.com/api/newspaper/";
//    public static final String RATE_MESSAGE="আমাদের অ্যাপ ব্যবহারের জন্যে আপনাকে ধন্যবাদ!\nপ্লিজ আমাদের ৫স্টার রেটিং দিয়ে হেল্প করবেন :)";

    public static final int  MY_PERMISSIONS_REQUEST_CALL=0;
    public static int permission=7;


    public static void load_piasso(final Context c,final ImageView img,final String IMAGE_DOWNLOAD_LINK)
    {
        Picasso.with(c)
                .load(IMAGE_DOWNLOAD_LINK)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(img, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.e("piasso","success");
                    }

                    @Override
                    public void onError() {
                        Log.e("piasso","error");
                        // Try again online if cache failed
                        Picasso.with(c)
                                .load(IMAGE_DOWNLOAD_LINK)
                                .placeholder(R.mipmap.ic_launcher)

                                .into(img);
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

    public static void counter(Application a, ShowAdInterface mmActivity)
    {


        SharedPreferences pref = a.getSharedPreferences(Utility.SHARED_PREF_NAME , MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        int my_counter=pref.getInt("inter_count",1);
//        Log.e("count","called: "+my_counter);
//        if(my_counter%3==0) {
//            if(mmActivity.showAd()) {
//
//                Log.e("log ad should be show",my_counter+" load");
//                //my_counter = 0;
//
//            }
//            else Log.e("log ad","ad didnt load");
//        }
        my_counter++;
        editor.putInt("inter_count",my_counter);
        editor.apply();
        //Log.e("count2 static ",my_counter+"..."+mmActivity);

    }

    public static int getCount(Application a, ShowAdInterface mmActivity){

        SharedPreferences sharedPreferences= a.getSharedPreferences(Utility.SHARED_PREF_NAME , MODE_PRIVATE);
        return sharedPreferences.getInt("inter_count",0);

    }

    public static int getAdViewHeightInDP(Activity activity) {
        int adHeight = 0;

        int screenHeightInDP = getScreenHeightInDP(activity);
        if (screenHeightInDP < 400)
            adHeight = 32;
        else if (screenHeightInDP >= 400 && screenHeightInDP <= 720)
            adHeight = 50;
        else
            adHeight = 90;

        DisplayMetrics displayMetrics = ((Context) activity).getResources().getDisplayMetrics();

        int screenHeightInPX = Math.round(adHeight* displayMetrics.density);
        return screenHeightInPX;
    }

    public static int getScreenHeightInDP(Activity activity) {
        DisplayMetrics displayMetrics = ((Context) activity).getResources().getDisplayMetrics();

        float screenHeightInDP = displayMetrics.heightPixels / displayMetrics.density;

        return Math.round(screenHeightInDP);
    }
//    public static AdSize FAN_BANNER_HEIGHT(Activity activity)
//    {
//        int screenHeightInDP = getScreenHeightInDP(activity);
//        Log.e("height","height:"+screenHeightInDP);
//        if(screenHeightInDP<=720)
//            return AdSize.BANNER_HEIGHT_50;
//        else return AdSize.BANNER_HEIGHT_90;
//    }


    public static Drawable getBackgroundGradient( Context context) {
        String mypreference = "salat";

        SharedPreferences sharedPreferences = context.getSharedPreferences(mypreference,
                MODE_PRIVATE);

        int background_grad=sharedPreferences.getInt("background_grad",1);
        switch (background_grad){

            case 1: return context.getResources().getDrawable(R.drawable.gradient_1);
            case 2: return context.getResources().getDrawable(R.drawable.gradient_2);
            case 3: return context.getResources().getDrawable(R.drawable.gradient_3);
            case 4: return context.getResources().getDrawable(R.drawable.gradient_4);
            case 5: return context.getResources().getDrawable(R.drawable.gradient_5);
            case 6: return context.getResources().getDrawable(R.drawable.gradient_6);
            case 7: return context.getResources().getDrawable(R.drawable.gradient_7);
            case 8: return context.getResources().getDrawable(R.drawable.gradient_8);
            case 9: return context.getResources().getDrawable(R.drawable.gradient_9);
            case 10: return context.getResources().getDrawable(R.drawable.gradient_10);

            default: return context.getResources().getDrawable(R.drawable.gradient_1);

        }

    }



}
