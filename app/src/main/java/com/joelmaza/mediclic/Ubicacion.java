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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Ubicacion extends AppCompatActivity implements OnMapReadyCallback {

    private static final double UBICACION_LATITUD = -3.2622787; // Latitud de "P2QV+3JJ, Machala"
    private static final double UBICACION_LONGITUD = -79.9585661; // Longitud de "P2QV+3JJ, Machala"
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

        // Mostrar la latitud y longitud en los EditText
        // Suponiendo que tienes EditText con los IDs txtLatitud y txtLongitud
        // Puedes mostrar los valores de latitud y longitud aquí si es necesario
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Mover la cámara a la ubicación específica y establecer un zoom adecuado
        LatLng ubicacion = new LatLng(UBICACION_LATITUD, UBICACION_LONGITUD);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, DEFAULT_ZOOM));

        // Colocar un marcador en la ubicación específica
        mMap.addMarker(new MarkerOptions().position(ubicacion).title("Ubicación deseada"));

        // Mostrar la latitud y longitud en los EditText correspondientes
        TextView txtLatitud = findViewById(R.id.txtLatitud);
        TextView txtLongitud = findViewById(R.id.txtLongitud);
        txtLatitud.setText(String.valueOf(UBICACION_LATITUD));
        txtLongitud.setText(String.valueOf(UBICACION_LONGITUD));

        // Habilitar la funcionalidad de zoom en el mapa
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Establecer un listener para el marcador, para abrir la aplicación de mapas cuando se haga clic en él
        mMap.setOnMarkerClickListener(marker -> {
            // Crear una Uri para la ubicación específica
            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + UBICACION_LATITUD + "," + UBICACION_LONGITUD);

            // Crear un intent para la aplicación de mapas
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");

            // Verificar si hay aplicaciones disponibles para manejar la intención
            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                // Abrir la aplicación de mapas
                startActivity(mapIntent);
            }
            return true;
        });
    }

}
