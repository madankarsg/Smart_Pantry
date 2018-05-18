package com.vps.smartpantry.hotel.support;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.vps.smartpantry.R;
import com.vps.smartpantry.hotel.ItemsToOrderFragment;

import java.util.List;

/**
 * Created by Pravin on 4/8/2018.
 */

public class ItemAdapterForItemsToOrder extends RecyclerView.Adapter<ItemAdapterForItemsToOrder.MyViewHolder>
{
        private List<Item> items;
        private Context context;
        public static int itemCount=0;
        public class MyViewHolder extends RecyclerView.ViewHolder
        {
            public TextView item_name_tv,item_quantity_tv;
            public CheckBox select_cb;

            public MyViewHolder(View view)
            {
                super(view);
                item_name_tv = (TextView) view.findViewById(R.id.item_name_tv);
                item_quantity_tv= (TextView) view.findViewById(R.id.item_quantity_tv);
                select_cb=(CheckBox)view.findViewById(R.id.select_checkbox);
                view.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view) {
                        if(select_cb.isChecked())
                        {
                            select_cb.setChecked(false);
                        }
                        else
                        {
                           select_cb.setChecked(true);
                        }
                    }
                });

                select_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b)
                    {
                        if(b)
                        {
                            itemCount++;
                            ItemsToOrderFragment.items.get(getAdapterPosition()).is_to_buy=true;
                        }
                        else
                        {
                            itemCount--;
                            ItemsToOrderFragment.items.get(getAdapterPosition()).is_to_buy=false;
                        }
                    }
                });
            }
        }


        public ItemAdapterForItemsToOrder(List<Item> items, Context context)
        {
            this.items = items;
            this.context=context;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_to_order_list_item_row, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position)
        {
            Item item = items.get(position);
            holder.item_name_tv.setText(item.item_name);
            holder.item_quantity_tv.setText("Quantity to buy: "+Integer.toString(item.item_quantity)+" kg");
        }

        @Override
        public int getItemCount()
        {
            return items.size();
        }
}
