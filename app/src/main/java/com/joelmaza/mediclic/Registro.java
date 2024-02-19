package com.joelmaza.mediclic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.joelmaza.mediclic.Controllers.Progress_dialog;
import com.joelmaza.mediclic.Objetos.Usuario;

public class Registro extends AppCompatActivity {

    private EditText editText_email, editText_password,editText_nombre, editText_confpassword;
    DatabaseReference dbRef;
    TextView TengounacuentaTXT;
    Progress_dialog dialog;



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
        TengounacuentaTXT = findViewById(R.id.TengounacuentaTXT);
        dialog = new Progress_dialog(this);

        dbRef = MainActivity.DB.getReference();

        toolbar.setOnClickListener(view -> finish());

        btn_registrarse.setOnClickListener(view -> {
            dialog.mostrar_mensaje("Verificando datos...");
            if (!editText_email.getText().toString().trim().isEmpty()) {
                if (!editText_password.getText().toString().trim().isEmpty()) {
                    if (editText_password.getText().toString().trim().length() >= 6) {// Verifica que la contraseña tenga al menos 6 caracteres
                        if(editText_password.equals(editText_confpassword)){
                        MainActivity.mAuth.createUserWithEmailAndPassword(editText_email.getText().toString(), editText_password.getText().toString())
                                .addOnCompleteListener(this, task -> {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = MainActivity.mAuth.getCurrentUser();
                                        if (user != null){
                                            Usuario usuario = new Usuario();
                                            usuario.nombre = editText_nombre.getText().toString();
                                            usuario.email = user.getEmail();
                                            usuario.rol = "paciente";
                                            usuario.url_foto = "";
                                            MainActivity.ctlUsuario.crear_usuario(dbRef,user.getUid(),usuario);
                                            user.sendEmailVerification();
                                            MainActivity.mAuth.signOut();
                                            Toast.makeText(this, "¡Usuario creado Correctamente!, Verifica Tu Correo", Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(this, "Error al Obtener el usuario", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(this, "Error al Crear el usuario", Toast.LENGTH_SHORT).show();
                                    }
                                });
                        } else {
                            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Contraseña no válida", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Correo no válido", Toast.LENGTH_SHORT).show();
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