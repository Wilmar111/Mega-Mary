package com.example.distribuidora;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import modelo.Producto;

public class detallesproducto extends AppCompatActivity {
    private Button agregarCarrito;
    private ImageView productoImagen;
    TextView productoNOmbre, productoDescripcion,productoPrecio,productoStock;
    private String produtoId="", estado="Normal",CunrrentID;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detallesproducto);
        productoNOmbre = (TextView) findViewById(R.id.nombre_detalle);
        productoDescripcion = (TextView) findViewById(R.id.descripcion_detalle);
        productoPrecio = (TextView) findViewById(R.id.precio_detalle);
        productoStock = (TextView) findViewById(R.id.stock_detalle);
        productoImagen = (ImageView) findViewById(R.id.producto_imgen_detalle);
        agregarCarrito= (Button)findViewById(R.id.boton_sigiente_detalle);
        auth= FirebaseAuth.getInstance();
        CunrrentID= auth.getCurrentUser().getUid();
        agregarCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(estado.equals("Pedido")||estado.equals("Enviado")){
                    Toast.makeText(detallesproducto.this, "Esperando a que el prime pedido finalice...", Toast.LENGTH_SHORT).show();
                }else{
                    agregarAlaLista();
                }
            }
        });

        produtoId = getIntent().getStringExtra("pid");
        Log.d("DetallesProducto", "ID de producto recibido: " + produtoId);  // Agrega este log

        if (produtoId != null) {
            ObtenerDatosProducto(produtoId);
        } else {
            Log.e("DetallesProducto", "ID de producto es nulo");
            Toast.makeText(this, "ID de producto nulo", Toast.LENGTH_SHORT).show();
            finish();
        }

        // ...
    }

    @Override
    protected void onStart() {
        super.onStart();
        VerificarEstadoOrden();
    }

    private void ObtenerDatosProducto(String produtoId) {
        DatabaseReference ProductoRef = FirebaseDatabase.getInstance().getReference().child("Productos");
        ProductoRef.child(produtoId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Producto producto = snapshot.getValue(Producto.class);

                    if (producto != null) {
                        productoNOmbre.setText(" "
                                +producto.getNombre());
                        productoDescripcion.setText(""
                                +producto.getDescripcion());
                        productoPrecio.setText(""
                                +producto.getPrecio());
                        productoStock.setText(" "+producto.getStock());
                        Picasso.get().load(producto.getImagen()).into(productoImagen);

                        // Aquí es seguro llamar a agregarAlaLista, ya que tenemos la información del producto
                        //agregarAlaLista();
                    } else {
                        Toast.makeText(detallesproducto.this, "Producto nulo", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejar errores de base de datos
            }
        });
    }

    private void agregarAlaLista() {
        String CunrrentTime, CunrrentDate;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat date = new SimpleDateFormat("MM-dd-yyyy");
        CunrrentDate = date.format(calendar.getTime());
        SimpleDateFormat time = new SimpleDateFormat("MM:mm:ss");
        CunrrentTime = time.format(calendar.getTime());


            final DatabaseReference CarLisRef = FirebaseDatabase.getInstance().getReference().child("Carrito");

            final HashMap<String, Object> map = new HashMap<>();
            map.put("pid", produtoId);
            map.put("nombre", productoNOmbre.getText().toString());
            map.put("precio", productoPrecio.getText().toString());
            map.put("descripcion", productoDescripcion.getText().toString());
            map.put("stock", productoStock.getText().toString());
            map.put("descuento", "");
                CarLisRef.child("Usuario Compra").child(CunrrentID).child("Productos").child(produtoId).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            CarLisRef.child("Administrador").child(CunrrentDate).child("Productos").child(produtoId).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(detallesproducto.this, "agregando...", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(detallesproducto.this, PrincipalM.class);
                                        startActivity(intent);

                                    }
                                }
                            });
                        }
                    }
                });
                }




    private void VerificarEstadoOrden() {
        DatabaseReference OrdenRef;
        OrdenRef= FirebaseDatabase.getInstance().getReference().child("Ordenes").child(CunrrentID);
        OrdenRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String envioStado=snapshot.child("estado").getValue(String.class);
                    if(envioStado != null) {
                        if(envioStado.equals("Enviado")){
                            estado="Enviado";
                        } else if(envioStado.equals("No Enviado")){
                            estado="Pedido";
                        }
                        // Aquí es seguro llamar a funciones adicionales si es necesario
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Manejar errores de base de datos
            }
        });
    }


}