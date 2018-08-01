package com.android.simplechat.di.component;

import com.android.simplechat.di.module.AppModule;
import com.android.simplechat.di.module.ChatActivityModule;
import com.android.simplechat.di.module.HomeActivityModule;
import com.android.simplechat.di.module.LoginActivityModule;
import com.android.simplechat.di.scope.ActivityScope;
import com.android.simplechat.view.activites.ChatActivity;
import com.android.simplechat.view.activites.HomeActivity;
import com.android.simplechat.view.activites.LoginActivity;
import com.android.simplechat.view.fragments.LoginFragment;
import com.android.simplechat.view.fragments.UserListFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = {LoginActivityModule.class})
    abstract LoginFragment bindLoginFragment();

    @ContributesAndroidInjector(modules = {LoginActivityModule.class})
    abstract LoginActivity bindLoginActivity();

    @ContributesAndroidInjector(modules = {HomeActivityModule.class})
    abstract HomeActivity bindHomeActivity();

    @ContributesAndroidInjector(modules = {HomeActivityModule.class})
    abstract UserListFragment bindUserListFragment();

    @ActivityScope
    @ContributesAndroidInjector(modules = {ChatActivityModule.class, AppModule.class})
    abstract ChatActivity bindChatActivity();
}