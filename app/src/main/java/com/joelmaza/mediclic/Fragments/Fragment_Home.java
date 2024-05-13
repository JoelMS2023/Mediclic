package com.joelmaza.mediclic.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.joelmaza.mediclic.Citas.Ver_citas;
import com.joelmaza.mediclic.Doctores.Ver_doctores;
import com.joelmaza.mediclic.Horarios.Ver_horarios;
import com.joelmaza.mediclic.Login;
import com.joelmaza.mediclic.MainActivity;
import com.joelmaza.mediclic.Marcacion.Ver_marcaciones;
import com.joelmaza.mediclic.Principal;
import com.joelmaza.mediclic.R;
import com.joelmaza.mediclic.Reportes.Ver_reportes;
import com.joelmaza.mediclic.Solicitudes.Ver_solicitud;
import com.joelmaza.mediclic.Tienda.Ver_tienda;
import com.joelmaza.mediclic.Tratamientos.Ver_tratamientos;
import com.joelmaza.mediclic.Ubicacion;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.TimerTask;

public class Fragment_Home extends Fragment {

    CardView card_horario, card_marcacion, card_agendamiento, card_doctores, card_reportes, card_gps, card_tratamientos,card_solicitudes,card_tienda;
    TextView correo_home,nombre_home;
    ProgressBar progressBardatos;
    DatabaseReference usuarios;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_home, container, false);

        card_marcacion = (CardView) vista.findViewById(R.id.card_marcacion);
        card_horario = (CardView) vista.findViewById(R.id.card_horario);
        card_agendamiento = (CardView) vista.findViewById(R.id.card_agendamiento);
        card_doctores = (CardView) vista.findViewById(R.id.card_doctores);
        card_gps = (CardView) vista.findViewById(R.id.card_gps);
        card_reportes = (CardView) vista.findViewById(R.id.card_reportes);
        card_tratamientos = (CardView) vista.findViewById(R.id.card_tratamientos);
        card_solicitudes= (CardView) vista.findViewById(R.id.card_solicitudes);
        card_tienda=(CardView) vista.findViewById(R.id.card_tienda);
        correo_home= vista.findViewById(R.id.correo_home);
        nombre_home= vista.findViewById(R.id.nombre_home);
        progressBardatos=vista.findViewById(R.id.progressBardatos);
        usuarios= FirebaseDatabase.getInstance().getReference("usuarios");





        firebaseAuth =FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();


        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Inicio");
        }




        if (!Principal.rol.isEmpty()) {
            if (Principal.rol.equals("Administrador") || Principal.rol.equals("Doctor")) {
                card_marcacion.setVisibility(View.VISIBLE);
            } else {
                card_marcacion.setVisibility(View.GONE);
            }
        }
        card_marcacion.setOnClickListener(view -> {
            startActivity(new Intent(vista.getContext(), Ver_marcaciones.class));

        });
        if (!Principal.rol.isEmpty()) {
            if (Principal.rol.equals("Administrador")) {
                card_tienda.setVisibility(View.VISIBLE);
            } else {
                card_tienda.setVisibility(View.GONE);
            }
        }
        card_marcacion.setOnClickListener(view -> {
            startActivity(new Intent(vista.getContext(), Ver_marcaciones.class));

        });
        card_tienda.setOnClickListener(view -> {
            startActivity(new Intent(vista.getContext(), Ver_tienda.class));
        });


        card_horario.setOnClickListener(view -> {
            startActivity(new Intent(vista.getContext(), Ver_horarios.class));

        });
        if (!Principal.rol.isEmpty()) {
            if (Principal.rol.equals("Administrador") || Principal.rol.equals("Paciente")) {
                card_doctores.setVisibility(View.VISIBLE);
            } else {
                card_doctores.setVisibility(View.GONE);
            }
        }
        card_doctores.setOnClickListener(view -> {
            startActivity(new Intent(vista.getContext(), Ver_doctores.class));

        });
        card_agendamiento.setOnClickListener(view -> {
            startActivity(new Intent(vista.getContext(), Ver_citas.class));
        });
        if (!Principal.rol.isEmpty()) {
            if (Principal.rol.equals("Administrador")) {
                card_reportes.setVisibility(View.VISIBLE);
            } else {
                card_reportes.setVisibility(View.GONE);
            }
        }
        card_reportes.setOnClickListener(view -> {
            startActivity(new Intent(vista.getContext(), Ver_reportes.class));
        });

        if (!Principal.rol.isEmpty()) {
            if (Principal.rol.equals("Administrador") || Principal.rol.equals("Paciente")) {
                card_tratamientos.setVisibility(View.VISIBLE);
            } else {
                card_tratamientos.setVisibility(View.GONE);
            }
        }
        card_tratamientos.setOnClickListener(view -> {
            startActivity(new Intent(vista.getContext(), Ver_tratamientos.class));
        });

        if (!Principal.rol.isEmpty()) {
            if (Principal.rol.equals("Administrador") || Principal.rol.equals("Paciente")) {
                card_gps.setVisibility(View.VISIBLE);
            } else {
                card_gps.setVisibility(View.GONE);
            }
        }
        card_gps.setOnClickListener(view -> {
            startActivity(new Intent(vista.getContext(), Ubicacion.class));
        });

        card_solicitudes.setOnClickListener(view -> {
            startActivity(new Intent(vista.getContext(), Ver_solicitud.class));

        });


        return vista;


    }

    @Override
    public void onStart() {
        Comprobarinicio();
        super.onStart();
    }

    private void  Cargardatos(){
        usuarios.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    progressBardatos.setVisibility(View.GONE);

                    nombre_home.setVisibility(View.VISIBLE);
                    correo_home.setVisibility(View.VISIBLE);

                    String nombre=""+snapshot.child("nombre").getValue();
                    String rol=""+snapshot.child("rol").getValue();

                    nombre_home.setText(nombre);
                    correo_home.setText((rol));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void Comprobarinicio() {
        if (user != null) {
            Cargardatos();
        } else {
            // Si el usuario es nulo, iniciar la actividad de Login
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);

            // Cerrar la actividad actual (si es necesario)
            getActivity().finish();
        }
    }

}




