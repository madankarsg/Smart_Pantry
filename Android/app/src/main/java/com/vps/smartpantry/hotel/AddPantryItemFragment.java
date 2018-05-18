package com.vps.smartpantry.hotel;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

public class AddPantryItemFragment extends Fragment
{
    private EditText item_name_et,capacity_et,refill_at_et;
    private Button submit;
    private RequestQueue volleyRequestQueue;
    private String item_name,capacity,refill_at;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.add_pantry_item, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        item_name_et=(EditText)view.findViewById(R.id.item_name_et);
        capacity_et=(EditText)view.findViewById(R.id.capacity_et);
        refill_at_et=(EditText)view.findViewById(R.id.refill_at_et);
        submit=(Button)view.findViewById(R.id.submit_bt);
        volleyRequestQueue = Volley.newRequestQueue(getContext());
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(item_name_et.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(getContext(),"Item name should not be empty",Toast.LENGTH_SHORT).show();
                }
                else if(capacity_et.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(getContext(),"Capacity value should not be empty",Toast.LENGTH_SHORT).show();
                }
                else if(refill_at_et.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(getContext(),"Refill value should not be empty",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    item_name=item_name_et.getText().toString().trim();
                    capacity=capacity_et.getText().toString().trim();
                    refill_at=refill_at_et.getText().toString().trim();
                    item_name_et.setText("");
                    capacity_et.setText("");
                    refill_at_et.setText("");
                    submitData();
                }
            }
        });
    }

    public void submitData()
    {
        final ProgressDialog mDialog =new ProgressDialog(getContext());
        mDialog.setMessage("Adding item to pantry");
        mDialog.setCancelable(false);
        mDialog.show();
        String url = "http://" + Config.SERVER_ADDRESS + "/android/add_pantry_item.php";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                mDialog.dismiss();
                if(response.equals("YES"))
                {
                    Toast.makeText(getContext(),"Item added to pantry successfully", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getContext(),"Failed to add item to pantry", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                mDialog.dismiss();
                Toast.makeText(getContext(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", Config.ID);
                params.put("item_name", item_name);
                params.put("capacity",capacity);
                params.put("refill_at",refill_at);
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