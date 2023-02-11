package com.daniminguet.trabajofrasescelebres.fragments;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

import com.daniminguet.trabajofrasescelebres.MainActivity;
import com.daniminguet.trabajofrasescelebres.R;
import com.daniminguet.trabajofrasescelebres.interfaces.IAPIService;
import com.daniminguet.trabajofrasescelebres.models.Autor;
import com.daniminguet.trabajofrasescelebres.models.Categoria;
import com.daniminguet.trabajofrasescelebres.models.Frase;
import com.daniminguet.trabajofrasescelebres.rest.RestClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentModificarFrase extends Fragment implements SpinnerAdapter {
    private String[] frasesString;
    private String[] nombresAutor;
    private IAPIService apiService;

    public FragmentModificarFrase() {
        super(R.layout.modificar_frase);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        apiService = RestClient.getInstance();
        List<Frase> frases = new ArrayList<>();
        List<Autor> autores = new ArrayList<>();
        List<Categoria> categorias = new ArrayList<>();

        apiService.getFrases().enqueue(new Callback<List<Frase>>() {
            @Override
            public void onResponse(@NonNull Call<List<Frase>> call, @NonNull Response<List<Frase>> response) {
                if(response.isSuccessful()) {
                    assert response.body() != null;
                    frases.addAll(response.body());
                    frasesString = new String[frases.size()];
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Frase>> call, @NonNull Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), "No se han podido obtener las frases", Toast.LENGTH_LONG).show();
            }
        });

        apiService.getAutores().enqueue(new Callback<List<Autor>>() {
            @Override
            public void onResponse(@NonNull Call<List<Autor>> call, @NonNull Response<List<Autor>> response) {
                if(response.isSuccessful()) {
                    assert response.body() != null;
                    autores.addAll(response.body());
                    nombresAutor = new String[autores.size()];
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

                    String[] nombresCategoria = new String[categorias.size()];
                    String[] campos = {"Texto", "Fecha programada", "Autor", "Categoría"};

                    for (int i = 0; i < frasesString.length; i++) {
                        frasesString[i] = frases.get(i).getTexto();
                    }
                    for (int i = 0; i < nombresAutor.length; i++) {
                        nombresAutor[i] = autores.get(i).getNombre();
                    }
                    for (int i = 0; i < nombresCategoria.length; i++) {
                        nombresCategoria[i] = categorias.get(i).getNombre();
                    }

                    Spinner sFrases = view.findViewById(R.id.sFrases);
                    sFrases.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, frasesString));

                    Spinner sCampos = view.findViewById(R.id.sCamposFrase);
                    sCampos.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, campos));

                    Spinner sNuevoValor = view.findViewById(R.id.sNuevoValor);
                    sNuevoValor.setVisibility(View.INVISIBLE);

                    EditText etNuevoValor = view.findViewById(R.id.etNuevoValorFrase);
                    etNuevoValor.setVisibility(View.VISIBLE);

                    Button btnModificar = view.findViewById(R.id.btnModificarFrase);
                    TextView tvAdvertencias = view.findViewById(R.id.tvAdvertenciaModifFrase);
                    tvAdvertencias.setText("");

                    sCampos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String campoSeleccionado = sCampos.getSelectedItem().toString();

                            switch (campoSeleccionado) {
                                case "Texto":
                                    etNuevoValor.setInputType(View.AUTOFILL_TYPE_TEXT);
                                    sNuevoValor.setVisibility(View.INVISIBLE);
                                    etNuevoValor.setVisibility(View.VISIBLE);
                                    break;
                                case "Fecha programada":
                                    etNuevoValor.setInputType(View.AUTOFILL_TYPE_DATE);
                                    sNuevoValor.setVisibility(View.INVISIBLE);
                                    etNuevoValor.setVisibility(View.VISIBLE);
                                    break;
                                case "Autor":
                                    sNuevoValor.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, nombresAutor));
                                    sNuevoValor.setVisibility(View.VISIBLE);
                                    etNuevoValor.setVisibility(View.INVISIBLE);
                                    break;
                                case "Categoría":
                                    sNuevoValor.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, nombresCategoria));
                                    sNuevoValor.setVisibility(View.VISIBLE);
                                    etNuevoValor.setVisibility(View.INVISIBLE);
                                    break;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });




                    btnModificar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String fraseSeleccionada = sFrases.getSelectedItem().toString();
                            String campoSeleccionado = sCampos.getSelectedItem().toString();
                            String nuevoValor;

                            Frase fraseCorrespondiente = null;
                            for (Frase frase : frases) {
                                if (frase.getTexto().equalsIgnoreCase(fraseSeleccionada)) {
                                    fraseCorrespondiente = frase;
                                }
                            }

                            switch (campoSeleccionado) {
                                case "Texto":
                                    nuevoValor = etNuevoValor.getText().toString();

                                    if (nuevoValor.isEmpty()) {
                                        etNuevoValor.setError("No has indicado el nuevo valor");
                                        etNuevoValor.requestFocus();
                                        return;
                                    }

                                    fraseCorrespondiente.setTexto(nuevoValor);

                                    modificarFrase(fraseCorrespondiente);

                                    break;
                                case "Fecha programada":
                                    nuevoValor = etNuevoValor.getText().toString();

                                    if (nuevoValor.isEmpty()) {
                                        etNuevoValor.setError("No has indicado el nuevo valor");
                                        etNuevoValor.requestFocus();
                                        return;
                                    } else if (nuevoValor.length() > 10) {
                                        etNuevoValor.setError("Fecha programada no válida (yyyy-mm-dd)");
                                        etNuevoValor.requestFocus();
                                        return;
                                    }

                                    fraseCorrespondiente.setFechaprogramada(nuevoValor);

                                    modificarFrase(fraseCorrespondiente);

                                    break;
                                case "Autor":
                                    nuevoValor = sNuevoValor.getSelectedItem().toString();

                                    Autor autorSeleccionado = null;
                                    for (Autor autor : autores) {
                                        if (autor.getNombre().equals(nuevoValor)) {
                                            autorSeleccionado = autor;
                                        }
                                    }

                                    fraseCorrespondiente.setAutorId(autorSeleccionado.getId());

                                    modificarFrase(fraseCorrespondiente);

                                    break;
                                case "Categoría":
                                    nuevoValor = sNuevoValor.getSelectedItem().toString();

                                    Categoria categoriaSeleccionada = null;
                                    for (Categoria categoria : categorias) {
                                        if (categoria.getNombre().equals(nuevoValor)) {
                                            categoriaSeleccionada = categoria;
                                        }
                                    }

                                    fraseCorrespondiente.setCategoriaId(categoriaSeleccionada.getId());

                                    modificarFrase(fraseCorrespondiente);

                                    break;
                            }
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

    private void modificarFrase(Frase frase) {
        apiService.updateFrase(frase).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.body()) {
                    Log.i(MainActivity.class.getSimpleName(), "Autor modificado correctamente");
                    Toast.makeText(getContext(), "Frase modificada correctamente", Toast.LENGTH_LONG).show();
                } else {
                    Log.i(MainActivity.class.getSimpleName(), "Error al modificar el autor");

                    Log.i(MainActivity.class.getSimpleName(), response.raw().toString());

                    Toast.makeText(getContext(), "Error al modificar la frase", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.i(MainActivity.class.getSimpleName(), t.getMessage());
            }
        });
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
