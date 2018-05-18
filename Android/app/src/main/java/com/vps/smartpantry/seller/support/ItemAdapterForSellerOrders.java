package com.vps.smartpantry.seller.support;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vps.smartpantry.R;
import com.vps.smartpantry.seller.support.*;

import java.util.List;

public class ItemAdapterForSellerOrders extends RecyclerView.Adapter<ItemAdapterForSellerOrders.MyViewHolder>
{
    private List<Item> items;

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView item_name_tv,item_quantity_tv,hotel_name_tv,hotel_address_tv,contact_tv;

        public MyViewHolder(View view)
        {
            super(view);
            item_name_tv = (TextView) view.findViewById(R.id.item_name_tv);
            item_quantity_tv = (TextView) view.findViewById(R.id.quantity_tv);
            hotel_name_tv = (TextView) view.findViewById(R.id.hotel_name_tv);
            hotel_address_tv = (TextView) view.findViewById(R.id.hotel_address_tv);
            contact_tv =(TextView) view.findViewById(R.id.contact_tv);
        }
    }


    public ItemAdapterForSellerOrders(List<Item> items)
    {
        this.items = items;
    }

    @Override
    public ItemAdapterForSellerOrders.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.seller_orders_item_row, parent, false);

        return new ItemAdapterForSellerOrders.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemAdapterForSellerOrders.MyViewHolder holder, int position)
    {
        Item item = items.get(position);
        holder.item_name_tv.setText(item.item_name);
        holder.item_quantity_tv.setText("Quantity: "+Integer.toString(item.quantity)+" kg");
        holder.hotel_name_tv.setText("Hotel: "+item.hotel_name);
        holder.hotel_address_tv.setText("Address: "+item.hotel_address);
        holder.contact_tv.setText("Contact: "+item.hotel_contact);
    }

    @Override
    public int getItemCount()
    {
        return items.size();
    }
}
