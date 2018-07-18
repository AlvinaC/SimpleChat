package com.android.simplechat.view.activites;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.android.simplechat.BR;
import com.android.simplechat.R;
import com.android.simplechat.databinding.ActivityLoginBinding;
import com.android.simplechat.view.fragments.LoginFragment;
import com.android.simplechat.viewmodel.LoginViewModel;

import javax.inject.Inject;

public class LoginActivity extends BaseActivity<ActivityLoginBinding, LoginViewModel> {

    @Inject
    LoginViewModel mLoginViewModel;
    private ActivityLoginBinding mActivityLoginBinding;

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public LoginViewModel getViewModel() {
        return mLoginViewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityLoginBinding = getViewDataBinding();
        setUpActionBar();
        setUpLogin();

        if (savedInstanceState == null) {
            LoginFragment fragment = new LoginFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment, LoginFragment.TAG).commit();
        }


    }

    private void setUpLogin() {
        mLoginViewModel.getSignInStatus().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isSignedIn) {
                if (isSignedIn != null) {
                    if (isSignedIn) {
                        openMainActivity();
                    }
                }
            }
        });
    }

    private void setUpActionBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setTitle(R.string.login);
    }

    public void openMainActivity() {
        Intent intent = HomeActivity.newIntent(LoginActivity.this);
        startActivity(intent);
        finish();
    }

}
