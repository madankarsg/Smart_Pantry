package com.vps.smartpantry.seller;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
import com.vps.smartpantry.R;
import com.vps.smartpantry.hotel.support.RecyclerItemClickListener;
import com.vps.smartpantry.seller.support.Item;
import com.vps.smartpantry.seller.support.ItemAdapterForSellerOrders;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccptedOrdersFragment extends Fragment {
    private RecyclerView orders_rv;
    private List<Item> items;
    private ItemAdapterForSellerOrders itemAdapterForSellerOrders;
    private RequestQueue volleyRequestQueue;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.seller_orders_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        orders_rv = (RecyclerView) view.findViewById(R.id.orders_rv);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        orders_rv.setLayoutManager(mLayoutManager);
        orders_rv.setItemAnimator(new DefaultItemAnimator());
        items = new ArrayList<Item>();
        volleyRequestQueue = Volley.newRequestQueue(getContext());

        orders_rv.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), orders_rv, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                        alertDialogBuilder.setTitle("Order");
                        // set dialog message
                        alertDialogBuilder
                                .setMessage("Deliver order?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        calibrateOrder(true, position);
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        calibrateOrder(false, position);
                                    }
                                });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );
    }

    public void calibrateOrder(boolean cmd, final int pos) {
        final ProgressDialog mDialog = new ProgressDialog(getContext());
        mDialog.setCancelable(false);
        mDialog.setMessage("Please wait...");
        mDialog.show();
        String url = "";
        if (cmd)
            url = "http://" + Config.SERVER_ADDRESS + "/android/deliver_order.php";
        else
            url = "http://" + Config.SERVER_ADDRESS + "/android/cancel_delivery.php";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("YES")) {
                    Toast.makeText(getContext(), "Operation Successful", Toast.LENGTH_SHORT).show();
                    getOrders();
                    mDialog.dismiss();
                    itemAdapterForSellerOrders.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Operation failed", Toast.LENGTH_LONG).show();
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
                params.put("id", Config.ID);
                params.put("hotel_id", items.get(pos).hotel_contact);
                params.put("item_name", items.get(pos).item_name);
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

    public void getOrders() {
        final ProgressDialog mDialog = new ProgressDialog(getContext());
        mDialog.setCancelable(false);
        mDialog.setMessage("Getting orders...");
        mDialog.show();
        items.clear();
        itemAdapterForSellerOrders = new ItemAdapterForSellerOrders(items);
        orders_rv.setAdapter(itemAdapterForSellerOrders);
        String url = "http://" + Config.SERVER_ADDRESS + "/android/accepted_orders.php";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals("null")) {
                    try {
                        mDialog.dismiss();
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            Item item = new Item(jsonObject.getString("item_name"), Integer.parseInt(jsonObject.getString("quantity")), jsonObject.getString("hotel_name"), jsonObject.getString("address"), jsonObject.getString("contact"));
                            items.add(item);
                        }
                        itemAdapterForSellerOrders.notifyDataSetChanged();
                    } catch (Exception e) {
                        itemAdapterForSellerOrders.notifyDataSetChanged();
                        mDialog.dismiss();
                        Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    itemAdapterForSellerOrders.notifyDataSetChanged();
                    Toast.makeText(getContext(), "No orders accepted...", Toast.LENGTH_LONG).show();
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
                params.put("id", Config.ID);
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
    public void onResume() {
        super.onResume();
        getOrders();
    }
}