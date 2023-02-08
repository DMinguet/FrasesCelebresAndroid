package com.daniminguet.trabajofrasescelebres.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.daniminguet.trabajofrasescelebres.R;
import com.daniminguet.trabajofrasescelebres.models.Autor;
import com.daniminguet.trabajofrasescelebres.models.Categoria;
import com.daniminguet.trabajofrasescelebres.models.Frase;

import java.util.List;

public class FragmentConsultas extends Fragment {

    public FragmentConsultas() {
        super(R.layout.consultas);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnAutores = view.findViewById(R.id.btnFrasesPorAutor);
        Button btnCategorias = view.findViewById(R.id.btnFrasesPorCategoria);
        Button btnVolver = view.findViewById(R.id.btnVolver);

        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.beginTransaction()
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .replace(R.id.frgConsultas, FragmentTodasFrases.class, null)
                .commit();

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getParentFragmentManager();
                manager.beginTransaction()
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .replace(R.id.frgPrincipal, FragmentPrincipal.class, null)
                        .commit();
            }
        });

        btnAutores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getParentFragmentManager();
                manager.beginTransaction()
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .replace(R.id.frgConsultas, FragmentAutores.class, null)
                        .commit();
            }
        });

        btnCategorias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getParentFragmentManager();
                manager.beginTransaction()
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .replace(R.id.frgConsultas, FragmentCategorias.class, null)
                        .commit();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }
}
