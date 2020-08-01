package com.example.fbuapp.SignUp;

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

import com.example.fbuapp.databinding.FragmentRegister1Binding;
import com.parse.ParseUser;

public class RegisterFragment1 extends Fragment {

    private static final String PARSE_USER = "user";

    private FragmentRegister1Binding mBinding;

    private ParseUser mUser;
    private EditText mFirstNameEditText;
    private EditText mLastNameEditText;
    private EditText mUniversityEditText;
    private Button mNextButton;

    public RegisterFragment1() {
        // Required empty public constructor
    }

    public static RegisterFragment1 newInstance(ParseUser user) {
        RegisterFragment1 fragment = new RegisterFragment1();
        Bundle args = new Bundle();
        args.putParcelable(PARSE_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentRegister1Binding.inflate(getLayoutInflater(), container, false);
        mUser = getArguments().getParcelable(PARSE_USER);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bind();
        bindData();

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName = mFirstNameEditText.getText().toString();
                String lastName = mLastNameEditText.getText().toString();
                String university = mUniversityEditText.getText().toString();
                if(firstName.equals("") || lastName.equals("") || university.equals("")){
                    createToast("Missing information!");
                    return;
                }
                RegisterActivity activity = (RegisterActivity) getActivity();
                updateUser(firstName, lastName, university);
                activity.goToNextFragment(mUser);
            }
        });
    }

    private void bindData() {
        String firstName = mUser.getString("firstName");
        String lastName = mUser.getString("lastName");
        String university = mUser.getString("university");
        if(firstName != null){
            mFirstNameEditText.setText(firstName);
        }
        if(lastName != null){
            mLastNameEditText.setText(lastName);
        }
        if(university != null){
            mUniversityEditText.setText(university);
        }
    }

    private void updateUser(String firstName, String lastName, String university) {
        mUser.put("firstName", firstName);
        mUser.put("lastName", lastName);
        mUser.put("university", university);
    }

    private void bind() {
        mFirstNameEditText = mBinding.etFirstName;
        mLastNameEditText = mBinding.etLastName;
        mUniversityEditText = mBinding.etUniversity;
        mNextButton = mBinding.btnNext;
    }

    private void createToast(String text){
        Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
    }


}