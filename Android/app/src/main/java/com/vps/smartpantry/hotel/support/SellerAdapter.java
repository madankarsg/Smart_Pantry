package com.vps.smartpantry.hotel.support;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vps.smartpantry.R;

import java.util.List;

public class SellerAdapter extends RecyclerView.Adapter<SellerAdapter.MyViewHolder>
{
    private List<Seller> sellers;
    private Context context;
    public static int itemCount=0;
    public static int placedOrders=0;
    public static int failedOrders=0;
    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView seller_name_tv,seller_address_tv;
        public CardView row;

        public MyViewHolder(View view)
        {
            super(view);
            seller_name_tv = (TextView) view.findViewById(R.id.seller_name_place_order_item_tv);
            seller_address_tv= (TextView) view.findViewById(R.id.seller_address_place_order_item_et);
            row=(CardView)view.findViewById(R.id.seller_row_item);
        }
    }


    public SellerAdapter(List<Seller> sellers, Context context)
    {
        this.sellers = sellers;
        this.context=context;
    }

    @Override
    public SellerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sellers_details_item_row, parent, false);

        return new SellerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SellerAdapter.MyViewHolder holder, final int position)
    {
       final Seller seller = sellers.get(position);
       holder.seller_name_tv.setText(seller.name);
       holder.seller_address_tv.setText(seller.address);
    }

    @Override
    public int getItemCount()
    {
        return sellers.size();
    }


}