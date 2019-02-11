package com.example.du.qianrushizhushou;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.example.du.qianrushizhushou.bluetooth_mode.bluetooth_mode;
import com.example.du.qianrushizhushou.sensor_mode.*;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private sensor_fragment sensorFragment;
    private bluetooth_mode bluetoothModeFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id){
            case R.id.menu_item_1:{
                if(sensorFragment==null)sensorFragment=new sensor_fragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.mian_fragment_content,sensorFragment).commit();

            }break;
            case R.id.menu_item_2:{
                if(bluetoothModeFragment==null)bluetoothModeFragment=new bluetooth_mode();
                getSupportFragmentManager().beginTransaction().replace(R.id.mian_fragment_content,bluetoothModeFragment).commit();
            }break;
            case R.id.menu_item_3:{

            }break;
            case R.id.menu_item_4:{

            }break;
            case R.id.menu_item_5:break;
            case R.id.menu_item_6:break;
            case R.id.menu_item_7:break;
            case R.id.menu_item_8:break;
            case R.id.menu_item_9:break;

        }
//        if (id == R.id.menu_item_1) {
//            // Handle the camera action
//        } else if (id == R.id.menu_item_2) {
//
//        } else if (id == R.id.menu_item_3) {
//
//        } else if (id == R.id.menu_item_4) {
//
//        } else if (id == R.id.menu_item_5) {
//
//        } else if (id == R.id.menu_item_6) {
//
//        } else if (id == R.id.menu_item_7) {
//
//        } else if (id == R.id.nav_send) {
//
//        } else if (id == R.id.nav_send) {
//
//        } else if (id == R.id.nav_send) {
//
//        } else if (id == R.id.nav_send) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
