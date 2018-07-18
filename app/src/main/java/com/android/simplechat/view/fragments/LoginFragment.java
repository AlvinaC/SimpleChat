package com.android.simplechat.view.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.android.simplechat.BR;
import com.android.simplechat.R;
import com.android.simplechat.databinding.FragmentLoginBinding;
import com.android.simplechat.utils.SnackbarMessage;
import com.android.simplechat.utils.SnackbarUtils;
import com.android.simplechat.viewmodel.LoginViewModel;

import javax.inject.Inject;

public class LoginFragment extends BaseFragment<FragmentLoginBinding, LoginViewModel> {

    public static String TAG = "LoginFragment";

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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpLogin();
        setupSnackbar();
        setUpProgress();
    }

    private void setUpProgress() {
        final ProgressBar progress = getActivity().findViewById(R.id.progress);
        mLoginViewModel.setLoadingStatus(false);
        mLoginViewModel.getLoadingStatus().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isLoading) {
                if (isLoading != null) {
                    if (isLoading) {
                        progress.setVisibility(View.VISIBLE);
                    } else
                        progress.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setUpLogin() {
        final Button login = getActivity().findViewById(R.id.btn_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mFragmentLoginBinding.etEmail.getText().toString();
                String password = mFragmentLoginBinding.etPassword.getText().toString();
                if (mLoginViewModel.isEmailAndPasswordValid(email, password)) {
                    hideKeyboard();
                    mLoginViewModel.performLoginOrAccountCreation(email, password);
                } else {
                    mLoginViewModel.setSnackbarMessage(R.string.message_login_8);
                }
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFragmentLoginBinding = getViewDataBinding();

    }

    private void setupSnackbar() {
        mLoginViewModel.getSnackbarMessage().observe(this, new SnackbarMessage.SnackbarObserver() {
            @Override
            public void onNewMessage(@StringRes int snackbarMessageResourceId) {
                SnackbarUtils.showSnackbar(getView(), getString(snackbarMessageResourceId));
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

}
