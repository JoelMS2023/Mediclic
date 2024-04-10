package com.joelmaza.mediclic.Citas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.joelmaza.mediclic.Controllers.Alert_dialog;
import com.joelmaza.mediclic.Controllers.Progress_dialog;
import com.joelmaza.mediclic.Fragments.Dialog_Fragment_Usuarios;
import com.joelmaza.mediclic.Objetos.Ob_citas;
import com.joelmaza.mediclic.Principal;
import com.joelmaza.mediclic.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;


public class Add_citas extends AppCompatActivity {
    Spinner spinner_tipo;
    Button  btn_add_actividad, add_empleado;
    public static TextView card_nombre, card_cedula;
    ArrayAdapter<CharSequence> adapterspinner_tipo;
    boolean puedeguardar = false;
    public static String UID_EMPLEADO ;
    EditText editTextActividad;
    CalendarView cal_inicio;
    TimePicker time_inicio;
    Alert_dialog alertDialog;
    Progress_dialog dialog;
    long fecha_cal_ini;
    String hora_time_inicio;
    DatabaseReference dbref;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_citas);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setOnClickListener(view -> finish());


        btn_add_actividad = findViewById(R.id.btn_add_actividad);
        add_empleado = findViewById(R.id.add_empleado);


        card_nombre = findViewById(R.id.card_nombre);
        card_cedula = findViewById(R.id.card_cedula);


        editTextActividad = findViewById(R.id.editTextActividad);
        cal_inicio = findViewById(R.id.fecha_inicio);
        time_inicio = findViewById(R.id.hora_inicio);

        dialog = new Progress_dialog(this);
        alertDialog = new Alert_dialog(this);

        spinner_tipo = findViewById(R.id.spinner_tipo);

        adapterspinner_tipo = ArrayAdapter.createFromResource(this, R.array.tipo_cita, android.R.layout.simple_spinner_item);
        adapterspinner_tipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_tipo.setAdapter(adapterspinner_tipo);

        Date dia = new Date();

        cal_inicio.setMinDate(dia.getTime());



        fecha_cal_ini = dia.getTime();

        add_empleado.setOnClickListener(view -> {

            Dialog_Fragment_Usuarios dialogFragmentUsuarios = new Dialog_Fragment_Usuarios();
            dialogFragmentUsuarios.show(getSupportFragmentManager(),"Doctor");

        });




        hora_time_inicio = String.format("%02d:%02d",dia.getHours(),dia.getMinutes()) + " "+ ((dia.getHours()<12) ? "am":"pm");

        cal_inicio.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year,month,dayOfMonth);
            view.setDate(calendar.getTimeInMillis());
            fecha_cal_ini = view.getDate();
        });



        time_inicio.setOnTimeChangedListener((view, hourOfDay, minute) -> {
            hora_time_inicio = String.format("%02d:%02d", hourOfDay, minute) + " " + ((hourOfDay < 12) ? "am" : "pm");



            String fecha_calendario = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(fecha_cal_ini);
            Principal.databaseReference.child("usuarios").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if (snapshot.child("citas").exists()) {
                                for (DataSnapshot datos : snapshot.child("citas").getChildren()) {

                                    String fechaCita = Objects.requireNonNull(datos.child("fecha_inicio").getValue()).toString();

                                    if(fechaCita.equals(fecha_calendario)) {

                                        String horaCita = datos.child("hora_inicio").getValue().toString(); // Suponiendo que "hora" es el campo que almacena la hora de la cita en tu base de datos.

                                        if (horaCita.equals(hora_time_inicio)) {
                                            Toast.makeText(getBaseContext(), "Hora ocupada", Toast.LENGTH_SHORT).show();
                                            //time_inicio.setBackgroundColor(getResources().getColor(R.color.warning));
                                            puedeguardar = false;
                                            break;

                                        }

                                    }
                                }
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Manejo de errores de lectura de base de datos
                }
            });
        });


        btn_add_actividad.setOnClickListener(view -> {

            dialog.mostrar_mensaje("Creando cita...");

            if (!editTextActividad.getText().toString().isEmpty()) {

                if (!spinner_tipo.getSelectedItem().toString().equals("Selecciona")) {

                    if (!UID_EMPLEADO.isEmpty()) {

                        if(puedeguardar) {

                            Ob_citas obActividad = new Ob_citas();
                            obActividad.estado = "Pendiente";
                            obActividad.fecha_inicio = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(fecha_cal_ini);
                            obActividad.hora_inicio = hora_time_inicio;
                            obActividad.tipo = spinner_tipo.getSelectedItem().toString();
                            obActividad.mensaje = editTextActividad.getText().toString();

                            Ver_citas.ctlActividad.crear_cita(UID_EMPLEADO, obActividad);

                            dialog.ocultar_mensaje();
                            alertDialog.crear_mensaje("Correcto", "Cita Creada Correctamente", builder -> {
                                builder.setCancelable(false);
                                builder.setNeutralButton("Aceptar", (dialogInterface, i) -> finish());
                                builder.create().show();
                            });
                        }else{
                            dialog.ocultar_mensaje();
                            alertDialog.crear_mensaje("¡Advertencia!", "EL HORARIO ESTÁ OCUPADO", builder -> {
                                builder.setCancelable(true);
                                builder.setNeutralButton("Aceptar", (dialogInterface, i) -> {});
                                builder.create().show();
                            });
                        }
                    }else{
                        dialog.ocultar_mensaje();
                        alertDialog.crear_mensaje("¡Advertencia!", "Selecciona un doctor", builder -> {
                            builder.setCancelable(true);
                            builder.setNeutralButton("Aceptar", (dialogInterface, i) -> {});
                            builder.create().show();
                        });
                    }

                } else {
                        dialog.ocultar_mensaje();
                        alertDialog.crear_mensaje("¡Advertencia!", "Selecciona un Tipo de Cita", builder -> {
                            builder.setCancelable(true);
                            builder.setNeutralButton("Aceptar", (dialogInterface, i) -> {
                            });
                            builder.create().show();
                        });
                }
            } else {
                    dialog.ocultar_mensaje();
                    alertDialog.crear_mensaje("¡Advertencia!", "Completa todos los campos", builder -> {
                        builder.setCancelable(true);
                        builder.setNeutralButton("Aceptar", (dialogInterface, i) -> {
                        });
                        builder.create().show();
                    });
                }
        });







    }
    
}