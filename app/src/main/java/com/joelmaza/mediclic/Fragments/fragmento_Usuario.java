package com.joelmaza.mediclic.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;




public class fragmento_Usuario extends Fragment {

    RecyclerView recyclerview_usuarios;
    Adaptador_usuarios lista_usuarios;
    DatabaseReference dbRef;
    Button btn_add_usuario;
    public static Ctl_usuario ctlUsuarios;
    EditText txt_buscador;
    Spinner spinner_rol;


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
        txt_buscador = vista.findViewById(R.id.txt_buscador);
        spinner_rol = vista.findViewById(R.id.spinner_rol);



        //metodo para ver usuario.

        lista_usuarios = new  Adaptador_usuarios(vista.getContext());
        ctlUsuarios = new Ctl_usuario();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerview_usuarios.setLayoutManager(linearLayoutManager);
        recyclerview_usuarios.setAdapter(lista_usuarios);

        ArrayAdapter<CharSequence> adapterspinner_rol = ArrayAdapter.createFromResource(vista.getContext(), R.array.rol1, android.R.layout.simple_spinner_item);
        adapterspinner_rol.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_rol.setAdapter(adapterspinner_rol);

        if(!Principal.id.isEmpty()) {
            MainActivity.ctlUsuario.VerUsuarios(dbRef,lista_usuarios, Principal.id , "Rol","", txt_existe, progressBar, txt_contador);


            spinner_rol.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    MainActivity.ctlUsuario.VerUsuarios(dbRef,lista_usuarios, Principal.id ,spinner_rol.getSelectedItem().toString(),txt_buscador.getText().toString(), txt_existe, progressBar, txt_contador);

                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}

            });
            txt_buscador.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    ocultar_teclado();
                    MainActivity.ctlUsuario.VerUsuarios(dbRef,lista_usuarios, Principal.id ,spinner_rol.getSelectedItem().toString(),txt_buscador.getText().toString(), txt_existe, progressBar, txt_contador);
                    return true;
                }
                return false;
            });
            btn_add_usuario = vista.findViewById(R.id.btn_add_usuario);
            if (Principal.rol.equals("Administrador")){
                btn_add_usuario.setVisibility(View.VISIBLE);

            }else{
                btn_add_usuario.setVisibility(View.GONE);
            }

            btn_add_usuario.setOnClickListener(v -> {

                startActivity(new Intent(vista.getContext(), Add_usuario.class));
            });
        }else{
        Toast.makeText(vista.getContext(), "Ocurrió un error al cargar el id",Toast.LENGTH_LONG).show();
    }









        return vista;


    }
    public void ocultar_teclado(){
        InputMethodManager inputMethodManager = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(Objects.requireNonNull(requireActivity().getCurrentFocus()).getWindowToken(), 0);

    }


}