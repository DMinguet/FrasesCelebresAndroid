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
import com.daniminguet.trabajofrasescelebres.rest.RestClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentAnyadirAutor extends Fragment {
    private IAPIService apiService;
    boolean valido = true;

    public FragmentAnyadirAutor() {
        super(R.layout.anyadir_autor);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        apiService = RestClient.getInstance();

        EditText etNombre = view.findViewById(R.id.etNombreAutor);
        EditText etAnyoNacimiento = view.findViewById(R.id.etAnyoNacimiento);
        EditText etAnyoMuerte = view.findViewById(R.id.etAnyoMuerte);
        EditText etProfesion = view.findViewById(R.id.etProfesion);
        Button btnAnyadir = view.findViewById(R.id.btnAnyadirAutor);
        TextView tvResultado = view.findViewById(R.id.tvAdvertenciaAnyadirAutor);
        tvResultado.setText("");

        btnAnyadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = etNombre.getText().toString();
                String anyoNacimiento = etAnyoNacimiento.getText().toString();
                String anyoMuerte = etAnyoMuerte.getText().toString();
                String profesion = etProfesion.getText().toString();
                comprobarAutor(nombre);

                if (nombre.isEmpty()) {
                    etNombre.setError("Se requiere un nombre de autor");
                    etNombre.requestFocus();
                    return;
                } else if (!valido) {
                    etNombre.setError("El autor ya está creado");
                    etNombre.requestFocus();
                    return;
                } else if (anyoNacimiento.isEmpty()) {
                    etAnyoNacimiento.setError("Se requiere un año de nacimiento");
                    etAnyoNacimiento.requestFocus();
                    return;
                } else if (anyoNacimiento.length() > 4) {
                    etAnyoNacimiento.setError("Año de nacimiento no válido");
                    etAnyoNacimiento.requestFocus();
                    return;
                } else if (profesion.isEmpty()) {
                    etProfesion.setError("Se requiere una profesión");
                    etProfesion.requestFocus();
                    return;
                }

                Autor nuevoAutor = new Autor(nombre, Integer.parseInt(anyoNacimiento), anyoMuerte, profesion);

                Log.i(MainActivity.class.getSimpleName(), "Añadiendo autor ...");
                apiService.addAutor(nuevoAutor).enqueue(new Callback<Boolean>() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if(response.isSuccessful()) {
                            if(response.body()) {
                                Log.i(MainActivity.class.getSimpleName(), "Autor añadido correctamente");
                                tvResultado.setTextColor(R.color.blue);
                                tvResultado.setText("Autor añadido!");
                                etNombre.setText("");
                                etAnyoNacimiento.setText("");
                                etAnyoMuerte.setText("");
                                etProfesion.setText("");
                            } else {
                                Log.i(MainActivity.class.getSimpleName(), "Error al añadir el autor");

                                Log.i(MainActivity.class.getSimpleName(), response.raw().toString());

                                tvResultado.setTextColor(R.color.red);
                                tvResultado.setText("Error al añadir el autor");
                                etNombre.setText("");
                                etAnyoNacimiento.setText("");
                                etAnyoMuerte.setText("");
                                etProfesion.setText("");
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

    public void comprobarAutor(String nombre) {
        apiService.getAutores().enqueue(new Callback<List<Autor>>() {
            @Override
            public void onResponse(@NonNull Call<List<Autor>> call, @NonNull Response<List<Autor>> response) {
                if(response.isSuccessful()) {
                    for (Autor autor : response.body()) {
                        if (autor.getNombre().equalsIgnoreCase(nombre)) {
                            valido = false;
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Autor>> call, @NonNull Throwable t) {
                Log.i(MainActivity.class.getSimpleName(), t.getMessage());
            }
        });

        valido = true;
    }
}
