package com.joelmaza.mediclic.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import com.joelmaza.mediclic.Adaptadores.Adaptador_usuarios;
import com.joelmaza.mediclic.Controllers.Ctl_usuario;
import com.joelmaza.mediclic.MainActivity;
import com.joelmaza.mediclic.Objetos.Usuario;
import com.joelmaza.mediclic.Principal;
import com.joelmaza.mediclic.R;
import com.joelmaza.mediclic.Registro;
import com.joelmaza.mediclic.Usuarios.Add_usuario;

import java.util.Objects;




public class fragmento_Usuario extends Fragment {

    RecyclerView recyclerview_usuarios;
    Adaptador_usuarios adaptadorUsuarios;
    DatabaseReference dbRef;
    Button btn_add_usuario;
    Ctl_usuario ctlUsuarios;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        final View vista= inflater.inflate(R.layout.fragment_usuarios,container,false);

        recyclerview_usuarios = (RecyclerView) vista.findViewById(R.id.recyclerview_usuarios);
        dbRef = MainActivity.DB.getReference();
        adaptadorUsuarios = new Adaptador_usuarios(getContext());

        btn_add_usuario = vista.findViewById(R.id.btn_add_usuario);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerview_usuarios.setLayoutManager(linearLayoutManager);
        recyclerview_usuarios.setAdapter(adaptadorUsuarios);
        ctlUsuarios = new Ctl_usuario(Principal.databaseReference);

        btn_add_usuario.setOnClickListener(v -> {

            startActivity(new Intent(vista.getContext(), Add_usuario.class));
        });

        dbRef.child("usuarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){

                    adaptadorUsuarios.Clear();

                    for (DataSnapshot datos: snapshot.getChildren()) {
                        Usuario user = new Usuario();
                        user.uid = datos.getKey();

                        if(!MainActivity.mAuth.getUid().equals(user.uid)){

                            user.nombre = Objects.requireNonNull(datos.child("nombre").getValue()).toString();

                            if(datos.child("url_foto").exists()){
                                user.url_foto = Objects.requireNonNull(datos.child("url_foto").getValue()).toString();
                            }

                            if(datos.child("telefono").exists()){
                                user.telefono = Objects.requireNonNull(datos.child("telefono").getValue()).toString();
                            }
                            if(datos.child("direccion").exists()){
                                user.direccion = Objects.requireNonNull(datos.child("direccion").getValue()).toString();
                            }
                            if(datos.child("email").exists()){
                                user.direccion = Objects.requireNonNull(datos.child("email").getValue()).toString();
                            }

                            adaptadorUsuarios.Add_usuarios(user);

                        }

                    }

                    adaptadorUsuarios.notifyDataSetChanged();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //ctlUsuarios.Vi_det_usuario(adapterUsuario,"",Principal.id, txt_sinresultados, progressBar, txt_contador);

        return vista;

    }
}