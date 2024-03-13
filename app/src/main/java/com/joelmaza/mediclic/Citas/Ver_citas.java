package com.joelmaza.mediclic.Citas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.joelmaza.mediclic.Adaptadores.Adapter_citas;
import com.joelmaza.mediclic.Controllers.Ctl_citas;
import com.joelmaza.mediclic.Principal;
import com.joelmaza.mediclic.R;
import com.joelmaza.mediclic.Usuarios.Add_usuario;

public class Ver_citas extends AppCompatActivity {
    RecyclerView recyclerView;
    ProgressBar progressBar;
    TextView txt_sinresultados, txt_contador, txt_nombre;
    Adapter_citas adapterActividad;
    public static Ctl_citas ctlActividad;
    CardView cardview_nombre;
    Button add_citas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_citas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setOnClickListener(view -> finish());

        recyclerView = findViewById(R.id.recyclerview_cita);
        txt_sinresultados = findViewById(R.id.txt_sinresultados);
        progressBar = findViewById(R.id.progressBar);
        txt_contador = findViewById(R.id.txt_contador);
        txt_nombre = findViewById(R.id.txt_nombre);
        cardview_nombre = findViewById(R.id.cardview_nombre);

        add_citas =findViewById(R.id.add_citas);

        add_citas.setOnClickListener(View->{
            Intent i = new Intent();
            i.setClass(this, Add_citas.class);
            startActivity(i);
        });

        adapterActividad = new Adapter_citas(this);
        ctlActividad = new Ctl_citas(Principal.databaseReference);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapterActividad);
        if(!Principal.id.isEmpty() && !Principal.Nombre.isEmpty()) {

            if(Principal.rol.equals("Administrador")){
                cardview_nombre.setVisibility(View.GONE);

                txt_nombre.setText("");
                ctlActividad.VerActividades(adapterActividad, txt_sinresultados, progressBar, txt_contador);
                //ctlMarcacion.VerMarcaciones(adapterMarcacion, txt_sinresultados, progressBar, txt_contador);
            }else{
                cardview_nombre.setVisibility(View.VISIBLE);
                txt_nombre.setText(Principal.Nombre);
                ctlActividad.Ver_my_Actividades(adapterActividad, Principal.id, txt_sinresultados, progressBar, txt_contador);
            }
        }



        if (Principal.rol.equals("Administrador")){
            add_citas.setVisibility(View.VISIBLE);

        }else{
            add_citas.setVisibility(View.GONE);
        }
    }
}