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
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.File;
import java.util.Calendar;
import java.util.Random;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // Global variables.
    private TextView currentNumber;
    private TextView lastPull;
    private int year;
    private TextView chosenYear;
    private ProgressDialog mProgress;
    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initial API call. Should only be called on initial launch, then saved, then AlarmManager will update afterwards.
        /*if(fileExistence("TheCountedVictims")){

            ArrayList<Victim> loadedArray = DataHelper.getSavedData(this);

            // Populate UI with existing data.
            chosenYear.setText(String.valueOf(year));

            for (int i = 0; i < loadedArray.size(); i++) {

                if (loadedArray.get(i).getYear().equals(String.valueOf(year))) {

                    counter = counter+1;

                }

                // Load last update "time stamp" from default shared preferences.
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                // Populate UI with existing data.
                chosenYear.setText(String.valueOf(year));
                currentNumber.setText(String.valueOf(counter));
                // Load last update "time stamp" from default shared preferences.
                lastPull.setText(preferences.getString("Current", ""));

            }

        }else{*/

        if(!fileExistence("TheCountedVictims")) {
            startProgressDialog("Please Wait", "Pulling fresh data for " + year);
            Intent initialIntent = new Intent(this, ApiService.class);
            initialIntent.putExtra("Year", year)
                    .putExtra("Fresh", true);
            startService(initialIntent);
        }

        //}

        Log.d("MAINACTIVITY", "INSIDE ONCREATE!!!!!");
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        // AlarmManager to update data every night at 10:30.
        // Random hour and minutes so as not to flood the server. (Between 10:30 and 11:59)
        boolean alarmUp = (PendingIntent.getService(this, 0, new Intent(this, ApiService.class), PendingIntent.FLAG_NO_CREATE) != null);
        if(!alarmUp) {
            Random r = new Random();
            int lowHour = 22;
            int highHour = 23;
            int lowMins = 30;
            int highMins = 59;
            int hour = r.nextInt(highHour - lowHour) + lowHour;
            int mins = r.nextInt(highMins - lowMins) + lowMins;

            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, mins);
            calendar.set(Calendar.SECOND, 0);
            Intent intent = new Intent(this, ApiService.class);
            intent.putExtra("Year", year)
                    .putExtra("Fresh", true)
                    .putExtra("Progress", false);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, 0);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        }

        // Load initial Fragment.
        if(savedInstanceState == null){

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

        // AlarmManager to update data every night at 10:30.
        // Random hour and minutes so as not to flood the server. (Between 10:30 and 11:59)
        /*Random r = new Random();
        int lowHour = 22;
        int highHour = 23;
        int lowMins = 30;
        int highMins = 59;
        int hour = r.nextInt(highHour-lowHour) + lowHour;
        int mins = r.nextInt(highMins-lowMins) + lowMins;

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, mins);
        calendar.set(Calendar.SECOND, 0);
        Intent intent = new Intent(MainActivity.this , ApiService.class);
        intent.putExtra("Year", year)
                .putExtra("Fresh", true)
                .putExtra("Progress", false);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(MainActivity.this, 0, intent, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY , pendingIntent);*/

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

        // TextViews to be updated.
        /*currentNumber = (TextView)findViewById(R.id.recentNumberText);
        chosenYear = (TextView) findViewById(R.id.yearText);*/
        lastPull = (TextView) findViewById(R.id.updateTime);

        // Initial API call. Should only be called on initial launch, then saved, then AlarmManager will update afterwards.
        /*if(fileExistence("TheCountedVictims")){

            ArrayList<Victim> loadedArray = DataHelper.getSavedData(MainActivity.this);

            for (int i = 0; i < loadedArray.size(); i++) {

                if (loadedArray.get(i).getYear().equals(String.valueOf(year))) {

                    counter = counter+1;

                }

                // Load last update "time stamp" from default shared preferences.
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

                // Populate UI with existing data.
                chosenYear.setText(String.valueOf(year));
                currentNumber.setText(String.valueOf(counter));
                lastPull.setText(preferences.getString("Current", ""));

            }

        }else{

            startProgressDialog("Please Wait", "Pulling fresh data for "+year);
            Intent initialIntent = new Intent(MainActivity.this , ApiService.class);
            initialIntent.putExtra("Year", year)
                    .putExtra("Fresh", true);
            startService(initialIntent);

        }*/

    }

    // Handle on back button.
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    // Inflate menu.
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/

    // Get user's chosen time frame from overflow menu item and update UI.
    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id){

            case R.id.action_total:
                year = 0;
                startProgressDialog("Working", "Obtaining total data.");
                fireService();
                break;

            case R.id.action_2016:
                year = 2016;
                startProgressDialog("Working", "Obtaining data for "+year);
                fireService();
                break;

            case R.id.action_2015:
                year = 2015;
                startProgressDialog("Working", "Obtaining data for "+year);
                fireService();
                break;

            default:
                break;

        }

        return super.onOptionsItemSelected(item);

    }*/

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

            String refreshTime = intent.getStringExtra("Update");
            boolean cancel = intent.getBooleanExtra("Progress", true);
            /*String yearToShow = intent.getStringExtra("Year");
            if(yearToShow.equals(String.valueOf(0))){

                yearToShow = "TOTAL";

            }

            // Display selected year, current tally, and update time.
            chosenYear.setText(yearToShow);
            currentNumber.setText(intent.getStringExtra("Number"));*/
            if(cancel) {

                mProgress.cancel();

            }

            if(!refreshTime.equals("")){

                lastPull.setText(refreshTime);

            }

        }

    };

    // Start progress dialog.
    protected void startProgressDialog(String title, String message) {
        mProgress = new ProgressDialog(MainActivity.this);
        mProgress.setTitle(title);
        mProgress.setIndeterminate(false);
        mProgress.setMessage(message);
        mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgress.setCancelable(false);
        mProgress.show();
    }

    // Start IntentService.
    /*protected void fireService(){

        Intent intent = new Intent(this, ApiService.class);
        intent.putExtra("Year", year);
        startService(intent);

    }*/

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

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Application has been killed. Wipe SharedPreferences so data is loaded on new start.


    }
}