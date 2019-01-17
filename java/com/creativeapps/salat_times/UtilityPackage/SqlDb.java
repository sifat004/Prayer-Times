package com.creativeapps.salat_times.UtilityPackage;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/**
 * Created by tanveer on 8/17/15.
 */
public class SqlDb {
    private SQLiteDatabase db;

    public class FeedReaderDbHelper extends SQLiteOpenHelper {
        // If you change the database schema, you must increment the database version.
        private static final int DATABASE_VERSION = 1;
        private static final String DATABASE_NAME = "CardSaver.db";

        public FeedReaderDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        public void onCreate(SQLiteDatabase db) {

            db.execSQL("create table ads(id integer primary key,name TEXT," +
                    "description TEXT,icon_url TEXT,forward_url TEXT,apk_size TEXT)");

            db.execSQL("create table names(id integer primary key,name TEXT," +
                    "newspaper_category_id TEXT, ic_launcher TEXT,url TEXT)");

            db.execSQL("create table categories(id integer primary key,name TEXT)");


        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL("drop table cards if exists");
            onCreate(db);
        }
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }
    public void open(Context app)
    {
        FeedReaderDbHelper helper =new FeedReaderDbHelper(app);
        db=helper.getWritableDatabase();

    }
    public void close()
    {
        db.close();
    }




    public void initAds(String jsonResponse)
    {
        String sql="null";
        try {
            // JSONObject temp = new JSONObject(jsonResponse);
            JSONArray operators = new JSONArray(jsonResponse);

            for (int i = 0; i < operators.length(); i++)
            {
                JSONObject operator = operators.getJSONObject(i);
                sql="insert into ads values("+operator.getInt("id")+",'"
                        +operator.getString("name")+"','"
                        +operator.getString("description")+ "','"+
                        operator.getString("icon_url")+"','"+
                        operator.getString("forward_url")+"','"+
                        operator.getString("apk_size")+
                        "')";
                Log.e("ads ",sql);
                db.execSQL(sql);
            }


        } catch (JSONException e) {
            Log.e("error insert sql",sql);
            e.printStackTrace();
        }
    }

    public void initNewsNames(String jsonResponse)
    {
        String sql="null";
        try {
            JSONArray operators = new JSONArray(jsonResponse);

            for (int i = 0; i < operators.length(); i++)
            {
                JSONObject operator = operators.getJSONObject(i);
                sql="insert into names values("+operator.getInt("id")+",'"
                        +operator.getString("name")+"','"
                        +operator.getString("newspaper_category_id")+ "','"+
                        operator.getString("ic_launcher")+"','"+
                        operator.getString("url")+
                        "')";
                Log.e("names ",sql);
                db.execSQL(sql);
            }


        } catch (JSONException e) {
            Log.e("error insert sql",sql);
            e.printStackTrace();
        }
    }
    public void initNewsCategory(String jsonResponse)
    {
        String sql="null";
        try {
            // JSONObject temp = new JSONObject(jsonResponse);
            JSONArray operators = new JSONArray(jsonResponse);

            for (int i = 0; i < operators.length(); i++)
            {
                JSONObject operator = operators.getJSONObject(i);
                sql="insert into categories values("+operator.getInt("id")+",'"
                        +operator.getString("name")+
                        "')";
                Log.e("cat ",sql);
                db.execSQL(sql);
            }


        } catch (JSONException e) {
            Log.e("error insert sql",sql);
            e.printStackTrace();
        }
    }
    public void deleteAll( )
    {
        String sql="null";
        try {

            db.execSQL("delete from ads");
            db.execSQL("delete from names");
            db.execSQL("delete from categories");

            Log.e("delte","delted all column");

        } catch (Throwable e) {
            Log.e("error insert sql",sql);
            e.printStackTrace();
        }
    }
    public Cursor grabAds()
    {
        Cursor cursor = db.rawQuery("select * from ads order by RANDOM()",null);
        return cursor;

    }

    public Cursor grabNames(String cat_id)
    {
        Cursor cursor = db.rawQuery("select * from names where newspaper_category_id= "+cat_id,null);
        return cursor;

    }
    public Cursor grabCategories()
    {
        Cursor cursor = db.rawQuery("select * from categories",null);
        return cursor;

    }

}
