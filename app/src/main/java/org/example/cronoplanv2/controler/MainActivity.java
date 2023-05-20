package org.example.cronoplanv2.controler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;

import org.example.cronoplanv2.R;
import org.example.cronoplanv2.model.ItemsDAO.SettingsDAO;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
/**
 * Clase MainActivity que actúa como controlador principal de la aplicación.
 * Administra la navegación entre fragmentos y configura la barra de navegación inferior.
 */
public class MainActivity extends AppCompatActivity {
    private final SettingsDAO SETTINGS = new SettingsDAO();
    private ChartFragment firstFragment = new ChartFragment();
    private KanbanFragment secondFragment = new KanbanFragment();
    private TimerFragment thirdFragment = new TimerFragment();
    private BottomNavigationView navigation;
    private NavigationView navigationView;
    /**
     * Método llamado al crear la actividad principal.
     * Configura la interfaz de usuario, solicita permisos y carga el fragmento inicial.
     */
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
    /**
     * Carga el fragmento seleccionado en el contenedor de fragmentos.
     * @param fragment El fragmento que se va a cargar.
     */
    public void loadSelectedFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container,fragment);
        transaction.commit();
    }
    /**
     * Carga el fragmento seleccionado en el contenedor de fragmentos.
     * @param f El fragmento que se va a cargar.
     */
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