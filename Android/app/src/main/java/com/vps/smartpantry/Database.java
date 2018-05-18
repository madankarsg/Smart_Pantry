package com.vps.smartpantry;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Database
{

    public static String ITEM_NAME="item_name";
    public static String SELLER_ID="seller_id";
    public static String TABLE_NAME="autobuy";
    public static String DATABASE_NAME="autobuy";
    public static String NULL="null";
    private SQLiteDatabase database;
    private Context context;
    public Database(Context context)
    {
        this.context=context;
        database = context.openOrCreateDatabase(DATABASE_NAME,context.MODE_PRIVATE,null);
        database.execSQL("CREATE TABLE IF NOT EXISTS '"+TABLE_NAME+"'(item_name VARCHAR(30),seller_id VARCHAR(10));");
    }

    public void insert(String item_name,String seller_id)
    {
        if(getSellerId(item_name).equals("null"))
            database.execSQL("INSERT INTO '"+TABLE_NAME+"' VALUES('"+item_name+"','"+seller_id+"');");
        else
            database.execSQL("UPDATE '"+TABLE_NAME+"' SET seller_id='"+seller_id+"' where item_name='"+item_name+"'");
    }

    public String getSellerId(String item_name)
    {
        Cursor resultSet = database.rawQuery("Select seller_id from '"+TABLE_NAME+"' where item_name='"+item_name+"'",null);
        resultSet.moveToFirst();
        if(resultSet.getCount()==0)
        {
           return "null";
        }
       else
           return resultSet.getString(0);

    }

    public void close()
    {
        database.close();
    }
}
