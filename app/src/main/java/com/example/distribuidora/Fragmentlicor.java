package com.example.distribuidora;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;


public class Fragmentlicor extends Fragment {
    private View fragmento;
private ImageView Arroz, Harina, Aseo_Personal, Licor, lacteos;



    public Fragmentlicor() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmento =  inflater.inflate(R.layout.fragment_fragmentinicio, container, false);

        Arroz = (ImageView) fragmento.findViewById(R.id.imgarroz);

        Harina = (ImageView) fragmento.findViewById(R.id.imgharina);

        Aseo_Personal = (ImageView) fragmento.findViewById(R.id.imgasop);

        Licor = (ImageView) fragmento.findViewById(R.id.imglicor);
        lacteos = (ImageView) fragmento.findViewById(R.id.imglacteo);
        lacteos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), agregarproducto.class);
                intent.putExtra("categoria", "Lacteos");
                startActivity(intent);
            }
        });

        Arroz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), agregarproducto.class);
                intent.putExtra("categoria", "Arroz");
                startActivity(intent);
            }
        });

        Harina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), agregarproducto.class);
                intent.putExtra("categoria", "Harina");
                startActivity(intent);
            }
        });

        Aseo_Personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), agregarproducto.class);
                intent.putExtra("categoria", "Aseo_Personal");
                startActivity(intent);
            }
        });

        Licor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), agregarproducto.class);
                intent.putExtra("categoria", "Licor");
                startActivity(intent);
            }
        });
        return fragmento;



    }
    }
