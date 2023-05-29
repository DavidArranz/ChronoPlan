package org.example.cronoplanv2.controler;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import org.example.cronoplanv2.R;
import org.example.cronoplanv2.model.ItemsDAO.ChartDAO;
import org.example.cronoplanv2.model.ItemsDAO.SettingsDAO;
import org.example.cronoplanv2.model.Settings;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import it.sephiroth.android.library.numberpicker.NumberPicker;

/**
 * Clase ChartFragment que representa un fragmento para mostrar un gráfico de barras en la aplicación Cronoplan.
 * Administra la visualización y configuración del gráfico de barras, así como la interacción con los controles de configuración.
 */
public class ChartFragment extends Fragment {
    private BarChart barChart;
    private BarDataSet barDataSet;
    private NumberPicker npAmmount;
    private Spinner smeasurement;
    private final ChartDAO CHARTDATA;
    private final SettingsDAO SETTINGS;
    private ImageView ivReload;
    private int measure,ammount;
    public ChartFragment() {
        CHARTDATA = new ChartDAO();
        SETTINGS = new SettingsDAO();
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
        ivReload = (ImageView) view.findViewById(R.id.ivreload);
        ivReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setdata();
                saveSettings();
            }
        });
        setSettings();
        setdata();
        xAxis();
        yAxis();

        return view;
    }
    /**
     * Configura los valores de los controles de configuración (NumberPicker y Spinner) con los valores guardados en la base de datos.
     */
    private void setSettings() {
        Settings settings = SETTINGS.getSettings();
        npAmmount.setProgress(settings.getChart_ammount());
        smeasurement.setSelection(settings.getChart_time_interval());
    }
    /**
     * Guarda los valores seleccionados en los controles de configuración en la base de datos.
     */
    private void saveSettings(){
        SETTINGS.save(smeasurement.getSelectedItemPosition(),npAmmount.getProgress());
    }

    /**
     * Configura los datos del gráfico de barras utilizando los valores seleccionados en los controles de configuración.
     * Obtiene los datos del gráfico de la base de datos y los muestra en el gráfico de barras.
     */
    private void setdata() {
        ArrayList<Float> y;
        ArrayList<String> x;
        ArrayList<Integer> date;
        measure=smeasurement.getSelectedItemPosition();
        ammount=npAmmount.getProgress();
        ArrayList<BarEntry> entries = new ArrayList<>();

        ArrayList[] xy = CHARTDATA.getData(smeasurement.getSelectedItemPosition(),npAmmount.getProgress());

        x=xy[0];
        y=xy[1];
        date=xy[2];
        int i=0;
        if(measure==0||measure == 2){
        while( i<=y.size()-1){
            entries.add(new BarEntry(i, y.get(i)));
            i++;
        }
        if(measure==0) {
            for (int d : date) {
                try {
                    x.add(new SimpleDateFormat("yyyy MMM dd").format(new SimpleDateFormat("yyyyMMdd").parse(String.valueOf(d))));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        }else{
            for (int d : date) {
                try {
                    x.add(new SimpleDateFormat("yyyy MMM").format(new SimpleDateFormat("yyyyMM").parse(String.valueOf(d))));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        }else if(measure==1) {
            while (i <= y.size() - 1) {
                entries.add(new BarEntry(i, y.get(i)));
                i++;
            }

        }
        // Set the value formatter for the X-axis labels
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(x));
        barDataSet = new BarDataSet(entries,"Hours focused");
        barDataSet.setColors(new int[]{ContextCompat.getColor(getContext(), R.color.green_light_2)});

        BarData data = new BarData();
        data.addDataSet(barDataSet);
        barChart.setData(data);
        barChart.notifyDataSetChanged();// Notify the chart that the data has changed
        barChart.invalidate();// Redraw the chart
    }
    /**
     * Configura el eje Y del gráfico de barras.
     * Define el formato de los valores del eje Y y configura las propiedades del eje.
     */
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
        barChart.getDescription().setEnabled(false); // hide chart description


        // Get a reference to the right axis object and disable it
        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setEnabled(false);
    }
    /**
     * Configura el eje X del gráfico de barras.
     * Define las propiedades del eje X, como la posición de las etiquetas y el formato de las mismas.
     */
    private void xAxis() {
        //x axis modification
        // Get the X-axis object
        XAxis xAxis = barChart.getXAxis();

        xAxis.setDrawLabels(true);// Enable the X-axis labels to be drawn
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false); // hide x-axis grid lines
            xAxis.setLabelRotationAngle(-35);//label tilt to avoid superposition
    }
}