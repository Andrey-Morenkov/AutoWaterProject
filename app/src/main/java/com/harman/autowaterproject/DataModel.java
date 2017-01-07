package com.harman.autowaterproject;

import android.content.Context;

import com.harman.autowaterproject.database.DBworker;

import java.util.ArrayList;

// Singleton

public class DataModel
{
    private static DataModel instance = new DataModel();
    private ArrayList<Flower> mFlowerList = null;
    private DBworker          mDbWorker = null;


    private DataModel()
    {
    }

    private void refresh()
    {
        mFlowerList = mDbWorker.getFlowers();
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
        }
    }

    public ArrayList<Flower> getFlowerList ()
    {
        if (mFlowerList == null)
        {
            mFlowerList = new ArrayList<>();
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
        refresh();
    }




}
