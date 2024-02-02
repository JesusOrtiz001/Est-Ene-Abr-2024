package com.example.checklist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    String URL_SERVIDOR = "http://192.168.1.137/server/registro.php";

    EditText etUsuario, etContrasena;
    Button btnRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsuario = findViewById(R.id.etUsuario);
        etContrasena = findViewById(R.id.etContrasena);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrar();
                etUsuario.setText("");
                etContrasena.setText("");
            }
        });
    }

    public void registrar() {
        final String RPE = etUsuario.getText().toString().trim();
        final String noECO = etContrasena.getText().toString().trim();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando...");

        if (RPE.isEmpty()) {
            etUsuario.setError("Ingrese los datos");
            return;
        } else {
            progressDialog.show();
            StringRequest request = new StringRequest(Request.Method.POST, "http://192.168.2.93/server/registro.php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equalsIgnoreCase("Datos insertados")) {
                        Toast.makeText(RegisterActivity.this, "No se puede insertar", Toast.LENGTH_SHORT).show();

                        progressDialog.dismiss();

                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        //Toast.makeText(RegisterActivity.this, "No se puede insertar", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(RegisterActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("usuario", RPE);
                    params.put("contrasena", noECO);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);
            requestQueue.add(request);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void login(View v) {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }
}