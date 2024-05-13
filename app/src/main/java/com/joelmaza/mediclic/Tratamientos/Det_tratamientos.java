package com.joelmaza.mediclic.Tratamientos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.joelmaza.mediclic.Controllers.Alert_dialog;
import com.joelmaza.mediclic.Controllers.Progress_dialog;
import com.joelmaza.mediclic.Objetos.Ob_tratamientos;
import com.joelmaza.mediclic.Principal;
import com.joelmaza.mediclic.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class Det_tratamientos extends AppCompatActivity {

    EditText editTextMotivo;
    TextView txt_nombre;

    Button btn_edit_solicitud, btn_del_solicitud;;
    Alert_dialog alertDialog;
    Progress_dialog dialog;
    String uid = "";
    String uid_tratamiento = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_det_tratamientos);


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setOnClickListener(view -> finish());

        btn_edit_solicitud = findViewById(R.id.btn_edit_solicitud);
        btn_del_solicitud = findViewById(R.id.btn_del_solicitud);



        editTextMotivo = findViewById(R.id.editTextMotivo);
        txt_nombre= findViewById(R.id.txt_nombre);



        dialog = new Progress_dialog(this);
        alertDialog = new Alert_dialog(this);




        if(Principal.rol.equals("Administrador")){
            btn_del_solicitud.setVisibility(View.VISIBLE);
            btn_edit_solicitud.setVisibility(View.VISIBLE);
            editTextMotivo.setEnabled(true);
        }else{
            btn_del_solicitud.setVisibility(View.GONE);
            btn_edit_solicitud.setVisibility(View.GONE);
            editTextMotivo.setEnabled(false);
            }

            btn_del_solicitud.setOnClickListener(view -> {

                alertDialog.crear_mensaje("¿Estás Seguro de Eliminar la solicitud?", "¡Esta acción no es reversible!", builder -> {
                    builder.setPositiveButton("Aceptar", (dialogInterface, i) -> {
                        dialog.mostrar_mensaje("Eliminando Solicitud...");
                        Ver_tratamientos.ctlTratamientos.eliminar_tratamientos(Principal.databaseReference,uid_tratamiento);
                        dialog.ocultar_mensaje();
                        finish();
                    });
                    builder.setNeutralButton("Cancelar", (dialogInterface, i) -> {});
                    builder.setCancelable(false);
                    builder.create().show();
                });

            });

            btn_edit_solicitud.setOnClickListener(view -> {

                dialog.mostrar_mensaje("Actualizando Solicitud...");

                if(!editTextMotivo.getText().toString().isEmpty()  ) {


                    Ob_tratamientos tratamiento = new Ob_tratamientos();
                    tratamiento.uid = uid;
                    tratamiento.mensaje = editTextMotivo.getText().toString();


                    Ver_tratamientos.ctlTratamientos.actualizar_tratamientos(Principal.databaseReference,tratamiento);

                    dialog.ocultar_mensaje();
                    alertDialog.crear_mensaje("Correcto", "Tratamiento Actualizado Correctamente", builder -> {
                        builder.setCancelable(false);
                        builder.setNeutralButton("Aceptar", (dialogInterface, i) -> finish());
                        builder.create().show();
                    });

                }else{
                    dialog.ocultar_mensaje();
                    alertDialog.crear_mensaje("¡Advertencia!", "Completa todos los campos", builder -> {
                        builder.setCancelable(true);
                        builder.setNeutralButton("Aceptar", (dialogInterface, i) -> {});
                        builder.create().show();
                    });
                }

            });

        Principal.databaseReference.child("Tratamientos").child(uid_tratamiento).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(snapshot.exists()){


                        if(snapshot.child("tipo").exists()){
                            txt_nombre.setText(Objects.requireNonNull(snapshot.child("tipo").getValue()).toString());
                        }
                        if(snapshot.child("mensaje").exists()){
                            editTextMotivo.setText(Objects.requireNonNull(snapshot.child("mensaje").getValue()).toString());
                        }


                    }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }


    }
