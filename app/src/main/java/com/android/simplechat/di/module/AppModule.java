package com.android.simplechat.di.module;

import com.android.simplechat.di.scope.ActivityScope;
import com.android.simplechat.rx.AppSchedulerProvider;
import com.android.simplechat.rx.RxBus;
import com.android.simplechat.rx.SchedulerProvider;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    @Provides
    SchedulerProvider provideSchedulerProvider() {
        return new AppSchedulerProvider();
    }

    @ActivityScope
    @Provides
    RxBus provideRxBus() {
        return new RxBus();
    }
}
