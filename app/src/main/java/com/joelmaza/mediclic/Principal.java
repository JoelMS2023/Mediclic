package com.joelmaza.mediclic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;


import com.joelmaza.mediclic.Controllers.Alert_dialog;
import com.joelmaza.mediclic.databinding.ActivityPrincipalBinding;

public class Principal extends AppCompatActivity {

    public static String rol="";
    public static String id = "";
    public static String Nombre = "";
    private AppBarConfiguration mAppBarConfiguration;
    public static DatabaseReference databaseReference;
    Alert_dialog alertDialog;
    public static  SharedPreferences preferences;
    private boolean doubleBackToExitPressedOnce = false;
    private static final int DOUBLE_CLICK_INTERVAL = 2000;
    public static boolean vale_biometrico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences=getSharedPreferences("Mediclic", MODE_PRIVATE);
        rol= preferences.getString("rol","");

        if(MainActivity.mAuth.getUid() != null) {

            ActivityPrincipalBinding binding = ActivityPrincipalBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            databaseReference = MainActivity.DB.getReference();

            setSupportActionBar(binding.appBarPrincipal.toolbar);
            binding.appBarPrincipal.fab.setOnClickListener(view -> Snackbar.make(view, "mazasalazarj@gmail.com", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show());
            DrawerLayout drawer = binding.drawerLayout;
            NavigationView navigationView = binding.navView;

            View headerView = navigationView.getHeaderView(0);
            TextView headerTextView = headerView.findViewById(R.id.header_username);
            TextView txt_correo = headerView.findViewById(R.id.header_correo);

            alertDialog = new Alert_dialog(this);

            if(preferences.getString("uid_biometric","").isEmpty()){

                alertDialog.crear_mensaje("¿Desea Agregar este usuario al Biométrico?", "Accede con un solo usuario, directamente con Biometría", builder -> {
                    builder.setPositiveButton("Aceptar", (dialogInterface, i) -> {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("uid_biometric", id);
                        editor.apply();
                        Toast.makeText(this,"Biometrico Agregado Correctamente", Toast.LENGTH_SHORT).show();

                    });
                    builder.setNeutralButton("Cancelar", (dialogInterface, i) -> {});
                    builder.setCancelable(false);
                    builder.create().show();
                });
            }


            MainActivity.ctlUsuario.Obtener_usuario(databaseReference,MainActivity.mAuth.getUid(),user -> {

                headerTextView.setText(user.nombre);
                txt_correo.setText(user.email);


            });


            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.

            if(rol.equals("Administrador"))  {
                mAppBarConfiguration = new AppBarConfiguration.Builder(
                        R.id.nav_home, R.id.nav_profile, R.id.nav_horario,R.id.nav_user)
                        .setOpenableLayout(drawer)
                        .build();
            }else{
                mAppBarConfiguration = new AppBarConfiguration.Builder(
                        R.id.nav_home, R.id.nav_profile)
                        .setOpenableLayout(drawer)
                        .build();
                binding.navView.getMenu().findItem(R.id.nav_horario).setVisible(false);
                binding.navView.getMenu().findItem(R.id.nav_user).setVisible(false);

            }
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_principal);
            NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
            NavigationUI.setupWithNavController(navigationView, navController);
        }else {
            finish();
            startActivity(new Intent(this, MainActivity.class));

        }

    }
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Presione de nuevo para salir", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, DOUBLE_CLICK_INTERVAL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_principal);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();


    }

    //actividad = this;


}