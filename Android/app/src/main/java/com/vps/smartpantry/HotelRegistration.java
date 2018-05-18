package com.vps.smartpantry;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Pravin on 4/7/2018.
 */

public class HotelRegistration extends AppCompatActivity implements View.OnClickListener
{
    private EditText hotel_name_et,authority_name_et,address_et,mobile_number_et;
    private Button register_bt;
    private String SMS_SENT = "SMS_SENT";
    private static final int PERMISSION_REQUEST_CODE = 1;
    private ProgressDialog mDialog;
    private String hotel_name,authority_name,address,mobile_number,otp;
    private BroadcastReceiver sms_sent_notifier;
    private Spinner sellers_locations;
    private RequestQueue volleyRequestQueue;
    private ArrayList<String> locations;
    @Override
    public void onCreate(Bundle b)
    {
        super.onCreate(b);
        setContentView(R.layout.hotel_registration);

        hotel_name_et=(EditText)findViewById(R.id.hotel_name_hotel_registration_et);
        authority_name_et=(EditText)findViewById(R.id.authority_name_hotel_registration_et);
        address_et=(EditText)findViewById(R.id.address_hotel_registration_et);
        mobile_number_et=(EditText)findViewById(R.id.mobile_number_hotel_registration_et);
        register_bt=(Button)findViewById(R.id.register_hotel_registration_bt);
        sellers_locations=(Spinner)findViewById(R.id.sellers_locations_spinner);

        mDialog = new ProgressDialog(this);
        mDialog.setCancelable(false);
        volleyRequestQueue = Volley.newRequestQueue(this);
        locations=new ArrayList<String>();
        resetLocationList();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 123);
            register_bt.setEnabled(false);
        }
        else
        {
            setToReadSmsStatus();
            register_bt.setEnabled(true);
        }


        register_bt.setOnClickListener(this);
        sellers_locations.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, locations));
        getSellersLocations();
    }


    @Override
    public void onClick(View v)
    {
        if(hotel_name_et.getText().toString().trim().isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Hotel name should not be empty", Toast.LENGTH_SHORT).show();
        }
        else if(authority_name_et.getText().toString().trim().isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Authority name should not be empty", Toast.LENGTH_SHORT).show();
        }
        else if(address_et.getText().toString().trim().isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Address should not be empty", Toast.LENGTH_SHORT).show();
        }
        else if(mobile_number_et.getText().toString().trim().isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Mobile number should not be empty", Toast.LENGTH_SHORT).show();
        }
        else if(mobile_number_et.getText().toString().trim().length()<10 || mobile_number_et.getText().toString().trim().length()>10 )
        {
            Toast.makeText(getApplicationContext(), "Mobile number length should be 10 digit only", Toast.LENGTH_SHORT).show();
        }
        else {
            mDialog.setMessage("Registering..");
            mDialog.show();
            hotel_name = hotel_name_et.getText().toString().trim();
            authority_name = authority_name_et.getText().toString().trim();
            address = address_et.getText().toString().trim();
            mobile_number = mobile_number_et.getText().toString().trim();
            otp = Integer.toString(new Random().nextInt(600000));

            if (sellers_locations.getSelectedItem().toString().equals("Select location..")) {
                Toast.makeText(this, "Please select location", Toast.LENGTH_SHORT).show();
                mDialog.hide();
            } else {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(mobile_number, null, otp, PendingIntent.getBroadcast(this, 0, new Intent(SMS_SENT), 0), null);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        if (requestCode == 123) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this,"Permission for sending sms is granted",Toast.LENGTH_SHORT).show();
                setToReadSmsStatus();
                register_bt.setEnabled(true);
            }
            else
            {
                Toast.makeText(this,"Permission for sending sms is not granted",Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }


    void setToReadSmsStatus()
    {
       sms_sent_notifier= new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        mDialog.hide();
                        Toast.makeText(context, "OTP sent successfully", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), OTPVerificationForHotel.class);
                        i.putExtra("hotel_name",hotel_name);
                        i.putExtra("authority_name",authority_name);
                        i.putExtra("address",address);
                        i.putExtra("mobile_number",mobile_number);
                        i.putExtra("otp",otp);
                        i.putExtra("location",sellers_locations.getSelectedItem().toString());
                        startActivity(i);
                        finish();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        mDialog.hide();
                        Toast.makeText(context, "Failed to send OTP", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        mDialog.hide();
                        Toast.makeText(context, "Failed to send OTP", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        mDialog.hide();
                        Toast.makeText(context, "Failed to send OTP", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        mDialog.hide();
                        Toast.makeText(context, "Failed to send OTP", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
        registerReceiver(sms_sent_notifier, new IntentFilter(SMS_SENT));

    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        mDialog.dismiss();
        unregisterReceiver(sms_sent_notifier);
    }

    public void resetLocationList()
    {
        locations.clear();
        locations.add("Select location..");
    }

    public void getSellersLocations()
    {
        mDialog.setMessage("Getting available locations...");
        mDialog.show();
        String url="http://"+Config.SERVER_ADDRESS+"/android/get_sellers_location.php";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    resetLocationList();
                    if(!response.contains("null"))
                    {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            if(!locations.contains(jsonObject.getString("location")))
                            {
                                locations.add(jsonObject.getString("location"));
                            }
                        }
                    }
                    sellers_locations.setAdapter(new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, locations));
                    sellers_locations.invalidate();
                    mDialog.hide();
                }
                catch (Exception e)
                {
                    mDialog.hide();
                    Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                mDialog.hide();
                Toast.makeText(getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String,String> getParams()
            {
                Map<String,String> data=new HashMap<String, String>();
                data.put("data","null");
                return data;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        volleyRequestQueue.add(request);
    }


}
