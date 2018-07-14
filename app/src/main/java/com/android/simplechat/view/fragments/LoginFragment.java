package com.android.simplechat.view.fragments;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.android.simplechat.BR;
import com.android.simplechat.R;
import com.android.simplechat.databinding.FragmentLoginBinding;
import com.android.simplechat.utils.AppConstants;
import com.android.simplechat.viewmodel.LoginViewModel;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class LoginFragment extends BaseFragment<FragmentLoginBinding, LoginViewModel> implements HasSupportFragmentInjector {

    public static String TAG = "LoginFragment";

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentDispatchingAndroidInjector;

    @Inject
    ViewModelProvider.Factory mViewModelFactory;

    private LoginViewModel mLoginViewModel;
    private FragmentLoginBinding mFragmentLoginBinding;

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    public LoginViewModel getViewModel() {
        mLoginViewModel = ViewModelProviders.of(this, mViewModelFactory).get(LoginViewModel.class);
        return mLoginViewModel;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFragmentLoginBinding = getViewDataBinding();
        String email = mFragmentLoginBinding.etEmail.getText().toString();
        String password = mFragmentLoginBinding.etPassword.getText().toString();
        if (mLoginViewModel.isEmailAndPasswordValid(email, password)) {
            hideKeyboard();
            mLoginViewModel.signIn(email, password);
        } else {
            Toast.makeText(getBaseActivity(), "", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentDispatchingAndroidInjector;
    }
}
