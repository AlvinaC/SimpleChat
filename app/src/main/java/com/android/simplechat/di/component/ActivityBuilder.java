package com.android.simplechat.di.component;

import com.android.simplechat.di.module.AppModule;
import com.android.simplechat.di.module.LoginActivityModule;
import com.android.simplechat.view.activites.LoginActivity;
import com.android.simplechat.view.fragments.LoginFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = {LoginActivityModule.class})
    abstract LoginFragment bindLoginFragment();


}