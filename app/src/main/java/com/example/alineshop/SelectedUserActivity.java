package com.example.alineshop;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SelectedUserActivity extends AppCompatActivity {

    TextView tvUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_user);

        tvUser = findViewById(R.id.selectedUser);

        ItemModel t = (ItemModel) savedInstanceState.getSerializable("data");

        tvUser.setText(t.getName());
    }
}