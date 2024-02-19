package com.joelmaza.mediclic.Controllers;

import com.google.firebase.database.DatabaseReference;
import com.joelmaza.mediclic.Objetos.Ob_citas;
import com.joelmaza.mediclic.Objetos.Ob_horario;

public class Ctl_citas {

    DatabaseReference dbref;
    public Ctl_citas(DatabaseReference dbref) {
        this.dbref = dbref;
    }

    public void crear_cita(Ob_citas obCitas){

        dbref.child("citas").push().setValue(obCitas);

    }
    public void eliminar_citas(String uid){
        dbref.child("citas").child(uid).removeValue();
    }


    }
