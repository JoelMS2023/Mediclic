package com.joelmaza.mediclic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.joelmaza.mediclic.Controllers.Ctl_usuario;
import com.joelmaza.mediclic.Objetos.Usuario;


public class MainActivity extends AppCompatActivity {


    public static FirebaseAuth mAuth;

    public static FirebaseDatabase DB = FirebaseDatabase.getInstance();
    public static Ctl_usuario ctlUsuario;
    public static DatabaseReference databaseReference;



    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_ingresar = (Button) findViewById(R.id.btn_ingresar);


        mAuth = FirebaseAuth.getInstance();
        databaseReference = DB.getReference();

        ctlUsuario = new Ctl_usuario();


        preferences = getSharedPreferences("Mediclic", MODE_PRIVATE);

        btn_ingresar.setOnClickListener(view -> {

            Intent i = new Intent();
            i.setClass(this, Login.class);
            startActivity(i);

        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser usuario = mAuth.getCurrentUser();


        if(usuario!=null && !preferences.getString("uid","").isEmpty()) {
            Intent i = new Intent();
            Principal.id = usuario.getUid();
            i.setClass(this,Principal.class);
            startActivity(i);
        }

    }


}