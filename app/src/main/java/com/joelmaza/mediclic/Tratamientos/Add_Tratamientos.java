package com.joelmaza.mediclic.Tratamientos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.joelmaza.mediclic.R;

import javax.annotation.Nullable;

public class Add_Tratamientos extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 101;
    EditText editTextNombre, editTextDescripcion;
    ImageView imageViewTratamiento;
    Button btnSeleccionarImagen, btnGuardar;
    Bitmap imageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tratamientos);

        editTextNombre = findViewById(R.id.editTextNombre);
        editTextDescripcion = findViewById(R.id.editTextDescripcion);
        imageViewTratamiento = findViewById(R.id.imageViewTratamiento);
        btnSeleccionarImagen = findViewById(R.id.btnSeleccionarImagen);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnSeleccionarImagen.setOnClickListener(v -> {
            dispatchTakePictureIntent();
        });

        btnGuardar.setOnClickListener(v -> {
            guardarTratamiento();
        });
    }

    private void guardarTratamiento() {
        String nombre = editTextNombre.getText().toString();
        String descripcion = editTextDescripcion.getText().toString();

        // Aquí puedes agregar el código para guardar el tratamiento en tu base de datos o hacer lo que necesites con los datos ingresados
        // Además, puedes guardar la imagen en la base de datos o en el almacenamiento local de tu dispositivo

        // Por ahora, solo mostramos un mensaje de confirmación
        Toast.makeText(this, "Tratamiento guardado:\nNombre: " + nombre + "\nDescripción: " + descripcion, Toast.LENGTH_LONG).show();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            imageViewTratamiento.setImageBitmap(imageBitmap);
        }
    }
}