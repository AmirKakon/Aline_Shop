package com.example.alineshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText m_email;
    private Button m_confirmBtn, m_cancelBtn;
    private TextView m_errorTv;
    private AlertDialog.Builder m_dialogBuilder;

    private LoginUser m_loginUser;

    private FirebaseAuth fb_auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        initialSetup();

        m_confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_loginUser.setUsername(m_email.getText().toString());

                if(m_loginUser.isEmptyUsername()) {
                    m_errorTv.setText(R.string.empty_email_error_message);
                }
                else{
                    fb_auth.sendPasswordResetEmail(m_loginUser.getUsername()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            showPopUpInfoMessage(R.string.dialog_title_email_sent, R.string.dialog_message_email_sent);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            m_errorTv.setText(e.getLocalizedMessage());
                        }
                    });
                }
            }
        });

        m_cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResetPasswordActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private void initialSetup() {
        m_email = findViewById(R.id.emailET);
        m_confirmBtn = findViewById(R.id.registerBtn);
        m_cancelBtn = findViewById(R.id.cancelBtn);
        m_errorTv = findViewById(R.id.errorMessageTV);

        fb_auth = FirebaseAuth.getInstance();
        m_loginUser = new LoginUser();

        m_dialogBuilder = new AlertDialog.Builder(this);

        m_errorTv.setText(R.string.empty_text);
    }

    private void showPopUpInfoMessage(int title, int message) {
        m_dialogBuilder.setTitle(title).setMessage(message).setIcon(R.drawable.information_icon).setCancelable(false).setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        AlertDialog dialog = m_dialogBuilder.create();
        dialog.show();
    }
}