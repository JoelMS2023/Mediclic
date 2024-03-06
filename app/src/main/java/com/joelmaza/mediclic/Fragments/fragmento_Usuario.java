package com.joelmaza.mediclic.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

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
    Adaptador_usuarios lista_usuarios;
    DatabaseReference dbRef;
    Button btn_add_usuario;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        final View vista= inflater.inflate(R.layout.fragment_usuarios,container,false);

        recyclerview_usuarios = (RecyclerView) vista.findViewById(R.id.recyclerview_usuarios);
        dbRef = MainActivity.DB.getReference();
        ProgressBar progressBar = vista.findViewById(R.id.progressBar);
        TextView txt_existe = vista.findViewById(R.id.txt_existe);
        TextView txt_contador = vista.findViewById(R.id.txt_contador);

        //metodo para ver usuario.

        lista_usuarios = new  Adaptador_usuarios(vista.getContext());

        btn_add_usuario = vista.findViewById(R.id.btn_add_usuario);
        if (Principal.rol.equals("Administrador")){
            btn_add_usuario.setVisibility(View.VISIBLE);

        }else{
            btn_add_usuario.setVisibility(View.GONE);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerview_usuarios.setLayoutManager(linearLayoutManager);
        recyclerview_usuarios.setAdapter(lista_usuarios);

        MainActivity.ctlUsuario.verUsuarios(dbRef,"",lista_usuarios, Principal.id,txt_existe,progressBar, txt_contador);


        btn_add_usuario.setOnClickListener(v -> {

            startActivity(new Intent(vista.getContext(), Add_usuario.class));
        });

        /*
        dbRef.child("usuarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){

                    lista_usuarios.Clear();

                    for (DataSnapshot datos: snapshot.getChildren()) {
                        Usuario user = new Usuario();
                        user.uid = datos.getKey();

                        Log.e("PRUEBA", user.uid.toString());

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

                            lista_usuarios.Add_usuarios(user);

                        }

                    }

                    lista_usuarios.notifyDataSetChanged();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        */
        return vista;

    }
}