package com.joelmaza.mediclic.Solicitudes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.joelmaza.mediclic.Adaptadores.Adapter_solicitud;
import com.joelmaza.mediclic.Controllers.Ctl_solicitud;
import com.joelmaza.mediclic.Principal;
import com.joelmaza.mediclic.R;

public class Ver_solicitud extends AppCompatActivity {
    RecyclerView recyclerView;
    ProgressBar progressBar;
    TextView txt_sinresultados, txt_contador, txt_nombre;
    Adapter_solicitud adapterSolicitud;
    public static Ctl_solicitud ctlSolicitud;
    CardView cardview_nombre;
    Button btn_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_solicitud);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setOnClickListener(view -> finish());

        recyclerView = findViewById(R.id.recyclerview_solicitudes);
        txt_sinresultados = findViewById(R.id.txt_sinresultados);
        progressBar = findViewById(R.id.progressBar);
        txt_contador = findViewById(R.id.txt_contador);
        txt_nombre = findViewById(R.id.txt_nombre);
        cardview_nombre = findViewById(R.id.cardview_nombre);

        btn_add = findViewById(R.id.add_solicitudes);

        adapterSolicitud = new Adapter_solicitud(this);
        ctlSolicitud = new Ctl_solicitud(Principal.databaseReference);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapterSolicitud);

        btn_add.setOnClickListener(view -> {
            startActivity(new Intent(this, Add_solicitud.class));
        });

        if (Principal.rol.equals("Administrador")|| Principal.rol.equals("Paciente")){
            btn_add.setVisibility(View.VISIBLE);

        }else{
            btn_add.setVisibility(View.GONE);
        }

        if(!Principal.id.isEmpty() && !Principal.Nombre.isEmpty()) {

            if(Principal.rol.equals("Administrador")|| Principal.rol.equals("Doctor")){
                cardview_nombre.setVisibility(View.GONE);
                txt_nombre.setText("");
                ctlSolicitud.VerSolicitudes(adapterSolicitud, txt_sinresultados, progressBar, txt_contador);
            }else{
                cardview_nombre.setVisibility(View.VISIBLE);
                txt_nombre.setText(Principal.Nombre);
                ctlSolicitud.Ver_my_Solicitudes(adapterSolicitud, Principal.id, txt_sinresultados, progressBar, txt_contador);
            }
        }

    }
}