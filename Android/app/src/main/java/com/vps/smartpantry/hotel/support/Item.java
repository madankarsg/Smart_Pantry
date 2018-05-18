package com.vps.smartpantry.hotel.support;

/**
 * Created by Pravin on 4/8/2018.
 */

public class Item
{
    public String item_name;
    public int item_quantity;
    public String seller_name;
    public boolean is_to_buy;
    public String date;
    public int remaining_quantity;
    public int refill_at;
    public int capacity;
    public int acceptance_status;
    public Item(String name,int qty)   //for items to order
    {
        item_name=name;
        item_quantity=qty;
        is_to_buy=false;
    }

    public Item(String name,int qty,String seller_name,String date)   //for Delivered items
    {
        item_name=name;
        item_quantity=qty;
        this.seller_name=seller_name;
        this.date=date;
    }

    public Item(String item_name,int remaining_quantity,int refill_at,int capacity)  //for Pantry Check
    {
        this.item_name=item_name;
        this.remaining_quantity=remaining_quantity;
        this.refill_at=refill_at;
        this.capacity=capacity;
    }

    public Item(String item_name,int quantity,String seller_name,int status)
    {
        this.item_name=item_name;
        this.item_quantity=quantity;
        this.seller_name=seller_name;
        this.acceptance_status=status;
    }
}
