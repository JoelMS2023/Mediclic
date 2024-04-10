package com.joelmaza.mediclic.Citas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.joelmaza.mediclic.Controllers.Alert_dialog;
import com.joelmaza.mediclic.Controllers.Progress_dialog;
import com.joelmaza.mediclic.Objetos.Ob_citas;
import com.joelmaza.mediclic.Principal;
import com.joelmaza.mediclic.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class Det_citas extends AppCompatActivity {

    ArrayAdapter<CharSequence> adapterspinner_tipo, adapterspinner_estado;
    Spinner spinner_tipo, spinner_estado;
    EditText editTextActividad;
    Alert_dialog alertDialog;
    TextView txt_fecha, txt_hora, txt_nombre,txt_cedula;
    Progress_dialog dialog;
    TextView card_nombre, card_cedula;
    String uid = "", ced_empleado = "", nom_empleado ="",uid_empleado = "";
    Button btn_edit_actividad, btn_del_actividad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_det_citas);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setOnClickListener(view -> finish());

        spinner_tipo = findViewById(R.id.spinner_tipo);
        spinner_estado = findViewById(R.id.spinner_estado);
        editTextActividad = findViewById(R.id.editTextActividad);
        card_nombre = findViewById(R.id.card_nombre);
        card_cedula = findViewById(R.id.card_cedula);
        txt_fecha = findViewById(R.id.txt_fecha);
        txt_hora = findViewById(R.id.txt_hora);
        txt_nombre = findViewById(R.id.txt_nombre);
        txt_cedula=findViewById(R.id.txt_cedula);



        // Obtener la fecha y la hora de la cita de la actividad anterior
        String fechaCita = getIntent().getStringExtra("fecha_cita");
        String horaCita = getIntent().getStringExtra("hora_cita");

        // Mostrar la fecha y la hora en los TextViews
        txt_fecha.setText("Fecha de la cita: " + fechaCita);
        txt_hora.setText("Hora de la cita: " + horaCita);





        btn_edit_actividad = findViewById(R.id.btn_edit_actividad);
        btn_del_actividad = findViewById(R.id.btn_del_actividad);

        uid = Objects.requireNonNull(getIntent().getExtras()).getString("uid","");
        uid_empleado = Objects.requireNonNull(getIntent().getExtras()).getString("uid_empleado","");
        ced_empleado = Objects.requireNonNull(getIntent().getExtras()).getString("ced_empleado","");
        nom_empleado = Objects.requireNonNull(getIntent().getExtras()).getString("nom_empleado","");

        dialog = new Progress_dialog(this);
        alertDialog = new Alert_dialog(this);

        adapterspinner_tipo = ArrayAdapter.createFromResource(this, R.array.tipo_cita, android.R.layout.simple_spinner_item);
        adapterspinner_tipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_tipo.setAdapter(adapterspinner_tipo);

        adapterspinner_estado = ArrayAdapter.createFromResource(this, R.array.estado_actividad, android.R.layout.simple_spinner_item);
        adapterspinner_estado.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_estado.setAdapter(adapterspinner_estado);

        Date dia = new Date();


        assert uid !=null;
        if(!uid.isEmpty() && !uid_empleado.isEmpty()) {

            card_cedula.setText(ced_empleado);
            card_nombre.setText(nom_empleado);

            if(Principal.rol.equals("Administrador")){
                btn_del_actividad.setVisibility(View.VISIBLE);
                spinner_tipo.setEnabled(true);
                editTextActividad.setEnabled(true);


            }else{
                btn_del_actividad.setVisibility(View.GONE);
                spinner_tipo.setEnabled(false);
                editTextActividad.setEnabled(false);


            }

            btn_del_actividad.setOnClickListener(view -> {

                alertDialog.crear_mensaje("¿Estás Seguro de Eliminar la cita?", "¡Esta acción no es reversible!", builder -> {
                    builder.setPositiveButton("Aceptar", (dialogInterface, i) -> {
                        dialog.mostrar_mensaje("Eliminando Solicitud...");
                        Ver_citas.ctlActividad.eliminar_citas(uid_empleado, uid);
                        dialog.ocultar_mensaje();
                        finish();
                    });
                    builder.setNeutralButton("Cancelar", (dialogInterface, i) -> {});
                    builder.setCancelable(false);
                    builder.create().show();
                });

            });

            btn_edit_actividad.setOnClickListener(view -> {

                dialog.mostrar_mensaje("Actualizando Cita...");

                if(!editTextActividad.getText().toString().isEmpty() && !spinner_tipo.getSelectedItem().toString().equals("Selecciona") && !spinner_estado.getSelectedItem().toString().equals("Selecciona")) {

                    Ob_citas obActividad = new Ob_citas();
                    obActividad.uid = uid;
                    obActividad.estado = spinner_estado.getSelectedItem().toString();
                    obActividad.tipo = spinner_tipo.getSelectedItem().toString();
                    obActividad.mensaje = editTextActividad.getText().toString();

                    Ver_citas.ctlActividad.update_cita(uid_empleado,obActividad);

                    dialog.ocultar_mensaje();
                    alertDialog.crear_mensaje("Correcto", "Actividad Actualizada Correctamente", builder -> {
                        builder.setCancelable(false);
                        builder.setNeutralButton("Aceptar", (dialogInterface, i) -> {});
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

            Principal.databaseReference.child("usuarios").child(uid_empleado).child("citas").child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(snapshot.exists()){

                        if(snapshot.child("fecha_inicio").exists()) {
                            txt_fecha.setText(Objects.requireNonNull(snapshot.child("fecha_inicio").getValue()).toString());

                        }
                        if(snapshot.child("nombre").exists()) {
                            txt_nombre.setText(Objects.requireNonNull(snapshot.child("nombre").getValue()).toString());

                        }
                        if(snapshot.child("cedula").exists()) {
                            txt_cedula.setText(Objects.requireNonNull(snapshot.child("cedula").getValue()).toString());
                        }

                        if(snapshot.child("hora_inicio").exists()) {
                            txt_hora.setText(Objects.requireNonNull(snapshot.child("hora_inicio").getValue()).toString());

                        }

                        if(snapshot.child("mensaje").exists()){
                            editTextActividad.setText(Objects.requireNonNull(snapshot.child("mensaje").getValue()).toString());
                        }

                        if(snapshot.child("estado").exists()){
                            String estado = Objects.requireNonNull(snapshot.child("estado").getValue()).toString();
                            int spinnerPosition = adapterspinner_estado.getPosition(estado);
                            spinner_estado.setSelection(spinnerPosition);
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