package com.example.demogame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.demogame.utils.SoundManager;
import com.example.demogame.utils.UserManager;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin, btnRegister;
    private TextView tvTitle;
    private boolean isLoginMode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeViews();
        setupListeners();

        // Play login sound
        //SoundManager.getInstance().playSound(this, R.raw.login_sound, true);
        SoundManager.getInstance().playBgm(this, R.raw.themesound2, true);
    }

    private void initializeViews() {
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        tvTitle = findViewById(R.id.tvTitle);
    }

    private void setupListeners() {
        btnLogin.setOnClickListener(v -> handleLogin());

        btnRegister.setOnClickListener(v -> {
            if (isLoginMode) {
                switchToRegisterMode();
            } else {
                handleRegister();
            }
        });
    }

    private void handleLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (UserManager.getInstance().login(username, password, this)) {
            Toast.makeText(this, "Welcome, " + username + "!", Toast.LENGTH_SHORT).show();
            //SoundManager.getInstance().stopSound();
            Intent intent = new Intent(LoginActivity.this, BettingActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleRegister() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (username.length() < 3) {
            Toast.makeText(this, "Username must be at least 3 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 4) {
            Toast.makeText(this, "Password must be at least 4 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        if (UserManager.getInstance().register(username, password, this)) {
            Toast.makeText(this, "Registration successful! Starting balance: $10,000", Toast.LENGTH_LONG).show();
            //SoundManager.getInstance().stopSound();
            Intent intent = new Intent(LoginActivity.this, BettingActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show();
        }
    }

    private void switchToRegisterMode() {
        isLoginMode = false;
        tvTitle.setText("Register");
        btnLogin.setVisibility(View.GONE);
        btnRegister.setText("Create Account");

        // Add a button to go back to login
        TextView tvSwitchToLogin = findViewById(R.id.tvSwitchMode);
        tvSwitchToLogin.setText("Already have an account? Login");
        tvSwitchToLogin.setVisibility(View.VISIBLE);
        tvSwitchToLogin.setOnClickListener(v -> switchToLoginMode());
    }

    private void switchToLoginMode() {
        isLoginMode = true;
        tvTitle.setText("Racing Game - Login");
        btnLogin.setVisibility(View.VISIBLE);
        btnRegister.setText("Register");

        TextView tvSwitchToLogin = findViewById(R.id.tvSwitchMode);
        tvSwitchToLogin.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //SoundManager.getInstance().stopSound();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SoundManager.getInstance().playBgm(this, R.raw.themesound2, true);
    }
}
