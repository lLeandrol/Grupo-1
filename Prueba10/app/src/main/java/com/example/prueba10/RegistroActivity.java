package com.example.prueba10;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.prueba10.database.UserDatabase;
import com.example.prueba10.database.model.User;
import com.example.prueba10.util.Utilidades;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistroActivity extends AppCompatActivity {

    private CheckBox chx_terminos;
    private Button btn_registro;
    private EditText edt_nombre;
    private EditText edt_apellido;
    private EditText edt_contrasena;
    private EditText edt_email;

    private UserDatabase database;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        chx_terminos = findViewById(R.id.chx_terminos);
        btn_registro = findViewById(R.id.btn_registro);
        edt_nombre = findViewById(R.id.edt_nombre);
        edt_apellido = findViewById(R.id.edt_apellido);
        edt_contrasena = findViewById(R.id.edt_contrasena);
        edt_email = findViewById(R.id.edt_email);

        btn_registro.setEnabled(false);

        Log.e("MD5", Utilidades.md5("sergio"));

        database = UserDatabase.getInstance(this);



        /*chx_terminos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (chx_terminos.isChecked()) {
                    btn_registro.setEnabled(true);
                } else {
                    btn_registro.setEnabled(false);
                }

            }
        });*/

        chx_terminos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                btn_registro.setEnabled(isChecked);
            }
        });

        btn_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nombre = edt_nombre.getText().toString();
                String apellido = edt_apellido.getText().toString();
                String correo = edt_email.getText().toString();
                String contrasena = edt_contrasena.getText().toString();


                if (contrasena.length() <= 8) {
                    Toast.makeText(RegistroActivity.this, "LA CONTRASEÑA DEBE TENER MAS DE 8 CARACTERES", Toast.LENGTH_SHORT).show();
                } else {
                    if(!isValidPassword(contrasena)){
                        Toast.makeText(RegistroActivity.this, "CONTRASEÑA INVALIDA", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(RegistroActivity.this, "CONTRASEÑA VALIDA", Toast.LENGTH_SHORT).show();
                        Log.e("MD5_CONTRASENA", Utilidades.md5(contrasena));

                        User user = new User(nombre, apellido, Utilidades.md5(contrasena), correo);

                        long response = database.getUserDao().insertUser(user);




                        //Intent returnIntent = new Intent();
                        //returnIntent.putExtra("USUARIO", edt_email.getText().toString());
                        //setResult(Activity.RESULT_OK, returnIntent);
                        finish();

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


}