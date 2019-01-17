package com.creativeapps.salat_times.Model;

import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Sifat Ullah on 3/3/2018.
 */

public class DateCell {

    private TextView engDate;
    private TextView engMonth;

    private TextView arbDate;
    private RelativeLayout boundingBox;


    public DateCell(TextView engDate, TextView arbDate, TextView engMonth, RelativeLayout boundingBox) {
        this.engDate = engDate;
        this.arbDate = arbDate;
        this.engMonth = engMonth;

        this.boundingBox = boundingBox;
    }

    public TextView getEngDate() {
        return engDate;
    }


    public TextView getArbDate() {
        return arbDate;
    }


    public TextView getEngMonth() {
        return engMonth;
    }


    public RelativeLayout getBoundingBox() {
        return boundingBox;
    }
}
