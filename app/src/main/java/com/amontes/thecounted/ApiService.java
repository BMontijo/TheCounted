package com.amontes.thecounted;

import android.app.IntentService;
import android.content.Intent;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiService extends IntentService {

    final String TAG = "ApiService";

    // Default constructor.
    public ApiService(){

        super("ApiService");

    }

    @Override
    protected void onHandleIntent(Intent intent) {

        int year = intent.getIntExtra("Year", 0);
        String stringValueYear = String.valueOf(year);
        String currentNum = null;

        try{

            String apiEndpoint = "https://thecountedapi.com/api/counted/?year=" + stringValueYear;
            URL url = new URL(apiEndpoint);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            InputStream is = connection.getInputStream();
            String jsonDataString = IOUtils.toString(is);
            JSONArray jsonArray = new JSONArray(jsonDataString);
            currentNum = String.valueOf(jsonArray.length());

        } catch (IOException | JSONException e) {

            e.printStackTrace();

        }

        // Broadcast to update list.
        Intent toMain = new Intent("com.fullsail.android.ACTION_UPDATE_UI");
        toMain.putExtra("Current", currentNum);
        this.sendBroadcast(toMain);

    }

}