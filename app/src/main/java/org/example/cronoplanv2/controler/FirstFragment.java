package org.example.cronoplanv2.controler;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

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

import it.sephiroth.android.library.numberpicker.NumberPicker;


public class FirstFragment extends Fragment {
    private BarChart barChart;
    private BarDataSet barDataSet;
    private NumberPicker npAmmount;
    private Spinner smeasurement;
    private final ChartDAO CHARTDATA;
    public FirstFragment() {
        CHARTDATA = new ChartDAO();
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
        npAmmount = (NumberPicker) view.findViewById(R.id.npAmountOf);
        smeasurement = (Spinner) view.findViewById(R.id.cbTimeMeasure);
        setdata();
        xAxis();
        yAxis();

        return view;
    }

    private void setdata() {
        ArrayList<Float> x,y;
        int measure=smeasurement.getSelectedItemPosition(),ammount=npAmmount.getProgress(),i;
        ArrayList<BarEntry> entries = new ArrayList<>();
        /*ArrayList[] rawData = CHARTDATA.getData();
        ArrayList<Float> x = (ArrayList<Float>) rawData[0];
        ArrayList<Float> y = (ArrayList<Float>) rawData[1];
        ArrayList<BarEntry> entries = new ArrayList<>(); //
        for(int i=0; i<=x.size();i++) {
            entries.add(new BarEntry(x.get(i), y.get(i)));
        }*/

        ArrayList[] xy = CHARTDATA.getData(smeasurement.getSelectedItemPosition(),npAmmount.getProgress());

        x=xy[0];
        y=xy[1];
        i=0;
        if(measure==0){
        while(ammount>i || i==x.size()-1){
            entries.add(new BarEntry(x.get(i), y.get(i)));
            i--;
        }
        }else if(measure==1) {
            while (ammount > i || i == x.size() - 1) {
                entries.add(new BarEntry(x.get(i), y.get(i)));
                i--;
            }
        }else{
            while (ammount > i || i == x.size() - 1) {
                entries.add(new BarEntry(x.get(i), y.get(i)));
                i--;
            }
        }
        barDataSet = new BarDataSet(entries,"Hours focused");
        barDataSet.setColors(new int[]{ContextCompat.getColor(getContext(), R.color.green_light_2)});

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
        barChart.setVisibleYRangeMinimum(2, YAxis.AxisDependency.LEFT);
        barChart.setVisibleXRangeMinimum(2);


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