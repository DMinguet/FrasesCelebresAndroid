package com.daniminguet.trabajofrasescelebres;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.preference.PreferenceManager;

import com.daniminguet.trabajofrasescelebres.interfaces.IAPIService;
import com.daniminguet.trabajofrasescelebres.models.Autor;
import com.daniminguet.trabajofrasescelebres.models.Categoria;
import com.daniminguet.trabajofrasescelebres.models.Frase;
import com.daniminguet.trabajofrasescelebres.models.Usuario;
import com.daniminguet.trabajofrasescelebres.rest.RestClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private boolean tabletLayout;
    private IAPIService apiService;
    private Usuario activeUser;
    private SharedPreferences prefs;
    private RestClient restClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        restClient = new RestClient();
        apiService = RestClient.getInstance();
        getAutores();
        getCategorias();
        getFrases();
        getUsuarios();
        loadActiveUser();

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("usernamePref", activeUser.getNombre());
        editor.putString("passwordPref", activeUser.getContrasenya());
        editor.putString("ip", restClient.obtenerIp());
        editor.putString("port", String.valueOf(restClient.obtenerPuerto()));
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
                    for(Autor autor: response.body()) {
                        Log.i(MainActivity.class.getSimpleName(), autor.toString());
                    }
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
                    for(Categoria categoria: response.body()) {
                        Log.i(MainActivity.class.getSimpleName(), categoria.toString());
                    }
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
                    for(Frase frase: response.body()) {
                        Log.i(MainActivity.class.getSimpleName(), frase.toString());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Frase>> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, "No se han podido obtener las frases", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getUsuarios() {
        apiService.getUsers().enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(@NonNull Call<List<Usuario>> call, @NonNull Response<List<Usuario>> response) {
                if(response.isSuccessful()) {
                    assert response.body() != null;
                    for(Usuario usuario: response.body()) {
                        Log.i(MainActivity.class.getSimpleName(), usuario.toString());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Usuario>> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, "No se han podido obtener los usuarios", Toast.LENGTH_LONG).show();
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
}