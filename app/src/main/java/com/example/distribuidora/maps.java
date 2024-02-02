package com.example.distribuidora;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class maps extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {
    //private FusedLocationProviderClient fusedLocationProviderClient;
    EditText txtLatitud, txtLongitud;
    Button enviarubicacion;
    GoogleMap mMap;
    private DatabaseReference mDatabase;

    private Button enviarUbicacion;


    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        txtLatitud = findViewById(R.id.txtLatitud);
        txtLongitud = findViewById(R.id.txtLongitud);
        enviarUbicacion = findViewById(R.id.buttonenviarubicacion);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Ordenes");
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        enviarUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarUbicacionEnFirebase();
            }
        });

        // Verificar y solicitar permisos si es necesario
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        } else {
            // Si los permisos ya están otorgados, inicia la actualización de ubicación
            startLocationUpdates();
        }
    }

    private void startLocationUpdates() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000); // Intervalo de actualización en milisegundos (aquí cada 10 segundos)
        locationRequest.setFastestInterval(5000); // Intervalo más rápido
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Actualizar la ubicación en los EditText
                    txtLatitud.setText(String.valueOf(location.getLatitude()));
                    txtLongitud.setText(String.valueOf(location.getLongitude()));
                    // Centrar el mapa en la nueva ubicación
                    LatLng newLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(newLocation).title("Ubicación Actual"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLocation, 15));
                }
            }
        };

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }

        private void guardarUbicacionEnFirebase() {
            String latitudStr = txtLatitud.getText().toString().trim();
            String longitudStr = txtLongitud.getText().toString().trim();

            if (latitudStr.isEmpty() || longitudStr.isEmpty()) {
                // Manejar el caso de cadenas vacías
                Toast.makeText(maps.this, "Por favor, ingrese valores de latitud y longitud", Toast.LENGTH_SHORT).show();
                return;
            }

            double latitud = Double.parseDouble(latitudStr);
            double longitud = Double.parseDouble(longitudStr);

            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            String key = mDatabase.child(userId).push().getKey();

            Map<String, Object> ubicacionMap = new HashMap<>();
            ubicacionMap.put("latitud", latitud);
            ubicacionMap.put("longitud", longitud);

            mDatabase.child(userId).child(key).setValue(ubicacionMap)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(maps.this, "Ubicación guardada en Firebase", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(maps.this, "Error al guardar la ubicación", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            mMap = googleMap;
            this.mMap.setOnMapClickListener(this);
            this.mMap.setOnMapLongClickListener(this);
            LatLng cayambe = new LatLng(0.0274996, -78.1563443);
            mMap.addMarker(new MarkerOptions().position(cayambe).title("Cayambe"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cayambe, 10));
        }

        @Override
        public void onMapClick(@NonNull LatLng latLng) {
            txtLatitud.setText(String.valueOf(latLng.latitude));
            txtLongitud.setText(String.valueOf(latLng.longitude));
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(latLng).title(""));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }

        @Override
        public void onMapLongClick(@NonNull LatLng latLng) {
            txtLatitud.setText(String.valueOf(latLng.latitude));
            txtLongitud.setText(String.valueOf(latLng.longitude));
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(latLng).title(""));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }

        @Override
        protected void onPause() {
            super.onPause();
            stopLocationUpdates();
        }

        private void stopLocationUpdates() {
            if (locationCallback != null) {
                fusedLocationClient.removeLocationUpdates(locationCallback);
            }
        }

    @Override
    public void onMapLoaded() {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}