package com.vps.smartpantry.hotel.support;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vps.smartpantry.R;

import java.util.List;

public class ItemAdapterForCheckPantry extends RecyclerView.Adapter<ItemAdapterForCheckPantry.MyViewHolder>
        {
private List<Item> items;

public class MyViewHolder extends RecyclerView.ViewHolder
{
    public TextView item_name_tv,remaining_quantity_tv,refill_at_tv,capacity_tv;

    public MyViewHolder(View view)
    {
        super(view);
        item_name_tv = (TextView) view.findViewById(R.id.item_name_tv);
        capacity_tv = (TextView) view.findViewById(R.id.capacity_tv);
        remaining_quantity_tv = (TextView) view.findViewById(R.id.remaining_quantity_tv);
        refill_at_tv = (TextView) view.findViewById(R.id.refill_at_tv);
    }
}


    public ItemAdapterForCheckPantry(List<Item> items)
    {
        this.items = items;
    }

    @Override
    public ItemAdapterForCheckPantry.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.check_pantry_item_row, parent, false);

        return new ItemAdapterForCheckPantry.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemAdapterForCheckPantry.MyViewHolder holder, int position)
    {
        Item item = items.get(position);
        holder.item_name_tv.setText(item.item_name);
        holder.capacity_tv.setText("Capacity: "+Integer.toString(item.capacity)+" kg");
        holder.remaining_quantity_tv.setText("Remaing: "+Integer.toString(item.remaining_quantity)+" kg");
        holder.refill_at_tv.setText("Refill at: "+Integer.toString(item.refill_at)+" kg");
    }

    @Override
    public int getItemCount()
    {
        return items.size();
    }
}
