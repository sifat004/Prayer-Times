package com.creativeapps.salat_times.UtilityPackage;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.creativeapps.salat_times.R;
public class AdAdapter extends ArrayAdapter {

    private Cursor opCursor;

    private Activity mActivity;
    public AdAdapter(Activity context, Cursor cursor) {
        super(context, R.layout.ads_single_item);
        mActivity =context;
        opCursor =cursor;
    }


    @Override
    public int getCount() {
        return opCursor.getCount();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        opCursor.moveToPosition(position);
        String operatorName = opCursor.getString(1);
        String bannerMessage = opCursor.getString(2);
        String Size=opCursor.getString(5);
        if(convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.ads_single_item,parent,false);

        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opCursor.moveToPosition(position);

                mActivity.startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse(opCursor.getString(4)) ) );



            }
        });
        TextView opName= (TextView) convertView.findViewById(R.id.title);
        TextView opBanner= (TextView) convertView.findViewById(R.id.description);
        TextView size= (TextView) convertView.findViewById(R.id.size);
        final ImageView imageView = (ImageView) convertView.findViewById(R.id.list_item_icon);
        Utility.load_piasso(mActivity,imageView,Utility.AD_SERVER_URL+"ads/"+opCursor.getString(0));

        size.setText(Size);
        opName.setText(operatorName);
        opBanner.setText(bannerMessage);
        return convertView;
    }

}
