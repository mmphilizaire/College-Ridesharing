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

import com.example.fbuapp.Activities.RegisterActivity;
import com.example.fbuapp.R;
import com.example.fbuapp.databinding.FragmentRegisterNameBinding;
import com.parse.ParseUser;

public class RegisterNameFragment extends Fragment {

    private FragmentRegisterNameBinding mBinding;

    private EditText mFirstNameEditText;
    private EditText mLastNameEditText;
    private EditText mUniversityEditText;
    private Button mNextButton;

    public RegisterNameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentRegisterNameBinding.inflate(getLayoutInflater(), container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bind();

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName = mFirstNameEditText.getText().toString();
                String lastName = mLastNameEditText.getText().toString();
                String university = mUniversityEditText.getText().toString();
                RegisterActivity activity = (RegisterActivity) getActivity();
                activity.goToNextFragment(createUser(firstName, lastName, university));
            }
        });
    }

    private ParseUser createUser(String firstName, String lastName, String university) {
        ParseUser user = new ParseUser();
        user.put("firstName", firstName);
        user.put("lastName", lastName);
        user.put("university", university);
        return user;
    }

    private void bind() {
        mFirstNameEditText = mBinding.etFirstName;
        mLastNameEditText = mBinding.etLastName;
        mUniversityEditText = mBinding.etUniversity;
        mNextButton = mBinding.btnNext;
    }
}