package com.example.fbuapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class RegisterActivity extends AppCompatActivity {

    private EditText mFirstNameEditText;
    private EditText mLastNameEditText;
    private EditText mUniversityEditText;
    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private Button mSignUpButton;
    private TextView mLoginTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirstNameEditText = findViewById(R.id.etFirstName);
        mLastNameEditText = findViewById(R.id.etLastName);
        mUniversityEditText = findViewById(R.id.etUniversity);
        mEmailEditText = findViewById(R.id.etEmail);
        mPasswordEditText = findViewById(R.id.etPassword);
        mSignUpButton = findViewById(R.id.btnRegister);
        mLoginTextView = findViewById(R.id.tvLogin);

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName = mFirstNameEditText.getText().toString();
                String lastName = mLastNameEditText.getText().toString();
                String university = mUniversityEditText.getText().toString();
                String email = mEmailEditText.getText().toString();
                String password = mPasswordEditText.getText().toString();
                signUpUser(firstName, lastName, university, email, password);
            }
        });

        mLoginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goLoginActivity();
            }
        });

    }

    private void goMainActivity() {
        Intent main = new Intent(this, MainActivity.class);
        startActivity(main);
        finish();
    }

    private void goLoginActivity() {
        Intent main = new Intent(this, LoginActivity.class);
        startActivity(main);
        finish();
    }

    private void signUpUser(String firstName, String lastName, String university, String email, String password) {
        if(firstName.equals("")){
            Toast.makeText(this, "Must enter full name!", Toast.LENGTH_LONG).show();
            return;
        }
        if(lastName.equals("")){
            Toast.makeText(this, "Must enter full name!", Toast.LENGTH_LONG).show();
            return;
        }
        if(university.equals("")){
            Toast.makeText(this, "Must enter university!", Toast.LENGTH_LONG).show();
            return;
        }
        if(email.equals("")){
            Toast.makeText(this, "Must enter email!", Toast.LENGTH_LONG).show();
            return;
        }
        if(password.equals("")){
            Toast.makeText(this, "Must enter password!", Toast.LENGTH_LONG).show();
            return;
        }

        ParseUser user = new ParseUser();
        user.setUsername(email);
        user.setPassword(password);
        user.setEmail(email);
        user.put("firstName", firstName);
        user.put("lastName", lastName);
        user.put("university", university);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    switch(e.getCode()){
                        case ParseException.USERNAME_TAKEN:
                            Toast.makeText(RegisterActivity.this, "Email has already been taken. Login!", Toast.LENGTH_LONG).show();
                            break;
                        case ParseException.INVALID_EMAIL_ADDRESS:
                            Toast.makeText(RegisterActivity.this, "Invalid email address!", Toast.LENGTH_LONG).show();
                            break;
                        default:
                            Toast.makeText(RegisterActivity.this, "Server error!", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                    }
                    return;
                }
                goMainActivity();
            }
        });
    }
}