package com.daniminguet.trabajofrasescelebres;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.daniminguet.trabajofrasescelebres.interfaces.IAPIService;
import com.daniminguet.trabajofrasescelebres.models.Usuario;
import com.daniminguet.trabajofrasescelebres.rest.RestClient;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private IAPIService apiService;
    private EditText etUsername, etPassword;

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
                loginUser();
            }
        });

        findViewById(R.id.tvRegistrar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private void loginUser() {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        if (username.isEmpty()) {
            etUsername.setError("Se requiere un nombre de usuario");
            etUsername.requestFocus();
            return;
        } else if (password.isEmpty()) {
            etPassword.setError("Se requiere una contraseña");
            etPassword.requestFocus();
            return;
        }

        Call<Boolean> booleanCall = apiService.logUsuario(new Usuario(username, password));

        booleanCall.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                System.out.println(response.body());
                if (response.body()) {
                    Toast.makeText(LoginActivity.this, "Has iniciado sesión!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class).putExtra("username", username));
                } else {
                    Toast.makeText(LoginActivity.this, "Usuario no válido", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
