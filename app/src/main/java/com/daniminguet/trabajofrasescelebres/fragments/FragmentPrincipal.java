package com.daniminguet.trabajofrasescelebres.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daniminguet.trabajofrasescelebres.LoginActivity;
import com.daniminguet.trabajofrasescelebres.MainActivity;
import com.daniminguet.trabajofrasescelebres.R;
import com.daniminguet.trabajofrasescelebres.models.Frase;
import com.daniminguet.trabajofrasescelebres.models.Usuario;

import java.util.Random;

public class FragmentPrincipal extends Fragment {
    public interface IOnAttachListener {
        Usuario getUser();
        Frase getFraseDia();
    }

    private Usuario user;
    private Frase frase;

    public FragmentPrincipal() {
        super(R.layout.principal);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView tvBienvenida = view.findViewById(R.id.tvBienvenida);
        TextView tvFrase = view.findViewById(R.id.tvFraseDia);
        Button btnConsultas = view.findViewById(R.id.btnConsultas);
        Button btnAdmin = view.findViewById(R.id.btnAdmin);

        tvBienvenida.setText("Hola de nuevo " + user.getNombre() + "!");
        tvFrase.setText(frase.getTexto());

        if (user.getAdmin() == 0) {
            btnAdmin.setVisibility(View.INVISIBLE);
        }

        btnConsultas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getParentFragmentManager();
                manager.beginTransaction()
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .replace(R.id.frgPrincipal, FragmentConsultas.class, null)
                        .commit();
            }
        });

        btnAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getParentFragmentManager();
                manager.beginTransaction()
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .replace(R.id.frgPrincipal, FragmentAdmin.class, null)
                        .commit();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        IOnAttachListener attachListener = (IOnAttachListener) context;
        user = attachListener.getUser();
        frase = attachListener.getFraseDia();
    }
}