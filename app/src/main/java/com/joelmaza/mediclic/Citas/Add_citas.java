package com.joelmaza.mediclic.Citas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
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
import com.joelmaza.mediclic.MainActivity;
import com.joelmaza.mediclic.Objetos.Ob_citas;
import com.joelmaza.mediclic.Principal;
import com.joelmaza.mediclic.R;


import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class Add_citas extends AppCompatActivity {
    Spinner spinner_tipo, spinner_tipo_cita;
    Button btn_add_actividad, add_empleado, btnAdelantarHora, btnRetrocederHora;
    public static TextView card_nombre, card_cedula;
    ArrayAdapter<CharSequence> adapterspinner_tipo, adapterspinner_tipo_cita;
    public static String UID_EMPLEADO;

    CalendarView cal_inicio;
    Progress_dialog dialog;
    long fecha_cal_ini;
    String hora_time_inicio;
    TimePicker time_inicio;
    Alert_dialog alertDialog;
    ImageView card_foto;
    int selectedYear;
    int selectedMonth;
    int selectedDayOfMonth;


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
        card_foto = findViewById(R.id.card_foto);

        cal_inicio = findViewById(R.id.fecha_inicio);
        time_inicio = findViewById(R.id.hora_inicio);
        time_inicio.setIs24HourView(false); // Para usar el formato de 12 horas

        // Limitar la selección de fechas solo a la semana actual
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        long firstDayOfWeek = calendar.getTimeInMillis();

        calendar.add(Calendar.DAY_OF_WEEK, 6); // Sumar 6 días para obtener el último día de la semana
        long lastDayOfWeek = calendar.getTimeInMillis();

        cal_inicio.setMinDate(firstDayOfWeek);
        cal_inicio.setMaxDate(lastDayOfWeek);

        dialog = new Progress_dialog(this);
        alertDialog = new Alert_dialog(this);

        spinner_tipo = findViewById(R.id.spinner_tipo);
        spinner_tipo_cita = findViewById(R.id.spinner_tipo_cita);

        adapterspinner_tipo = ArrayAdapter.createFromResource(this, R.array.tipo_cita, android.R.layout.simple_spinner_item);
        adapterspinner_tipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_tipo.setAdapter(adapterspinner_tipo);

        adapterspinner_tipo_cita = ArrayAdapter.createFromResource(this, R.array.tipo, android.R.layout.simple_spinner_item);
        adapterspinner_tipo_cita.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_tipo_cita.setAdapter(adapterspinner_tipo_cita);

        Date dia = new Date();

        cal_inicio.setMinDate(dia.getTime());

        fecha_cal_ini = dia.getTime();

        add_empleado.setOnClickListener(view -> {
            Dialog_Fragment_Usuarios dialogFragmentUsuarios = new Dialog_Fragment_Usuarios();
            dialogFragmentUsuarios.show(getSupportFragmentManager(), "Doctor");
        });
        btn_add_actividad.setOnClickListener(view -> {
            dialog.mostrar_mensaje("Creando Cita...");
            if (!spinner_tipo_cita.getSelectedItem().toString().equals("Selecciona")) {
                if (!spinner_tipo.getSelectedItem().toString().equals("Selecciona")) {
                    if (!UID_EMPLEADO.isEmpty()) {
                        crearCita();
                    } else {
                        dialog.ocultar_mensaje();
                        alertDialog.crear_mensaje("¡Advertencia!", "Selecciona un Doctor", builder -> {
                            builder.setCancelable(true);
                            builder.setNeutralButton("Aceptar", (dialogInterface, i) -> {});
                            builder.create().show();
                        });
                    }
                } else {
                    dialog.ocultar_mensaje();
                    alertDialog.crear_mensaje("¡Advertencia!", "Selecciona un Tipo de Tratamiento", builder -> {
                        builder.setCancelable(true);
                        builder.setNeutralButton("Aceptar", (dialogInterface, i) -> {});
                        builder.create().show();
                    });
                }
            } else {
                dialog.ocultar_mensaje();
                alertDialog.crear_mensaje("¡Advertencia!", "Completa todos los campos", builder -> {
                    builder.setCancelable(true);
                    builder.setNeutralButton("Aceptar", (dialogInterface, i) -> {});
                    builder.create().show();
                });
            }
        });



        hora_time_inicio = String.format("%02d:%02d", dia.getHours(), dia.getMinutes()) + " " + ((dia.getHours() < 12) ? "am" : "pm");
        cal_inicio.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            // Lógica para establecer la fecha seleccionada
            Calendar calendar1 = Calendar.getInstance();
            calendar1.set(year, month, dayOfMonth);
            view.setDate(calendar1.getTimeInMillis());
            fecha_cal_ini = view.getDate();

            // Guardar la fecha seleccionada en las variables
            selectedYear = year;
            selectedMonth = month;
            selectedDayOfMonth = dayOfMonth;
        });

        time_inicio.setOnTimeChangedListener((view, hourOfDay, minute) -> {
            // Lógica para establecer la hora seleccionada y validarla
            int minuteAdjusted = (minute < 30) ? 0 : 30;

            Calendar currentCalendar = Calendar.getInstance();
            int currentHour = currentCalendar.get(Calendar.HOUR_OF_DAY);
            int currentMinute = currentCalendar.get(Calendar.MINUTE);

            if (hourOfDay < 8 || (hourOfDay == 8 && minuteAdjusted < 30)) {
                view.setCurrentHour(8);
                view.setCurrentMinute(0);
                hora_time_inicio = "08:00 am";
            } else if (hourOfDay >= 12 && hourOfDay < 13) {
                view.setCurrentHour(13);
                view.setCurrentMinute(30);
                hora_time_inicio = "01:30 pm";
            } else if (hourOfDay >= 17 && minuteAdjusted >= 30) {
                view.setCurrentHour(17);
                view.setCurrentMinute(30);
                hora_time_inicio = "05:30 pm";
                // Mostrar alerta sobre el horario de trabajo
                AlertDialog.Builder builder = new AlertDialog.Builder(Add_citas.this);
                builder.setTitle("Advertencia");
                builder.setMessage("El horario de atención es hasta las 5:30 pm. Por favor, seleccione un horario disponible.");
                builder.setCancelable(false);
                builder.setPositiveButton("Aceptar", (dialog, which) -> dialog.dismiss());
                AlertDialog alert = builder.create();
                alert.show();
            } else if (hourOfDay >= 17 && minuteAdjusted < 30) {
                // Si se selecciona entre 17:00 y 17:29
                view.setCurrentHour(17);
                view.setCurrentMinute(30);
                hora_time_inicio = "05:30 pm";
                // Mostrar alerta sobre el horario de trabajo
                AlertDialog.Builder builder = new AlertDialog.Builder(Add_citas.this);
                builder.setTitle("Advertencia");
                builder.setMessage("El horario de atención es hasta las 5:30 pm. Por favor, seleccione un horario disponible.");
                builder.setCancelable(false);
                builder.setPositiveButton("Aceptar", (dialog, which) -> dialog.dismiss());
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                // Guardar la hora seleccionada
                hora_time_inicio = String.format("%02d:%02d", hourOfDay, minuteAdjusted) + " " + ((hourOfDay < 12) ? "am" : "pm");

                // Si la fecha seleccionada es anterior a la fecha actual
                // Verificar si la hora seleccionada es antes de la hora actual
                if (hourOfDay < currentHour || (hourOfDay == currentHour && minuteAdjusted < currentMinute)) {
                    // Establecer la hora y minuto en la hora actual
                    view.setCurrentHour(currentHour);
                    view.setCurrentMinute(currentMinute);
                    hora_time_inicio = String.format("%02d:%02d", currentHour, currentMinute) + " " + ((currentHour < 12) ? "am" : "pm");

                    // Mostrar un toast en lugar de la alerta
                    Toast.makeText(Add_citas.this, "No puedes seleccionar un horario anterior al actual.", Toast.LENGTH_SHORT).show();
                } else {
                    // Resto del código para verificar horas como las 8, 12 y 17:30
                    // ...
                }
            }
        });


        // Busca los botones por ID
        btnAdelantarHora = findViewById(R.id.btn_adelantar_hora);
        btnRetrocederHora = findViewById(R.id.btn_retroceder_hora);

        // Configura los eventos de clic para los botones
        btnAdelantarHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adelantarHora();
            }
        });

        btnRetrocederHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrocederHora();
            }
        });

        // Resto de tu código...
    }

    // Método para adelantar la hora en el TimePicker
    private void adelantarHora() {
        int currentHour = time_inicio.getCurrentHour();
        int currentMinute = time_inicio.getCurrentMinute();

        if (currentHour == 23 && currentMinute >= 30) {
            // Si es 11:30 PM, no permitir adelantar más
            return;
        }

        // Adelantar la hora en 30 minutos
        currentMinute += 30;
        if (currentMinute >= 60) {
            currentMinute -= 60;
            currentHour = (currentHour + 1) % 24;
        }

        // Establecer la nueva hora en el TimePicker
        time_inicio.setCurrentHour(currentHour);
        time_inicio.setCurrentMinute(currentMinute);

        // Actualizar la hora_time_inicio
        hora_time_inicio = String.format("%02d:%02d", currentHour, currentMinute) + ((currentHour < 12) ? " am" : " pm");

        verificarDisponibilidad();
    }

    // Método para retroceder la hora en el TimePicker
    private void retrocederHora() {
        int currentHour = time_inicio.getCurrentHour();
        int currentMinute = time_inicio.getCurrentMinute();

        if (currentHour == 0 && currentMinute <= 0) {
            // Si es 12:00 AM, no permitir retroceder más
            return;
        }

        // Retroceder la hora en 30 minutos
        currentMinute -= 30;
        if (currentMinute < 0) {
            currentMinute += 60;
            currentHour = (currentHour - 1 + 24) % 24;
        }

        // Establecer la nueva hora en el TimePicker
        time_inicio.setCurrentHour(currentHour);
        time_inicio.setCurrentMinute(currentMinute);

        // Actualizar la hora_time_inicio
        hora_time_inicio = String.format("%02d:%02d", currentHour, currentMinute) + ((currentHour < 12) ? " am" : " pm");

        verificarDisponibilidad();
    }

    // Método para redondear los minutos al intervalo de 30 minutos más cercano
    private int roundToNearest30Minutes(int minute) {
        if (minute < 15) {
            return 0;
        } else if (minute < 45) {
            return 30;
        } else {
            return 0; // Redondea a la siguiente hora
        }
    }

    private void verificarDisponibilidad() {
        if (fecha_cal_ini == 0) {
            return;
        }
        Principal.databaseReference.child("usuarios").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot.child("citas").exists()) {
                            for (DataSnapshot datos : snapshot.child("citas").getChildren()) {
                                String fechaCita = datos.child("fecha_inicio").getValue(String.class);
                                String horaCita = datos.child("hora_inicio").getValue(String.class);
                                String paciente = datos.child("paciente").getValue(String.class);
                                if (fechaCita != null && fechaCita.equals(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(fecha_cal_ini)) && paciente != null && paciente.equals(Principal.Nombre)) {
                                    // El paciente ya tiene una cita ese día
                                    Toast.makeText(Add_citas.this, "Ya tienes una cita programada para este día", Toast.LENGTH_SHORT).show();
                                btn_add_actividad.setEnabled(false); // Deshabilitar el botón de agregar cita
                                    return;
                                }
                                // Verificar si la hora de la cita coincide
                                if (fechaCita != null && fechaCita.equals(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(fecha_cal_ini)) && horaCita != null && horaCita.equals(hora_time_inicio)) {
                                    // La hora ya está ocupada
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Add_citas.this);
                                    builder.setTitle("Advertencia");
                                    builder.setMessage("La hora seleccionada ya está ocupada. Por favor, elija otro horario.");


                                    builder.setCancelable(false);
                                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                    btn_add_actividad.setEnabled(false); // Deshabilitar el botón de agregar cita
                                    return;
                                }
                            }
                        }
                    }
                }
                // El paciente no tiene cita para ese día y hora, habilitar el botón de agregar cita
                btn_add_actividad.setEnabled(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejo de errores de lectura de base de datos
            }
        });
    }


    private void crearCita() {
        dialog.ocultar_mensaje();
        Ob_citas obActividad = new Ob_citas();
        obActividad.estado = "Pendiente";
        obActividad.fecha_inicio = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(fecha_cal_ini);
        obActividad.hora_inicio = hora_time_inicio;
        obActividad.tipo = spinner_tipo.getSelectedItem().toString();
        obActividad.tipo_cita = spinner_tipo_cita.getSelectedItem().toString();
        obActividad.paciente = Principal.Nombre;
        obActividad.ced_usuario = Principal.Cedula;
        obActividad.uid_paciente = Principal.id;
        Ver_citas.ctlActividad.crear_cita(UID_EMPLEADO, obActividad);
        alertDialog.crear_mensaje("Correcto", "Cita Creada Correctamente", builder -> {
            builder.setCancelable(false);
            builder.setNeutralButton("Aceptar", (dialogInterface, i) -> finish());
            builder.create().show();
        });
    }

}