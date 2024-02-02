package com.example.distribuidora;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import Clases.Ordenes;
import modelo.Producto;


public class Fragmentharina extends Fragment {

private View vista;
private RecyclerView recycler;
DatabaseReference OrdenRef;


    public Fragmentharina() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            vista=inflater.inflate(R.layout.fragment_fragmentagregarpro, container, false);
            OrdenRef= FirebaseDatabase.getInstance().getReference().child("Ordenes");
            recycler=(RecyclerView) vista.findViewById(R.id.recycler_ordenes);
            recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        return vista;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions options= new FirebaseRecyclerOptions.Builder<Ordenes>().setQuery(OrdenRef, Ordenes.class).build();
        FirebaseRecyclerAdapter<Ordenes, OrdenesViewHolder>adapter= new FirebaseRecyclerAdapter<Ordenes, OrdenesViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrdenesViewHolder holder, int position, @NonNull Ordenes model) {
             holder.nombreOrden.setText("Nombre: "+model.getNombre());
             holder.telefonoOrden.setText("Telefono: "+model.getTelefono());
             holder.precioOrden.setText("Total de pagar: "+ model.getTotal());
             holder.correoOrden.setText("Correo: "+ model.getCorreo()+ "Direccion: "+model.getDireccion());
             holder.fechaOrden.setText("Fecha:"+model.getFecha() + "Hora:"+model.getHora());
                holder.modoPago.setText("Forma de Pago: " + model.getModoPago());
             holder.botontonver.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     List<Producto> productos = model.getProducto();

                     // Verifica si la lista de productos está vacía o nula
                     if (productos != null && !productos.isEmpty()) {
                         // Construye el mensaje para la alerta
                         StringBuilder mensaje = new StringBuilder("Productos en la orden:\n");
                         for (Producto producto : productos) {
                             mensaje.append(producto.getNombre()).append(": ").append(producto.getCantidad()).append("\n");
                         }

                         // Muestra la alerta con el nombre del producto y la cantidad
                         mostrarAlerta("Productos en la Orden", mensaje.toString());
                     } else {
                         // Manejo cuando la lista de productos está vacía o es nula
                         mostrarAlerta("Advertencia", "La lista de productos está vacía o es nula");
                     }
                 }
             });
            }

            @NonNull
            @Override
            public OrdenesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.ordenes, parent, false);
                OrdenesViewHolder viewHolder= new OrdenesViewHolder(view);


                return viewHolder;
            }
        };
        recycler.setAdapter(adapter);
        adapter.startListening();
    }

    private void mostrarAlerta(String productosEnLaOrden, String toString) {
        String titulo = "los productos son:";
        String mensaje = "listo";

// Luego, puedes usarlos en un AlertDialog.Builder

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());// Reemplaza 'this' con tu contexto adecuado
        builder.setTitle(titulo)
                .setMessage(mensaje)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Código para manejar el botón "Aceptar"
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Código para manejar el botón "Cancelar"
                    }
                });

// Mostrar el diálogo
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public  static  class OrdenesViewHolder extends RecyclerView.ViewHolder{
        TextView modoPago;
        TextView nombreOrden, telefonoOrden, precioOrden, correoOrden, fechaOrden;
        Button botontonver;
        public OrdenesViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreOrden=itemView.findViewById(R.id.nombreorden);
            telefonoOrden=itemView.findViewById(R.id.telefonoeorden);
            precioOrden=itemView.findViewById(R.id.precioorden);
            correoOrden=itemView.findViewById(R.id.coreoorden);
            fechaOrden=itemView.findViewById(R.id.fechaorden);
            botontonver=itemView.findViewById(R.id.verorden);
            modoPago = itemView.findViewById(R.id.formaPagoOrden);

        }
    }
}