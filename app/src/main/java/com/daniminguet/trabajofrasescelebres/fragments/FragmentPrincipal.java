package com.daniminguet.trabajofrasescelebres.fragments;

import android.content.Intent;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.daniminguet.trabajofrasescelebres.R;
import com.daniminguet.trabajofrasescelebres.models.Usuario;

public class FragmentPrincipal extends Fragment {
    public interface IOnAttachListener {
        Usuario getUser();
    }

    private Usuario user;

    public FragmentPrincipal() {
        super(R.layout.principal);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }
}