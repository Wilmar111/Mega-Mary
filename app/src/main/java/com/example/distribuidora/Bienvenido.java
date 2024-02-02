package com.example.distribuidora;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Bienvenido extends AppCompatActivity {
    private Button btnUsuario, btnAdministrador;
    private Spinner sp45;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenido);
        btnUsuario = findViewById(R.id.btnUsuario);
        btnAdministrador = findViewById(R.id.btnAdministrador);

        btnAdministrador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser user = auth.getCurrentUser();

                if (user != null) {
                    // El usuario está autenticado, ahora verifica si es un administrador
                    // Puedes hacer esto comprobando si el usuario tiene un rol específico o un campo personalizado en la base de datos
                    // Aquí asumiré que tienes un campo "esAdmin" en la base de datos que indica si el usuario es un administrador o no
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("usuarios/" + user.getUid() + "/esAdmin");

                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists() && dataSnapshot.getValue(Boolean.class)) {
                                // El usuario es un administrador
                                Intent intent = new Intent(Bienvenido.this, Administrador.class);
                                startActivity(intent);
                            } else {
                                // El usuario no es un administrador, redirige a LoginAdmin
                                Intent intent = new Intent(Bienvenido.this, LoginAdmin.class);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Manejo de errores si es necesario
                            Toast.makeText(Bienvenido.this, "Error al verificar el estado de administrador.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // El usuario no está autenticado, redirige a LoginAdmin
                    Intent intent = new Intent(Bienvenido.this, LoginAdmin.class);
                    startActivity(intent);
                }

            }
        });






        btnUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Verificar si el usuario ha iniciado sesión
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    // El usuario ha iniciado sesión, dirigirlo a la pantalla principal
                    Intent intent = new Intent(Bienvenido.this, PrincipalM.class);
                    startActivity(intent);
                } else {
                    // El usuario no ha iniciado sesión, dirigirlo a la pantalla de registro
                    Intent intent = new Intent(Bienvenido.this, Reguistrar.class);
                    startActivity(intent);
                }
            }
        });


    }
}