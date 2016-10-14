package com.example.reol.hdweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**Database for CityInfo
 * Created by reol on 16-10-11.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String CREATE_CITYINFO = "create table CityInfo(" +
            "_id integer primary key autoincrement," +
            "cityid text," +
            "cityname text," +
            "province text);";

    private Context mContext;

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CITYINFO);
//        Toast.makeText(mContext, "Create complete", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
