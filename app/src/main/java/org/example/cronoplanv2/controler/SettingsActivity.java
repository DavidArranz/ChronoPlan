package org.example.cronoplanv2.controler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import org.example.cronoplanv2.R;
import org.example.cronoplanv2.model.ItemsDAO.SettingsDAO;
import org.example.cronoplanv2.model.Settings;
import org.intellij.lang.annotations.Language;

import java.util.Locale;

import it.sephiroth.android.library.numberpicker.NumberPicker;

public class SettingsActivity extends AppCompatActivity {
    private final SettingsDAO SETTINGS = new SettingsDAO();
    private NumberPicker npTime;
    private Button bnSave;
    private Settings settings;
    private Spinner cb_lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        cb_lang = (Spinner) findViewById(R.id.cbLang);
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
                if(cb_lang.getSelectedItemPosition()==0){
                    setLocal("en");
                    System.out.println(SettingsActivity.this.getResources().getConfiguration().locale.getLanguage());
                } else {
                    setLocal("es");
                    System.out.println(SettingsActivity.this.getResources().getConfiguration().locale.getLanguage());
                }
                // Close the SettingsActivity
                finish();
            }
        });

    }
    public void setLocal( String langCode){
        Locale locale = new Locale(langCode);
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config,resources.getDisplayMetrics());
    }
}