package com.joelmaza.mediclic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.joelmaza.mediclic.Controllers.Alert_dialog;

public class Olvide_usuario extends AppCompatActivity {
    private EditText editTextEmail;
    private FirebaseAuth  mAuth;
    Alert_dialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_olvide_usuario);

        mAuth = FirebaseAuth.getInstance();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setOnClickListener(view -> finish());

        editTextEmail = findViewById(R.id.editTextEmail);
        Button buttonResetPassword = findViewById(R.id.buttonResetPassword);
        alertDialog = new Alert_dialog(this);


        buttonResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    editTextEmail.setError("Por favor, introduce tu correo electrónico");
                    return;
                }

                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Olvide_usuario.this, "Se ha enviado un correo electrónico para restablecer tu contraseña", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(Olvide_usuario.this, "No se pudo enviar el correo electrónico de restablecimiento de contraseña. Por favor, comprueba tu correo electrónico.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}