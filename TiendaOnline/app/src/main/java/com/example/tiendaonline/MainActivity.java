package com.example.tiendaonline;


import android.app.Activity;
import android.app.backup.SharedPreferencesBackupHelper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_login;
    private Button btn_singup;
    private Activity myactivity;
    private EditText edt_user;
    private EditText edt_password;

    SharedPreferences mispreferencias;

    String user = edt_user.getText().toString();
    String password = edt_password.getText().toString();


    private  final int ACTIVITY_REGISTRO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myactivity = this;
        btn_login = findViewById(R.id.btn_login);
        btn_singup = findViewById(R.id.btn_singup);
        edt_user = findViewById(R.id.edt_user);
        edt_password = findViewById(R.id.edt_password);
        mispreferencias = getSharedPreferences(Constant, MODE_PRIVATE);


        btn_singup.setOnClickListener(this);
        btn_login.setOnClickListener(this);


}

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_login:

                Log.e("USER", user);
                Log.e("PASSWORD", password);

                if (user.equals("Admin") && password.equals("12345")){
                    toMenu(user);


                } else {
                    Toast.makeText(myactivity, "Error start session", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.btn_singup:
                Intent intentRegistro = new Intent(myactivity, SingupActivity.class);
                //iniciar actividad
                //startActivity(intentsing);

                //iniciar actividad con resultado
                startActivityForResult(intentRegistro, ACTIVITY_REGISTRO);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ACTIVITY_REGISTRO){
            if (resultCode == Activity.RESULT_OK){

                Intent intent = new Intent(myactivity, MenuActivity.class);
                String usuario = data.getStringExtra("User");
                intent.putExtra("User", "usuario");
                startActivity(intent);

                toMenu(user);
            }
            else {
                Toast.makeText(myactivity, "no redirigir el login", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public  void toMenu(String User){
        Intent intent = new Intent(myactivity, MenuActivity.class);
        intent.putExtra("User", user);

        SharedPreferences.Editor editor= mispreferencias.edit();
        editor.putString("User", user);

        editor.commit();

        startActivity(intent);
    }
}