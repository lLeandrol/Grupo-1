package com.example.prueba2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_login;
    private Button btn_signup;
    private TextView tv_forgot;

    private EditText et_username;
    private EditText et_password;
    private Activity myActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_login = findViewById(R.id.btn_login);
        btn_signup = findViewById(R.id.btn_signup);
        tv_forgot = findViewById(R.id.tv_forgot);

        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);

        myActivity = this;


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = et_username.getText().toString();
                String password = et_password.getText().toString();

                Log.e("USUARIO: ",username);
                Log.e("CONTRASENA: ",password);

                if(username.equals("admin") && password.equals("admin")){
                    Intent intent = new Intent(LoginActivity.this,Menu_Activity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(myActivity, "Datos de ingreso incorrectos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        tv_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,TermsActivity.class);
                startActivity(intent);
            }
        });
    }


    public void onClick(View view) {
    }
}