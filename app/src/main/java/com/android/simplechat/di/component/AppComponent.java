package com.android.simplechat.di.component;

import com.android.simplechat.MvvmApp;
import com.android.simplechat.di.module.AppModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import dagger.android.support.AndroidSupportInjectionModule;


@Component(modules = {AndroidSupportInjectionModule.class, AppModule.class, ActivityBuilder.class})
@Singleton
public interface AppComponent extends AndroidInjector<DaggerApplication> {

    void inject(MvvmApp mvvmApp);

    @Override
    void inject(DaggerApplication instance);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(MvvmApp application);

        AppComponent build();
    }
}

