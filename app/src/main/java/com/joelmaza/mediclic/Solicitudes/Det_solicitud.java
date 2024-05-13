package com.joelmaza.mediclic.Solicitudes;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.joelmaza.mediclic.Controllers.Alert_dialog;
import com.joelmaza.mediclic.Controllers.Progress_dialog;
import com.joelmaza.mediclic.Objetos.Ob_solicitud;
import com.joelmaza.mediclic.Principal;
import com.joelmaza.mediclic.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class Det_solicitud extends AppCompatActivity {
    Spinner spinner_tipo, spinner_estado;
    TextView fecha_solicitud, fecha_respuesta;
    EditText editTextMotivo;
    String uid = "";
    String uid_empleado = "";
    String ced_empleado = "";
    ArrayAdapter<CharSequence> adapterspinner_tipo, adapterspinner_estado;
    Button btn_edit_solicitud, btn_del_solicitud;;
    Alert_dialog alertDialog;
    Progress_dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_det_solicitud);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setOnClickListener(view -> finish());

        btn_edit_solicitud = findViewById(R.id.btn_edit_solicitud);
        btn_del_solicitud = findViewById(R.id.btn_del_solicitud);

        spinner_tipo = findViewById(R.id.spinner_tipo);
        spinner_estado = findViewById(R.id.spinner_estado);
        editTextMotivo = findViewById(R.id.editTextMotivo);

        fecha_solicitud = findViewById(R.id.fecha_solicitud);
        fecha_respuesta = findViewById(R.id.fecha_respuesta);

        dialog = new Progress_dialog(this);
        alertDialog = new Alert_dialog(this);

        uid = Objects.requireNonNull(getIntent().getExtras()).getString("uid","");
        uid_empleado = Objects.requireNonNull(getIntent().getExtras()).getString("uid_empleado","");
        ced_empleado = Objects.requireNonNull(getIntent().getExtras()).getString("ced_empleado","");

        adapterspinner_tipo = ArrayAdapter.createFromResource(this, R.array.tipo_solicitud, android.R.layout.simple_spinner_item);
        adapterspinner_tipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_tipo.setAdapter(adapterspinner_tipo);

        adapterspinner_estado = ArrayAdapter.createFromResource(this, R.array.estado, android.R.layout.simple_spinner_item);
        adapterspinner_estado.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_estado.setAdapter(adapterspinner_estado);

        assert uid != null;
        if(!uid.isEmpty() && !uid_empleado.isEmpty() ) {

            if(Principal.rol.equals("Administrador")|| Principal.rol.equals("Doctor")){
                btn_del_solicitud.setVisibility(View.VISIBLE);
                btn_edit_solicitud.setVisibility(View.VISIBLE);
                spinner_estado.setEnabled(true);
                spinner_tipo.setEnabled(true);
                editTextMotivo.setEnabled(true);
            }else{
                btn_del_solicitud.setVisibility(View.GONE);
                btn_edit_solicitud.setVisibility(View.GONE);
                spinner_estado.setEnabled(false);
                spinner_tipo.setEnabled(false);
                editTextMotivo.setEnabled(false);
            }

            btn_del_solicitud.setOnClickListener(view -> {

                alertDialog.crear_mensaje("¿Estás Seguro de Eliminar la solicitud?", "¡Esta acción no es reversible!", builder -> {
                    builder.setPositiveButton("Aceptar", (dialogInterface, i) -> {
                        dialog.mostrar_mensaje("Eliminando Solicitud...");
                        Ver_solicitud.ctlSolicitud.eliminar_solicitud(uid_empleado, uid);
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

                if(!editTextMotivo.getText().toString().isEmpty() && !spinner_tipo.getSelectedItem().toString().equals("Selecciona") && !spinner_estado.getSelectedItem().toString().equals("Selecciona")) {

                    Date dia = new Date();
                    String hora = String.format("%02d:%02d", dia.getHours(), dia.getMinutes())+ " "+ ((dia.getHours()<12) ? "am":"pm");

                    Ob_solicitud solicitud = new Ob_solicitud();
                    solicitud.uid = uid;
                    solicitud.motivo = editTextMotivo.getText().toString();
                    solicitud.tipo = spinner_tipo.getSelectedItem().toString();
                    solicitud.estado = spinner_estado.getSelectedItem().toString();

                    if(solicitud.estado.equals("Aprobado") || solicitud.estado.equals("Rechazado")){
                        solicitud.fecha_respuesta = (new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(dia.getTime())) + " - "+ hora;

                    }else{
                        solicitud.fecha_respuesta = "";
                    }


                    Ver_solicitud.ctlSolicitud.update_solicitud(uid_empleado,solicitud);

                    dialog.ocultar_mensaje();
                    alertDialog.crear_mensaje("Correcto", "Solicitud Actualizada Correctamente", builder -> {
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

            Principal.databaseReference.child("usuarios").child(uid_empleado).child("solicitudes").child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(snapshot.exists()){

                        if(snapshot.child("fecha_solicitud").exists()) {
                            fecha_solicitud.setText(Objects.requireNonNull(snapshot.child("fecha_solicitud").getValue()).toString());
                        }
                        if(snapshot.child("fecha_respuesta").exists()) {
                            fecha_respuesta.setText(Objects.requireNonNull(snapshot.child("fecha_respuesta").getValue()).toString());
                        }else{
                            fecha_respuesta.setText("-");
                        }

                        if(snapshot.child("motivo").exists()){
                            editTextMotivo.setText(Objects.requireNonNull(snapshot.child("motivo").getValue()).toString());
                        }

                        if(snapshot.child("estado").exists()){
                            String estado = Objects.requireNonNull(snapshot.child("estado").getValue()).toString();
                            int spinnerPosition = adapterspinner_estado.getPosition(estado);
                            spinner_estado.setSelection(spinnerPosition);

                            if(Principal.rol.equals("Administrador")) {
                                if (estado.equalsIgnoreCase("aprobado") || estado.equalsIgnoreCase("rechazado")) {
                                    spinner_estado.setEnabled(false);
                                    spinner_tipo.setEnabled(false);
                                    editTextMotivo.setEnabled(false);
                                    btn_edit_solicitud.setVisibility(View.GONE);
                                } else {
                                    spinner_estado.setEnabled(true);
                                    spinner_tipo.setEnabled(true);
                                    editTextMotivo.setEnabled(true);
                                    btn_edit_solicitud.setVisibility(View.VISIBLE);
                                }
                            }

                        }
                        if(snapshot.child("tipo").exists()){
                            String tipo = Objects.requireNonNull(snapshot.child("tipo").getValue()).toString();
                            int spinnerPosition = adapterspinner_tipo.getPosition(tipo);
                            spinner_tipo.setSelection(spinnerPosition);
                        }

                    }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }


    }
}