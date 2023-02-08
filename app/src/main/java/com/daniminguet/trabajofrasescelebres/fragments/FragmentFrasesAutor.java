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
import com.daniminguet.trabajofrasescelebres.adaptadores.AdaptadorFrases;
import com.daniminguet.trabajofrasescelebres.adaptadores.AdaptadorFrasesAutor;
import com.daniminguet.trabajofrasescelebres.models.Autor;
import com.daniminguet.trabajofrasescelebres.models.Frase;

import java.util.ArrayList;
import java.util.List;

public class FragmentFrasesAutor extends Fragment {
    public interface IOnAttachListener {
        List<Frase> getFrasesAutor();
        Autor getAutorSeleccionado();
    }

    private List<Frase> frases, frasesAutor = new ArrayList<>();
    private Autor autor;

    public FragmentFrasesAutor() {
        super(R.layout.lista);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rvLista = view.findViewById(R.id.rvLista);

        for (Frase frase : frases) {
            if (frase.getAutorId() == autor.getId()) {
                frasesAutor.add(frase);
            }
        }

        AdaptadorFrasesAutor adaptadorFrases = new AdaptadorFrasesAutor(frasesAutor, autor);
        rvLista.setHasFixedSize(true);
        rvLista.setAdapter(adaptadorFrases);
        rvLista.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        IOnAttachListener attachListener = (IOnAttachListener) context;
        frases = attachListener.getFrasesAutor();
        autor = attachListener.getAutorSeleccionado();
    }
}
