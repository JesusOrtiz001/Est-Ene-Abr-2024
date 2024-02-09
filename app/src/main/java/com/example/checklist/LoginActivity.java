package com.example.checklist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    //9A8JW 8000056527 nAtnnie117@*

    //String URL_SERVIDOR = "http://192.168.115.4/server/servidor/login.php";
    String URL_SERVIDOR = "http://192.168.2.93/server/inicio.php";
    EditText etUsuario, etContrasena;
    Button btnLogin, btnRegistrar;

    private static final String PREFS_NAME = "LoginPrefs";
    private static final String PREF_USERNAME = "usuario";
    private static final String PREF_PASSWORD= "contrasena";
    String usuario, contrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsuario = findViewById(R.id.edituser);
        etUsuario.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        etContrasena = findViewById(R.id.editpwd);
        btnRegistrar = findViewById(R.id.btnReg);

        etContrasena.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        etContrasena.setTransformationMethod(PasswordTransformationMethod.getInstance());

        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        etUsuario.setText(preferences.getString(PREF_USERNAME, ""));
        etContrasena.setText(preferences.getString(PREF_PASSWORD, ""));
    }

    public void login(View view) {
        if (etUsuario.getText().toString().equals("")){
            Toast.makeText(this, "Ingrese RPE", Toast.LENGTH_SHORT).show();
        } else if (etContrasena.getText().toString().equals("")) {
            Toast.makeText(this, "Ingrese No. ECO", Toast.LENGTH_SHORT).show();
        } else {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Verificando... ");
            progressDialog.show();

            usuario = etUsuario.getText().toString().trim();
            contrasena = etContrasena.getText().toString().trim();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SERVIDOR, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();

                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                if (jsonResponse.getString("status").equals("Correcto")) {
                                    String nombreUsuario = jsonResponse.getString("nombreUsuario");

                                    saveCredentials();
                                    etUsuario.setText("");
                                    etContrasena.setText("");
                                    Intent intent = new Intent(getApplicationContext(), ServerActivity.class);
                                    intent.putExtra("usuario", usuario);
                                    intent.putExtra("nombreUsuario", nombreUsuario);
                                    startActivity(intent);
                                    Toast.makeText(LoginActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                                } else if (jsonResponse.getString("status").equals("Incorrecto")) {
                                    Toast.makeText(LoginActivity.this, "RPE o No. ECO incorrectos", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                                }
                            } catch(JSONException e){
                                e.printStackTrace();
                                Toast.makeText(LoginActivity.this, "Error al procesar la respuesta del servidor", Toast.LENGTH_SHORT).show();
                                }
                            }
                    }, 1500);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("usuario", usuario);
                    params.put("contrasena", contrasena);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
            requestQueue.add(stringRequest);
        }
    }

    public void registro(View view) {
        startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
        finish();
    }

    private void saveCredentials() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREF_USERNAME, etUsuario.getText().toString().trim());
        editor.putString(PREF_PASSWORD, etContrasena.getText().toString().trim());
        editor.apply();
    }
}