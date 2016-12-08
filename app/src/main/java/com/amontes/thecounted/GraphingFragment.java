package com.amontes.thecounted;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;

public class GraphingFragment extends Fragment implements OnChartValueSelectedListener{

    private static final String TAG = "GraphingFragment";
    Typeface tf;
    //private PieChart mPieChart;
    private HorizontalBarChart mBarChart;
    private int year;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Chart titles.
        ArrayList<String> titles = new ArrayList<>();
        titles.add("Unknown");
        titles.add("White");
        titles.add("Hispanic");
        titles.add("Black");
        titles.add("Asian");
        titles.add("Native");

        setHasOptionsMenu(true);
        Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        // Initial AsyncTask call.
        GraphingAsyncTask graphingAsyncTask = new GraphingAsyncTask();
        graphingAsyncTask.execute(year);

    }

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
                GraphingFragment.GraphingAsyncTask graphingAsyncTask = new GraphingAsyncTask();
                graphingAsyncTask.execute(year);
                break;

            case R.id.action_2016:
                year = 2016;
                GraphingFragment.GraphingAsyncTask graphingAsyncTaskTwo = new GraphingAsyncTask();
                graphingAsyncTaskTwo.execute(year);
                break;

            case R.id.action_2015:
                year = 2015;
                GraphingFragment.GraphingAsyncTask graphingAsyncTaskThree = new GraphingAsyncTask();
                graphingAsyncTaskThree.execute(year);
                break;

            default:
                break;

        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_graphing, container, false);

        TextView lastPull = (TextView) view.findViewById(R.id.updateTimeGraph);

        // Show last updated data.
        if(lastPull.getText().equals("")){

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            lastPull.setText(preferences.getString("Current", ""));

        }

        mBarChart = (HorizontalBarChart) view.findViewById(R.id.barChart);
        mBarChart.setOnChartValueSelectedListener(GraphingFragment.this);
        mBarChart.getDescription().setEnabled(false);
        mBarChart.setMaxVisibleValueCount(6);
        mBarChart.setPinchZoom(false);
        mBarChart.setDoubleTapToZoomEnabled(false);

        // X axis start.
        tf = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Black.ttf");
        XAxis xl = mBarChart.getXAxis();
        //xl.setDrawGridLines(false);
        xl.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        xl.setTypeface(tf);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(false);
        xl.setGranularity(10f);

        // Y axis start.
        YAxis yl = mBarChart.getAxisLeft();
        yl.setTypeface(tf);
        yl.setDrawAxisLine(true);
        yl.setDrawGridLines(false);
        yl.setAxisMinimum(0f);

        // Y axis end.
        YAxis yr = mBarChart.getAxisRight();
        yr.setTypeface(tf);
        yr.setDrawAxisLine(true);
        yr.setDrawGridLines(false);
        yr.setAxisMinimum(0f);

        mBarChart.setFitBars(true);
        mBarChart.animateY(1800);
        mBarChart.setPinchZoom(false);
        mBarChart.setDoubleTapToZoomEnabled(false);
        //mBarChart.setGridBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        //mBarChart.setDrawGridBackground(false);

        return view;

    }

    // Implemented chart click methods.
    @Override
    public void onValueSelected(Entry e, Highlight h) {

        /*int killedAmount = 0;
        String yearString;
        String selectedRace = titles.get(Math.round(h.getX()));
        switch(selectedRace){

            case "Unknown":
                killedAmount = numUnknown;
                break;

            case "White":
                killedAmount = numWhite;
                break;

            case "Hispanic":
                killedAmount = numHispanic;
                break;

            case "Black":
                killedAmount = numBlack;
                break;

            case "Asian":
                killedAmount = numAsian;
                break;

            case "Native":
                killedAmount = numNative;
                break;

            default:
                break;

        }

        if(year == 0){

            yearString = "Total";

        }else{

            yearString = String.valueOf(year);

        }*/

        Toast.makeText(getActivity(), "Pressed!", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected() {


    }

    // AsyncTask to parse data and populate chart/views.********************************************
    public class GraphingAsyncTask extends AsyncTask<Integer, Integer, String>{

        private int counter1 = 0;
        private int counter2 = 0;
        private int counter3 = 0;
        private int counter4 = 0;
        private int counter5 = 0;
        private int counter6 = 0;
        private int counter7 = 0;
        private String yearArg;
        private ArrayList<Integer> totalsArray = new ArrayList<>();

        @Override
        protected String doInBackground(Integer... params) {
            // Pull latest data from local storage.
            ArrayList<Victim> loadedArray = DataHelper.getSavedData(getContext());
            // Parse corresponding data.
            yearArg = String.valueOf(params[0]);
            if(yearArg.equals("2015") || yearArg.equals("2016")) {
                for (int i = 0; i < loadedArray.size(); i++) {

                    String race = loadedArray.get(i).getRace();
                    String year = loadedArray.get(i).getYear();
                    if (race.equals("Unknown") && year.equals(yearArg)) {

                        counter1 = counter1 + 1;

                    } else if (race.equals("White") && year.equals(yearArg)) {

                        counter2 = counter2 + 1;

                    } else if (race.equals("Hispanic/Latino") && year.equals(yearArg)) {

                        counter3 = counter3 + 1;

                    } else if (race.equals("Black") && year.equals(yearArg)) {

                        counter4 = counter4 + 1;

                    } else if (race.equals("Asian/Pacific Islander") && year.equals(yearArg)) {

                        counter5 = counter5 + 1;

                    } else if (race.equals("Native American") && year.equals(yearArg)) {

                        counter6 = counter6 + 1;
                    // Get amount of "Others" not listed by race and add to unknown later.
                    } else if(year.equals(yearArg)){

                        counter7 = counter7 + 1;

                    }

                }

            }else{

                for (int i = 0; i < loadedArray.size(); i++) {
                    String race = loadedArray.get(i).getRace();
                    switch (race) {
                        case "Unknown":
                            counter1 = counter1 + 1;


                            break;
                        case "White":
                            counter2 = counter2 + 1;


                            break;
                        case "Hispanic/Latino":
                            counter3 = counter3 + 1;


                            break;
                        case "Black":
                            counter4 = counter4 + 1;


                            break;
                        case "Asian/Pacific Islander":
                            counter5 = counter5 + 1;


                            break;
                        case "Native American":
                            counter6 = counter6 + 1;

                            break;
                        default:

                            counter7 = counter7 + 1;

                            break;
                    }

                }

            }

            int numUnknown = counter1 + counter7;
            int numWhite = counter2;
            int numHispanic = counter3;
            int numBlack = counter4;
            int numAsian = counter5;
            int numNative = counter6;
            int numAll = numUnknown + numWhite + numHispanic + numBlack + numAsian + numNative;
            totalsArray.add(numUnknown); totalsArray.add(numWhite); totalsArray.add(numHispanic);
            totalsArray.add(numBlack); totalsArray.add(numAsian); totalsArray.add(numNative);
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // Create chart data and populate UI.
            float barWidth = 4f;
            float spaceForBar = 5f;
            String yearString;
            ArrayList<BarEntry> yVals1 = new ArrayList<>();

            for (int i = 0; i < totalsArray.size(); i++) {

                int val = totalsArray.get(i);
                Log.d(TAG, "THIS NUMBER IS: "+totalsArray.get(i));
                yVals1.add(new BarEntry(i * spaceForBar, val));

            }

            BarDataSet set1;

            if (mBarChart.getData() != null && mBarChart.getData().getDataSetCount() > 0) {

                set1 = (BarDataSet)mBarChart.getData().getDataSetByIndex(0);
                set1.setValues(yVals1);
                set1.setColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                mBarChart.getData().notifyDataChanged();
                mBarChart.notifyDataSetChanged();
                mBarChart.animateY(1800);
                mBarChart.invalidate();

            } else {

                if(year == 0){

                    yearString = "Total";

                }else{

                    yearString = String.valueOf(year);

                }

                set1 = new BarDataSet(yVals1, yearString);
                set1.setColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));

                ArrayList<IBarDataSet> dataSets = new ArrayList<>();
                dataSets.add(set1);

                BarData data = new BarData(dataSets);
                data.setValueTextSize(10f);
                data.setValueTypeface(tf);
                data.setBarWidth(barWidth);
                mBarChart.setData(data);
                mBarChart.animateY(1800);
                mBarChart.invalidate();

            }

        }

        /*private SpannableString generateCenterText() {
            SpannableString s;
            if(yearArg.equals("2015") || yearArg.equals("2016")) {

                s = new SpannableString(yearArg + "\nDeaths by Race");
                s.setSpan(new RelativeSizeSpan(4f), 0, 4, 0);
                s.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getActivity(), R.color.counterColor)), 4, s.length(), 0);

            }else{

                s = new SpannableString("Total\nDeaths by Race");
                s.setSpan(new RelativeSizeSpan(4f), 0, 5, 0);
                s.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getActivity(), R.color.counterColor)), 5, s.length(), 0);

            }

            return s;

        }*/

    }

}