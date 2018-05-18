package com.vps.smartpantry;

/**
 * Created by Pravin on 4/8/2018.
 */


import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
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
import com.vps.smartpantry.hotel.MainPage;

import java.util.HashMap;
import java.util.Map;

public class OTPVerificationForHotel extends AppCompatActivity implements View.OnClickListener {
    private String otp, hotel_name, mobile_number, authority_name, address,location;
    private EditText otpEditText;
    private Button submit_otp;
    private BroadcastReceiver sms_receive_br;
    private SharedPreferences.Editor editor;
    private RequestQueue volleyRequestQueue;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otpverificationforhotel);
        otpEditText = (EditText) findViewById(R.id.otp_edit_text);
        submit_otp = (Button) findViewById(R.id.submit_button);
        submit_otp.setOnClickListener(this);
        Intent i = getIntent();
        volleyRequestQueue = Volley.newRequestQueue(this);

        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Please wait...");
        mDialog.setCancelable(false);


        mobile_number = i.getStringExtra("mobile_number");
        otp = i.getStringExtra("otp");

        Log.e("otp",otp);

        hotel_name = i.getStringExtra("hotel_name");
        authority_name = i.getStringExtra("authority_name");
        address = i.getStringExtra("address");
        location=i.getStringExtra("location");

        editor = getSharedPreferences(Config.MY_PREFS_NAME, MODE_PRIVATE).edit();

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        sms_receive_br = new ReceiveOTP();
        registerReceiver(sms_receive_br, filter);
    }

    @Override
    public void onBackPressed() {
        unregisterReceiver(sms_receive_br);
        finish();
    }


    @Override
    public void onClick(View view) {
        mDialog.show();

        if (otpEditText.getText().toString().equals(otp)) {
            Toast.makeText(getApplicationContext(), " OTP verification successful", Toast.LENGTH_SHORT).show();
            registerOnline();
        } else {
            Toast.makeText(getApplicationContext(), "Wrong OTP", Toast.LENGTH_SHORT).show();
            mDialog.hide();
        }
    }


    public class ReceiveOTP extends BroadcastReceiver {
        final String SMS_BUNDLE = "pdus";

        public void onReceive(Context context, Intent intent) {
            Bundle intentExtras = intent.getExtras();
            if (intentExtras != null) {
                Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
                for (int i = 0; i < sms.length; ++i) {
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);
                    String smsBody = smsMessage.getMessageBody().toString();
                    String address = smsMessage.getOriginatingAddress();
                    if (address.contains(mobile_number)) {
                        if (smsBody.equals(otp)) {
                            Toast.makeText(context, "OTP verification successful", Toast.LENGTH_SHORT).show();
                            registerOnline();
                            break;
                        }
                    }
                }
            }
        }
    }


    void registerOnline() {
        mDialog.show();
        String url = "http://" + Config.SERVER_ADDRESS + "/android/hotel_registration.php";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if (response.trim().equals("YES")) {
                        editor.putString("hotel_name", hotel_name);
                        editor.putString("authority_name", authority_name);
                        editor.putString("address",address);
                        editor.putString("mobile_number", mobile_number);
                        editor.putString("USER_TYPE","HOTEL");
                        editor.commit();
                        Toast.makeText(getApplicationContext(), "Registration successful", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), MainPage.class));
                        finish();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Failed to register on server", Toast.LENGTH_LONG).show();
                    }
                    mDialog.hide();
                } catch (Exception e) {
                    mDialog.hide();
                    Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mDialog.hide();
                Toast.makeText(getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("hotel_name", hotel_name);
                params.put("mobile_number", mobile_number);
                params.put("authority_name",authority_name);
                params.put("address",address);
                params.put("location",location);
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
    public void onDestroy() {

        super.onDestroy();
        mDialog.dismiss();
        unregisterReceiver(sms_receive_br);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        mDialog.dismiss();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Please wait...");
        mDialog.setCancelable(false);
    }
}