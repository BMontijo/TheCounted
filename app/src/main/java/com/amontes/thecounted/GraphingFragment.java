package com.amontes.thecounted;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;

public class GraphingFragment extends Fragment implements OnChartValueSelectedListener{

    private static final String TAG = "GraphingFragment";
    private PieChart mPieChart;
    private int year;
    private TextView lastPull;
    private ArrayList<String> titles;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Chart titles.
        titles = new ArrayList<>();
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

        lastPull = (TextView) view.findViewById(R.id.updateTimeGraph);

        // Show last updated data.
        if(lastPull.getText().equals("")){

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            lastPull.setText(preferences.getString("Current", ""));

        }

        mPieChart = (PieChart) view.findViewById(R.id.pieChart);
        mPieChart.getDescription().setEnabled(true);

        // Change font later!
        /*Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Black.ttf");

        mPieChart.setCenterTextTypeface(tf);
        mPieChart.setCenterText(generateCenterText());
        mPieChart.setCenterTextSize(10f);
        mPieChart.setCenterTextTypeface(tf);

        // radius of the center hole in percent of maximum radius
        mPieChart.setHoleRadius(45f);
        mPieChart.setTransparentCircleRadius(48f);
        mPieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        mPieChart.setOnChartValueSelectedListener(this);

        Legend l = mPieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
        l.setEnabled(true);

        mPieChart.setData(generatePieData());*/

        return view;

    }

    // Implemented chart click methods.
    @Override
    public void onValueSelected(Entry e, Highlight h) {

        Toast.makeText(getActivity(), "More info shown here!", Toast.LENGTH_SHORT).show();

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
        private int numUnknown, numWhite, numHispanic, numBlack, numAsian, numNative, numAll;
        private String yearArg;

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

            numUnknown = counter1 + counter7;
            numWhite = counter2;
            numHispanic = counter3;
            numBlack = counter4;
            numAsian = counter5;
            numNative = counter6;
            numAll = numUnknown+numWhite+numHispanic+numBlack+numAsian+numNative;
            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // Create chart data and populate UI.
            // Change font later!
            Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Black.ttf");

            mPieChart.setCenterTextTypeface(tf);
            mPieChart.setCenterText(generateCenterText());
            mPieChart.setCenterTextSize(10f);
            mPieChart.setCenterTextTypeface(tf);
            // radius of the center hole in percent of maximum radius
            mPieChart.setHoleRadius(45f);
            mPieChart.setTransparentCircleRadius(48f);
            mPieChart.animateY(700, Easing.EasingOption.EaseInOutQuad);
            mPieChart.setOnChartValueSelectedListener(GraphingFragment.this);
            mPieChart.setData(generatePieData());

        }

        private PieData generatePieData() {

            ArrayList<PieEntry> pieEntries = new ArrayList<>();

            pieEntries.add(new PieEntry(numUnknown));
            pieEntries.add(new PieEntry(numWhite));
            pieEntries.add(new PieEntry(numHispanic));
            pieEntries.add(new PieEntry(numBlack));
            pieEntries.add(new PieEntry(numAsian));
            pieEntries.add(new PieEntry(numNative));

            PieDataSet pieDataSet = new PieDataSet(pieEntries, "Test");

            // Colors.
            ArrayList<Integer> colors = new ArrayList<>();

            // TODO: Add two more colors!!!!
            colors.add(ContextCompat.getColor(getActivity(), R.color.colorAccent));
            colors.add(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
            colors.add(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
            colors.add(ContextCompat.getColor(getActivity(), R.color.counterColor));
            pieDataSet.setSliceSpace(2);
            pieDataSet.setColors(colors);

            PieData pieData = new PieData(pieDataSet);
            //pieData.setValueFormatter(new PercentFormatter());
            pieData.setValueTextSize(11f);
            pieData.setValueTextColor(Color.WHITE);
            pieData.setValueTextSize(16);

            return pieData;

        }

        private SpannableString generateCenterText() {
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

        }

    }

}