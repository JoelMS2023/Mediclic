package com.joelmaza.mediclic.Adaptadores;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.joelmaza.mediclic.Citas.Add_citas;
import com.joelmaza.mediclic.Fragments.Dialog_Fragment_Usuarios;
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
        holder.card_telefono.setText(lista_usuarios.get(position).telefono);
        holder.card_estado.setText(lista_usuarios.get(position).estado);
        holder.card_cedula.setText(lista_usuarios.get(position).cedula);

        if(lista_usuarios.get(position).estado!=null){
            switch (lista_usuarios.get(position).estado.toLowerCase()){
                case "activo":
                    holder.card_estado.setTextColor(ContextCompat.getColor(context,R.color.success));
                    break;
                case "inactivo":
                    holder.card_estado.setTextColor(ContextCompat.getColor(context,R.color.danger));
                    break;
                default:
                    holder.card_estado.setTextColor(ContextCompat.getColor(context,R.color.proyecto_night));
                    break;
            }
        }
        holder.card_rol.setText(lista_usuarios.get(position).rol);

        if(lista_usuarios.get(position).url_foto != null){
            Glide.with(context).load(lista_usuarios.get(position).url_foto).centerCrop().into(holder.card_foto);
        }else{
            holder.card_foto.setImageResource(R.drawable.perfil);
        }


        holder.cardview_usuario.setOnClickListener(view ->{

            if(Dialog_Fragment_Usuarios.dialogFragment != null){

                Add_citas.card_cedula.setText(lista_usuarios.get(position).cedula);
                Add_citas.card_nombre.setText(lista_usuarios.get(position).nombre);
                Add_citas.UID_EMPLEADO = lista_usuarios.get(position).uid;
                Dialog_Fragment_Usuarios.dialogFragment.dismiss();

            }else {


                Intent i = new Intent();
                i.setClass(context, Vi_det_usuario.class);
                i.putExtra("uid", lista_usuarios.get(position).uid);
                context.startActivity(i);
            }
        });
        holder.cardview_usuario.setOnLongClickListener(view -> {

            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            String text = lista_usuarios.get(position).telefono;
            ClipData clip = ClipData.newPlainText("telefono",  text);
            clipboard.setPrimaryClip(clip);

            Toast.makeText(context,"Telefono copiado al Portapapeles",Toast.LENGTH_SHORT).show();

            return true;
        });
    }

    @Override
    public int getItemCount() {
        return lista_usuarios.size();
    }
}

