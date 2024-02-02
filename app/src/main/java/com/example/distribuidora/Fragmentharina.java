package com.example.distribuidora;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Clases.Ordenes;


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
             holder.botontonver.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     Toast.makeText(getContext(), "Correcto•", Toast.LENGTH_SHORT).show();

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
    public  static  class OrdenesViewHolder extends RecyclerView.ViewHolder{
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

        }
    }
}