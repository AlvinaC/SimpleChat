package com.android.simplechat.di.module;

import android.arch.lifecycle.ViewModelProvider;

import com.android.simplechat.rx.SchedulerProvider;
import com.android.simplechat.utils.ViewModelProviderFactory;
import com.android.simplechat.viewmodel.HomeViewModel;
import com.android.simplechat.viewmodel.LoginViewModel;

import dagger.Module;
import dagger.Provides;

@Module
public class HomeActivityModule {

    @Provides
    ViewModelProvider.Factory homeViewModelProvider(HomeViewModel homeViewModel) {
        return new ViewModelProviderFactory<>(homeViewModel);
    }

    @Provides
    HomeViewModel provideHomeViewModel(SchedulerProvider schedulerProvider) {
        return new HomeViewModel(schedulerProvider);
    }
}
