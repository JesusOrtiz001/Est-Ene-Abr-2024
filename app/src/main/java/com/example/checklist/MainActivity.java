package com.example.checklist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private EditText ipEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ipEdit = findViewById(R.id.ipEdit);
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (end > start) {
                    String destText = dest.toString();
                    String resultText = destText.substring(0, dstart) + source.subSequence(start, end) + destText.substring(dend);
                    if (!resultText.matches("^\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3})?)?)?)?)?)?")) {
                        return "";
                    } else {
                        String[] splits = resultText.split("\\.");
                        for (String split : splits) {
                            if (Integer.parseInt(split) > 255) {
                                return "";
                            }
                        }
                    }
                }
                return null;
            }
        };

        ipEdit.setFilters(filters);

        Button connBtn = findViewById(R.id.connBtn);

        connBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ipAddress = ipEdit.getText().toString();
                new ConnectTask().execute(ipAddress);
            }
        });
    }

    private class ConnectTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            String ipAddress = params[0];
            int port = 80;

            try {
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress(ipAddress, port), 5000);
                socket.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean isConnected) {
            if (isConnected) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                Toast.makeText(MainActivity.this, "Conexión establecida correctamente", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MainActivity.this, "No es posible conectarse al servidor", Toast.LENGTH_LONG).show();
            }
        }
    }
}