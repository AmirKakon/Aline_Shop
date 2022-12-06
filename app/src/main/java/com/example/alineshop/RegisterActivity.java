package com.example.alineshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private EditText m_email, m_password;
    private Button m_registerBtn, m_cancelBtn;
    private TextView m_errorTV;
    private LoginUser m_loginUser;

    private FirebaseAuth fb_auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initialSetup();

        m_registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_loginUser.setUser(m_email.getText().toString(), m_password.getText().toString());

                if(m_loginUser.getUsername().isEmpty() || m_loginUser.getPassword().isEmpty()) {
                    m_errorTV.setText(R.string.empty_credentials);
                }
                else if (m_loginUser.getPassword().length() < 6){
                    m_errorTV.setText(R.string.password_too_short_error);
                }
                else{
                    registerUser(m_loginUser.getUsername(), m_loginUser.getPassword());
                }
            }
        });

        m_cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private void initialSetup() {
        m_email = findViewById(R.id.emailET);
        m_password = findViewById(R.id.passwordET);
        m_registerBtn = findViewById(R.id.registerBtn);
        m_cancelBtn = findViewById(R.id.cancelBtn);
        m_errorTV = findViewById(R.id.errorMessageTV);

        fb_auth = FirebaseAuth.getInstance();
        m_loginUser = new LoginUser();
    }

    private void registerUser(String email, String password) {
        fb_auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(RegisterActivity.this, R.string.registration_complete, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                m_errorTV.setText(e.getLocalizedMessage());
            }
        });
    }
}