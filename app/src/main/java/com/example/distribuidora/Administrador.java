package com.example.distribuidora;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Administrador extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrador);
        bottomNavigationView = findViewById(R.id.navegation);
        bottomNavigationView.setOnItemSelectedListener(   listener);
    }

    private final NavigationBarView.OnItemSelectedListener listener = new NavigationBarView.OnItemSelectedListener() {


        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if (item.getItemId() == R.id.Inicio) {
                loadFragment(new Fragmentlicor());

            }
            if (item.getItemId() == R.id.PerfilAd) {
                ActivityRegistroEmpleado();

            }
            if (item.getItemId() == R.id.Orden) {
                loadFragment(new Fragmentharina());

            }
            if (item.getItemId() == R.id.PerfilChofer) {
                ActivityReguistroChofer();

            }
            return true;
        }

        private void ActivityReguistroChofer() {
            Intent intent = new Intent(Administrador.this, ReguistroChofer.class);
            startActivity(intent);
        }
    };

    private void ActivityRegistroEmpleado() {
        Intent intent = new Intent(Administrador.this, RegistroEmpleado.class);
        startActivity(intent);
    }


    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
        }

}



