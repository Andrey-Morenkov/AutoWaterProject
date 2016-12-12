package com.harman.autowaterproject.adapter;

/**
 * Created by Hryasch on 11.12.2016.
 */

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.harman.autowaterproject.FlowerItem;
import com.harman.autowaterproject.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FlowersListAdapter extends RecyclerView.Adapter<FlowersListAdapter.FlowerViewHolder>
{
    private List<FlowerItem> data;
    private List<String> uris;

    public FlowersListAdapter(List<FlowerItem> data, List<String> uris)
    {
        this.data = data;
        this.uris = uris;
    }

    @Override
    public FlowerViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.flowercard_item,parent,false);
        return new FlowerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FlowerViewHolder holder, int position)
    {
        holder.title.setText(data.get(position).getTitle());
        Picasso.with(holder.imageView.getContext())
                .load(uris.get(position))
                .resize(100,100)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }

    public static class FlowerViewHolder extends RecyclerView.ViewHolder
    {
        CardView cardView;
        ImageView imageView;
        TextView title;

        public FlowerViewHolder (View itemView)
        {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            title = (TextView) itemView.findViewById(R.id.title);
            imageView = (ImageView) itemView.findViewById(R.id.cardImage);
        }
    }


}
