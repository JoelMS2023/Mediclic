package com.joelmaza.mediclic.Holders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.joelmaza.mediclic.R;

public class Holder_tratamientos extends RecyclerView.ViewHolder {

    public TextView card_fecha,  card_tipo, card_estado, card_empleado;
    public CardView cardView;


    public Holder_tratamientos(@NonNull View itemView) {
        super(itemView);

        card_fecha = itemView.findViewById(R.id.card_fecha);
        card_tipo = itemView.findViewById(R.id.card_tipo);
        card_estado = itemView.findViewById(R.id.card_estado);
        card_empleado = itemView.findViewById(R.id.card_empleado);
        cardView =  itemView.findViewById(R.id.cardview_tratamientos);

    }
}
