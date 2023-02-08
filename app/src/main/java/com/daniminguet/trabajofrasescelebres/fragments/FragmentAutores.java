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
import com.daniminguet.trabajofrasescelebres.adaptadores.AdaptadorAutores;
import com.daniminguet.trabajofrasescelebres.interfaces.IAutorListener;
import com.daniminguet.trabajofrasescelebres.models.Autor;
import com.daniminguet.trabajofrasescelebres.models.Frase;

import java.util.List;

public class FragmentAutores extends Fragment {
    public interface IOnAttachListener {
        List<Autor> getAutoresAutores();
    }

    private List<Autor> autores;

    private IAutorListener listener;

    public FragmentAutores() {
        super(R.layout.lista);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rvLista = view.findViewById(R.id.rvLista);

        AdaptadorAutores adaptadorAutores = new AdaptadorAutores(autores, listener);
        rvLista.setHasFixedSize(true);
        rvLista.setAdapter(adaptadorAutores);
        rvLista.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (IAutorListener) context;
        IOnAttachListener attachListener = (IOnAttachListener) context;
        autores = attachListener.getAutoresAutores();
    }
}
