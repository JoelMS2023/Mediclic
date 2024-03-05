package com.joelmaza.mediclic.Doctores;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.joelmaza.mediclic.Principal;
import com.joelmaza.mediclic.R;
import com.joelmaza.mediclic.Registro;
import com.joelmaza.mediclic.Usuarios.Add_usuario;

public class Ver_doctores extends AppCompatActivity {

    Button add_doctores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_doctores);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setOnClickListener(view -> finish());


        add_doctores= findViewById(R.id.add_doctores);
        if (Principal.rol.equals("Administrador")){
            add_doctores.setVisibility(View.VISIBLE);

        }else{
            add_doctores.setVisibility(View.GONE);
        }

        add_doctores.setOnClickListener(view -> {
            Intent i = new Intent();
            i.setClass(this, Add_usuario.class);
            startActivity(i);


        });


    }
}