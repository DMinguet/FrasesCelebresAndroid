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
import com.daniminguet.trabajofrasescelebres.models.Autor;
import com.daniminguet.trabajofrasescelebres.models.Frase;

import java.util.List;

public class FragmentTodasFrases extends Fragment {
    public interface IOnAttachListener {
        List<Frase> getFrasesConsultas();
        List<Autor> getAutoresConsultas();
    }

    private List<Frase> frases;
    private List<Autor> autores;

    public FragmentTodasFrases() {
        super(R.layout.lista);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rvLista = view.findViewById(R.id.rvLista);

        AdaptadorFrases adaptadorFrases = new AdaptadorFrases(frases, autores);
        rvLista.setHasFixedSize(true);
        rvLista.setAdapter(adaptadorFrases);
        rvLista.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        IOnAttachListener attachListener = (IOnAttachListener) context;
        frases = attachListener.getFrasesConsultas();
        autores = attachListener.getAutoresConsultas();
    }
}
