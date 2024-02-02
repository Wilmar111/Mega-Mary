package com.example.distribuidora;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class confiOrden extends AppCompatActivity {
    private GoogleMap googleMap;
    private FusedLocationProviderClient ubicacion;
    private EditText nombre, correo, direccion, telefono;
    private Button confirmar, generarasUbicacion;
    private String totalPago = "";
    private FirebaseAuth auth;
    private String CurrentUserId;
    private Button btnPagarEfectivo, btnPagarTransferencia;
    private String modoPago;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confi_orden);

        totalPago = getIntent().getStringExtra("Total");
        Toast.makeText(this, "Total a pagar: $ " + totalPago, Toast.LENGTH_SHORT).show();

        auth = FirebaseAuth.getInstance();
        CurrentUserId = auth.getCurrentUser().getUid();

        nombre = findViewById(R.id.edtNombrecon);
        correo = findViewById(R.id.edtCorreocon);
        telefono = findViewById(R.id.edttelefonocon);
        direccion = findViewById(R.id.edtEntrega);
        confirmar = findViewById(R.id.btnterminarentrega);
        generarasUbicacion = findViewById(R.id.generarubicacio);
        btnPagarEfectivo = findViewById(R.id.btnEfectivo);
        btnPagarTransferencia = findViewById(R.id.btnTransferencia);

        btnPagarEfectivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modoPago = "Efectivo";
                ConfirmarOrden();
                Toast.makeText(confiOrden.this, "Has elegido pagar en efectivo", Toast.LENGTH_SHORT).show();
            }
        });

        btnPagarTransferencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modoPago = "Transferencia";
                ConfirmarOrden();
                Toast.makeText(confiOrden.this, "Has elegido pagar por transferencia", Toast.LENGTH_SHORT).show();
            }
        });

        generarasUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(confiOrden.this, maps.class);
                startActivity(intent);
            }
        });

        confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerificarDatos();
            }
        });
    }

    private void VerificarDatos() {
        if (TextUtils.isEmpty(nombre.getText().toString()) ||
                TextUtils.isEmpty(correo.getText().toString()) ||
                TextUtils.isEmpty(direccion.getText().toString()) ||
                TextUtils.isEmpty(telefono.getText().toString())) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
        } else {
            ConfirmarOrden();
        }
    }

    private void ConfirmarOrden() {
        String currentTime, currentDate;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        currentDate = dateFormat.format(calendar.getTime());

        SimpleDateFormat dateFormat1 = new SimpleDateFormat("HH:mm:ss");
        currentTime = dateFormat1.format((calendar.getTime()));

        DatabaseReference OrdenesRef = FirebaseDatabase.getInstance().getReference().child("Ordenes").child(CurrentUserId);

        HashMap<String, Object> map = new HashMap<>();
        map.put("total", totalPago);
        map.put("nombre", nombre.getText().toString());
        map.put("correo", correo.getText().toString());
        map.put("direccion", direccion.getText().toString());
        map.put("telefono", telefono.getText().toString());
        map.put("fecha", currentDate);
        map.put("hora", currentTime);
        map.put("modoPago", modoPago);
        map.put("estado", "No Enviado");

        OrdenesRef.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    FirebaseDatabase.getInstance().getReference().child("Carrito")
                            .child("Usuario Compra").child(CurrentUserId).removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(confiOrden.this, "Tu orden fue tomada con éxito", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(confiOrden.this, PrincipalM.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                }
            }
        });
    }
}

