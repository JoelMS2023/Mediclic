package com.joelmaza.mediclic.Adaptadores;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.joelmaza.mediclic.Controllers.Ctl_citas;
import com.joelmaza.mediclic.Holders.Holder_citas;
import com.joelmaza.mediclic.Objetos.Ob_citas;
import com.joelmaza.mediclic.Objetos.Ob_horario;

import java.util.ArrayList;
import java.util.List;

public class Adapter_citas extends RecyclerView.Adapter<Holder_citas> {


    @NonNull
    @Override
    public Holder_citas onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder_citas holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
