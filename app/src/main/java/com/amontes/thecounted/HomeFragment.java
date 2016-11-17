package com.amontes.thecounted;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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

import java.util.ArrayList;
import java.util.Calendar;

import static com.amontes.thecounted.R.id.updateTime;

public class HomeFragment extends Fragment {

    public static final String TAG = "HOMEFRAGMENT";
    private TextView currentNumber;
    private TextView lastPull;
    private int year;
    private TextView chosenYear;
    private ProgressDialog mProgress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // TextViews to be updated.
        currentNumber = (TextView) view.findViewById(R.id.recentNumberText);
        chosenYear = (TextView) view.findViewById(R.id.yearText);
        lastPull = (TextView) view.findViewById(updateTime);

        if(lastPull.getText().equals("")){

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            lastPull.setText(preferences.getString("Current", ""));

        }

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
                HomeAsyncTask homeAsyncTask = new HomeAsyncTask();
                homeAsyncTask.execute(year);
                break;

            case R.id.action_2016:
                year = 2016;
                HomeAsyncTask homeAsyncTaskTwo = new HomeAsyncTask();
                homeAsyncTaskTwo.execute(year);
                break;

            case R.id.action_2015:
                year = 2015;
                HomeAsyncTask homeAsyncTaskThree = new HomeAsyncTask();
                homeAsyncTaskThree.execute(year);
                break;

            default:
                break;

        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initial progress dialog to alert user something is happening in the background.
        mProgress = new ProgressDialog(getContext());
        mProgress.setIndeterminate(false);
        mProgress.setMessage("Working");
        mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgress.setCancelable(false);
        mProgress.show();

        setHasOptionsMenu(true);
        Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        HomeAsyncTask homeAsyncTask = new HomeAsyncTask();
        homeAsyncTask.execute(year);

    }

    // Local BroadcastReceiver.
    BroadcastReceiver currentNumReceiverHome = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String refreshTime = intent.getStringExtra("Update");

            if(!refreshTime.equals("")){

                lastPull.setText(refreshTime);

            }

        }

    };

    // AsyncTask to parse data/populate UI**********************************************************
    public class HomeAsyncTask extends AsyncTask<Integer, Integer, String>{

        private String numAll;
        private int counter = 0;

        @Override
        protected String doInBackground(Integer... params) {

            // Pull latest data from local storage.
            ArrayList<Victim> loadedArray = DataHelper.getSavedData(getContext());
            // Parse corresponding data.
            int yearArg = params[0];
            switch (yearArg){

                case 2016:
                    for (int i = 0; i < loadedArray.size(); i++) {

                        if (loadedArray.get(i).getYear().equals("2016")) {

                            counter = counter+1;

                        }

                        numAll = String.valueOf(counter);

                    }
                    break;

                case 2015:
                    for (int i = 0; i < loadedArray.size(); i++) {

                        if (loadedArray.get(i).getYear().equals("2015")) {

                            counter = counter+1;

                        }

                        numAll = String.valueOf(counter);

                    }
                    break;

                case 0:
                    numAll = String.valueOf(loadedArray.size());
                    break;

                default:
                    break;

            }

            return null;

        }

        @Override
        protected void onPostExecute(String current) {
            super.onPostExecute(current);

            // Populate UI with user's chosen data.
            if(year == 0){

                chosenYear.setText(R.string.total_label_home);

            }else{

                chosenYear.setText(String.valueOf(year));

            }
            currentNumber.setText(numAll);

            if(mProgress != null){

                mProgress.cancel();

            }

        }

    }

    @Override
    public void onPause() {
        super.onPause();

        // Un-Register BroadcastReceiver.
        getActivity().unregisterReceiver(currentNumReceiverHome);

    }

    @Override
    public void onResume() {
        super.onResume();

        // Register BroadcastReceiver.
        IntentFilter recFilter = new IntentFilter();
        recFilter.addAction("com.fullsail.android.ACTION_UPDATE_UI");
        getActivity().registerReceiver(currentNumReceiverHome, recFilter);

    }

}