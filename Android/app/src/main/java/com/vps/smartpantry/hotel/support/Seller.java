package com.vps.smartpantry.hotel.support;

public class Seller
{
    public String name;
    public String address;
    public String id;
    public String contact;

    public Seller(String name,String address,String id,String contact)
    {
        this.name=name;
        this.address=address;
        this.id=id;
    }

    public Seller(String name,String address,String contact)
    {
        this.name=name;
        this.address=address;
        this.contact=contact;
    }
}
