package com.joelmaza.mediclic.Adaptadores;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.joelmaza.mediclic.Citas.Det_citas;
import com.joelmaza.mediclic.Controllers.Ctl_citas;
import com.joelmaza.mediclic.Holders.Holder_citas;
import com.joelmaza.mediclic.Objetos.Ob_citas;
import com.joelmaza.mediclic.Objetos.Ob_horario;
import com.joelmaza.mediclic.Principal;
import com.joelmaza.mediclic.R;

import java.util.ArrayList;
import java.util.List;

public class Adapter_citas extends RecyclerView.Adapter<Holder_citas> {

    public List<Ob_citas> list_actividad = new ArrayList<>();
    Context context;


    public Adapter_citas(Context context) {
        this.context = context;
    }

    public void AddActividad (Ob_citas obActividad ){
        list_actividad.add(obActividad);
        notifyItemInserted(list_actividad.size());
    }

    public void ClearActividad (){
        list_actividad.clear();
    }

    @NonNull
    @Override
    public Holder_citas onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview_citas,parent,false);
        return new Holder_citas(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder_citas holder, int position) {

        holder.card_fecha.setText(list_actividad.get(position).fecha_inicio + " - " +list_actividad.get(position).hora_inicio);
        holder.card_fecha_fin.setText(list_actividad.get(position).fecha_fin + " - " +list_actividad.get(position).hora_fin);
        holder.card_tipo.setText(list_actividad.get(position).tipo);
        holder.card_estado.setText(list_actividad.get(position).estado);

        if(list_actividad.get(position).estado!=null){
            switch (list_actividad.get(position).estado.toLowerCase()){
                case "pendiente":
                    holder.card_estado.setTextColor(ContextCompat.getColor(context,R.color.warning));
                    break;
                case "finalizado":
                    holder.card_estado.setTextColor(ContextCompat.getColor(context,R.color.success));
                    break;
                default:
                    holder.card_estado.setTextColor(ContextCompat.getColor(context,R.color.proyecto_dark));
                    break;
            }
        }

        if(Principal.rol.equals("Administrador")) {
            holder.card_empleado.setVisibility(View.VISIBLE);
            holder.card_empleado.setText(list_actividad.get(position).empleado + " - " + list_actividad.get(position).ced_empleado);
        }else{
            holder.card_empleado.setVisibility(View.GONE);
            holder.card_empleado.setText("");
        }

        holder.cardView.setOnClickListener(view -> {

            Intent i = new Intent();
            i.setClass(context, Det_citas.class);
            i.putExtra("uid",list_actividad.get(position).uid);
            i.putExtra("uid_empleado",list_actividad.get(position).uid_empleado);
            i.putExtra("ced_empleado",list_actividad.get(position).ced_empleado);
            i.putExtra("nom_empleado",list_actividad.get(position).empleado);
            context.startActivity(i);

        });

    }

    @Override
    public int getItemCount() {
        return list_actividad.size();
    }



}
