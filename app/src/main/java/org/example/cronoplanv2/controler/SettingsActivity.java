package org.example.cronoplanv2.controler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.example.cronoplanv2.R;
import org.example.cronoplanv2.model.ItemsDAO.SettingsDAO;
import org.example.cronoplanv2.model.Settings;

import it.sephiroth.android.library.numberpicker.NumberPicker;

public class SettingsActivity extends AppCompatActivity {
    private final SettingsDAO SETTINGS = new SettingsDAO();
    private NumberPicker npTime;
    private Button bnSave;
    private Settings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        npTime = (NumberPicker) findViewById(R.id.npTime);
        bnSave = (Button) findViewById(R.id.bnSave);
        settings = SETTINGS.getSettings();
        if(settings!=null){
            npTime.setProgress(settings.getTime());
        }
        bnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SETTINGS.save(npTime.getProgress());
                // Close the SettingsActivity
                finish();
            }
        });

    }
}