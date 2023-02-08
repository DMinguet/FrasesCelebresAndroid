package com.daniminguet.trabajofrasescelebres.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daniminguet.trabajofrasescelebres.R;
import com.daniminguet.trabajofrasescelebres.models.Frase;
import com.daniminguet.trabajofrasescelebres.models.Usuario;

public class FragmentPrincipal extends Fragment {
    public interface IOnAttachListener {
        Usuario getUser();
        //Frase getFraseDelDia();
    }

    private Usuario user;
    private TextView tvBienvenida, tvFraseDelDia;
    private Button btnConsultas, btnAdmin;
    private Frase fraseDelDia;

    public FragmentPrincipal() {
        super(R.layout.principal);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvBienvenida = view.findViewById(R.id.tvBienvenida);
        tvFraseDelDia = view.findViewById(R.id.tvFraseCorrespondiente);
        btnConsultas = view.findViewById(R.id.btnConsultas);
        btnAdmin = view.findViewById(R.id.btnAdmin);

        tvBienvenida.setText("Hola de nuevo " + user.getNombre() + "!");
        //tvFraseDelDia.setText(fraseDelDia.getTexto());

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
        //fraseDelDia = attachListener.getFraseDelDia();
    }
}