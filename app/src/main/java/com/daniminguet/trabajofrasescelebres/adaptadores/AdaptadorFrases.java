package com.daniminguet.trabajofrasescelebres.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daniminguet.trabajofrasescelebres.R;
import com.daniminguet.trabajofrasescelebres.models.Autor;
import com.daniminguet.trabajofrasescelebres.models.Frase;

import java.util.List;

public class AdaptadorFrases extends RecyclerView.Adapter<AdaptadorFrases.ViewHolder>{
    private final List<Frase> frases;
    private final List<Autor> autores;

    public AdaptadorFrases(List<Frase> frases, List<Autor> autores) {
        this.frases = frases;
        this.autores = autores;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_frases, parent, false);

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
        private final TextView tvAutor;
        private final TextView tvFrase;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvAutor = itemView.findViewById(R.id.tvAutor);
            this.tvFrase = itemView.findViewById(R.id.tvFrase);
        }

        public void bindFrase(Frase frase) {
            for (Autor autor : autores) {
                if (autor.getId() == frase.getAutorId()) {
                    tvAutor.setText(autor.getNombre());
                }
            }
            tvFrase.setText(frase.getTexto());
        }
    }
}
