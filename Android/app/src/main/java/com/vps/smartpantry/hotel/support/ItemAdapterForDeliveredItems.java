package com.vps.smartpantry.hotel.support;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vps.smartpantry.R;

import java.util.List;

public class ItemAdapterForDeliveredItems extends RecyclerView.Adapter<ItemAdapterForDeliveredItems.MyViewHolder>
{
    private List<Item> items;

        public class MyViewHolder extends RecyclerView.ViewHolder
        {
            public TextView item_name_tv,item_quantity_tv,seller_name_tv,date_tv;

            public MyViewHolder(View view)
            {
                super(view);
                item_name_tv = (TextView) view.findViewById(R.id.item_name_tv);
                item_quantity_tv = (TextView) view.findViewById(R.id.item_quantity_tv);
                seller_name_tv = (TextView) view.findViewById(R.id.seller_name_tv);
                date_tv = (TextView) view.findViewById(R.id.date_tv);
            }
        }


    public ItemAdapterForDeliveredItems(List<Item> items)
    {
        this.items = items;
    }

    @Override
    public ItemAdapterForDeliveredItems.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.delivered_items_hotel_row, parent, false);

        return new ItemAdapterForDeliveredItems.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemAdapterForDeliveredItems.MyViewHolder holder, int position)
    {
        Item item = items.get(position);
        holder.item_name_tv.setText(item.item_name);
        holder.item_quantity_tv.setText("Quantity: "+Integer.toString(item.item_quantity)+" kg");
        holder.seller_name_tv.setText("Seller: "+item.seller_name);
        holder.date_tv.setText("Date: "+item.date);
    }

    @Override
    public int getItemCount()
    {
        return items.size();
    }
}
