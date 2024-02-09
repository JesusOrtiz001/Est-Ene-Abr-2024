package com.example.checklist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ServerActivity extends AppCompatActivity {

    String URL = "http://192.168.1.137/server/logout.php";
    TextView userRPE, userNom;
    EditText date;
    private int nYearIni, nMonthIni, nDayIni, sYearIni, sMonthIni, sDayIni;
    static final int DATE_ID = 0;
    Calendar calendar = Calendar.getInstance();
    Button hola, hola2, hola3;
    ImageButton settings;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        Intent intent = getIntent();
        if (intent != null) {
            String usuario = intent.getStringExtra("usuario");
            String nombreUsuario = intent.getStringExtra("nombreUsuario");
            userRPE = findViewById(R.id.rpe);
            userRPE.setText("RPE: " + usuario);
            userNom = findViewById(R.id.user);
            userNom.setText("Nombre: " + nombreUsuario);
        }

        hola = findViewById(R.id.hola);
        hola.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeColor(hola);
                fetchData("B");
            }
        });
        hola2 = findViewById(R.id.hola2);
        hola2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeColor(hola2);
                fetchData("Regular");
            }
        });
        hola3 = findViewById(R.id.hola3);
        hola3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeColor(hola3);
                fetchData("Malo");
            }
        });

        /*settings = findViewById(R.id.settings);
        spinner = findViewById(R.id.spinner);

        List<String> opciones = new ArrayList<>();
        opciones.add("Cambiar IP");
        opciones.add("Cerrar sesión");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opciones);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String opcionSeleccionada = opciones.get(position);
                if (opcionSeleccionada.equals("Cambiar IP")) {

                } else if (opcionSeleccionada.equals("Cerrar sesión")) {
                    Toast.makeText(ServerActivity.this, "Sesión finalizada", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.performClick();
            }
        });*/

        sYearIni = calendar.get(Calendar.YEAR);
        sMonthIni = calendar.get(Calendar.MONTH);
        sDayIni = calendar.get(Calendar.DAY_OF_MONTH);
        date = findViewById(R.id.fecha);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DATE_ID);
            }
        });
    }

    private void changeColor(Button selectedButton) {
        hola.setBackgroundColor(getResources().getColor(android.R.color.background_light));
        hola2.setBackgroundColor(getResources().getColor(android.R.color.background_light));
        hola3.setBackgroundColor(getResources().getColor(android.R.color.background_light));

        if (selectedButton == hola) {
            hola.setBackgroundColor(Color.GREEN);
        } else if (selectedButton == hola2) {
            hola2.setBackgroundColor(Color.YELLOW);
        } else if (selectedButton == hola3) {
            hola3.setBackgroundColor(Color.RED);
        }
        //selectedButton.setBackgroundColor(Color.GREEN);
    }

    private void fetchData(String botonSeleccionado) {
        try {
            Connection connection = DatabaseConnection.connection();
            if (connection != null) {
                String insertQuery = "SELECT checklist.estado_llanta_1, empleados.rpe_empleado " + "FROM estado_llanta_1 " +
                        "JOIN empleados ON checklist.estado_llanta_1 = empleados.rpe_empleado " +
                        "WHERE checklist.estado_llanta_1 = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        if (resultSet.next()) {
                            String resultado = "Valor: " + resultSet.getString("estado_llanta_1") +
                                    "\nNombre: " + resultSet.getString("rpe_empleado");
                            //userNom.setText(resultado);
                        }
                    }
                    preparedStatement.setString(1, botonSeleccionado);
                    preparedStatement.executeUpdate();
                    Toast.makeText(this, "DATO INSERTADO", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }

    private void colocarFecha() {
        date.setText(nDayIni + "-" + (nMonthIni + 1) + "-" +  nYearIni + "");
    }

    private DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            nYearIni = year;
            nMonthIni = monthOfYear;
            nDayIni = dayOfMonth;
            colocarFecha();
        }
    };

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_ID:
                return new DatePickerDialog(this, onDateSetListener, sYearIni, sMonthIni, sDayIni);
        }
        return null;
    }
}