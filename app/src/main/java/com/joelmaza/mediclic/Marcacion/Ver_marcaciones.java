package com.joelmaza.mediclic.Marcacion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.joelmaza.mediclic.Adaptadores.Adapter_marcacion;
import com.joelmaza.mediclic.Controllers.Ctl_marcacion;
import com.joelmaza.mediclic.Principal;
import com.joelmaza.mediclic.R;

public class Ver_marcaciones extends AppCompatActivity {

    RecyclerView recyclerView;
    ProgressBar progressBar;
    TextView txt_sinresultados, txt_contador, txt_nombre;
    Adapter_marcacion adapterMarcacion;
    public static Ctl_marcacion ctlMarcacion;
    CardView cardview_nombre;
    Button btn_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_marcaciones);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setOnClickListener(view -> finish());

        recyclerView = findViewById(R.id.recyclerview_marcaciones);
        txt_sinresultados = findViewById(R.id.txt_sinresultados);
        progressBar = findViewById(R.id.progressBar);
        txt_contador = findViewById(R.id.txt_contador);
        txt_nombre = findViewById(R.id.txt_nombre);
        cardview_nombre = findViewById(R.id.cardview_nombre);

        btn_add = findViewById(R.id.add_marcaciones);

        adapterMarcacion = new Adapter_marcacion(this);
        ctlMarcacion = new Ctl_marcacion(Principal.databaseReference);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapterMarcacion);

        btn_add.setOnClickListener(view -> {
            startActivity(new Intent(this, Add_marcacion.class));
        });


        txt_nombre.setText(Principal.Nombre);
        ctlMarcacion.Ver_my_Marcaciones(adapterMarcacion, Principal.id, txt_sinresultados, progressBar, txt_contador);

    }
}