package com.joelmaza.mediclic.Usuarios;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.joelmaza.mediclic.Controllers.Alert_dialog;
import com.joelmaza.mediclic.Controllers.Progress_dialog;
import com.joelmaza.mediclic.MainActivity;
import com.joelmaza.mediclic.Objetos.Usuario;
import com.joelmaza.mediclic.Principal;
import com.joelmaza.mediclic.R;


import com.joelmaza.mediclic.Fragments.fragmento_Usuario;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class Add_usuario extends AppCompatActivity {

    EditText editTextcedula, editTextnombre, editTextTextEmailAddress, editTextTextPhone;
    Spinner spinner_tipo;
    Button btn_add_usuario;
    ArrayAdapter<CharSequence> adapterspinner_tipo;
    Alert_dialog alertDialog;
    Progress_dialog dialog;
    CalendarView fecha_inicio;
    long fecha_cal_ini;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_usuario);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setOnClickListener(view -> finish());

        editTextcedula = findViewById(R.id.editTextcedula);
        editTextnombre = findViewById(R.id.editTextnombre);
        editTextTextEmailAddress = findViewById(R.id.editTextTextEmailAddress);
        editTextTextPhone = findViewById(R.id.editTextTextPhone);
        spinner_tipo = findViewById(R.id.spinner_tipo);
        btn_add_usuario = findViewById(R.id.btn_add_usuario);



        Date dia = new Date();
        fecha_inicio = findViewById(R.id.fecha_inicio);
        fecha_cal_ini = dia.getTime();

        dialog = new Progress_dialog(this);
        alertDialog = new Alert_dialog(this);

        adapterspinner_tipo = ArrayAdapter.createFromResource(this, R.array.rol, android.R.layout.simple_spinner_item);
        adapterspinner_tipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_tipo.setAdapter(adapterspinner_tipo);

        fecha_inicio.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year,month,dayOfMonth);
            view.setDate(calendar.getTimeInMillis());
            fecha_cal_ini = view.getDate();
        });





        editTextcedula.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(editable.toString().trim().length() == 10){
                    if(!MainActivity.ctlUsuario.Validar_Cedula(editable.toString().trim())){
                        editTextcedula.setError("Cédula Incorrecta");
                    }
                }else{
                    editTextcedula.setError("Ingresa 10 dígitos");
                }

            }
        });

        editTextnombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!MainActivity.ctlUsuario.validar_usuario(editable.toString().trim())){
                    editTextnombre.setError("Ingresa un nombre válido");
                }
            }
        });

        editTextTextEmailAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!MainActivity.ctlUsuario.validar_correo(editable.toString().trim())){
                    editTextTextEmailAddress.setError("Ingresa un correo válido");
                }
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
                    if (!MainActivity.ctlUsuario.validar_celular(editable.toString().trim())) {
                        editTextTextPhone.setError("Ingresa un celular válido");
                    }
                }else{
                    editTextTextPhone.setError("Ingresa 10 dígitos");
                }
            }
        });

        btn_add_usuario.setOnClickListener(view -> {

            dialog.mostrar_mensaje("Creando Usuario...");

            if(!editTextcedula.getText().toString().trim().isEmpty() && editTextcedula.getError() == null &&
                    !editTextnombre.getText().toString().trim().isEmpty() && editTextnombre.getError() == null  &&
                    !editTextTextEmailAddress.getText().toString().trim().isEmpty() && editTextTextEmailAddress.getError() == null &&
                    !editTextTextPhone.getText().toString().trim().isEmpty() &&  !spinner_tipo.getSelectedItem().toString().equals("Selecciona")
                    ) {

                Usuario usuario = new Usuario();
                usuario.cedula = editTextcedula.getText().toString();
                usuario.nombre = editTextnombre.getText().toString();
                usuario.email = editTextTextEmailAddress.getText().toString();
                usuario.telefono = editTextTextPhone.getText().toString();
                usuario.rol = spinner_tipo.getSelectedItem().toString();
                usuario.fecha_ini_contrato = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(fecha_cal_ini);
                usuario.clave = usuario.cedula; //preguntarle al profesor

                FirebaseUser actual = MainActivity.mAuth.getCurrentUser();

                MainActivity.mAuth.createUserWithEmailAndPassword(usuario.email,usuario.clave).addOnCompleteListener(task -> {

                    if(task.isSuccessful()){

                        usuario.uid = Objects.requireNonNull(task.getResult().getUser()).getUid();

                        if(!usuario.uid.isEmpty()){

                            MainActivity.ctlUsuario.crear_usuario(Principal.databaseReference, usuario.uid,usuario);

                            MainActivity.mAuth.updateCurrentUser(actual);

                            dialog.ocultar_mensaje();
                            alertDialog.crear_mensaje("Correcto", "Usuario Creado Correctamente", builder -> {
                                builder.setCancelable(false);
                                builder.setNeutralButton("Aceptar", (dialogInterface, i) -> finish());
                                builder.create().show();
                            });

                        }else{
                            alertDialog.crear_mensaje("Error", "Usuario No creado", builder -> {
                                builder.setCancelable(false);
                                builder.setNeutralButton("Aceptar", (dialogInterface, i) -> finish());
                                builder.create().show();
                            });
                        }

                    }

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


    }
}