package com.joelmaza.mediclic.Doctores;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.joelmaza.mediclic.Citas.Add_citas;
import com.joelmaza.mediclic.MainActivity;

import com.joelmaza.mediclic.R;
import com.joelmaza.mediclic.Usuarios.Add_usuario;

import java.util.Objects;

public class Det_doctores extends AppCompatActivity {
    TextView txt_nombre, txt_email;
    EditText editext_direccion, editext_telefono;
    DatabaseReference dbref;
    Button btn_update, btn_delete;
    String uid;
    ImageView Agenda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_det_doctores);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setOnClickListener(view -> finish());

        dbref = MainActivity.DB.getReference();

        uid = getIntent().getExtras().getString("uid");

        txt_nombre = findViewById(R.id.txt_nombre);
        txt_email = findViewById(R.id.txt_email);
        editext_direccion = findViewById(R.id.editext_direccion);
        editext_telefono = findViewById(R.id.editext_telefono);
        Agenda=findViewById(R.id.agenda);

        btn_update  = findViewById(R.id.btn_update);
        btn_delete = findViewById(R.id.btn_delete);



        btn_delete.setOnClickListener(v ->{


            MainActivity.ctlUsuario.eliminar_usuario(dbref,uid);

            Toast.makeText(this,"Usuario Eliminado Correctamente",Toast.LENGTH_SHORT).show();

            finish();

        });

        Agenda.setOnClickListener(view -> {
            Intent i = new Intent();
            i.setClass(this, Add_citas.class);
            startActivity(i);

        });


        dbref.child("usuarios").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datos) {

                if(datos.exists()){

                    txt_nombre.setText(Objects.requireNonNull(datos.child("nombre").getValue()).toString());
                    txt_email.setText(Objects.requireNonNull(datos.child("email").getValue()).toString());

                    if(datos.child("direccion").exists()){
                        editext_direccion.setText(Objects.requireNonNull(datos.child("direccion").getValue()).toString());
                    }else{
                        editext_direccion.setText("");
                    }
                    if(datos.child("telefono").exists()){
                        editext_telefono.setText(Objects.requireNonNull(datos.child("telefono").getValue()).toString());
                    }else{
                        editext_telefono.setText("");
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}