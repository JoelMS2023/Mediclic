package com.joelmaza.mediclic.Controllers;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.joelmaza.mediclic.Adaptadores.Adapter_tratamientos;
import com.joelmaza.mediclic.Objetos.Ob_citas;
import com.joelmaza.mediclic.Objetos.Ob_tratamientos;
import com.joelmaza.mediclic.Objetos.Usuario;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Ctl_tratamientos {

    DatabaseReference dbref;

    public Ctl_tratamientos(DatabaseReference dbref) {
        this.dbref = dbref;
    }


    public void crear_tratamientos(String uid_tratamiento, Ob_tratamientos obtratamientos) {

        dbref.child("Tratamientos").child(uid_tratamiento).setValue(obtratamientos);


    }
    public void actualizar_tratamientos(DatabaseReference dbref, Ob_tratamientos obtratamientos){

        Map<String, Object> datos = new HashMap<>();
        datos.put("nombre", obtratamientos.nombre);
        datos.put("estado",obtratamientos.estado);
        datos.put("mensaje",obtratamientos.mensaje);
        datos.put("empleado",obtratamientos.empleado);

        dbref.child("Tratamientos").child(obtratamientos.uid).updateChildren(datos);

    }
    public void eliminar_tratamientos(DatabaseReference dbref, String uid_tratamiento){

        dbref.child("Tratamientos").child(uid_tratamiento).removeValue();

    }
    public void Vertratamientos(Adapter_tratamientos list_tratamientos, final TextView textView, final ProgressBar progressBar, TextView txt_contador) {

        progressBar.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);

        dbref.child("usuarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    list_tratamientos.ClearActividad();
                    int contador = 0;

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        if (snapshot.child("Tratamientos").exists()) {

                            for (DataSnapshot datos : snapshot.child("tratamientos").getChildren()) {

                                Ob_tratamientos actividad = new Ob_tratamientos();
                                actividad.uid = datos.getKey();

                                if (datos.child("fecha_inicio").exists()) {
                                    actividad.fecha_inicio = Objects.requireNonNull(datos.child("fecha_inicio").getValue()).toString();
                                }
                                if (datos.child("hora_inicio").exists()) {
                                    actividad.hora_inicio = Objects.requireNonNull(datos.child("hora_inicio").getValue()).toString();
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


                                list_tratamientos.AddActividad(actividad);
                                contador++;

                            }

                        }

                    }

                    txt_contador.setText(contador + " tratamientos");
                    progressBar.setVisibility(View.GONE);

                    textView.setVisibility(list_tratamientos.getItemCount() == 0 ? View.VISIBLE : View.GONE);

                    list_tratamientos.notifyDataSetChanged();

                } else {
                    list_tratamientos.ClearActividad();
                    list_tratamientos.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }
    public void Ver_my_tratamientos(Adapter_tratamientos list_actividad, String uid, final TextView textView, final ProgressBar progressBar, TextView txt_contador) {

        progressBar.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);

        dbref.child("usuarios").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    list_actividad.ClearActividad();
                    int contador = 0;

                    if (dataSnapshot.child("tratamientos").exists()) {

                        for (DataSnapshot snapshot : dataSnapshot.child("tratamientos").getChildren()) {

                            Ob_tratamientos actividad = new Ob_tratamientos();
                            actividad.uid = snapshot.getKey();

                            if (snapshot.child("fecha_inicio").exists()) {
                                actividad.fecha_inicio = Objects.requireNonNull(snapshot.child("fecha_inicio").getValue()).toString();
                            }
                            if (snapshot.child("hora_inicio").exists()) {
                                actividad.hora_inicio = Objects.requireNonNull(snapshot.child("hora_inicio").getValue()).toString();
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

                    txt_contador.setText(contador + " tratamientos");
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
