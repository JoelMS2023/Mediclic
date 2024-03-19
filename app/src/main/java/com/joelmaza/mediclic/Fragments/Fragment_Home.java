package com.joelmaza.mediclic.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.joelmaza.mediclic.Citas.Add_citas;
import com.joelmaza.mediclic.Citas.Ver_citas;
import com.joelmaza.mediclic.Doctores.Ver_doctores;
import com.joelmaza.mediclic.Horarios.Ver_horarios;
import com.joelmaza.mediclic.Marcacion.Ver_marcaciones;
import com.joelmaza.mediclic.Principal;
import com.joelmaza.mediclic.R;
import com.joelmaza.mediclic.Reportes.Ver_reportes;
import com.joelmaza.mediclic.Tratamientos.Add_Tratamientos;
import com.joelmaza.mediclic.Tratamientos.Ver_tratamientos;
import com.joelmaza.mediclic.Ubicacion;

public class Fragment_Home extends Fragment {
    CardView card_horario, card_marcacion, card_agendamiento, card_doctores, card_reportes,card_gps,card_tratamientos;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_home, container, false);

        card_marcacion = (CardView) vista.findViewById(R.id.card_marcacion);
        card_horario = (CardView) vista.findViewById(R.id.card_horario);
        card_agendamiento=(CardView)vista.findViewById(R.id.card_agendamiento) ;
        card_doctores=(CardView)vista.findViewById(R.id.card_doctores) ;
        card_gps=(CardView)vista.findViewById(R.id.card_gps) ;
        card_reportes=(CardView)vista.findViewById(R.id.card_reportes) ;
        card_tratamientos=(CardView)vista.findViewById(R.id.card_tratamientos);

        card_marcacion.setOnClickListener(v -> {
            startActivity(new Intent(vista.getContext(), Ver_marcaciones.class));
        });

        card_horario.setOnClickListener(view -> {
            startActivity(new Intent(vista.getContext(), Ver_horarios.class));

        });
        card_doctores.setOnClickListener(view -> {
            startActivity(new Intent(vista.getContext(), Ver_doctores.class));

        });

        card_agendamiento.setOnClickListener(view -> {
            startActivity(new Intent(vista.getContext(), Ver_citas.class));
        });
        card_reportes.setOnClickListener(view -> {
            startActivity(new Intent(vista.getContext(), Ver_reportes.class));
        });
        card_tratamientos.setOnClickListener(view -> {
            startActivity(new Intent(vista.getContext(), Ver_tratamientos.class));
        });
        card_gps.setOnClickListener(view -> {
            startActivity(new Intent(vista.getContext(), Ubicacion.class));
        });
        if (!Principal.rol.isEmpty()) {
            if (Principal.rol.equals("Administrador") || Principal.rol.equals("Paciente")) {
                card_doctores.setVisibility(View.VISIBLE);
            } else {
                card_doctores.setVisibility(View.GONE);
            }
        }


        return vista;
    }

}
