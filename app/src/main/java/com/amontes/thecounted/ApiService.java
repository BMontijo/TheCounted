package com.amontes.thecounted;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ApiService extends IntentService {

    ArrayList<Victim> victimArray = new ArrayList<>();
    final String TAG = "ApiService";

    // Default constructor.
    public ApiService(){

        super("ApiService");

    }

    @Override
    protected void onHandleIntent(Intent intent) {

        int counter = 0;
        int apiYear = intent.getIntExtra("Year", 0);
        String stringValueYear = String.valueOf(apiYear);
        String numAll = null;
        String name;
        String age;
        String sex;
        String race;
        String month;
        String day;
        String year;
        String address;
        String city;
        String state;
        String cause;
        String dept;
        String armed;

        // If saved array exists utilize it before making an unnecessary API call!
        // Add boolean later to check if scheduled update(send as Extra)! This will skip saved data, call API, and update stored array.
        if(fileExistence("Victims")){

            Log.d(TAG, "SAVE EXISTS! USING SAVED DATA!!!!");
            ArrayList<Victim> savedArray = null;

            try {

                FileInputStream fis = getBaseContext().openFileInput("Victims");
                ObjectInputStream ois = new ObjectInputStream(fis);
                savedArray = (ArrayList<Victim>) ois.readObject();
                ois.close();

            } catch (IOException | ClassNotFoundException e) {

                e.printStackTrace();

            }

            victimArray = savedArray;

        }else{

            Log.d(TAG, "THERE IS NO SAVED DATA! CALLING API FOR UPDATE");

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

        }

        // Save array to storage.
        File dataFile = new File(getBaseContext().getFilesDir(), "Victims");
        try {

            FileOutputStream fos = new FileOutputStream(dataFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(victimArray);
            oos.close();

        } catch (IOException e) {

            e.printStackTrace();

        }

        // Check which total should be returned according to year.
        if(apiYear == 0) {

            numAll = String.valueOf(victimArray.size());

        }else if(apiYear == 2016){

            for (int i = 0; i < victimArray.size(); i++) {

                if (victimArray.get(i).getYear().equals("2016")) {

                    counter = counter+1;

                }

                numAll = String.valueOf(counter);

            }

        }else if(apiYear == 2015){

            for (int i = 0; i < victimArray.size(); i++) {

                if (victimArray.get(i).getYear().equals("2015")) {

                    counter = counter+1;

                }

                numAll = String.valueOf(counter);

            }

        }

        // Broadcast to update TextViews in MainActivity.
        Intent toMain = new Intent("com.fullsail.android.ACTION_UPDATE_UI");
        toMain.putExtra("Number", numAll)
                .putExtra("Year", stringValueYear);
        this.sendBroadcast(toMain);

    }

    // Check if file exists.
    public boolean fileExistence(String fName){

        File file = getBaseContext().getFileStreamPath(fName);
        return file.exists();

    }

}