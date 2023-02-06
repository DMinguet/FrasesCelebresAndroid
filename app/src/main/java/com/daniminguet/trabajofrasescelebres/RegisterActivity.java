package com.daniminguet.trabajofrasescelebres;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.daniminguet.trabajofrasescelebres.interfaces.IAPIService;
import com.daniminguet.trabajofrasescelebres.models.Usuario;
import com.daniminguet.trabajofrasescelebres.rest.RestClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private IAPIService apiService;
    private EditText etUsername, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        apiService = RestClient.getInstance();

        etUsername = findViewById(R.id.etUsernameRegister);
        etPassword = findViewById(R.id.etContrasenyaRegister);

        findViewById(R.id.btnRegistrarse).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        findViewById(R.id.btnVolverLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }

    private void registerUser() {
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

        Call<Boolean> booleanCall = apiService.addUsuario(new Usuario(username, password));

        booleanCall.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.body()) {
                    Toast.makeText(RegisterActivity.this, "Te has registrado correctamente. Por favor, haz el login", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                } else {
                    Toast.makeText(RegisterActivity.this, "El usuario ya existe", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
