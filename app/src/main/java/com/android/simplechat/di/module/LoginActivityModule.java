/*
 *  Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      https://mindorks.com/license/apache-v2
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License
 */

package com.android.simplechat.di.module;

import android.arch.lifecycle.ViewModelProvider;

import com.android.simplechat.rx.SchedulerProvider;
import com.android.simplechat.utils.ViewModelProviderFactory;
import com.android.simplechat.viewmodel.LoginViewModel;

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
