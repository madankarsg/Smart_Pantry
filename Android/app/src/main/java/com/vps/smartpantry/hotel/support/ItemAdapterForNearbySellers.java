package com.vps.smartpantry.hotel.support;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vps.smartpantry.R;

import java.util.List;

public class ItemAdapterForNearbySellers extends RecyclerView.Adapter<ItemAdapterForNearbySellers.MyViewHolder>
{
    private List<Seller> sellers;

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView seller_name_tv,seller_address,seller_contact_tv;

        public MyViewHolder(View view)
        {
            super(view);
            seller_name_tv = (TextView) view.findViewById(R.id.seller_name_tv);
            seller_address = (TextView) view.findViewById(R.id.seller_address_tv);
            seller_contact_tv = (TextView) view.findViewById(R.id.seller_contact_tv);
        }
    }


    public ItemAdapterForNearbySellers(List<Seller> sellers)
    {
        this.sellers = sellers;
    }

    @Override
    public ItemAdapterForNearbySellers.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.nearby_seller_row, parent, false);

        return new ItemAdapterForNearbySellers.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemAdapterForNearbySellers.MyViewHolder holder, int position)
    {
        Seller seller = sellers.get(position);
        holder.seller_name_tv.setText(seller.name);
        holder.seller_address.setText("Address: "+seller.address);
        holder.seller_contact_tv.setText("Contact: "+seller.contact);
    }

    @Override
    public int getItemCount()
    {
        return sellers.size();
    }
}

