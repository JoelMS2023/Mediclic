package com.joelmaza.mediclic.Marcacion;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.joelmaza.mediclic.Controllers.Alert_dialog;
import com.joelmaza.mediclic.Controllers.Progress_dialog;
import com.joelmaza.mediclic.Objetos.Ob_marcacion;
import com.joelmaza.mediclic.Principal;
import com.joelmaza.mediclic.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;

public class Add_marcacion extends AppCompatActivity implements OnMapReadyCallback {
    private static Double LATITUD, LONGITUD;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    Boolean mLocationPermissionsGranted = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    FusedLocationProviderClient fusedLocationProviderClient;
    GoogleMap mMap;
    Alert_dialog alertDialog;
    Progress_dialog dialog;
    Button btn_marcar_manual;
    Button btn_marcar_huella;
    ArrayAdapter<String> adapterspinner_tipo;
    Spinner spinner_tipo;
    String uid_biometric  = "";
    TextView estado_gps, fecha_hora_ingreso, fecha_hora_salida;
    String estado = "Asistencia";
    String fecha_horario = "";
    int hora_inicio, minutos_inicio, hora_fin, minutos_fin;
    List<String> listaTipoMarcacion;
    Date horaActual_prueba;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_marcacion);

        uid_biometric = Principal.id;

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setOnClickListener(view -> finish());

        btn_marcar_manual = findViewById(R.id.btn_marcar_manual);
        btn_marcar_huella = findViewById(R.id.btn_marcar_huella);

        fecha_hora_ingreso = findViewById(R.id.fecha_hora_ingreso);
        fecha_hora_salida = findViewById(R.id.fecha_hora_salida);

        estado_gps = findViewById(R.id.estado_gps);

        btn_marcar_manual.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        btn_marcar_huella.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

        spinner_tipo = findViewById(R.id.spinner_tipo);

        listaTipoMarcacion = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.tipo_marcacion)));
        adapterspinner_tipo = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,listaTipoMarcacion);
        adapterspinner_tipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_tipo.setAdapter(adapterspinner_tipo);

        dialog = new Progress_dialog(this);
        alertDialog = new Alert_dialog(this);


        if(!uid_biometric.isEmpty()){

            //Actualizar la fecha del servidor
            Map<String, Object> timestampData = new HashMap<>();
            timestampData.put("timestamp", ServerValue.TIMESTAMP);
            Principal.databaseReference.child("servidor").setValue(timestampData);

            String fecha_comparar = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

            Principal.databaseReference.child("usuarios").child(uid_biometric).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(snapshot.exists()) {

                        int count = 0;

                        if (snapshot.child("marcaciones").exists()) {

                            for (DataSnapshot datos : snapshot.child("marcaciones").getChildren()) {

                                if (datos.child("fecha_hora").exists() && datos.child("tipo").exists()) {

                                    if (Objects.requireNonNull(datos.child("fecha_hora").getValue()).toString().contains(fecha_comparar)) {

                                        String tipo = Objects.requireNonNull(datos.child("tipo").getValue()).toString();
                                        if(!tipo.equalsIgnoreCase("permiso laboral")) {
                                            count++;
                                            listaTipoMarcacion.remove(tipo);
                                            adapterspinner_tipo.notifyDataSetChanged();
                                        }else{
                                            count = -1;
                                        }

                                    }

                                }

                            }

                            if(count == -1){
                                listaTipoMarcacion.clear();
                                listaTipoMarcacion.add("Selecciona");
                                adapterspinner_tipo.notifyDataSetChanged();
                            }

                            if (count == 0) {
                                listaTipoMarcacion.clear();
                                listaTipoMarcacion.add("Selecciona");
                                listaTipoMarcacion.add("Inicio de Jornada");
                                adapterspinner_tipo.notifyDataSetChanged();
                            }
                            if (count == 1) {
                                listaTipoMarcacion.clear();
                                listaTipoMarcacion.add("Selecciona");
                                listaTipoMarcacion.add("Inicio de Almuerzo");
                                adapterspinner_tipo.notifyDataSetChanged();
                            }

                            if (count == 2) {
                                listaTipoMarcacion.clear();
                                listaTipoMarcacion.add("Selecciona");
                                listaTipoMarcacion.add("Fin de Almuerzo");
                                adapterspinner_tipo.notifyDataSetChanged();
                            }

                            if (count == 3) {
                                listaTipoMarcacion.clear();
                                listaTipoMarcacion.add("Selecciona");
                                listaTipoMarcacion.add("Fin de Jornada");
                                adapterspinner_tipo.notifyDataSetChanged();
                            }

                        } else {
                            listaTipoMarcacion.clear();
                            listaTipoMarcacion.add("Selecciona");
                            listaTipoMarcacion.add("Inicio de Jornada");
                            adapterspinner_tipo.notifyDataSetChanged();
                        }

                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            spinner_tipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if(fecha_hora_ingreso.getText().toString().equalsIgnoreCase("Sin Horario")){
                        listaTipoMarcacion.clear();
                        listaTipoMarcacion.add("Selecciona");
                        adapterspinner_tipo.notifyDataSetChanged();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            Principal.databaseReference.child("horarios").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(snapshot.exists()) {
                        int count = 0;
                        for (DataSnapshot datos : snapshot.getChildren()) {

                            if (datos.child("fecha").exists() && datos.child("hora_inicio").exists() && datos.child("hora_fin").exists()) {

                                if(Objects.requireNonNull(datos.child("fecha").getValue()).toString().equalsIgnoreCase(fecha_comparar)){

                                    fecha_horario = fecha_comparar;
                                    count++;
                                    if(datos.child("hora_inicio").exists()) {
                                        String h_inicio = Objects.requireNonNull(datos.child("hora_inicio").getValue()).toString();
                                        hora_inicio = Integer.parseInt(h_inicio.split(":")[0]);
                                        minutos_inicio = Integer.parseInt(h_inicio.split(":")[1].split(" ")[0]);
                                        fecha_hora_ingreso.setText(fecha_horario +" - "+h_inicio);
                                    }
                                    if(datos.child("hora_fin").exists()) {
                                        String h_fin = Objects.requireNonNull(datos.child("hora_fin").getValue()).toString();
                                        hora_fin = Integer.parseInt(h_fin.split(":")[0]);
                                        minutos_fin = Integer.parseInt(h_fin.split(":")[1].split(" ")[0]);
                                        fecha_hora_salida.setText(fecha_horario +" - "+h_fin);
                                    }
                                }

                            }
                        }

                        if(count<=0){
                            fecha_hora_ingreso.setText("Sin Horario");
                            fecha_hora_salida.setText("Sin Horario");
                        }

                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            Principal.databaseReference.child("servidor").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(snapshot.exists()){

                        if(snapshot.child("timestamp").exists()){

                            horaActual_prueba =  new Date(Long.parseLong(snapshot.child("timestamp").getValue().toString()));

                            Handler handler = new Handler(Looper.getMainLooper());

                            Runnable runnable = new Runnable() {
                                @Override
                                public void run() {

                                    Date horaActual = new Date(horaActual_prueba.getTime());

                                    String hora_now = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(horaActual);
                                    // Actualiza el TextView con la hora formateada
                                    estado_gps.setText("Marcación: "+hora_now);

                                    // Programa el próximo llamado después de 1000 milisegundos (1 segundo)

                                    if(listaTipoMarcacion.contains("Inicio de Jornada")) {

                                        if (hora_inicio >= Integer.parseInt(hora_now.split(":")[0]) && minutos_inicio >= Integer.parseInt(hora_now.split(":")[1])) {
                                            estado_gps.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.success));
                                            estado = "Asistencia";
                                        } else {
                                            estado_gps.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.danger));
                                            estado = "Atraso";
                                        }

                                    }

                                    if(listaTipoMarcacion.contains("Fin de Jornada")) {

                                        if(Integer.parseInt(hora_now.split(":")[0]) < hora_fin ||
                                                (Integer.parseInt(hora_now.split(":")[0]) == hora_fin && Integer.parseInt(hora_now.split(":")[1]) < minutos_fin )){
                                            estado_gps.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.danger));
                                            estado = "Salida temprano";

                                        } else if(Integer.parseInt(hora_now.split(":")[0]) > hora_fin ||
                                                (Integer.parseInt(hora_now.split(":")[0]) == hora_fin && Integer.parseInt(hora_now.split(":")[1]) > minutos_fin )){
                                            estado_gps.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.warning));
                                            estado = "Horas extras";

                                        }else{
                                            estado_gps.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                                            estado = "Salida";
                                        }

                                    }

                                    if(listaTipoMarcacion.contains("Inicio de Almuerzo") || listaTipoMarcacion.contains("Fin de Almuerzo")) {
                                        estado_gps.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                                        estado = "Registro";
                                    }

                                    horaActual_prueba = new Date(horaActual_prueba.getTime()+1000);

                                    handler.postDelayed(this, 1000);

                                }
                            };

                            // Inicia el primer llamado
                            handler.post(runnable);

                        }

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });




            if(Principal.id.equals(uid_biometric)){
                btn_marcar_huella.setVisibility(View.VISIBLE);
            }else{
                btn_marcar_huella.setVisibility(View.GONE);
            }


        }

        getLocationPermission();

    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (mLocationPermissionsGranted) {

            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            getDeviceLocation();
            init();

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        mLocationPermissionsGranted = true;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {

                    for (int result: grantResults) {
                        if(result != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionsGranted = false;
                            break;
                        }
                    }

                    if(mLocationPermissionsGranted){

                        activarGPS();

                        if(mLocationPermissionsGranted){

                            initMap();
                        }

                    }else{

                        alertDialog.crear_mensaje("Advertencia", "Debes ACTIVAR el Permiso de Ubicación", builder -> {
                            builder.setNeutralButton("Cambiar Permisos de Ubicación", (dialogInterface, i) -> {
                                finish();
                                startActivity(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:"+getPackageName())));
                            });
                            builder.setCancelable(false);
                            builder.create().show();
                        });

                    }

                }

            } break;

        }
    }

    private void init(){
        mMap.clear();
        getDeviceLocation();
    }

    private void initMap() {

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

    }


    private void moveCamera(LatLng latLng, float zoom, String title) {

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        MarkerOptions options = new MarkerOptions().position(latLng).title(title).draggable(false);
        Objects.requireNonNull(mMap.addMarker(options)).showInfoWindow();

    }


    //Activar permisos de locación
    private void getLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                activarGPS();

                if(mLocationPermissionsGranted){
                    initMap();
                }

            } else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    //Activación del GPS
    private void activarGPS(){

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER )) {

            mLocationPermissionsGranted = false;

            alertDialog.crear_mensaje("Advertencia", "¡El GPS está DESACTIVADO!", builder -> {
                builder.setPositiveButton("Activar GPS", (dialogInterface, i) -> {
                    finish();
                    startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                });
                builder.setNegativeButton("Cancelar", (dialogInterface, i) -> {finish();});
                builder.setCancelable(false);
                builder.create().show();
            });

        }else{
            mLocationPermissionsGranted = true;

        }

    }


    //Obtener ubicacion del dispositivo
    private void getDeviceLocation() {
        try {

            if (mLocationPermissionsGranted) {

                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

                fusedLocationProviderClient.getLastLocation().addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        Location currLocation = (Location) task.getResult();

                        if(currLocation != null){

                            LATITUD = currLocation.getLatitude();
                            LONGITUD = currLocation.getLongitude();

                            moveCamera(new LatLng(currLocation.getLatitude(), currLocation.getLongitude()), DEFAULT_ZOOM,"Mi Ubicación");

                            btn_marcar_manual.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.success)));
                            btn_marcar_huella.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.proyecto_light)));

                            btn_marcar_manual.setOnClickListener(view -> {

                                dialog.mostrar_mensaje("Guardando Marcación...");

                                if (!spinner_tipo.getSelectedItem().toString().equals("Selecciona")) {

                                    Ob_marcacion marcacion = new Ob_marcacion();
                                    marcacion.fecha_hora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
                                    marcacion.latitud = LATITUD;
                                    marcacion.longitud =  LONGITUD;
                                    marcacion.estado = estado;
                                    marcacion.tipo = spinner_tipo.getSelectedItem().toString();

                                    Ver_marcaciones.ctlMarcacion.crear_marcacion(Principal.id,marcacion);

                                    dialog.ocultar_mensaje();
                                    alertDialog.crear_mensaje("Correcto", "Marcación Creada Correctamente", builder -> {
                                        builder.setCancelable(false);
                                        builder.setNeutralButton("Aceptar", (dialogInterface, i) -> finish());
                                        builder.create().show();
                                    });

                                }else{
                                    dialog.ocultar_mensaje();
                                    alertDialog.crear_mensaje("¡Advertencia!", "Selecciona un Tipo de Marcación", builder -> {
                                        builder.setCancelable(true);
                                        builder.setNeutralButton("Aceptar", (dialogInterface, i) -> {});
                                        builder.create().show();
                                    });
                                }

                            });

                            btn_marcar_huella.setOnClickListener(view -> {

                                Executor executor = ContextCompat.getMainExecutor(this);

                                BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
                                    @Override
                                    public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                                        super.onAuthenticationError(errorCode, errString);
                                        Toast.makeText(getApplicationContext(), "ERROR "+errString,Toast.LENGTH_SHORT).show();

                                        alertDialog.crear_mensaje("No está Configurado el Biométrico", "Configura y vuelve a Intentar", builder -> {
                                            builder.setCancelable(false);
                                            builder.setNeutralButton("Aceptar", (dialogInterface, i) -> {

                                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                                                    Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
                                                    enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED, BiometricManager.Authenticators.BIOMETRIC_STRONG | BiometricManager.Authenticators.DEVICE_CREDENTIAL);
                                                    startActivity(enrollIntent);
                                                }

                                            });
                                            builder.create().show();
                                        });

                                    }

                                    @Override
                                    public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                                        super.onAuthenticationSucceeded(result);

                                        if(!uid_biometric.isEmpty()) {

                                            dialog.mostrar_mensaje("Guardando Marcación...");

                                            if (!spinner_tipo.getSelectedItem().toString().equals("Selecciona")) {
                                                Ob_marcacion marcacion = new Ob_marcacion();
                                                marcacion.fecha_hora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
                                                marcacion.estado = estado;
                                                marcacion.latitud = LATITUD;
                                                marcacion.longitud =  LONGITUD;
                                                marcacion.tipo = spinner_tipo.getSelectedItem().toString();

                                                Ver_marcaciones.ctlMarcacion.crear_marcacion(Principal.id,marcacion);

                                                dialog.ocultar_mensaje();
                                                alertDialog.crear_mensaje("Correcto", "Marcación Creada Correctamente", builder -> {
                                                    builder.setCancelable(false);
                                                    builder.setNeutralButton("Aceptar", (dialogInterface, i) -> finish());
                                                    builder.create().show();
                                                });
                                            }else{
                                                dialog.ocultar_mensaje();
                                                alertDialog.crear_mensaje("¡Advertencia!", "Selecciona un Tipo de Marcación", builder -> {
                                                    builder.setCancelable(true);
                                                    builder.setNeutralButton("Aceptar", (dialogInterface, i) -> {});
                                                    builder.create().show();
                                                });
                                            }

                                        }else{
                                            Toast.makeText(getApplicationContext(),"No hay Registro Biometrico guardado",Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                    @Override
                                    public void onAuthenticationFailed() {
                                        super.onAuthenticationFailed();
                                        Toast.makeText(getApplicationContext(), "Error al Autenticar",Toast.LENGTH_SHORT).show();
                                    }
                                });


                                BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                                        .setTitle("Verificación Mediclic")
                                        .setSubtitle("Ingresa tu huella para registrar la marcación")
                                        .setNegativeButtonText("Cancelar")
                                        .setConfirmationRequired(false)
                                        .build();

                                biometricPrompt.authenticate(promptInfo);


                            });

                        }else{

                            alertDialog.crear_mensaje("Error al Obtener la Ubicación", "Activa los permisos de ubicación", builder -> {
                                builder.setPositiveButton("Activar GPS", (dialogInterface, i) -> {
                                    finish();
                                    startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                });
                                builder.setNegativeButton("Cancelar", (dialogInterface, i) -> {finish();});
                                builder.setCancelable(false);
                                builder.create().show();
                            });

                        }
                    }

                });

            }

        } catch (SecurityException e) {
            alertDialog.crear_mensaje("Error al Obtener la Ubicación", Objects.requireNonNull(e.getLocalizedMessage()), builder -> {
                builder.setCancelable(false);
                builder.setNeutralButton("Aceptar", (dialogInterface, i) -> finish());
                builder.create().show();
            });
        }

    }
}