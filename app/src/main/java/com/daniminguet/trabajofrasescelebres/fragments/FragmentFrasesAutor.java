package com.daniminguet.trabajofrasescelebres.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daniminguet.trabajofrasescelebres.R;
import com.daniminguet.trabajofrasescelebres.adaptadores.AdaptadorAutores;
import com.daniminguet.trabajofrasescelebres.adaptadores.AdaptadorFrases;
import com.daniminguet.trabajofrasescelebres.adaptadores.AdaptadorFrasesAutor;
import com.daniminguet.trabajofrasescelebres.interfaces.IAPIService;
import com.daniminguet.trabajofrasescelebres.models.Autor;
import com.daniminguet.trabajofrasescelebres.models.Frase;
import com.daniminguet.trabajofrasescelebres.rest.RestClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentFrasesAutor extends Fragment {
    public interface IOnAttachListener {
        Autor getAutorSeleccionado();
    }

    private Autor autor;

    public FragmentFrasesAutor() {
        super(R.layout.lista);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView rvLista = view.findViewById(R.id.rvLista);
        IAPIService apiService = RestClient.getInstance();
        List<Frase> frases = new ArrayList<>();
        List<Frase> frasesAutor = new ArrayList<>();

        apiService.getFrases().enqueue(new Callback<List<Frase>>() {
            @Override
            public void onResponse(@NonNull Call<List<Frase>> call, @NonNull Response<List<Frase>> response) {
                if(response.isSuccessful()) {
                    assert response.body() != null;
                    frases.addAll(response.body());

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
            }

            @Override
            public void onFailure(@NonNull Call<List<Frase>> call, @NonNull Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), "No se han podido obtener las frases", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        IOnAttachListener attachListener = (IOnAttachListener) context;
        autor = attachListener.getAutorSeleccionado();
    }
}
