package com.vps.smartpantry.hotel.support;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vps.smartpantry.R;

import java.util.List;

public class ItemAdapterForOrderedItems extends RecyclerView.Adapter<ItemAdapterForOrderedItems.MyViewHolder>
{
    private List<Item> items;

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView item_name_tv,quantity_tv,seller_name_tv,status_tv;

        public MyViewHolder(View view)
        {
            super(view);
            item_name_tv=(TextView)view.findViewById(R.id.item_name_tv);
            quantity_tv = (TextView) view.findViewById(R.id.quantity_tv);
            seller_name_tv = (TextView) view.findViewById(R.id.seller_name_tv);
            status_tv = (TextView) view.findViewById(R.id.status_tv);
        }
    }


    public ItemAdapterForOrderedItems(List<Item> items)
    {
        this.items = items;
    }

    @Override
    public ItemAdapterForOrderedItems.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ordered_items_hotel_item_row, parent, false);

        return new ItemAdapterForOrderedItems.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemAdapterForOrderedItems.MyViewHolder holder, int position)
    {
        Item item=items.get(position);
        holder.item_name_tv.setText(item.item_name);
        holder.quantity_tv.setText("Quantity: "+Integer.toString(item.item_quantity));
        holder.seller_name_tv.setText("Seller: "+item.seller_name);
        if (item.acceptance_status==0)
            holder.status_tv.setText("Acceptance status: Pending");
        else
            holder.status_tv.setText("Acceptance status: Accepted");
    }

    @Override
    public int getItemCount()
    {
        return items.size();
    }
}


