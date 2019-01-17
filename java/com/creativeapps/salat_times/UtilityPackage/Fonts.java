package com.creativeapps.salat_times.UtilityPackage;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by Sifat Ullah on 2/8/2018.
 */

public class Fonts {

    Context mContext;

    public Fonts(Context mContext) {
        this.mContext = mContext;
    }

    public Typeface lemonMilk(){
        Typeface face = Typeface.createFromAsset(mContext.getAssets(),
                "LemonMilkbold.otf");


        return face;
    }

    public Typeface FutureCndNormal(){
        Typeface face = Typeface.createFromAsset(mContext.getAssets(),
                "FuturaCndNormaRegular.ttf");


        return face;
    }

    public Typeface robi(){
        Typeface face = Typeface.createFromAsset(mContext.getAssets(),
                "Robi.ttf");


        return face;
    }

    public Typeface mohananda(){
        Typeface face = Typeface.createFromAsset(mContext.getAssets(),
                "Mohanonda.ttf");


        return face;
    }


    public Typeface bensen(){
        Typeface face = Typeface.createFromAsset(mContext.getAssets(),
                "bensenhandwriting.ttf");


        return face;
    }

    public Typeface mitra(){
        Typeface face = Typeface.createFromAsset(mContext.getAssets(),
                "mitra.ttf");


        return face;
    }

    public Typeface mukti(){
        Typeface face = Typeface.createFromAsset(mContext.getAssets(),
                "mukti.ttf");


        return face;
    }
}
