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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.daniminguet.trabajofrasescelebres.MainActivity;
import com.daniminguet.trabajofrasescelebres.R;
import com.daniminguet.trabajofrasescelebres.adaptadores.AdaptadorAutores;
import com.daniminguet.trabajofrasescelebres.interfaces.IAPIService;
import com.daniminguet.trabajofrasescelebres.models.Autor;
import com.daniminguet.trabajofrasescelebres.models.Frase;
import com.daniminguet.trabajofrasescelebres.rest.RestClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentModificarAutor extends Fragment implements SpinnerAdapter {

    private IAPIService apiService;

    public FragmentModificarAutor() {
        super(R.layout.modificar_autor);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        apiService = RestClient.getInstance();
        List<Autor> autores = new ArrayList<>();

        apiService.getAutores().enqueue(new Callback<List<Autor>>() {
            @Override
            public void onResponse(@NonNull Call<List<Autor>> call, @NonNull Response<List<Autor>> response) {
                if(response.isSuccessful()) {
                    assert response.body() != null;
                    autores.addAll(response.body());

                    String[] nombres = new String[autores.size()];
                    String[] campos = {"Nombre", "Nacimiento", "Muerte", "Profesión"};

                    for (int i = 0; i < nombres.length; i++) {
                        nombres[i] = autores.get(i).getNombre();
                    }

                    Spinner sAutores = view.findViewById(R.id.sListaAutores);
                    sAutores.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, nombres));
                    Spinner sCampos = view.findViewById(R.id.sListaCamposAutor);
                    sCampos.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, campos));
                    EditText etNuevoValor = view.findViewById(R.id.etValorAutor);
                    Button btnModificar = view.findViewById(R.id.btnModificarAutor);
                    TextView tvAdvertencias = view.findViewById(R.id.tvAdvertenciaModifAutor);
                    tvAdvertencias.setText("");

                    btnModificar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String autorSeleccionado = sAutores.getSelectedItem().toString();
                            String campoSeleccionado = sCampos.getSelectedItem().toString();
                            String nuevoValor = etNuevoValor.getText().toString();

                            if (nuevoValor.isEmpty()) {
                                etNuevoValor.setError("No has indicado el nuevo valor");
                                etNuevoValor.requestFocus();
                                return;
                            }

                            Autor autorCorrespondiente = null;
                            for (Autor autor : autores) {
                                if (autorSeleccionado.equals(autor.getNombre())) {
                                    autorCorrespondiente = autor;
                                }
                            }

                            switch (campoSeleccionado) {
                                case "Nombre":
                                    autorCorrespondiente.setNombre(nuevoValor);
                                    break;
                                case "Nacimiento":
                                    try {
                                        int nacimiento = Integer.parseInt(nuevoValor);
                                    } catch (NumberFormatException nfe) {
                                        etNuevoValor.setError("Indica la fecha de nacimiento en números");
                                        etNuevoValor.requestFocus();
                                        return;
                                    }

                                    if (nuevoValor.length() > 4) {
                                        etNuevoValor.setError("Año de nacimiento no válido");
                                        etNuevoValor.requestFocus();
                                        return;
                                    } else if (!autorCorrespondiente.getMuerte().isEmpty() && Integer.parseInt(nuevoValor) > Integer.parseInt(autorCorrespondiente.getMuerte())) {
                                        etNuevoValor.setError("El año de nacimiento no puede ser mayor que el de muerte");
                                        etNuevoValor.requestFocus();
                                        return;
                                    }
                                    autorCorrespondiente.setNacimiento(Integer.parseInt(nuevoValor));
                                    break;
                                case "Muerte":
                                    if (nuevoValor.length() > 4) {
                                        etNuevoValor.setError("Año de muerte no válido");
                                        etNuevoValor.requestFocus();
                                        return;
                                    } else if (autorCorrespondiente.getNacimiento() > Integer.parseInt(nuevoValor)) {
                                        etNuevoValor.setError("El año de nacimiento no puede ser mayor que el de muerte");
                                        etNuevoValor.requestFocus();
                                        return;
                                    }
                                    autorCorrespondiente.setMuerte(nuevoValor);
                                    break;
                                case "Profesión":
                                    autorCorrespondiente.setProfesion(nuevoValor);
                                    break;
                            }

                            Log.i(MainActivity.class.getSimpleName(), "Modificando autor...");
                            apiService.updateAutor(autorCorrespondiente).enqueue(new Callback<Boolean>() {
                                @SuppressLint("ResourceAsColor")
                                @Override
                                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                    if(response.isSuccessful()) {
                                        if(response.body()) {
                                            Log.i(MainActivity.class.getSimpleName(), "Autor modificado correctamente");
                                            tvAdvertencias.setTextColor(R.color.blue);
                                            tvAdvertencias.setText("Autor modificado!");
                                            etNuevoValor.setText("");
                                        } else {
                                            Log.i(MainActivity.class.getSimpleName(), "Error al modificar el autor");

                                            Log.i(MainActivity.class.getSimpleName(), response.raw().toString());

                                            tvAdvertencias.setTextColor(R.color.red);
                                            tvAdvertencias.setText("Error al modificar el autor");
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
            public void onFailure(@NonNull Call<List<Autor>> call, @NonNull Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), "No se han podido obtener los autores", Toast.LENGTH_LONG).show();
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
