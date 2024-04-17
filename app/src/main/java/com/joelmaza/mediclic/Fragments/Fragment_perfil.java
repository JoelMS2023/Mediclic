package com.joelmaza.mediclic.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.glance.ImageProvider;

import com.bumptech.glide.Glide;
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
import com.joelmaza.mediclic.Vi_fotos;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public class Fragment_perfil extends Fragment {
    Button btn_salir, btn_update_profile;
    TextView txt_nombre, txt_cedula,txt_rol, TextEmail, txt_estado,cant_solicitudes,txtfecha_ini_contrato;
    EditText editxt_direccion, editTextTextPhone, editTextTextClave;
    Progress_dialog dialog;
    ImageView img_perfil;
    Alert_dialog alertDialog;
    DatabaseReference dbReference;
    String URL_FOTO = "", NOMBRE = "", clave ="";

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
        txt_estado= vista.findViewById(R.id.txt_estado);
        editTextTextClave = vista.findViewById(R.id.editTextTextClave);
        btn_update_profile = vista.findViewById(R.id.btn_update_profile);
        cant_solicitudes = vista.findViewById(R.id.cant_solicitudes);
        txtfecha_ini_contrato = vista.findViewById(R.id.txtfecha_ini_contrato);


        dbReference = MainActivity.DB.getReference();

        FirebaseUser usuario = MainActivity.mAuth.getCurrentUser();

        if (usuario != null){

            TextEmail.setText(usuario.getEmail());

            MainActivity.ctlUsuario.Obtener_usuario(dbReference,MainActivity.mAuth.getUid(), user -> {

                txt_nombre.setText(user.nombre.trim());
                txt_rol.setText(user.rol.toUpperCase().trim());
                txt_cedula.setText(user.cedula);
                editxt_direccion.setText(user.direccion);
                TextEmail.setText(user.email);
                editTextTextPhone.setText(user.telefono);
                editTextTextClave.setText((user.clave));


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

            btn_update_profile.setOnClickListener(view -> {

                dialog.mostrar_mensaje("Actualizando Perfil...");

                if(!TextEmail.getText().toString().isEmpty() && TextEmail.getError() == null
                        && !editTextTextPhone.getText().toString().isEmpty()  && editTextTextPhone.getError() == null
                        && !editxt_direccion.getText().toString().isEmpty()
                        && !editTextTextClave.getText().toString().isEmpty()){

                    Usuario user = new Usuario();
                    user.uid = Principal.id;
                    user.email = TextEmail.getText().toString();
                    user.telefono = editTextTextPhone.getText().toString();
                    user.clave = editTextTextClave.getText().toString();
                    update_perfil(user);

                    dialog.ocultar_mensaje();
                    alertDialog.crear_mensaje("Correcto", "Usuario Actualizado Correctamente", builder -> {
                        builder.setCancelable(false);
                        builder.setNeutralButton("Aceptar", (dialogInterface, i) -> {});
                        builder.create().show();
                    });

                }else{
                    dialog.ocultar_mensaje();
                    alertDialog.crear_mensaje("¡Advertencia!", "Completa todos los campos", builder -> {
                        builder.setCancelable(true);
                        builder.setNeutralButton("Aceptar", (dialogInterface, i) -> {});
                        builder.create().show();
                    });
                }
            });

            editTextTextPhone.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if(editable.toString().trim().length() == 10) {
                        if (!validar_celular(editable.toString().trim())) {
                            editTextTextPhone.setError("Ingresa un celular válido");
                        }
                    }else{
                        editTextTextPhone.setError("Ingresa 10 dígitos");
                    }
                }
            });

            txt_rol.setText(Principal.rol);
            Principal.databaseReference.child("usuarios").child(Principal.id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(snapshot.exists()){

                        if(snapshot.child("cedula").exists()) {
                            txt_cedula.setText(Objects.requireNonNull(snapshot.child("cedula").getValue()).toString());
                        }
                        if(snapshot.child("estado").exists()) {
                            txt_estado.setText(Objects.requireNonNull(snapshot.child("estado").getValue()).toString());
                            switch (txt_estado.getText().toString().toLowerCase()){
                                case "activo":
                                    txt_estado.setTextColor(ContextCompat.getColor(vista.getContext(),R.color.success));
                                    break;
                                case "inactivo":
                                    txt_estado.setTextColor(ContextCompat.getColor(vista.getContext(),R.color.danger));
                                    break;
                                default:
                                    txt_estado.setTextColor(ContextCompat.getColor(vista.getContext(),R.color.proyecto_night));
                                    break;
                            }
                        }
                        if(snapshot.child("nombre").exists()) {
                            NOMBRE = Objects.requireNonNull(snapshot.child("nombre").getValue()).toString();
                            txt_nombre.setText(NOMBRE);
                        }
                        if(snapshot.child("url_foto").exists()){
                            URL_FOTO = Objects.requireNonNull(snapshot.child("url_foto").getValue()).toString();
                            Glide.with(vista.getContext()).load(URL_FOTO).centerCrop().into(img_perfil);
                        }else{
                            img_perfil.setImageResource(R.drawable.perfil);
                        }
                        if(snapshot.child("clave").exists()){
                            editTextTextClave.setText(Objects.requireNonNull(snapshot.child("clave").getValue()).toString());
                            clave = Objects.requireNonNull(snapshot.child("clave").getValue()).toString();
                        }
                        if(snapshot.child("telefono").exists()){
                            editTextTextPhone.setText(Objects.requireNonNull(snapshot.child("telefono").getValue()).toString().trim());
                        }
                        if(snapshot.child("fecha_ini_contrato").exists()){
                            txtfecha_ini_contrato.setText(Objects.requireNonNull(snapshot.child("fecha_ini_contrato").getValue()).toString());
                        }
                        if(snapshot.child("citas").exists()){
                            cant_solicitudes.setText(snapshot.child("citas").getChildrenCount()+" Citas");
                        }


                    }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        img_perfil.setOnClickListener(view -> {

            alertDialog.crear_mensaje("Información", "Selecciona una opción", builder -> {

                if(!URL_FOTO.isEmpty()) {

                    builder.setPositiveButton("Ver Foto", (dialogInterface, i) -> {

                        startActivity(new Intent(getContext(), Vi_fotos.class)
                                .putExtra("url", URL_FOTO)
                                .putExtra("titulo", NOMBRE));

                    });
                    builder.setNeutralButton("Subir Foto", (dialogInterface, i) -> {


                    });

                    builder.setCancelable(true);
                    builder.create().show();

                }else{




                }

            });

        });






        return vista;

    }
    public void update_perfil(Usuario usuario) {

        if(usuario.uid != null) {
            Map<String, Object> datos = new HashMap<>();
            datos.put("email", usuario.email.toLowerCase());
            datos.put("telefono", usuario.telefono);

            if(!clave.equals(usuario.clave)){
                if(!Principal.preferences.getString("uid_biometric","").isEmpty()){
                    SharedPreferences.Editor editor = Principal.preferences.edit();
                    editor.putString("uid_biometric","");
                    editor.apply();
                }
                datos.put("clave", usuario.clave);
            }

            Principal.databaseReference.child("usuarios").child(usuario.uid).updateChildren(datos);


        }

    }
    public boolean validar_celular(String celular){

        Pattern patron = Pattern.compile("^(0|593)?9[0-9]\\d{7}$");

        return patron.matcher(celular).matches();

    }

    public boolean validar_correo(String correo){

        Pattern patron = Pattern.compile("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.([a-zA-Z]{2,4})+$");

        return patron.matcher(correo).matches();

    }


}



