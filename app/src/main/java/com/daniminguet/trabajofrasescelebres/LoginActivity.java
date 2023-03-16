package com.daniminguet.trabajofrasescelebres;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.daniminguet.trabajofrasescelebres.interfaces.IAPIService;
import com.daniminguet.trabajofrasescelebres.models.Frase;
import com.daniminguet.trabajofrasescelebres.models.Usuario;
import com.daniminguet.trabajofrasescelebres.rest.RestClient;

import java.io.IOException;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private IAPIService apiService;
    private EditText etUsername, etPassword;
    private Usuario usuarioActivo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        apiService = RestClient.getInstance();

        etUsername = findViewById(R.id.etUsernameLogin);
        etPassword = findViewById(R.id.etContrasenyaLogin);

        findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    loginUser();
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        findViewById(R.id.tvRegistrar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private void loginUser() throws NoSuchAlgorithmException {
        String username = etUsername.getText().toString();
        String password = HashGenerator.getSHAString(etPassword.getText().toString());

        if (username.isEmpty()) {
            etUsername.setError("Se requiere un nombre de usuario");
            etUsername.requestFocus();
            return;
        } else if (password.isEmpty()) {
            etPassword.setError("Se requiere una contraseña");
            etPassword.requestFocus();
            return;
        }

        usuarioActivo = new Usuario(username, password);

        Call<Usuario> usuarioCall = apiService.logUsuario(usuarioActivo);

        usuarioCall.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.body() != null) {
                    Toast.makeText(LoginActivity.this, "Has iniciado sesión!", Toast.LENGTH_LONG).show();
                    usuarioActivo = response.body();
                    apiService.getFrases().enqueue(new Callback<List<Frase>>() {
                        @Override
                        public void onResponse(@NonNull Call<List<Frase>> call, @NonNull Response<List<Frase>> response) {
                            if(response.isSuccessful()) {
                                assert response.body() != null;

                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                                Date fechaHoy = new Date();

                                for (Frase fraseFecha : response.body()) {
                                    if (fraseFecha.getFechaprogramada().equalsIgnoreCase(sdf.format(fechaHoy))) {
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class).putExtra("user", usuarioActivo).putExtra("frase", fraseFecha));
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<List<Frase>> call, @NonNull Throwable t) {
                            Toast.makeText(LoginActivity.this, "No se han podido obtener las frases", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Toast.makeText(LoginActivity.this, "Usuario no válido", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Error al iniciar sesión", Toast.LENGTH_LONG).show();
            }
        });
    }
}
