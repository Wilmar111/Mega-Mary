package com.example.distribuidora;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

import modelo.chofer;

public class ReguistroChofer extends AppCompatActivity {
    private EditText edtCedulaCh, edtNombreCh,edtApellidoCh,edtCorreoCho;
    private EditText edtTelefono1;
    private RadioButton Hombre, Mujer;


    private Button btnActulizarCho, btnRegistrarCh, btnBuscarCh;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    FirebaseAuth auth;

    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reguistro_chofer);

            auth = FirebaseAuth.getInstance();
            edtCedulaCh = (EditText) findViewById(R.id.edtceduch);
            edtNombreCh = (EditText) findViewById(R.id.edtnombrech);
            edtApellidoCh= (EditText) findViewById(R.id.edtapellidoch);
            edtTelefono1= (EditText) findViewById(R.id.edtTelefono1);
            Hombre = (RadioButton) findViewById(R.id.rdbHombreCh);
            Mujer= (RadioButton) findViewById(R.id.rbdMujerCh);
            edtCorreoCho= (EditText) findViewById(R.id.edtcorreoch);

            btnRegistrarCh= (Button) findViewById(R.id.btnRegistrarCh);





           btnRegistrarCh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    inicializarFirebase();


                    listaDatos();
                    limpiarCajas();
                }
            });





        }



        private void limpiarCajas() {
            edtCedulaCh.setText("");
            edtNombreCh.setText("");
            edtApellidoCh.setText("");
            edtTelefono1.setText("");
            edtCorreoCho.setText("");

        }

    private void listaDatos() {
        String cedula = edtCedulaCh.getText().toString();
        String nombres = edtNombreCh.getText().toString();
        String apellido = edtApellidoCh.getText().toString();
        String genero = obtenerGenero();  // Método para obtener el valor del género desde los radio buttons
        String telefono = edtTelefono1.getText().toString();  // Cambié edtTeleCh a un String porque no está definido en tu código
        String correo = edtCorreoCho.getText().toString();

        // Validación
        if (cedula.equals("") || nombres.equals("") || apellido.equals("") || genero.equals("") || correo.equals("")) {
            validacion();
            return; // Detener la ejecución si hay campos vacíos
        }

        chofer C = new chofer();
        C.setUid(UUID.randomUUID().toString());
        C.setCedulaCh(cedula);
        C.setNombreCo(nombres);
        C.setApellidoCh(apellido);
        C.setGenero(genero);
        C.setTelefonoCho(telefono);
        C.setCorreoCh(correo);

        // Guardar en Firebase
        databaseReference.child("Chofer").child(C.getUid()).setValue(C);
        Toast.makeText(this, "Agregado", Toast.LENGTH_SHORT).show();
    }

        // Método para obtener el valor del género desde los radio buttons
        private String obtenerGenero() {
            if (Hombre.isChecked()) {
                return "Hombre";
            } else if (Mujer.isChecked()) {
                return "Mujer";
            } else {
                // Manejar el caso en el que no se selecciona ningún género
                return "";
            }
        }


        private void validacion() {
            String cedula = edtCedulaCh.getText().toString();
            String nombres = edtNombreCh.getText().toString();
            String apellido = edtApellidoCh.getText().toString();
            String genero = obtenerGenero();// Método para obtener el valor del género desde los radio buttons
            String telefono = edtTelefono1.getText().toString();
            String correo = edtCorreoCho.getText().toString();
            if (nombres.equals("")) {
                edtNombreCh.setError("Requiered");
            } else nombres.equals("");

            if (apellido.equals("")) {
                edtApellidoCh.setError("Requiered");
            } else apellido.equals("");


        }

        private void inicializarFirebase() {
            FirebaseApp.initializeApp(this);
            firebaseDatabase=FirebaseDatabase.getInstance();
            databaseReference= firebaseDatabase.getReference();
        }

    }
