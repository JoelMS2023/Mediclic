package com.joelmaza.mediclic.Controllers;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.joelmaza.mediclic.Adaptadores.Adapter_citas;
import com.joelmaza.mediclic.Objetos.Ob_citas;
import com.joelmaza.mediclic.Objetos.Ob_horario;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Ctl_citas {

    DatabaseReference dbref;

    public Ctl_citas(DatabaseReference dbref) {
        this.dbref = dbref;
    }

    public void crear_cita(String uid_user, Ob_citas obCitas) {

        dbref.child("usuarios").child(uid_user).child("citas").push().setValue(obCitas);


    }

    public void eliminar_citas(String uid_user, String uid_cita) {
        dbref.child("usuarios").child(uid_user).child("citas").child(uid_cita).removeValue();

    }

    public void update_cita(String uid_user, Ob_citas obActividad) {

        if (obActividad.uid != null) {
            Map<String, Object> datos = new HashMap<>();
            datos.put("mensaje", obActividad.mensaje);
            datos.put("tipo", obActividad.tipo);
            datos.put("estado", obActividad.estado);
            datos.put("fecha_inicio", obActividad.fecha_inicio);
            datos.put("fecha_fin", obActividad.fecha_fin);
            datos.put("hora_inicio", obActividad.hora_inicio);
            datos.put("hora_fin", obActividad.hora_fin);
            dbref.child("usuarios").child(uid_user).child("citas").child(obActividad.uid).updateChildren(datos);
        }

    }

    public void VerActividades(Adapter_citas list_actividad, final TextView textView, final ProgressBar progressBar, TextView txt_contador) {

        progressBar.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);

        dbref.child("usuarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    list_actividad.ClearActividad();
                    int contador = 0;

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        if (snapshot.child("citas").exists()) {

                            for (DataSnapshot datos : snapshot.child("citas").getChildren()) {

                                Ob_citas actividad = new Ob_citas();
                                actividad.uid = datos.getKey();

                                if (datos.child("fecha_inicio").exists()) {
                                    actividad.fecha_inicio = Objects.requireNonNull(datos.child("fecha_inicio").getValue()).toString();
                                }
                                if (datos.child("hora_inicio").exists()) {
                                    actividad.hora_inicio = Objects.requireNonNull(datos.child("hora_inicio").getValue()).toString();
                                }
                                if (datos.child("fecha_fin").exists()) {
                                    actividad.fecha_fin = Objects.requireNonNull(datos.child("fecha_fin").getValue()).toString();
                                }
                                if (datos.child("hora_fin").exists()) {
                                    actividad.hora_fin = Objects.requireNonNull(datos.child("hora_fin").getValue()).toString();
                                }
                                if (datos.child("estado").exists()) {
                                    actividad.estado = Objects.requireNonNull(datos.child("estado").getValue()).toString();
                                }
                                if (datos.child("tipo").exists()) {
                                    actividad.tipo = Objects.requireNonNull(datos.child("tipo").getValue()).toString();
                                }
                                if (datos.child("mensaje").exists()) {
                                    actividad.mensaje = Objects.requireNonNull(datos.child("mensaje").getValue()).toString();
                                }
                                if (snapshot.child("nombre").exists()) {
                                    actividad.empleado = Objects.requireNonNull(snapshot.child("nombre").getValue()).toString();
                                }
                                if (snapshot.child("cedula").exists()) {
                                    actividad.ced_empleado = Objects.requireNonNull(snapshot.child("cedula").getValue()).toString();
                                }
                                actividad.uid_empleado = snapshot.getKey();


                                list_actividad.AddActividad(actividad);
                                contador++;

                            }

                        }


                    }
                    txt_contador.setText(contador + " Actividades");
                    progressBar.setVisibility(View.GONE);

                    textView.setVisibility(list_actividad.getItemCount() == 0 ? View.VISIBLE : View.GONE);

                    list_actividad.notifyDataSetChanged();

                } else {
                    list_actividad.ClearActividad();
                    list_actividad.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });


    }

    public void Ver_my_Actividades(Adapter_citas list_actividad, String uid, final TextView textView, final ProgressBar progressBar, TextView txt_contador) {

        progressBar.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);

        dbref.child("usuarios").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    list_actividad.ClearActividad();
                    int contador = 0;

                    if (dataSnapshot.child("citas").exists()) {

                        for (DataSnapshot snapshot : dataSnapshot.child("citas").getChildren()) {

                            Ob_citas actividad = new Ob_citas();
                            actividad.uid = snapshot.getKey();

                            if (snapshot.child("fecha_inicio").exists()) {
                                actividad.fecha_inicio = Objects.requireNonNull(snapshot.child("fecha_inicio").getValue()).toString();
                            }
                            if (snapshot.child("hora_inicio").exists()) {
                                actividad.hora_inicio = Objects.requireNonNull(snapshot.child("hora_inicio").getValue()).toString();
                            }
                            if (snapshot.child("fecha_fin").exists()) {
                                actividad.fecha_fin = Objects.requireNonNull(snapshot.child("fecha_fin").getValue()).toString();
                            }
                            if (snapshot.child("hora_fin").exists()) {
                                actividad.hora_fin = Objects.requireNonNull(snapshot.child("hora_fin").getValue()).toString();
                            }
                            if (snapshot.child("estado").exists()) {
                                actividad.estado = Objects.requireNonNull(snapshot.child("estado").getValue()).toString();
                            }
                            if (snapshot.child("tipo").exists()) {
                                actividad.tipo = Objects.requireNonNull(snapshot.child("tipo").getValue()).toString();
                            }
                            if (snapshot.child("mensaje").exists()) {
                                actividad.mensaje = Objects.requireNonNull(snapshot.child("mensaje").getValue()).toString();
                            }
                            if (dataSnapshot.child("nombre").exists()) {
                                actividad.empleado = Objects.requireNonNull(dataSnapshot.child("nombre").getValue()).toString();
                            }
                            if (dataSnapshot.child("cedula").exists()) {
                                actividad.ced_empleado = Objects.requireNonNull(dataSnapshot.child("cedula").getValue()).toString();
                            }
                            actividad.uid_empleado = dataSnapshot.getKey();


                            list_actividad.AddActividad(actividad);
                            contador++;

                        }

                    }

                    txt_contador.setText(contador + " citas");
                    progressBar.setVisibility(View.GONE);

                    if (list_actividad.getItemCount() == 0) {
                        textView.setVisibility(View.VISIBLE);
                    } else {
                        textView.setVisibility(View.GONE);
                    }

                    list_actividad.notifyDataSetChanged();

                } else {
                    list_actividad.ClearActividad();
                    list_actividad.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }
}
