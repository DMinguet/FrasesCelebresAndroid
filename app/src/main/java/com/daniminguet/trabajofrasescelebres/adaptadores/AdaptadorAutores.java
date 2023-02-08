package com.daniminguet.trabajofrasescelebres.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daniminguet.trabajofrasescelebres.R;
import com.daniminguet.trabajofrasescelebres.interfaces.IAutorListener;
import com.daniminguet.trabajofrasescelebres.models.Autor;

import java.util.List;

public class AdaptadorAutores extends RecyclerView.Adapter<AdaptadorAutores.ViewHolder>{
    private final List<Autor> autores;
    private final IAutorListener autorListener;

    public AdaptadorAutores(List<Autor> autores, IAutorListener autorListener) {
        this.autores = autores;
        this.autorListener = autorListener;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_autores, parent, false);

        return new ViewHolder(itemView, autorListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Autor autor = autores.get(position);

        holder.bindAutor(autor);
    }

    public int getItemCount() {
        if (autores == null) {
            return 0;
        } else {
            return autores.size();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView tvNombre;
        private final TextView tvNacimiento;
        private final TextView tvProfesion;
        private final IAutorListener autorListener;

        public ViewHolder(@NonNull View itemView, IAutorListener autorListener) {
            super(itemView);
            this.tvNombre = itemView.findViewById(R.id.tvNombre);
            this.tvNacimiento = itemView.findViewById(R.id.tvNacimiento);
            this.tvProfesion = itemView.findViewById(R.id.tvProfesion);
            this.autorListener = autorListener;
            itemView.setOnClickListener(this);
        }

        public void bindAutor(Autor autor) {
            tvNombre.setText(autor.getNombre());
            tvNacimiento.setText("Año nacimiento: " + String.valueOf(autor.getNacimiento()));
            tvProfesion.setText("Profesión: " + autor.getProfesion());
        }

        @Override
        public void onClick(View v) {
            if (autorListener != null) {
                autorListener.onAutorSeleccionado(getAdapterPosition());
            }
        }
    }
}
