package com.harman.autowaterproject.adapter;

/**
 * Created by Hryasch on 11.12.2016.
 */

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.harman.autowaterproject.DataModel;
import com.harman.autowaterproject.Flower;
import com.harman.autowaterproject.R;

import java.util.ArrayList;

public class FlowersListAdapter extends RecyclerView.Adapter<FlowersListAdapter.FlowerViewHolder>
{
    private ArrayList<Flower> data;

    public final int SEND_MQTT_ARDUINO = 666;
    public final int SEND_MQTT_NODEMCU = 667;
    final String LogPrefix = "****AutoWater**** ";

    public FlowersListAdapter(ArrayList<Flower> data)
    {
        this.data = data;
    }

    public ArrayList<Flower> getData()
    {
        return data;
    }

    @Override
    public FlowerViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.flowercard_item, parent, false);
        return new FlowerViewHolder(view);
    }

    public void refresh()
    {
        data = DataModel.getInstance().getFlowerList();
        notifyDataSetChanged();
    }

    public void refreshAddItem()
    {
        data = DataModel.getInstance().getFlowerList();
        notifyItemInserted(data.size()-1);
    }

    @Override
    public void onBindViewHolder(final FlowerViewHolder holder, final int position)
    {
        holder.cardFlowerId = data.get(position).getId();
        holder.cardTitle.setText(data.get(position).getName());
        holder.cardTextWetnessAlert.setText(String.valueOf(data.get(position).getCritical_wetness()));

        holder.cardButtonWater.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Log.d(LogPrefix, "Pressed WaterButton at position " + position);

            }
        });
    }



    @Override
    public int getItemCount()
    {
        return data.size();
    }

    public static class FlowerViewHolder extends RecyclerView.ViewHolder
    {
        CardView    cardView;
        TextView    cardTitle;
        TextView    cardTextWetness;
        TextView    cardTextWetnessAlert;
        ImageView   cardImageMain;
        ImageView   cardImageWetness;
        ImageView   cardImageWetnessAlert;
        ImageButton cardButtonRefresh;
        ImageButton cardButtonWater;
        ImageButton cardButtonInfo;
        int         cardFlowerId;


        public FlowerViewHolder (View itemView)
        {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            cardTitle = (TextView) itemView.findViewById(R.id.cardTitle);
            cardTextWetness = (TextView) itemView.findViewById(R.id.cardTextWetness);
            cardTextWetnessAlert = (TextView) itemView.findViewById(R.id.cardTextWetnessAlert);
            cardImageMain = (ImageView) itemView.findViewById(R.id.cardImageMain);
            cardImageWetness = (ImageView) itemView.findViewById(R.id.cardImageWetness);
            cardImageWetnessAlert = (ImageView) itemView.findViewById(R.id.cardImageWetnessAlert);
            cardButtonRefresh = (ImageButton) itemView.findViewById(R.id.cardButtonRefresh);
            cardButtonWater = (ImageButton) itemView.findViewById(R.id.cardButtonWater);
            cardButtonInfo = (ImageButton) itemView.findViewById(R.id.cardButtonInfo);
        }
    }


}
