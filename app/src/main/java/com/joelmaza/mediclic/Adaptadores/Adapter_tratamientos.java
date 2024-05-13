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
import com.joelmaza.mediclic.Solicitudes.Det_solicitud;
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

        holder.card_tipo.setText(list_tratamientos.get(position).tipo);






    }

    @Override
    public int getItemCount() {
        return list_tratamientos.size();
    }



}