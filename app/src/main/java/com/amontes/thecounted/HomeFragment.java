package com.amontes.thecounted;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;

import static com.amontes.thecounted.R.id.updateTime;

public class HomeFragment extends Fragment {

    private TextView currentNumber;
    private TextView lastPull;
    private int year;
    private TextView chosenYear;
    private ProgressDialog mProgress;
    private int counter = 0;
    private SharedPreferences preferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // TextViews to be updated.
        currentNumber = (TextView) view.findViewById(R.id.recentNumberText);
        chosenYear = (TextView) view.findViewById(R.id.yearText);
        lastPull = (TextView) view.findViewById(updateTime);

        // Initial API call. Should only be called on initial launch, then saved, then AlarmManager will update afterwards.
        /*if(fileExistence("TheCountedVictims")){

            ArrayList<Victim> loadedArray = DataHelper.getSavedData(getContext());

            // Populate UI with existing data.
            chosenYear.setText(String.valueOf(year));

            for (int i = 0; i < loadedArray.size(); i++) {

                if (loadedArray.get(i).getYear().equals(String.valueOf(year))) {

                    counter = counter+1;

                }

                currentNumber.setText(String.valueOf(counter));
                // Load last update "time stamp" from default shared preferences.
                lastPull.setText(preferences.getString("Current", ""));

            }

        }else{

            startProgressDialog("Please Wait", "Pulling fresh data for "+year);
            Intent initialIntent = new Intent(getContext(), ApiService.class);
            initialIntent.putExtra("Year", year)
                    .putExtra("Fresh", true);
            getActivity().startService(initialIntent);

        }*/

        return view;

    }

    // Inflate menu.
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.main, menu);

    }

    @Override
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

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        setHasOptionsMenu(true);

        /*Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        // AlarmManager to update data every night at 10:30.
        // Random hour and minutes so as not to flood the server. (Between 10:30 and 11:59)
        boolean alarmUp = (PendingIntent.getService(getContext(), 0, new Intent(getContext(), ApiService.class), PendingIntent.FLAG_NO_CREATE) != null);
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
            Intent intent = new Intent(getContext(), ApiService.class);
            intent.putExtra("Year", year)
                    .putExtra("Fresh", true)
                    .putExtra("Progress", false);
            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
            PendingIntent pendingIntent = PendingIntent.getService(getContext(), 0, intent, 0);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        }*/

    }

    // Start progress dialog.
    protected void startProgressDialog(String title, String message) {
        mProgress = new ProgressDialog(getContext());
        mProgress.setTitle(title);
        mProgress.setIndeterminate(false);
        mProgress.setMessage(message);
        mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgress.setCancelable(false);
        mProgress.show();

    }

    // Start IntentService.
    protected void fireService(){

        Intent intent = new Intent(getContext(), ApiService.class);
        intent.putExtra("Year", year);
        getActivity().startService(intent);

    }

    // Check if file exists.
    public boolean fileExistence(String fName){

        File file = getActivity().getFileStreamPath(fName);
        return file.exists();

    }

    @Override
    public void onPause() {
        super.onPause();


    }

    @Override
    public void onResume() {
        super.onResume();

    }

}