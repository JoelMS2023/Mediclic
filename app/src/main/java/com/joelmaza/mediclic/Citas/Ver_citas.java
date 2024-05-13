package com.joelmaza.mediclic.Citas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.joelmaza.mediclic.Adaptadores.Adapter_citas;
import com.joelmaza.mediclic.Controllers.Ctl_citas;
import com.joelmaza.mediclic.Principal;
import com.joelmaza.mediclic.R;
import com.joelmaza.mediclic.Usuarios.Add_usuario;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Ver_citas extends AppCompatActivity {
    RecyclerView recyclerView;
    ProgressBar progressBar;
    TextView txt_sinresultados, txt_contador, txt_nombre;
    Adapter_citas adapterActividad;
    public static Ctl_citas ctlActividad;
    CardView cardview_nombre;
    Button add_citas;
    ImageView filtro;
    CardView card_filtro;
    CalendarView fecha_busqueda;
    long fecha;




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
        filtro = findViewById(R.id.filtro);
        card_filtro = findViewById(R.id.card_filtro);
        fecha_busqueda = findViewById(R.id.fecha_busqueda);

        add_citas =findViewById(R.id.add_citas);

        add_citas.setOnClickListener(View->{
            startActivity(new Intent(this, Add_citas.class));

        });
        Date dia = new Date();
        fecha = dia.getTime();

        card_filtro.setVisibility(View.GONE);

        filtro.setOnClickListener(v -> {
            card_filtro.setVisibility(card_filtro.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        });
        fecha_busqueda.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String fecha_now = "";
            Calendar calendar = Calendar.getInstance();
            calendar.set(year,month,dayOfMonth);
            view.setDate(calendar.getTimeInMillis());
            fecha = view.getDate();

            if(card_filtro.getVisibility() == View.VISIBLE){
                fecha_now =  new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(fecha);
            }
            ctlActividad.Buscarcitas(adapterActividad,fecha_now, txt_sinresultados, progressBar, txt_contador);

        });




        adapterActividad = new Adapter_citas(this);
        ctlActividad = new Ctl_citas(Principal.databaseReference);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapterActividad);

        if (!Principal.id.isEmpty() && !Principal.Nombre.isEmpty()) {
            if (Principal.rol.equals("Administrador")) {
                // Mostrar elementos específicos para el administrador
                cardview_nombre.setVisibility(View.VISIBLE);
                txt_nombre.setText(Principal.Nombre);
                ctlActividad.VerActividades(adapterActividad, txt_sinresultados, progressBar, txt_contador);
            }
            if (Principal.rol.equals("Paciente")) {
                cardview_nombre.setVisibility(View.VISIBLE);
                txt_nombre.setText(Principal.Nombre);
                ctlActividad.Ver_my_Citas(adapterActividad, Principal.id, txt_sinresultados, progressBar, txt_contador);
            }
            if (Principal.rol.equals("Doctor")) {

                cardview_nombre.setVisibility(View.VISIBLE);
                txt_nombre.setText(Principal.Nombre);
                ctlActividad.Ver_my_Actividades(adapterActividad, Principal.id, txt_sinresultados, progressBar, txt_contador);
            }
        }

        if (Principal.rol.equals("Administrador")|| Principal.rol.equals("Paciente")){
            add_citas.setVisibility(View.VISIBLE);
        }else{
            add_citas.setVisibility(View.GONE);
        }
        if (Principal.rol.equals("Administrador")|| Principal.rol.equals("Doctor")){
            filtro.setVisibility(View.VISIBLE);
        }else{
            filtro.setVisibility(View.GONE);
        }
    }
}