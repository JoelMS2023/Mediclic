package com.joelmaza.mediclic.Controllers;

import com.google.firebase.database.DatabaseReference;
import com.joelmaza.mediclic.Objetos.Ob_doctores;
import com.joelmaza.mediclic.Objetos.Usuario;

import java.util.HashMap;
import java.util.Map;

public class Ctl_doctores {
    DatabaseReference dbref;
    public Ctl_doctores(){}

    public void crear_usuario(DatabaseReference dbref, String uid, Usuario usuario){

        dbref.child("doctores").child(uid).setValue(usuario);

    }


    public void actualizar_usuario(DatabaseReference dbref, Ob_doctores usuario){

        Map<String, Object> datos = new HashMap<>();
        datos.put("direccion", usuario.direccion);
        datos.put("telefono",usuario.telefono);
        datos.put("email",usuario.email);

        dbref.child("doctores").child(usuario.uid).updateChildren(datos);

    }

    public void eliminar_usuario(DatabaseReference dbref, String uid_usuario){

        dbref.child("doctores").child(uid_usuario).removeValue();

    }


}

