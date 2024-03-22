package com.joelmaza.mediclic.Tratamientos;

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
import com.joelmaza.mediclic.Controllers.Ctl_tratamientos;
import com.joelmaza.mediclic.Marcacion.Add_marcacion;
import com.joelmaza.mediclic.Principal;
import com.joelmaza.mediclic.R;

public class Ver_tratamientos extends AppCompatActivity {

    RecyclerView recyclerView;
    ProgressBar progressBar;
    TextView txt_sinresultados, txt_contador, txt_nombre;
    Adapter_marcacion adapterMarcacion;
    public static Ctl_tratamientos ctlTratamientos;
    CardView cardview_nombre;
    Button btn_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_tratamientos);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setOnClickListener(view -> finish());
        recyclerView = findViewById(R.id.recyclerview_tratamientos);
        txt_sinresultados = findViewById(R.id.txt_sinresultados);
        progressBar = findViewById(R.id.progressBar);
        txt_contador = findViewById(R.id.txt_contador);
        txt_nombre = findViewById(R.id.txt_nombre);
        cardview_nombre = findViewById(R.id.cardview_nombre);

        btn_add = findViewById(R.id.add_marcaciones);

        adapterMarcacion = new Adapter_marcacion(this);
        ctlTratamientos = new Ctl_tratamientos(Principal.databaseReference);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapterMarcacion);

        btn_add.setOnClickListener(view -> {
            startActivity(new Intent(this, Add_marcacion.class));
        });


        txt_nombre.setText(Principal.Nombre);
        ctlTratamientos.Ver_my_tratamientos(adapterMarcacion, Principal.id, txt_sinresultados, progressBar, txt_contador);

    }
}
