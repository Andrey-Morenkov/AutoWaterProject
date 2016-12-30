package com.harman.autowaterproject.stuff;

import android.os.Handler;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

/**
 * Created by Hryasch on 27.12.2016.
 */

public class MQTTParams
{
    private MqttAndroidClient   mClient;
    private MqttConnectOptions  mOptions;
    private int                 mHandleTag;
    private int                 mQuitTag;
    private Handler             mParentHandler;
    private String              mLogPrefix;


    public MQTTParams (MqttAndroidClient _client, MqttConnectOptions _options, int _handletag, int _quittag, Handler _parenthandler, String _logprefix)
    {
        mClient = _client;
        mOptions = _options;
        mHandleTag = _handletag;
        mParentHandler = _parenthandler;
        mLogPrefix = _logprefix;
        mQuitTag = _quittag;
    }

    public MqttAndroidClient getClient()
    {
        return mClient;
    }
    public MqttConnectOptions getOptions()
    {
        return mOptions;
    }
    public int getHandleTag()
    {
        return mHandleTag;
    }
    public int getQuitTag()
    {
        return mQuitTag;
    }
    public Handler getParentHandler()
    {
        return mParentHandler;
    }
    public String getLogPrefix()
    {
        return mLogPrefix;
    }




    public void setClient (MqttAndroidClient _client)
    {
        mClient = _client;
    }
    public void setOptions(MqttConnectOptions _options)
    {
        mOptions = _options;
    }
    public void setHandleTag(int _handletag)
    {
        mHandleTag = _handletag;
    }
    public void setQuitTag(int _quittag)
    {
        mQuitTag = _quittag;
    }
    public void setParentHandler(Handler _parenthandler)
    {
        mParentHandler = _parenthandler;
    }
    public void setLogPrefix(String _logprefix)
    {
        mLogPrefix = _logprefix;
    }
}
