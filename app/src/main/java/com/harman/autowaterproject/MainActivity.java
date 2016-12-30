package com.harman.autowaterproject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.harman.autowaterproject.adapter.FlowersListAdapter;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private FlowersListAdapter mAdapter;

    public static Handler mainHandler;
    public final int MQTTAction = 666;
    public MqttAndroidClient client;
    public MqttConnectOptions options;
    public String clientId;

    final String LogPrefix = "****AutoWater**** ";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.d(LogPrefix, "< onCreate main >");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Intent intent1 = getIntent();

        clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(this.getApplicationContext(), "tcp://m21.cloudmqtt.com:19348", clientId);
        options = new MqttConnectOptions();
        options.setUserName("pnuxphmq");
        options.setPassword("74G0GoSMqfDx".toCharArray());

        mainHandler = new Handler()
        {
            public void handleMessage(Message msg)
            {
                switch (msg.what)
                {
                    case MQTTAction:
                        Log.d(LogPrefix, "<Main Handler> Incoming message about MQTT ");
                        break;
                }
            }
        };

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        mRecyclerView.setAdapter(new FlowersListAdapter(createTestData(), createTestUris(), client));
    }

    @Override
    protected void onResume()
    {
        Log.d(LogPrefix, "< onResume main >");
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        Log.d(LogPrefix, "< onPause main >");
        super.onPause();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        /*
        try
        {
            client.disconnect();
        }
        catch (MqttException e)
        {
            e.printStackTrace();
        }*/
    }

    private List<FlowerItem> createTestData() {
        List<FlowerItem> myDataset = new ArrayList<>();
        myDataset.add(new FlowerItem("Бонсай"));
        myDataset.add(new FlowerItem("Фиалка"));
        myDataset.add(new FlowerItem("Каланхоэ"));
        myDataset.add(new FlowerItem("Юкка"));
        myDataset.add(new FlowerItem("Кактус"));
        return myDataset;
    }

    private List<String> createTestUris() {
        List<String> myDataset = new ArrayList<>();
        myDataset.add("http://flowertimes.ru/wp-content/uploads/2013/10/bonsai_original.jpg");
        myDataset.add("http://splants.info/uploads/posts/2015-05/1431624995_anyutiny-glazki-i-fialki.jpg");
        myDataset.add("http://www.glav-dacha.ru/wp-content/uploads/2015/10/Obrezannyy-dvukhletniy-kust-kalankhoye.jpg");
        myDataset.add("http://cvetolubam.ru/img/yukka.jpg");
        myDataset.add("http://amazingwoman.ru/wp-content/uploads/2013/06/cacti.jpg");
        return myDataset;
    }
}

