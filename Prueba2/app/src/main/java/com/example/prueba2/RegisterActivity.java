package com.example.prueba2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private TextView tv_terms;
    private CheckBox cb_terms;
    private Button btn_signup;
    private EditText et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        tv_terms = findViewById(R.id.tv_terms);
        cb_terms = findViewById(R.id.cb_terms);
        btn_signup = findViewById(R.id.btn_signup);
        et_password = findViewById(R.id.et_password);

        btn_signup.setEnabled(false);

        tv_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, TermsActivity.class);
                startActivity(intent);
            }
        });

/*        cb_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cb_terms.isChecked()){
                    btn_signup.setEnabled(true);
                }else {
                    btn_signup.setEnabled(false);
                }
            }
        });*/
        cb_terms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                btn_signup.setEnabled(isChecked);
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(et_password.getText().toString().length()<8 ||!isValidPassword(et_password.getText().toString())){
                    Toast.makeText(RegisterActivity.this, "Invalid", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(RegisterActivity.this, "Valid", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^}*&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();
    }
}