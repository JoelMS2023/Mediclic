package com.joelmaza.mediclic.Adaptadores;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.joelmaza.mediclic.Holders.Holder_usuarios;
import com.joelmaza.mediclic.Objetos.Usuario;
import com.joelmaza.mediclic.R;
import com.joelmaza.mediclic.Vi_det_usuario;


import java.util.ArrayList;
import java.util.List;



public class Adaptador_usuarios extends RecyclerView.Adapter<Holder_usuarios> {

    List<Usuario> lista_usuarios =new ArrayList<>();
    Context context;

    public Adaptador_usuarios(Context context){
        this.context=context;

    }

    public void Clear(){

        lista_usuarios.clear();
    }

    public void Add_usuarios(Usuario user){
        lista_usuarios.add(user);
        notifyItemInserted(lista_usuarios.size());

    }
    @NonNull
    @Override
    public Holder_usuarios onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview_usuario,parent,false);


        return new Holder_usuarios(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder_usuarios holder, int position) {

        holder.card_nombre.setText(lista_usuarios.get(position).nombre);
        holder.card_direccion.setText(lista_usuarios.get(position).direccion);
        holder.card_telefono.setText(lista_usuarios.get(position).telefono);
        holder.card_rol.setText(lista_usuarios.get(position).rol);


        holder.cardview_usuario.setOnClickListener(view ->{


            Intent i = new Intent();
            i.setClass(context, Vi_det_usuario.class);
            i.putExtra("uid",lista_usuarios.get(position).uid);
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return lista_usuarios.size();
    }
}

