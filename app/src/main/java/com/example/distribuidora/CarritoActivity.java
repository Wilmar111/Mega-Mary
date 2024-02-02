package com.example.distribuidora;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import modelo.Carrito;

public class CarritoActivity extends AppCompatActivity  {
private RecyclerView recyclerView;
private Button btnConfirmar;
private RecyclerView.LayoutManager layoutManager;
private TextView txtTotal,mensaje1;
private double PrecioTotalId = 00;
    private double totalAPagar = 0.0;
private String CurrengUserId;
private FirebaseAuth auth;




    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);


        recyclerView = (RecyclerView) findViewById(R.id.carritoLista);
        recyclerView.setHasFixedSize(true);
        layoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mensaje1=(TextView) findViewById(R.id.mensaje1);
        btnConfirmar=(Button) findViewById(R.id.btnconfir);
        txtTotal=(TextView) findViewById(R.id.txtTotalCarrito);
        auth=FirebaseAuth.getInstance();
        CurrengUserId= auth.getCurrentUser().getUid();
        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CarritoActivity.this, confiOrden.class);
                i.putExtra("Total", String.valueOf(totalAPagar));
                startActivity(i);
                finish();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        VerificarEstadOrigen();






        final DatabaseReference CarLisRef = FirebaseDatabase.getInstance().getReference().child("Carrito");

       FirebaseRecyclerOptions<Carrito> options= new FirebaseRecyclerOptions.Builder<Carrito>()
               .setQuery(CarLisRef.child("Usuario Compra").child(CurrengUserId).child("Productos"), Carrito.class).build();
        FirebaseRecyclerAdapter<Carrito, carViewHolder> adapter= new FirebaseRecyclerAdapter<Carrito, carViewHolder>(options) {


            @Override
            protected void onBindViewHolder(@NonNull carViewHolder holder, int position, @NonNull Carrito model) {
                holder.carProductoNombre.setText("Nombre"+model.getNombre());
                holder.carProductoStock.setText("Stock"+model.getStock());
                holder.carProductoPrecio.setText("Precio"+model.getPrecio());

                try {
                    double precio = Double.parseDouble(model.getPrecio());
                    int cantidad = Integer.parseInt(model.getStock().trim());  // Elimina espacios en blanco antes de convertir

                    double subtotal = precio * cantidad;

                    // Muestra el subtotal y el total en el Logcat
                    Log.d("CarritoActivity", "Subtotal: " + subtotal);
                    Log.d("CarritoActivity", "Total a Pagar: " + totalAPagar);

                    // Suma el subtotal al total a pagar
                    totalAPagar += subtotal;

                    // Muestra el total en el TextView correspondiente
                    txtTotal.setText("Total a Pagar: " + totalAPagar);
                } catch (NumberFormatException e) {
                    Log.e("CarritoActivity", "Error al convertir a número: " + e.getMessage());
                }




                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        CharSequence opcions[] = new CharSequence[]{
                                "Editar",
                                "Eliminar"
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(CarritoActivity.this);
                        builder.setTitle("Opciones del producto");

                        builder.setItems(opcions, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                if(i==0) {
                                    Intent intent = new Intent(CarritoActivity.this, detallesproducto.class );
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);
                                }
                                if(i==1){
                                    CarLisRef.child("Usuario Compra")
                                            .child(CurrengUserId)
                                            .child("Productos")
                                            .child(model.getPid()).removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(CarritoActivity.this, "Producto Eliminado", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(CarritoActivity.this, PrincipalM.class);
                                                        startActivity(intent);
                                                    }
                                                }
                                            });
                                }
                            }
                        });
                        builder.show();
                    }
                });
            }



            @NonNull
            @Override
            public carViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.car_item, parent,false);
                carViewHolder holder = new carViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        //txtTotal.setText("Total: " + String.valueOf(PrecioTotalId));
    }

    private void VerificarEstadOrigen() {
        DatabaseReference ordenref;
        ordenref=FirebaseDatabase.getInstance().getReference().child("Ordenes").child(CurrengUserId);

        ordenref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String estado = null;
                    if (snapshot.child("estado").getValue() != null) {
                        estado = snapshot.child("estado").getValue().toString();
                    }

                    if (estado != null) {
                        if (estado.equals("Enviado")) {
                            // Código cuando el estado es "Enviado"
                        } else if (estado.equals("No Enviado")) {
                            // Código cuando el estado es "No Enviado"
                        } else {
                            // Código para otros casos
                        }
                    } else {
                        // Manejar el caso cuando el estado es nulo
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}




