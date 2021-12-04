package com.example.prueba10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;

import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;



public class ForgotPassword extends AppCompatActivity{
    MaterialButton btn_recupera;
    TextInputEditText edt_correorecuperar;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        btn_recupera= findViewById(R.id.btn_recupera);
        edt_correorecuperar = findViewById(R.id.edt_correorecuperar);
        btn_recupera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            validate();
            }
        });
    }
    public void validate (){
        String email =edt_correorecuperar.getText().toString().trim();
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edt_correorecuperar.setError("Correo valido");
            return;
        }
        sendEmail(email);
        }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(ForgotPassword.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }


    public void sendEmail(String email){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddres = email;
        auth.sendPasswordResetEmail(emailAddres)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(ForgotPassword.this, "Correo enviado exitosamente ", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ForgotPassword.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Toast.makeText(ForgotPassword.this, "Correo Inválido", Toast.LENGTH_SHORT).show();
                    }
                    }


                });
    }
    }

