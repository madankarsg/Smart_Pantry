package com.vps.smartpantry.seller;


import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.vps.smartpantry.Config;
import com.vps.smartpantry.MainActivity;
import com.vps.smartpantry.R;


/**
 * Created by Pravin on 4/8/2018.
 */

public class MainPage extends AppCompatActivity
{
    private DrawerLayout mDrawerLayout;
    private SharedPreferences prefs;
    public String name,address,mobile_number,location;
    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.main_page_seller);
        prefs= getSharedPreferences(Config.MY_PREFS_NAME, Activity.MODE_PRIVATE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#303F9F")));
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_hotel);
        View hView =  navigationView.getHeaderView(0);
        TextView name_et = (TextView)hView.findViewById(R.id.hotel_name_nav_header_et);
        TextView location_et=(TextView)hView.findViewById(R.id.authority_name_nav_et);


        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem)
                    {
                        mDrawerLayout.closeDrawers();
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });

        name=prefs.getString("name",null);
        mobile_number=prefs.getString("mobile_number",null);
        address=prefs.getString("address",null);
        location=prefs.getString("location",null);

        name_et.setText(name);
        location_et.setText(location);
        Config.ID=mobile_number;

        if(!isServiceRunning())
        {
            Intent i = new Intent(getApplicationContext(), ClientService.class);
            startService(i);
        }

        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.content_frame, new SellerOrdersFragment());
        tx.commit();



    }

    private boolean isServiceRunning()
    {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
            if("com.vps.smartpantry.seller.ClientService".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void selectDrawerItem(MenuItem menuItem)
    {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass=null;
        switch(menuItem.getItemId())
        {
            case R.id.orders:
            {
                fragmentClass=SellerOrdersFragment.class;
                break;
            }
            case R.id.accepted_orders:
            {
                fragmentClass=AccptedOrdersFragment.class;
                break;
            }
            case R.id.delivered_orders:
            {
                fragmentClass=DeliveredOrdersFragment.class;
                break;
            }
            case R.id.nearby_hotels:
            {

                fragmentClass=NearbyHotelsFragment.class;
                break;
            }

            case R.id.logout:
            {
                fragmentClass=null;
                if(isServiceRunning())
                {
                    Intent i = new Intent(getApplicationContext(), ClientService.class);
                    stopService(i);
                }
                getApplicationContext().getSharedPreferences(Config.MY_PREFS_NAME, 0).edit().clear().commit();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
                break;
            }
        }
        try
        {
            if(fragmentClass!=null)
            {
                fragment = (Fragment) fragmentClass.newInstance();
                // Insert the fragment by replacing any existing fragment
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame , fragment).commit();
            }
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }


        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawerLayout.closeDrawers();
    }
}



