package com.harman.autowaterproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.harman.autowaterproject.adapter.FlowersListAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private RecyclerView mRecyclerView;

    private RecyclerView.LayoutManager mLayoutManager;
    private FlowersListAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent1 = getIntent();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        mRecyclerView.setAdapter(new FlowersListAdapter(createTestData(),createTestUris()));
    }

    private List<FlowerItem> createTestData()
    {
        List<FlowerItem> myDataset = new ArrayList<>();
        myDataset.add(new FlowerItem("Бонсай"));
        myDataset.add(new FlowerItem("Фиалка"));
        myDataset.add(new FlowerItem("Каланхоэ"));
        myDataset.add(new FlowerItem("Юкка"));
        myDataset.add(new FlowerItem("Кактус"));
        return myDataset;
    }

    private List<String> createTestUris()
    {
        List<String> myDataset = new ArrayList<>();
        myDataset.add("http://flowertimes.ru/wp-content/uploads/2013/10/bonsai_original.jpg");
        myDataset.add("http://splants.info/uploads/posts/2015-05/1431624995_anyutiny-glazki-i-fialki.jpg");
        myDataset.add("http://www.glav-dacha.ru/wp-content/uploads/2015/10/Obrezannyy-dvukhletniy-kust-kalankhoye.jpg");
        myDataset.add("http://cvetolubam.ru/img/yukka.jpg");
        myDataset.add("http://amazingwoman.ru/wp-content/uploads/2013/06/cacti.jpg");
        return myDataset;
    }
}
