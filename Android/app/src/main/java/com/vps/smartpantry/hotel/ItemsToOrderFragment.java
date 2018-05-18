package com.vps.smartpantry.hotel;

/**
 * Created by Pravin on 4/8/2018.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.vps.smartpantry.R;
import com.vps.smartpantry.hotel.support.Item;
import com.vps.smartpantry.hotel.support.ItemAdapterForItemsToOrder;
import com.vps.smartpantry.hotel.support.Seller;
import com.vps.smartpantry.hotel.support.SellerAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ItemsToOrderFragment extends Fragment {
    private RecyclerView items_rv;
    public static List<Item> items;
    private ItemAdapterForItemsToOrder itemAdapterForItemsToOrder;
    private ProgressDialog mDialog;
    private RequestQueue volleyRequestQueue;
    private SharedPreferences prefs;
    private String id;
    private Database db;
    private BroadcastReceiver update_receiver;
    private IntentFilter filter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.items_to_order_hotel_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        prefs = getActivity().getSharedPreferences(Config.MY_PREFS_NAME, Activity.MODE_PRIVATE);
        items_rv = (RecyclerView) view.findViewById(R.id.items_rv);

        volleyRequestQueue = Volley.newRequestQueue(getContext());
        db = new Database(getContext());
        id = prefs.getString("mobile_number", null);
        mDialog = new ProgressDialog(getContext());
        mDialog.setCancelable(false);

        items = new ArrayList<Item>();
        //getData();
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        items_rv.setLayoutManager(mLayoutManager);
        items_rv.setItemAnimator(new DefaultItemAnimator());

         filter = new IntentFilter();
        filter.addAction("com.smartpantry.updateui");
        update_receiver=new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent) {
                getData();

            }
        };
        getActivity().registerReceiver(update_receiver,filter);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.buy_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.chechouts_item_menu:
                if (ItemAdapterForItemsToOrder.itemCount == 0) {
                    Toast.makeText(getContext(), "No item is selected to checkout", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                    alertDialogBuilder.setTitle("Items selected");
                    // set dialog message
                    alertDialogBuilder
                            .setMessage("Confirmed items?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    startActivity(new Intent(getContext(), PlaceOrder.class));
                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
                return true;
            default:
                break;
        }

        return false;
    }

    public void getData() {
        mDialog.setMessage("Getting items...");
        mDialog.show();
        items.clear();
        itemAdapterForItemsToOrder = new ItemAdapterForItemsToOrder(items, getContext());
        items_rv.setAdapter(itemAdapterForItemsToOrder);
        String url = "http://" + Config.SERVER_ADDRESS + "/android/items_to_buy.php";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals("null")) {
                    try {
                        mDialog.dismiss();
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            Item item = new Item(jsonObject.getString("item_name"), Integer.parseInt(jsonObject.getString("quantity_to_buy")));
                            if (db.getSellerId(jsonObject.getString("item_name")).equals(Database.NULL))
                            {
                                items.add(item);
                            }
                            else
                             {
                                placeOrder(item.item_name,item.item_quantity,db.getSellerId(item.item_name));
                             }
                        }
                        ItemAdapterForItemsToOrder.itemCount = 0;
                    } catch (Exception e) {
                        mDialog.dismiss();
                        Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                    itemAdapterForItemsToOrder.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "No items have to buy..", Toast.LENGTH_LONG).show();
                    mDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mDialog.dismiss();
                Toast.makeText(getContext(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
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

    @Override
    public void onPause()
    {
        super.onPause();
        getActivity().unregisterReceiver(update_receiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(update_receiver,filter);
        getData();
    }

    public void placeOrder(final String item_name,final int quantity_to_buy,final String seller_id)
    {
         String url = "http://" + Config.SERVER_ADDRESS + "/android/order_item.php";

            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response)
                {
                    if(response.equals("YES"))
                    {
                        Toast.makeText(getContext(), "Auto order placed", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getContext(),response, Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error)
                {
                    Toast.makeText(getContext(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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
            volleyRequestQueue.add(request);

        }


}