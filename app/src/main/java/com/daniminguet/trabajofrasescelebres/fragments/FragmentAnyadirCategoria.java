package com.daniminguet.trabajofrasescelebres.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.daniminguet.trabajofrasescelebres.MainActivity;
import com.daniminguet.trabajofrasescelebres.R;
import com.daniminguet.trabajofrasescelebres.interfaces.IAPIService;
import com.daniminguet.trabajofrasescelebres.models.Autor;
import com.daniminguet.trabajofrasescelebres.models.Categoria;
import com.daniminguet.trabajofrasescelebres.rest.RestClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentAnyadirCategoria extends Fragment {

    private IAPIService apiService;
    boolean valido = true;

    public FragmentAnyadirCategoria() {
        super(R.layout.anyadir_categoria);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        apiService = RestClient.getInstance();

        EditText etNombre = view.findViewById(R.id.etNombreCategoria);
        TextView tvAdvertencias = view.findViewById(R.id.tvAdvertenciasAnyadirCategoria);
        Button btnAnyadir = view.findViewById(R.id.btnAnyadirCategoria);
        tvAdvertencias.setText("");

        btnAnyadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = etNombre.getText().toString();
                comprobarCategoria(nombre);

                if (nombre.isEmpty()) {
                    etNombre.setError("Se requiere un nombre de categoría");
                    etNombre.requestFocus();
                    return;
                } else if (!valido) {
                    etNombre.setError("La categoría ya existe");
                    etNombre.requestFocus();
                    return;
                }

                Categoria nuevaCategoria = new Categoria(nombre);

                Log.i(MainActivity.class.getSimpleName(), "Añadiendo categoría ...");
                apiService.addCategoria(nuevaCategoria).enqueue(new Callback<Boolean>() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if(response.isSuccessful()) {
                            if(response.body()) {
                                Log.i(MainActivity.class.getSimpleName(), "Categoría añadida correctamente");
                                tvAdvertencias.setTextColor(R.color.blue);
                                tvAdvertencias.setText("Categoría añadida!");
                                etNombre.setText("");
                            } else {
                                Log.i(MainActivity.class.getSimpleName(), "Error al añadir la categoría");

                                Log.i(MainActivity.class.getSimpleName(), response.raw().toString());

                                tvAdvertencias.setTextColor(R.color.red);
                                tvAdvertencias.setText("Error al añadir el autor");
                                etNombre.setText("");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        Log.i(MainActivity.class.getSimpleName(), t.getMessage());
                    }
                });
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    public void comprobarCategoria(String nombre) {
        apiService.getCategorias().enqueue(new Callback<List<Categoria>>() {
            @Override
            public void onResponse(@NonNull Call<List<Categoria>> call, @NonNull Response<List<Categoria>> response) {
                if(response.isSuccessful()) {
                    for (Categoria categoria : response.body()) {
                        if (categoria.getNombre().equalsIgnoreCase(nombre)) {
                            valido = false;
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Categoria>> call, @NonNull Throwable t) {
                Log.i(MainActivity.class.getSimpleName(), t.getMessage());
            }
        });

        valido = true;
    }
}
