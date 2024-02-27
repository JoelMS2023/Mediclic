package com.joelmaza.mediclic.Controllers;

import android.app.AlertDialog;

import com.joelmaza.mediclic.Objetos.Usuario;

public class Interfaces {

    public Interfaces(){}

    public interface build{
        void verbuilder(AlertDialog.Builder builder);
    }

    public interface perfil{
        void verPerfil(Usuario user);
    }
    public interface Obt_rol{

        void rol(String rol);

    }


}

