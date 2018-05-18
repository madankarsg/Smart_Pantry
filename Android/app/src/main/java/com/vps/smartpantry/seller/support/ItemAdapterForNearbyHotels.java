package com.vps.smartpantry.seller.support;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vps.smartpantry.R;

import java.util.List;

public class ItemAdapterForNearbyHotels extends RecyclerView.Adapter<ItemAdapterForNearbyHotels.MyViewHolder>
{
    private List<Item> items;

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView hotel_name_tv,address_tv,contact_tv;

        public MyViewHolder(View view)
        {
            super(view);
            hotel_name_tv = (TextView) view.findViewById(R.id.hotel_name_tv);
            address_tv = (TextView) view.findViewById(R.id.hotel_address_tv);
            contact_tv =(TextView) view.findViewById(R.id.contact_tv);
        }
    }


    public ItemAdapterForNearbyHotels(List<Item> items)
    {
        this.items = items;
    }

    @Override
    public ItemAdapterForNearbyHotels.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.nearby_hotels_item_row, parent, false);

        return new ItemAdapterForNearbyHotels.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemAdapterForNearbyHotels.MyViewHolder holder, int position)
    {
        Item item = items.get(position);
        holder.hotel_name_tv.setText(item.hotel_name);
        holder.address_tv.setText("Address: "+item.hotel_address);
        holder.contact_tv.setText("Contact: "+item.hotel_contact);
    }

    @Override
    public int getItemCount()
    {
        return items.size();
    }
}
