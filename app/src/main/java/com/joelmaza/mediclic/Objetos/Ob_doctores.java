package com.joelmaza.mediclic.Objetos;

public class Ob_doctores {
    public String uid;
    public String nombre;
    public String direccion;
    public String telefono;
    public String email;
    public String url_foto;
    public String rol;

    public Ob_doctores(String uid, String nombre, String direccion, String telefono, String email, String url_foto, String rol) {
        this.uid = uid;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.url_foto = url_foto;
        this.rol = rol;
    }

    public Ob_doctores() {

    }
}
