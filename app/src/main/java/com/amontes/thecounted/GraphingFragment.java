package com.amontes.thecounted;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;

public class GraphingFragment extends Fragment implements OnChartValueSelectedListener{

    private PieChart mPieChart;
    ArrayList<PieEntry> pieEntries = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_graphing, container, false);

        mPieChart = (PieChart) view.findViewById(R.id.pieChart);
        mPieChart.getDescription().setEnabled(false);

        // Change font later!
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Black.ttf");

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

        mPieChart.setData(generatePieData());

        return view;

    }

    private PieData generatePieData() {

        pieEntries.add(new PieEntry(10));
        pieEntries.add(new PieEntry(30));
        pieEntries.add(new PieEntry(20));
        pieEntries.add(new PieEntry(30));
        pieEntries.add(new PieEntry(5));
        pieEntries.add(new PieEntry(10));

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "Test");

        // Colors.
        ArrayList<Integer> colors = new ArrayList<>();

        colors.add(ContextCompat.getColor(getActivity(), R.color.colorAccent));
        colors.add(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        colors.add(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        colors.add(ContextCompat.getColor(getActivity(), R.color.counterColor));

        /*for (int c : ContextCompat.getColor(getActivity(), R.color.colorAccent))
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());*/

        pieDataSet.setColors(colors);

        PieData pieData = new PieData(pieDataSet);
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextSize(11f);
        pieData.setValueTextColor(Color.WHITE);
        pieData.setValueTextSize(18);

        return pieData;

    }

    private SpannableString generateCenterText() {

        SpannableString s = new SpannableString("Race\nTestValues");
        s.setSpan(new RelativeSizeSpan(3f), 0, 4, 0);
        s.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getActivity(), R.color.counterColor)), 4, s.length(), 0);
        return s;

    }

    // Implemented chart click methods.
    @Override
    public void onValueSelected(Entry e, Highlight h) {

        Toast.makeText(getActivity(), "More info shown here!", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected() {


    }

}