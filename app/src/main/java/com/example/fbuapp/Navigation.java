package com.example.fbuapp;

import android.app.Activity;
import android.content.Intent;

import com.example.fbuapp.Activities.LoginActivity;
import com.example.fbuapp.Activities.MainActivity;
import com.example.fbuapp.SignUp.RegisterActivity;

public class Navigation {

    public static void goMainActivity(Activity activity) {
        Intent main = new Intent(activity, MainActivity.class);
        activity.startActivity(main);
        activity.finish();
    }

    public static void goRegisterActivity(Activity activity) {
        Intent register = new Intent(activity, RegisterActivity.class);
        activity.startActivity(register);
        activity.finish();
    }

    public static void goLoginActivity(Activity activity) {
        Intent login = new Intent(activity, LoginActivity.class);
        activity.startActivity(login);
        activity.finish();
    }

}
