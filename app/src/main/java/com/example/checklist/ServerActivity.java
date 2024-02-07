package com.example.checklist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;

public class ServerActivity extends AppCompatActivity {

    TextView userRPE, userNom;
    EditText date;
    private int nYearIni, nMonthIni, nDayIni, sYearIni, sMonthIni, sDayIni;
    static final int DATE_ID = 0;
    Calendar calendar = Calendar.getInstance();
    Button hola, hola2, hola3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        Intent intent = getIntent();
        if (intent.hasExtra("usuario")) {
            String usuario = intent.getStringExtra("usuario");
            userRPE = findViewById(R.id.rpe);
            userRPE.setText("RPE: " + usuario);
        }

        /*hola = findViewById(R.id.hola);
        hola.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeColor(hola);
                insertData("Bueno");
            }
        });
        hola2 = findViewById(R.id.hola2);
        hola2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeColor(hola2);
                insertData("Regular");
            }
        });
        hola3 = findViewById(R.id.hola3);
        hola3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeColor(hola3);
                insertData("Malo");
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

    /*private void changeColor(Button selectedButton) {
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

    /*private void insertData(String botonSeleccionado) {
        try {
            Connection connection = DatabaseConnection.connection();
            if (connection != null) {
                String insertQuery = "INSERT INTO checklist (estado_llanta_1) VALUES (?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                    preparedStatement.setString(1, botonSeleccionado);
                    preparedStatement.executeUpdate();
                    Toast.makeText(this, "DATO INSERTADO", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }*/

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