package com.joelmaza.mediclic.Holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.joelmaza.mediclic.R;

public class Holder_usuarios extends RecyclerView.ViewHolder {


    public TextView card_nombre, card_cedula, card_telefono, card_rol,card_estado,card_canton;
    public ImageView card_foto;
    public CardView cardview_usuario;

    public Holder_usuarios(@NonNull View itemView) {
        super(itemView);
        card_cedula = itemView.findViewById(R.id.card_cedula);
        card_nombre = itemView.findViewById(R.id.card_nombre);
        card_estado = itemView.findViewById(R.id.card_estado);
        card_telefono = itemView.findViewById(R.id.card_telefono);
        card_rol = itemView.findViewById(R.id.card_rol);
        card_foto = itemView.findViewById(R.id.card_foto);
        card_canton = itemView.findViewById(R.id.card_canton);
        cardview_usuario = itemView.findViewById(R.id.cardview_usuario);





    }
}
