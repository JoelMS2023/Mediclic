package com.joelmaza.mediclic.Adaptadores;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.joelmaza.mediclic.Holders.Holder_doctores;
import com.joelmaza.mediclic.Objetos.Ob_doctores;

import com.joelmaza.mediclic.R;
import com.joelmaza.mediclic.Vi_det_usuario;

import java.util.ArrayList;
import java.util.List;

public class Adapter_doctores extends RecyclerView.Adapter<Holder_doctores>{
    List<Ob_doctores> lista_doctores =new ArrayList<>();
    Context context;

    public Adapter_doctores(Context context){
        this.context=context;

    }

    public void Clear(){

        lista_doctores.clear();
    }

    public void Add_usuarios(Ob_doctores user){
        lista_doctores.add(user);
        notifyItemInserted(lista_doctores.size());

    }
    @NonNull
    @Override
    public Holder_doctores onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview_doctores,parent,false);


        return new Holder_doctores(view);

    }

    @Override
    public void onBindViewHolder(@NonNull Holder_doctores holder, int position) {
        holder.card_nombre.setText(lista_doctores.get(position).nombre);
        holder.card_direccion.setText(lista_doctores.get(position).direccion);
        holder.card_telefono.setText(lista_doctores.get(position).telefono);


        holder.cardview_doctores.setOnClickListener(view ->{


            Intent i = new Intent();
            i.setClass(context, Vi_det_usuario.class);
            i.putExtra("uid",lista_doctores.get(position).uid);
            context.startActivity(i);
        });
    }


    @Override
    public int getItemCount() {
        return 0;
    }
}
