package com.joelmaza.mediclic.Doctores;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.joelmaza.mediclic.Adaptadores.Adaptador_usuarios;
import com.joelmaza.mediclic.MainActivity;
import com.joelmaza.mediclic.Principal;
import com.joelmaza.mediclic.R;
import com.joelmaza.mediclic.Registro;
import com.joelmaza.mediclic.Usuarios.Add_usuario;


public class Ver_doctores extends AppCompatActivity {

    Button btn_add_usuario;
    DatabaseReference dbRef;
    RecyclerView recyclerview_usuarios;
    Adaptador_usuarios lista_usuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_doctores);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setOnClickListener(view -> finish());

        recyclerview_usuarios = (RecyclerView) findViewById(R.id.recyclerview_usuarios);
        dbRef = MainActivity.DB.getReference();
        ProgressBar progressBar = findViewById(R.id.progressBar);
        TextView txt_existe = findViewById(R.id.txt_existe);
        TextView txt_contador = findViewById(R.id.txt_contador);


        btn_add_usuario= findViewById(R.id.btn_add_usuario);
        if (Principal.rol.equals("Administrador")){
            btn_add_usuario.setVisibility(View.VISIBLE);

        }else{
            btn_add_usuario.setVisibility(View.GONE);
        }

        btn_add_usuario.setOnClickListener(view -> {
            Intent i = new Intent();
            i.setClass(this, Add_usuario.class);
            startActivity(i);


        });

        MainActivity.ctlUsuario.verUsuarios(dbRef,"Doctor",lista_usuarios, Principal.id,txt_existe,progressBar, txt_contador);



    }
}