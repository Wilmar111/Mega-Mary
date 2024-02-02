package com.example.distribuidora;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class LoginAdmin extends AppCompatActivity {
    private GoogleApiClient apiClient;
    private static final int RC_SIGN_IN = 1001;

    private ProgressDialog progressDialog;
    private static final String TAG = "MainActivity";

    private SignInButton btnSignIn;
    private Button btnSignOut;
    private Button btnRevoke;
    private EditText etUserAdmin;
    private EditText etPasswordAdmin;
    private Button btnRegistrar;
    private Button btnIngresar;
    FirebaseAuth auth;

    private DatabaseReference databaseReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin);

        auth = FirebaseAuth.getInstance();

        etUserAdmin = findViewById(R.id.edtUsuarioadmin);
        etPasswordAdmin = findViewById(R.id.edtPasswordadmin);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        progressDialog = new ProgressDialog(this);
        btnIngresar = findViewById(R.id.btnIngresar);

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ingresarConCorreoContraseña();
            }
        });
    }

    private void ingresarConCorreoContraseña() {
        String correo = etUserAdmin.getText().toString().trim();
        String contraseña =etPasswordAdmin.getText().toString().trim();

        if (TextUtils.isEmpty(correo) || TextUtils.isEmpty(contraseña)) {
            Toast.makeText(LoginAdmin.this, "Ingrese correo y contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.signInWithEmailAndPassword(correo, contraseña)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Inicio de sesión exitoso
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            // Verificar si el usuario es un administrador (puedes utilizar lógica similar a la respuesta anterior)
                            // En este ejemplo, asumimos que todos los usuarios registrados son administradores
                            Intent intent = new Intent(LoginAdmin.this, Administrador.class);
                            startActivity(intent);
                            finish(); // Cerramos esta actividad para que no se pueda volver a ella desde el botón Atrás
                        } else {
                            Toast.makeText(LoginAdmin.this, "Error al obtener información del usuario", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Si el inicio de sesión falla, mostrar un mensaje al usuario.
                        Toast.makeText(LoginAdmin.this, "Error al iniciar sesión: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });



      btnRegistrar.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              registrarAdministrador();
          }
      });
    }

    private void registrarAdministrador() {
            String correo = etUserAdmin.getText().toString().trim();
            String contraseña = etPasswordAdmin.getText().toString().trim();

            if (TextUtils.isEmpty(correo) || TextUtils.isEmpty(contraseña)) {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            progressDialog.setMessage("Registrando administrador...");
            progressDialog.show();

            // Crear usuario en Firebase Authentication
            auth.createUserWithEmailAndPassword(correo, contraseña)
                    .addOnCompleteListener(this, task -> {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            // Registro exitoso
                            Toast.makeText(LoginAdmin.this, "Administrador registrado exitosamente", Toast.LENGTH_SHORT).show();
                            // Puedes redirigir a otra actividad aquí
                            Intent intent = new Intent(LoginAdmin.this, Administrador.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Si el registro falla, mostrar un mensaje al usuario.
                            Toast.makeText(LoginAdmin.this, "Error al registrar administrador: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }

    }


