package com.harman.autowaterproject;

import android.content.Context;
import android.os.Message;
import android.util.Log;

import com.google.gson.JsonParser;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Hryasch on 09.01.2017.
 */

// Singleton

public class Controller
{
    private static Controller instance = new Controller();

    private final String LogPrefix = "< Controller > ";
    public final int REFRESH_DATA = 777;
    public final int REFRESH_ADD_ITEM = 778;
    public final int REFRESH_REMOVE_ITEM = 779;
    public final int MQTT_CONNECTION_LOST = 990;

    private String             clientId;
    private MqttAndroidClient  client;
    private MqttConnectOptions options;
    private JsonParser         jParcer;
    private ArrayList<Flower>  newFlowerList;
    private ArrayList<Integer> busyIdList;

    private Controller()
    {}

    public void init (Context context)
    {
        newFlowerList = new ArrayList<>();
        busyIdList = new ArrayList<>();
        newFlowerList.clear();

        jParcer = new JsonParser();
        clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(context, "tcp://m21.cloudmqtt.com:19348", clientId);
        options = new MqttConnectOptions();
        client.setCallback(new MqttCallback()
        {
            @Override
            public void connectionLost(Throwable cause)
            {
                Message msg = new Message();
                msg.what = MQTT_CONNECTION_LOST;
                MainActivity.mainHandler.sendMessage(msg);
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception
            {
                String incomingMessage = String.valueOf(message.getPayload());
                Log.d(LogPrefix, "messageArrived MQTT: " + incomingMessage);

                Object obj = jParcer.parse(incomingMessage);
                JSONObject jsonObj = (JSONObject) obj;
                if (jsonObj.get("Target").toString().equals("phone"))
                {
                    parseCommand(jsonObj.get("Command").toString());
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token)
            {

            }
        });
        options.setUserName("pnuxphmq");
        options.setPassword("74G0GoSMqfDx".toCharArray());

        try
        {
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener()
            {
                @Override
                public void onSuccess(IMqttToken asyncActionToken)
                {
                    Log.d(LogPrefix, "Success connected to MQTT broker");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception)
                {
                    Log.d(LogPrefix, "Failed connection to MQTT broker");
                }
            });
        }
        catch (MqttException e)
        {
            e.printStackTrace();
        }

        try
        {
            IMqttToken token = client.subscribe("toPhone",0);
            token.setActionCallback(new IMqttActionListener()
            {
                @Override
                public void onSuccess(IMqttToken asyncActionToken)
                {
                    Log.d(LogPrefix, "Success subscribed to topic");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception)
                {
                    Log.d(LogPrefix, "Failed subscribed to topic");
                }
            });
        }
        catch (MqttException e)
        {
            e.printStackTrace();
        }
    }

    public static Controller getInstance()
    {
        return instance;
    }

    public synchronized void addFlower (Flower _flower)
    {
        String command = "f"+ String.valueOf(_flower.getId()) + ":" +_flower.getName() + ":" + String.valueOf(_flower.getValve_pin()) + ":" + String.valueOf(_flower.getHygrometer_pin()) + ":" + String.valueOf(_flower.getCritical_wetness()) +";";
        sendArduinoMqtt(command);
    }

    public synchronized void checkFlower (int id)
    {
        String command = "c"+ String.valueOf(id) +";";
        sendArduinoMqtt(command);
    }

    public synchronized void waterFlower (int id)
    {
        String command = "w"+ String.valueOf(id) +";";
        sendArduinoMqtt(command);
    }

    public synchronized void deleteFlower (int id)
    {
        String command = "d"+ String.valueOf(id) +";";
        sendArduinoMqtt(command);
    }

    public synchronized void checkAll ()
    {
        String command = "c;";
        sendArduinoMqtt(command);
    }

    public synchronized void waterAll()
    {
        String command = "w;";
        sendArduinoMqtt(command);
    }

    public synchronized void deleteAll()
    {
        String command = "r;";
        sendArduinoMqtt(command);
    }

    public synchronized void refreshFlowerList ()
    {
        String command = "u;";
        sendArduinoMqtt(command);
    }

    public synchronized int generateId()
    {
        // сделать проверку до 255 но это нереально тут
        int id = 0;
        while (busyIdList.contains(id))
        {
            id ++;
        }
        return id;
    }

    private synchronized void parseCommand(String _command)
    {
        Log.d(LogPrefix, "< parseCommand > " + _command);
        switch (_command.charAt(0))
        {
            case 'f':
            {
                // добавление цветка

                if (_command.charAt(1) != ';')
                {
                    // команда на добавление цветка

                    String[] flower_maket = _command.substring(1).split("[:,;]");

                    // Id
                    // Name
                    // pinValve
                    // pinHygro
                    // critWet

                    Flower newFlower = new Flower();
                    newFlower.setId(Integer.valueOf(flower_maket[0]));
                    newFlower.setName(flower_maket[1]);
                    newFlower.setValve_pin(Integer.parseInt(flower_maket[2]));
                    newFlower.setHygrometer_pin(Integer.parseInt(flower_maket[3]));
                    newFlower.setCritical_wetness(Integer.parseInt(flower_maket[4]));
                    newFlowerList.add(newFlower);

                    busyIdList.add(newFlower.getId());
                }
                else
                {
                    // конец добавления

                    if (newFlowerList.size() == 1)
                    {
                        // добавили только один цветок

                        DataModel.getInstance().addNewFlower(newFlowerList.get(0));
                        newFlowerList.clear();
                    }
                    else
                        if (newFlowerList.size() > 1)
                        {
                            // обновили весь список
                            DataModel.getInstance().refreshFlowerList(newFlowerList);
                            newFlowerList.clear();
                        }
                }
            }
            case 'r' :
            {
                if (_command.charAt(1) != ';')
                {
                    // команда на удаление цветка

                    String[] flower_maket = _command.substring(1).split("[:,;]");

                    // Id
                    // Name
                    // pinValve
                    // pinHygro
                    // critWet

                    Flower delFlower = new Flower();
                    delFlower.setId(Integer.valueOf(flower_maket[0]));
                    delFlower.setName(flower_maket[1]);
                    delFlower.setValve_pin(Integer.parseInt(flower_maket[2]));
                    delFlower.setHygrometer_pin(Integer.parseInt(flower_maket[3]));
                    delFlower.setCritical_wetness(Integer.parseInt(flower_maket[4]));

                    DataModel.getInstance().removeFlower(delFlower);
                    busyIdList.remove(delFlower.getId());
                }
                else
                {
                    // стираем все

                    newFlowerList.clear();
                    DataModel.getInstance().refreshFlowerList(newFlowerList);
                    busyIdList.clear();
                }
            }
        }
    }

    private synchronized String commandBuild (String target, String command)
    {
        JSONObject mMsg = new JSONObject();
        String buildedCommand = command; // исправить на норм логику
        switch (target)
        {
            case "arduino" :
            {
                try
                {
                    mMsg.put("Target","arduino");
                    mMsg.put("Command",buildedCommand);
                }
                catch (JSONException j)
                {
                    Log.d(LogPrefix,j.getMessage());
                }
                break;
            }
            case "nodemcu" :
            {
                try
                {
                    mMsg.put("Target","nodemcu");
                    mMsg.put("Type","simple");
                    mMsg.put("Command",buildedCommand);
                }
                catch (JSONException j)
                {
                    Log.d(LogPrefix,j.getMessage());
                }
                break;
            }
        }
        return mMsg.toString();
    }

    private synchronized void sendArduinoMqtt(String message)
    {
        MqttMessage mqttMsg = new MqttMessage();
        mqttMsg.setPayload(commandBuild("arduino",message).getBytes());

        try
        {
            client.publish("toController", mqttMsg);
        }
        catch (MqttException e)
        {
            e.printStackTrace();
        }
    }

}