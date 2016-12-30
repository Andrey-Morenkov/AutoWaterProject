package com.harman.autowaterproject.thread;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.harman.autowaterproject.stuff.MQTTParams;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 * Created by Hryasch on 27.12.2016.
 */
public class MQTTLoginThread extends Thread
{
    private MqttAndroidClient tClient;
    private MqttConnectOptions tOptions;
    private boolean tIsConnect = false;
    private boolean tThreadQuit = false;
    private final int tQuitTag;
    private final int tHandleTag;
    private String tLogPrefix;
    private Handler tParentHandler;

    public static Handler mctHandler;

    public MQTTLoginThread (final MQTTParams parentParams)
    {
        tClient = parentParams.getClient();
        tOptions = parentParams.getOptions();
        tHandleTag = parentParams.getHandleTag();
        tQuitTag = parentParams.getQuitTag();
        tLogPrefix = parentParams.getLogPrefix();
        tParentHandler = parentParams.getParentHandler();

        mctHandler = new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                if (msg.what == tQuitTag)
                {
                    Log.d(tLogPrefix, "<MQTTConnectThread> Incoming message about MQTT");
                    if (msg.obj.toString().equals("quit"))
                    {
                        Log.d(tLogPrefix, "<MQTTConnectThread> Thread Quit ");
                        tThreadQuit = true;
                    }
                    else
                    {
                        Log.d(tLogPrefix, "<MQTTConnectThread> Unknown command ");
                    }
                }
                else
                {
                    Log.d(tLogPrefix, "<MQTTConnectThread> Unknown prefix" + msg.what);
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
                        Log.d(tLogPrefix, "<MQTTConnectThread> Success connected to MQTT broker");
                        tIsConnect = true;
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception)
                    {
                        Log.d(tLogPrefix, "<MQTTConnectThread> Failed connection to MQTT broker");
                        try
                        {
                            Log.d(tLogPrefix, "<MQTTConnectThread> Sleep 3 sec");
                            sleep(3000);
                        }
                        catch (InterruptedException e1)
                        {
                            Log.d(tLogPrefix, "<MQTTConnectThread> Can't sleep 3 secs" + e1.getMessage());
                        }
                    }
                });
            }
            catch (MqttException e)
            {
                e.printStackTrace();
            }
        }
        if ((tIsConnect) && (!tThreadQuit))
        {
            // self-connected, not force stopping
            Log.d(tLogPrefix, "<MQTTConnectThread> Success connection, closing thread");
            tParentHandler.obtainMessage(tHandleTag,"connected");
        }
        if ((!tIsConnect) && (tThreadQuit))
        {
            // force stop
            Log.d(tLogPrefix, "<MQTTConnectThread> Closing thread forced");
            tParentHandler.obtainMessage(tHandleTag,"notconnected");
        }
    }
}
