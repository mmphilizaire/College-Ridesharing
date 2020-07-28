package com.example.fbuapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fbuapp.Navigation;
import com.example.fbuapp.databinding.ActivityRegisterBinding;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding mBinding;

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
        mBinding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        bind();

        setOnClickListeners();

    }

    private void setOnClickListeners() {
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
                Navigation.goLoginActivity(RegisterActivity.this);
            }
        });
    }

    private void bind() {
        mFirstNameEditText = mBinding.etFirstName;
        mLastNameEditText = mBinding.etLastName;
        mUniversityEditText = mBinding.etUniversity;
        mEmailEditText = mBinding.etEmail;
        mPasswordEditText = mBinding.etPassword;
        mSignUpButton = mBinding.btnRegister;
        mLoginTextView = mBinding.tvLogin;
    }

    private void goMainActivity() {
        Intent main = new Intent(this, MainActivity.class);
        startActivity(main);
        finish();
    }

    private void signUpUser(String firstName, String lastName, String university, String email, String password) {
        if(firstName.equals("") || lastName.equals("") || university.equals("") || email.equals("") || password.equals("")){
            createToast("Missing information!");
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
                            createToast("Email has already been taken. Login!");
                            break;
                        case ParseException.INVALID_EMAIL_ADDRESS:
                            createToast("Invalid email address!");
                            break;
                        default:
                            createToast("Server error!");
                            e.printStackTrace();
                    }
                    return;
                }
                Navigation.goMainActivity(RegisterActivity.this);
            }
        });
    }

    private void createToast(String text){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

}