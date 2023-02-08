package com.daniminguet.trabajofrasescelebres.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daniminguet.trabajofrasescelebres.R;
import com.daniminguet.trabajofrasescelebres.models.Autor;
import com.daniminguet.trabajofrasescelebres.models.Categoria;
import com.daniminguet.trabajofrasescelebres.models.Frase;

import java.util.List;

public class AdaptadorFrasesCategoria extends RecyclerView.Adapter<AdaptadorFrasesCategoria.ViewHolder>{
    private final List<Frase> frases;
    private final Categoria categoria;

    public AdaptadorFrasesCategoria(List<Frase> frases, Categoria categoria) {
        this.frases = frases;
        this.categoria = categoria;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_frases_categoria, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Frase frase = frases.get(position);

        holder.bindFrase(frase);
    }

    public int getItemCount() {
        if (frases == null) {
            return 0;
        } else {
            return frases.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvCategoria;
        private final TextView tvFrase;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvCategoria = itemView.findViewById(R.id.tvCategoria);
            this.tvFrase = itemView.findViewById(R.id.tvFraseCateg);
        }

        public void bindFrase(Frase frase) {
            tvCategoria.setText(categoria.getNombre());
            tvFrase.setText(frase.getTexto());
        }
    }
}
