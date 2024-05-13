package com.joelmaza.mediclic.Tratamientos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.joelmaza.mediclic.Citas.Ver_citas;
import com.joelmaza.mediclic.Controllers.Alert_dialog;
import com.joelmaza.mediclic.Controllers.Progress_dialog;
import com.joelmaza.mediclic.Objetos.Ob_citas;
import com.joelmaza.mediclic.Objetos.Ob_tratamientos;
import com.joelmaza.mediclic.R;

import javax.annotation.Nullable;

public class Add_Tratamientos extends AppCompatActivity {

    EditText editTextNombre, editTextDescripcion;
    Button  btnGuardar;

    Alert_dialog alertDialog;
    public static String UID_TRATAMIENTO = "";
    Progress_dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tratamientos);


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setOnClickListener(view -> finish());
        editTextNombre = findViewById(R.id.editTextNombre);
        editTextDescripcion = findViewById(R.id.editTextDescripcion);
        btnGuardar = findViewById(R.id.btnGuardar);

        dialog = new Progress_dialog(this);
        alertDialog = new Alert_dialog(this);

        btnGuardar.setOnClickListener(v -> {
            dialog.mostrar_mensaje("Creando Tratamiento...");
            if (!editTextNombre.getText().toString().isEmpty() && !editTextDescripcion.getText().toString().isEmpty()) {
                Ob_tratamientos obTratamientos = new Ob_tratamientos();
                obTratamientos.tipo = editTextNombre.getText().toString();
                obTratamientos.mensaje = editTextDescripcion.getText().toString();

                Ver_tratamientos.ctlTratamientos.crear_tratamientos(obTratamientos);

                dialog.ocultar_mensaje();
                alertDialog.crear_mensaje("Correcto", "Tratamiento Creado Correctamente", builder -> {
                    builder.setCancelable(false);
                    builder.setNeutralButton("Aceptar", (dialogInterface, i) -> finish());
                    builder.create().show();
                });
            } else if (editTextNombre.getText().toString().isEmpty() || editTextDescripcion.getText().toString().isEmpty()) {
                dialog.ocultar_mensaje();
                alertDialog.crear_mensaje("¡Advertencia!", "Completa todos los campos", builder -> {
                    builder.setCancelable(true);
                    builder.setNeutralButton("Aceptar", (dialogInterface, i) -> {
                    });
                    builder.create().show();
                });
            } else {
                dialog.ocultar_mensaje();
                alertDialog.crear_mensaje("¡Advertencia!", "Selecciona un Tipo de Cita", builder -> {
                    builder.setCancelable(true);
                    builder.setNeutralButton("Aceptar", (dialogInterface, i) -> {
                    });
                    builder.create().show();
                });
            }
        });
    }
}
