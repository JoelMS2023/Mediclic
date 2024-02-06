package com.joelmaza.mediclic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.SharedPreferences;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.joelmaza.mediclic.R;


public class Login extends AppCompatActivity {

    SharedPreferences preferences;
    DatabaseReference dbref;
    EditText  editText_email, editText_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Button btn_ingresar = (Button) findViewById(R.id.btn_ingresar);
        Button btn_registrarse= (Button) findViewById(R.id.btn_registrarse);

        editText_email = (EditText) findViewById(R.id.editText_email);
        editText_password = (EditText) findViewById(R.id.editText_password);

        dbref= MainActivity.DB.getReference();

        preferences=getSharedPreferences("Mediclic", MODE_PRIVATE);

        toolbar.setOnClickListener(view -> finish());

        btn_ingresar.setOnClickListener(view -> {

            if(!editText_email.getText().toString().isEmpty() && !editText_password.getText().toString().isEmpty()){

                MainActivity.mAuth.signInWithEmailAndPassword(editText_email.getText().toString(),editText_password.getText().toString())
                        .addOnCompleteListener(this, task -> {

                            if(task.isSuccessful()){

                                FirebaseUser user = MainActivity.mAuth.getCurrentUser();

                                if(user != null) {

                                    //if (user.isEmailVerified()) {

                                    dbref.child("usuarios").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot datos) {

                                            if(datos.exists()){

                                                if(preferences.getString("uid","").isEmpty()) {
                                                    SharedPreferences.Editor editor = preferences.edit();
                                                    editor.putString("uid", user.getUid());
                                                    editor.putString("rol", datos.child("rol").getValue().toString());
                                                    editor.apply();

                                                    Intent i = new Intent();
                                                    i.setClass(getApplicationContext(), Principal.class);
                                                    startActivity(i);

                                                    editText_email.setText("");
                                                    editText_password.setText("");

                                                    finish();

                                                }

                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });



                                    //}else{
                                    //  Toast.makeText(this,"Debes verificar tu correo",Toast.LENGTH_LONG).show();
                                    //  MainActivity.mAuth.signOut();
                                    //}

                                }else{
                                    Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_LONG).show();
                                }

                            }else{
                                Toast.makeText(this, "Usuario y/o Clave Incorrectos",Toast.LENGTH_LONG).show();
                            }

                        });
            }else{
                Toast.makeText(this, "Ingresa el usuario y la clave",Toast.LENGTH_SHORT).show();
            }

        });

        btn_registrarse.setOnClickListener(view -> {
            Intent i = new Intent();
            i.setClass(this, Registro.class);
            startActivity(i);
        });

    }
}