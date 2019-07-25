package com.example.cipso.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cipso.R;
import com.example.cipso.core.login.LoginContract;
import com.example.cipso.core.login.LoginPresenter;
import com.example.cipso.ui.activities.RegisterActivity;
import com.example.cipso.ui.activities.UserListingActivity;

public class LoginFragment extends Fragment implements View.OnClickListener, LoginContract.View {
    private LoginPresenter loginPresenter;

    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin, buttonRegister;


    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_login, container, false);
        bindViews(fragmentView);
        return fragmentView;
    }

    private void bindViews(View view) {
        editTextEmail = view.findViewById(R.id.edit_text_email_id);
        editTextPassword = view.findViewById(R.id.edit_text_password);
        buttonLogin = view.findViewById(R.id.button_login);
        buttonRegister = view.findViewById(R.id.button_register);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        loginPresenter = new LoginPresenter(this);

        buttonLogin.setOnClickListener(this);
        buttonRegister.setOnClickListener(this);

        setDummyCredentials();
    }

    private void setDummyCredentials() {
        editTextEmail.setText("test@deneme.com");
        editTextPassword.setText("1234");
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        switch (viewId) {
            case R.id.button_login:
                onLogin(view);
                break;
            case R.id.button_register:
                onRegister(view);
                break;

        }

    }

    // TODO: call register activity
    private void onRegister(View view) {
        // call register activity
        RegisterActivity.startActivity(getActivity());
    }

    private void onLogin(View view) {
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        loginPresenter.login(getActivity(), email, password);
    }

    @Override
    public void onLoginSuccess(String message) {
        Toast.makeText(getActivity(), "Logged in succesfully", Toast.LENGTH_SHORT).show();
        UserListingActivity.startActivity(getActivity(),
                Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    @Override
    public void onLoginFailure(String message) {
        Toast.makeText(getActivity(), "Error: " + message, Toast.LENGTH_SHORT).show();

    }
}
