package com.amontes.thecounted;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.io.File;
import java.util.Calendar;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);

        // Initial API call. Should only be called on initial launch, then saved, then AlarmManager will update afterwards.
        if(!fileExistence("TheCountedVictims")) {

            //startProgressDialog("Welcome!", "Initial set up.");
            mProgress = new ProgressDialog(MainActivity.this);
            mProgress.setTitle("Welcome");
            mProgress.setIndeterminate(false);
            mProgress.setMessage("Initial set up in progress.");
            mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgress.setCancelable(false);
            mProgress.show();

            Intent initialIntent = new Intent(this, ApiService.class);
            initialIntent.putExtra("Progress", true)
                    .putExtra("Reload", true);
            startService(initialIntent);

        }else{

            loadInitialFrag();

        }

        // AlarmManager to update data every night at 10:30.
        // Random hour and minutes so as not to flood the server. (Between 10:30 and 11:59)
        boolean alarmUp = (PendingIntent.getService(this, 0, new Intent(this, ApiService.class), PendingIntent.FLAG_NO_CREATE) != null);
        if(!alarmUp) {

            Random r = new Random();
            int lowHour = 22;
            int highHour = 23;
            int lowMins = 20;
            int highMins = 45;
            int hour = r.nextInt(highHour - lowHour) + lowHour;
            int mins = r.nextInt(highMins - lowMins) + lowMins;

            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, mins);
            calendar.set(Calendar.SECOND, 0);
            Intent intent = new Intent(this, ApiService.class);
            intent.putExtra("Progress", false)
                    .putExtra("Reload", false);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, 0);
            alarmManager.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        }

        // TODO: Decide if FAB is needed or just obstructive at this point.
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent refreshIntent = new Intent(MainActivity.this , ApiService.class);
                refreshIntent.putExtra("Year", year)
                        .putExtra("Fresh", true)
                        .putExtra("Progress", false);
                startService(refreshIntent);
                Snackbar.make(view, "Requesting fresh data! Please wait.", Snackbar.LENGTH_SHORT)
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

    }

    // Handle on back button.
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)){

            drawer.closeDrawer(GravityCompat.START);

        }else{

            super.onBackPressed();

        }

    }

    // TODO: Add specific nav items to application. Use Fragments! V4!
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;

        if (id == R.id.nav_home) {

            fragment = new HomeFragment();

        } else if (id == R.id.nav_graph) {

            fragment = new GraphingFragment();

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        // Close drawer. Switch with frag load????
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        // Replace FrameLayout with chosen Fragment.
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit();

        return true;

    }

    // Local BroadcastReceiver.
    BroadcastReceiver currentNumReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            boolean cancel = intent.getBooleanExtra("Progress", true);
            boolean reload = intent.getBooleanExtra("Reload", true);
            if(cancel) {

                mProgress.cancel();

            }

            if(reload) {

                loadInitialFrag();

            }

        }

    };

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

    // Check if file exists.
    public boolean fileExistence(String fName){

        File file = getBaseContext().getFileStreamPath(fName);
        return file.exists();

    }

    // Load initial Fragment at proper time.
    protected void loadInitialFrag(){

        Fragment fragment = null;
        Class fragmentClass;
        fragmentClass = HomeFragment.class;
        try {

            fragment = (Fragment) fragmentClass.newInstance();

        } catch (Exception e) {

            e.printStackTrace();

        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit();

    }

}