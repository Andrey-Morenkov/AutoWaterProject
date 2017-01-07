package com.harman.autowaterproject.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Hryasch on 07.01.2017.
 */

public class FlowerDbHelper extends SQLiteOpenHelper
{
    private static final String LOG_TAG = "****AutoWater**** ";
    private static final String DATABASE_NAME    = "plants.db";
    private static final int    DATABASE_VERSION = 1;
    public static final String TABLE            = "flowers"; // название таблицы в бд

    // названия столбцов
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_VALVE = "valve_pin";
    public static final String COLUMN_HYGROMETER = "hygrometer_pin";
    public static final String COLUMN_CRITICAL_WETNESS = "critical_wetness";
    public static final String COLUMN_CRITICAL_TEMPERATURE = "critical_temperature";
    public static final String COLUMN_CRITICAL_LUMINOSITY = "critical_luminosity";

    public FlowerDbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        Log.d(LOG_TAG,"Create FlowerDbHelper");
        db.execSQL("CREATE TABLE " + TABLE + " (" +
                   COLUMN_ID    + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                   COLUMN_NAME  + " TEXT, " +
                   COLUMN_VALVE + " INTEGER, " +
                   COLUMN_HYGROMETER + " INTEGER, " +
                   COLUMN_CRITICAL_WETNESS + " INTEGER, " +
                   COLUMN_CRITICAL_TEMPERATURE + " INTEGER, " +
                   COLUMN_CRITICAL_LUMINOSITY  + " INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }
}
