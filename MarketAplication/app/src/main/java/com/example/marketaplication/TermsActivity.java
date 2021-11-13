package com.example.marketaplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;


public class TermsActivity extends AppCompatActivity {

    private Button btn_acept_terms;
    private Button btn_cancel_terms;
    private CheckBox chx_terms;
    private Activity miActividad;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);


        btn_acept_terms = findViewById(R.id.btn_acept_terms);
        btn_cancel_terms = findViewById(R.id.btn_cancel_terms);
        chx_terms = findViewById(R.id.chx_terms);
        miActividad = TermsActivity.this;

        btn_acept_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(miActividad, RegistroActivity.class);
                startActivity(intent);
            }
        });

        btn_cancel_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(miActividad, Login1Activity.class);
                startActivity(intent);
            }
        });
    }
}