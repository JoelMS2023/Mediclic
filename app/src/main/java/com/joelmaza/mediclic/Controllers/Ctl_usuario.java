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
import java.util.regex.Pattern;


public class Ctl_usuario {
    DatabaseReference dbref;


    public Ctl_usuario() {
    }

    public void crear_usuario(DatabaseReference dbref, String uid, Usuario usuario){

        dbref.child("usuarios").child(uid).setValue(usuario);

    }


    public void actualizar_usuario(DatabaseReference dbref, Usuario usuario){

        Map<String, Object> datos = new HashMap<>();
        datos.put("direccion", usuario.direccion);
        datos.put("telefono",usuario.telefono);
        datos.put("email",usuario.email);
        datos.put("cedula",usuario.cedula);

        dbref.child("usuarios").child(usuario.uid).updateChildren(datos);

    }

    public void eliminar_usuario(DatabaseReference dbref, String uid_usuario){

        dbref.child("usuarios").child(uid_usuario).removeValue();

    }

    public void Obtener_usuario(DatabaseReference dbref, String uid_usuario, Interfaces.perfil perfil) {

        dbref.child("usuarios").child(uid_usuario).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    Usuario user = new Usuario();

                    if (snapshot.child("nombre").exists()) {
                        user.nombre = Objects.requireNonNull(snapshot.child("nombre").getValue()).toString();
                    }
                    if (snapshot.child("email").exists()) {
                        user.email = Objects.requireNonNull(snapshot.child("email").getValue()).toString();
                    }

                    if (snapshot.child("rol").exists()) {
                        user.rol = Objects.requireNonNull(snapshot.child("rol").getValue()).toString();

                    }
                    if (snapshot.child("url_foto").exists()) {
                        user.url_foto = Objects.requireNonNull(snapshot.child("url_foto").getValue()).toString();
                    }
                    if (snapshot.child("cedula").exists()) {
                        user.cedula = Objects.requireNonNull(snapshot.child("cedula").getValue()).toString();
                    }
                    if (snapshot.child("telefono").exists()) {
                        user.telefono = Objects.requireNonNull(snapshot.child("telefono").getValue()).toString();
                    }

                    perfil.verPerfil(user);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

        public boolean Validar_Cedula(String cedula){

            int suma = 0;

            for (int i = 0; i < 9; i++)
            {
                int coeficiente = ((i % 2) == 0) ? 2 : 1;
                int calculo = Integer.parseInt(String.valueOf(cedula.charAt(i))) * coeficiente;
                suma += (calculo >= 10) ? calculo - 9 : calculo;
            }

            int residuo = suma % 10;
            int valor = (residuo == 0) ? 0 : (10 - residuo);

            return Integer.parseInt(String.valueOf(cedula.charAt(9))) == valor;

        }

        public boolean validar_correo(String correo){

            Pattern patron = Pattern.compile("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.([a-zA-Z]{2,4})+$");

            return patron.matcher(correo).matches();

        }

        public boolean validar_usuario(String usuario){

            Pattern patron = Pattern.compile("^[ a-zA-Z]+$");

            return patron.matcher(usuario).matches();

        }

        public boolean validar_celular(String celular){

            Pattern patron = Pattern.compile("^(0|593)?9[0-9]\\d{7}$");

            return patron.matcher(celular).matches();

        }

    }
