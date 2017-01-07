package com.harman.autowaterproject.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.harman.autowaterproject.Flower;

import java.util.ArrayList;

/**
 * Created by Hryasch on 07.01.2017.
 */

public class DBworker
{
    private static final String LOG_TAG = "****AutoWater**** ";
    private FlowerDbHelper dbHelper;
    private Cursor cursor;
    private SQLiteDatabase db;

    public DBworker(Context context)
    {
        Log.d(LOG_TAG, "Create DBWorker");
        dbHelper = new FlowerDbHelper(context);
    }
    // возвращает количество записей в таблице
    public int getItemCount()
    {
        db = dbHelper.getReadableDatabase();

        cursor = db.query(FlowerDbHelper.TABLE, null, null, null, null, null, null);
        int cnt = cursor.getCount();
        cursor.close();

        return cnt;
    }
    // метод для обновления Name
    public void updateName(String name, String newName)
    {
        db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(FlowerDbHelper.COLUMN_NAME, newName);
        String[] args = new String[]{name};
        db.update(FlowerDbHelper.COLUMN_NAME, cv, "name = ?", args);
    }

    public void addFlower(Flower _flower)
    {
        db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(FlowerDbHelper.COLUMN_NAME, _flower.getName());
        cv.put(FlowerDbHelper.COLUMN_VALVE, _flower.getValve_pin());
        cv.put(FlowerDbHelper.COLUMN_HYGROMETER, _flower.getHygrometer_pin());
        cv.put(FlowerDbHelper.COLUMN_CRITICAL_WETNESS, _flower.getCritical_wetness());
        cv.put(FlowerDbHelper.COLUMN_CRITICAL_TEMPERATURE, _flower.getCritical_temperature());
        cv.put(FlowerDbHelper.COLUMN_CRITICAL_LUMINOSITY, _flower.getCritical_luminosity());

        db.insert(FlowerDbHelper.TABLE, null, cv);
    }
    // метод для удаления строки по id
    public void deleteItem(int id)
    {
        db = dbHelper.getWritableDatabase();
        db.delete(FlowerDbHelper.TABLE, FlowerDbHelper.COLUMN_ID + "=" + id, null);
    }
    // метод возвращающий коллекцию всех данных
    public ArrayList<Flower> getFlowers()
    {
        dbHelper.getReadableDatabase();
        cursor = db.query(FlowerDbHelper.TABLE, null, null, null, null, null, null);
        ArrayList<Flower> mFlowersList = new ArrayList<>();

        if (cursor.moveToFirst())
        {
            int idColInd = cursor.getColumnIndex(FlowerDbHelper.COLUMN_ID);
            int nameColInd = cursor.getColumnIndex(FlowerDbHelper.COLUMN_NAME);
            int valveColInd = cursor.getColumnIndex(FlowerDbHelper.COLUMN_VALVE);
            int hygrometerColInd = cursor.getColumnIndex(FlowerDbHelper.COLUMN_HYGROMETER);
            int critWetnessColInd = cursor.getColumnIndex(FlowerDbHelper.COLUMN_CRITICAL_WETNESS);
            int critTempColInd = cursor.getColumnIndex(FlowerDbHelper.COLUMN_CRITICAL_TEMPERATURE);
            int critLumColInd = cursor.getColumnIndex(FlowerDbHelper.COLUMN_CRITICAL_LUMINOSITY);

            do
            {
                Flower dbflower = new Flower(cursor.getString(nameColInd),
                                            cursor.getInt(valveColInd),
                                            cursor.getInt(hygrometerColInd),
                                            cursor.getInt(critWetnessColInd),
                                            cursor.getInt(critTempColInd),
                                            cursor.getInt(critLumColInd));
                mFlowersList.add(dbflower);
            }
            while (cursor.moveToNext());

        }
        else
        {
            Log.d(LOG_TAG, "В базе нет данных!");
        }

        cursor.close();

        return mFlowersList;

    }
    // здесь закрываем все соединения с базой и класс-помощник
    public void close()
    {
        dbHelper.close();
        db.close();
    }
}
