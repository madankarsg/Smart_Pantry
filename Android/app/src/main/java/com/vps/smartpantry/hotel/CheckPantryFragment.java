package com.vps.smartpantry.hotel;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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
import com.vps.smartpantry.hotel.support.Item;
import com.vps.smartpantry.hotel.support.ItemAdapterForCheckPantry;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckPantryFragment extends Fragment
{
        private RecyclerView pantry_items_rv;
        private List<Item> items;
        private ItemAdapterForCheckPantry itemAdapterForCheckPantry;
        private RequestQueue volleyRequestQueue;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
        {
            return inflater.inflate(R.layout.check_pantry_fragment, container, false);
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
        {
            super.onViewCreated(view, savedInstanceState);
            pantry_items_rv=(RecyclerView)view.findViewById(R.id.items_rv);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            pantry_items_rv.setLayoutManager(mLayoutManager);
            pantry_items_rv.setItemAnimator(new DefaultItemAnimator());
            items=new ArrayList<Item>();
            volleyRequestQueue = Volley.newRequestQueue(getContext());

        }

        public void getData()
        {
            final ProgressDialog mDialog = new ProgressDialog(getContext());
            mDialog.setCancelable(false);
            mDialog.setMessage("Getting items...");
            mDialog.show();
            items.clear();
            itemAdapterForCheckPantry =new ItemAdapterForCheckPantry(items);
            pantry_items_rv.setAdapter(itemAdapterForCheckPantry);
            String url = "http://" + Config.SERVER_ADDRESS + "/android/check_pantry.php";

            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response)
                {
                    if(!response.equals("null"))
                    {
                        try
                        {
                            mDialog.dismiss();
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++)
                            {
                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                Item item = new Item(jsonObject.getString("item_name"), Integer.parseInt(jsonObject.getString("remaining_qty")),Integer.parseInt(jsonObject.getString("refill_at")),Integer.parseInt(jsonObject.getString("capacity")));
                                items.add(item);
                            }
                        }
                        catch (Exception e)
                        {
                            mDialog.dismiss();
                            Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                        itemAdapterForCheckPantry.notifyDataSetChanged();
                    }
                    else
                    {
                        Toast.makeText(getContext(),"No items in pantry..", Toast.LENGTH_LONG).show();
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
            getData();
        }

}
