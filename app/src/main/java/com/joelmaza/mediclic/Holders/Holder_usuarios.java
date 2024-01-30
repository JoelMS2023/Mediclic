package com.joelmaza.mediclic.Holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.joelmaza.mediclic.R;

public class Holder_usuarios extends RecyclerView.ViewHolder {


    public TextView card_nombre, card_direccion, card_telefono;
    public ImageView card_foto;
    public CardView cardview_usuario;

    public Holder_usuarios(@NonNull View itemView) {
        super(itemView);
        card_nombre=(TextView) itemView.findViewById(R.id.card_nombre);
        card_direccion=(TextView) itemView.findViewById(R.id.card_direccion);
        card_telefono=(TextView) itemView.findViewById(R.id.card_telefono);
        card_foto=(ImageView) itemView.findViewById(R.id.card_foto);
        cardview_usuario=(CardView) itemView.findViewById(R.id.cardview_usuario);

    }
}
