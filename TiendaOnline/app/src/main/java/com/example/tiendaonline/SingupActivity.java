package com.example.tiendaonline;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SingupActivity extends AppCompatActivity {

    private CheckBox chx_terms;
    private Activity activity;
    private Button btn_registro;
    private EditText edt_password;
    private String password;
    private EditText edt_user;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singup);

        activity = this;
        chx_terms = findViewById(R.id.chx_terms);
        btn_registro = findViewById(R.id.btn_registro);
        edt_password = findViewById(R.id.edt_password);
        edt_user = findViewById(R.id.edt_user);

        btn_registro.setEnabled(false);

        /*chx_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (chx_terms.isChecked()){
                    btn_registro.setEnabled(true);
                    Toast.makeText(activity, "Hability", Toast.LENGTH_SHORT).show();

            }else {
                    btn_registro.setEnabled(false);
                    Toast.makeText(activity, "Unhability", Toast.LENGTH_SHORT).show();
                }
            }
        });*/

        chx_terms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                btn_registro.setEnabled(isChecked);

            }
        });



        btn_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String password = edt_password.getText().toString();

                if(password.length()<=8){
                    Toast.makeText(activity, "la contraseña debe tener mas de 8 caracteres", Toast.LENGTH_SHORT).show();
                }

                else {
                    if (!isValidPassword(password)) {
                        Toast.makeText(activity, "Pasword invalid", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(activity, "Pasword acepted", Toast.LENGTH_SHORT).show();

                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("User",edt_user.getText().toString());
                        setResult(Activity.RESULT_OK, returnIntent);

                        finish();


                    }
                }
            }
        });

    }



    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    }