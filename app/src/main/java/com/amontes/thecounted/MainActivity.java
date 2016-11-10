package com.amontes.thecounted;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Global variables.
    private TextView currentNumber;
    private int year;
    private TextView chosenYear;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // TODO: Decide if FAB is needed or just obstructive at this point.
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService();
                Snackbar.make(view, "Current number has been refreshed!", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Get current year.
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        // Initial API call. Should only be called on initial launch, then saved, then AlarmManager will update afterwards.
        // Use conditional statement: if(save exists){skip service} else {run service}.
        startProgressDialog();
        startService();

        // TextViews to be updated.
        currentNumber = (TextView)findViewById(R.id.recentNumberText);
        chosenYear = (TextView) findViewById(R.id.yearText);

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

    // Get user's chosen time frame and update UI.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id){

            case R.id.action_total:
                year = 0;
                startProgressDialog();
                startService();
                break;

            case R.id.action_2016:
                year = 2016;
                startProgressDialog();
                startService();
                break;

            case R.id.action_2015:
                year = 2015;
                startProgressDialog();
                startService();
                break;

            default:
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    // TODO: Add specific nav items to application.
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // Local BroadcastReceiver.
    BroadcastReceiver currentNumReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String yearToShow = intent.getStringExtra("Year");
            if(yearToShow.equals(String.valueOf(0))){

                yearToShow = "TOTAL";

            }

            // Display year's current number.
            chosenYear.setText(yearToShow);
            currentNumber.setText(intent.getStringExtra("Number"));
            mProgress.cancel();

        }

    };

    // Start progress dialog.
    protected void startProgressDialog() {
        mProgress = new ProgressDialog(MainActivity.this);
        //mProgress.setIcon(R.drawable.beer_icon_progress);
        mProgress.setTitle("Working");
        mProgress.setIndeterminate(false);
        mProgress.setMessage("Retrieving latest data.");
        mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgress.setCancelable(false);
        mProgress.show();
    }

    // Start IntentService.
    protected void startService(){

        Intent intent = new Intent(this, ApiService.class);
        intent.putExtra("Year", year);
        startService(intent);

    }

    @Override
    protected void onPause() {
        super.onPause();

        // Un-Register BroadcastReceiver.
        unregisterReceiver(currentNumReceiver);

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Register BroadcastReceiver.
        IntentFilter recFilter = new IntentFilter();
        recFilter.addAction("com.fullsail.android.ACTION_UPDATE_UI");
        registerReceiver(currentNumReceiver, recFilter);

    }

}