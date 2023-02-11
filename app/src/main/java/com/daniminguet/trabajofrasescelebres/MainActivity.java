package com.daniminguet.trabajofrasescelebres;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceManager;

import com.daniminguet.trabajofrasescelebres.fragments.FragmentAutores;
import com.daniminguet.trabajofrasescelebres.fragments.FragmentCategorias;
import com.daniminguet.trabajofrasescelebres.fragments.FragmentConsultas;
import com.daniminguet.trabajofrasescelebres.fragments.FragmentFrasesAutor;
import com.daniminguet.trabajofrasescelebres.fragments.FragmentFrasesCategoria;
import com.daniminguet.trabajofrasescelebres.fragments.FragmentTodasFrases;
import com.daniminguet.trabajofrasescelebres.fragments.FragmentPrincipal;
import com.daniminguet.trabajofrasescelebres.interfaces.IAPIService;
import com.daniminguet.trabajofrasescelebres.interfaces.IAutorListener;
import com.daniminguet.trabajofrasescelebres.interfaces.ICategoriaListener;
import com.daniminguet.trabajofrasescelebres.models.Autor;
import com.daniminguet.trabajofrasescelebres.models.Categoria;
import com.daniminguet.trabajofrasescelebres.models.Frase;
import com.daniminguet.trabajofrasescelebres.models.Usuario;
import com.daniminguet.trabajofrasescelebres.rest.RestClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements FragmentPrincipal.IOnAttachListener,
        FragmentTodasFrases.IOnAttachListener, FragmentAutores.IOnAttachListener, IAutorListener,
        FragmentFrasesAutor.IOnAttachListener, FragmentCategorias.IOnAttachListener,
        ICategoriaListener, FragmentFrasesCategoria.IOnAttachListener
{
    private boolean tabletLayout;
    private final IAPIService apiService = RestClient.getInstance();;
    private Usuario activeUser;
    private SharedPreferences prefs;
    private List<Frase> frases = new ArrayList<>();
    private Frase frase;
    private List<Autor> autores = new ArrayList<>();
    private Autor autorSeleccionado;
    private List<Categoria> categorias = new ArrayList<>();
    private Categoria categoriaSeleccionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println(getIntent().getSerializableExtra("fraseDia"));
        frase = (Frase) getIntent().getSerializableExtra("fraseDia");
        System.out.println(frase);
        setContentView(R.layout.activity_main);

        cargarDatos();
    }

    private void cargarDatos() {
        getAutores();
        getCategorias();
        getFrases();
        getFrase();
        loadActiveUser();

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("usernamePref", activeUser.getNombre());
        editor.putString("passwordPref", activeUser.getContrasenya());
        editor.putString("ip", RestClient.IP_INSTI);
        editor.putString("port", String.valueOf(RestClient.PORT));
        editor.apply();
    }

    private void loadActiveUser() {
        if (activeUser == null) {
            activeUser = (Usuario) getIntent().getSerializableExtra("user");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.iPreferencias) {
            startActivity(new Intent(MainActivity.this, PreferencesActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getAutores() {
        apiService.getAutores().enqueue(new Callback<List<Autor>>() {
            @Override
            public void onResponse(@NonNull Call<List<Autor>> call, @NonNull Response<List<Autor>> response) {
                if(response.isSuccessful()) {
                    assert response.body() != null;
                    autores.addAll(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Autor>> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, "No se han podido obtener los autores", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getCategorias() {
        apiService.getCategorias().enqueue(new Callback<List<Categoria>>() {
            @Override
            public void onResponse(@NonNull Call<List<Categoria>> call, @NonNull Response<List<Categoria>> response) {
                if(response.isSuccessful()) {
                    assert response.body() != null;
                    categorias.addAll(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Categoria>> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, "No se han podido obtener las categorias", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getFrases() {
        apiService.getFrases().enqueue(new Callback<List<Frase>>() {
            @Override
            public void onResponse(@NonNull Call<List<Frase>> call, @NonNull Response<List<Frase>> response) {
                if(response.isSuccessful()) {
                    assert response.body() != null;
                    frases.addAll(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Frase>> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, "No se han podido obtener las frases", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getFrasesLimit(int offset) {
        apiService.getFrasesLimit(offset).enqueue(new Callback<List<Frase>>() {
            @Override
            public void onResponse(Call<List<Frase>> call, Response<List<Frase>> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    frases.addAll(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Frase>> call, Throwable t) {

            }
        });
    }

    public void getFrase() {
        apiService.getFraseDelDia(new Date()).enqueue(new Callback<Frase>() {
            @Override
            public void onResponse(Call<Frase> call, Response<Frase> response) {
                if(response.isSuccessful()) {
                    frase = response.body();
                }
            }

            @Override
            public void onFailure(Call<Frase> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void addFraseValues() {
        Log.i(MainActivity.class.getSimpleName(), "Añadiendo frase ...");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        try {
            apiService.addFraseValues("Frase Values", sdf.parse("2021-02-09"), 1, 1).enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                    if(response.isSuccessful()) {
                        if(response.body()) {
                            Log.i(MainActivity.class.getSimpleName(), "Frase añadida correctamente");
                        } else {
                            Log.i(MainActivity.class.getSimpleName(), "Error al añadir la frase");

                            Log.i(MainActivity.class.getSimpleName(), response.raw().toString());
                        }
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public void addAutor(Autor autor) {
        Log.i(MainActivity.class.getSimpleName(), "Añadiendo autor ...");
        apiService.addAutor(autor).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.isSuccessful()) {
                    if(response.body()) {
                        Log.i(MainActivity.class.getSimpleName(), "Autor añadido correctamente");
                    } else {
                        Log.i(MainActivity.class.getSimpleName(), "Error al añadir el autor");

                        Log.i(MainActivity.class.getSimpleName(), response.raw().toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void addCategoria(Categoria categoria) {
        apiService.addCategoria(categoria).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                if(response.isSuccessful()) {
                    if(response.body()) {
                        Log.i(MainActivity.class.getSimpleName(), "Categoria añadida correctamente");
                    } else {
                        Log.i(MainActivity.class.getSimpleName(), "Error al añadir la categoria");

                        Log.i(MainActivity.class.getSimpleName(), response.raw().toString());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void addFrase(Frase frase) {
        apiService.addFrase(frase).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                if(response.isSuccessful()) {
                    if(response.body()) {
                        Log.i(MainActivity.class.getSimpleName(), "Frase añadida correctamente");
                    } else {
                        Log.i(MainActivity.class.getSimpleName(), "Error al añadir la frase");

                        Log.i(MainActivity.class.getSimpleName(), response.raw().toString());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public Usuario getUser() {
        if (activeUser == null) {
            loadActiveUser();
        }
        return activeUser;
    }


    @Override
    public List<Frase> getFrasesConsultas() {
        return frases;
    }

    @Override
    public List<Autor> getAutoresConsultas() {
        return autores;
    }

    @Override
    public List<Autor> getAutoresAutores() {
        return autores;
    }

    @Override
    public void onAutorSeleccionado(int id) {
        autorSeleccionado = autores.get(id);

        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .replace(R.id.frgConsultas, FragmentFrasesAutor.class, null)
                .commit();
    }

    @Override
    public List<Frase> getFrasesAutor() {
        return frases;
    }

    @Override
    public Autor getAutorSeleccionado() {
        return autorSeleccionado;
    }

    @Override
    public List<Categoria> getCategoriasCategorias() {
        return categorias;
    }

    @Override
    public List<Frase> getFrasesCategoria() {
        return frases;
    }

    @Override
    public Categoria getCategoriaSeleccionada() {
        return categoriaSeleccionada;
    }

    @Override
    public void onCategoriaSeleccionada(int id) {
        categoriaSeleccionada = categorias.get(id);

        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .replace(R.id.frgConsultas, FragmentFrasesCategoria.class, null)
                .commit();
    }
}