package com.joelmaza.mediclic.Reportes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;

import com.joelmaza.mediclic.R;

public class Ver_reportes extends AppCompatActivity {

    CardView card_rpt_marcacion, card_rpt_citas, card_rpt_medico;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_reportes);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setOnClickListener(view -> finish());

        card_rpt_marcacion = findViewById(R.id.card_rpt_marcacion);

        card_rpt_marcacion.setOnClickListener(view -> {
            startActivity(new Intent(this, Rpt_marcacion.class));
        });
    }
}