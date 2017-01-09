package com.harman.autowaterproject;

import android.content.Context;
import android.os.Message;
import android.util.Log;

import com.harman.autowaterproject.database.DBworker;

import java.util.ArrayList;

// Singleton

public class DataModel
{
    private int mFreeId ;
    private static DataModel instance = new DataModel();
    private ArrayList<Flower> mFlowerList = null;
    private DBworker          mDbWorker = null;

    final String LogPrefix = "****AutoWater**** ";
    public final int REFRESH_DATA = 777;
    public final int REFRESH_ADD_ITEM = 778;
    public final int REFRESH_REMOVE_ITEM = 779;


    private DataModel()
    {
    }

    private void refreshAddItem()
    {
        mFlowerList = mDbWorker.getFlowers();

        Message msg = new Message();
        msg.what = REFRESH_ADD_ITEM;
        MainActivity.mainHandler.sendMessage(msg);
    }

    private void refresh()
    {
        mFlowerList = mDbWorker.getFlowers();

        Message msg = new Message();
        msg.what = REFRESH_DATA;
        MainActivity.mainHandler.sendMessage(msg);
    }

    public static DataModel getInstance()
    {
        return instance;
    }

    public void setContext (Context _context)
    {
        if (mDbWorker == null)
        {
            mDbWorker = new DBworker(_context);
            Log.d(LogPrefix, "Database size " + String.valueOf(mDbWorker.getItemCount()));
        }
    }

    public ArrayList<Flower> getFlowerList ()
    {
        if (mFlowerList == null)
        {
            mFlowerList = new ArrayList<>();
            refresh();
        }
        return mFlowerList;
    }

    public int getFlowerListSize()
    {
        return mFlowerList.size();
    }

    public void addNewFlower (Flower _flower)
    {
        mDbWorker.addFlower(_flower);
        refreshAddItem();
        //refresh();
    }




}
