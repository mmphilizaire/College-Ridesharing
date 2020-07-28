package com.example.fbuapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fbuapp.Navigation;
import com.example.fbuapp.databinding.ActivityLoginBinding;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding mBinding;

    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private Button mLoginButton;
    private TextView mSignUpTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        if(ParseUser.getCurrentUser() != null){
            Navigation.goMainActivity(LoginActivity.this);
        }

        bind();

        setOnClickListeners();

    }

    private void bind() {
        mEmailEditText = mBinding.etEmail;
        mPasswordEditText = mBinding.etPassword;
        mLoginButton = mBinding.btnLogin;
        mSignUpTextView = mBinding.tvLogin;
    }

    private void setOnClickListeners(){
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
                Navigation.goRegisterActivity(LoginActivity.this);
            }
        });
    }

    private void loginUser(String email, String password) {
        ParseUser.logInInBackground(email, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e != null){
                    switch(e.getCode()){
                        case ParseException.OBJECT_NOT_FOUND:
                            createToast("Email or password invalid!");
                            break;
                        case ParseException.USERNAME_MISSING:
                            createToast("Must enter email!");
                            break;
                        case ParseException.PASSWORD_MISSING:
                            createToast("Must enter password!");
                            break;
                        default:
                            createToast("Issue logging in!");
                            e.printStackTrace();
                    }
                    return;
                }
                createToast("Success logging in!");
                Navigation.goMainActivity(LoginActivity.this);
            }
        });
    }

    private void createToast(String text){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

}