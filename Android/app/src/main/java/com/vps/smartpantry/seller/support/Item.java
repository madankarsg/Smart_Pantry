package com.vps.smartpantry.seller.support;

public class Item
{
    public String item_name;
    public int quantity;
    public String seller_name;
    public String hotel_name;
    public String hotel_address;
    public String hotel_contact;

    public Item(String item_name,int quantity,String hotel_name,String hotel_address,String hotel_contact)
    {
        this.item_name=item_name;
        this.quantity=quantity;
        this.hotel_name=hotel_name;
        this.hotel_address=hotel_address;
        this.hotel_contact=hotel_contact;
    }
    public Item(String hotel_name,String address,String contact)
    {
        this.hotel_name=hotel_name;
        this.hotel_address=address;
        this.hotel_contact=contact;
    }
}
