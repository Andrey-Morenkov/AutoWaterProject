package com.harman.autowaterproject;

import android.os.Message;

import java.util.ArrayList;

// Singleton


// ТОЛЬКО ЧТЕНИЕ. ИЗМЕНЕНИЕ ЧЕРЕЗ КОНТРОЛЛЕР


public class DataModel
{
    private static DataModel instance = new DataModel();
    private ArrayList<Flower> mFlowerList = null;

    final String LogPrefix = "< DataModel > ";
    public final int REFRESH_DATA = 777;
    public final int REFRESH_ADD_ITEM = 778;
    public final int REFRESH_REMOVE_ITEM = 779;


    private DataModel()
    {
    }

    private void refreshAddItem()
    {
        //mFlowerList = mDbWorker.getFlowers();

        Message msg = new Message();
        msg.what = REFRESH_ADD_ITEM;
        MainActivity.mainHandler.sendMessage(msg);
    }

    private void refreshRemoveItem()
    {

    }

    public static DataModel getInstance()
    {
        return instance;
    }

    public ArrayList<Flower> getFlowerList ()
    {
        if (mFlowerList == null)
        {
            mFlowerList = new ArrayList<>();
            refreshFlowerList(mFlowerList);
        }
        return mFlowerList;
    }

    public int getFlowerListSize()
    {
        return mFlowerList.size();
    }

    public void addNewFlower (Flower _flower)
    {
        mFlowerList.add(_flower);
        Message msg = new Message();
        msg.what = REFRESH_ADD_ITEM;
        MainActivity.mainHandler.sendMessage(msg);
    }

    public void removeFlower (Flower _flower)
    {

    }

    public void refreshFlowerList(ArrayList<Flower> newFLowerList)
    {
        mFlowerList = newFLowerList;
        Message msg = new Message();
        msg.what = REFRESH_DATA;
        MainActivity.mainHandler.sendMessage(msg);
    }

    public Flower getFlower (int position)
    {
        return mFlowerList.get(position);
    }




}
