package com.vps.smartpantry.hotel;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vps.smartpantry.Config;
import com.vps.smartpantry.Database;
import com.vps.smartpantry.hotel.support.Item;
import com.vps.smartpantry.hotel.support.ItemAdapterForItemsToOrder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ClientService extends Service
{
    private Notification notification;
    private Thread t;
    private Database db;
    public int thread_flag=1;
    private String id;
    private List<String> notifiedOrders;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        notification = new Notification(getApplicationContext());
        db=new Database(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        super.onStartCommand(intent,flags,startId);
        Toast.makeText(getApplicationContext(), "Service started", Toast.LENGTH_LONG).show();
        id=getSharedPreferences(Config.MY_PREFS_NAME, Activity.MODE_PRIVATE).getString("mobile_number",null);
        if(t==null)
            t = new OrderThread();
        t.start();
        return START_STICKY;
    }


    class OrderThread extends Thread
    {
        RequestQueue queue;

        public  void init()
        {
            queue = Volley.newRequestQueue(getApplicationContext());

        }

        public void getOrderData()
        {
            String url = "http://" + Config.SERVER_ADDRESS + "/android/items_to_buy_notification.php";

            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onResponse(String response)
                {
                    if (!response.equals("null"))
                    {
                        try
                        {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                Item item = new Item(jsonObject.getString("item_name"), Integer.parseInt(jsonObject.getString("quantity_to_buy")));
                                if (db.getSellerId(jsonObject.getString("item_name")).equals(Database.NULL))
                                {
                                    Intent local = new Intent();
                                    local.setAction("com.smartpantry.updateui");
                                    getBaseContext().sendBroadcast(local);

                                        notification.genrateNotification("Order to place",jsonObject.getString("item_name"));
                                }
                                else
                                {
                                    placeOrder(item.item_name,item.item_quantity,db.getSellerId(item.item_name));
                                }
                            }
                        }
                        catch (Exception e)
                        {
                            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                   Toast.makeText(getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id",id);
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/x-www-form-urlencoded");
                    return params;
                }
            };
            queue.add(request);
        }


        public void placeOrder(final String item_name,final int quantity_to_buy,final String seller_id)
        {
            String url = "http://" + Config.SERVER_ADDRESS + "/android/order_item.php";

            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response)
                {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error)
                {

                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("id", Config.ID);
                    params.put("item_name",item_name);
                    params.put("qty_to_buy",Integer.toString(quantity_to_buy));
                    params.put("seller_id", seller_id);
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/x-www-form-urlencoded");
                    return params;
                }
            };
            queue.add(request);

        }

        @Override
        public void run()
        {
            init();
            while (thread_flag==1)
            {
                try
                {
                    getOrderData();
                    sleep(5000);
                }
                catch (InterruptedException e)
                {
                    Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
        public void onDestroy()
        {
        super.onDestroy();
            if(db!=null)
            {
                db.close();
            }
        if(t!=null) {
            if (t.isAlive())
            {
                thread_flag=0;
            }
        }
    }
}






