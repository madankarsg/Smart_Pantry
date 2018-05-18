package com.vps.smartpantry.hotel;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vps.smartpantry.Config;
import com.vps.smartpantry.R;
import com.vps.smartpantry.hotel.support.RecyclerItemClickListener;
import com.vps.smartpantry.hotel.support.Seller;
import com.vps.smartpantry.hotel.support.SellerAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaceOrder extends AppCompatActivity
{
    private RecyclerView sellers_rv;
    private RequestQueue volleyRequestQueue;
    private SharedPreferences prefs;
    private String id;
    private SellerAdapter sellerAdapter;
    public static List<Seller> sellers;
    private int orderPlaced;
    private int orderResponded;
    private PlaceOrder placeOrder;
    @Override
    public void onCreate(Bundle b)
    {
        super.onCreate(b);
        setContentView(R.layout.place_order);
        placeOrder=this;
        sellers_rv=(RecyclerView)findViewById(R.id.sellers_list_place_order_rv);
        volleyRequestQueue = Volley.newRequestQueue(getApplicationContext());
        prefs= getSharedPreferences(Config.MY_PREFS_NAME, Activity.MODE_PRIVATE);
        id=prefs.getString("mobile_number",null);
        sellers=new ArrayList<Seller>();
        getSeller();
        SellerAdapter.placedOrders=0;
        SellerAdapter.failedOrders=0;
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        sellers_rv.setLayoutManager(mLayoutManager);
        sellers_rv.setItemAnimator(new DefaultItemAnimator());
        sellers_rv.addOnItemTouchListener( new RecyclerItemClickListener(getBaseContext(), sellers_rv ,new RecyclerItemClickListener.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(View view, final int position)
                    {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(placeOrder);
                        alertDialogBuilder.setTitle("Place Order");
                        // set dialog message
                        alertDialogBuilder
                                .setMessage("Are you sure")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id)
                                    {
                                        orderPlaced=0;
                                        orderResponded=0;
                                        final ProgressDialog mDialog= new ProgressDialog(placeOrder);
                                        mDialog.setCancelable(false);
                                        mDialog.setMessage("Placing order...");
                                        mDialog.show();
                                        for (int i = 0; i< ItemsToOrderFragment.items.size(); i++)
                                        {
                                            if(ItemsToOrderFragment.items.get(i).is_to_buy)
                                            {
                                                orderPlaced++;
                                                placeOrder(i, position, placeOrder);
                                            }
                                        }
                                        new Thread()
                                        {
                                            public void run()
                                            {
                                                while (orderPlaced!=orderResponded)
                                                {
                                                    try
                                                    {
                                                        Thread.sleep(1000);
                                                    }
                                                    catch (InterruptedException e)
                                                    {
                                                        e.printStackTrace();
                                                    }
                                                }
                                                new Handler(Looper.getMainLooper()).post(new Runnable() { // Tried new Handler(Looper.myLopper()) also
                                                    @Override
                                                    public void run()
                                                    {
                                                        mDialog.dismiss();
                                                        orderPlacedMessage();
                                                    }
                                                });
                                                orderResponded=0;
                                                orderPlaced=0;

                                            }
                                        }.start();

                                    }
                                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id)
                            {

                            }
                        });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
    }

    public void orderPlacedMessage()
    {
        AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(placeOrder);
        alertDialogBuilder2.setTitle("Orders");
        alertDialogBuilder2
                .setMessage("\nPlaced Orders: "+Integer.toString(SellerAdapter.placedOrders)+"\n\nFailed to place: "+Integer.toString(SellerAdapter.failedOrders))
                .setCancelable(false)
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        finish();
                    }});
        AlertDialog alertDialog2 = alertDialogBuilder2.create();
        alertDialog2.show();
    }

    void getSeller()
    {
        final ProgressDialog mDialog= new ProgressDialog(this);
        mDialog.setCancelable(false);
        mDialog.setMessage("Getting sellers...");
        mDialog.show();
        String url = "http://" + Config.SERVER_ADDRESS + "/android/get_sellers.php";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    mDialog.dismiss();
                    JSONArray jsonArray=new JSONArray(response);
                    for (int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject=(JSONObject)jsonArray.get(i);
                        Seller seller=new Seller(jsonObject.getString("name"),jsonObject.getString("address"),jsonObject.getString("id"),"null");
                        sellers.add(seller);
                    }
                    sellerAdapter=new SellerAdapter(sellers,getBaseContext());
                    sellers_rv.setAdapter(sellerAdapter);

                }
                catch (Exception e)
                {
                    mDialog.dismiss();
                    Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                mDialog.dismiss();
                Toast.makeText(getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        volleyRequestQueue.add(request);
    }



    public  void placeOrder(final int indexOfItem, final int indexOfSeller, final Context context)
    {

        String url = "http://" + Config.SERVER_ADDRESS + "/android/order_item.php";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                orderResponded++;
                if(response.equals("YES"))
                {
                   SellerAdapter.placedOrders++;
                }
                else
                {
                    SellerAdapter.failedOrders++;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                orderResponded++;
                Toast.makeText(context, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", Config.ID);
                params.put("item_name", ItemsToOrderFragment.items.get(indexOfItem).item_name);
                params.put("qty_to_buy",Integer.toString(ItemsToOrderFragment.items.get(indexOfItem).item_quantity));
                params.put("seller_id",PlaceOrder.sellers.get(indexOfSeller).id);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        volleyRequestQueue.add(request);

    }
    @Override
    public void onPause()
    {
        super.onPause();
        finish();
    }

}
