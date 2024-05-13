package com.joelmaza.mediclic.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.joelmaza.mediclic.Adaptadores.Adaptador_usuarios;
import com.joelmaza.mediclic.Controllers.Ctl_usuario;
import com.joelmaza.mediclic.Principal;
import com.joelmaza.mediclic.R;
import com.joelmaza.mediclic.Usuarios.Add_usuario;

public class Dialog_Fragment_Usuarios extends DialogFragment {

    RecyclerView recyclerView;
    ProgressBar progressBar;
    TextView txt_sinresultados, txt_contador;
    Adaptador_usuarios adapterUsuario;
    Ctl_usuario ctlUsuarios;
    public static DialogFragment dialogFragment;
    Button btn_add;
    @Override
    public void onDetach() {
        super.onDetach();
        dialogFragment = null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View vista = inflater.inflate(R.layout.fragment_usuarios, null);
        builder.setView(vista);

        recyclerView = vista.findViewById(R.id.recyclerview_usuarios);
        txt_sinresultados = vista.findViewById(R.id.txt_existe);
        progressBar = vista.findViewById(R.id.progressBar);
        txt_contador = vista.findViewById(R.id.txt_contador);

        btn_add = vista.findViewById(R.id.btn_add_usuario);

        adapterUsuario = new Adaptador_usuarios(vista.getContext());
        ctlUsuarios = new Ctl_usuario();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(vista.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapterUsuario);

        btn_add.setOnClickListener(view -> {
            startActivity(new Intent(vista.getContext(), Add_usuario.class));
        });


        if (Principal.rol.equals("Administrador")){
            btn_add.setVisibility(View.VISIBLE);

        }else{
            btn_add.setVisibility(View.GONE);
        }

        ctlUsuarios.verUsuarios(Principal.databaseReference,"Doctor",adapterUsuario,Principal.id, txt_sinresultados, progressBar, txt_contador);

        dialogFragment = this;
        return builder.create();


    }
}