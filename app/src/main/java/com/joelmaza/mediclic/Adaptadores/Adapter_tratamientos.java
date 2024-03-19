package com.joelmaza.mediclic.Adaptadores;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.joelmaza.mediclic.Holders.Holder_tratamientos;
import com.joelmaza.mediclic.Objetos.Ob_tratamientos;
import com.joelmaza.mediclic.Principal;
import com.joelmaza.mediclic.R;
import com.joelmaza.mediclic.Tratamientos.Det_tratamientos;

import java.util.ArrayList;
import java.util.List;

public class Adapter_tratamientos extends RecyclerView.Adapter<Holder_tratamientos>{
    public List<Ob_tratamientos> list_tratamientos = new ArrayList<>();
    Context context;

    public Adapter_tratamientos(Context context) {
        this.context = context;
    }

    public void AddActividad (Ob_tratamientos obTratamientos ){
        list_tratamientos.add(obTratamientos);
        notifyItemInserted(list_tratamientos.size());
    }

    public void ClearActividad (){
        list_tratamientos.clear();
    }

    @NonNull
    @Override
    public Holder_tratamientos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview_tratamientos,parent,false);
        return new Holder_tratamientos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder_tratamientos holder, int position) {

        holder.card_fecha.setText(list_tratamientos.get(position).fecha_inicio + " - " +list_tratamientos.get(position).hora_inicio);
        holder.card_tipo.setText(list_tratamientos.get(position).tipo);
        holder.card_estado.setText(list_tratamientos.get(position).estado);

        if(list_tratamientos.get(position).estado!=null){
            switch (list_tratamientos.get(position).estado.toLowerCase()){
                case "pendiente":
                    holder.card_estado.setTextColor(ContextCompat.getColor(context,R.color.warning));
                    break;
                case "finalizado":
                    holder.card_estado.setTextColor(ContextCompat.getColor(context,R.color.success));
                    break;
                default:
                    holder.card_estado.setTextColor(ContextCompat.getColor(context,R.color.proyecto_night));
                    break;
            }
        }

        if(Principal.rol.equals("Administrador")) {
            holder.card_empleado.setVisibility(View.VISIBLE);
            holder.card_empleado.setText(list_tratamientos.get(position).empleado + " - " + list_tratamientos.get(position).ced_empleado);
        }else{
            holder.card_empleado.setVisibility(View.GONE);
            holder.card_empleado.setText("");
        }

        holder.cardView.setOnClickListener(view -> {

            Intent i = new Intent();
            i.setClass(context, Det_tratamientos.class);
            i.putExtra("uid",list_tratamientos.get(position).uid);
            i.putExtra("uid_empleado",list_tratamientos.get(position).uid_empleado);
            i.putExtra("ced_empleado",list_tratamientos.get(position).ced_empleado);
            i.putExtra("nom_empleado",list_tratamientos.get(position).empleado);
            context.startActivity(i);

        });

    }

    @Override
    public int getItemCount() {
        return list_tratamientos.size();
    }



}