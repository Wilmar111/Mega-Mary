package com.example.distribuidora;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

import modelo.Empleado;

public class RegistroEmpleado extends AppCompatActivity {

    EditText edtPassword33Emp;
    private EditText edtCedulaEmp, edtNombreEmp,edtApellidoEmp, edtTelefonoEmp, edtDireccionEmp, edtCorreoEmp;
    private Button btnReguistrarEmp, btnBuscarEmp;
private Spinner Sp1;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_empleado);


        Sp1 = findViewById(R.id.spinner);


        // Datos de ejemplo para el Spinner
        String[] opcionesSpinner = {"Administrador"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, opcionesSpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Sp1.setAdapter(adapter);

        // Manejar la selección del Spinner
        Sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Obtener el elemento seleccionado del Spinner
                String selectedItem = parentView.getItemAtPosition(position).toString();
                // Mostrar la información en el TextView

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Acciones a realizar cuando no hay elementos seleccionados
            }
        });




        //datime base
        edtCedulaEmp = (EditText) findViewById(R.id.edtCedulaEmp);
        edtNombreEmp = (EditText) findViewById(R.id.edtNombreEmp);
        edtApellidoEmp = (EditText) findViewById(R.id.edtApellidoEmp);

        edtTelefonoEmp = (EditText) findViewById(R.id.edteleEmp);
        edtDireccionEmp = (EditText) findViewById(R.id.edtDireccionEmp);
        edtCorreoEmp = (EditText) findViewById(R.id.edtCorrEmp);


        btnReguistrarEmp = (Button) findViewById(R.id.btnRegistrarEmp);

        //ListV_Persona = this.<ListView>findViewById(R.id.ListaV_Cliente);


        btnReguistrarEmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                inicializarFirebase();


                listaDatos();
                limpiarCajas();

            }
        });




    }



    private void limpiarCajas() {
        edtCedulaEmp.setText("");
        edtNombreEmp.setText("");
        edtApellidoEmp.setText("");
        edtTelefonoEmp.setText("");
        edtDireccionEmp.setText("");
        edtCorreoEmp.setText("");


    }

    private void listaDatos() {
        String cedula = edtCedulaEmp.getText().toString();
        String nombres = edtNombreEmp.getText().toString();
        String apellido = edtApellidoEmp.getText().toString();
        String pais = Sp1.getSelectedItem().toString();  // Obtener el valor seleccionado del spinner
        String telefono = edtTelefonoEmp.getText().toString();
        String direccion = edtDireccionEmp.getText().toString();
        String correo = edtCorreoEmp.getText().toString();

        String cargo = Sp1.getSelectedItem().toString();  // Obtener el valor seleccionado del spinner

        // Validacion
        if (cedula.equals("") || nombres.equals("") || apellido.equals("") || cargo.equals("") ||
                telefono.equals("") || direccion.equals("") || correo.equals("")) {
            validacion();
        }

        Empleado e = new Empleado();
        e.setUid(UUID.randomUUID().toString());
        e.setCedula(cedula);
        e.setNombres(nombres);
        e.setApellidos(apellido);
        e.setTelefono(telefono);
        e.setDireccion(direccion);
        e.setCorreo(correo);
        e.setCargo(cargo);

        databaseReference.child("Empleados").child(e.getUid()).setValue(e);
        Toast.makeText(this, "Agregado", Toast.LENGTH_SHORT).show();
    }

    private void validacion() {
        String nombres = edtNombreEmp.getText().toString();
        String apellido = edtApellidoEmp.getText().toString();

        if (nombres.equals("")) {
            edtNombreEmp.setError("Requerido");
        }

        if (apellido.equals("")) {
            edtApellidoEmp.setError("Requerido");
        }
        // Agrega más validaciones según sea necesario
    }


    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase= FirebaseDatabase.getInstance();
        databaseReference= firebaseDatabase.getReference();
    }

}

