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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText ip1, ip2, ip3, ip4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ip1 = findViewById(R.id.ip1);
        ip2 = findViewById(R.id.ip2);
        ip3 = findViewById(R.id.ip3);
        ip4 = findViewById(R.id.ip4);
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

        ip1.setFilters(filters);
        ip2.setFilters(filters);
        ip3.setFilters(filters);
        ip4.setFilters(filters);

        Button connBtn = findViewById(R.id.connBtn);

        connBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ipAddress = ip1.getText().toString() + "." +
                        ip2.getText().toString() + "." +
                        ip3.getText().toString() + "." +
                        ip4.getText().toString();
                new ConnectTask().execute(ipAddress);
            }
        });
    }

    private class ConnectTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String ipAddress = params[0];
            String result = "";
            int port = 80;

            try {
                URL url =  new URL("http://" + ipAddress + "server/conexion.php");
                HttpURLConnection httpURLConnection =  (HttpURLConnection) url.openConnection();

                InputStream in = httpURLConnection.getInputStream();
                BufferedReader reader= new BufferedReader(new InputStreamReader(in, "UTF-8"));
                String line;

                while ((line = reader.readLine()) != null) {
                    result += line;
                }

                reader.close();
                in.close();
                httpURLConnection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return result;
            }

        @Override
        protected void onPostExecute(String result) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                Toast.makeText(MainActivity.this, "Conexión establecida correctamente", Toast.LENGTH_LONG).show();
            }
        }
    }