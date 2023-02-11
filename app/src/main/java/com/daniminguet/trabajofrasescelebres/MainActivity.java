package com.daniminguet.trabajofrasescelebres;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceManager;

import com.daniminguet.trabajofrasescelebres.fragments.FragmentAnyadirFrase;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements FragmentPrincipal.IOnAttachListener,
        IAutorListener, FragmentFrasesAutor.IOnAttachListener, ICategoriaListener,
        FragmentFrasesCategoria.IOnAttachListener
{
    private final IAPIService apiService = RestClient.getInstance();;
    private Usuario activeUser;
    private List<Frase> frases;
    private Frase frase;
    private List<Autor> autores;
    private Autor autorSeleccionado;
    private List<Categoria> categorias;
    private Categoria categoriaSeleccionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        frases = new ArrayList<>();
        autores = new ArrayList<>();
        categorias = new ArrayList<>();

        cargarDatos();
    }

    private void cargarDatos() {
        getAutores();
        getCategorias();
        getFrases();
        getFrase();
        loadActiveUser();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

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

    @Override
    public Usuario getUser() {
        if (activeUser == null) {
            loadActiveUser();
        }
        return activeUser;
    }

    @Override
    public void onAutorSeleccionado(int id) {
        apiService.getAutores().enqueue(new Callback<List<Autor>>() {
            @Override
            public void onResponse(@NonNull Call<List<Autor>> call, @NonNull Response<List<Autor>> response) {
                if(response.isSuccessful()) {
                    assert response.body() != null;
                    autores.clear();
                    autores.addAll(response.body());

                    autorSeleccionado = autores.get(id);

                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction()
                            .setReorderingAllowed(true)
                            .addToBackStack(null)
                            .replace(R.id.frgConsultas, FragmentFrasesAutor.class, null)
                            .commit();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Autor>> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, "No se han podido obtener los autores", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public Autor getAutorSeleccionado() {
        return autorSeleccionado;
    }

    @Override
    public Categoria getCategoriaSeleccionada() {
        return categoriaSeleccionada;
    }

    @Override
    public void onCategoriaSeleccionada(int id) {
        apiService.getCategorias().enqueue(new Callback<List<Categoria>>() {
            @Override
            public void onResponse(@NonNull Call<List<Categoria>> call, @NonNull Response<List<Categoria>> response) {
                if(response.isSuccessful()) {
                    assert response.body() != null;
                    categorias.clear();
                    categorias.addAll(response.body());

                    categoriaSeleccionada = categorias.get(id);

                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction()
                            .setReorderingAllowed(true)
                            .addToBackStack(null)
                            .replace(R.id.frgConsultas, FragmentFrasesCategoria.class, null)
                            .commit();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Categoria>> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, "No se han podido obtener las categorias", Toast.LENGTH_LONG).show();
            }
        });
    }
}