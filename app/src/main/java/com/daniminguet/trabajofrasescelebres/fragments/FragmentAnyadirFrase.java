package com.daniminguet.trabajofrasescelebres.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.DataSetObserver;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.daniminguet.trabajofrasescelebres.MainActivity;
import com.daniminguet.trabajofrasescelebres.R;
import com.daniminguet.trabajofrasescelebres.adaptadores.AdaptadorAutores;
import com.daniminguet.trabajofrasescelebres.interfaces.IAPIService;
import com.daniminguet.trabajofrasescelebres.models.Autor;
import com.daniminguet.trabajofrasescelebres.models.Categoria;
import com.daniminguet.trabajofrasescelebres.models.Frase;
import com.daniminguet.trabajofrasescelebres.rest.RestClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentAnyadirFrase extends Fragment implements SpinnerAdapter {
    private String[] nombresAutores;
    private IAPIService apiService;
    boolean valido = true;

    public FragmentAnyadirFrase() {
        super(R.layout.anyadir_frase);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        apiService = RestClient.getInstance();
        List<Autor> autores = new ArrayList<>();
        List<Categoria> categorias =  new ArrayList<>();

        apiService.getAutores().enqueue(new Callback<List<Autor>>() {
            @Override
            public void onResponse(@NonNull Call<List<Autor>> call, @NonNull Response<List<Autor>> response) {
                if(response.isSuccessful()) {
                    assert response.body() != null;
                    autores.addAll(response.body());
                    nombresAutores = new String[autores.size()];
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Autor>> call, @NonNull Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), "No se han podido obtener los autores", Toast.LENGTH_LONG).show();
            }
        });

        apiService.getCategorias().enqueue(new Callback<List<Categoria>>() {
            @Override
            public void onResponse(@NonNull Call<List<Categoria>> call, @NonNull Response<List<Categoria>> response) {
                if(response.isSuccessful()) {
                    assert response.body() != null;
                    categorias.addAll(response.body());

                    String[] nombresCategorias = new String[categorias.size()];

                    for (int i = 0; i < nombresAutores.length; i++) {
                        nombresAutores[i] = autores.get(i).getNombre();
                    }
                    for (int i = 0; i < nombresCategorias.length; i++) {
                        nombresCategorias[i] = categorias.get(i).getNombre();
                    }

                    EditText etFrase = view.findViewById(R.id.etTexto);
                    EditText etFechaProgramada = view.findViewById(R.id.etFechaProgramada);
                    Spinner sAutores = view.findViewById(R.id.sAutoresAnyadirFrase);
                    sAutores.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, nombresAutores));
                    Spinner sCategorias = view.findViewById(R.id.sCategAnyadirFrase);
                    sCategorias.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, nombresCategorias));
                    TextView tvAdvertencias = view.findViewById(R.id.tvAdvertenciaAnyadirFrase);
                    Button btnAnyadir = view.findViewById(R.id.btnAnyadirFrase);
                    tvAdvertencias.setText("");

                    btnAnyadir.setOnClickListener(new View.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onClick(View v) {
                            String frase = etFrase.getText().toString();
                            String fechaProgramada = etFechaProgramada.getText().toString();
                            String autorSeleccionado = sAutores.getSelectedItem().toString();
                            String categoriaSeleccionada = sCategorias.getSelectedItem().toString();
                            comprobarFrase(frase);

                            if (frase.isEmpty()) {
                                etFrase.setError("Se requiere una frase");
                                etFrase.requestFocus();
                                return;
                            } else if (fechaProgramada.isEmpty()) {
                                etFechaProgramada.setError("Se requiere una fecha programada");
                                etFechaProgramada.requestFocus();
                                return;
                            } else if (fechaProgramada.length() != 10) {
                                etFechaProgramada.setError("Fecha programada no válida (yyyy-mm-dd)");
                                etFechaProgramada.requestFocus();
                                return;
                            } else if (!valido) {
                                etFrase.setError("La frase ya está creada");
                                etFrase.requestFocus();
                                return;
                            }

                            try {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                sdf.setLenient(false);
                                sdf.parse(fechaProgramada);
                            } catch (ParseException e) {
                                etFechaProgramada.setError("Fecha programada no válida (yyyy-mm-dd)");
                                etFechaProgramada.requestFocus();
                                return;
                            }

                            Autor autorCorrespondiente = null;
                            for (Autor autor : autores) {
                                if (autorSeleccionado.equals(autor.getNombre())) {
                                    autorCorrespondiente = autor;
                                }
                            }
                            Categoria categoriaCorrespondiente = null;
                            for (Categoria categoria : categorias) {
                                if (categoriaSeleccionada.equals(categoria.getNombre())) {
                                    categoriaCorrespondiente = categoria;
                                }
                            }

                            Frase nuevaFrase = new Frase(frase, fechaProgramada, autorCorrespondiente.getId(), categoriaCorrespondiente.getId());

                            Log.i(MainActivity.class.getSimpleName(), "Añadiendo frase...");
                            apiService.addFrase(nuevaFrase).enqueue(new Callback<Boolean>() {
                                @SuppressLint("ResourceAsColor")
                                @Override
                                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                    if(response.isSuccessful()) {
                                        if(response.body()) {
                                            Log.i(MainActivity.class.getSimpleName(), "Frase añadido correctamente");
                                            tvAdvertencias.setTextColor(R.color.blue);
                                            tvAdvertencias.setText("Frase añadida!");
                                            etFrase.setText("");
                                            etFechaProgramada.setText("");
                                        } else {
                                            Log.i(MainActivity.class.getSimpleName(), "Error al añadir la frase");

                                            Log.i(MainActivity.class.getSimpleName(), response.raw().toString());

                                            tvAdvertencias.setTextColor(R.color.red);
                                            tvAdvertencias.setText("Error al añadir la frase");
                                            etFrase.setText("");
                                            etFechaProgramada.setText("");
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<Boolean> call, Throwable t) {
                                    tvAdvertencias.setText("Fallo al añadir la frase");
                                    Log.i(MainActivity.class.getSimpleName(), t.getMessage());
                                }
                            });
                        }
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Categoria>> call, @NonNull Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), "No se han podido obtener las categorias", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    public void comprobarFrase(String texto) {
        apiService.getFrases().enqueue(new Callback<List<Frase>>() {
            @Override
            public void onResponse(@NonNull Call<List<Frase>> call, @NonNull Response<List<Frase>> response) {
                if(response.isSuccessful()) {
                    for (Frase frase : response.body()) {
                        if (frase.getTexto().equalsIgnoreCase(texto)) {
                            valido = false;
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Frase>> call, @NonNull Throwable t) {
                Log.i(MainActivity.class.getSimpleName(), t.getMessage());
            }
        });

        valido = true;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
