package com.joelmaza.mediclic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;


import com.joelmaza.mediclic.databinding.ActivityPrincipalBinding;

public class Principal extends AppCompatActivity {

    public static String rol="";
    private AppBarConfiguration mAppBarConfiguration;
    public static DatabaseReference databaseReference;
    public static  SharedPreferences preferences;

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

            MainActivity.ctlUsuario.Obtener_usuario(databaseReference,MainActivity.mAuth.getUid(),user -> {

                headerTextView.setText(user.nombre);

            });


            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.

            if(rol.equals("Administrador")) {
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