package com.harman.autowaterproject.MQTT;

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
    private int                 mParentHandleTag;
    private int                 mQuitTag;
    private Handler             mParentHandler;
    private String              mLogPrefix;
    private String              mSuccessLogin;
    private String              mFailLogin;


    public MQTTParams (MqttAndroidClient _client, MqttConnectOptions _options, int _parenthandletag, int _quittag,
                       Handler _parenthandler, String _logprefix, String _successlogin, String _faillogin)
    {
        mClient = _client;
        mOptions = _options;
        mParentHandleTag = _parenthandletag;
        mParentHandler = _parenthandler;
        mLogPrefix = _logprefix;
        mQuitTag = _quittag;
        mSuccessLogin = _successlogin;
        mFailLogin = _faillogin;
    }

    public MqttAndroidClient getClient()
    {
        return mClient;
    }
    public MqttConnectOptions getOptions()
    {
        return mOptions;
    }
    public int getParentHandleTag()
    {
        return mParentHandleTag;
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

    public String getSuccessLogin()
    {
        return mSuccessLogin;
    }

    public String getFailLogin()
    {
        return mFailLogin;
    }


    public void setClient (MqttAndroidClient _client)
    {
        mClient = _client;
    }
    public void setOptions(MqttConnectOptions _options)
    {
        mOptions = _options;
    }
    public void setParentHandleTag(int _parenthandletag)
    {
        mParentHandleTag = _parenthandletag;
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
    public void setSuccessLogin(String _successlogin)
    {
        mSuccessLogin = _successlogin;
    }
    public void setFailLogin(String _faillogin)
    {
        mFailLogin = _faillogin;
    }
}
