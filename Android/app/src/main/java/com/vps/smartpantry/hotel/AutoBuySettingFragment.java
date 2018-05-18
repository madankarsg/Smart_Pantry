package com.vps.smartpantry.hotel;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.PathParser;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.vps.smartpantry.hotel.support.ItemAdapterForCheckPantry;
import com.vps.smartpantry.hotel.support.ItemAdapterForNearbySellers;
import com.vps.smartpantry.hotel.support.Seller;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AutoBuySettingFragment extends Fragment
{
    private List<String> item_names;
    private List<String> seller_names;
    private Spinner item_name_spinner,seller_name_spinner;
    private Database db;
    private RequestQueue volleyRequestQueue;
    private Button set_autobuy_bt;
    private List<String> seller_ids;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.autobuy_setting, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        item_name_spinner=(Spinner)view.findViewById(R.id.item_spinner);
        seller_name_spinner=(Spinner)view.findViewById(R.id.seller_spinner);
        volleyRequestQueue = Volley.newRequestQueue(getContext());
        item_names=new ArrayList<String>();
        seller_names=new ArrayList<String>();
        seller_ids=new ArrayList<String>();
        db = new Database(getContext());
        set_autobuy_bt=(Button)view.findViewById(R.id.submit_bt);
        set_autobuy_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                setToAutoBuy();
            }
        });
    }

    public void resetItems()
    {
        item_names.clear();
        item_names.add("Select item");
    }

    public void resetSellers()
    {
        seller_names.clear();
        seller_names.add("Select seller");
    }

    public void getItems()
    {
        final ProgressDialog mDialog = new ProgressDialog(getContext());
        mDialog.setCancelable(false);
        mDialog.setMessage("Getting items...");
        mDialog.show();
        resetItems();
        final ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, item_names);
        item_name_spinner.setAdapter(arrayAdapter);
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
                            if(db.getSellerId(jsonObject.getString("item_name")).equals(Database.NULL))
                            {
                                item_names.add(jsonObject.getString("item_name"));
                            }
                        }
                    }
                    catch (Exception e)
                    {
                        mDialog.dismiss();
                        Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                   arrayAdapter.notifyDataSetChanged();
                }
                else
                {
                    Toast.makeText(getContext(),"No item found to add..", Toast.LENGTH_LONG).show();
                    mDialog.dismiss();
                    arrayAdapter.notifyDataSetChanged();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mDialog.dismiss();
                arrayAdapter.notifyDataSetChanged();
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

    public void getSeller()
    {
        final ProgressDialog mDialog = new ProgressDialog(getContext());
        mDialog.setCancelable(false);
        mDialog.setMessage("Getting sellers...");
        mDialog.show();
        resetSellers();
        final ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, seller_names);
        seller_name_spinner.setAdapter(arrayAdapter);

        String url = "http://" + Config.SERVER_ADDRESS + "/android/near_by_sellers.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>()
        {
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
                            seller_names.add(jsonObject.getString("name"));
                            seller_ids.add(jsonObject.getString("id"));
                        }
                    }
                    catch (Exception e)
                    {
                        mDialog.dismiss();
                        Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                    arrayAdapter.notifyDataSetChanged();
                }
                else
                {
                    arrayAdapter.notifyDataSetChanged();
                    Toast.makeText(getContext(),"No seller found for your location..", Toast.LENGTH_LONG).show();
                    mDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                arrayAdapter.notifyDataSetChanged();
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

    public void setToAutoBuy()
    {
        if(item_names.size()==1)
        {
            Toast.makeText(getContext(),"No items found to add in auto order", Toast.LENGTH_SHORT).show();
        }
        else if(item_name_spinner.getSelectedItem().toString().equals("Select item"))
        {
            Toast.makeText(getContext(),"Please select item", Toast.LENGTH_SHORT).show();
        }
        else if(seller_names.size()==1)
        {
            Toast.makeText(getContext(),"No seller available for your location", Toast.LENGTH_SHORT).show();
        }
        else if(seller_name_spinner.getSelectedItem().toString().equals("Select seller"))
        {
            Toast.makeText(getContext(),"Please select seller", Toast.LENGTH_SHORT).show();
        }
        else
        {
            db.insert(item_name_spinner.getSelectedItem().toString(),seller_ids.get(seller_name_spinner.getSelectedItemPosition()-1));
            Toast.makeText(getContext(),"Item successfully added to auto order", Toast.LENGTH_SHORT).show();
            item_name_spinner.invalidate();
            seller_name_spinner.invalidate();
            onResume();
        }
    }
    @Override
    public void onResume()
    {
        super.onResume();
        getItems();
        getSeller();
    }
}
