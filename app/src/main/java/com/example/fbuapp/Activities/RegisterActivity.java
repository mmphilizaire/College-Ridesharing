package com.example.fbuapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.fbuapp.Fragments.RegisterFragment2;
import com.example.fbuapp.Fragments.RegisterFragment3;
import com.example.fbuapp.Fragments.RegisterFragment1;
import com.example.fbuapp.Navigation;
import com.example.fbuapp.R;
import com.example.fbuapp.databinding.ActivityRegisterBinding;
import com.parse.ParseUser;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding mBinding;

    private ParseUser mUser;
    private Fragment mCurrentFragment;
    final FragmentManager mFragmentManager = getSupportFragmentManager();
    private Toolbar mToolbar;
    private ImageView mBackImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mUser = new ParseUser();

        configureToolbar();

        mBackImageView = (ImageView) findViewById(R.id.ivBack);
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLastFragment();
            }
        });

        mCurrentFragment = RegisterFragment1.newInstance(mUser);
        mFragmentManager.beginTransaction().replace(R.id.flContainer, mCurrentFragment).commit();
    }

    private void configureToolbar() {
        mToolbar = mBinding.toolbar;
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.miClose:
                        Navigation.goLoginActivity(RegisterActivity.this);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }


////
//    private void signUpUser(String firstName, String lastName, String university, String email, String password) {
//        if(firstName.equals("") || lastName.equals("") || university.equals("") || email.equals("") || password.equals("")){
//            createToast("Missing information!");
//            return;
//        }
//    }

    public void goToNextFragment(ParseUser user){
        if(mCurrentFragment instanceof RegisterFragment1){
            mCurrentFragment = RegisterFragment2.newInstance(user);
            mBackImageView.setVisibility(View.VISIBLE);
            mFragmentManager.beginTransaction().replace(R.id.flContainer, mCurrentFragment).commit();
        }
        else{
            mCurrentFragment = RegisterFragment3.newInstance(user);
            mFragmentManager.beginTransaction().replace(R.id.flContainer, mCurrentFragment).commit();
        }
    }

    public void goToLastFragment(){
        if(mCurrentFragment instanceof RegisterFragment2){
            mCurrentFragment = RegisterFragment1.newInstance(mUser);
            mBackImageView.setVisibility(View.GONE);
            mFragmentManager.beginTransaction().replace(R.id.flContainer, mCurrentFragment).commit();
        }
        else{
            mCurrentFragment = RegisterFragment2.newInstance(mUser);
            mBackImageView.setVisibility(View.GONE);
            mFragmentManager.beginTransaction().replace(R.id.flContainer, mCurrentFragment).commit();
        }

    }

    private void createToast(String text){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

}