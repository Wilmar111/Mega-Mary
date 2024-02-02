package com.example.distribuidora;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class agregarproducto extends AppCompatActivity {
 private static int Gallery_Pick=1;
    private Uri imagenUri;
    private String productoRandomKey, dowloadUri, CurrentDate, CurrentTime;
    private StorageReference ProductoImagenRef;
    private DatabaseReference ProductoRef;
    private ProgressDialog dialog;
    private String Categoria, codigo, nombrepro, descripcionpro, stockpro, preciopro, Cantidad;
    private ImageView Imagenes;
    private EditText Codigo, NombreP, Descripcion, Stock, Precio, cantidad;
    private Button AgregarProducto;


   


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Categoria= getIntent().getExtras().get("categoria").toString();
        ProductoImagenRef = FirebaseStorage.getInstance().getReference().child("Imagenes de Productos");
        ProductoRef= FirebaseDatabase.getInstance().getReference().child("Productos");

        Toast.makeText(this, Categoria, Toast.LENGTH_SHORT).show();

        setContentView(R.layout.activity_agregarproducto);
        Imagenes = (ImageView) findViewById(R.id.imglist);
        Codigo = (EditText) findViewById(R.id.edtCodigoPro);
        NombreP = (EditText) findViewById(R.id.edtNombrePro);
        Descripcion = (EditText) findViewById(R.id.edtDescripcionPro);
        Stock = (EditText) findViewById(R.id.edtStock);
        Precio = (EditText) findViewById(R.id.edtPrecio);
        cantidad= (EditText)findViewById(R.id.edtCantida);
        AgregarProducto= (Button) findViewById(R.id.btnguardarp);


        dialog= new ProgressDialog(this);

        Imagenes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AbrirGaleria();
            }

            
        });
        AgregarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidarProductos();
                GuardarEnFireBase();
            }
        });
    }

    private void ValidarProductos() {
        codigo = Codigo.getText().toString();
        nombrepro = NombreP.getText().toString();
        descripcionpro = Descripcion.getText().toString();
        stockpro = Stock.getText().toString();
        preciopro = Precio.getText().toString();

        if (imagenUri == null) {
            Toast.makeText(this, "Primero Agrega una Imgen", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(codigo)) {
            Toast.makeText(this, "Ingrese primero el codigo", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(nombrepro)) {
            Toast.makeText(this, "Ingrese el nombre del producto", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(descripcionpro)) {
            Toast.makeText(this, "Ingrese la descriocion", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(stockpro)) {
            Toast.makeText(this, "Ingrese el stock", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(preciopro)) {
            Toast.makeText(this, "Ingrese el precio", Toast.LENGTH_SHORT).show();

        }
        else {
            GuardarInformacionProducto();
        }
    }

    private void GuardarInformacionProducto() {
        dialog.setTitle("Guardado Producto");
        dialog.setMessage("Por favor Espere ");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDateFormat = new SimpleDateFormat("MM-dd-yyyy");
        CurrentDate = currentDateFormat.format(calendar.getTime());

        SimpleDateFormat currentTimeFormat = new SimpleDateFormat("HH:mm:ss");
        CurrentTime = currentTimeFormat.format(calendar.getTime());

        productoRandomKey = CurrentDate + CurrentTime;

        final StorageReference filePath = ProductoImagenRef.child(productoRandomKey + ".jpg");
        final UploadTask uploadTask = filePath.putFile(imagenUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String mensaje = e.toString();
                Toast.makeText(agregarproducto.this, "Error: " + mensaje, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(agregarproducto.this, "Imagen guardada exitosamente", Toast.LENGTH_SHORT).show();
                filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        dowloadUri = uri.toString();
                        Toast.makeText(agregarproducto.this, "Imagen Guardada en FireBase", Toast.LENGTH_SHORT).show();
                        GuardarEnFireBase();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(agregarproducto.this, "Error al obtener la URL de descarga", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void GuardarEnFireBase() {
        HashMap<String,Object> map = new HashMap<>();
        map.put("pid", productoRandomKey);
        map.put("fecha",CurrentDate);
        map.put("hora", CurrentTime);
        map.put("codigo", codigo);
        map.put("nombre", nombrepro);
        map.put("descripcion", descripcionpro);
        map.put("stock", stockpro);
        map.put("precio", preciopro);
        map.put("imagen", dowloadUri);
        map.put("categoria", Categoria);

        ProductoRef.child(productoRandomKey).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Intent intent = new Intent(agregarproducto.this, Administrador.class);
                    startActivity(intent);
                    dialog.dismiss();
                    Toast.makeText(agregarproducto.this, "Solicitud exitosa", Toast.LENGTH_SHORT).show();
                }else{
                    dialog.dismiss();
                    String mensaje = task.getException().toString();
                    Toast.makeText(agregarproducto.this, "ERROR:"+ mensaje, Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void AbrirGaleria() {
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/");
        startActivityForResult(intent,Gallery_Pick);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==Gallery_Pick && resultCode==RESULT_OK && data != null);
        imagenUri=data.getData();
        Imagenes.setImageURI(imagenUri);
    }
}