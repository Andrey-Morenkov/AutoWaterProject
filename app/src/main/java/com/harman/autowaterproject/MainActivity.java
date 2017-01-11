package com.harman.autowaterproject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.harman.autowaterproject.adapter.FlowersListAdapter;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import jp.wasabeef.recyclerview.animators.LandingAnimator;

public class MainActivity extends AppCompatActivity implements Handler.Callback
{
    private RecyclerView mRecyclerView;
    private TextView     mEmptyViewText;
    private RecyclerView.LayoutManager mLayoutManager;
    private FlowersListAdapter mAdapter;
    private Toolbar mToolbar;

    private FloatingActionButton mFabAddNewFlower;
    private FloatingActionButton mFabRemoveAll;

    public static Handler mainHandler;

    public final int MQTT_SEND_ARDUINO = 666;
    public final int MQTT_SEND_NODEMCU = 667;
    public final int MQTT_ACTION = 668;
    final String MQTT_LOGIN_SUCCESS = "MQTT_LOGIN_SUCCESS";
    final String MQTT_LOGIN_FAIL    = "MQTT_LOGIN_FAIL";
    final int THREAD_QUIT        = 999;
    public final int MQTT_CONNECTION_LOST = 990;

    public final int REFRESH_UPDATE_ITEM = 776;
    public final int REFRESH_DATA = 777;
    public final int REFRESH_ADD_ITEM = 778;
    public final int REFRESH_REMOVE_ITEM = 779;
    public final int MQTT_CONNECTION_SUCCESS = 991;
    public final int ARDUINO_CONNECTION_SUCCESS = 556;

    public final int ADD_NEW_FLOWER = 888;


    public MqttAndroidClient client;
    private ImageView mCloudImage;
    private ImageView mArduinoImage;

    final String LogPrefix = "****AutoWater**** ";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.d(LogPrefix, "< onCreate main >");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.mainToolbar);
        setSupportActionBar(mToolbar);

        mCloudImage = (ImageView) findViewById(R.id.imageCloudConnect);
        mArduinoImage = (ImageView) findViewById(R.id.imageArduinoConnect);

        mFabAddNewFlower = (FloatingActionButton) findViewById(R.id.fabAddNewFlower);
        mFabAddNewFlower.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.flower_dialog, null);
                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();

                final EditText textName = (EditText) mView.findViewById(R.id.textName);
                final EditText textValvePin = (EditText) mView.findViewById(R.id.textValvePin);
                final EditText textHydroPin = (EditText) mView.findViewById(R.id.textHydroPin);
                final EditText textCritWet = (EditText) mView.findViewById(R.id.textCritWet);
                EditText textCritTemp = (EditText) mView.findViewById(R.id.textCritTemp);
                EditText textCritLum = (EditText) mView.findViewById(R.id.textCritLum);
                Button buttonAddNewFlower = (Button) mView.findViewById(R.id.buttonAddNewFlower);

                buttonAddNewFlower.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        if (!textName.getText().toString().isEmpty() && !textValvePin.getText().toString().isEmpty() &&
                                !textHydroPin.getText().toString().isEmpty() && !textCritWet.getText().toString().isEmpty())
                        {

                            Flower newFlower = new Flower(textName.getText().toString(), Integer.parseInt(textValvePin.getText().toString()),
                                    Integer.parseInt(textHydroPin.getText().toString()),Integer.parseInt(textCritWet.getText().toString()), -1, -1);
                            newFlower.setId(Controller.getInstance().generateId());
                            Controller.getInstance().addFlower(newFlower);
                            dialog.dismiss();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Не все поля заполнены",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                dialog.show();
            }
        });

        mFabRemoveAll = (FloatingActionButton) findViewById(R.id.fabRemoveAll);
        mFabRemoveAll.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Controller.getInstance().deleteAll();
            }

        });

        mainHandler = new Handler(this);

        DataModel.getInstance();
        client = new MqttAndroidClient(getApplicationContext(), "tcp://m21.cloudmqtt.com:19348", MqttClient.generateClientId(), new MemoryPersistence());
        Controller.getInstance().init(client);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(getBaseContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new LandingAnimator());
        mRecyclerView.getItemAnimator().setAddDuration(500);
        mRecyclerView.getItemAnimator().setChangeDuration(400);
        mRecyclerView.getItemAnimator().setRemoveDuration(200);

        mAdapter = new FlowersListAdapter(DataModel.getInstance().getFlowerList());

        mRecyclerView.setAdapter(mAdapter);

        mEmptyViewText = (TextView) findViewById(R.id.emptyViewText);

        if (DataModel.getInstance().getFlowerList().isEmpty())
        {
            mRecyclerView.setVisibility(View.GONE);
            mEmptyViewText.setVisibility(View.VISIBLE);
        }
        else
        {
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyViewText.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean handleMessage(Message msg)
    {
        Log.d(LogPrefix,"<Main Handler> Incoming message " + msg.what);
        switch (msg.what)
        {
            case  MQTT_CONNECTION_SUCCESS:
            {
                mCloudImage.setImageResource(R.mipmap.cloud_check);
                break;
            }

            case  ARDUINO_CONNECTION_SUCCESS:
            {
                mArduinoImage.setImageResource(R.mipmap.lan_connect);
                break;
            }

            case MQTT_CONNECTION_LOST :
            {
                mCloudImage.setImageResource(R.mipmap.cloud_sync);
                break;
            }

            case ADD_NEW_FLOWER :
            {
                break;
            }

            case REFRESH_DATA :
            {
                mAdapter.refresh();
                if (mAdapter.getData().size() == 0)
                {
                    mRecyclerView.setVisibility(View.GONE);
                    mEmptyViewText.setVisibility(View.VISIBLE);
                }
                else
                {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mEmptyViewText.setVisibility(View.GONE);
                }
                break;
            }

            case REFRESH_ADD_ITEM :
            {
                mAdapter.refreshAddItem();
                mRecyclerView.setVisibility(View.VISIBLE);
                mEmptyViewText.setVisibility(View.GONE);
                break;
            }

            case REFRESH_UPDATE_ITEM :
            {
                mAdapter.refreshUpdateItem(msg.arg1);
                break;
            }
        }
        return true;
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
        Log.d(LogPrefix, "< onStop main >");
        super.onStop();

    }
}

