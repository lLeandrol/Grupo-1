package com.example.prueba10;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_jugar, btn_perfil, btn_ajustes, btn_acerca_de;

    private Activity miActividad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        miActividad = this;

        btn_jugar = findViewById(R.id.btn_jugar);
        btn_perfil = findViewById(R.id.btn_perfil);
        btn_ajustes = findViewById(R.id.btn_ajustes);
        btn_acerca_de = findViewById(R.id.btn_acerca_de);



        btn_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("CLICK2", "PERFIL");
                //Toast.makeText(MainActivity.this, getString(R.string.txt_click_jugar), Toast.LENGTH_SHORT).show();
                Toast.makeText(miActividad, getString(R.string.txt_click_perfil), Toast.LENGTH_SHORT).show();
            }
        });

        //btn_jugar.setOnClickListener(this);
        btn_perfil.setOnClickListener(this);
        btn_ajustes.setOnClickListener(this);
        btn_acerca_de.setOnClickListener(this);
    }

    public void clickJugar(View v) {
        Log.e("CLICK1", "JUGAR");
        Toast.makeText(this, getString(R.string.txt_click_jugar), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_ajustes:
                Log.e("CLICK", "AJUSTES");
                Toast.makeText(this, getString(R.string.txt_click_ajustes), Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_acerca_de:
                Log.e("CLICK", "ACERCA_DE");
                Toast.makeText(this, getString(R.string.txt_click_acerca_de), Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_jugar:
                Log.e("CLICK", "JUGAR");
                Toast.makeText(this, getString(R.string.txt_click_jugar), Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_perfil:
                Log.e("CLICK", "PERFIL");
                Toast.makeText(this, getString(R.string.txt_click_perfil), Toast.LENGTH_SHORT).show();
                break;
        }


    }
}