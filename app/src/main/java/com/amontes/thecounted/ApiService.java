package com.amontes.thecounted;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiService extends IntentService {

    String currentNum = null;
    String apiEndpoint;

    final String TAG = "ApiService";

    // Default constructor.
    public ApiService(){

        super("ApiService");

    }

    @Override
    protected void onHandleIntent(Intent intent) {

        boolean isTotal = false;
        int year = intent.getIntExtra("Year", 0);
        String stringValueYear = String.valueOf(year);
        String numAll = null;
        String numUnknown = null;
        String numWhite = null;
        String numHispanic = null;
        String numBlack = null;
        String numAsian = null;
        String numNative = null;
        String raceApiEndpoint;

        if(year == 0){

            apiEndpoint = "https://thecountedapi.com/api/counted";
            isTotal = true;

        }else{

            apiEndpoint = "https://thecountedapi.com/api/counted/?year=" + stringValueYear;

        }

        // Loop through all possible endpoints by race to get total number killed for each.
        for(int i=0; i<7; i++){

            switch(i) {

                case 0:
                    raceApiEndpoint = apiEndpoint;
                    numAll = getNumberKilled(raceApiEndpoint);
                    break;

                case 1:
                    if(isTotal){

                        raceApiEndpoint = apiEndpoint + "/?race=unknown";

                    }else{

                        raceApiEndpoint = apiEndpoint + "&race=unknown";

                    }
                    numUnknown = getNumberKilled(raceApiEndpoint);
                    break;

                case 2:
                    if(isTotal){

                        raceApiEndpoint = apiEndpoint + "/?race=white";

                    }else{

                        raceApiEndpoint = apiEndpoint + "&race=white";

                    }
                    numWhite = getNumberKilled(raceApiEndpoint);
                    break;

                case 3:
                    if(isTotal){

                        raceApiEndpoint = apiEndpoint + "/?race=hispanic/latino";

                    }else{

                        raceApiEndpoint = apiEndpoint + "&race=hispanic/latino";

                    }
                    numHispanic = getNumberKilled(raceApiEndpoint);
                    break;

                case 4:
                    if(isTotal){

                        raceApiEndpoint = apiEndpoint + "/?race=black";

                    }else{

                        raceApiEndpoint = apiEndpoint + "&race=black";

                    }
                    numBlack = getNumberKilled(raceApiEndpoint);
                    break;

                case 5:
                    if(isTotal){

                        raceApiEndpoint = apiEndpoint + "/?race=asian/pacific%20islander";

                    }else{

                        raceApiEndpoint = apiEndpoint + "&race=asian/pacific%20islander";

                    }
                    numAsian = getNumberKilled(raceApiEndpoint);
                    break;

                case 6:
                    if(isTotal){

                        raceApiEndpoint = apiEndpoint + "/?race=native%20american";

                    }else{

                        raceApiEndpoint = apiEndpoint + "&race=native%20american";

                    }
                    numNative = getNumberKilled(raceApiEndpoint);
                    break;

                default:
                    break;

            }

        }

        Log.d(TAG, "Unknown race killed: " + numUnknown);
        Log.d(TAG, "Hispanics killed: " + numHispanic);
        Log.d(TAG, "Blacks killed: " + numBlack);
        Log.d(TAG, "Whites killed: " + numWhite);
        Log.d(TAG, "Natives killed: " + numNative);
        Log.d(TAG, "Asians killed: " + numAsian);
        Log.d(TAG, "Total killed: " + numAll);

        // Broadcast to update TextView in MainActivity.
        Intent toMain = new Intent("com.fullsail.android.ACTION_UPDATE_UI");
        toMain.putExtra("Number", numAll)
                .putExtra("Year", stringValueYear);
        this.sendBroadcast(toMain);

    }

    // Take passed in endpoint and return total number.
    protected String getNumberKilled(String endpoint){
        try{

            URL url = new URL(endpoint);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            InputStream is = connection.getInputStream();
            String jsonDataString = IOUtils.toString(is);
            JSONArray jsonArray = new JSONArray(jsonDataString);
            // Current number of killed for specific year.
            currentNum = String.valueOf(jsonArray.length());

        } catch (IOException | JSONException e) {

            e.printStackTrace();

        }

        return currentNum;

    }

}