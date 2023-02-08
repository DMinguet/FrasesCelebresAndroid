package com.daniminguet.trabajofrasescelebres.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daniminguet.trabajofrasescelebres.R;
import com.daniminguet.trabajofrasescelebres.adaptadores.AdaptadorCategorias;
import com.daniminguet.trabajofrasescelebres.interfaces.ICategoriaListener;
import com.daniminguet.trabajofrasescelebres.models.Categoria;

import java.util.List;

public class FragmentCategorias extends Fragment {
    public interface IOnAttachListener {
        List<Categoria> getCategoriasCategorias();
    }

    private List<Categoria> categorias;

    private ICategoriaListener listener;

    public FragmentCategorias() {
        super(R.layout.lista);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rvLista = view.findViewById(R.id.rvLista);

        AdaptadorCategorias adaptadorCategorias = new AdaptadorCategorias(categorias, listener);
        rvLista.setHasFixedSize(true);
        rvLista.setAdapter(adaptadorCategorias);
        rvLista.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (ICategoriaListener) context;
        IOnAttachListener attachListener = (IOnAttachListener) context;
        categorias = attachListener.getCategoriasCategorias();
    }
}
