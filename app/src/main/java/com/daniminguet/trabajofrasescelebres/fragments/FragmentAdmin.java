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

public class FragmentAdmin extends Fragment implements View.OnClickListener{

    public FragmentAdmin() {
        super(R.layout.admin);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnAnyadirAutor = view.findViewById(R.id.btnAnyadirAutorAdmin);
        Button btnModificarAutor = view.findViewById(R.id.btnModificarAutorAdmin);
        Button btnAnyadirCategoria = view.findViewById(R.id.btnAnyadirCategoria);
        Button btnModificarCategoria = view.findViewById(R.id.btnModificarCategoriaAdmin);
        Button btnAnyadirFrase = view.findViewById(R.id.btnAnyadirFraseAdmin);
        Button btnModificarFrase = view.findViewById(R.id.btnModificarFraseAdmin);
        Button btnVolver = view.findViewById(R.id.btnVolverAdmin);
        btnAnyadirAutor.setOnClickListener(this);
        btnModificarAutor.setOnClickListener(this);
        btnAnyadirCategoria.setOnClickListener(this);
        btnModificarCategoria.setOnClickListener(this);
        btnAnyadirFrase.setOnClickListener(this);
        btnModificarFrase.setOnClickListener(this);
        btnVolver.setOnClickListener(this);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onClick(View v) {
        FragmentManager manager = getParentFragmentManager();
        switch (v.getId()) {
            case R.id.btnAnyadirAutorAdmin:
                manager.beginTransaction()
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .replace(R.id.frgPrincipal, FragmentAnyadirAutor.class, null)
                        .commit();
                break;
            case R.id.btnModificarAutorAdmin:
                manager.beginTransaction()
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .replace(R.id.frgPrincipal, FragmentModificarAutor.class, null)
                        .commit();
                break;
            case R.id.btnAnyadirCategoria:
                manager.beginTransaction()
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .replace(R.id.frgPrincipal, FragmentConsultas.class, null)
                        .commit();
                break;
            case R.id.btnModificarCategoriaAdmin:
                manager.beginTransaction()
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .replace(R.id.frgPrincipal, FragmentConsultas.class, null)
                        .commit();
                break;
            case R.id.btnAnyadirFraseAdmin:
                manager.beginTransaction()
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .replace(R.id.frgPrincipal, FragmentConsultas.class, null)
                        .commit();
                break;
            case R.id.btnModificarFraseAdmin:
                manager.beginTransaction()
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .replace(R.id.frgPrincipal, FragmentConsultas.class, null)
                        .commit();
                break;
            case R.id.btnVolverAdmin:
                manager.beginTransaction()
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .replace(R.id.frgPrincipal, FragmentPrincipal.class, null)
                        .commit();
                break;
            default:
                break;
        }
    }
}
