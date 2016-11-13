package com.amontes.thecounted;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class GraphingActivity extends AppCompatActivity {

    private String numAll = null;
    private String numUnknown = null;
    private String numWhite = null;
    private String numHispanic = null;
    private String numBlack = null;
    private String numAsian = null;
    private String numNative = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphing);

        // Up button.
        assert getSupportActionBar() !=null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayList<Victim> savedArray = null;

        try {

            FileInputStream fis = getBaseContext().openFileInput("Victims");
            ObjectInputStream ois = new ObjectInputStream(fis);
            savedArray = (ArrayList<Victim>) ois.readObject();
            ois.close();

        } catch (IOException | ClassNotFoundException e) {

            e.printStackTrace();

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
