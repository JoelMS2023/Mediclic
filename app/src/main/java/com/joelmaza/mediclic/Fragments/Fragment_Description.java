package com.joelmaza.mediclic.Fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.joelmaza.mediclic.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Description#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Description extends Fragment implements OnMapReadyCallback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private GoogleMap mMap;
    private static final LatLng LOCATION_MACHALA = new LatLng(-3.264545, -79.958322);
    private static final float DEFAULT_ZOOM = 15f;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment_Description() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_Description.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_Description newInstance(String param1, String param2) {
        Fragment_Description fragment = new Fragment_Description();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista= inflater.inflate(R.layout.fragment__description, container, false);

        TextView txtUbicacion = vista.findViewById( R.id.txtUbicacion);
        txtUbicacion.setOnClickListener(view -> openInGoogleMaps());


        ImageButton btnInstagram = vista.findViewById(R.id.btnInstagram);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        btnInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirSitioWeb("https://www.instagram.com/torremedparalafamilia/");
            }
        });

        // Botón de Facebook
        ImageButton btnFacebook = vista.findViewById(R.id.btnFacebook);
        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirSitioWeb("https://www.facebook.com/profile.php?id=100047580051310");
            }
        });

        // Botón de WhatsApp
        ImageButton btnWhatsApp = vista.findViewById(R.id.btnWhatsApp);
        btnWhatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirSitioWeb("https://api.whatsapp.com/send?phone=+593968171997&text=Deseo%20agendar%20una%20cita%20en%20la%20especialidad%20de");
            }
        });

        return vista;
    }

    private void openInGoogleMaps() {
        // Crear la URI para la ubicación específica en Google Maps
        Uri gmmIntentUri = Uri.parse("https://www.google.com/maps/place/Torre+Medica+Para+La+Familia/@-3.267922,-79.898226,11z/data=!4m6!3m5!1s0x90330e5b832d41dd:0x59af7e04e5c46b12!8m2!3d-3.2622841!4d-79.9559912!16s%2Fg%2F11clyth8r5?hl=es-419&entry=ttu");

        // Crear un intent para ver la ubicación en la aplicación de Google Maps
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);

        // Especificar que queremos abrir el intent en la aplicación de Google Maps
        mapIntent.setPackage("com.google.android.apps.maps");

        // Verificar si hay una aplicación que pueda manejar el intent
        if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Si hay una aplicación, iniciar el intent
            startActivity(mapIntent);
        }
    }

    private void abrirSitioWeb(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        mMap = googleMap;

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);

        // Mover la cámara a la ubicación específica y establecer un zoom adecuado
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LOCATION_MACHALA, DEFAULT_ZOOM));

        // Agregar marcador en la ubicación específica
        mMap.addMarker(new MarkerOptions().position(LOCATION_MACHALA).title("Torre Medica Para la familia"));
    }
}

