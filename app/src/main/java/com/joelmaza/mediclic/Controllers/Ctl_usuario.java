package com.joelmaza.mediclic.Controllers;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.joelmaza.mediclic.Objetos.Usuario;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;



public class Ctl_usuario {
    DatabaseReference dbref;


    public Ctl_usuario(){}

    public void crear_usuario(DatabaseReference dbref, String uid, Usuario usuario){

        dbref.child("usuarios").child(uid).setValue(usuario);

    }


    public void actualizar_usuario(DatabaseReference dbref, Usuario usuario){

        Map<String, Object> datos = new HashMap<>();
        datos.put("direccion", usuario.direccion);
        datos.put("telefono",usuario.telefono);
        datos.put("email",usuario.email);

        dbref.child("usuarios").child(usuario.uid).updateChildren(datos);

    }

    public void eliminar_usuario(DatabaseReference dbref, String uid_usuario){

        dbref.child("usuarios").child(uid_usuario).removeValue();

    }

    public void Obtener_usuario(DatabaseReference dbref, String uid_usuario, Interfaces.perfil perfil){

        dbref.child("usuarios").child(uid_usuario).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){

                    Usuario user = new Usuario();

                    if(snapshot.child("nombre").exists()){
                        user.nombre = Objects.requireNonNull(snapshot.child("nombre").getValue()).toString();
                    }
                    if(snapshot.child("email").exists()) {
                        user.email = Objects.requireNonNull(snapshot.child("email").getValue()).toString();
                    }

                    if(snapshot.child("rol").exists()) {
                        user.rol = Objects.requireNonNull(snapshot.child("rol").getValue()).toString();

                    }
                    if(snapshot.child("url_foto").exists()) {
                        user.url_foto = Objects.requireNonNull(snapshot.child("url_foto").getValue()).toString();
                    }

                    perfil.verPerfil(user);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}
