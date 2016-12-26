
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
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private FlowersListAdapter mAdapter;
    private boolean IsConnect = false;

    public static Handler     mainHandler;
    public boolean            ThreadQuit = false;
    public final int          MQTTAction = 666;
    public MqttAndroidClient  client;
    public MqttConnectOptions options;
    public String clientId;

    final String LogPrefix = "****AutoWater**** ";



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.d(LogPrefix, "onCreate Main");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Intent intent1 = getIntent();

        clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(this.getApplicationContext(), "tcp://m21.cloudmqtt.com:19348",clientId);
        options = new MqttConnectOptions();
        options.setUserName("pnuxphmq");
        options.setPassword("74G0GoSMqfDx".toCharArray());

        MQTTConnectThread mqttRequestThread = new MQTTConnectThread(client,options);
        mqttRequestThread.start();

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
        mRecyclerView.setAdapter(new FlowersListAdapter(createTestData(),createTestUris(),client));
    }

    @Override
    protected void onResume()
    {



    }

    @Override
    protected void onPause()
    {

    }

    @Override
    protected void onStop()
    {
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



    //-----------------------------------------------------------------------MQTT THREAD--------------------------------------------------------------------------
    public class MQTTConnectThread extends Thread
    {
        private MqttAndroidClient tClient;
        private MqttConnectOptions tOptions;
        private boolean tIsConnect = false;
        private boolean tThreadQuit = false;
        public Handler mctHandler;

        public MQTTConnectThread (MqttAndroidClient _client, MqttConnectOptions _options)
        {
            tClient = _client;
            tOptions = _options;
            mctHandler = new Handler()
            {
                public void handleMessage(Message msg)
                {
                    switch (msg.what)
                    {
                        case MQTTAction:
                            Log.d(LogPrefix, "<MQTTConnectThread> Incoming message about MQTT ");
                            if (msg.obj.toString().equals("quit"))
                            {
                                Log.d(LogPrefix, "<MQTTConnectThread> Thread Quit ");
                                tThreadQuit = true;
                            }
                            else
                            {
                                Log.d(LogPrefix, "<MQTTConnectThread> Unknown message ");
                            }
                            break;
                        default:
                            Log.d(LogPrefix, "<MQTTConnectThread> Unknown prefix" + msg.what);
                            break;
                    }
                }
            };
        }

        public void run()
        {
            while((!tIsConnect)&&(!tThreadQuit))
            {
                try
                {
                    IMqttToken token = tClient.connect(tOptions);
                    token.setActionCallback(new IMqttActionListener()
                    {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken)
                        {
                            Log.d(LogPrefix, "<MQTTConnectThread> Success connected to MQTT broker");
                            IsConnect = true;
                            tIsConnect = true;
                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception)
                        {
                            Log.d(LogPrefix, "<MQTTConnectThread> Failed connection to MQTT broker");
                            try
                            {
                                Log.d(LogPrefix, "<MQTTConnectThread> Sleep 5 sec");
                                sleep(5000);
                            }
                            catch (InterruptedException e1)
                            {
                                Log.d(LogPrefix, "<MQTTConnectThread> Can't sleep 5 secs" + e1.getMessage());
                            }
                        }
                    });
                }
                catch (MqttException e)
                {
                    e.printStackTrace();
                }
                if ((tIsConnect == true) && (tThreadQuit == false))
                {
                    Log.d(LogPrefix, "<MQTTConnectThread> Success connection, closing thread");
                }
                else
                {
                    Log.d(LogPrefix, "<MQTTConnectThread> Closing thread forced");
                }
            }
        }

    }
}


