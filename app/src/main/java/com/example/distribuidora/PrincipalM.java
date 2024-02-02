package com.example.distribuidora;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import modelo.Producto;

public class PrincipalM extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener {



    private DatabaseReference ProductoRef;
    private NavigationView navigationView;
    private FloatingActionButton buttonFloatante;
    private RecyclerView recyclerViewMenu;
    RecyclerView.LayoutManager layoutManager;



    FirebaseAuth auth;
    private String CurrentUserId;
    private DatabaseReference UserRef;
    private String Correo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_m);
        ProductoRef = FirebaseDatabase.getInstance().getReference().child("Productos");


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Correo = bundle.getString("userE");
        }

        auth = FirebaseAuth.getInstance();
        CurrentUserId = auth.getCurrentUser().getUid();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Usuarios");


        recyclerViewMenu = findViewById(R.id.recycleMenu);
        recyclerViewMenu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerViewMenu.setLayoutManager(layoutManager);
        buttonFloatante = (FloatingActionButton) findViewById(R.id.fab);


        buttonFloatante.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrincipalM.this, CarritoActivity.class);
                startActivity(intent);

            }
        });


        navigationView = findViewById(R.id.nav_vi);
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layaut1);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_vi);
        navigationView.setNavigationItemSelectedListener(this);


        View herderView = navigationView.getHeaderView(0);
        TextView nombreHeader = findViewById(R.id.tvUsuario);
        ImageFilterView imagenHeader = (ImageFilterView) findViewById(R.id.usuario_perfil);

    }






            @Override
            protected void onStart() {
                super.onStart();

                FirebaseUser firebaseUser= auth.getCurrentUser();
                if(firebaseUser== null){
                    EnviarAlLogin();
                    
                }else{
                    VerificarUsuarioExistente();
                }

                FirebaseRecyclerOptions<Producto> options = new FirebaseRecyclerOptions.Builder<Producto>()
                        .setQuery(ProductoRef, Producto.class).build();
                // para adaptar la parte de productos
                FirebaseRecyclerAdapter<Producto, ProductoViewHolder> adapter = new FirebaseRecyclerAdapter<Producto, ProductoViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductoViewHolder holder, int position, @NonNull Producto model) {


                        holder.productoNOm.setText(model.getNombre().toUpperCase());
                        holder.productoDescrip.setText("Descripcion: " + model.getDescripcion());
                        holder.productoPrecioUni.setText("Unidads: " + model.getPrecio());
                        holder.productoStock.setText("Stock: " + model.getStock());

                        Picasso.get().load(model.getImagen()).into(holder.productoImg);


                        holder.productoImg.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(PrincipalM.this, detallesproducto.class);
                                intent.putExtra("pid", model.getPid());
                                Log.d("PrincipalM", "ID de producto a enviar: " + model.getPid());  // Agrega este log
                                startActivity(intent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.verproductos, parent, false);
                        ProductoViewHolder holder = new ProductoViewHolder(view);
                        return holder;
                    }
                };

                recyclerViewMenu.setAdapter(adapter);
                adapter.startListening();
            }

    private void VerificarUsuarioExistente() {
        final String CurrentUserId= auth.getCurrentUser().getUid();
        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.hasChild(CurrentUserId)){
                        EnviarALReguistro();
                        
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void EnviarALReguistro() {
        Intent intent = new Intent(PrincipalM.this, ReguistroCliente.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("userE",Correo);
        startActivity(intent);
        finish();
    }

    private void EnviarAlLogin() {
        Intent intent = new Intent(PrincipalM.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);
        finish();
    }

    @Override
            public void onBackPressed() {
                DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layaut1);
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);

                } else {
                    super.onBackPressed();
                }
            }


            @Override
            public boolean onOptionsItemSelected(MenuItem item) {
                return super.onOptionsItemSelected(item);

            }

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.carrito) {
                    ActivityProductos();

                } else if (item.getItemId() == R.id.categorias) {


                } else if (item.getItemId() == R.id.Maps) {
                    ActivityMaps();

                } else if (item.getItemId() == R.id.cerrarsesion) {
                    ActivityCerrarSesion();


                } else if (item.getItemId() == R.id.perfilcli) {
                    ActivityCliente();


                }

                DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layaut1);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }

    private void ActivityCerrarSesion() {
        auth.signOut();

        Intent intent = new Intent(PrincipalM.this, Bienvenido.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void ActivityCliente() {
                Intent intent = new Intent(PrincipalM.this, ReguistroCliente.class);
                startActivity(intent);
            }

            private void ActivityMaps() {
                Intent intent = new Intent(PrincipalM.this, maps.class);
                startActivity(intent);
            }


            private void ActivityProductos() {
                Intent intent = new Intent(PrincipalM.this, CarritoActivity.class);
                startActivity(intent);
            }


        }


