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


    public void crear_tratamientos(Ob_tratamientos obtratamientos) {

        dbref.child("Tratamientos").push().setValue(obtratamientos);


    }
    public void actualizar_tratamientos(DatabaseReference dbref, Ob_tratamientos obtratamientos){

        Map<String, Object> datos = new HashMap<>();
        datos.put("nombre", obtratamientos.nombre);
        datos.put("estado",obtratamientos.estado);
        datos.put("mensaje",obtratamientos.mensaje);

        dbref.child("Tratamientos").child(obtratamientos.uid).updateChildren(datos);

    }
    public void eliminar_tratamientos(DatabaseReference dbref, String uid_tratamiento){

        dbref.child("Tratamientos").child(uid_tratamiento).removeValue();

    }
    public void Vertratamientos(Adapter_tratamientos list_tratamientos, final TextView textView, final ProgressBar progressBar, TextView txt_contador) {

        progressBar.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);

        dbref.child("Tratamientos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    list_tratamientos.ClearActividad();
                    int contador = 0;

                    if (dataSnapshot.child("Tratamientos").exists()) {

                        for (DataSnapshot datos : dataSnapshot.child("Tratamientos").getChildren()) {

                            Ob_tratamientos actividad = new Ob_tratamientos();
                            actividad.uid = datos.getKey();

                            if (datos.child("mensaje").exists()) {
                                actividad.mensaje = Objects.requireNonNull(datos.child("mensaje").getValue()).toString();
                            }
                            if (datos.child("nombre").exists()) {
                                actividad.nombre = Objects.requireNonNull(datos.child("nombre").getValue()).toString();
                            }
                            if (datos.child("estado").exists()) {
                                actividad.mensaje = Objects.requireNonNull(datos.child("estado").getValue()).toString();
                            }

                            actividad.uid_tratamiento = dataSnapshot.getKey();
                            list_tratamientos.AddActividad(actividad);
                            contador++;

                        }

                    }

                    txt_contador.setText(contador + " Tratamientos");
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

}
