package com.example.alineshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
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

public class LoginActivity extends AppCompatActivity {
    private EditText m_email, m_password;
    private Button m_loginBtn, m_registerBtn;
    private TextView errorTv, forgotPasswordTv;

    private LoginUser m_loginUser;

    private FirebaseAuth fb_auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseAuth.getInstance().signOut();  //[(6)] add check if user is logged in and automatically jump to main activity.

        initialSetup();
        resetText();

        m_loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_loginUser.setUser(m_email.getText().toString(), m_password.getText().toString());
                loginUser(m_loginUser.getUsername(), m_loginUser.getPassword());
            }
        });

        m_registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorTv.setText(R.string.empty_text);
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        forgotPasswordTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorTv.setText(R.string.empty_text);
                startActivity(new Intent(LoginActivity.this,ResetPasswordActivity.class));
            }
        });
    }

    private void initialSetup() {
        m_email = findViewById(R.id.emailET);
        m_password = findViewById(R.id.passwordET);
        m_loginBtn = findViewById(R.id.loginBtn);
        m_registerBtn = findViewById(R.id.registerBtn);
        errorTv = findViewById(R.id.errorMessageTV);
        forgotPasswordTv = findViewById(R.id.forgotPasswordTV);

        fb_auth = FirebaseAuth.getInstance();
        m_loginUser = new LoginUser();
    }

    private void resetText() {
        errorTv.setText(R.string.empty_text);
        m_email.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
        m_password.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
    }

    private void loginUser(String email, String password) {
        fb_auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(LoginActivity.this, R.string.login_complete, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, ItemListActivity.class));
                //finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                errorTv.setText(e.getLocalizedMessage());
                m_loginUser.resetUser();
                m_email.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                m_password.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
            }
        });
    }
}