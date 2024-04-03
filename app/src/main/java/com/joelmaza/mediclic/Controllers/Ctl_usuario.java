package com.joelmaza.mediclic.Controllers;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.joelmaza.mediclic.Adaptadores.Adaptador_usuarios;
import com.joelmaza.mediclic.Objetos.Usuario;
import com.joelmaza.mediclic.Principal;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;


public class Ctl_usuario {




    public Ctl_usuario() {
    }

    public void crear_usuario(DatabaseReference dbref, String uid, Usuario usuario){

        dbref.child("usuarios").child(uid).setValue(usuario);

    }

    public void eliminar_fecha_fin_contrato(DatabaseReference dbref,String uid){
        dbref.child("usuarios").child(uid).child("fecha_fin_contrato").removeValue();
    }


    public void actualizar_usuario(DatabaseReference dbref, Usuario usuario){

        Map<String, Object> datos = new HashMap<>();
        datos.put("direccion", usuario.direccion);
        datos.put("telefono",usuario.telefono);
        datos.put("email", usuario.email.toLowerCase());
        datos.put("cedula",usuario.cedula);
        datos.put("rol", usuario.rol);
        String estado = usuario.estado;
        if(usuario.fecha_ini_contrato != null){
            datos.put("fecha_ini_contrato", usuario.fecha_ini_contrato);
        }
        if(usuario.fecha_fin_contrato != null){
            datos.put("fecha_fin_contrato", usuario.fecha_fin_contrato);
            estado = "Inactivo";
        }else{
            estado = usuario.estado;
        }
        datos.put("estado", estado);

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
                    if (snapshot.child("direccion").exists()) {
                        user.direccion = Objects.requireNonNull(snapshot.child("direccion").getValue()).toString();
                    }
                    if (snapshot.child("estado").exists()) {
                        user.estado = Objects.requireNonNull(snapshot.child("estado").getValue()).toString();
                    }

                    perfil.verPerfil(user);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void Obtener_rol(DatabaseReference dbref,String uid, Interfaces.Obt_rol obtRol){

        dbref.child("usuarios").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    if(snapshot.child("rol").exists()){
                        obtRol.rol(Objects.requireNonNull(snapshot.child("rol").getValue()).toString());
                    }
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

        public void verUsuarios(DatabaseReference dbref,String rol,Adaptador_usuarios lista_usuarios, String uid, final TextView txt_existe, final ProgressBar progressBar,TextView txt_contador){
            progressBar.setVisibility(View.VISIBLE);
            txt_existe.setVisibility(View.VISIBLE);
            dbref.child("usuarios").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {

                        lista_usuarios.Clear();
                        int contador= 0;

                        for (DataSnapshot usuarios : snapshot.getChildren()){
                            if(!Objects.equals(usuarios.getKey(), uid)){
                                Usuario user =new Usuario();
                                user.uid =usuarios.getKey();

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
                                if (snapshot.child("direccion").exists()) {
                                    user.direccion = Objects.requireNonNull(snapshot.child("direccion").getValue()).toString();
                                }
                                if (snapshot.child("estado").exists()) {
                                    user.estado = Objects.requireNonNull(snapshot.child("estado").getValue()).toString();
                                }
                                if (usuarios.child("rol").exists()){

                                    if (usuarios.child("rol").getValue().toString().equalsIgnoreCase(rol) || rol.isEmpty()){
                                        lista_usuarios.Add_usuarios(user);
                                        contador++;

                                    }

                                }



                            }

                        }
                        txt_contador.setText(contador + "usuarios");
                        progressBar.setVisibility(View.GONE);
                        txt_existe.setVisibility(lista_usuarios.getItemCount() == 0 ? View.VISIBLE :View.GONE);
                        lista_usuarios.notifyDataSetChanged();
                    }else{
                        lista_usuarios.Clear();
                        lista_usuarios.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                        txt_existe.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }


    }
