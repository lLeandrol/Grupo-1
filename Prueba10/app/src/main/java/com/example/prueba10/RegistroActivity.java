package com.example.prueba10;

import androidx.annotation.NonNull;
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

import com.example.prueba10.database.UserDatabase;
import com.example.prueba10.database.model.User;
import com.example.prueba10.util.Utilidades;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistroActivity extends AppCompatActivity implements View.OnClickListener{

    private CheckBox chx_terminos;
    private Button btn_registro;
    private EditText edt_nombre;
    private EditText edt_apellido;
    private EditText edt_contrasena;
    private EditText edt_email;
    private Activity miActividad;
    private Button btn_terminos;
    private FirebaseAuth mAuth;
    private UserDatabase database;
    FirebaseFirestore db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        mAuth = FirebaseAuth.getInstance();
        chx_terminos = findViewById(R.id.chx_terminos);
        btn_registro = findViewById(R.id.btn_registro);
        edt_nombre = findViewById(R.id.edt_nombre);
        edt_apellido = findViewById(R.id.edt_apellido);
        edt_contrasena = findViewById(R.id.edt_contrasena);
        edt_email = findViewById(R.id.edt_email);
        miActividad = this;
        btn_terminos = findViewById(R.id.btn_terminos);
        btn_registro.setEnabled(false);

        Log.e("MD5", Utilidades.md5("sergio"));

        database = UserDatabase.getInstance(this);

        db = FirebaseFirestore.getInstance();


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
/*        Map<String, Object> user = new HashMap<>();
        user.put("nombre", nombre);
        user.put("last", apellido);
        user.put("born", Utilidades.md5(Contraseña));
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("TAG", "Error adding document", e);
                    }
                });*/
/*        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
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

                        //User user = new User(nombre, apellido, Utilidades.md5(contrasena), correo);

                        //long response = database.getUserDao().insertUser(user);

                        Map<String, Object> usuario = new HashMap<>();
                        usuario.put("nombre", nombre);
                        usuario.put("apellido", apellido);
                        usuario.put("contrasena", Utilidades.md5(contrasena));
                        usuario.put("correo", correo);


                        db.collection("usuario").document(correo)
                                .set(usuario)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.e("TAG", "DocumentSnapshot successfully written!");
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e("TAG", "Error writing document", e);
                                    }
                                });


                        //Intent returnIntent = new Intent();
                        //returnIntent.putExtra("USUARIO", edt_email.getText().toString());
                        //setResult(Activity.RESULT_OK, returnIntent);
                        //finish();

                    }
                }

            }
        });
        btn_terminos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent terminos = new Intent(miActividad,TerminosActivity.class);
                startActivity(terminos);
            }

        });
    }

    public void registrar_usuario (String usuario, String contraseña){
        mAuth.createUserWithEmailAndPassword(usuario, contraseña)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();

                            user.sendEmailVerification()
                                    .addOnCompleteListener(miActividad, new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.e("EMAIL", "correo enviado correctamente", task.getException());
                                            } else {
                                                Log.e("EMAIL", "correo enviado con errores", task.getException());
                                                Toast.makeText(miActividad, "USUARIO CREADO", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                            finish();
                        } else {
                            Toast.makeText(miActividad, "Registro incorrecto", Toast.LENGTH_SHORT).show();
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


    @Override
    public void onClick(View v) {

    }
}