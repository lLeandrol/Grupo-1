package com.example.prueba10;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.prueba10.database.UserDatabase;
import com.example.prueba10.database.model.User;
import com.example.prueba10.util.Constant;
import com.example.prueba10.util.Utilidades;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.ref.WeakReference;
import java.util.List;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_login;
    private Button btn_registro;
    private Activity miActividad;
    private FirebaseAuth mAuth;
    private EditText edt_usuario;
    private EditText edt_contrasena;
    private Button btn_olvidaste;
    private final int ACTIVITY_REGISTRO = 1;
    private final int ForgotPassword = 2;
    SharedPreferences mispreferencias;
    private FirebaseFirestore db;
    private UserDatabase database;

    private List<User> listaUsuarios;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        miActividad = this;


        db = FirebaseFirestore.getInstance();

        btn_login = findViewById(R.id.btn_login);
        btn_registro = findViewById(R.id.btn_registro);
        mAuth = FirebaseAuth.getInstance();
        edt_usuario = findViewById(R.id.edt_usuario);
        edt_contrasena = findViewById(R.id.edt_contrasena);
        btn_olvidaste = findViewById(R.id.btn_olvidaste);
        mispreferencias = getSharedPreferences(Constant.PREFERENCE, MODE_PRIVATE);

        String usuario = mispreferencias.getString("USUARIO", "");

        if (!usuario.equals("")) {
            toMenu(usuario);
        }


        btn_olvidaste.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        btn_registro.setOnClickListener(this);
        database = UserDatabase.getInstance(this);

        new GetUserTask(this).execute();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:

                String usuario = edt_usuario.getText().toString();
                String contrasena = edt_contrasena.getText().toString();

                Log.e("USUARIO", usuario);
                Log.e("CONTRASENA", contrasena);

                if (usuario.equals("admin") && Utilidades.md5(contrasena).equals("21232f297a57a5a743894a0e4a801fc3")) {
                    toMenu(usuario);
                } else {

                    //new GetUserLoginTask(this, usuario, Utilidades.md5(contrasena)).execute();
                    loginFirebase(usuario,Utilidades.md5(contrasena));
                    //Toast.makeText(miActividad, "Error iniciando sesión", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_registro:
                Intent intentRegistro = new Intent(this, RegistroActivity.class);
                //startActivity(intentRegistro);

                startActivityForResult(intentRegistro, ACTIVITY_REGISTRO);

                break;
            case R.id.btn_olvidaste:
                Intent intentOlvidaste = new Intent(this,ForgotPassword.class);
                startActivityForResult(intentOlvidaste, ForgotPassword);
                break;
        }
    }

    public void loginFirebase(String usuario, String contrasena) {
        DocumentReference docRef = db.collection("usuario").document(usuario);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String userContrasena = document.getData().get("contrasena").toString();
                        Log.e("TAG", "DocumentSnapshot data: " + document.getData().get("contrasena"));

                        if (contrasena.equals(userContrasena)) {
                            toMenu(usuario);
                        }

                    } else {
                        Toast.makeText(miActividad, "Usuario o contraseña incorrecta", Toast.LENGTH_SHORT).show();
                        Log.e("TAG", "No such document");
                    }
                } else {
                    Log.e("TAG", "get failed with ", task.getException());
                }
            }
        });
    }

    public void toMenu(String usuario){
        Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
        intent.putExtra("usuario", usuario);

        SharedPreferences.Editor editor = mispreferencias.edit();
        editor.putString("USUARIO", usuario);

        editor.commit();

        finish();
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == ACTIVITY_REGISTRO) {
            if (resultCode == Activity.RESULT_OK) {
                Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                String usuario = data.getStringExtra("USUARIO");
                toMenu(usuario);
            } else {
                Toast.makeText(miActividad, "No redirigir al login", Toast.LENGTH_SHORT).show();
            }
        }

    }


    private static class GetUserTask extends AsyncTask<Void, Void, List<User>> {

        private WeakReference<LoginActivity> loginActivityWeakReference;

        GetUserTask(LoginActivity context) {
            this.loginActivityWeakReference = new WeakReference<>(context);
        }

        @Override
        protected List<User> doInBackground(Void... voids) {
            if (loginActivityWeakReference.get() != null) {
                List<User> users = loginActivityWeakReference.get().database.getUserDao().getUser();
                return users;
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<User> users) {
            if (users != null && users.size() > 0) {
                loginActivityWeakReference.get().listaUsuarios = users;
            }
            super.onPostExecute(users);
        }
    }

    private static class GetUserLoginTask extends AsyncTask<Void, Void, User> {

        private String correo;
        private String contrasena;
        private WeakReference<LoginActivity> loginActivityWeakReference;

        GetUserLoginTask(LoginActivity context, String correo, String contrasena) {
            this.loginActivityWeakReference = new WeakReference<>(context);
            this.correo = correo;
            this.contrasena= contrasena;
        }

        @Override
        protected User doInBackground(Void... voids) {
            if (loginActivityWeakReference.get() != null) {
                User user = loginActivityWeakReference.get().database.getUserDao().getUserLogin(correo, contrasena);
                return user;
            }
            return null;
        }

        @Override
        protected void onPostExecute(User user) {
            if (user != null) {
                //loginActivityWeakReference.get().listaUsuarios = users;
                loginActivityWeakReference.get().toMenu(correo);
            }
            super.onPostExecute(user);
        }
    }
}