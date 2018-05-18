package com.vps.smartpantry.seller;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.vps.smartpantry.seller.support.Item;
import com.vps.smartpantry.seller.support.ItemAdapterForNearbyHotels;
import com.vps.smartpantry.seller.support.ItemAdapterForSellerOrders;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NearbyHotelsFragment extends Fragment
{
    private RecyclerView hotels_rv;
    private List<Item> items;
    private ItemAdapterForNearbyHotels itemAdapterForNearbyHotels;
    private RequestQueue volleyRequestQueue;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.nearby_hotes_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        hotels_rv = (RecyclerView) view.findViewById(R.id.hotels_rv);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        hotels_rv.setLayoutManager(mLayoutManager);
        hotels_rv.setItemAnimator(new DefaultItemAnimator());
        items = new ArrayList<Item>();
        volleyRequestQueue = Volley.newRequestQueue(getContext());

    }


    public void getNearbyHotels()
    {
        final ProgressDialog mDialog = new ProgressDialog(getContext());
        mDialog.setCancelable(false);
        mDialog.setMessage("Getting nearby hotels...");
        mDialog.show();
        items.clear();
        itemAdapterForNearbyHotels = new ItemAdapterForNearbyHotels(items);
        hotels_rv.setAdapter(itemAdapterForNearbyHotels);

        String url = "http://" + Config.SERVER_ADDRESS + "/android/nearby_hotels.php";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals("null")) {
                    try {
                        mDialog.dismiss();
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            Item item = new Item(jsonObject.getString("name"),jsonObject.getString("address"),jsonObject.getString("contact"));
                            items.add(item);
                        }
                        itemAdapterForNearbyHotels.notifyDataSetChanged();
                    } catch (Exception e) {
                        itemAdapterForNearbyHotels.notifyDataSetChanged();
                        mDialog.dismiss();
                        Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    itemAdapterForNearbyHotels.notifyDataSetChanged();
                    Toast.makeText(getContext(), "No nearby hotels found...", Toast.LENGTH_LONG).show();
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
    public void onResume()
    {
        super.onResume();
        getNearbyHotels();
    }
}

