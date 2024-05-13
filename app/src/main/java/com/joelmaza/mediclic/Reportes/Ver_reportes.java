package com.joelmaza.mediclic.Reportes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.joelmaza.mediclic.Controllers.Progress_dialog;
import com.joelmaza.mediclic.MainActivity;
import com.joelmaza.mediclic.Principal;
import com.joelmaza.mediclic.R;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;



public class Ver_reportes extends AppCompatActivity {

    CardView card_rpt_marcacion, card_rpt_citas;
    HSSFWorkbook hssfWorkbook;
    HSSFSheet hssfSheet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_reportes);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setOnClickListener(view -> finish());

        card_rpt_marcacion = findViewById(R.id.card_rpt_marcacion);
        card_rpt_citas = findViewById(R.id.card_rpt_citas);

        card_rpt_marcacion.setOnClickListener(view -> {
            startActivity(new Intent(this, Rpt_marcacion.class));
        });
        card_rpt_citas.setOnClickListener(view -> {
            startActivity(new Intent(this, Rpt_citas.class));
        });

        ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        Progress_dialog dialog = new Progress_dialog(this);
        CardView txt_consolidado = findViewById(R.id.txt_consolidado);


        hssfWorkbook = new HSSFWorkbook();

        txt_consolidado.setOnClickListener(view -> {

            dialog.mostrar_mensaje("Creando Reporte...");

            hssfSheet = hssfWorkbook.createSheet("CITAS");
            encabezado(hssfSheet);
            Principal.databaseReference.child("usuarios").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {

                        int contador = 1;

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            String rolUsuario = snapshot.child("rol").getValue(String.class);

                            if (rolUsuario != null && rolUsuario.equals("Doctor")) { // Verifica si el rol del usuario es "Doctor"

                                HSSFRow hssfRow = hssfSheet.createRow(contador);
                                HSSFCell hssfCell0 = hssfRow.createCell(0);
                                HSSFCell hssfCell1 = hssfRow.createCell(1);
                                HSSFCell hssfCell2 = hssfRow.createCell(2);
                                HSSFCell hssfCell3 = hssfRow.createCell(3);
                                HSSFCell hssfCell4 = hssfRow.createCell(4);
                                HSSFCell hssfCell5 = hssfRow.createCell(5);
                                HSSFCell hssfCell6 = hssfRow.createCell(6);
                                HSSFCell hssfCell7 = hssfRow.createCell(7);
                                HSSFCell hssfCell8 = hssfRow.createCell(8);

                                hssfCell0.setCellValue(snapshot.child("nombre").getValue(String.class));
                                Object direccionObject = snapshot.child("direccion").getValue();
                                if (direccionObject != null) {
                                    hssfCell1.setCellValue(direccionObject.toString());
                                } else {
                                    hssfCell1.setCellValue(""); // O algún valor predeterminado si prefieres
                                }
                                hssfCell2.setCellValue(snapshot.child("telefono").getValue(String.class));
                                hssfCell3.setCellValue(snapshot.child("email").getValue(String.class));
                                hssfCell4.setCellValue(rolUsuario);
                                Object cedulaObject = snapshot.child("cedula").getValue();
                                if (cedulaObject != null) {
                                    hssfCell5.setCellValue(cedulaObject.toString());
                                } else {
                                    hssfCell5.setCellValue(""); // O algún valor predeterminado si prefieres
                                }

                                if(snapshot.child("url_foto").exists()){
                                    hssfCell6.setCellValue("SI");
                                    hssfCell7.setCellValue(snapshot.child("url_foto").getValue(String.class));
                                }else{
                                    hssfCell6.setCellValue("NO");
                                    hssfCell7.setCellValue("");
                                }

                                if (snapshot.child("citas").exists()) {
                                    hssfCell8.setCellValue(snapshot.child("citas").getChildrenCount());
                                } else {
                                    hssfCell8.setCellValue(0); // O cualquier otro valor predeterminado que desees
                                }

                                contador++;

                            }

                        }

                        try {

                            String nombre = "/rpt_citas_"+(new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss", Locale.getDefault()).format(new Date()))+".xls";
                            File file = new File(Environment.getExternalStorageDirectory()+nombre);

                            FileOutputStream fileOutputStream = new FileOutputStream(file);
                            hssfWorkbook.write(fileOutputStream);
                            hssfWorkbook.close();
                            fileOutputStream.close();

                            dialog.ocultar_mensaje();
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setDataAndType(Uri.fromFile(file),"application/vnd.ms-excel");
                            startActivity(i);

                        } catch (Exception e) {
                            Toast.makeText(getBaseContext(),"No se puede generar el reporte",Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

            });

        });





    }

    public void encabezado(HSSFSheet hssfSheet){

        String [] columnas = {"nombre","direccion","telefono","email","rol","cedula","url_foto","url","citas"};
        HSSFRow hssfRow0 = hssfSheet.createRow(0);

        for (int i = 0; i < columnas.length ; i++) {
            HSSFCell hssfCell = hssfRow0.createCell(i);
            hssfCell.setCellValue(columnas[i]);
        }
    }
}