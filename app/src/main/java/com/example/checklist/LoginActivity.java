package com.example.checklist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Calendar;
import java.util.Hashtable;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    String URL_SERVIDOR = "http://192.168.1.137/server/login.php";

    private EditText rp, ECO;
    private Button btnInit, btnReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        rp = findViewById(R.id.edituser);
        ECO = findViewById(R.id.editpwd);
        btnInit = findViewById(R.id.btnInit);
        btnReg = findViewById(R.id.btnReg);

        btnInit.setOnClickListener(v -> login());
        btnReg.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), RegisterActivity.class)));
    }

    public void login() {
        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.POST, URL_SERVIDOR,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("ERROR 1")) {
                            Toast.makeText(getApplicationContext(), "Deben llenarse todos los campos", Toast.LENGTH_SHORT).show();
                        } else if (response.equals("ERROR 2")) {
                            Toast.makeText(getApplicationContext(), "No existe el registro", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Inicio de sesión exitoso", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "ERROR AL INICIAR SESIÓN", Toast.LENGTH_LONG).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> parametros = new Hashtable<String, String>();
                parametros.put("RP",rp.getText().toString().trim());
                parametros.put("NO. ECO", ECO.getText().toString().trim());

                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        requestQueue.add(stringRequest);
    }
}