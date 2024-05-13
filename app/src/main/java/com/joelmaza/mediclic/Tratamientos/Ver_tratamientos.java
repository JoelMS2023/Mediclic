package com.joelmaza.mediclic.Tratamientos;

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
import com.joelmaza.mediclic.Adaptadores.Adapter_marcacion;
import com.joelmaza.mediclic.Adaptadores.Adapter_tratamientos;
import com.joelmaza.mediclic.Citas.Add_citas;
import com.joelmaza.mediclic.Controllers.Ctl_citas;
import com.joelmaza.mediclic.Controllers.Ctl_marcacion;
import com.joelmaza.mediclic.Controllers.Ctl_tratamientos;
import com.joelmaza.mediclic.Marcacion.Add_marcacion;
import com.joelmaza.mediclic.Principal;
import com.joelmaza.mediclic.R;

public class Ver_tratamientos extends AppCompatActivity {

    RecyclerView recyclerView;
    ProgressBar progressBar;
    TextView txt_sinresultados, txt_contador, txt_nombre;
    Adapter_tratamientos adaptertratamientos;
    public static Ctl_tratamientos ctlTratamientos;
    CardView cardview_nombre;
    Button add_tratamientos;



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



        ctlTratamientos = new Ctl_tratamientos(Principal.databaseReference);

        add_tratamientos =findViewById(R.id.add_tratamientos);

        add_tratamientos.setOnClickListener(View->{
            startActivity(new Intent(this, Add_Tratamientos.class));
        });


        adaptertratamientos = new Adapter_tratamientos(this);
        ctlTratamientos = new Ctl_tratamientos(Principal.databaseReference);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adaptertratamientos);
        if(!Principal.id.isEmpty()) {

            if(Principal.rol.equals("Administrador")){
                cardview_nombre.setVisibility(View.GONE);

                txt_nombre.setText("");
                ctlTratamientos.Vertratamientos(adaptertratamientos, txt_sinresultados, progressBar, txt_contador);
                //ctlMarcacion.VerMarcaciones(adapterMarcacion, txt_sinresultados, progressBar, txt_contador);
            }else{
                cardview_nombre.setVisibility(View.VISIBLE);
                txt_nombre.setText(Principal.Nombre);
                ctlTratamientos.Vertratamientos(adaptertratamientos, txt_sinresultados, progressBar, txt_contador);
            }
        }



        if (Principal.rol.equals("Administrador")){
            add_tratamientos.setVisibility(View.VISIBLE);

        }else{
            add_tratamientos.setVisibility(View.GONE);
        }
    }
}