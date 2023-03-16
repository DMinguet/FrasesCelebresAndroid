package com.daniminguet.trabajofrasescelebres;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.preference.PreferenceManager;

import com.daniminguet.trabajofrasescelebres.fragments.FragmentFrasesAutor;
import com.daniminguet.trabajofrasescelebres.fragments.FragmentFrasesCategoria;
import com.daniminguet.trabajofrasescelebres.fragments.FragmentPrincipal;
import com.daniminguet.trabajofrasescelebres.interfaces.IAPIService;
import com.daniminguet.trabajofrasescelebres.interfaces.IAutorListener;
import com.daniminguet.trabajofrasescelebres.interfaces.ICategoriaListener;
import com.daniminguet.trabajofrasescelebres.models.Autor;
import com.daniminguet.trabajofrasescelebres.models.Categoria;
import com.daniminguet.trabajofrasescelebres.models.Frase;
import com.daniminguet.trabajofrasescelebres.models.Usuario;
import com.daniminguet.trabajofrasescelebres.rest.RestClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    private Frase fraseDelDia;
    private List<Autor> autores;
    private Autor autorSeleccionado;
    private List<Categoria> categorias;
    private Categoria categoriaSeleccionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        autores = new ArrayList<>();
        categorias = new ArrayList<>();

        cargarDatos();
    }

    private void cargarDatos() {
        getAutores();
        getCategorias();
        loadFraseDia();
        loadActiveUser();

        Intent notifyIntent = new Intent(this, CrearNotificacion.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast
                (this, 1, notifyIntent, PendingIntent.FLAG_IMMUTABLE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 16);
        calendar.set(Calendar.MINUTE, 40);
        calendar.set(Calendar.SECOND, 59);

        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);

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



    private void loadFraseDia() {
        if (fraseDelDia == null) {
            fraseDelDia = (Frase) getIntent().getSerializableExtra("frase");
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

    @Override
    public Usuario getUser() {
        if (activeUser == null) {
            loadActiveUser();
            loadFraseDia();
        }
        return activeUser;
    }

    @Override
    public Frase getFraseDia() {
        if (fraseDelDia == null) {
            loadFraseDia();
            loadActiveUser();
        }
        return fraseDelDia;
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