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
    private final int tParentHandleTag;
    private String tLogPrefix;
    private Handler tParentHandler;
    private String tLoginFail;
    private String tLoginSuccess;

    public static Handler mctHandler;

    public MQTTLoginThread (final MQTTParams parentParams)
    {
        tClient = parentParams.getClient();
        tOptions = parentParams.getOptions();
        tParentHandleTag = parentParams.getParentHandleTag();
        tQuitTag = parentParams.getQuitTag();
        tLogPrefix = parentParams.getLogPrefix();
        tParentHandler = parentParams.getParentHandler();
        tLoginFail = parentParams.getFailLogin();
        tLoginSuccess = parentParams.getSuccessLogin();
    }

    public void run()
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
                    Message msg = new Message();
                    msg.obj = tLoginSuccess;
                    tParentHandler.sendMessage(msg);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception)
                {
                    Log.d(tLogPrefix, "<MQTTConnectThread> Failed connection to MQTT broker");
                    tIsConnect = false;
                    Message msg = new Message();
                    msg.obj = tLoginFail;
                    tParentHandler.sendMessage(msg);
                }
            });
        }
        catch (MqttException e)
        {
            e.printStackTrace();
        }
    }
}
