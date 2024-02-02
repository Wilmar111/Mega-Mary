package com.example.distribuidora;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ReguistroCliente extends AppCompatActivity {

    private EditText edtCedula, edtNombre,edtApellido,edtNombreNogocio, edtTelefono, edtDireccion;

    private String userE="";
    private  String CurrentUserId;



    private Button btnReguistrar2;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference UserRef;

    FirebaseAuth auth;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_reguistro_cliente);

        auth = FirebaseAuth.getInstance();
        CurrentUserId= auth.getCurrentUser().getUid();
        UserRef= FirebaseDatabase.getInstance().getReference().child("Usuarios");
        progressDialog= new ProgressDialog(this);


        edtCedula = (EditText) findViewById(R.id.edtCed);
        edtNombre = (EditText) findViewById(R.id.edtNombre);
        edtApellido = (EditText) findViewById(R.id.edtApellido);
        edtNombreNogocio = (EditText) findViewById(R.id.edtNombreNegocio);
        edtTelefono = (EditText) findViewById(R.id.edtele);
        edtDireccion = (EditText) findViewById(R.id.edtDireccion);


        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            userE=bundle.getString("userE");

        }

        btnReguistrar2 = (Button) findViewById(R.id.btnRegistrar);



        btnReguistrar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                agregarDatos();

                inicializarFirebase();


            }
        });

    }

    private void agregarDatos() {
        String cedula=edtCedula.getText().toString().toUpperCase();
        String nombre=edtNombre.getText().toString();
        String apellido=edtApellido.getText().toString();
        String nombrenegocio =edtNombreNogocio.getText().toString();
        String telefono =edtTelefono.getText().toString();
        String direccion =edtDireccion.getText().toString();

       if (TextUtils.isEmpty(cedula)) {
            Toast.makeText(this, "Ingrese primero la cedula", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(nombre)) {
            Toast.makeText(this, "Ingrese los nombres", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(apellido)) {
            Toast.makeText(this, "Ingrese los apellidos", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(nombrenegocio)) {
            Toast.makeText(this, "Ingrese el nombre del negocio", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(telefono)) {
            Toast.makeText(this, "Ingrese el telefono", Toast.LENGTH_SHORT).show();
        }if (TextUtils.isEmpty(direccion)) {
            Toast.makeText(this, "Ingrese direccion", Toast.LENGTH_SHORT).show();
        }
        else {
            progressDialog.setTitle("Guardao");
            progressDialog.setMessage("Pro Favor espere ☻...");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(true);

            HashMap map = new HashMap();
            map.put("cedula", cedula);
            map.put("nombre", nombre);
            map.put("apellido", apellido);
            map.put("nombrenegocio", nombrenegocio);
            map.put("telefono", telefono);
            map.put("direcion",direccion);
            map.put("uid",CurrentUserId);

            UserRef.child(CurrentUserId).updateChildren(map).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                  if(task.isSuccessful()){
                      EnviarAlInicio();
                      progressDialog.dismiss();


                  }else{
                     String mensaje= task.getException().toString();
                      Toast.makeText(ReguistroCliente.this, "Error:"+mensaje, Toast.LENGTH_SHORT).show();
                      progressDialog.dismiss();
                  }
                }
            });

        }
        }

    private void EnviarAlInicio() {
        Intent intent= new Intent(ReguistroCliente.this, PrincipalM.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }


    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase=FirebaseDatabase.getInstance();
        UserRef= firebaseDatabase.getReference();


    }

   
}
