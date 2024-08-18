package com.example.personalcalendarmanagement;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.personalcalendarmanagement.data.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {
    private Button mbtnLogin;
    private TextView mtxtRegister, mtxtForgetPassword;
    private EditText medtUsername, medtPassword;
    DatabaseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init() {
        medtUsername = findViewById(R.id.edtUsername);
        medtPassword = findViewById(R.id.edtPassword);
        mtxtForgetPassword = findViewById(R.id.txtForgetPassword);
        mtxtRegister = findViewById(R.id.txtRegister);
        mbtnLogin = findViewById(R.id.btnLogin);
        helper = new DatabaseHelper(this);

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String save = sharedPreferences.getString("username", "");
        medtUsername.setText(save);

        mtxtRegister.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        mbtnLogin.setOnClickListener(view -> {
            String username = medtUsername.getText().toString();
            String password = medtPassword.getText().toString();

            Cursor cursor = helper.checkLogin(username, password);
            if (cursor != null && cursor.moveToFirst()) {
                @SuppressLint("Range") int roleId = cursor.getInt(cursor.getColumnIndex("role_id"));
                Intent intent;
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("username", username);
                editor.apply();
                if (roleId == 1) {
                    intent = new Intent(LoginActivity.this, AdminActivity.class);
                } else {
                    intent = new Intent(LoginActivity.this, MainActivity.class);
                }
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, R.string.noti_login_fail, Toast.LENGTH_SHORT).show();
            }
            assert cursor != null;
            cursor.close();
        });

        mtxtForgetPassword.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
            startActivity(intent);
            finish();
        });
    }
}