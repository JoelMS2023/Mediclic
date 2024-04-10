package com.joelmaza.mediclic.Objetos;

public class Usuario {


    public String uid;
    public String nombre;
    public String direccion;
    public String telefono;
    public String email;
    public String url_foto;
    public String rol;
    public String cedula;
    public String clave;
    public String fecha_ini_contrato;
    public String fecha_fin_contrato;

    public String estado;


    public Usuario() {
    }

    public Usuario(String uid, String nombre, String direccion, String telefono, String email, String url_foto, String rol, String cedula, String clave,String estado) {
        this.uid = uid;
        this.nombre=nombre;
        this.direccion=direccion;
        this.email=email;
        this.telefono=telefono;
        this.url_foto=url_foto;
        this.rol=rol;
        this.cedula=cedula;
        this.clave=clave;
        this.estado=estado;
    }
}

