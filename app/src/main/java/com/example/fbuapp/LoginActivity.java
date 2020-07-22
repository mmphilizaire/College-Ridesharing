package com.example.fbuapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private Button mLoginButton;
    private TextView mSignUpTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(ParseUser.getCurrentUser() != null){
            goMainActivity();
        }

        mEmailEditText = findViewById(R.id.etEmail);
        mPasswordEditText = findViewById(R.id.etPassword);
        mLoginButton = findViewById(R.id.btnLogin);
        mSignUpTextView = findViewById(R.id.tvLogin);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmailEditText.getText().toString();
                String password = mPasswordEditText.getText().toString();
                loginUser(email, password);
            }
        });

        mSignUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goRegisterActivity();
            }
        });

    }

    private void goMainActivity() {
        Intent main = new Intent(this, MainActivity.class);
        startActivity(main);
        finish();
    }

    private void goRegisterActivity() {
        Intent main = new Intent(this, RegisterActivity.class);
        startActivity(main);
        finish();
    }

    private void loginUser(String email, String password) {
        ParseUser.logInInBackground(email, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e != null){
                    switch(e.getCode()){
                        case ParseException.OBJECT_NOT_FOUND:
                            Toast.makeText(LoginActivity.this, "Email or password invalid!", Toast.LENGTH_LONG).show();
                            break;
                        case ParseException.USERNAME_MISSING:
                            Toast.makeText(LoginActivity.this, "Must enter email!", Toast.LENGTH_LONG).show();
                            break;
                        case ParseException.PASSWORD_MISSING:
                            Toast.makeText(LoginActivity.this, "Must enter password!", Toast.LENGTH_LONG).show();
                            break;
                        default:
                            Toast.makeText(LoginActivity.this, "Server error!", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                    }
                    return;
                }
                goMainActivity();
            }
        });
    }
}