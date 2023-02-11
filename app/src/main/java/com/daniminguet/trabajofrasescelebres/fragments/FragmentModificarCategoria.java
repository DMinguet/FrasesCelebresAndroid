package com.daniminguet.trabajofrasescelebres.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.DataSetObserver;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.daniminguet.trabajofrasescelebres.MainActivity;
import com.daniminguet.trabajofrasescelebres.R;
import com.daniminguet.trabajofrasescelebres.interfaces.IAPIService;
import com.daniminguet.trabajofrasescelebres.models.Autor;
import com.daniminguet.trabajofrasescelebres.models.Categoria;
import com.daniminguet.trabajofrasescelebres.rest.RestClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentModificarCategoria extends Fragment implements SpinnerAdapter {

    private IAPIService apiService;

    public FragmentModificarCategoria() {
        super(R.layout.modificar_categoria);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        apiService = RestClient.getInstance();
        List<Categoria> categorias = new ArrayList<>();

        apiService.getCategorias().enqueue(new Callback<List<Categoria>>() {
            @Override
            public void onResponse(@NonNull Call<List<Categoria>> call, @NonNull Response<List<Categoria>> response) {
                if(response.isSuccessful()) {
                    assert response.body() != null;
                    categorias.addAll(response.body());

                    String[] nombres = new String[categorias.size()];

                    for (int i = 0; i < nombres.length; i++) {
                        nombres[i] = categorias.get(i).getNombre();
                    }

                    Spinner sCategorias = view.findViewById(R.id.sListaCategorias);
                    sCategorias.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, nombres));
                    EditText etNuevoValor = view.findViewById(R.id.etValorCategoria);
                    TextView tvAdvertencias = view.findViewById(R.id.tvAdvertenciasModifCateg);
                    Button btnModificar = view.findViewById(R.id.btnModificarCategoria);
                    tvAdvertencias.setText("");

                    btnModificar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String categoriaSeleccionada = sCategorias.getSelectedItem().toString();
                            String nuevoValor = etNuevoValor.getText().toString();

                            if (nuevoValor.isEmpty()) {
                                etNuevoValor.setError("No has indicado el nuevo valor");
                                etNuevoValor.requestFocus();
                                return;
                            }

                            Categoria categoriaCorrespondiente = null;
                            for (Categoria categoria : categorias) {
                                if (categoriaSeleccionada.equals(categoria.getNombre())) {
                                    categoriaCorrespondiente = categoria;
                                }
                            }

                            categoriaCorrespondiente.setNombre(nuevoValor);

                            Log.i(MainActivity.class.getSimpleName(), "Modificando categoría...");
                            apiService.updateCategoria(categoriaCorrespondiente).enqueue(new Callback<Boolean>() {
                                @SuppressLint("ResourceAsColor")
                                @Override
                                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                    if(response.isSuccessful()) {
                                        if(response.body()) {
                                            Log.i(MainActivity.class.getSimpleName(), "Categoría modificada correctamente");
                                            tvAdvertencias.setTextColor(R.color.blue);
                                            tvAdvertencias.setText("Categoría modificada!");
                                            etNuevoValor.setText("");
                                        } else {
                                            Log.i(MainActivity.class.getSimpleName(), "Error al modificar la categoría");

                                            Log.i(MainActivity.class.getSimpleName(), response.raw().toString());

                                            tvAdvertencias.setTextColor(R.color.red);
                                            tvAdvertencias.setText("Error al modificar la categoría");
                                            etNuevoValor.setText("");
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
