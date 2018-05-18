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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Pravin on 4/8/2018.
 */

public class SellerRegistration extends AppCompatActivity implements View.OnClickListener
{
    private EditText name_et, address_et, location_et, mobile_no_et;
    private Button register_bt;
    private String SMS_SENT = "SMS_SENT";
    private static final int PERMISSION_REQUEST_CODE = 1;
    private ProgressDialog mDialog;
    private String name,address,location,mobile_number,otp;
    private BroadcastReceiver sms_sent_notifier;
    private ArrayList<String> locations;
    private RequestQueue volleyRequestQueue;

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.selller_registration);
        name_et = (EditText) findViewById(R.id.seller_name_et);
        address_et = (EditText) findViewById(R.id.seller_address_et);
        location_et = (EditText) findViewById(R.id.seller_location_et);
        mobile_no_et = (EditText) findViewById(R.id.mobile_number_et);
        register_bt = (Button) findViewById(R.id.register_bt);
        volleyRequestQueue = Volley.newRequestQueue(this);
        mDialog=new ProgressDialog(this);
        register_bt.setOnClickListener(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 123);
            register_bt.setEnabled(false);
        } else {
            setToReadSmsStatus();
            register_bt.setEnabled(true);
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
            public void onReceive(Context context, Intent intent)
            {
                mDialog.dismiss();
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(context, "OTP sent successfully", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), OTPVerificationForSeller.class);
                        i.putExtra("name",name);
                        i.putExtra("address",address);
                        i.putExtra("location",location);
                        i.putExtra("mobile_number",mobile_number);
                        i.putExtra("otp",otp);
                        startActivity(i);
                        finish();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(context, "Failed to send OTP", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(context, "Failed to send OTP", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(context, "Failed to send OTP", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(context, "Failed to send OTP", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
        registerReceiver(sms_sent_notifier, new IntentFilter(SMS_SENT));

    }

    @Override
    public void onClick(View v)
    {
        if(name_et.getText().toString().trim().isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Seller name should not be empty", Toast.LENGTH_SHORT).show();
        }
        else if(address_et.getText().toString().trim().isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Address should not be empty", Toast.LENGTH_SHORT).show();
        }
        else if(location_et.getText().toString().trim().isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Location should not be empty", Toast.LENGTH_SHORT).show();
        }
        else if(mobile_no_et.getText().toString().trim().isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Mobile number should not be empty", Toast.LENGTH_SHORT).show();
        }
        else if(mobile_no_et.getText().toString().trim().length()<10 || mobile_no_et.getText().toString().trim().length()>10 )
        {
            Toast.makeText(getApplicationContext(), "Mobile number length should be 10 digit only", Toast.LENGTH_SHORT).show();
        }
        else
        {
            mDialog.setMessage("Registering..");
            mDialog.show();
            name = name_et.getText().toString().trim();
            address = address_et.getText().toString().trim();
            location = location_et.getText().toString().trim();
            mobile_number = mobile_no_et.getText().toString().trim();
            otp = Integer.toString(new Random().nextInt(600000));
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(mobile_number, null, otp, PendingIntent.getBroadcast(this, 0, new Intent(SMS_SENT), 0), null);
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        unregisterReceiver(sms_sent_notifier);
    }


}
