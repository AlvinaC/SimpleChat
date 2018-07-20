package com.android.simplechat.di.module;

import android.arch.lifecycle.ViewModelProvider;

import com.android.simplechat.rx.SchedulerProvider;
import com.android.simplechat.utils.ViewModelProviderFactory;
import com.android.simplechat.viewmodel.LoginViewModel;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class LoginActivityModule {

    @Provides
    ViewModelProvider.Factory loginViewModelProvider(LoginViewModel loginViewModel) {
        return new ViewModelProviderFactory<>(loginViewModel);
    }

    @Provides
    LoginViewModel provideLoginViewModel(SchedulerProvider schedulerProvider) {
        return new LoginViewModel(schedulerProvider);
    }
}
