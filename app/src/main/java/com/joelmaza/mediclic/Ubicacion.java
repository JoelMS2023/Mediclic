package com.joelmaza.mediclic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.joelmaza.mediclic.Controllers.Alert_dialog;

import java.util.Objects;





public class Ubicacion extends AppCompatActivity implements OnMapReadyCallback {

    private static Double LATITUD, LONGITUD;
    private static final String FINE_LOCATION = android.Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    Boolean mLocationPermissionsGranted = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    FusedLocationProviderClient fusedLocationProviderClient;
    GoogleMap mMap;
    Alert_dialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubicacion);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setOnClickListener(view -> finish());

        alertDialog = new Alert_dialog(this);

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

    private void init(){
        mMap.clear();
        getDeviceLocation();
    }

    private void initMap() {

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

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

    private void moveCamera(LatLng latLng, float zoom, String title) {

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        MarkerOptions options = new MarkerOptions().position(latLng).title(title).draggable(false);
        Objects.requireNonNull(mMap.addMarker(options)).showInfoWindow();

    }

    private void getDeviceLocation(){

        try {

            if (mLocationPermissionsGranted) {

                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

                fusedLocationProviderClient.getLastLocation().addOnCompleteListener(task -> {

                    if(task.isSuccessful()){


                        Location currLocation = (Location) task.getResult();

                        if(currLocation != null){

                            LATITUD = currLocation.getLatitude();
                            LONGITUD = currLocation.getLongitude();

                            moveCamera(new LatLng(LATITUD, LONGITUD), DEFAULT_ZOOM, "Mi ubicación");

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

        }catch (SecurityException e){

            alertDialog.crear_mensaje("Error al Obtener la Ubicación", Objects.requireNonNull(e.getLocalizedMessage()), builder -> {
                builder.setCancelable(false);
                builder.setNeutralButton("Aceptar", (dialogInterface, i) -> finish());
                builder.create().show();
            });

        }

    }


}