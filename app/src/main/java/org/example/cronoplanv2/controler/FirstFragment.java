package org.example.cronoplanv2.controler;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import org.example.cronoplanv2.R;
import org.example.cronoplanv2.model.ItemsDAO.ChartDAO;
import org.example.cronoplanv2.model.ItemsDAO.TaskDAO;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class FirstFragment extends Fragment {
    private BarChart barChart;
    private BarDataSet barDataSet;
    private final ChartDAO CHARTDATA = new ChartDAO();
    public FirstFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        barChart=(BarChart) view.findViewById(R.id.weekBarGraph);
        setdata();
        xAxis();
        yAxis();

        return view;
    }

    private void setdata() {
        /*ArrayList[] rawData = CHARTDATA.getData();
        ArrayList<Float> x = (ArrayList<Float>) rawData[0];
        ArrayList<Float> y = (ArrayList<Float>) rawData[1];
        ArrayList<BarEntry> entries = new ArrayList<>(); //
        for(int i=0; i<=x.size();i++) {
            entries.add(new BarEntry(x.get(i), y.get(i)));
        }*/
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, 44f));
        entries.add(new BarEntry(1f, 30f));
        entries.add(new BarEntry(2f, 20f));
        entries.add(new BarEntry(3f, 30f));
        entries.add(new BarEntry(4f, 55f));
        barDataSet = new BarDataSet(entries,"week");

        BarData data = new BarData();
        data.addDataSet(barDataSet);
        barChart.setData(data);
    }

    private void yAxis() {
        //y axis modification
        // get the y-axis object
        YAxis yAxis = barChart.getAxisLeft();

        // set the value formatter to convert floats to integers
        yAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) value);
            }
        });
        yAxis.setGranularity(1f);
        barChart.invalidate();

        // Get a reference to the right axis object and disable it
        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setEnabled(false);
    }

    private void xAxis() {
        //x axis modification
        // Get the X-axis object
        XAxis xAxis = barChart.getXAxis();

        // Set the value formatter for the X-axis labels
        xAxis.setValueFormatter(new IndexAxisValueFormatter(new String[]{"January", "February", "March", "April", "May"}));

        // Enable the X-axis labels to be drawn
        xAxis.setDrawLabels(true);
        xAxis.setGranularity(1f);
    }
}