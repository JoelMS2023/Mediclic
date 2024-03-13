package com.joelmaza.mediclic;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Ubicacion extends AppCompatActivity implements OnMapReadyCallback {

    private static final double UBICACION_LATITUD = -3.267922;
    private static final double UBICACION_LONGITUD = -79.898226;
    private static final float DEFAULT_ZOOM = 15f;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubicacion);

        // Inicializar el mapa
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        // Botón para abrir la ubicación en Google Maps
        TextView txtUbicacion = findViewById(R.id.txtUbicacion);
        txtUbicacion.setOnClickListener(view -> openInGoogleMaps());
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Configurar el estilo del puntero
        mMap.getUiSettings().setMapToolbarEnabled(false); // Desactivar la barra de herramientas del mapa
        mMap.getUiSettings().setMyLocationButtonEnabled(false); // Desactivar el botón de ubicación del usuario

        // Mover la cámara a la ubicación específica y establecer un zoom adecuado
        LatLng ubicacion = new LatLng(UBICACION_LATITUD, UBICACION_LONGITUD);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, DEFAULT_ZOOM));

        // Agregar un marcador grande en la ubicación específica
        mMap.addMarker(new MarkerOptions()
                .position(ubicacion)
                .title("Torre Médica Para La Familia")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .alpha(0.7f) // Opacidad del marcador
                .anchor(0.5f, 0.5f)); // Anclaje del marcador (centrado)

        // Habilitar la funcionalidad de zoom en el mapa
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }

    // Método para abrir la ubicación en Google Maps
    private void openInGoogleMaps() {
        Uri gmmIntentUri = Uri.parse("https://www.google.com/maps/place/Torre+Medica+Para+La+Familia/@-3.267922,-79.898226,11z/data=!4m6!3m5!1s0x90330e5b832d41dd:0x59af7e04e5c46b12!8m2!3d-3.2622841!4d-79.9559912!16s%2Fg%2F11clyth8r5?hl=es-419&entry=ttu");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");

        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }
}