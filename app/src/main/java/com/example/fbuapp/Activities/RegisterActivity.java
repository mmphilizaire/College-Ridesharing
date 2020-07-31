package com.example.fbuapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fbuapp.Fragments.RegisterFragment2;
import com.example.fbuapp.Fragments.RegisterFragment3;
import com.example.fbuapp.Fragments.RegisterNameFragment;
import com.example.fbuapp.Navigation;
import com.example.fbuapp.R;
import com.example.fbuapp.databinding.ActivityRegisterBinding;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding mBinding;

    private Fragment mCurrentFragment;
    final FragmentManager mFragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mCurrentFragment = new RegisterNameFragment();

        mFragmentManager.beginTransaction().replace(R.id.flContainer, mCurrentFragment).commit();
    }

//    private void setOnClickListeners() {
//
//        mLoginTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Navigation.goLoginActivity(RegisterActivity.this);
//            }
//        });
//    }
//
//    private void signUpUser(String firstName, String lastName, String university, String email, String password) {
//        if(firstName.equals("") || lastName.equals("") || university.equals("") || email.equals("") || password.equals("")){
//            createToast("Missing information!");
//            return;
//        }
//    }

    public void goToNextFragment(ParseUser user){
        if (mCurrentFragment instanceof RegisterNameFragment){
            mCurrentFragment = RegisterFragment2.newInstance(user);
            mFragmentManager.beginTransaction().replace(R.id.flContainer, mCurrentFragment).commit();
        }
        else{
            mCurrentFragment = RegisterFragment3.newInstance(user);
            mFragmentManager.beginTransaction().replace(R.id.flContainer, mCurrentFragment).commit();
        }
    }

    private void createToast(String text){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

}