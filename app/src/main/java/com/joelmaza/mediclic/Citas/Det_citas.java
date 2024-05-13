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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.bumptech.glide.Glide;
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

    ArrayAdapter<CharSequence> adapterspinner_tipo, adapterspinner_estado,adapterspinner_tipo_cita;
    Spinner spinner_tipo, spinner_estado,spinner_tipo_cita;

    Alert_dialog alertDialog;
    TextView txt_fecha, txt_hora, txt_nombre,txt_cedula;
    Progress_dialog dialog;
    ImageView card_foto;
    TextView card_nombre, card_cedula,card_info;
    String uid = "", ced_empleado = "", nom_empleado ="",uid_empleado = "";
    Button btn_edit_actividad, btn_del_actividad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_det_citas);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setOnClickListener(view -> finish());

        spinner_tipo = findViewById(R.id.spinner_tipo);
        spinner_tipo_cita = findViewById(R.id.spinner_tipo_cita);

        spinner_estado = findViewById(R.id.spinner_estado);
        card_nombre = findViewById(R.id.card_nombre);
        card_cedula = findViewById(R.id.card_cedula);
        card_foto = findViewById(R.id.card_foto);
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

        adapterspinner_tipo_cita = ArrayAdapter.createFromResource(this, R.array.tipo, android.R.layout.simple_spinner_item);
        adapterspinner_tipo_cita.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_tipo_cita.setAdapter(adapterspinner_tipo_cita);

        adapterspinner_estado = ArrayAdapter.createFromResource(this, R.array.estado_actividad, android.R.layout.simple_spinner_item);
        adapterspinner_estado.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_estado.setAdapter(adapterspinner_estado);

        Date dia = new Date();


        assert uid !=null;
        if(!uid.isEmpty() && !uid_empleado.isEmpty()) {

            card_cedula.setText(ced_empleado);
            card_nombre.setText(nom_empleado);

            if(Principal.rol.equals("Administrador")|| Principal.rol.equals("Doctor")){
                btn_del_actividad.setVisibility(View.VISIBLE);
                btn_edit_actividad.setVisibility(View.VISIBLE);
                spinner_tipo.setEnabled(false);
                spinner_tipo_cita.setEnabled(false);



            }else{
                btn_del_actividad.setVisibility(View.GONE);
                btn_edit_actividad.setVisibility(View.GONE);
                spinner_tipo.setEnabled(false);
                spinner_tipo_cita.setEnabled(false);
                spinner_estado.setEnabled(false);


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

                if(!spinner_tipo_cita.getSelectedItem().toString().isEmpty()  && !spinner_estado.getSelectedItem().toString().equals("Selecciona")) {

                    Ob_citas obActividad = new Ob_citas();
                    obActividad.uid = uid;
                    obActividad.estado = spinner_estado.getSelectedItem().toString();
                    obActividad.tipo_cita = spinner_tipo_cita.getSelectedItem().toString();

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
                        if(snapshot.child("paciente").exists()) {
                            txt_nombre.setText(Objects.requireNonNull(snapshot.child("paciente").getValue()).toString());

                        }
                        if(snapshot.child("ced_usuario").exists()) {
                            txt_cedula.setText(Objects.requireNonNull(snapshot.child("ced_usuario").getValue()).toString());
                        }

                        if(snapshot.child("hora_inicio").exists()) {
                            txt_hora.setText(Objects.requireNonNull(snapshot.child("hora_inicio").getValue()).toString());

                        }
                        if(snapshot.child("tipo_cita").exists()){
                            String tipo = Objects.requireNonNull(snapshot.child("tipo_cita").getValue()).toString();
                            int spinnerPosition = adapterspinner_tipo_cita.getPosition(tipo);
                            spinner_tipo_cita.setSelection(spinnerPosition);
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
                        if(snapshot.child("url_foto").exists()){
                            String URL_FOTO = Objects.requireNonNull(snapshot.child("url_foto").getValue()).toString();
                            Glide.with(getBaseContext()).load(URL_FOTO).centerCrop().into(card_foto);
                        }else{
                            card_foto.setImageResource(R.drawable.perfil);
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