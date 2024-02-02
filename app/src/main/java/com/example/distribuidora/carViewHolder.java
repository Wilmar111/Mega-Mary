package com.example.distribuidora;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class carViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView carProductoNombre, carProductoStock, carProductoPrecio;

    private ItemClickListener itemClickListener;


    public carViewHolder(@NonNull View itemView) {
        super(itemView);

        carProductoNombre=itemView.findViewById(R.id.carNomPro);
        carProductoStock=itemView.findViewById(R.id.caRStockPro);
        carProductoPrecio=itemView.findViewById(R.id.carPrecioPro);


    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);


    }
    public void setItemClickListener (ItemClickListener itemClickListener){
        this.itemClickListener=itemClickListener;
    }
}
