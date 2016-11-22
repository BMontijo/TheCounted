package com.amontes.thecounted;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ApiService extends IntentService {

    ArrayList<Victim> victimArray = new ArrayList<>();
    final String TAG = "ApiService";

    // Default constructor.
    public ApiService(){

        super("ApiService");

    }

    @Override
    protected void onHandleIntent(Intent intent) {

        boolean shouldReload = intent.getBooleanExtra("Reload", false);
        boolean cancel = intent.getBooleanExtra("Progress", true);
        String name, age, sex, race, month, day, year, address, city, state, cause, dept, armed;
        String updateTime = "";

        Log.d(TAG, "THERE IS NO SAVED DATA!(Or Fresh Data Needed) CALLING API FOR UPDATE");

        try {

            String apiEndpoint = "https://thecountedapi.com/api/counted";
            URL url = new URL(apiEndpoint);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream is = connection.getInputStream();
            String jsonDataString = IOUtils.toString(is);
            JSONArray jsonArray = new JSONArray(jsonDataString);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject victimObject = jsonArray.getJSONObject(i);
                // Parse object elements and create custom "Victim" objects then add to array.
                name = victimObject.getString("name");
                age = victimObject.getString("age");
                sex = victimObject.getString("sex");
                race = victimObject.getString("race");
                month = victimObject.getString("month");
                day = victimObject.getString("day");
                year = victimObject.getString("year");
                address = victimObject.getString("address");
                city = victimObject.getString("city");
                state = victimObject.getString("state");
                cause = victimObject.getString("cause");
                dept = victimObject.getString("dept");
                armed = victimObject.getString("armed");

                Victim victim = new Victim(name, age, sex, race, month, day, year, address, city, state, cause, dept, armed);

                    victimArray.add(victim);

            }

                is.close();

            } catch (IOException | JSONException e) {

                e.printStackTrace();

            }

        // Get current date and time to display to user.
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.US);
        updateTime = "*Last update: "+sdf.format(calendar.getTime());
        // Save time stamp in default shared preferences.
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Current", updateTime);
        editor.apply();

        // Save array to storage.
        File dataFile = new File(getBaseContext().getFilesDir(), "TheCountedVictims");
        try {

            FileOutputStream fos = new FileOutputStream(dataFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(victimArray);
            oos.close();

        } catch (IOException e) {

            e.printStackTrace();

        }

        // Broadcast to update TextViews.
        Intent toMain = new Intent("com.fullsail.android.ACTION_UPDATE_UI");
        toMain.putExtra("Progress", cancel)
                .putExtra("Update", updateTime)
                .putExtra("Reload", shouldReload);
        this.sendBroadcast(toMain);

    }

}