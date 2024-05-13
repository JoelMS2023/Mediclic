package com.joelmaza.mediclic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.joelmaza.mediclic.Controllers.Alert_dialog;
import com.joelmaza.mediclic.Controllers.Progress_dialog;
import com.joelmaza.mediclic.Objetos.Usuario;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Registro extends AppCompatActivity {

    private EditText editText_email, editText_password,editText_nombre, editText_confpassword,editTextcedula;
    DatabaseReference dbRef;
    TextView TengounacuentaTXT;
    Progress_dialog dialog;
    Alert_dialog alertDialog;
    long fecha_cal_ini;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Button btn_registrarse = (Button) findViewById(R.id.btn_registrarse);
        editText_email = (EditText) findViewById(R.id.editText_email);
        editText_password = (EditText) findViewById(R.id.editText_password);
        editText_nombre = (EditText) findViewById(R.id.editText_nombre);
        editText_confpassword=(EditText) findViewById(R.id.editText_confpassword);
        editTextcedula= findViewById(R.id.editTextcedula);
        TengounacuentaTXT = findViewById(R.id.TengounacuentaTXT);
        dialog = new Progress_dialog(this);
        alertDialog = new Alert_dialog(this);

        dbRef = MainActivity.DB.getReference();

        Calendar calendar = Calendar.getInstance();
        Date fecha_cal_ini = calendar.getTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String fechaInicio = dateFormat.format(fecha_cal_ini);

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
        editText_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!MainActivity.ctlUsuario.validar_correo(editable.toString().trim())){
                    editText_email.setError("Ingresa un correo válido");
                }
            }
        });
        editText_nombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!MainActivity.ctlUsuario.validar_usuario(editable.toString().trim())){
                    editText_nombre.setError("Ingresa un nombre válido");
                }
            }
        });






        toolbar.setOnClickListener(view -> finish());
        btn_registrarse.setOnClickListener(view -> {
            dialog.mostrar_mensaje("Verificando datos...");
            if (!editTextcedula.getText().toString().trim().isEmpty() && editTextcedula.getText().toString().trim().matches("\\d{10}")) {
                if (!editText_email.getText().toString().trim().isEmpty()) {
                    if (!editText_password.getText().toString().trim().isEmpty()) {
                        if (!editText_confpassword.getText().toString().trim().isEmpty()) { // Verifica si el campo de confirmación de contraseña está vacío
                            if (editText_password.getText().toString().trim().length() >= 6) { // Verifica que la contraseña tenga al menos 6 caracteres
                                if (editText_password.getText().toString().trim().equals(editText_confpassword.getText().toString().trim())) {
                                    MainActivity.mAuth.createUserWithEmailAndPassword(editText_email.getText().toString(), editText_password.getText().toString())
                                            .addOnCompleteListener(this, task -> {
                                                if (task.isSuccessful()) {
                                                    FirebaseUser user = MainActivity.mAuth.getCurrentUser();
                                                    if (user != null) {
                                                        Usuario usuario = new Usuario();
                                                        usuario.nombre = editText_nombre.getText().toString();
                                                        usuario.cedula = editTextcedula.getText().toString();
                                                        usuario.email = user.getEmail();
                                                        usuario.rol = "Paciente";
                                                        usuario.estado="Activo";
                                                        // Asignación de la contraseña al usuario después de la validación
                                                        String clave = editText_password.getText().toString();
                                                        if (clave.length() >= 6) { // Verifica que la contraseña tenga al menos 6 caracteres
                                                            usuario.clave = clave;
                                                        } else {
                                                            // Maneja el caso en el que la contraseña no cumple con los requisitos mínimos
                                                            Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                                                        }
                                                        // Aquí deberías obtener la fecha actual y asignarla a usuario.fecha_ini_contrato
                                                        usuario.fecha_ini_contrato = fechaInicio;

                                                        MainActivity.ctlUsuario.crear_usuario(dbRef, user.getUid(), usuario);
                                                        user.sendEmailVerification();
                                                        MainActivity.mAuth.signOut();
                                                        dialog.ocultar_mensaje();
                                                        alertDialog.crear_mensaje("Correcto", "Usuario Creado Correctamente", builder -> {
                                                            builder.setCancelable(false);
                                                            builder.setNeutralButton("Aceptar", (dialogInterface, i) -> finish());
                                                            builder.create().show();
                                                        });
                                                    } else {
                                                        dialog.ocultar_mensaje();
                                                        Toast.makeText(this, "Error al Obtener el usuario", Toast.LENGTH_SHORT).show();
                                                    }
                                                } else {
                                                    dialog.ocultar_mensaje();
                                                    Toast.makeText(this, "Error al Crear el usuario", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                } else {
                                    dialog.ocultar_mensaje();
                                    Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                dialog.ocultar_mensaje();
                                Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            dialog.ocultar_mensaje();
                            Toast.makeText(this, "Por favor, confirme su contraseña", Toast.LENGTH_SHORT).show(); // Mensaje de error para campo de confirmación de contraseña vacío
                        }
                    } else {
                        dialog.ocultar_mensaje();
                        Toast.makeText(this, "Contraseña no válida", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    dialog.ocultar_mensaje();
                    Toast.makeText(this, "Correo no válido", Toast.LENGTH_SHORT).show();
                }
            } else {
                dialog.ocultar_mensaje();
                Toast.makeText(this, "Cédula no válida. Debe contener 10 dígitos numéricos.", Toast.LENGTH_SHORT).show();
            }
        });



        TengounacuentaTXT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registro.this, Login.class));

            }
        });

    }
}