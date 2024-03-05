package com.joelmaza.mediclic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.joelmaza.mediclic.Controllers.Ctl_doctores;
import com.joelmaza.mediclic.Controllers.Ctl_usuario;


public class MainActivity extends AppCompatActivity {


    public static FirebaseAuth mAuth;

    public static FirebaseDatabase DB = FirebaseDatabase.getInstance();
    public static Ctl_usuario ctlUsuario;
    public static Ctl_doctores ctlDoctores;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_ingresar = (Button) findViewById(R.id.btn_ingresar);

        mAuth = FirebaseAuth.getInstance();

        ctlUsuario = new Ctl_usuario();
        ctlDoctores=new Ctl_doctores();

        preferences=getSharedPreferences("Mediclic", MODE_PRIVATE);

        btn_ingresar.setOnClickListener(view -> {

            Intent i = new Intent();
            i.setClass(this,Login.class);
            startActivity(i);

        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser usuario = mAuth.getCurrentUser();

        if(usuario!=null && !preferences.getString("uid","").isEmpty()) {
            Intent i = new Intent();
            Principal.id = usuario.getUid();
            i.setClass(this,Principal.class);
            startActivity(i);
        }

    }


}