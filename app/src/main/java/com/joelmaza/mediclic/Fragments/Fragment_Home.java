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

import com.joelmaza.mediclic.Doctores.Ver_doctores;
import com.joelmaza.mediclic.Horarios.Ver_horarios;
import com.joelmaza.mediclic.R;

public class Fragment_Home extends Fragment {
    CardView card_horario, card_agendamiento, card_doctores;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_home, container, false);


        card_horario = (CardView) vista.findViewById(R.id.card_horario);
        card_agendamiento=(CardView)vista.findViewById(R.id.card_agendamiento) ;
        card_doctores=(CardView)vista.findViewById(R.id.card_doctores) ;



        card_horario.setOnClickListener(view -> {
            startActivity(new Intent(vista.getContext(), Ver_horarios.class));

        });
        card_doctores.setOnClickListener(view -> {
            startActivity(new Intent(vista.getContext(), Ver_doctores.class));

        });




        return vista;
    }

}
