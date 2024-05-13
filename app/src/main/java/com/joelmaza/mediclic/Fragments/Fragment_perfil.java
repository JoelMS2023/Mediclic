package com.joelmaza.mediclic.Fragments;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.glance.ImageProvider;

import com.bumptech.glide.Glide;
import com.canhub.cropper.CropImageContract;
import com.canhub.cropper.CropImageContractOptions;
import com.canhub.cropper.CropImageOptions;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.joelmaza.mediclic.Citas.Det_citas;
import com.joelmaza.mediclic.Citas.Ver_citas;
import com.joelmaza.mediclic.Controllers.Alert_dialog;
import com.joelmaza.mediclic.Controllers.Progress_dialog;
import com.joelmaza.mediclic.Login;
import com.joelmaza.mediclic.MainActivity;
import com.joelmaza.mediclic.Objetos.Usuario;
import com.joelmaza.mediclic.Principal;
import com.joelmaza.mediclic.R;
import com.joelmaza.mediclic.Vi_fotos;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public class Fragment_perfil extends Fragment {
    Button btn_salir, btn_update_profile;
    TextView txt_nombre, txt_cedula,txt_rol, txt_estado,cant_solicitudes,txtfecha_ini_contrato;
    EditText editxt_direccion, editTextTextPhone, editTextTextClave, TextEmail;
    Progress_dialog dialog;
    ImageView img_perfil;
    Alert_dialog alertDialog;
    DatabaseReference dbReference;
    String URL_FOTO = "", NOMBRE = "", clave ="";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista =  inflater.inflate(R.layout.fragment_perfil,container,false);

        txt_nombre = vista.findViewById(R.id.txt_nombre);
        txt_cedula = vista.findViewById(R.id.txt_cedula);
        editxt_direccion=vista.findViewById(R.id.editxt_direccion);
        TextEmail = vista.findViewById(R.id.TextEmail);
        editTextTextPhone = vista.findViewById(R.id.editTextTextPhone);
        btn_salir = vista.findViewById(R.id.btn_salir);
        img_perfil = vista.findViewById(R.id.img_perfil);
        dialog = new Progress_dialog(vista.getContext());
        alertDialog = new Alert_dialog(vista.getContext());
        txt_rol = vista.findViewById(R.id.txt_rol);
        txt_estado= vista.findViewById(R.id.txt_estado);
        editTextTextClave = vista.findViewById(R.id.editTextTextClave);
        btn_update_profile = vista.findViewById(R.id.btn_update_profile);
        txtfecha_ini_contrato = vista.findViewById(R.id.txtfecha_ini_contrato);
        ImageButton imageButton = vista.findViewById(R.id.btn_ver_asist);


        dbReference = MainActivity.DB.getReference();

        //FirebaseUser usuario = MainActivity.mAuth.getCurrentUser();

        if(!Principal.id.isEmpty()){



                MainActivity.ctlUsuario.Obtener_usuario(dbReference, MainActivity.mAuth.getUid(), user -> {

                    txt_nombre.setText(user.nombre.trim());
                    txt_rol.setText(user.rol.toUpperCase().trim());
                    txt_cedula.setText(user.cedula);
                    editxt_direccion.setText(user.direccion);
                    TextEmail.setText(user.email);
                    editTextTextPhone.setText(user.telefono);
                    editTextTextClave.setText((user.clave));
                    URL_FOTO = user.url_foto;

                    if (user.url_foto != null && !user.url_foto.isEmpty()) {
                        Glide.with(vista.getContext().getApplicationContext()).load(user.url_foto).centerCrop().into(img_perfil);
                    }


                });

                img_perfil.setOnClickListener(view1 -> {

                    if( URL_FOTO!=null && !URL_FOTO.isEmpty()) {
                        alertDialog.crear_mensaje("Información", "Selecciona una opción", builder -> {
                            builder.setPositiveButton("Ver Foto", (dialogInterface, i) -> {
                                startActivity(new Intent(getContext(), Vi_fotos.class).putExtra("url", URL_FOTO));
                            });
                            builder.setNeutralButton("Actualizar Foto", (dialogInterface, i) -> upload_foto());
                            builder.setCancelable(true);
                            builder.create().show();
                        });
                    }else{
                        upload_foto();
                    }

                });
                btn_salir.setOnClickListener(view -> {

                    alertDialog.crear_mensaje("Confirmacion", "Estas Seguro que deseas salir", builder -> {
                        builder.setPositiveButton("Aceptar", (dialog1, which) -> {


                            MainActivity.mAuth.signOut();

                            SharedPreferences.Editor editor = Principal.preferences.edit();
                            editor.putString("uid", "");
                            editor.putString("rol", "");
                            editor.apply();
                            startActivity(new Intent(vista.getContext(), Login.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                            requireActivity().finish();

                        });

                        builder.setNegativeButton("Cancelar", (dialog1, which) -> {
                        });
                        builder.setCancelable(false);
                        builder.create().show();
                    });


                });
            imageButton.setOnClickListener(view1 -> {
                startActivity(new Intent(vista.getContext(), Ver_citas.class)
                        .putExtra("uid",Principal.id)
                        .putExtra("nombre", NOMBRE));
            });


                btn_update_profile.setOnClickListener(view -> {

                    dialog.mostrar_mensaje("Actualizando Perfil...");

                    if (!TextEmail.getText().toString().isEmpty() && TextEmail.getError() == null &&
                            !editTextTextPhone.getText().toString().isEmpty() && editTextTextPhone.getError() == null &&
                            !editTextTextClave.getText().toString().isEmpty() &&
                            !editxt_direccion.getText().toString().isEmpty()) {


                        Usuario user = new Usuario();
                        user.uid = Principal.id;
                            user.email = TextEmail.getText().toString();
                            user.telefono = editTextTextPhone.getText().toString();
                            user.direccion =editxt_direccion.getText().toString();
                            user.clave = editTextTextClave.getText().toString();

                            update_perfil(user);

                        dialog.ocultar_mensaje();
                        alertDialog.crear_mensaje("Correcto", "Usuario Actualizado Correctamente", builder -> {
                            builder.setCancelable(false);
                            builder.setNeutralButton("Aceptar", (dialogInterface, i) -> {
                                // Obtener la actividad que contiene el fragmento y finalizarla
                                if (getActivity() != null) {
                                    getActivity().finish();
                                }
                            });
                            builder.create().show();
                        });


                    } else {
                        dialog.ocultar_mensaje();
                        alertDialog.crear_mensaje("¡Advertencia!", "Completa todos los campos", builder -> {
                            builder.setCancelable(true);
                            builder.setNeutralButton("Aceptar", (dialogInterface, i) -> {
                            });
                            builder.create().show();
                        });
                    }
                });

                editTextTextPhone.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if(editable.toString().trim().length() == 10) {
                            if (!validar_celular(editable.toString().trim())) {
                                editTextTextPhone.setError("Ingresa un celular válido");
                            }
                        }else{
                            editTextTextPhone.setError("Ingresa 10 dígitos");
                        }
                    }
                });
                TextEmail.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if(!validar_correo(editable.toString().trim())){
                            TextEmail.setError("Ingresa un correo válido");
                        }
                    }
                });

                txt_rol.setText(Principal.rol);
                Principal.databaseReference.child("usuarios").child(Principal.id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {

                            if (snapshot.child("cedula").exists()) {
                                txt_cedula.setText(Objects.requireNonNull(snapshot.child("cedula").getValue()).toString());
                            }
                            if (snapshot.child("estado").exists()) {
                                txt_estado.setText(Objects.requireNonNull(snapshot.child("estado").getValue()).toString());
                                switch (txt_estado.getText().toString().toLowerCase()) {
                                    case "activo":
                                        txt_estado.setTextColor(ContextCompat.getColor(vista.getContext(), R.color.success));
                                        break;
                                    case "inactivo":
                                        txt_estado.setTextColor(ContextCompat.getColor(vista.getContext(), R.color.danger));
                                        break;
                                    default:
                                        txt_estado.setTextColor(ContextCompat.getColor(vista.getContext(), R.color.proyecto_night));
                                        break;
                                }
                            }
                            if (snapshot.child("nombre").exists()) {
                                NOMBRE = Objects.requireNonNull(snapshot.child("nombre").getValue()).toString();
                                txt_nombre.setText(NOMBRE);
                            }
                            if (snapshot.child("url_foto").exists()) {
                                URL_FOTO = Objects.requireNonNull(snapshot.child("url_foto").getValue()).toString();
                                Glide.with(vista.getContext().getApplicationContext()).load(URL_FOTO).centerCrop().into(img_perfil);
                            } else {
                                img_perfil.setImageResource(R.drawable.imageadd);
                            }
                            if (snapshot.child("clave").exists()) {
                                editTextTextClave.setText(Objects.requireNonNull(snapshot.child("clave").getValue()).toString());
                                clave = Objects.requireNonNull(snapshot.child("clave").getValue()).toString();
                            }
                            if (snapshot.child("telefono").exists()) {
                                editTextTextPhone.setText(Objects.requireNonNull(snapshot.child("telefono").getValue()).toString().trim());
                            }
                            if (snapshot.child("fecha_ini_contrato").exists()) {
                                txtfecha_ini_contrato.setText(Objects.requireNonNull(snapshot.child("fecha_ini_contrato").getValue()).toString());
                            }



                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }


        return vista;

    }
    private void upload_foto(){
        if (isPermitted()) {
            getImageFile();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requestAndroid11StoragePermission();
        } else {
            requestPermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    ActivityResultLauncher<Intent> getImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            if (data != null && data.getData() != null) {
                Uri imageUri = data.getData();
                launchImageCropper(imageUri);
            }
        }
    });

    private final ActivityResultLauncher<String> requestPermission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        if (isGranted) {
            getImageFile();
        } else {
            Toast.makeText(getContext(), "Permiso Denegado", Toast.LENGTH_LONG).show();
        }
    });

    ActivityResultLauncher<Intent> android11StoragePermission = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (isPermitted()) {
            getImageFile();
        } else {
            Toast.makeText(getContext(), "Permiso Denegado", Toast.LENGTH_LONG).show();
        }
    });

    ActivityResultLauncher<CropImageContractOptions> cropImage = registerForActivityResult(new CropImageContract(), result -> {
        if (result.isSuccessful()) {
            Bitmap cropped = BitmapFactory.decodeFile(result.getUriFilePath(requireContext(), true));
            saveCroppedImage(cropped);
        }
    });
    private void saveCroppedImage(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);

        final byte [] thumb_byte = byteArrayOutputStream.toByteArray();
        StorageReference ref = Principal.storageReference.child("usuarios").child(Principal.id);

        dialog.mostrar_mensaje("Actualizando Foto...");
        ref.putBytes(thumb_byte).addOnSuccessListener(taskSnapshot -> {

            ref.getDownloadUrl().addOnSuccessListener(uri -> {
                URL_FOTO = uri.toString();
                Usuario user = new Usuario();
                user.uid = Principal.id;
                user.url_foto = URL_FOTO ;
                MainActivity.ctlUsuario.update_foto(dbReference,user);
                dialog.ocultar_mensaje();
                alertDialog.crear_mensaje("Correcto", "Foto Actualizada Correctamente", builder -> {
                    builder.setCancelable(false);
                    builder.setNeutralButton("Aceptar", (dialogInterface, i) -> {});
                    builder.create().show();
                });
            }).addOnFailureListener(e -> {
                dialog.ocultar_mensaje();
                Toast.makeText(getContext(), "Ocurrió un error al actualizar la foto",Toast.LENGTH_LONG).show();
            });

        }).addOnFailureListener(e -> {
            dialog.ocultar_mensaje();
            Toast.makeText(getContext(), "Ocurrió un error al actualizar la foto",Toast.LENGTH_LONG).show();
        });

    }

    private void getImageFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        getImage.launch(intent);
    }

    @TargetApi(Build.VERSION_CODES.R)
    private void requestAndroid11StoragePermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setData(Uri.parse(String.format("package:%s", requireContext().getPackageName())));
        android11StoragePermission.launch(intent);
    }

    private boolean isPermitted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void launchImageCropper(Uri uri) {
        CropImageOptions cropImageOptions = new CropImageOptions();
        cropImageOptions.imageSourceIncludeGallery = false;
        cropImageOptions.imageSourceIncludeCamera = true;
        cropImageOptions.outputCompressFormat = Bitmap.CompressFormat.JPEG;
        cropImageOptions.outputCompressQuality = 90;
        cropImageOptions.aspectRatioX = 1;
        cropImageOptions.aspectRatioY = 1;
        cropImageOptions.maxCropResultWidth = 512;
        cropImageOptions.maxCropResultHeight = 512;
        cropImageOptions.minCropResultHeight = 512;
        cropImageOptions.minCropResultWidth = 512;
        CropImageContractOptions cropImageContractOptions = new CropImageContractOptions(uri, cropImageOptions);
        cropImage.launch(cropImageContractOptions);
    }
    public void update_perfil(Usuario usuario) {

        if(usuario.uid != null) {
            Map<String, Object> datos = new HashMap<>();
            datos.put("email", usuario.email.toLowerCase());
            datos.put("telefono", usuario.telefono);
            datos.put("direccion",usuario.direccion);

            if(!clave.equals(usuario.clave)){
                if(!Principal.preferences.getString("uid_biometric","").isEmpty()){
                    SharedPreferences.Editor editor = Principal.preferences.edit();
                    editor.putString("uid_biometric","");
                    editor.apply();
                }
                datos.put("clave", usuario.clave);
            }

            Principal.databaseReference.child("usuarios").child(usuario.uid).updateChildren(datos);


        }

    }
    public boolean validar_celular(String celular){

        Pattern patron = Pattern.compile("^(0|593)?9[0-9]\\d{7}$");

        return patron.matcher(celular).matches();

    }

    public boolean validar_correo(String correo){

        Pattern patron = Pattern.compile("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.([a-zA-Z]{2,4})+$");

        return patron.matcher(correo).matches();

    }





}



