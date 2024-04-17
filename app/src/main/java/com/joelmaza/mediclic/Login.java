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


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.joelmaza.mediclic.Controllers.Alert_dialog;
import com.joelmaza.mediclic.Controllers.Progress_dialog;
import com.joelmaza.mediclic.Objetos.Usuario;
import com.joelmaza.mediclic.R;

import com.google.android.gms.tasks.Task;


public class Login extends AppCompatActivity {

    SharedPreferences preferences;
    DatabaseReference dbref;
    EditText  editText_email, editText_password;
    Progress_dialog dialog;
    Alert_dialog alertDialog;
    private static final int RC_SIGN_IN = 1;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth mAuth;
    SignInButton mSignInButtonGoogle;
    TextView text_forget;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        mAuth = FirebaseAuth.getInstance();
        Button btn_ingresar = (Button) findViewById(R.id.btn_ingresar);
        TextView btn_registrarse= (TextView) findViewById(R.id.btn_registrarse);
        mSignInButtonGoogle = findViewById(R.id.btnGoogle);
        text_forget = findViewById(R.id.text_forget);

        text_forget.setOnClickListener(view -> {
            startActivity(new Intent(this,Olvide_usuario.class));
        });

    //Configuracion de google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("58905270224-c8pdncnv7pl1ia92ieu2eom8c0ijp0io.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        editText_email = (EditText) findViewById(R.id.editText_email);
        editText_password = (EditText) findViewById(R.id.editText_password);
        dialog = new Progress_dialog(this);
        alertDialog = new Alert_dialog(this);

        dbref= MainActivity.DB.getReference();

        preferences=getSharedPreferences("Mediclic", MODE_PRIVATE);


        btn_ingresar.setOnClickListener(view -> {
            dialog.mostrar_mensaje("Iniciando sesión...");

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
                                                    dialog.ocultar_mensaje();
                                                    SharedPreferences.Editor editor = preferences.edit();
                                                    editor.putString("uid", user.getUid());
                                                    editor.putString("rol", datos.child("rol").getValue().toString());
                                                    editor.apply();

                                                    Intent i = new Intent();
                                                    Principal.id = user.getUid();
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
                                            dialog.ocultar_mensaje();
                                            alertDialog.crear_mensaje("Advertencia", "Error al Iniciar Sesión",builder -> {
                                                builder.setCancelable(true);
                                                builder.setNeutralButton("Aceptar", (dialogInterface, i) -> {});
                                                builder.create().show();
                                            });

                                        }
                                    });



                                    //}else{
                                    //  Toast.makeText(this,"Debes verificar tu correo",Toast.LENGTH_LONG).show();
                                    //  MainActivity.mAuth.signOut();
                                    //}

                                }else{
                                    dialog.ocultar_mensaje();
                                    Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_LONG).show();
                                }

                            }else{
                                dialog.ocultar_mensaje();
                                Toast.makeText(this, "Usuario y/o Clave Incorrectos",Toast.LENGTH_LONG).show();
                            }

                        });
            }else{
                dialog.ocultar_mensaje();
                Toast.makeText(this, "Ingresa el usuario y la clave",Toast.LENGTH_SHORT).show();
            }

        });

        btn_registrarse.setOnClickListener(view -> {
            Intent i = new Intent();
            i.setClass(this, Registro.class);
            startActivity(i);
        });

    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                //mTextViewRespuesta.setText(e.getMessage());

            }
        }
    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);

                            Usuario usuario = new Usuario();
                            usuario.uid = user.getUid();
                            usuario.email = user.getEmail();
                            usuario.rol = "Paciente";
                            MainActivity.ctlUsuario.actualizar_usuario(dbref,usuario);
                            Principal.id = user.getUid();
                            irHome();

                        } else {
                            // If sign in fails, display a message to the user.
                            //mTextViewRespuesta.setText(task.getException().toString());
                            updateUI(null);
                        }
                    }
                });
    }
    private void updateUI(FirebaseUser user) {
        user = mAuth.getCurrentUser();
        if (user != null){
            irHome();
        }
    }
    private void irHome() {
        Intent intent = new Intent(Login.this, Principal.class);
        startActivity(intent);
        finish();
    }

}