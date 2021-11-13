package com.example.marketaplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistroActivity extends AppCompatActivity {

    private Button btn_cancel_register;
    private CheckBox chx_terms;
    private Button btn_register_user;
    private Button btn_terms;
    private Activity miActividad;
    private EditText edt_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        btn_cancel_register = findViewById(R.id.btn_cancel_register);
        chx_terms = findViewById(R.id.chx_terms);
        btn_register_user = findViewById(R.id.btn_register_user);
        btn_terms = findViewById(R.id.btn_terms);
        edt_password = findViewById(R.id.edt_password);
        miActividad = RegistroActivity.this;

        btn_register_user.setEnabled(false);

        btn_cancel_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(miActividad, Login1Activity.class);
                startActivity(intent);
            }
        });

        btn_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(miActividad, TermsActivity.class);
                startActivity(intent);
            }
        });

        /* chx_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chx_terms.isChecked()){
                    btn_register_user.setEnabled(true);
                } else {
                    btn_register_user.setEnabled(false);
                }
            }
        }); */

        chx_terms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                btn_register_user.setEnabled(isChecked);
            }
        });

        btn_register_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = edt_password.getText().toString();
                if (password.length() <= 8) {
                    Toast.makeText(RegistroActivity.this, "LA CONTRASEÑA DEBE TENER MAS DE 8 CARACTERES", Toast.LENGTH_SHORT).show();
                } else {
                    if (!isValidPassword(password)) {
                        Toast.makeText(RegistroActivity.this, "CONTRASEÑA INVALIDA", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegistroActivity.this, "CONTRASEÑA VALIDA", Toast.LENGTH_SHORT).show();
                        Log.e("MD5_CONTRASENA", md5(password));
                    }
                }
            }
        });
    }
        public static boolean isValidPassword(String password) {
            Pattern pattern;
            Matcher matcher;
            final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
            pattern = Pattern.compile(PASSWORD_PATTERN);
            matcher = pattern.matcher(password);

            return matcher.matches();

        }

        public static String md5(final String s) {
            final String MD5 = "MD5";
            try {
                // Create MD5 Hash
                MessageDigest digest = java.security.MessageDigest
                        .getInstance(MD5);
                digest.update(s.getBytes());
                byte messageDigest[] = digest.digest();

                // Create Hex String
                StringBuilder hexString = new StringBuilder();
                for (byte aMessageDigest : messageDigest) {
                    String h = Integer.toHexString(0xFF & aMessageDigest);
                    while (h.length() < 2)
                        h = "0" + h;
                    hexString.append(h);
                }
                return hexString.toString();

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return "";
        }

    }