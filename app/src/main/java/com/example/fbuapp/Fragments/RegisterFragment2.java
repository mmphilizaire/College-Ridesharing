package com.example.fbuapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fbuapp.Activities.RegisterActivity;
import com.example.fbuapp.Navigation;
import com.example.fbuapp.databinding.FragmentRegister2Binding;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class RegisterFragment2 extends Fragment {

    private FragmentRegister2Binding mBinding;

    private static final String PARSE_USER = "user";

    private ParseUser mUser;
    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private Button mSignUpButton;

    public RegisterFragment2() {
        // Required empty public constructor
    }

    public static RegisterFragment2 newInstance(ParseUser user) {
        RegisterFragment2 fragment = new RegisterFragment2();
        Bundle args = new Bundle();
        args.putParcelable(PARSE_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mUser = getArguments().getParcelable(PARSE_USER);
        mBinding = FragmentRegister2Binding.inflate(getLayoutInflater(), container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bind();

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmailEditText.getText().toString();
                String password = mPasswordEditText.getText().toString();
                if(email.equals("") || password.equals("")){
                    createToast("Missing information!");
                    return;
                }
                updateUser(email, password);
                RegisterActivity activity = (RegisterActivity) getActivity();
                activity.goToNextFragment(mUser);
            }
        });
    }

    private void updateUser(String email, String password) {
        mUser.setUsername(email);
        mUser.setPassword(password);
        mUser.setEmail(email);
        mUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    switch(e.getCode()){
                        case ParseException.USERNAME_TAKEN:
                            createToast("Email has already been taken!");
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
            }
        });
    }

    private void bind() {
        mEmailEditText = mBinding.etEmail;
        mPasswordEditText = mBinding.etPassword;
        mSignUpButton = mBinding.btnSignUp;
    }

    private void createToast(String text){
        Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
    }

    public ParseUser getUser(){
        return mUser;
    }

}