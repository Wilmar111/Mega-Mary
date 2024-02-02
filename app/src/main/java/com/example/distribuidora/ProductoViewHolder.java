package com.example.distribuidora;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import modelo.Producto;

public class ProductoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
public TextView productoNOm, productoDescrip, productoPrecioUni,productoStock, cantidad;
public ImageView productoImg;
public Button agrgarAlCarrito;

public ItemClickListener listener;


    public ProductoViewHolder(@NonNull View itemView) {
        super(itemView);

        productoNOm=(TextView) itemView.findViewById(R.id.productoNombre);
        productoDescrip=(TextView) itemView.findViewById(R.id.descripcionproducto);
        productoPrecioUni=(TextView) itemView.findViewById(R.id.precioproductounitario);
        productoStock=(TextView) itemView.findViewById(R.id.stockproducto);

        productoImg=(ImageView) itemView.findViewById(R.id.imgenproducto);


    }

    public class AdaptadorCarrito extends RecyclerView.Adapter<ProductoViewHolder> {
        private List<Producto> productosEnCarrito;

        public AdaptadorCarrito(List<Producto> productosEnCarrito) {
            this.productosEnCarrito = productosEnCarrito;
        }


        @NonNull
        @Override
        public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_item, parent, false);
            return new ProductoViewHolder(view);

        }

        @Override
        public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
            Producto producto = productosEnCarrito.get(position);

            holder.productoNOm.setText(producto.getNombre());
            holder.productoPrecioUni.setText("Precio: " + producto.getPrecio());
            holder.cantidad.setText("Cantidad: " + producto.getCantidad());

        }

        @Override
        public int getItemCount() {
            return productosEnCarrito.size();

        }
    }
    public void setItemClickListener (ItemClickListener listener ){
        this.listener=listener;

    }

    @Override
    public void onClick(View v) {
     listener.onClick(v, getAdapterPosition(),false);
    }
}
