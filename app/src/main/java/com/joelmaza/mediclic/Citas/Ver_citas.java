package com.joelmaza.mediclic.Citas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.joelmaza.mediclic.Principal;
import com.joelmaza.mediclic.R;

public class Ver_citas extends AppCompatActivity {
    Button add_citas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_citas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setOnClickListener(view -> finish());

        add_citas =findViewById(R.id.add_citas);

        if (Principal.rol.equals("Administrador")){
            add_citas.setVisibility(View.VISIBLE);

        }else{
            add_citas.setVisibility(View.GONE);
        }
    }
}