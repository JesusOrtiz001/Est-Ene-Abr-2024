package com.example.checklist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MainActivity extends AppCompatActivity {

    private EditText ip1, ip2, ip3, ip4;
    private Button hola, hola2, hola3;

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
        /**/

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

    /**/

    private class ConnectTask extends AsyncTask<String, Void, Boolean> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Verificando conexión... ");
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String ipAddress = params[0];
            String result = "";
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

            /*try {
                URL url = new URL("http://" + ipAddress + "server/conexion.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                InputStream in = httpURLConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
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
        }*/

        @Override
        protected void onPostExecute(Boolean result) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                    if (result) {
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        Toast.makeText(MainActivity.this, "Conexión establecida correctamente", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MainActivity.this, "No es posible conectarse al servidor", Toast.LENGTH_LONG).show();
                    }
                }
            }, 1500);
        }
    }
}