package com.joelmaza.mediclic.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.joelmaza.mediclic.Controllers.Alert_dialog;
import com.joelmaza.mediclic.Controllers.Progress_dialog;
import com.joelmaza.mediclic.Login;
import com.joelmaza.mediclic.MainActivity;
import com.joelmaza.mediclic.Objetos.Usuario;
import com.joelmaza.mediclic.Principal;
import com.joelmaza.mediclic.R;

public class Fragment_perfil extends Fragment {
    Button btn_salir, btn_update_profile;
    TextView txt_nombre, txt_cedula,txt_rol, TextEmail ;
    EditText editxt_direccion, editTextTextPhone;
    Progress_dialog dialog;
    ImageView img_perfil;
    Alert_dialog alertDialog;
    private FirebaseUser usuario;
    DatabaseReference dbReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista =  inflater.inflate(R.layout.fragment_perfil,container,false);

        txt_nombre = vista.findViewById(R.id.txt_nombre);
        txt_cedula = vista.findViewById(R.id.txt_cedula);
        editxt_direccion=vista.findViewById(R.id.editxt_direccion);
        TextEmail = vista.findViewById(R.id.TextEmail);
        editTextTextPhone = vista.findViewById(R.id.editTextTextPhone);
        btn_salir = vista.findViewById(R.id.btn_salir);
        img_perfil = vista.findViewById(R.id.img_perfil);
        dialog = new Progress_dialog(vista.getContext());
        alertDialog = new Alert_dialog(vista.getContext());
        txt_rol = vista.findViewById(R.id.txt_rol);

        btn_update_profile = vista.findViewById(R.id.btn_update_profile);

        dbReference = MainActivity.DB.getReference();

        usuario = MainActivity.mAuth.getCurrentUser();

        if (usuario != null){

            TextEmail.setText(usuario.getEmail());

            MainActivity.ctlUsuario.Obtener_usuario(dbReference,MainActivity.mAuth.getUid(), user -> {

                txt_nombre.setText(user.nombre.trim());
                txt_rol.setText(user.rol.toUpperCase().trim());
                txt_cedula.setText(user.cedula);
                editxt_direccion.setText(user.direccion);
                TextEmail.setText(user.email);
                editTextTextPhone.setText(user.telefono);


            });
            btn_salir.setOnClickListener(view -> {

                alertDialog.crear_mensaje("Confirmacion","Estas Seguro que deseas salir",builder -> {
                    builder.setPositiveButton("Aceptar",(dialog1, which) -> {


                        MainActivity.mAuth.signOut();

                        SharedPreferences.Editor editor= Principal.preferences.edit();
                        editor.putString("uid","");
                        editor.putString("rol","");
                        editor.apply();
                        startActivity(new Intent(vista.getContext(), Login.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                        requireActivity().finish();

                    });

                    builder.setNegativeButton("Cancelar",(dialog1, which) -> {});
                    builder.setCancelable(false);
                    builder.create().show();
                });



            });
            btn_update_profile.setOnClickListener(v -> {


                String telefono = editTextTextPhone.getText().toString().trim();
                String direccion = editxt_direccion.getText().toString().trim();

                if(!telefono.isEmpty()){
                    Usuario user = new Usuario();
                    user.telefono = telefono;
                    user.direccion = direccion;
                    MainActivity.ctlUsuario.actualizar_usuario(usuario.getUid(),user);
                    Toast.makeText(vista.getContext(),"Usuario actualizado",Toast.LENGTH_SHORT).show();
                }else{

                    Toast.makeText(vista.getContext(),"Completa los campos requeridos",Toast.LENGTH_SHORT).show();
                }
            });




        }





        return vista;

    }


}



