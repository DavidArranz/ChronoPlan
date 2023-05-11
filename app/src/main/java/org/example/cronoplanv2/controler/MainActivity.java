package org.example.cronoplanv2.controler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;

import org.example.cronoplanv2.R;
import org.example.cronoplanv2.model.ItemsDAO.SettingsDAO;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private final SettingsDAO SETTINGS = new SettingsDAO();
    private FirstFragment firstFragment = new FirstFragment();
    private KanbanFragment secondFragment = new KanbanFragment();
    private TimerFragment thirdFragment = new TimerFragment();
    private BottomNavigationView navigation;
    private NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);



        navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.secondFragment);
        loadSelectedFragment(secondFragment);

    }

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();
            switch (item.getItemId()) {
                case R.id.firstFragment:
                    loadSelectedFragment(firstFragment);
                    return true;
                case R.id.secondFragment:
                    loadSelectedFragment(secondFragment);
                    return true;
                case R.id.thirdFragment:
                    loadSelectedFragment(thirdFragment);
                    return true;
            }
            return false;
        }
    };

    public void loadSelectedFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container,fragment);
        transaction.commit();
    }

    public void setNavigationBar(int f){
        if(f==1){
            navigation.setSelectedItemId(R.id.firstFragment);

        }else if(f==2){
            navigation.setSelectedItemId(R.id.secondFragment);
        }else if(f==3){
            navigation.setSelectedItemId(R.id.thirdFragment);
        }
    }

}