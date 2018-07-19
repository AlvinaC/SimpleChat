package com.android.simplechat.di.module;

import android.arch.lifecycle.ViewModelProvider;
import android.support.v7.widget.LinearLayoutManager;

import com.android.simplechat.rx.SchedulerProvider;
import com.android.simplechat.utils.ViewModelProviderFactory;
import com.android.simplechat.view.activites.HomeActivity;
import com.android.simplechat.view.adapter.MainPagerAdapter;
import com.android.simplechat.view.adapter.UserListAdapter;
import com.android.simplechat.view.fragments.UserListFragment;
import com.android.simplechat.viewmodel.HomeViewModel;

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

    @Provides
    MainPagerAdapter providesMainPagerAdapter(HomeActivity activity) {
        return new MainPagerAdapter(activity.getSupportFragmentManager());
    }

    @Provides
    LinearLayoutManager provideLinearLayoutManager(UserListFragment fragment) {
        return new LinearLayoutManager(fragment.getActivity());
    }

    @Provides
    UserListAdapter provideUserListAdapter() {
        return new UserListAdapter();
    }
}
