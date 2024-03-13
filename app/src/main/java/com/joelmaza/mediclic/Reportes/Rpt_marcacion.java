package com.joelmaza.mediclic.Reportes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.joelmaza.mediclic.Adaptadores.Adapter_marcacion;
import com.joelmaza.mediclic.Controllers.Ctl_marcacion;
import com.joelmaza.mediclic.Principal;
import com.joelmaza.mediclic.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Rpt_marcacion extends AppCompatActivity {

    RecyclerView recyclerView;
    ProgressBar progressBar;
    EditText txt_buscador;
    ImageView filtro;
    TextView txt_sinresultados, txt_contador;
    CardView card_filtro;
    Adapter_marcacion adapterMarcacion;
    Ctl_marcacion ctlMarcacion;
    CalendarView fecha_busqueda;
    long fecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rpt_marcacion);
        recyclerView = findViewById(R.id.recyclerview_marcaciones);
        txt_sinresultados = findViewById(R.id.txt_sinresultados);
        progressBar = findViewById(R.id.progressBar);
        txt_contador = findViewById(R.id.txt_contador);
        filtro = findViewById(R.id.filtro);
        card_filtro = findViewById(R.id.card_filtro);
        fecha_busqueda = findViewById(R.id.fecha_busqueda);
        txt_buscador = findViewById(R.id.txt_buscador);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setOnClickListener(view -> finish());

        Date dia = new Date();
        fecha = dia.getTime();

        adapterMarcacion = new Adapter_marcacion(this);
        ctlMarcacion = new Ctl_marcacion(Principal.databaseReference);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapterMarcacion);

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

            ctlMarcacion.VerMarcaciones(adapterMarcacion,txt_buscador.getText().toString().trim(),fecha_now, txt_sinresultados, progressBar, txt_contador);

        });

        txt_buscador.setOnEditorActionListener((v, actionId, event) -> {

            String fecha_now = "";

            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                if(!txt_buscador.getText().toString().trim().isEmpty()) {

                    ocultar_teclado();

                    if (card_filtro.getVisibility() == View.VISIBLE) {
                        fecha_now = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(fecha);
                    }

                    ctlMarcacion.VerMarcaciones(adapterMarcacion, txt_buscador.getText().toString().trim(), fecha_now, txt_sinresultados, progressBar, txt_contador);

                }else{

                    Toast.makeText(this,"Ingresa la cédula o nombre a buscar",Toast.LENGTH_SHORT).show();
                }

                return true;

            }

            return false;

        });

    }

    public void ocultar_teclado(){
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

    }

}