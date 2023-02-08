package com.daniminguet.trabajofrasescelebres.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daniminguet.trabajofrasescelebres.R;
import com.daniminguet.trabajofrasescelebres.interfaces.ICategoriaListener;
import com.daniminguet.trabajofrasescelebres.models.Categoria;

import java.util.List;

public class AdaptadorCategorias extends RecyclerView.Adapter<AdaptadorCategorias.ViewHolder>{
    private final List<Categoria> categorias;
    private final ICategoriaListener categoriaListener;

    public AdaptadorCategorias(List<Categoria> categorias, ICategoriaListener categoriaListener) {
        this.categorias = categorias;
        this.categoriaListener = categoriaListener;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_categorias, parent, false);

        return new ViewHolder(itemView, categoriaListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Categoria categoria = categorias.get(position);

        holder.bindCategoria(categoria);
    }

    public int getItemCount() {
        if (categorias == null) {
            return 0;
        } else {
            return categorias.size();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView tvCategoria;
        private final ICategoriaListener categoriaListener;

        public ViewHolder(@NonNull View itemView, ICategoriaListener categoriaListener) {
            super(itemView);
            this.tvCategoria = itemView.findViewById(R.id.tvCategoriaCateg);
            this.categoriaListener = categoriaListener;
            itemView.setOnClickListener(this);
        }

        public void bindCategoria(Categoria categoria) {
            tvCategoria.setText(categoria.getNombre());
        }

        @Override
        public void onClick(View v) {
            if (categoriaListener != null) {
                categoriaListener.onCategoriaSeleccionada(getAdapterPosition());
            }
        }
    }
}
