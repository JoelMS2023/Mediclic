package com.joelmaza.mediclic;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.joelmaza.mediclic.Controllers.Alert_dialog;
import com.joelmaza.mediclic.Controllers.Progress_dialog;
import com.joelmaza.mediclic.Objetos.Usuario;
import com.joelmaza.mediclic.Fragments.fragmento_Usuario;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class Vi_det_usuario extends AppCompatActivity {


    TextView txt_nombre, txt_email, txt_cedula, cant_marcaciones, cant_solicitudes;
    EditText editext_direccion, editext_telefono;
    DatabaseReference dbref;
    CalendarView fecha_inicio, fecha_fin;
    Button btn_update, btn_delete, btn_add_fecha_fin, btn_add_fecha_inicio, btn_del_fecha_fin;
    Spinner spinner_estado;
    String uid;
    Alert_dialog alertDialog;
    Progress_dialog dialog;
    long fecha_cal_ini = -1, fecha_cal_fin = -1;
    ImageView img_perfil;

    ArrayAdapter<CharSequence> adapterspinner_estado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vi_det_usuario);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setOnClickListener(view -> finish());

        dbref = MainActivity.DB.getReference();

        uid = getIntent().getExtras().getString("uid");

        txt_nombre = findViewById(R.id.txt_nombre);
        txt_email = findViewById(R.id.txt_email);
        editext_direccion = findViewById(R.id.editext_direccion);
        editext_telefono = findViewById(R.id.editext_telefono);
        btn_update = findViewById(R.id.btn_update);
        btn_delete = findViewById(R.id.btn_delete);
        dialog = new Progress_dialog(this);
        alertDialog = new Alert_dialog(this);
        btn_add_fecha_inicio = findViewById(R.id.btn_add_fecha_inicio);
        btn_add_fecha_fin = findViewById(R.id.btn_add_fecha_fin);
        btn_del_fecha_fin = findViewById(R.id.btn_del_fecha_fin);
        img_perfil = findViewById(R.id.img_perfil);
        spinner_estado = findViewById(R.id.spinner_estado);

        uid = Objects.requireNonNull(getIntent().getExtras()).getString("uid", "");


        cant_marcaciones = findViewById(R.id.cant_marcaciones);
        cant_solicitudes = findViewById(R.id.cant_solicitudes);

        adapterspinner_estado = ArrayAdapter.createFromResource(this, R.array.estado_user, android.R.layout.simple_spinner_item);
        adapterspinner_estado.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_estado.setAdapter(adapterspinner_estado);


        assert uid != null;
        if (!uid.isEmpty()) {

            btn_add_fecha_inicio.setOnClickListener(v -> {
                fecha_inicio.setVisibility(View.VISIBLE);
                btn_add_fecha_inicio.setVisibility(View.GONE);
            });

            btn_add_fecha_fin.setOnClickListener(v -> {
                fecha_fin.setVisibility(View.VISIBLE);
                btn_add_fecha_fin.setVisibility(View.GONE);
            });

            btn_del_fecha_fin.setOnClickListener(v -> {
                fragmento_Usuario.ctlUsuarios.eliminar_fecha_fin_contrato(dbref, uid);
                btn_del_fecha_fin.setVisibility(View.GONE);
            });

            btn_update.setOnClickListener(v -> {

                if (editext_direccion.getText().toString().isEmpty() || editext_telefono.getText().toString().isEmpty()
                        || (editext_telefono.getText().toString().trim().isEmpty() && editext_telefono.getError() == null)
                        || spinner_estado.getSelectedItem().toString().equals("Selecciona")) {

                    Usuario user = new Usuario();
                    user.uid = uid;
                    user.direccion = editext_direccion.getText().toString();
                    user.telefono = editext_telefono.getText().toString();
                    user.estado = spinner_estado.getSelectedItem().toString();
                    MainActivity.ctlUsuario.actualizar_usuario(dbref, user);

                    if (fecha_inicio.getVisibility() == View.VISIBLE) {
                        long fechaContrato = (fecha_cal_ini != -1) ? fecha_cal_ini : new Date().getTime();
                        user.fecha_ini_contrato = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(fechaContrato);
                    }

                    if (fecha_fin.getVisibility() == View.VISIBLE) {
                        long fechaContratoFin = (fecha_cal_fin != -1) ? fecha_cal_fin : new Date().getTime();
                        user.fecha_fin_contrato = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(fechaContratoFin);
                    }
                    fragmento_Usuario.ctlUsuarios.actualizar_usuario(dbref,user);

                    dialog.ocultar_mensaje();
                    alertDialog.crear_mensaje("Correcto", "Usuario Actualizado Correctamente", builder -> {
                        builder.setCancelable(false);
                        builder.setNeutralButton("Aceptar", (dialogInterface, i) -> {});
                        builder.create().show();
                    });

                } else {
                    dialog.ocultar_mensaje();
                    alertDialog.crear_mensaje("¡Advertencia!", "Completa todos los campos", builder -> {
                        builder.setCancelable(true);
                        builder.setNeutralButton("Aceptar", (dialogInterface, i) -> {});
                        builder.create().show();
                    });
                }

            });

            btn_delete.setOnClickListener(v -> {

                alertDialog.crear_mensaje("¿Estás Seguro de Eliminar el usuario?", "¡Esta acción no es reversible!", builder -> {
                    builder.setPositiveButton("Aceptar", (dialogInterface, i) -> {
                        dialog.mostrar_mensaje("Eliminando Usuario...");
                        MainActivity.ctlUsuario.eliminar_usuario(dbref, uid);
                        dialog.ocultar_mensaje();
                        finish();
                    });
                    builder.setNeutralButton("Cancelar", (dialogInterface, i) -> {
                    });
                    builder.setCancelable(false);
                    builder.create().show();
                });

            });
            editext_telefono.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if(editable.toString().trim().length() == 10) {
                        if (!fragmento_Usuario.ctlUsuarios.validar_celular(editable.toString().trim())) {
                            editext_telefono.setError("Ingresa un celular válido");
                        }
                    }else{
                        editext_telefono.setError("Ingresa 10 dígitos");
                    }
                }
            });


            dbref.child("usuarios").child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot datos) {

                    if (datos.exists()) {

                        if (datos.child("cedula").exists()) {
                            txt_cedula.setText(Objects.requireNonNull(datos.child("cedula").getValue()).toString());
                        }
                        if (datos.child("estado").exists()) {
                            String estado = Objects.requireNonNull(datos.child("estado").getValue()).toString();
                            int spinnerPosition = adapterspinner_estado.getPosition(estado);
                            spinner_estado.setSelection(spinnerPosition);
                        }
                        if (datos.child("nombre").exists()) {
                            txt_nombre.setText(Objects.requireNonNull(datos.child("nombre").getValue()).toString());
                        }
                        if (datos.child("email").exists()) {
                            txt_email.setText(Objects.requireNonNull(datos.child("email").getValue()).toString());
                        }
                        if (datos.child("direccion").exists()) {
                            editext_direccion.setText(datos.child("direccion").getValue().toString());
                        } else {
                            editext_direccion.setText("");
                        }
                        if (datos.child("telefono").exists()) {
                            editext_telefono.setText(datos.child("telefono").getValue().toString());
                        } else {
                            editext_telefono.setText("");
                        }
                        if(datos.child("fecha_ini_contrato").exists()){
                            btn_add_fecha_inicio.setVisibility(View.GONE);
                            btn_add_fecha_fin.setVisibility(View.VISIBLE);
                            String f_inicio = Objects.requireNonNull(datos.child("fecha_ini_contrato").getValue()).toString();
                            int dia = Integer.parseInt(f_inicio.split("/")[0]);
                            int mes = Integer.parseInt(f_inicio.split("/")[1]) - 1;
                            int anio = Integer.parseInt(f_inicio.split("/")[2]);
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(anio,mes,dia);
                            fecha_inicio.setDate(calendar.getTimeInMillis());
                            fecha_cal_ini = calendar.getTimeInMillis();
                        }else{
                            fecha_inicio.setVisibility(View.GONE);
                            btn_add_fecha_inicio.setVisibility(View.VISIBLE);
                            btn_add_fecha_fin.setVisibility(View.GONE);
                        }

                        if(datos.child("fecha_fin_contrato").exists()){
                            btn_add_fecha_fin.setVisibility(View.GONE);
                            btn_del_fecha_fin.setVisibility(View.VISIBLE);
                            String f_fin = Objects.requireNonNull(datos.child("fecha_fin_contrato").getValue()).toString();
                            int dia2 = Integer.parseInt(f_fin.split("/")[0]);
                            int mes2 = Integer.parseInt(f_fin.split("/")[1]) - 1;
                            int anio2 = Integer.parseInt(f_fin.split("/")[2]);
                            Calendar calendar2 = Calendar.getInstance();
                            calendar2.set(anio2,mes2,dia2);
                            fecha_fin.setDate(calendar2.getTimeInMillis());
                            fecha_cal_fin = calendar2.getTimeInMillis();
                        }else{
                            fecha_fin.setVisibility(View.GONE);
                            btn_del_fecha_fin.setVisibility(View.GONE);
                        }
                        if(datos.child("citas").exists()){
                            cant_solicitudes.setText(datos.child("citas").getChildrenCount()+" Solicitudes");
                        }
                        if(datos.child("marcaciones").exists()){
                            cant_marcaciones.setText(datos.child("marcaciones").getChildrenCount()+" Marcaciones");
                        }
                        if(datos.child("url_foto").exists()){
                            String url_foto = Objects.requireNonNull(datos.child("url_foto").getValue()).toString();
                            Glide.with(getBaseContext()).load(url_foto).centerCrop().into(img_perfil);
                        }else{
                            img_perfil.setImageResource(R.drawable.perfil);
                        }

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });



        }
        fecha_inicio.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);
            view.setDate(calendar.getTimeInMillis());
            fecha_cal_ini = view.getDate();
        });

        fecha_fin.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);
            view.setDate(calendar.getTimeInMillis());
            fecha_cal_fin = view.getDate();
        });
    }
}
