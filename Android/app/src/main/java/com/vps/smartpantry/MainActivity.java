package com.vps.smartpantry;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity
{
    private RadioButton hotel_rb,seller_rb;
    private Button continue_bt;
    private SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hotel_rb=(RadioButton)findViewById(R.id.hotel_type_rb_main);
        seller_rb=(RadioButton)findViewById(R.id.seller_type_main);
        continue_bt=(Button)findViewById(R.id.continue_main_activity_bt);
        prefs= getSharedPreferences(Config.MY_PREFS_NAME, Activity.MODE_PRIVATE);

        if(prefs.getString("USER_TYPE",null)!=null)
        if(prefs.getString("USER_TYPE",null).equals("HOTEL"))
        {
            startActivity(new Intent(this,com.vps.smartpantry.hotel.MainPage.class));
            finish();
        }
        else if(prefs.getString("USER_TYPE",null).equals("SELLER"))
        {
            startActivity(new Intent(this,com.vps.smartpantry.seller.MainPage.class));
            finish();
        }

        continue_bt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(hotel_rb.isChecked())
                {
                    startActivity(new Intent(getApplicationContext(),HotelRegistration.class));
                    finish();
                }
                else if(seller_rb.isChecked())
                {
                     startActivity(new Intent(getApplicationContext(),SellerRegistration.class));
                     finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Select user type to register",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
