package com.example.checklist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GeneralActivity extends AppCompatActivity {

    public void B (View v){
        //Bueno

    };

    public void R (View v){
        //Regular

    };

    public void M (View v){
        //Malo

    };

    Button siguiente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general);
        siguiente=(Button) findViewById(R.id.siguiente1);


            siguiente.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent packageContext;
                    Intent i = new Intent( "packageContext; GeneralActivity.this, electrico.class");
                    startActivity(i);
    }

                };
}