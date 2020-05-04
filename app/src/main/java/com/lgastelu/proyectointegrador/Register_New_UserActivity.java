package com.lgastelu.proyectointegrador;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.lgastelu.proyectointegrador.models.Usuarios;
import com.lgastelu.proyectointegrador.service.ApiService;
import com.lgastelu.proyectointegrador.service.ApiServiceGenerator;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register_New_UserActivity extends AppCompatActivity {

    private EditText txt_nombre,txt_dni,txt_correo,txt_password,txt_co_password;
    private Button btn_register;

    private static final String TAG = Register_New_UserActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register__new__user);

        txt_nombre = findViewById(R.id.txt_name);
        txt_dni = findViewById(R.id.txt_dni) ;
        txt_correo = findViewById(R.id.txt_email);
        txt_password = findViewById(R.id.txt_password);
        txt_co_password = findViewById(R.id.txt_re_password);

    }

    public void callRegister(View view){

        String nombre = txt_nombre.getText().toString();
        String dni = txt_dni.getText().toString();
        String correo = txt_correo.getText().toString();
        String contraseña = txt_password.getText().toString();
        String re_contraseña = txt_co_password.getText().toString();

        if (correo.isEmpty() || contraseña.isEmpty() || re_contraseña.isEmpty()) {
            Toasty.warning(this, "Los campos correo y contraseña no pueden estar vacio", Toast.LENGTH_SHORT).show();
            return;
        }

        if (contraseña.length()<=5){
            Toasty.warning(this, "Se necesita un minimo de 6 caracteres para la contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        if (dni.length()<=7 && dni.length()>=9){
            Toasty.warning(this, "Ingrese correactamente su DNI", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!contraseña.equals(re_contraseña)) {
            Toasty.warning(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        final String compruebaemail = txt_correo.getEditableText().toString().trim();

        if (!compruebaemail.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+"))
        {
            Toasty.error(Register_New_UserActivity.this, "Por favor introduzca su correo correctamente", Toast.LENGTH_LONG).show();
            return;
        }

        Usuarios usuarios = new Usuarios(nombre, dni, correo, contraseña);
        ApiService service = ApiServiceGenerator.createService(ApiService.class);

        Call<Usuarios> call;

        call=service.createUsuario(usuarios);

        call.enqueue(new Callback<Usuarios>() {
            @Override
            public void onResponse(@NonNull Call<Usuarios> call, @NonNull Response<Usuarios> response) {
                try {
                    if(response.isSuccessful()) {

                        Usuarios usuarios = response.body();
                        Log.d(TAG, "usuario: " + usuarios);

                        Toasty.success(Register_New_UserActivity.this, "Usuario creado correctamente", Toast.LENGTH_SHORT).show();

                        setResult(RESULT_OK);

                        startActivity(new Intent(Register_New_UserActivity.this, LoginActivity.class));
                        finish();

                    }else{
                        throw new Exception(ApiServiceGenerator.parseError(response).getMessage());
                    }
                } catch (Throwable t) {
                    Log.e(TAG, "onThrowable: " + t.getMessage(), t);
                    Toasty.error(Register_New_UserActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Usuarios> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage(), t);
                Toasty.error(Register_New_UserActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
}
